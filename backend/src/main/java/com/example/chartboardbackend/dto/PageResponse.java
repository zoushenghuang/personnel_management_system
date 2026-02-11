package com.example.chartboardbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> items;
    private PaginationInfo pagination;
    private PageLinks links;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        private Integer page;
        private Integer pageSize;
        private Long total;
        private Integer totalPages;
        private Boolean hasNext;
        private Boolean hasPrev;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageLinks {
        private String first;
        private String prev;
        private String next;
        private String last;
    }
    
    public static <T> PageResponse<T> of(List<T> items, int page, int pageSize, long total, String baseUrl) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        boolean hasNext = page < totalPages;
        boolean hasPrev = page > 1;
        
        PaginationInfo pagination = new PaginationInfo(
            page, pageSize, total, totalPages, hasNext, hasPrev
        );
        
        PageLinks links = new PageLinks(
            baseUrl + "?page=1&size=" + pageSize,
            hasPrev ? baseUrl + "?page=" + (page - 1) + "&size=" + pageSize : null,
            hasNext ? baseUrl + "?page=" + (page + 1) + "&size=" + pageSize : null,
            baseUrl + "?page=" + totalPages + "&size=" + pageSize
        );
        
        return new PageResponse<>(items, pagination, links);
    }
}

import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Spin, message, Modal, Descriptions, Tag } from 'antd';
import {
  DollarOutlined,
  ShoppingCartOutlined,
  TeamOutlined,
  RiseOutlined,
} from '@ant-design/icons';
import ReactECharts from 'echarts-for-react';
import { getSalesData, getTrafficSources, getVisitData, getProductSales, getTrafficSourceDetail } from '../../services/api';
import type { SalesData, TrafficSource, VisitData, ProductSales } from '../../types';
import './index.less';

const DashboardPage: React.FC = () => {
  const [salesData, setSalesData] = useState<SalesData[]>([]);
  const [trafficData, setTrafficData] = useState<TrafficSource[]>([]);
  const [visitData, setVisitData] = useState<VisitData[]>([]);
  const [productData, setProductData] = useState<ProductSales[]>([]);
  const [loading, setLoading] = useState(true);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [selectedTraffic, setSelectedTraffic] = useState<TrafficSource | null>(null);
  const [detailLoading, setDetailLoading] = useState(false);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        setLoading(true);
        const [sales, traffic, visits, products] = await Promise.all([
          getSalesData(),
          getTrafficSources(),
          getVisitData(),
          getProductSales(),
        ]);
        setSalesData(sales);
        setTrafficData(traffic);
        setVisitData(visits);
        setProductData(products);
      } catch (err) {
        message.error('获取数据失败');
        console.error('Failed to fetch data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchAllData();
  }, []);

  const totalSales = salesData.reduce((sum, item) => sum + item.sales, 0);
  const avgSales = salesData.length > 0 ? totalSales / salesData.length : 0;

  const barOption = {
    title: { text: '月度销售数据', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: salesData.map((item) => item.month) },
    yAxis: { type: 'value' },
    series: [
      {
        data: salesData.map((item) => item.sales),
        type: 'bar',
        itemStyle: { color: '#5470c6' },
        label: { show: true, position: 'top' },
      },
    ],
  };

  const lineOption = {
    title: { text: '日访问量趋势', left: 'center' },
    tooltip: { trigger: 'axis' },
    legend: { data: ['访问量', '注册量'], bottom: 10 },
    xAxis: { type: 'category', data: visitData.map((item) => item.day) },
    yAxis: { type: 'value' },
    series: [
      {
        name: '访问量',
        type: 'line',
        data: visitData.map((item) => item.visits),
        smooth: true,
      },
      {
        name: '注册量',
        type: 'line',
        data: visitData.map((item) => item.registrations),
        smooth: true,
      },
    ],
  };

  // 处理饼图点击事件
  const handlePieClick = async (params: any) => {
    const clickedItem = trafficData.find((item) => item.source === params.name);
    if (clickedItem) {
      setDetailLoading(true);
      setDetailModalVisible(true);
      try {
        const detail = await getTrafficSourceDetail(clickedItem.id);
        setSelectedTraffic(detail);
      } catch (err) {
        message.error('获取详情失败');
        console.error('Failed to fetch detail:', err);
      } finally {
        setDetailLoading(false);
      }
    }
  };

  const pieOption = {
    title: { text: '流量来源分布', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        type: 'pie',
        radius: '60%',
        data: trafficData.map((item) => ({ value: item.value, name: item.source })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  };

  const pieEvents = {
    click: handlePieClick,
  };

  const productOption = {
    title: { text: '产品销售对比', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: productData.map((item) => item.product) },
    yAxis: { type: 'value' },
    series: [
      {
        data: productData.map((item) => item.sales),
        type: 'bar',
        itemStyle: { color: '#91cc75' },
      },
    ],
  };

  return (
    <div className="dashboard-page">
      <Row gutter={16} className="statistics-row">
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="总销售额"
              value={totalSales.toFixed(2)}
              prefix={<DollarOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="平均销售额"
              value={avgSales.toFixed(2)}
              prefix={<ShoppingCartOutlined />}
              valueStyle={{ color: '#667eea' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic title="总用户数" value={15280} prefix={<TeamOutlined />} suffix="人" />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="增长率"
              value={28.5}
              prefix={<RiseOutlined />}
              suffix="%"
              valueStyle={{ color: '#cf1322' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]}>
        <Col xs={24} lg={12}>
          <Card bordered={false} className="chart-card">
            {loading ? (
              <div className="loading-container">
                <Spin size="large" />
              </div>
            ) : (
              <ReactECharts option={barOption} style={{ height: 350 }} />
            )}
          </Card>
        </Col>

        <Col xs={24} lg={12}>
          <Card bordered={false} className="chart-card">
            <ReactECharts option={lineOption} style={{ height: 350 }} />
          </Card>
        </Col>

        <Col xs={24} lg={12}>
          <Card bordered={false} className="chart-card">
            <ReactECharts option={pieOption} style={{ height: 350 }} onEvents={pieEvents} />
          </Card>
        </Col>

        <Col xs={24} lg={12}>
          <Card bordered={false} className="chart-card">
            <ReactECharts option={productOption} style={{ height: 350 }} />
          </Card>
        </Col>
      </Row>

      {/* 流量来源详情Modal */}
      <Modal
        title="流量来源详情"
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={null}
        width={700}
      >
        {detailLoading ? (
          <div style={{ textAlign: 'center', padding: '40px 0' }}>
            <Spin size="large" />
          </div>
        ) : selectedTraffic ? (
          <Descriptions bordered column={2}>
            <Descriptions.Item label="来源名称" span={2}>
              <Tag color="blue" style={{ fontSize: 14 }}>
                {selectedTraffic.source}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="访问量">
              <strong style={{ fontSize: 16, color: '#1890ff' }}>{selectedTraffic.value}</strong>
            </Descriptions.Item>
            <Descriptions.Item label="页面浏览量">
              <strong style={{ fontSize: 16, color: '#52c41a' }}>{selectedTraffic.pageViews || 0}</strong>
            </Descriptions.Item>
            <Descriptions.Item label="转化率">
              <Tag color={selectedTraffic.conversionRate && selectedTraffic.conversionRate > 0.25 ? 'green' : 'orange'}>
                {selectedTraffic.conversionRate ? `${(selectedTraffic.conversionRate * 100).toFixed(1)}%` : '0%'}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="跳出率">
              <Tag color={selectedTraffic.bounceRate && selectedTraffic.bounceRate < 0.5 ? 'green' : 'red'}>
                {selectedTraffic.bounceRate ? `${(selectedTraffic.bounceRate * 100).toFixed(1)}%` : '0%'}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="平均停留时长" span={2}>
              {selectedTraffic.avgDuration ? `${Math.floor(selectedTraffic.avgDuration / 60)}分${selectedTraffic.avgDuration % 60}秒` : '0秒'}
            </Descriptions.Item>
            <Descriptions.Item label="描述" span={2}>
              {selectedTraffic.description || '暂无描述'}
            </Descriptions.Item>
            <Descriptions.Item label="创建时间">
              {selectedTraffic.createdAt ? new Date(selectedTraffic.createdAt).toLocaleString('zh-CN') : '-'}
            </Descriptions.Item>
            <Descriptions.Item label="更新时间">
              {selectedTraffic.updatedAt ? new Date(selectedTraffic.updatedAt).toLocaleString('zh-CN') : '-'}
            </Descriptions.Item>
          </Descriptions>
        ) : null}
      </Modal>
    </div>
  );
};

export default DashboardPage;

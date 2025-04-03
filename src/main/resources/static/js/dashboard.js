// 仪表盘 Vue 实例
new Vue({
    el: '#dashboard',
    data: {
        // 统计数据
        stats: {
            totalRooms: 0,
            availableRooms: 0,
            todayBookings: 0,
            todayCheckins: 0,
            yesterdayTotalRooms: 0,
            yesterdayAvailable: 0,
            yesterdayBookings: 0,
            yesterdayCheckins: 0,
            monthlyBookings: 0,
            monthlyRevenue: 0,
            occupancyRate: 0
        },
        // 趋势数据
        trend: {
            dates: [],
            bookingCounts: [],
            revenues: []
        },
        // 图表实例
        bookingChart: null,
        revenueChart: null
    },
    mounted() {
        // 页面加载完成后获取数据
        this.fetchDashboardStats();
        this.fetchBookingTrend();
        // 每5分钟刷新一次数据
        setInterval(() => {
            this.fetchDashboardStats();
            this.fetchBookingTrend();
        }, 300000);
    },
    methods: {
        // 获取统计数据
        fetchDashboardStats() {
            axios.get('/api/dashboard/stats')
                .then(response => {
                    if (response.data.code === 200) {
                        this.stats = response.data.data;
                    } else {
                        this.$message.error('获取统计数据失败：' + response.data.msg);
                    }
                })
                .catch(error => {
                    console.error('获取统计数据出错：', error);
                    this.$message.error('获取统计数据出错');
                });
        },
        // 获取趋势数据
        fetchBookingTrend(days = 7) {
            axios.get('/api/dashboard/trend', {
                params: { days: days }
            })
                .then(response => {
                    if (response.data.code === 200) {
                        this.trend = response.data.data;
                        this.updateCharts();
                    } else {
                        this.$message.error('获取趋势数据失败：' + response.data.msg);
                    }
                })
                .catch(error => {
                    console.error('获取趋势数据出错：', error);
                    this.$message.error('获取趋势数据出错');
                });
        },
        // 更新图表
        updateCharts() {
            // 预订趋势图表
            if (!this.bookingChart) {
                this.bookingChart = echarts.init(document.getElementById('bookingTrendChart'));
            }
            this.bookingChart.setOption({
                title: {
                    text: '预订趋势'
                },
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    data: this.trend.dates
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: '预订数量',
                    type: 'line',
                    data: this.trend.bookingCounts,
                    smooth: true
                }]
            });

            // 收入趋势图表
            if (!this.revenueChart) {
                this.revenueChart = echarts.init(document.getElementById('revenueTrendChart'));
            }
            this.revenueChart.setOption({
                title: {
                    text: '收入趋势'
                },
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    data: this.trend.dates
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: '收入',
                    type: 'bar',
                    data: this.trend.revenues
                }]
            });
        }
    }
}); 
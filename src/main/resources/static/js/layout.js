// 布局工具
const layoutTools = {
    // 初始化用户信息
    initUserInfo() {
        const userInfo = localStorage.getItem('userInfo');
        if (!userInfo) {
            // 如果没有用户信息，跳转到登录页
            window.location.href = 'login.html';
            return { username: '未登录' };
        }
        return JSON.parse(userInfo);
    },

    // 退出登录
    logout() {
        return new Promise((resolve, reject) => {
            // 清除本地存储
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            
            // 跳转到登录页
            const baseUrl = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1);
            window.location.href = baseUrl + 'login.html';
            resolve();
        });
    }
};

// 导出布局工具
window.layoutTools = layoutTools;

// 统一布局处理函数
function applyLayout(contentId) {
    // 获取内容元素
    const contentElement = document.getElementById(contentId);
    if (!contentElement) return;

    // 创建布局容器
    const layoutContainer = document.createElement('div');
    layoutContainer.className = 'el-container';
    
    // 创建侧边栏
    const aside = document.createElement('div');
    aside.className = 'el-aside';
    aside.innerHTML = `
        <el-menu default-active="2" class="el-menu">
            <el-menu-item index="1" onclick="window.location.href='/pages/index.html'">
                <i class="el-icon-s-home"></i>
                <span slot="title">首页</span>
            </el-menu-item>
            <el-menu-item index="2" onclick="window.location.href='/pages/room.html'">
                <i class="el-icon-s-order"></i>
                <span slot="title">房间管理</span>
            </el-menu-item>
            <el-menu-item index="3" onclick="window.location.href='/pages/customer.html'">
                <i class="el-icon-user"></i>
                <span slot="title">客户管理</span>
            </el-menu-item>
            <el-menu-item index="4" onclick="window.location.href='/pages/booking.html'">
                <i class="el-icon-s-claim"></i>
                <span slot="title">预订管理</span>
            </el-menu-item>
        </el-menu>
    `;

    // 创建主要内容区域
    const main = document.createElement('div');
    main.className = 'el-container is-vertical';

    // 创建顶部导航栏
    const header = document.createElement('div');
    header.className = 'el-header';
    header.innerHTML = `
        <div class="header-left">
            <i class="el-icon-s-fold"></i>
            <span>酒店管理系统</span>
        </div>
        <el-dropdown trigger="click">
            <span class="el-dropdown-link">
                管理员 <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    `;

    // 创建主要内容区域的容器
    const mainContent = document.createElement('div');
    mainContent.className = 'main-content';

    // 移动原始内容到新的布局中
    mainContent.appendChild(contentElement.cloneNode(true));
    main.appendChild(header);
    main.appendChild(mainContent);

    // 组装布局
    layoutContainer.appendChild(aside);
    layoutContainer.appendChild(main);

    // 替换原始内容
    contentElement.parentNode.replaceChild(layoutContainer, contentElement);
}

// 导出函数
window.applyLayout = applyLayout; 
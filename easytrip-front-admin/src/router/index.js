import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "登录",
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/',
      name: 'layout',
      component: () => import('../views/Layout.vue'),
      children: [{
        path: "/home",
        name: "首页",
        component: () => import('../views/home/Home.vue'),
      }, {
        path: "/content/board",
        name: "板块管理",
        component: () => import('../views/content/BoardList.vue'),
      }, 
      {
        path: "/content/sales",
        name: "景点管理",
        component: () => import('../views/content/SalesInfoList.vue'),
      }, 
      {
        path: "/content/line",
        name: "线路管理",
        component: () => import('../views/content/LineInfoList.vue'),
      }, 
      {
        path: "/content/article",
        name: "文章管理",
        component: () => import('../views/content/ArticleInfoList.vue'),
      }, {
        path: "/web/userOrder",
        name: "订单管理",
        component: () => import('../views/web/UserOrder.vue'),
      }, {
        path: "/web/user",
        name: "用户管理",
        component: () => import('../views/web/UserList.vue'),
      },  {
        path: "/web/carouselList",
        name: "轮播图",
        component: () => import('../views/web/CarouselList.vue'),
      }, {
        path: "/web/feedbackList",
        name: "问题反馈",
        component: () => import('../views/web/FeedbackList.vue'),
      }, {
        path: "/setting/menu",
        name: "菜单管理",
        component: () => import('../views/setting/MenuList.vue'),
      }, {
        path: "/setting/role",
        name: "角色管理",
        component: () => import('../views/setting/RoleList.vue'),
      }, {
        path: "/setting/user",
        name: "系统用户",
        component: () => import('../views/setting/UserList.vue'),
      }]
    },
  ]
})

router.beforeEach((to, from, next) => {
  const userInfo = sessionStorage.getItem("userInfo");
  if (!userInfo && to.path != "/login") {
    router.push("/login");
  }
  next();
})

export default router

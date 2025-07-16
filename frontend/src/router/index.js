import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/ais',
      component: () => import('../components/ui/AiGrid.vue'),
    },
    {
      path: '/results',
      component: () => import('../components/ui/ResultGrid.vue'),
    },
    {
      path: '/replicate',
      component: () => import('../components/ui/ReplicateGrid.vue'),
    },
  ],
})

export default router;

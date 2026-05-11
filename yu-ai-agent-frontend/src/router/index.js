import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { applySeoFromRoute } from '../seo/applySeo'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        title: '首页 · 智能体应用',
        description:
          '在 YU AI Agent 选择 AI 恋爱大师或 AI 超级智能体，体验 SSE 流式对话、独立会话与多步推理能力。',
        keywords: 'YU AI Agent,首页,AI 应用,智能体,流式对话,恋爱大师,超级智能体',
      },
    },
    {
      path: '/love-app',
      name: 'love-app',
      component: () => import('../views/LoveAppChatView.vue'),
      meta: {
        title: 'AI 恋爱大师',
        description:
          'AI 恋爱大师：温馨聊天式界面，支持独立会话 ID 与 SSE 流式回复，适合情感倾诉与对话演示。',
        keywords: 'AI 恋爱大师,情感对话,SSE,聊天室,会话记忆,YU AI Agent',
      },
    },
    {
      path: '/manus',
      name: 'manus',
      component: () => import('../views/ManusChatView.vue'),
      meta: {
        title: 'AI 超级智能体',
        description:
          'AI 超级智能体：基于 Manus 流式接口，逐步展示推理与工具调用过程，适合任务型对话与能力展示。',
        keywords: 'AI 超级智能体,Manus,智能体,流式输出,工具调用,YU AI Agent',
      },
    },
  ],
})

router.afterEach((to) => {
  applySeoFromRoute(to)
})

export default router

<template>
  <div class="app-root">
    <header class="app-header">
      <RouterLink to="/" class="brand">YU AI Agent</RouterLink>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
    <footer class="app-footer">
      <div class="app-footer__inner">
        <p class="app-footer__line app-footer__copy">
          © {{ year }} {{ copyrightHolder }} · 保留所有权利
        </p>
        <p class="app-footer__line app-footer__note">
          本站仅供学习、研究与产品演示；请勿用于违法违规场景。
        </p>
        <nav class="app-footer__nav" aria-label="页脚链接">
          <RouterLink to="/" class="app-footer__link">首页</RouterLink>
          <span class="app-footer__sep" aria-hidden="true">|</span>
          <RouterLink to="/love-app" class="app-footer__link">AI 恋爱大师</RouterLink>
          <span class="app-footer__sep" aria-hidden="true">|</span>
          <RouterLink to="/manus" class="app-footer__link">AI 超级智能体</RouterLink>
        </nav>
        <p v-if="icpText" class="app-footer__line app-footer__icp">
          {{ icpText }}
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { SITE_COPYRIGHT_HOLDER } from './config/site'

const year = new Date().getFullYear()
const copyrightHolder = SITE_COPYRIGHT_HOLDER

const icpText = computed(() => String(import.meta.env.VITE_ICP_BEIAN || '').trim())
</script>

<style scoped>
.app-root {
  min-height: 100vh;
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(
    165deg,
    var(--color-bg-gradient-start) 0%,
    var(--color-bg-gradient-mid) 45%,
    var(--color-bg-gradient-end) 100%
  );
  color: var(--color-text-primary);
}

.app-header {
  flex-shrink: 0;
  padding: var(--space-3) var(--space-5);
  min-height: var(--layout-header-h);
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(196, 181, 253, 0.4);
  background: linear-gradient(135deg, #F5F3FF 0%, #EDE9FE 100%);
  box-shadow: 0 1px 4px rgba(139, 92, 246, 0.08);
}

.brand {
  font-weight: 600;
  letter-spacing: 0.02em;
  font-size: var(--font-size-md);
  color: var(--color-text-primary);
  text-decoration: none;
  padding: var(--space-2);
  margin: calc(var(--space-2) * -1);
  border-radius: var(--radius-sm);
  transition: color 0.2s ease;
}

.brand:hover {
  color: var(--color-accent);
}

.app-main {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.app-footer {
  flex-shrink: 0;
  margin-top: auto;
  padding: var(--space-5) var(--space-4) max(var(--space-5), env(safe-area-inset-bottom, 0px));
  border-top: 1px solid rgba(196, 181, 253, 0.35);
  background: linear-gradient(135deg, #F0E7FF 0%, #EDE9FE 100%);
}

.app-footer__inner {
  max-width: 960px;
  margin: 0 auto;
  text-align: center;
}

.app-footer__line {
  margin: 0;
  font-size: var(--font-size-xs);
  line-height: 1.65;
  color: var(--color-text-muted);
}

.app-footer__copy {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}

.app-footer__note {
  margin-top: var(--space-2);
  max-width: 36rem;
  margin-left: auto;
  margin-right: auto;
}

.app-footer__nav {
  margin-top: var(--space-3);
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  gap: var(--space-2) var(--space-3);
}

.app-footer__link {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color 0.2s ease;
}

.app-footer__link:hover {
  color: var(--color-accent);
  text-decoration: underline;
}

.app-footer__sep {
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
  user-select: none;
}

.app-footer__icp {
  margin-top: var(--space-3);
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

@media (max-width: 767px) {
  .app-header {
    padding: var(--space-3) var(--space-4);
  }

  .brand {
    font-size: var(--font-size-base);
  }

  .app-footer {
    padding-left: var(--space-3);
    padding-right: var(--space-3);
  }

  .app-footer__sep {
    display: none;
  }

  .app-footer__nav {
    flex-direction: column;
    gap: var(--space-2);
  }
}

/* 深色模式下页脚背景调整 */
@media (prefers-color-scheme: dark) {
  .app-header {
    background: linear-gradient(135deg, #312e81 0%, #3730a3 100%);
    border-bottom-color: rgba(167, 139, 250, 0.2);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);
  }
  
  .app-footer {
    background: linear-gradient(135deg, #312e81 0%, #3730a3 100%);
    border-top-color: rgba(167, 139, 250, 0.2);
  }
}
</style>

<template>
  <div class="home home--light">
    <div class="home__fx" aria-hidden="true">
      <div class="home__grid" />
      <div class="home__scan" />
      <div class="home__orb home__orb--a" />
      <div class="home__orb home__orb--b" />
      <div class="home__orb home__orb--c" />
      <div class="home__particles" />
      <div class="home__geo home__geo--1" />
      <div class="home__geo home__geo--2" />
      <div class="home__geo home__geo--3" />
    </div>

    <div class="home__inner">
      <section class="hero">
        <p class="hero-kicker">YU AI · NEXT GEN</p>
        <h1 class="hero-title">
          <span class="hero-title__glow">选择应用</span>
        </h1>
        <p class="hero-desc">双引擎智能体 · 流式对话 · 实时响应</p>
      </section>

      <div class="cards">
        <RouterLink class="card card--love" to="/love-app">
          <div class="card__shine" aria-hidden="true" />
          <div class="card-icon" aria-hidden="true">💬</div>
          <h2 class="card-title">AI 恋爱大师</h2>
          <p class="card-text">聊天室风格，SSE 流式对话，独立会话 ID</p>
          <span class="card-cta">进入系统 →</span>
        </RouterLink>

        <RouterLink class="card card--agent" to="/manus">
          <div class="card__shine" aria-hidden="true" />
          <div class="card-icon" aria-hidden="true">🤖</div>
          <h2 class="card-title">AI 超级智能体</h2>
          <p class="card-text">多步推理与工具调用，Manus 流式接口</p>
          <span class="card-cta">进入系统 →</span>
        </RouterLink>
      </div>

      <!-- 页面就绪后动态装配：填充下半屏，增强科技感 -->
      <section
        v-if="deckReady"
        class="home-deck"
        :class="{ 'home-deck--in': deckVisible }"
        aria-label="平台能力展示"
      >
        <header class="home-deck__head">
          <span class="home-deck__pulse" aria-hidden="true" />
          <span class="home-deck__badge">LIVE</span>
          <h2 class="home-deck__title">核心能力矩阵</h2>
          <p class="home-deck__sub">
            页面加载完成后动态装配 · 数据为展示用示意
          </p>
        </header>

        <div class="home-deck__metrics" role="presentation">
          <div
            v-for="(m, i) in metricDefs"
            :key="m.key"
            class="home-deck__metric"
          >
            <div class="home-deck__metric-val" :aria-label="m.label">
              <span v-if="m.prefix" class="home-deck__prefix">{{ m.prefix }}</span>
              <span class="home-deck__num">{{ metricDisplay[i] }}</span>
              <span v-if="m.suffix" class="home-deck__suffix">{{ m.suffix }}</span>
            </div>
            <div class="home-deck__metric-lab">{{ m.label }}</div>
          </div>
        </div>

        <ul class="home-deck__features">
          <li v-for="f in featureItems" :key="f.t" class="home-deck__feat">
            <div class="home-deck__feat-icon" aria-hidden="true" v-html="f.icon" />
            <div class="home-deck__feat-body">
              <h3 class="home-deck__feat-title">{{ f.t }}</h3>
              <p class="home-deck__feat-desc">{{ f.d }}</p>
            </div>
          </li>
        </ul>

        <div class="home-deck__ticker-wrap" aria-hidden="true">
          <div class="home-deck__ticker">
            <span class="home-deck__ticker-track">
              <span v-for="n in 2" :key="n" class="home-deck__ticker-seg">{{ tickerText }}</span>
            </span>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'

const metricDefs = [
  { key: 'engines', label: '双引擎场景', target: 2, decimals: 0, prefix: '', suffix: '' },
  { key: 'sse', label: '流式就绪（示意）', target: 99.9, decimals: 1, prefix: '', suffix: '%' },
  { key: 'lat', label: '切片响应（示意）', target: 50, decimals: 0, prefix: '<', suffix: 'ms' },
  { key: 'steps', label: '智能体步进视图', target: 100, decimals: 0, prefix: '', suffix: '+' },
]

const featureItems = [
  {
    t: '全链路 SSE',
    d: '基于 Fetch 流解析事件帧，逐 token 渲染，低耦合对接 Spring 网关。',
    icon: '<svg viewBox="0 0 24 24" fill="none"><path d="M4 12h16M4 8l3 4-3 4M20 8l-3 4 3 4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>',
  },
  {
    t: '会话与上下文',
    d: '恋爱大师独立 chatId；超级智能体多步气泡，结构清晰、可追溯。',
    icon: '<svg viewBox="0 0 24 24" fill="none"><rect x="4" y="5" width="16" height="14" rx="2" stroke="currentColor" stroke-width="1.5"/><path d="M8 9h8M8 13h5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>',
  },
  {
    t: '工程化交付',
    d: 'Vue 3 + Vite + 响应式布局，浅色弥散主页与双主题聊天室开箱即用。',
    icon: '<svg viewBox="0 0 24 24" fill="none"><path d="M4 19L12 5l8 14H4Z" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/></svg>',
  },
]

const tickerText =
  ' SSE · CHUNK PARSE · ROUTER META · MESH GRADIENT · GLASSMORPHISM · SAFE-AREA · VITE · SPRING API '

const deckReady = ref(false)
const deckVisible = ref(false)
const metricDisplay = ref(metricDefs.map(() => '0'))

function formatMetric(val, decimals) {
  if (decimals <= 0) return String(Math.round(val))
  return val.toFixed(decimals)
}

function easeOutCubic(t) {
  return 1 - (1 - t) ** 3
}

function runMetricCountUp() {
  const duration = 1100
  const start = performance.now()
  const from = metricDefs.map(() => 0)

  function frame(now) {
    const t = Math.min(1, (now - start) / duration)
    const e = easeOutCubic(t)
    metricDisplay.value = metricDefs.map((m, i) =>
      formatMetric(from[i] + (m.target - from[i]) * e, m.decimals),
    )
    if (t < 1) {
      requestAnimationFrame(frame)
    } else {
      metricDisplay.value = metricDefs.map((m) => formatMetric(m.target, m.decimals))
    }
  }
  requestAnimationFrame(frame)
}

onMounted(() => {
  const reduced =
    typeof window !== 'undefined' &&
    window.matchMedia('(prefers-reduced-motion: reduce)').matches
  const delay = reduced ? 80 : 360

  window.setTimeout(() => {
    deckReady.value = true
    nextTick(() => {
      requestAnimationFrame(() => {
        deckVisible.value = true
        if (reduced) {
          metricDisplay.value = metricDefs.map((m) => formatMetric(m.target, m.decimals))
        } else {
          runMetricCountUp()
        }
      })
    })
  }, delay)
})
</script>

<style scoped>
.home--light {
  position: relative;
  flex: 1;
  width: 100%;
  max-width: none;
  margin: 0;
  padding: 0 0 var(--space-10);
  overflow: hidden;
  isolation: isolate;
  background:
    radial-gradient(ellipse 90% 70% at 15% 10%, rgba(147, 197, 253, 0.65), transparent 52%),
    radial-gradient(ellipse 85% 75% at 88% 8%, rgba(196, 181, 253, 0.5), transparent 50%),
    radial-gradient(ellipse 100% 80% at 50% 95%, rgba(253, 186, 240, 0.42), transparent 55%),
    radial-gradient(ellipse 70% 60% at 0% 85%, rgba(186, 230, 253, 0.55), transparent 48%),
    radial-gradient(ellipse 55% 45% at 70% 45%, rgba(224, 242, 254, 0.9), transparent 60%),
    linear-gradient(168deg, #f0f9ff 0%, #e0f2fe 28%, #eef2ff 58%, #fdf4ff 100%);
}

.home__fx {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.home__grid {
  position: absolute;
  inset: -15%;
  background-image:
    linear-gradient(rgba(59, 130, 246, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(59, 130, 246, 0.05) 1px, transparent 1px);
  background-size: 40px 40px;
  transform: perspective(520px) rotateX(56deg);
  transform-origin: center top;
  mask-image: linear-gradient(to bottom, rgba(0, 0, 0, 0.35) 0%, transparent 68%);
  opacity: 0.5;
}

.home__scan {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to bottom,
    transparent 0%,
    rgba(255, 255, 255, 0.35) 50%,
    transparent 52%
  );
  background-size: 100% 240%;
  animation: home-scan 9s linear infinite;
  opacity: 0.35;
}

@keyframes home-scan {
  0% {
    background-position: 0 -40%;
  }
  100% {
    background-position: 0 140%;
  }
}

.home__orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(56px);
  opacity: 0.75;
  animation: home-orb-float 16s ease-in-out infinite;
}

.home__orb--a {
  width: min(48vw, 420px);
  height: min(48vw, 420px);
  top: -12%;
  left: -10%;
  background: radial-gradient(circle, rgba(125, 211, 252, 0.75), transparent 72%);
}

.home__orb--b {
  width: min(42vw, 360px);
  height: min(42vw, 360px);
  bottom: 5%;
  right: -8%;
  background: radial-gradient(circle, rgba(233, 213, 255, 0.75), transparent 70%);
  animation-delay: -4s;
}

.home__orb--c {
  width: min(36vw, 300px);
  height: min(36vw, 300px);
  top: 38%;
  left: 40%;
  background: radial-gradient(circle, rgba(251, 207, 232, 0.65), transparent 70%);
  animation-delay: -7s;
}

@keyframes home-orb-float {
  0%,
  100% {
    transform: translate(0, 0) scale(1);
  }
  50% {
    transform: translate(10px, -14px) scale(1.05);
  }
}

.home__particles {
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(1.2px 1.2px at 12% 22%, rgba(255, 255, 255, 0.85), transparent),
    radial-gradient(1px 1px at 32% 68%, rgba(255, 255, 255, 0.55), transparent),
    radial-gradient(1.1px 1.1px at 78% 28%, rgba(255, 255, 255, 0.65), transparent),
    radial-gradient(1px 1px at 90% 78%, rgba(255, 255, 255, 0.5), transparent);
  background-size: 100% 100%;
  animation: home-drift 24s linear infinite;
  opacity: 0.45;
}

@keyframes home-drift {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(-18px, -14px);
  }
}

.home__geo {
  position: absolute;
  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow:
    0 8px 28px rgba(59, 130, 246, 0.08),
    inset 0 1px 0 rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(8px);
  background: rgba(255, 255, 255, 0.12);
  animation: home-geo-spin 24s linear infinite;
}

.home__geo--1 {
  width: 72px;
  height: 72px;
  top: 18%;
  right: 8%;
  transform: rotate(12deg);
  clip-path: polygon(50% 0%, 100% 38%, 82% 100%, 18% 100%, 0% 38%);
}

.home__geo--2 {
  width: 56px;
  height: 56px;
  bottom: 22%;
  left: 6%;
  border-radius: 12px;
  transform: rotate(-18deg) skewX(-6deg);
  animation-duration: 19s;
  animation-direction: reverse;
  border-color: rgba(244, 114, 182, 0.35);
  box-shadow:
    0 8px 24px rgba(236, 72, 153, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.55);
}

.home__geo--3 {
  width: 44px;
  height: 44px;
  top: 52%;
  right: 18%;
  border-radius: 50%;
  animation-duration: 28s;
  border-color: rgba(129, 140, 248, 0.4);
}

@keyframes home-geo-spin {
  0% {
    transform: rotate(0deg) translateY(0);
  }
  100% {
    transform: rotate(360deg) translateY(-6px);
  }
}

.home__inner {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 1120px;
  margin: 0 auto;
  padding: var(--space-8) var(--space-5) 0;
}

.hero {
  text-align: center;
  margin-bottom: var(--space-8);
  width: 100%;
}

.hero-kicker {
  margin: 0 0 var(--space-3);
  font-size: var(--font-size-xs);
  letter-spacing: 0.32em;
  text-transform: uppercase;
  color: #0369a1;
  font-weight: 600;
}

.hero-title {
  margin: 0 0 var(--space-2);
  font-size: clamp(1.55rem, 4.2vw, 2rem);
  font-weight: 800;
  letter-spacing: 0.04em;
  line-height: var(--line-height-tight);
}

.hero-title__glow {
  background: linear-gradient(115deg, #0c4a6e 0%, #2563eb 28%, #7c3aed 58%, #db2777 92%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.hero-desc {
  margin: 0;
  color: #475569;
  font-size: var(--font-size-md);
  line-height: var(--line-height-body);
  max-width: 28em;
  margin-inline: auto;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: var(--space-5);
  width: 100%;
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: var(--space-5) var(--space-5) var(--space-4);
  border-radius: 22px;
  text-decoration: none;
  color: #0f172a;
  background: rgba(255, 255, 255, 0.42);
  border: 1px solid rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(22px);
  -webkit-backdrop-filter: blur(22px);
  overflow: hidden;
  min-height: 168px;
  box-shadow:
    0 1px 0 rgba(255, 255, 255, 0.9) inset,
    0 18px 40px rgba(15, 23, 42, 0.08);
  transition:
    transform 0.22s ease,
    border-color 0.22s ease,
    box-shadow 0.22s ease;
}

.card__shine {
  position: absolute;
  inset: -40% -60%;
  background: linear-gradient(
    120deg,
    transparent 40%,
    rgba(255, 255, 255, 0.45) 50%,
    transparent 60%
  );
  transform: translateX(-30%);
  transition: transform 0.55s ease;
  pointer-events: none;
}

.card:hover .card__shine {
  transform: translateX(40%);
}

.card--love:hover {
  border-color: rgba(244, 114, 182, 0.55);
  box-shadow:
    0 1px 0 rgba(255, 255, 255, 0.95) inset,
    0 20px 44px rgba(236, 72, 153, 0.18);
}

.card--agent:hover {
  border-color: rgba(56, 189, 248, 0.55);
  box-shadow:
    0 1px 0 rgba(255, 255, 255, 0.95) inset,
    0 20px 44px rgba(14, 165, 233, 0.2);
}

.card:hover {
  transform: translateY(-4px);
}

.card:focus-visible {
  outline-offset: 4px;
}

.card-icon {
  font-size: 1.85rem;
  margin-bottom: var(--space-3);
  line-height: 1;
  filter: drop-shadow(0 2px 8px rgba(59, 130, 246, 0.2));
}

.card-title {
  margin: 0 0 var(--space-2);
  font-size: var(--font-size-lg);
  font-weight: 700;
  letter-spacing: 0.02em;
  color: #0f172a;
}

.card-text {
  margin: 0 0 var(--space-4);
  font-size: var(--font-size-base);
  line-height: var(--line-height-body);
  color: #64748b;
  flex: 1;
}

.card-cta {
  font-size: var(--font-size-sm);
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: #0284c7;
}

.card--love .card-cta {
  color: #db2777;
}

/* —— 下部动态能力甲板（浅色毛玻璃） —— */
.home-deck {
  width: 100%;
  margin-top: var(--space-10);
  padding: var(--space-6) var(--space-5) var(--space-5);
  border-radius: 22px;
  border: 1px solid rgba(255, 255, 255, 0.75);
  background: rgba(255, 255, 255, 0.38);
  backdrop-filter: blur(22px);
  -webkit-backdrop-filter: blur(22px);
  box-shadow:
    0 1px 0 rgba(255, 255, 255, 0.95) inset,
    0 20px 48px rgba(15, 23, 42, 0.08);
  opacity: 0;
  transform: translateY(28px);
  transition:
    opacity 0.65s cubic-bezier(0.22, 1, 0.36, 1),
    transform 0.65s cubic-bezier(0.22, 1, 0.36, 1);
}

.home-deck--in {
  opacity: 1;
  transform: translateY(0);
}

.home-deck__head {
  position: relative;
  text-align: center;
  margin-bottom: var(--space-6);
}

.home-deck__pulse {
  position: absolute;
  top: 0;
  left: 50%;
  width: 140px;
  height: 140px;
  margin-left: -70px;
  margin-top: -40px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(56, 189, 248, 0.2), transparent 70%);
  animation: deck-pulse 3.2s ease-in-out infinite;
  pointer-events: none;
}

@keyframes deck-pulse {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(0.92);
  }
  50% {
    opacity: 0.85;
    transform: scale(1.04);
  }
}

.home-deck__badge {
  display: inline-block;
  padding: 3px 12px;
  border-radius: 999px;
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 0.2em;
  color: #0369a1;
  border: 1px solid rgba(56, 189, 248, 0.45);
  background: rgba(224, 242, 254, 0.75);
  box-shadow: 0 2px 12px rgba(14, 165, 233, 0.12);
}

.home-deck__title {
  margin: var(--space-3) 0 var(--space-2);
  font-size: clamp(1.15rem, 2.8vw, 1.45rem);
  font-weight: 800;
  letter-spacing: 0.1em;
  background: linear-gradient(100deg, #0c4a6e, #2563eb, #7c3aed);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.home-deck__sub {
  margin: 0;
  font-size: var(--font-size-xs);
  color: #64748b;
  letter-spacing: 0.04em;
}

.home-deck__metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-3);
  margin-bottom: var(--space-6);
}

.home-deck__metric {
  text-align: center;
  padding: var(--space-4) var(--space-2);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.55);
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.9) inset;
}

.home-deck__metric-val {
  font-family: ui-monospace, SFMono-Regular, 'Cascadia Code', monospace;
  font-size: clamp(1.2rem, 3.2vw, 1.55rem);
  font-weight: 700;
  color: #0c4a6e;
  line-height: 1.2;
}

.home-deck__prefix,
.home-deck__suffix {
  font-size: 0.75em;
  font-weight: 600;
  color: #0284c7;
  opacity: 0.95;
}

.home-deck__metric-lab {
  margin-top: var(--space-2);
  font-size: var(--font-size-xs);
  color: #64748b;
  line-height: 1.45;
}

.home-deck__features {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-4);
}

.home-deck__feat {
  display: flex;
  gap: var(--space-3);
  padding: var(--space-4);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.65);
  background: rgba(255, 255, 255, 0.48);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.home-deck__feat:hover {
  border-color: rgba(56, 189, 248, 0.45);
  box-shadow: 0 12px 28px rgba(14, 165, 233, 0.1);
}

.home-deck__feat-icon {
  flex-shrink: 0;
  width: 42px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  color: #0369a1;
  background: rgba(224, 242, 254, 0.95);
  border: 1px solid rgba(125, 211, 252, 0.55);
}

.home-deck__feat-icon :deep(svg) {
  width: 22px;
  height: 22px;
}

.home-deck__feat-title {
  margin: 0 0 var(--space-1);
  font-size: var(--font-size-sm);
  font-weight: 700;
  color: #0f172a;
}

.home-deck__feat-desc {
  margin: 0;
  font-size: var(--font-size-xs);
  line-height: 1.55;
  color: #64748b;
}

.home-deck__ticker-wrap {
  margin-top: var(--space-6);
  padding-top: var(--space-4);
  border-top: 1px solid rgba(148, 163, 184, 0.25);
  overflow: hidden;
  mask-image: linear-gradient(90deg, transparent, black 8%, black 92%, transparent);
}

.home-deck__ticker {
  overflow: hidden;
}

.home-deck__ticker-track {
  display: inline-block;
  white-space: nowrap;
  font-family: ui-monospace, SFMono-Regular, monospace;
  font-size: 11px;
  letter-spacing: 0.16em;
  color: #64748b;
  animation: deck-ticker 28s linear infinite;
}

.home-deck__ticker-seg {
  padding-right: 3rem;
}

@keyframes deck-ticker {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

@media (prefers-reduced-motion: reduce) {
  .home__scan,
  .home__particles,
  .home__orb,
  .home__geo {
    animation: none !important;
  }

  .card__shine {
    display: none;
  }

  .home-deck {
    opacity: 1;
    transform: none;
    transition: none;
  }

  .home-deck__pulse,
  .home-deck__ticker-track {
    animation: none !important;
  }
}

@media (min-width: 768px) and (max-width: 1199px) {
  .home__inner {
    max-width: min(1120px, 94vw);
    padding: var(--space-8) var(--space-6) 0;
  }

  .cards {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--space-5);
  }

  .home-deck__metrics {
    grid-template-columns: repeat(2, 1fr);
  }

  .home-deck__features {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 767px) {
  .home__inner {
    padding: var(--space-6) var(--space-4) 0;
  }

  .hero-title {
    letter-spacing: 0.03em;
  }

  .hero-kicker {
    letter-spacing: 0.2em;
  }

  .cards {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }

  .card {
    min-height: auto;
    padding: var(--space-4);
  }

  .home__geo--1 {
    right: 4%;
    opacity: 0.85;
  }

  .home-deck {
    margin-top: var(--space-8);
    padding: var(--space-4) var(--space-3);
  }

  .home-deck__metrics {
    grid-template-columns: repeat(2, 1fr);
  }

  .home-deck__features {
    grid-template-columns: 1fr;
  }
}

@media (min-width: 1200px) {
  .home__inner {
    max-width: 1120px;
  }

  .cards {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

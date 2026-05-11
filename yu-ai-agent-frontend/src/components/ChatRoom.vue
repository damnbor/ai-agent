<template>
  <div :class="rootClass">
    <div v-if="appearance === 'romance'" class="romance-deco" aria-hidden="true">
      <span class="romance-deco__heart romance-deco__heart--1">♥</span>
      <span class="romance-deco__heart romance-deco__heart--2">♥</span>
      <span class="romance-deco__flower romance-deco__flower--1">✿</span>
      <span class="romance-deco__flower romance-deco__flower--2">❀</span>
      <span class="romance-deco__spark">✦</span>
    </div>

    <div v-if="appearance === 'elite'" class="elite-deco" aria-hidden="true">
      <div class="elite-deco__grid" />
      <div class="elite-deco__flow" />
    </div>

    <div class="chat-toolbar">
      <RouterLink to="/" class="back">← 返回</RouterLink>
      <h1 class="title">{{ title }}</h1>
      <div class="toolbar-extra">
        <slot name="toolbar-extra" />
      </div>
    </div>

    <div ref="scrollRef" class="chat-messages">
      <div v-if="!messages.length" class="empty">发送一条消息开始对话</div>
      <div
        v-for="m in messages"
        :key="m.id"
        class="row"
        :class="m.role === 'user' ? 'row-user' : 'row-ai'"
      >
        <div class="msg-block" :class="m.role === 'user' ? 'msg-block--user' : 'msg-block--ai'">
          <ChatAvatar
            v-if="m.role === 'assistant'"
            role="assistant"
            :variant="variant"
            :appearance="appearance"
          />
          <div class="bubble" :class="bubbleUserClass(m)">
            <span class="role-label">{{ m.role === 'user' ? '我' : 'AI' }}</span>
            <div class="bubble-text">{{ m.content }}</div>
          </div>
          <ChatAvatar v-if="m.role === 'user'" role="user" :appearance="appearance" />
        </div>
      </div>
      <div v-if="streaming" class="row row-ai">
        <div class="msg-block msg-block--ai">
          <ChatAvatar role="assistant" :variant="variant" :appearance="appearance" />
          <div class="bubble bubble-ai typing">
            <span class="dot" /><span class="dot" /><span class="dot" />
          </div>
        </div>
      </div>
    </div>

    <form class="chat-input-bar" @submit.prevent="onSubmit">
      <textarea
        v-model="draft"
        class="input"
        rows="1"
        :placeholder="placeholder"
        :disabled="disabled"
        :aria-busy="disabled"
        @keydown="onKeydown"
      />
      <button
        type="submit"
        class="send"
        :class="{ 'send--heartbeat': sendPulse }"
        :disabled="disabled || !draft.trim()"
      >
        发送
      </button>
    </form>
  </div>
</template>

<script setup>
import { computed, nextTick, ref, watch } from 'vue'
import ChatAvatar from './ChatAvatar.vue'

const props = defineProps({
  title: { type: String, required: true },
  messages: { type: Array, required: true },
  streaming: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  placeholder: { type: String, default: '输入消息…' },
  variant: { type: String, default: 'love', validator: (v) => v === 'love' || v === 'manus' },
  /** default | romance（恋爱） | elite（超级智能体） */
  appearance: {
    type: String,
    default: 'default',
    validator: (v) => ['default', 'romance', 'elite'].includes(v),
  },
})

const emit = defineEmits(['send'])

const draft = ref('')
const scrollRef = ref(null)
const sendPulse = ref(false)

const rootClass = computed(() => {
  const base = ['chat-page']
  if (props.appearance !== 'default') base.push(`chat-page--${props.appearance}`)
  return base
})

function bubbleUserClass(m) {
  return m.role === 'user' ? 'bubble-user' : 'bubble-ai'
}

watch(
  () => [props.messages, props.streaming],
  async () => {
    await nextTick()
    const el = scrollRef.value
    if (el) el.scrollTop = el.scrollHeight
  },
  { deep: true },
)

function onKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    onSubmit()
  }
}

function triggerSendPulse() {
  if (props.appearance !== 'romance') return
  sendPulse.value = true
  window.setTimeout(() => {
    sendPulse.value = false
  }, 720)
}

function onSubmit() {
  const text = draft.value.trim()
  if (!text || props.disabled) return
  draft.value = ''
  triggerSendPulse()
  emit('send', text)
}
</script>

<style scoped>
/* ========== 基础（default） ========== */
.chat-page {
  position: relative;
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: var(--layout-max-chat);
  margin: 0 auto;
  padding: var(--space-4) var(--space-4) max(var(--space-5), env(safe-area-inset-bottom, 0px));
}

.chat-toolbar {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
  flex-shrink: 0;
  min-height: var(--touch-target-min);
}

.back {
  color: var(--color-text-secondary);
  text-decoration: none;
  font-size: var(--font-size-sm);
  flex-shrink: 0;
  padding: var(--space-2) var(--space-1);
  margin: calc(var(--space-2) * -1);
  border-radius: var(--radius-sm);
}

.back:hover {
  color: var(--color-accent-hover);
}

.title {
  margin: 0;
  font-size: var(--font-size-lg);
  font-weight: 600;
  flex: 1;
  min-width: 0;
  line-height: var(--line-height-tight);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--color-text-primary);
}

.toolbar-extra {
  flex-shrink: 0;
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  min-width: 0;
}

.chat-messages {
  position: relative;
  flex: 1;
  min-height: 200px;
  overflow-y: auto;
  overflow-x: hidden;
  padding: var(--space-3) var(--space-2);
  border-radius: var(--radius-lg);
  background: var(--color-surface-chat);
  border: 1px solid var(--color-border-subtle);
  -webkit-overflow-scrolling: touch;
}

.empty {
  text-align: center;
  color: var(--color-text-muted);
  padding: var(--space-10) var(--space-4);
  font-size: var(--font-size-sm);
}

.row {
  display: flex;
  margin-bottom: var(--space-3);
}

.row-user {
  justify-content: flex-end;
}

.row-ai {
  justify-content: flex-start;
}

.msg-block {
  display: flex;
  align-items: flex-end;
  gap: var(--space-2);
  max-width: 100%;
  min-width: 0;
}

.msg-block--ai,
.msg-block--user {
  flex-direction: row;
}

.bubble {
  max-width: min(72%, 520px);
  min-width: 0;
  padding: 10px 14px;
  border-radius: var(--radius-lg);
  line-height: var(--line-height-body);
  font-size: var(--font-size-base);
  word-break: break-word;
  text-align: left;
}

.bubble-user {
  background: linear-gradient(
    135deg,
    var(--color-user-bubble-start) 0%,
    var(--color-user-bubble-end) 100%
  );
  color: var(--color-text-on-accent);
  border-bottom-right-radius: var(--radius-sm);
}

.bubble-ai {
  background: rgba(255, 255, 255, 0.07);
  color: var(--color-text-primary);
  border: 1px solid var(--color-border-subtle);
  border-bottom-left-radius: var(--radius-sm);
}

.role-label {
  display: block;
  font-size: var(--font-size-xs);
  opacity: 0.8;
  margin-bottom: var(--space-1);
  text-align: left;
}

.bubble-text {
  white-space: pre-wrap;
  text-align: left;
}

.typing {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  padding: 14px 18px;
}

.dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-text-secondary);
  animation: chat-dots 1.2s infinite ease-in-out;
}

.dot:nth-child(2) {
  animation-delay: 0.15s;
}

.dot:nth-child(3) {
  animation-delay: 0.3s;
}

@keyframes chat-dots {
  0%,
  80%,
  100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  40% {
    transform: translateY(-4px);
    opacity: 1;
  }
}

.chat-input-bar {
  display: flex;
  gap: var(--space-3);
  margin-top: var(--space-3);
  flex-shrink: 0;
  align-items: flex-end;
}

.input {
  flex: 1;
  min-height: var(--touch-target-min);
  max-height: 160px;
  resize: vertical;
  padding: var(--space-3) 14px;
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border-default);
  background: var(--color-surface-input);
  color: var(--color-text-primary);
  outline: none;
}

.input::placeholder {
  color: var(--color-text-muted);
}

.input:focus {
  border-color: var(--color-border-strong);
}

.input:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.send {
  padding: var(--space-3) var(--space-5);
  min-height: var(--touch-target-min);
  border-radius: var(--radius-md);
  border: none;
  cursor: pointer;
  font-weight: 600;
  font-size: var(--font-size-base);
  background: linear-gradient(
    135deg,
    var(--color-send-start) 0%,
    var(--color-send-end) 100%
  );
  color: var(--color-text-on-accent);
}

.send:hover:not(:disabled) {
  filter: brightness(1.06);
}

.send:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

/* ========== 恋爱浪漫主题 ========== */
.romance-deco {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.romance-deco__heart,
.romance-deco__flower,
.romance-deco__spark {
  position: absolute;
  opacity: 0.22;
  user-select: none;
}

.romance-deco__heart {
  color: #fb7185;
  font-size: clamp(1.25rem, 3vw, 1.75rem);
  animation: romance-float 8s ease-in-out infinite;
}

.romance-deco__heart--1 {
  top: 8%;
  left: 4%;
}

.romance-deco__heart--2 {
  bottom: 14%;
  right: 6%;
  animation-delay: -3s;
}

.romance-deco__flower {
  color: #e879f9;
  font-size: clamp(1rem, 2.5vw, 1.35rem);
  animation: romance-float 10s ease-in-out infinite reverse;
}

.romance-deco__flower--1 {
  top: 22%;
  right: 10%;
}

.romance-deco__flower--2 {
  bottom: 28%;
  left: 8%;
  animation-delay: -2s;
}

.romance-deco__spark {
  top: 40%;
  left: 12%;
  color: #fda4af;
  font-size: 0.9rem;
  opacity: 0.35;
  animation: romance-twinkle 3.2s ease-in-out infinite;
}

@keyframes romance-float {
  0%,
  100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-10px) scale(1.08);
  }
}

@keyframes romance-twinkle {
  0%,
  100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.55;
    transform: scale(1.2);
  }
}

.chat-page--romance .chat-toolbar {
  position: relative;
  z-index: 1;
}

.chat-page--romance .chat-input-bar {
  position: relative;
  z-index: 1;
}

.chat-page--romance .title {
  color: #BE185D;
  font-weight: 600;
}

.chat-page--romance .back {
  color: #374151;
}

.chat-page--romance .back:hover {
  color: #BE185D;
}

.chat-page--romance .toolbar-extra,
.chat-page--romance :deep(.session-meta) {
  color: #6B7280;
}

.chat-page--romance .chat-messages {
  z-index: 1;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.chat-page--romance .empty {
  color: #6B7280;
}

.chat-page--romance .bubble-user {
  background: linear-gradient(145deg, #EC4899 0%, #BE185D 100%);
  border-radius: 16px 16px 6px 16px;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #FFFFFF;
}

.chat-page--romance .bubble-ai {
  background: #F3F4F6;
  color: #374151;
  border: 1px solid #E5E7EB;
  border-radius: 16px 16px 16px 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chat-page--romance .role-label {
  color: inherit;
  opacity: 0.85;
}

.chat-page--romance .bubble-ai .role-label {
  color: #6B7280;
}

.chat-page--romance .bubble-user .role-label {
  color: rgba(255, 255, 255, 0.9);
}

.chat-page--romance .typing {
  background: #F3F4F6;
}

.chat-page--romance .dot {
  background: #9CA3AF;
}

.chat-page--romance .input {
  background: #FFFFFF;
  border-color: #D1D5DB;
  color: #374151;
}

.chat-page--romance .input::placeholder {
  color: #9CA3AF;
}

.chat-page--romance .input:focus {
  border-color: #EC4899;
  box-shadow: 0 0 0 3px rgba(236, 72, 153, 0.15);
}

.chat-page--romance .send {
  background: linear-gradient(145deg, #EC4899, #BE185D);
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.35);
}

.chat-page--romance .send--heartbeat {
  animation: send-heartbeat 0.72s ease;
}

@keyframes send-heartbeat {
  0% {
    transform: scale(1);
  }
  20% {
    transform: scale(1.12);
  }
  40% {
    transform: scale(1);
  }
  60% {
    transform: scale(1.08);
  }
  100% {
    transform: scale(1);
  }
}

/* ========== 超级智能体精英主题 ========== */
.elite-deco {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.elite-deco__grid {
  position: absolute;
  inset: -10%;
  background-image:
    linear-gradient(rgba(56, 189, 248, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 163, 184, 0.05) 1px, transparent 1px);
  background-size: 36px 36px;
  animation: elite-grid-move 22s linear infinite;
  opacity: 0.85;
  mask-image: radial-gradient(ellipse 85% 75% at 50% 45%, black 25%, transparent 75%);
}

@keyframes elite-grid-move {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: 36px 36px;
  }
}

.elite-deco__flow {
  position: absolute;
  inset: 0;
  background: 
    linear-gradient(105deg, transparent 0%, rgba(56, 189, 248, 0.08) 35%, rgba(99, 102, 241, 0.1) 50%, rgba(139, 92, 246, 0.06) 65%, transparent 80%),
    linear-gradient(165deg, transparent 0%, rgba(14, 165, 233, 0.05) 40%, transparent 60%);
  background-size: 250% 100%, 200% 100%;
  animation: elite-flow 6s ease-in-out infinite;
}

@keyframes elite-flow {
  0%,
  100% {
    background-position: 0% center, 100% center;
  }
  50% {
    background-position: 100% center, 0% center;
  }
}

.chat-page--elite .chat-toolbar {
  position: relative;
  z-index: 1;
}

.chat-page--elite .chat-input-bar {
  position: relative;
  z-index: 1;
}

.chat-page--elite .title {
  font-weight: 700;
  letter-spacing: 0.04em;
  text-transform: none;
  color: #1D4ED8;
}

.chat-page--elite .back {
  color: #374151;
}

.chat-page--elite .back:hover {
  color: #1D4ED8;
}

.chat-page--elite .toolbar-extra,
.chat-page--elite :deep(.session-meta) {
  color: #6B7280;
}

.chat-page--elite .chat-messages {
  z-index: 1;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.chat-page--elite .empty {
  color: #6B7280;
}

.chat-page--elite .bubble-user {
  background: linear-gradient(145deg, #3B82F6 0%, #1D4ED8 100%);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px 16px 6px 16px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  color: #FFFFFF;
}

.chat-page--elite .bubble-ai {
  background: #F3F4F6;
  color: #374151;
  border: 1px solid #E5E7EB;
  border-radius: 16px 16px 16px 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chat-page--elite .bubble-ai .role-label {
  color: #6B7280;
}

.chat-page--elite .bubble-user .role-label {
  color: rgba(255, 255, 255, 0.9);
}

.chat-page--elite .dot {
  background: #9CA3AF;
}

.chat-page--elite .input {
  background: #FFFFFF;
  border-color: #D1D5DB;
  color: #374151;
  border-radius: 12px;
}

.chat-page--elite .input::placeholder {
  color: #9CA3AF;
}

.chat-page--elite .input:focus {
  border-color: #3B82F6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.chat-page--elite .send {
  background: linear-gradient(135deg, #3B82F6 0%, #1D4ED8 100%);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.35);
}

.chat-page--elite .send:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.chat-page--elite .send:active:not(:disabled) {
  transform: translateY(0);
}

@media (prefers-reduced-motion: reduce) {
  .send--heartbeat,
  .romance-deco__heart,
  .romance-deco__flower,
  .romance-deco__spark,
  .elite-deco__grid,
  .elite-deco__flow {
    animation: none !important;
  }
}

/* —— 平板 —— */
@media (min-width: 768px) and (max-width: 1199px) {
  .chat-page {
    max-width: 720px;
    padding: var(--space-4) var(--space-5) var(--space-5);
  }

  .bubble {
    max-width: min(75%, 480px);
  }
}

/* —— 手机 —— */
@media (max-width: 767px) {
  .chat-page {
    max-width: 100%;
    padding: var(--space-3) var(--space-3) var(--space-4);
  }

  .chat-toolbar {
    flex-wrap: wrap;
    row-gap: var(--space-2);
  }

  .title {
    font-size: var(--font-size-md);
    order: 3;
    flex-basis: 100%;
    width: 100%;
    white-space: normal;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .back {
    order: 1;
  }

  .toolbar-extra {
    order: 2;
    margin-left: auto;
  }

  .bubble {
    max-width: min(calc(100% - 48px), 88vw);
  }

  .chat-input-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .send {
    width: 100%;
  }
}

/* —— 大屏 —— */
@media (min-width: 1200px) {
  .chat-page {
    max-width: var(--layout-max-chat);
  }

  .bubble {
    max-width: min(72%, 560px);
  }
}
</style>

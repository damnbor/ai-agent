<template>
  <div
    class="chat-avatar"
    :class="rootClass"
    aria-hidden="true"
  >
    <svg v-if="role === 'user'" class="chat-avatar__svg" viewBox="0 0 24 24" fill="none">
      <path
        d="M12 11.5a3.25 3.25 0 1 0 0-6.5 3.25 3.25 0 0 0 0 6.5ZM5 20.25c0-3.45 3.13-6.25 7-6.25s7 2.8 7 6.25"
        stroke="currentColor"
        stroke-width="1.75"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
    </svg>
    <svg v-else-if="variant === 'love'" class="chat-avatar__svg" viewBox="0 0 24 24" fill="none">
      <path
        d="M12 20.35l-1.05-.95C6.4 15.1 3 12.12 3 8.5 3 5.42 5.42 3 8.5 3c1.74 0 3.41.81 4.5 2.09A5.98 5.98 0 0 1 15.5 3C18.58 3 21 5.42 21 8.5c0 3.62-3.4 6.6-7.95 11.9L12 20.35Z"
        fill="currentColor"
        opacity="0.95"
      />
    </svg>
    <svg v-else class="chat-avatar__svg" viewBox="0 0 24 24" fill="none">
      <rect x="4" y="4" width="16" height="16" rx="3" stroke="currentColor" stroke-width="1.6" />
      <path d="M9 9h2v2H9V9Zm4 0h2v2h-2V9ZM9 13h6v2H9v-2Z" fill="currentColor" />
    </svg>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  role: { type: String, required: true, validator: (v) => v === 'user' || v === 'assistant' },
  variant: { type: String, default: 'love', validator: (v) => v === 'love' || v === 'manus' },
  appearance: {
    type: String,
    default: 'default',
    validator: (v) => ['default', 'romance', 'elite'].includes(v),
  },
})

const rootClass = computed(() => {
  if (props.role === 'user') {
    if (props.appearance === 'romance') return 'chat-avatar--user chat-avatar--user-romance'
    if (props.appearance === 'elite') return 'chat-avatar--user chat-avatar--user-elite'
    return 'chat-avatar--user'
  }
  if (props.variant === 'manus') {
    if (props.appearance === 'elite') return 'chat-avatar--ai-manus chat-avatar--ai-manus-elite'
    return 'chat-avatar--ai-manus'
  }
  if (props.appearance === 'romance') return 'chat-avatar--ai-love chat-avatar--ai-love-romance'
  return 'chat-avatar--ai-love'
})
</script>

<style scoped>
.chat-avatar {
  flex-shrink: 0;
  width: var(--avatar-size, 40px);
  height: var(--avatar-size, 40px);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-border-default);
  color: var(--color-text-on-accent);
}

.chat-avatar__svg {
  width: 55%;
  height: 55%;
}

.chat-avatar--user {
  background: linear-gradient(
    145deg,
    var(--color-avatar-user-from) 0%,
    var(--color-avatar-user-to) 100%
  );
}

.chat-avatar--user-romance {
  background: linear-gradient(145deg, #fda4af 0%, #e879f9 100%);
  border-color: rgba(255, 255, 255, 0.35);
  box-shadow: 0 4px 14px rgba(236, 72, 153, 0.35);
}

.chat-avatar--user-elite {
  background: linear-gradient(145deg, #475569 0%, #1e293b 100%);
  border-color: rgba(148, 163, 184, 0.45);
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.12),
    0 2px 10px rgba(0, 0, 0, 0.35);
}

.chat-avatar--ai-love {
  background: linear-gradient(
    145deg,
    var(--color-avatar-love-from) 0%,
    var(--color-avatar-love-to) 100%
  );
  border-color: rgba(255, 255, 255, 0.12);
}

.chat-avatar--ai-love-romance {
  background: linear-gradient(145deg, #fb7185 0%, #f472b6 50%, #c084fc 100%);
  border-color: rgba(255, 255, 255, 0.35);
  box-shadow: 0 0 18px rgba(251, 113, 133, 0.45);
}

.chat-avatar--ai-manus {
  background: linear-gradient(
    145deg,
    var(--color-avatar-manus-from) 0%,
    var(--color-avatar-manus-to) 100%
  );
  border-color: rgba(255, 255, 255, 0.12);
}

.chat-avatar--ai-manus-elite {
  background: linear-gradient(145deg, #0c4a6e 0%, #1e3a8a 40%, #312e81 100%);
  border-color: rgba(56, 189, 248, 0.45);
  box-shadow:
    0 0 0 1px rgba(15, 23, 42, 0.6),
    0 0 20px rgba(56, 189, 248, 0.25);
}

@media (max-width: 767px) {
  .chat-avatar {
    --avatar-size: 36px;
  }
}
</style>

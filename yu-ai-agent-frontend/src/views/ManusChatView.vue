<template>
  <div class="elite-shell">
    <ChatRoom
      variant="manus"
      appearance="elite"
      title="AI 超级智能体"
      :messages="messages"
      :streaming="streaming"
      :disabled="streaming"
      placeholder="向智能体描述你的任务…"
      @send="onSend"
    >
      <template #toolbar-extra>
        <span v-if="sessionId" class="session-meta" :title="sessionId">会话 {{ shortId }}</span>
      </template>
    </ChatRoom>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { apiUrl } from '../api/http'
import { consumeSseGet } from '../api/sseChat'

let seq = 0
const nextId = () => `m-${++seq}`

function createSessionId() {
  const a = Date.now().toString(36)
  const b = Math.random().toString(36).slice(2, 12)
  return `${a}${b}`.slice(0, 20)
}

const sessionId = ref('')
const messages = ref([])
const streaming = ref(false)
let abortController = null

const shortId = computed(() => {
  const id = sessionId.value
  if (!id) return ''
  return id.length > 10 ? `${id.slice(0, 8)}…` : id
})

onMounted(() => {
  sessionId.value = createSessionId()
})

onBeforeUnmount(() => {
  abortController?.abort()
})

async function onSend(text) {
  messages.value.push({ id: nextId(), role: 'user', content: text })

  streaming.value = true
  abortController?.abort()
  abortController = new AbortController()

  const url =
    apiUrl('/ai/manus/chat') + '?' + new URLSearchParams({ message: text }).toString()

  try {
    await consumeSseGet(
      url,
      (chunk) => {
        const raw = chunk == null ? '' : String(chunk)
        if (!raw.trim()) return
        messages.value.push({
          id: nextId(),
          role: 'assistant',
          content: raw,
        })
      },
      abortController.signal,
    )
  } catch (e) {
    if (e?.name === 'AbortError') return
    messages.value.push({
      id: nextId(),
      role: 'assistant',
      content: `[错误] ${e?.message || e}`,
    })
  } finally {
    streaming.value = false
  }
}
</script>

<style scoped>
.elite-shell {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  width: 100%;
  background: linear-gradient(180deg, #E0F2FE 0%, #DBEAFE 50%, #EFF6FF 100%);
}
</style>

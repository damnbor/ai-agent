<template>
  <div class="love-shell">
    <ChatRoom
      variant="love"
      appearance="romance"
      title="AI 恋爱大师"
      :messages="messages"
      :streaming="streaming"
      :disabled="streaming"
      placeholder="说说你的想法…"
      @send="onSend"
    >
      <template #toolbar-extra>
        <span v-if="chatId" class="session-meta" :title="chatId">会话 {{ shortId }}</span>
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

const chatId = ref('')
const messages = ref([])
const streaming = ref(false)
let abortController = null

const shortId = computed(() => {
  const id = chatId.value
  if (!id) return ''
  return id.length > 10 ? `${id.slice(0, 8)}…` : id
})

onMounted(() => {
  chatId.value =
    typeof crypto !== 'undefined' && crypto.randomUUID
      ? crypto.randomUUID()
      : `${Date.now()}-${Math.random().toString(36).slice(2, 11)}`
})

onBeforeUnmount(() => {
  abortController?.abort()
})

async function onSend(text) {
  messages.value.push({ id: nextId(), role: 'user', content: text })
  const assistantId = nextId()
  messages.value.push({ id: assistantId, role: 'assistant', content: '' })

  streaming.value = true
  abortController?.abort()
  abortController = new AbortController()

  const url =
    apiUrl('/ai/love_app/chat/sse') +
    '?' +
    new URLSearchParams({ message: text, chatId: chatId.value }).toString()

  try {
    await consumeSseGet(
      url,
      (chunk) => {
        const i = messages.value.findIndex((x) => x.id === assistantId)
        if (i === -1) return
        const cur = messages.value[i]
        messages.value[i] = { ...cur, content: cur.content + chunk }
      },
      abortController.signal,
    )
  } catch (e) {
    if (e?.name === 'AbortError') return
    const i = messages.value.findIndex((x) => x.id === assistantId)
    if (i !== -1) {
      const cur = messages.value[i]
      const suffix = cur.content ? `\n[错误] ${e?.message || e}` : `[错误] ${e?.message || e}`
      messages.value[i] = { ...cur, content: cur.content + suffix }
    }
  } finally {
    streaming.value = false
  }
}
</script>

<style scoped>
.love-shell {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  width: 100%;
  background: linear-gradient(180deg, #E0F2FE 0%, #DBEAFE 50%, #EFF6FF 100%);
}
</style>

function emitSseDataBlock(rawEvent, onChunk) {
  const normalized = rawEvent.replace(/\r\n/g, '\n').replace(/\r/g, '\n')
  const lines = normalized.split('\n')
  const dataLines = []
  for (const line of lines) {
    if (line.startsWith('data:')) {
      dataLines.push(line.slice(5).trimStart())
    }
  }
  if (!dataLines.length) return
  const payload = dataLines.join('\n')
  if (payload && payload !== '[DONE]') onChunk(payload)
}

/**
 * 使用 fetch + ReadableStream 消费 SSE（GET，便于与 Spring 的 text/event-stream 对齐）
 * @param {string} url 完整请求 URL（含 query）
 * @param {(chunk: string) => void} onChunk 每个 SSE 事件中 data 载荷（多行 data: 会按规范拼接）
 * @param {AbortSignal} [signal]
 * @returns {Promise<void>}
 */
export async function consumeSseGet(url, onChunk, signal) {
  const res = await fetch(url, {
    method: 'GET',
    headers: { Accept: 'text/event-stream' },
    signal,
  })

  if (!res.ok) {
    const text = await res.text().catch(() => '')
    throw new Error(text || `请求失败: ${res.status}`)
  }

  const reader = res.body?.getReader()
  if (!reader) {
    throw new Error('响应不支持流式读取')
  }

  const decoder = new TextDecoder()
  let buffer = ''

  while (true) {
    const { done, value } = await reader.read()
    if (done) break
    buffer += decoder.decode(value, { stream: true })

    let idx
    while ((idx = buffer.indexOf('\n\n')) !== -1) {
      const rawEvent = buffer.slice(0, idx)
      buffer = buffer.slice(idx + 2)
      emitSseDataBlock(rawEvent, onChunk)
    }
  }

  const tail = buffer.trim()
  if (tail) emitSseDataBlock(tail, onChunk)
}

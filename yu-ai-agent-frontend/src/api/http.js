import axios from 'axios'

const baseURL =
  import.meta.env.VITE_API_BASE?.replace(/\/$/, '') || 'http://localhost:8123/api'

export const http = axios.create({
  baseURL,
  timeout: 30000,
})

/** @param {string} path 以 / 开头的接口路径，如 /ai/manus/chat */
export function apiUrl(path) {
  const p = path.startsWith('/') ? path : `/${path}`
  if (baseURL.startsWith('http')) {
    return `${baseURL}${p}`
  }
  return `${baseURL}${p}`
}

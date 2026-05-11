/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_BASE?: string
  /** 备案展示文案，如：京ICP备xxxxxxxx号 */
  readonly VITE_ICP_BEIAN?: string
}
interface ImportMeta {
  readonly env: ImportMetaEnv
}

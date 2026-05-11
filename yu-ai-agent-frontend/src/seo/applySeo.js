import { SITE_DESCRIPTION, SITE_KEYWORDS, SITE_NAME } from '../config/site'

/**
 * 设置或创建 <meta name="...">
 * @param {string} name
 * @param {string} content
 */
function setMetaName(name, content) {
  if (content == null || content === '') return
  let el = document.querySelector(`meta[name="${name}"]`)
  if (!el) {
    el = document.createElement('meta')
    el.setAttribute('name', name)
    document.head.appendChild(el)
  }
  el.setAttribute('content', content)
}

/**
 * 设置或创建 <meta property="og:...">
 * @param {string} property
 * @param {string} content
 */
function setMetaProperty(property, content) {
  if (content == null || content === '') return
  let el = document.querySelector(`meta[property="${property}"]`)
  if (!el) {
    el = document.createElement('meta')
    el.setAttribute('property', property)
    document.head.appendChild(el)
  }
  el.setAttribute('content', content)
}

/**
 * 设置或创建 <link rel="canonical">
 * @param {string} href
 */
function setCanonical(href) {
  if (!href) return
  let el = document.querySelector('link[rel="canonical"]')
  if (!el) {
    el = document.createElement('link')
    el.setAttribute('rel', 'canonical')
    document.head.appendChild(el)
  }
  el.setAttribute('href', href)
}

/**
 * 合并路由 matched 上的 meta，并写入 document
 * @param {import('vue-router').RouteLocationNormalizedLoaded} to
 */
export function applySeoFromRoute(to) {
  const merged = {}
  for (const record of to.matched) {
    if (record.meta && typeof record.meta === 'object') {
      Object.assign(merged, record.meta)
    }
  }

  const pageTitle = merged.title || '首页'
  const title =
    typeof pageTitle === 'string' && pageTitle.includes(SITE_NAME)
      ? pageTitle
      : `${pageTitle} | ${SITE_NAME}`

  document.title = title

  const description = merged.description || SITE_DESCRIPTION
  const keywords = merged.keywords || SITE_KEYWORDS

  setMetaName('description', description)
  setMetaName('keywords', keywords)

  const origin = typeof window !== 'undefined' ? window.location.origin : ''
  const path = to.fullPath.split('#')[0] || '/'
  const url = origin ? `${origin}${path}` : path

  setMetaProperty('og:type', merged.ogType || 'website')
  setMetaProperty('og:site_name', merged.ogSiteName || SITE_NAME)
  setMetaProperty('og:title', merged.ogTitle || title)
  setMetaProperty('og:description', merged.ogDescription || description)
  setMetaProperty('og:url', merged.ogUrl || url)
  if (merged.ogImage) {
    setMetaProperty('og:image', merged.ogImage)
  }

  setMetaName('twitter:card', merged.twitterCard || 'summary_large_image')
  setMetaName('twitter:title', merged.twitterTitle || title)
  setMetaName('twitter:description', merged.twitterDescription || description)

  setCanonical(merged.canonical || url)

  if (merged.robots) {
    setMetaName('robots', merged.robots)
  }
}

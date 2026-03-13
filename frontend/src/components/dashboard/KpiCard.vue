<script setup>
defineProps({
  label: {
    type: String,
    required: true
  },
  value: {
    type: String,
    required: true
  },
  hint: {
    type: String,
    required: true
  },
  tone: {
    type: String,
    default: 'neutral'
  }
})
</script>

<template>
  <article
    class="kpi-card"
    :class="`kpi-card--${tone}`"
  >
    <span>{{ label }}</span>
    <strong>{{ value }}</strong>
    <p>{{ hint }}</p>
  </article>
</template>

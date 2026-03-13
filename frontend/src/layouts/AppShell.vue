<script setup>
import { computed } from 'vue'
import SideNav from '@/components/navigation/SideNav.vue'
import TopBar from '@/components/navigation/TopBar.vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

const shellStyle = computed(() => ({
  '--sidebar-width': appStore.sidebarCollapsed ? '110px' : '320px'
}))
</script>

<template>
  <div
    class="shell"
    :data-theme="appStore.accentMode"
    :style="shellStyle"
  >
    <SideNav />
    <div class="shell__main">
      <TopBar />
      <main class="shell__content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

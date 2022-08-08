<template>
  <div class="h5 my-3" v-bind:class="{'text-muted' : muted}">{{ text }}</div>
</template>

<script>
export default {
  name: "TitleSecondary",
  props: ['text', 'muted']
}
</script>

<style scoped>

</style>
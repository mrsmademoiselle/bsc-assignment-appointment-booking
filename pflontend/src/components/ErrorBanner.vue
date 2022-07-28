<template>
  <div v-if="this.message" class="d-flex alert alert-danger justify-content-center">
    {{ message }}
  </div>
</template>

<script>
export default {
  name: "ErrorBanner",
  props: ['message'],
}
</script>

<style scoped>

</style>
<template>
  <div v-if="this.message" class="d-flex alert alert-success justify-content-center">
    {{ message }}
  </div>
</template>

<script>
export default {
  name: "SuccessBanner",
  props: ['message']
}
</script>

<style scoped>

</style>
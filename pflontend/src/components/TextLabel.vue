<template>
  <div class="d-flex justify-content-start container p-2">
    <div><b>{{ label }}:</b></div>
    <div>&nbsp;{{ input }}</div>
  </div>
</template>

<script>

export default {
  name: "TextLabel",
  props: ['label', 'input'],
}
</script>

<style scoped>

</style>
<template>
  <div class="col-auto d-inline-flex text-muted">
    <font-awesome-icon :icon="logo" class="pointer align-center my-1"/>
    <div class="pl-3">
      {{ text }}
    </div>
  </div>
</template>

<script>
export default {
  name: "LogoText",
  props: ['logo', 'text']
}
</script>

<style scoped>

</style>
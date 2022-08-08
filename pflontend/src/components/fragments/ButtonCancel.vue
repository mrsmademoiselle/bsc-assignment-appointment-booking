<template>
  <button class="btn btn-secondary mx-2" type="button" v-on:click="this.$emit('onclick')">{{ title }}</button>
</template>

<script>
export default {
  name: "ButtonCancel",
  props: ['title'],
}
</script>

<style scoped>

</style>
<template>
  <button class="btn btn-secondary mx-2" type="button" v-on:click="this.$emit('onclick')">{{ title }}</button>
</template>

<script>
export default {
  name: "CancelButton",
  props: ['title'],
}
</script>

<style scoped>

</style>
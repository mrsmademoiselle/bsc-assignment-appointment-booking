<template>
  <button class="btn btn-primary btn-login fw-bold" type="button" v-on:click="this.$emit('onclick')">{{
      title
    }}
  </button>
</template>

<script>
export default {
  name: "SubmitButton",
  props: ['title'],
  methods: {}
}
</script>

<style scoped>

</style>
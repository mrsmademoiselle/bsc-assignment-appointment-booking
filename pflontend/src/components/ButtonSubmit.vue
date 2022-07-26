<template>
  <button class="btn btn-primary btn-login fw-bold" type="button" v-on:click="this.$emit('onclick')">{{
      title
    }}
  </button>
</template>

<script>
export default {
  name: "ButtonSubmit",
  props: ['title'],
  methods: {}
}
</script>

<style scoped>

</style>
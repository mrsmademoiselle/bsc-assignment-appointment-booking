<template>
  <button :class="(danger ? 'btn-danger' : 'btn-primary') " class="btn btn-login fw-bold" type="button"
          v-on:click="this.$emit('onclick')">{{
      title
    }}
  </button>
</template>

<script>
export default {
  name: "ButtonSubmit",
  props: ['title', 'danger'],
  methods: {}
}
</script>

<style scoped>

</style>
<template>
  <div>I am login</div>
  <div>Dispatch Username and Password:</div>
  <button v-on:click="callLogin">Dispatch</button>
</template>

<script>

export default {
  name: "LoginView",
  data: function () {
    return {
      username: "admin",
      password: "adminPass",

      error: false,
      errors: []
    }
  },
  methods: {
    callLogin() {
      this.errors = [];
      this.$store.dispatch('login', {username: this.username, password: this.password})
          .then(() => {
            this.$router.push('/')
          })
          .catch((e) => {
            this.errors.push(e);
            this.error = true
          })
    }
  }
}
</script>

<style scoped>

</style>
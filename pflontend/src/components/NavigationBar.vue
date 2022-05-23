<template>
  <nav class="navbar navbar-light bg-light">
    <div class="container-fluid">
      <router-link class="navbar-link" to="/">Logo</router-link>
      <div class="d-flex">
        <router-link class="navbar-link" to="/book-appointment">Termin buchen</router-link>
        <router-link class="navbar-link" to="/login">Login</router-link>
      </div>
      <div>{{ msg }}</div>
    </div>

  </nav>
</template>

<script>

import myApi from "@/services/myApi";

export default {
  name: "NavigationBar",
  data: function () {
    return {
      msg: ""
    }
  },
  methods: {

    getInitialData() {
      myApi.get("example/get").then(response => this.msg = response.data)
    }
  }, beforeMount: function () {
    this.getInitialData()
  },
}
</script>

<style scoped>

</style>
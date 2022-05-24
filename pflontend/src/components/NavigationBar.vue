<template>
  <nav class="navbar navbar-light bg-light">
    <div class="container-fluid">
      <router-link class="navbar-link" to="/">
        VLH
      </router-link>
      <div class="d-flex">
        <router-link class="navbar-link" to="/book-appointment">Termin buchen</router-link>
        <router-link class="navbar-link" to="/login">Login</router-link>
      </div>
    </div>

  </nav>
</template>

<script>

export default {
  name: "NavigationBar",
}
</script>

<style scoped>

</style>
<template>
  <nav class="navbar navbar-light border">
    <div class="container-fluid">
      <router-link class="navbar-link" to="/">
        VLH
      </router-link>
      <div v-if="isLoggedIn" class="d-flex">
        <router-link class="navbar-link" to="/">Termin buchen</router-link>
        <router-link class="navbar-link" to="/uebersicht">Terminübersicht</router-link>
        <router-link class="navbar-link" to="/beratungsstellen">Beratungsstellen</router-link>
        <router-link class="navbar-link" to="/einstellungen">Meine Präferenzen</router-link>

        <a class="navbar-link" v-on:click="logout">Logout</a>
      </div>
      <div v-else class="d-flex">
        <router-link class="navbar-link" to="/">Termin buchen</router-link>
        <router-link class="navbar-link" to="/login">Login</router-link>
      </div>

    </div>
  </nav>
</template>

<script>

export default {
  name: "NavigationBar",
  methods: {
    logout() {
      this.$store.commit('logout');
      this.$router.push('/login')
    }
  },
  computed: {
    isLoggedIn: {
      get() {
        let token = this.$store.getters.token;
        return token !== null && token.length > 0;
      }
    }
  }
}
</script>

<style scoped>

</style>
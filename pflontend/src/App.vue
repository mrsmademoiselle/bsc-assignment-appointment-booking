<template>
  <NavigationBar></NavigationBar>
  <router-view></router-view>

</template>

<script>

import NavigationBar from "@/components/NavigationBar";

export default {
  name: 'App',
  components: {
    NavigationBar: NavigationBar
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #f0f0f0;
  text-decoration: none;
  height: 105vh;
}

.navbar-link {
  text-decoration: none;
  color: inherit;
  margin: 10px 20px 10px 20px;
  cursor: pointer;
}

.navbar-link:hover {
  text-decoration: none;
  color: inherit;

  border-bottom: solid #5fb75f;
  margin: 10px 20px 10px 20px;
  cursor: pointer;
}

.router-link-active {
  border-bottom: solid darkgreen;
}

</style>

<template>
  <NavigationBar></NavigationBar>
  <router-view></router-view>

</template>

<script>

import NavigationBar from "@/components/compositions/NavigationBar";

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
  background-color: white;
  text-decoration: none;
  height: 105vh;
}

.navbar-link {
  text-decoration: none;
  color: inherit;
  margin: 10px 20px 10px 20px;
  cursor: pointer;
  border-bottom: solid transparent;
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

.tab-active {
  color: #27718c;
  font-weight: bold;
  border-bottom: solid #27718c;
}

</style>

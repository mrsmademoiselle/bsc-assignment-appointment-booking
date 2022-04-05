<template>
  <TopBar></TopBar>
  <div class="hello">
    <h1>Hi, {{ msg }}</h1>
  </div>
</template>

<script>
import TopBar from "@/components/TopBar";
import myApi from "@/myApi";

export default {
  name: 'HelloWorld',
  components: {TopBar},
  data: function () {
    return {
      msg: ""
    }
  },
  methods: {
    getApiReply() {
      myApi.get("example/get").then(response => this.msg = response.data)
    }
  },
  beforeMount: function () {
    this.getApiReply()
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>

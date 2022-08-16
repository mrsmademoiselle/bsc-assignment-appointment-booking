<template>
  <div class="mt-4">
    <BeratungsstellenEinstellungen></BeratungsstellenEinstellungen>
  </div>
</template>

<script>
import BeratungsstellenEinstellungen from "@/components/compositions/BeratungsstellenEinstellungen";

export default {
  name: "BeratungsstellenVerwaltungView",
  components: {BeratungsstellenEinstellungen},
  methods: {
    async getApiInformation() {
      await this.$store.dispatch("fetchBeratungsstellen");
    },
  },
  beforeMount: function () {
    this.getApiInformation();
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <div>{{ rMsg }}</div>
  </div>
</template>

<script>
import {TerminService} from "@/services/TerminService";

export default {
  name: "TestingRandomShit",
  data: function () {
    return {
      rMsg: ""
    }
  },
  methods: {
    async callApi() {
      this.rMsg = await TerminService.getAllAppointments();
    },
  }, beforeMount: function () {
    this.callApi();
  },
}
</script>

<style scoped>

</style>
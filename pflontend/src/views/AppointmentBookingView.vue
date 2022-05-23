<template>
  <div>Bidde Daten eingeben damge</div>
  <section class="m-5 justify-content-center">
    <section class="col-6 m-auto p-4 bg-white">
      <div class="row justify-content-center">
        <label class="col-3" for="datum">Datum: </label>
        <input id="datum" class="col-3" type="date"/>
      </div>
      <div class="row justify-content-center">
        <label class="col-3" for="vorname">Vorname:</label>
        <input id="vorname" class="col-3" type="text"/>
      </div>
      <div class="row justify-content-center">
        <label class="col-3" for="nachname">Nachname:</label>
        <input id="nachname" class="col-3" type="text"/>
      </div>
      <div class="row justify-content-center">
        <label class="col-3" for="email">Email:</label>
        <input id="email" class="col-3" type="email"/>
      </div>
      <div class="mb-2 row justify-content-center">
        <label class="col-3" for="telefon">Telefon:</label>
        <input id="telefon" class="col-3" type="text"/>
      </div>
      <button class="m-2 btn btn-primary" @click="this.sendData">Abschicken</button>
    </section>
    <div>{{ responseMessage }}</div>
  </section>
</template>

<script>
import exampleTerminbuchung from "../assets/exampleData.json";
import myApi from "@/services/myApi";

export default {
  name: "AppointmentBookingView",
  data: function () {
    return {
      responseMessage: "No response yet."
    }
  },
  methods: {
    collectData() {
      return exampleTerminbuchung;
    },
    sendData() {
      myApi.post("example/post", this.collectData()).then(response => this.responseMessage = response.data)
    }
  }
}
</script>

<style scoped>

</style>
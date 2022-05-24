<template>
  <div v-if="appointment != ''" class="mt-4">
    <div class="h2 m-4">Terminabsage</div>
    <div class="my-4">Sind Sie sicher, dass Sie Ihren Termin vom</div>
    <div class="h4">{{ appointment.formatDateToGermanLocale() }} um {{ appointment.uhrzeit }} Uhr</div>
    <div class="h4">{{ appointment.beratungsstelle.id }}</div>
    <div class="mt-4"> absagen möchten?</div>
    <div class="my-4 justify-content-center d-flex">
      <button class="btn btn-secondary col-2 mx-2" v-on:click="$router.push('/')">Nein, abbrechen</button>
      <button class="btn btn-primary col-2 mx-2" v-on:click="cancelAppointment">Ja, absagen</button>
    </div>
  </div>
  <div v-if="abgesagt">Der Termin wurde erfolgreich abgesagt.</div>
</template>

<script>
import {TerminService} from "@/services/TerminService";

export default {
  name: "AppointmentCancellation",
  data: function () {
    return {
      appointment: "",
      abgesagt: false,
      token: ""
    }
  },
  methods: {
    async cancelAppointment() {
      try {
        await TerminService.cancelAppointment(this.appointment.id);
        this.abgesagt = true;
      } catch (e) {
        console.log(e);
      }

    },
    async getAppointmentFromServer() {
      let path = window.location.pathname;
      this.token = path.split('/').pop();

      try {
        this.appointment = await TerminService.getAppointmentForCancellationToken(this.token);
      } catch (e) {
        // TODO redirect zur Startseite mit Fehlermeldung
        await this.$router.push("/");
      }
    }
  },
  beforeMount: function () {
    this.getAppointmentFromServer();
  },
}
</script>

<style scoped>

</style>
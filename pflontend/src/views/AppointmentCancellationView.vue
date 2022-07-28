<template>
  <div v-if="appointment !== null" class="mt-4">
    <div class="h2 m-4">Terminabsage</div>
    <div class="mt-3 mb-4">Sind Sie sicher, dass Sie Ihren Termin am</div>
    <div class="h5 font-weight-bold">{{ appointment.formatDateToGermanLocale() }} um {{ appointment.uhrzeit }} Uhr</div>
    <div class="my-4">in der Beratungsstelle</div>
    <div class="h5 mt-4 font-weight-bold">{{ appointment.beratungsstelle.ansprechpartner }}</div>
    <div class="h5 font-weight-bold">{{ appointment.beratungsstelle.strasse }}
      {{ appointment.beratungsstelle.hausnummer }}
    </div>
    <div class="h5 font-weight-bold">{{ appointment.beratungsstelle.plz }} {{ appointment.beratungsstelle.ort }}</div>
    <div class="mt-4 pb-4"> absagen möchten?</div>
    <div v-if="!abgesagt" class="my-4 justify-content-center d-flex">
      <ButtonCancel title="Nein, abbrechen" @onclick="$router.push('/')"></ButtonCancel>
      <ButtonSubmit title="Ja, absagen" @onclick="cancelAppointment"></ButtonSubmit>
    </div>
    <div v-else>Der Termin wurde erfolgreich abgesagt.</div>
  </div>
</template>

<script>
import {TerminService} from "@/services/TerminService";
import ButtonCancel from "@/components/buttons/ButtonCancel";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";

export default {
  name: "AppointmentCancellation",
  components: {ButtonSubmit, ButtonCancel},
  data: function () {
    return {
      appointment: null,
      abgesagt: false,
      token: ""
    }
  },
  methods: {
    cancelAppointment() {
      try {
        TerminService.cancelAppointment(this.appointment.id);
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
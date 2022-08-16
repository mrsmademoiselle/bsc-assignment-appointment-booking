<template>
  <div v-if="appointment !== null" class="mt-4">
    <div class="h2 m-4">Terminabsage</div>
    <div class="mt-3 mb-4">Sind Sie sicher, dass Sie Ihren Termin am</div>
    <div class="h5 font-weight-bold">{{ appointment.formatDateToGermanLocale() }} um {{ appointment.uhrzeit }} Uhr</div>
    <div class="my-4">in der Beratungsstelle</div>
    <div class="h5 mt-4 font-weight-bold">{{
        appointment.beratungsstelle.formatToReadableString()
      }}
    </div>
    <div class="h5 font-weight-bold">{{ appointment.beratungsstelle.strasse }}
      {{ appointment.beratungsstelle.hausnummer }}
    </div>
    <div class="h5 font-weight-bold">{{ appointment.beratungsstelle.plz }} {{ appointment.beratungsstelle.ort }}</div>
    <div class="mt-4 pb-4"> absagen möchten?</div>
    <div v-if="!abgesagt" class="my-4 justify-content-center d-flex">
      <ButtonCancel title="Nein, abbrechen" @onclick="$router.push('/')"></ButtonCancel>
      <ButtonSubmit data-target="#myModal" data-toggle="modal" title="Ja, absagen"></ButtonSubmit>
    </div>
    <SuccessBanner v-for="successMessage in success" v-else :key="successMessage"
                   :message="successMessage"></SuccessBanner>
  </div>
  <!-- Modal -->
  <div v-if="appointment !== null" id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header justify-content-center">
          <h4 class="modal-title">Termin absagen</h4>
        </div>
        <div class="modal-body">
          <div>Möchten Sie diesen Termin wirklich absagen?</div>
          <div class="my-4">{{ appointment.formatDateToGermanLocale() }} {{
              appointment.uhrzeit
            }} Uhr
          </div>
          <div>{{ appointment.vorname }} {{ appointment.nachname }}</div>
          <div class="mt-4">Sie werden per E-Mail über die die Terminabsage benachrichtigt.</div>
          <div>Diese Aktion kann nicht rückgängig gemacht werden.</div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-dismiss="modal" type="button">Nein, abbrechen</button>
          <button class="btn btn-danger" data-dismiss="modal" type="button" v-on:click="cancelAppointment">Ja,
            absagen
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import {TerminService} from "@/services/TerminService";
import ButtonCancel from "@/components/fragments/ButtonCancel";
import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import SuccessBanner from "@/components/fragments/SuccessBanner";

export default {
  name: "TerminabsageView",
  components: {ButtonSubmit, ButtonCancel, SuccessBanner},
  data: function () {
    return {
      appointment: null,
      abgesagt: false,
      token: "",
      success: []
    }
  },
  methods: {
    cancelAppointment() {
      try {
        TerminService.cancelAppointment(this.appointment.id);
        this.abgesagt = true;
        this.success.push('Der Termin wurde erfolgreich abgesagt.')
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
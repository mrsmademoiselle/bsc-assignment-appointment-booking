<template>
  <div class="h2 m-4">Terminübersicht</div>
  <div class="justify-content-center d-flex mt-3">
    <table v-if="allAppointments.length > 0" class="col-lg-8 table">
      <thead>
      <tr>
        <th scope="col">Name</th>
        <th scope="col">Termin</th>
        <th scope="col">Email</th>
        <th scope="col">Details</th>
        <th scope="col">Absagen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="appointment in allAppointments" :key="appointment.id">
        <td>{{ appointment.vorname }} {{ appointment.nachname }}</td>
        <td>{{ appointment.formatDateToGermanLocale() }} {{ appointment.uhrzeit }} Uhr</td>
        <td>{{ appointment.email }}</td>
        <td>
          <router-link to="/appointment/{{appointment.id}}">zum Termin</router-link>
        </td>
        <td>
          <ButtonSubmit danger="true" data-target="#myModal" data-toggle="modal"
                        title="x" type="button" @onclick="selectAppointment(appointment)">
          </ButtonSubmit>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-if="allAppointments.length <= 0" class="col-lg-6">Derzeit gibt es keine Termine.</div>
  </div>

  <!-- Modal -->
  <div v-if="selectedAppointment !== null" id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header justify-content-center">
          <h4 class="modal-title">Termin absagen</h4>
        </div>
        <div class="modal-body">
          <div>Möchten Sie diesen Termin wirklich absagen?</div>
          <div class="my-4">{{ selectedAppointment.formatDateToGermanLocale() }} {{ selectedAppointment.uhrzeit }} Uhr
          </div>
          <div>{{ selectedAppointment.vorname }} {{ selectedAppointment.nachname }}</div>
          <div class="mt-4">Der Kunde wird über die Terminabsage per E-Mail benachrichtigt.</div>
          <div>Diese Aktion kann nicht rückgängig gemacht werden.</div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-dismiss="modal" type="button">Abbrechen</button>
          <button class="btn btn-danger" data-dismiss="modal" type="button" v-on:click="cancelAppointment">Ja, absagen
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>

import ButtonSubmit from "@/components/buttons/ButtonSubmit";

export default {
  name: "AppointmentOverview",
  components: {ButtonSubmit},
  data: function () {
    return {
      selectedAppointment: null
    }
  },
  methods: {
    selectAppointment(appointment) {
      this.selectedAppointment = appointment;
    },
    cancelAppointment() {
      this.$store.dispatch('removeAppointment', {id: this.selectedAppointment.id, token: this.$store.getters.token});
    }
  },
  computed: {
    allAppointments: {
      get() {
        return this.$store.getters.allAppointments;
      }
    }
  },
  /* Nur im Fall der Fälle, dass die Daten vor dem Weiterleiten aus irgendeinem Grund nicht geladen wurden */
  beforeMount: function () {
    this.$store.dispatch('fetchAppointments', this.$store.getters.token)
  },
  errorCaptured: function (err) {
    console.log(err)
    this.$router.push("/")
  }
}
</script>

<style scoped>

</style>
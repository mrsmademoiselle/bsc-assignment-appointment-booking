<template>
  <div class="d-flex justify-content-end border-1 my-2 border-top p-2">
    <div :title="'Zur '+(this.kalenderansicht ? 'Listen':'Kalender')+'ansicht wechseln'"
         class="tab col-1 tab-active"
         v-on:click="this.kalenderansicht = !this.kalenderansicht">
      <font-awesome-icon :icon="'fa-'+(this.kalenderansicht?'list':'calendar')" class="pointer"/>
    </div>
  </div>
  <div>

    <AppointmentCalendar v-if="kalenderansicht" :abwesenheiten="alleAbwesenheiten"
                         :termine="allAppointments" class="mb-2"></AppointmentCalendar>

    <div v-else class="my-3 ">
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
              <router-link :to="'/appointment/'+appointment.id">zum Termin</router-link>
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
              <div class="my-4">{{ selectedAppointment.formatDateToGermanLocale() }} {{
                  selectedAppointment.uhrzeit
                }} Uhr
              </div>
              <div>{{ selectedAppointment.vorname }} {{ selectedAppointment.nachname }}</div>
              <div class="mt-4">Der Kunde wird über die Terminabsage per E-Mail benachrichtigt.</div>
              <div>Diese Aktion kann nicht rückgängig gemacht werden.</div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" data-dismiss="modal" type="button">Abbrechen</button>
              <button class="btn btn-danger" data-dismiss="modal" type="button" v-on:click="cancelAppointment">Ja,
                absagen
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>

import ButtonSubmit from "@/components/buttons/ButtonSubmit";
import AppointmentCalendar from "@/components/AppointmentCalendar";

export default {
  name: "AppointmentOverview",
  components: {AppointmentCalendar, ButtonSubmit},
  data: function () {
    return {
      selectedAppointment: null,
      kalenderansicht: true,
    }
  },
  methods: {
    selectAppointment(appointment) {
      this.selectedAppointment = appointment;
    },
    cancelAppointment() {
      this.$store.dispatch('removeAppointment', {id: this.selectedAppointment.id, token: this.$store.getters.token});
      this.selectedAppointment = null;
    },
    async fetchApiInformation() {
      await this.$store.dispatch('fetchAbwesenheiten', this.$store.getters.token);
      await this.$store.dispatch('fetchAppointments', this.$store.getters.token);

    },
    liegtInDerVergangenheit(termin) {
      return termin.ausgewaehlterTermin <= new Date().format("YYYY-MM-DD")
    }
  },
  computed: {

    allAppointments: {
      get() {
        return this.$store.getters.allAppointments;
      }
    },
    alleAbwesenheiten: {
      get() {
        return this.$store.getters.alleAbwesenheiten;
      },
    },
  },
  /* Nur im Fall der Fälle, dass die Daten vor dem Weiterleiten aus irgendeinem Grund nicht geladen wurden */
  beforeMount: function () {
    this.fetchApiInformation();
  },
  errorCaptured: function (err) {
    console.log(err)
    this.$router.push("/")
  }
}
</script>

<style scoped>
.tab {
  font-size: 18pt;
  cursor: pointer;
  color: gray;
}
</style>
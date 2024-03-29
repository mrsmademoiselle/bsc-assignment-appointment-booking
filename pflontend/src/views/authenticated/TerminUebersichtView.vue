<template>
  <div class="d-flex justify-content-end border-1 my-2 border-top p-2">
    <div :title="'Zur '+(this.kalenderansicht ? 'Listen':'Kalender')+'ansicht wechseln'"
         class="tab col-1 tab-active"
         v-on:click="this.kalenderansicht = !this.kalenderansicht">
      <font-awesome-icon :icon="'fa-'+(this.kalenderansicht?'list':'calendar')" class="pointer"/>
    </div>
  </div>
  <div>

    <TerminKalender v-if="kalenderansicht" :abwesenheiten="alleAbwesenheiten"
                    :termine="alleTermine" class="mb-2"></TerminKalender>

    <div v-else class="my-3 ">
      <SuccessBanner :message="getMessage()"></SuccessBanner>
      <div class="justify-content-center d-flex mt-3">
        <table v-if="alleTermine.length > 0" class="col-lg-8 table">
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
          <tr v-for="appointment in alleTermine" :key="appointment.id"
              :class="istHeute(appointment) ? 'is-today' : ''"
              :title="(istHeute(appointment) ? 'Dieser Termin ist heute!' :null)">
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
        <div v-if="alleTermine.length <= 0" class="col-lg-6">Derzeit gibt es keine Termine.</div>
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

import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import TerminKalender from "@/components/compositions/TerminKalender";
import SuccessBanner from "@/components/fragments/SuccessBanner";

export default {
  name: "TerminUebersichtView",
  components: {SuccessBanner, TerminKalender, ButtonSubmit},
  data: function () {
    return {
      selectedAppointment: null,
      kalenderansicht: true,
      successes: []
    }
  },
  methods: {
    getMessage() {
      let message = ""
      this.successes.forEach(e => message = message + e)
      return message;
    },
    istHeute(appointment) {
      let date = new Date(appointment.ausgewaehlterTermin)
      const today = new Date()

      return date.getDate() === today.getDate() &&
          date.getMonth() === today.getMonth() &&
          date.getFullYear() === today.getFullYear();
    },
    selectAppointment(appointment) {
      this.selectedAppointment = appointment;
    },
    cancelAppointment() {
      this.$store.dispatch('removeAppointment', {
        id: this.selectedAppointment.id,
        token: this.$store.getters.token
      }).then(() => {
        this.successes = []
        this.successes.push("Der Termin wurde erfolgreich abgesagt.")
        this.selectedAppointment = null;
      });
    },
    async fetchApiInformation() {
      await this.$store.dispatch('fetchAbwesenheiten', {
        username: this.$store.getters.username,
        token: this.$store.getters.token
      });
      await this.$store.dispatch('fetchAppointments', this.$store.getters.token);

    },
    liegtInDerVergangenheit(termin) {
      return termin.ausgewaehlterTermin <= new Date().format("YYYY-MM-DD")
    }
  },
  computed: {

    alleTermine: {
      get() {
        return this.$store.getters.alleTermine;
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

.is-today {
  background-color: #ffeccc;
}
</style>
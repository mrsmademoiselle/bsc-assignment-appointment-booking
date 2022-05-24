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
      </tr>
      </tbody>
    </table>
    <div v-if="allAppointments.length <= 0" class="col-lg-6">Derzeit gibt es keine Termine.</div>
  </div>
</template>

<script>
import {TerminService} from "@/services/TerminService";

export default {
  name: "AppointmentOverview",
  data: function () {
    return {
      allAppointments: [],
    }
  },
  methods: {
    async getAppointments() {
      this.allAppointments = await TerminService.getAllAppointments();
    }
  },
  beforeMount: function () {
    this.getAppointments();
  },
}
</script>

<style scoped>

</style>
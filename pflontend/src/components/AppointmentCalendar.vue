<template>
  <vue-cal :disable-views="['years','year']" :events="events" :time-from="8 * 60" :time-to="19 * 60" cell-contextmenu
           class="vuecal--blue-theme" hide-weekends locale="de" style="height: 80vh"/>
</template>

<script>
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'
import {Appointment} from "@/entity/Appointment";
import {Abwesenheit} from "@/entity/Abwesenheit";

export default {
  name: "AppointmentCalendar",
  components: {VueCal},
  props: ['termine', 'abwesenheiten'],
  computed: {
    events: {
      get() {
        return this.convertEverythingToEvents(this.termine, this.abwesenheiten)
      }
    },
  },
  methods: {
    convertEverythingToEvents(termine, abwesenheiten) {
      let events = [];
      for (let counter in termine) {
        if (!Appointment.isValidAppointment(termine[counter])) console.log("not valid appointment. could not add to calendar. " + JSON.stringify(termine[counter]))
        let date = new Date(termine[counter].ausgewaehlterTermin + " " + termine[counter].uhrzeit);

        let newEvent = {
          start: date,
          end: date.addHours(1),
          title: termine[counter].vorname + ' ' + termine[counter].nachname,
          class: 'termin'
        };
        events.push(newEvent)
      }
      console.log("successfully converted " + termine.length + " termine.")

      for (let counter in abwesenheiten) {
        if (!Abwesenheit.isValidAbwesenheit(abwesenheiten[counter])) console.log("not valid appointment. could not add to calendar. " + JSON.stringify(termine[counter]))
        let date = new Date(abwesenheiten[counter].ausgewaehlterTermin + " " + abwesenheiten[counter].uhrzeit);

        events.push({
          start: date,
          end: date.addHours(1),
          title: abwesenheiten[counter].vorname + ' ' + abwesenheiten[counter].nachname,
          class: 'blocker'
        })
      }
      console.log("successfully converted " + abwesenheiten.length + " abwesenheiten.")
      return events;
    },
  }
}
</script>

<style>
.vuecal__event.termin {
  background: #70b5ce;
  color: #ffffff;
  font-size: 14px;
}

.vuecal__event.blocker {
  background: #fa6d6d;
  color: #ffffff;
  font-size: 18px;
}
</style>
<template>
  <vue-cal :disable-views="['years','year']" :events="events" :time-from="8 * 60" :time-to="19 * 60" cell-contextmenu
           class="vuecal--blue-theme" hide-weekends locale="de" style="height: 80vh; width:90vw; margin:auto"/>
</template>

<script>
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'
import {Termin} from "@/entity/Termin";
import {Abwesenheit} from "@/entity/Abwesenheit";

export default {
  name: "TerminKalender",
  components: {VueCal},
  props: ['termine', 'abwesenheiten'],
  computed: {
    events: {
      get() {
        return this.wandleUmInEvents(this.termine, this.abwesenheiten)
      }
    },
  },
  methods: {
    /**
     * Konvertiert die eingegebenen Termine und Abwesenheitseinträge in für die Calendar-Library kompatible Events.
     * Das ganze geschieht separat durch zwei Listen, da Abwesenheitseinträge ein anderes Styling erwünscht ist.
     */
    wandleUmInEvents(termine, abwesenheiten) {
      let events = [];

      // alle Termine konvertieren
      for (let counter in termine) {
        if (!Termin.hatKorrektesFormat(termine[counter])) console.log("not valid appointment. could not add to calendar. " + JSON.stringify(termine[counter]))
        let datum = new Date(termine[counter].ausgewaehlterTermin + " " + termine[counter].uhrzeit);

        let terminEvent = {
          start: datum,
          end: datum.addHours(1),
          title: termine[counter].vorname + ' ' + termine[counter].nachname,
          class: 'termin'
        };
        events.push(terminEvent)
      }
      console.log("Insgesamt wurden " + termine.length + " Termine erfolgreich konvertiert.")

      // alle Abwesenheitseinträge konvertieren
      for (let counter in abwesenheiten) {
        if (!Abwesenheit.hatKorrektesFormat(abwesenheiten[counter])) console.log("not valid appointment. could not add to calendar. " + JSON.stringify(termine[counter]))
        let startDate = abwesenheiten[counter].startDatum;
        let endDate = abwesenheiten[counter].endDatum;

        // Jeder Tag eines Abwesenheitseintrages muss separat im Kalender eingetragen werden
        for (let currentDate = new Date(startDate); currentDate <= new Date(endDate); currentDate.setDate(currentDate.getDate() + 1)) {
          events.push({
            start: currentDate.format("YYYY-MM-DD"),
            end: currentDate.format("YYYY-MM-DD"),
            content: "",
            title: "Abwesenheit",
            class: 'blocker'
          })
        }
      }
      console.log("Insgesamt wurden " + abwesenheiten.length + " Abwesenheitseinträge erfolgreich konvertiert.")
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
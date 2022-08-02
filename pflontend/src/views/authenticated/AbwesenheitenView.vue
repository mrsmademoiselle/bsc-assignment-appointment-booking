<template>
  <div class="container">
    <div class="form-group my-4 mx-3">
      <div class="border-bottom h5 d-flex justify-content-start mb-3">Neue Abwesenheit anlegen</div>
      <div class="row">
        <div class="col-auto pt-2"><b>Zeitraum auswählen: </b></div>
        <div class="col-auto">
          <Datepicker v-model="datePair" :disabledDates="nichtVerfuegbareTage"
                      :disabledWeekDays="[6, 0]" :enableTimePicker="false"
                      :format="formatDate"
                      autoApply class="mx-4 px-4"
                      locale="de"
                      multi-calendars="true" name="date"
                      range>
          </Datepicker>
        </div>
        <div class="m-auto text-secondary"><small>Bitte beachten Sie, dass derzeit nur ganze Tage als
          Abwesenheit buchbar
          sind.</small></div>
      </div>
      <ErrorBanner v-for="error in errors" :key="error" :message="error" class="mt-3"></ErrorBanner>
    </div>
    <div class=" d-flex justify-content-end mb-5">
      <ButtonSubmit class="col-auto" title="Anlegen" @onclick="addAbwesenheit"></ButtonSubmit>
    </div>
    <div class="my-3">
      <AbwesenheitenListe :abwesenheitsliste="this.abwesenheiten"></AbwesenheitenListe>
    </div>
  </div>
</template>

<script>
import AbwesenheitenListe from "@/components/AbwesenheitenListe";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";
import Datepicker from "@vuepic/vue-datepicker";
import ErrorBanner from "@/components/banner/ErrorBanner";

export default {
  name: "AbwesenheitenView",
  components: {ErrorBanner, ButtonSubmit, AbwesenheitenListe, Datepicker},
  data: function () {
    return {
      neueAbwesenheit: {
        startDatum: null,
        endDatum: null,
      },
      nichtVerfuegbareTage: [],
      datePair: [],
      datumsliste: [],
      errors: [],
    }
  },
  computed: {
    abwesenheiten: {
      get() {
        return this.$store.getters.alleAbwesenheiten;
      }
    }
  },
  methods: {
    addAbwesenheit() {
      if (this.datePair.length !== 2) {
        console.log("date has invalid attributes")
      }
      this.neueAbwesenheit.startDatum = this.datePair[0].toISOString();
      this.neueAbwesenheit.endDatum = this.datePair[1].toISOString();

      this.$store.dispatch('addAbwesenheit', {abwesenheit: this.neueAbwesenheit, token: this.$store.getters.token})
          .then(() => this.errors = [])
          .catch((e) => {
            this.errors.push(e);
          });
    },
    formatDate([date1, date2]) {
      return date1.toLocaleDateString() + " - " + date2.toLocaleDateString()
    },
    getDatumslisteFuerAbwesenheiten() {
      let datumsliste = []
      this.fetchAbwesenheiten();

      console.log("preparing to iterate through " + this.abwesenheiten.length + " abwesenheiten")
      // Für jeden Tag der Abwesenheit jedes einzelne Datum zwischen start- und enddatum rausparsen
      for (let counter in this.abwesenheiten) {
        try {
          let startDate = new Date(this.abwesenheiten[counter].startDatum);

          let currentDate = startDate;
          let endDate = new Date(this.abwesenheiten[counter].endDatum);
          for (currentDate; currentDate <= endDate; currentDate.setDate(currentDate.getDate() + 1)) {
            datumsliste.push(currentDate);
          }
        } catch (e) {
          console.log("Could not get dates from " + JSON.stringify(this.abwesenheiten[counter]))
        }
      }
      console.log("added " + datumsliste.length + " absences to list")
      return datumsliste;
    },
    async fetchAbwesenheiten() {
      await this.$store.dispatch('fetchAbwesenheiten', this.$store.getters.token);
    }
  },
  beforeMount: function () {
    this.fetchAbwesenheiten();
  },
  mounted() {
    const startDate = new Date();
    const endDate = new Date(new Date().setDate(startDate.getDate() + 7));
    this.datePair = [startDate, endDate];
    this.datumsliste = this.getDatumslisteFuerAbwesenheiten();
  },
}
</script>

<style scoped>

</style>
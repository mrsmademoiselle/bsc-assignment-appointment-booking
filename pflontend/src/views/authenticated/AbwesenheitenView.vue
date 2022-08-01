<template>
  <div class="container">
    <div class="form-group my-4 mx-3">
      <div class="border-bottom h5 d-flex justify-content-start mb-3">Neue Abwesenheit anlegen</div>
      <div class="row">
        <div class="col-auto pt-2"><b>Zeitraum auswählen: </b></div>
        <div class="col-auto">
          <Datepicker v-model="neueAbwesenheit.datePair" :disabledDates="nichtVerfuegbareTage"
                      :disabledWeekDays="[6, 0]" :enableTimePicker="false"
                      :format="formatDate"
                      autoApply class="mx-4 px-4"
                      locale="de"
                      multi-calendars="true" name="date"
                      range>
          </Datepicker>
        </div>
      </div>
    </div>
    <div class=" d-flex justify-content-end mb-5">
      <ButtonSubmit class="col-auto" title="Anlegen" @onclick="abwesenheitAnlegen"></ButtonSubmit>
    </div>
    <div class="my-3">
      <AbwesenheitenListe :abwesenheitsliste="abwesenheitsliste"></AbwesenheitenListe>
    </div>
  </div>
</template>

<script>
import AbwesenheitenListe from "@/components/AbwesenheitenListe";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";
import Datepicker from "@vuepic/vue-datepicker";
import {Abwesenheit} from "@/entity/Abwesenheit";

export default {
  name: "AbwesenheitenView",
  components: {ButtonSubmit, AbwesenheitenListe, Datepicker},
  data: function () {
    return {
      abwesenheitsliste: [new Abwesenheit({
        startDatum: new Date(),
        endDatum: new Date(new Date().setDate(new Date().getDate() + 7))
      })],
      neueAbwesenheit: {
        startDatum: null,
        endDatum: null,
      },
      nichtVerfuegbareTage: [],
      datePair: []
    }
  },
  methods: {
    abwesenheitAnlegen() {
      if (this.datePair.length !== 2) {
        console.log("date has invalid attributes")
      }
      this.neueAbwesenheit.startDatum = this.datePair[0];
      this.neueAbwesenheit.endDatum = this.datePair[1];
      alert("angelegt!")
    },
    formatDate([date1, date2]) {
      return date1.toLocaleDateString() + " - " + date2.toLocaleDateString()
    },
    getDatumslisteFuerAbwesenheiten() {
      return [new Date().toDateString()];
    }
  },
  beforeMount() {
    // get Abwesenheiten
    // this.getDatumslisteFuerAbwesenheiten()

  },
  mounted() {
    const startDate = new Date();
    const endDate = new Date(new Date().setDate(startDate.getDate() + 7));
    this.datePair = [startDate, endDate];
  }
}
</script>

<style scoped>

</style>
<template>
  <form class="mt-5">

    <!-- Step1: Grund -->
    <div v-if="step === 1 " class="container">
      <div class="bg-light rounded border mb-4 justify-content-center">
        <div class="h5 my-4">1. Welche Beratungsstelle möchten Sie besuchen?</div>
        <div v-for="beratungsstelle in alleBeratungsstellen" :key="beratungsstelle.id"
             class="form-check row">
          <input id="beratungsstelle" v-model="termin.beratungsstelle" class="form-check-input"
                 name="beratungsstelle" type="radio"
                 v-bind:value="beratungsstelle">
          <label class="form-check-label" for="beratungsstelle">
            {{ beratungsstelle.formatToReadableString() }}
          </label>
        </div>
        <div>
          <div class="h5 mt-4">2. Sind Sie bereits Mitglied der VLH?</div>
          <div class="form-check form-check-inline">
            <input id="ja" v-model="termin.kundeninformationen.bereitsMitglied" :value="true" class="form-check-input"
                   name="bereitsMitglied"
                   type="radio">
            <label class="form-check-label" for="ja">ja</label>
          </div>
          <div class="form-check form-check-inline">
            <input id="nein" v-model="termin.kundeninformationen.bereitsMitglied" :value="false"
                   class="form-check-input"
                   name="bereitsMitglied" type="radio">
            <label class="form-check-label" for="nein">nein</label>
          </div>
        </div>
        <div class="h5 mt-4">Worum geht es bei Ihrem Termin?</div>
        <div v-for="termingrund in alleTermingruende" :key="termingrund" class="form-check my-2">
          <input id="termingrund" v-model="termin.termingrund"
                 class="form-check-input"
                 name="termingrund"
                 type="radio" v-bind:value="termingrund">
          <label class="form-check-label" for="termingrund">
            {{ termingrund }}
          </label>
        </div>
      </div>
    </div>

    <!-- Step2: Termin -->
    <div v-if="step === 2" class="container">
      <div class="h4 mb-4">Wählen Sie einen Termin aus, der Ihnen passt.</div>
      <div class="row justify-content-center bg-light rounded border">
        <div class="col-5 my-4 mx-4">
          <Datepicker v-model="termin.ausgewaehlterTermin" :disabledDates="alleBelegtenTermine"
                      :disabledWeekDays="[6, 0]"
                      :enableTimePicker="false"
                      :maxDate="maxDate" :minDate="minDate"
                      autoApply
                      class="mx-4 px-4"
                      inline
                      locale="de" name="date"
                      preventMinMaxNavigation></Datepicker>
        </div>
        <div class="col">
          <div class="h5 mt-4">{{
              this.termin.ausgewaehlterTermin !== null
                  ? "Ausgewähltes Datum: " + this.termin.ausgewaehlterTermin.toLocaleDateString("de-DE")
                  : "Bitte wählen Sie ein Datum aus."
            }}
          </div>
          <div v-if="this.termin.ausgewaehlterTermin !== null">
            <div v-for="verfuegbareUhrzeit in verfuegbareUhrzeitenFuerDatum" :key="verfuegbareUhrzeit">
              <div class="btn-group btn-group-toggle" data-toggle="buttons">
                <label class="btn btn-warning active">
                  <input id="verfuegbareUhrzeit" v-model="termin.uhrzeit" autocomplete="off" name="verfuegbareUhrzeit"
                         type="radio">
                  {{ verfuegbareUhrzeit }} Uhr
                </label>
              </div>
              <!-- TODO Alle Termine ziehen und darstellen-->
              <div v-if="verfuegbareUhrzeitenFuerDatum.length === 0">Für diesen Tag sind keine Uhrzeiten verfügbar.
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

    <!-- Step3: persönliche Daten -->
    <div v-if="step === 3" class="container">
      <div class="form-group row">
        <label class="col-3">Anrede:</label>
        <select v-model="termin.kundeninformationen.anrede" class="custom-select col-6">
          <option v-for="anrede in alleAnreden" :key="anrede" :value="anrede">
            {{ anrede }}
          </option>
        </select>
      </div>
      <div class="form-group row">
        <label class="col-3" for="vorname">Vorname:</label>
        <input id="vorname" v-model="termin.kundeninformationen.vorname" class="form-control col-6"
               placeholder="Vorname"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="nachname">Nachname:</label>
        <input id="nachname" v-model="termin.kundeninformationen.nachname" class="form-control col-6"
               placeholder="Nachname"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="telefon">Telefon:</label>
        <input id="telefon" v-model="termin.kundeninformationen.telefon" class="form-control col-6"
               placeholder="01234 56789"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="email">E-Mail:</label>
        <input id="email" v-model="termin.kundeninformationen.email" class="form-control col-6"
               placeholder="max@mustermann.de"
               type="email">
      </div>
      <div class="form-group row">
        <label class="col-3" for="bemerkung">Bemerkung:</label>
        <textarea id="bemerkung" v-model="termin.bemerkung" class="form-control col-6"
                  placeholder="Gibt es noch etwas, das wir wissen sollten? Teilen Sie es uns hier mit!"></textarea>
      </div>
    </div>

    <!-- Step4: Zusammenfassung -->
    <div v-if="step === 4" class="container ">

      <div class="h4">Bitte überprüfen Sie Ihre Angaben und bestätigen dann mit "Buchen".</div>
      <div class="bg-light rounded border mb-4 justify-content-center">
        <div class="row">
          <div class="form-group my-4 col-6">
            <div class="col-6">Anrede: {{ this.termin.kundeninformationen.anrede }}</div>
            <div class="col-6">Vorname: {{ this.termin.kundeninformationen.vorname }}</div>
            <div class="col-6">Nachname: {{ this.termin.kundeninformationen.nachname }}</div>
            <!-- ... -->
          </div>
          <div class="form-group my-4 col-6">
            <div class="col-6">bereitsMitglied: {{
                this.termin.kundeninformationen.bereitsMitglied ? "ja" : "nein"
              }}
            </div>
            <!-- ... -->
          </div>
        </div>
        <div class="row">

          <div class="form-group my-4 col-6">
            <div class="col-6">Termin: {{ this.termin.ausgewaehlterTermin.toDateString() }} {{ this.termin.uhrzeit }}
              Uhr
            </div>
            <div class="col-6">Beratungsstelle: {{ this.termin.beratungsstelle.formatToReadableString() }}</div>

          </div>
          <div class="form-group my-4 col-6">
            <div class="col-6">Termingrund: {{ this.termin.termingrund }}</div>
            <div class="col-6">Bemerkung: {{ this.termin.bemerkung }}</div>

          </div>
        </div>
      </div>
    </div>

    <!-- Step5: Vielen Dank -->
    <div v-if="step === 5" class="container">
      <div>Vielen Dank für Ihre Buchung!</div>
    </div>


    <button class="btn btn-outline-primary mx-2" type="button" v-on:click="sendData">Fire Dummy Data</button>
    <button v-if="step > 1 && step < 5" class="btn btn-secondary mx-2" type="button" v-on:click="previous">Zurück
    </button>
    <button v-if="step < 4" class="btn btn-primary mx-2" type="button" v-on:click="nextStep">Weiter</button>
    <button v-if="step === 4" class="btn btn-primary mx-2" type="button" v-on:click="submit">Buchen</button>
  </form>
</template>

<script>
import {BeratungsstellenService} from "@/services/BeratungsstellenService";
import {TerminService} from "@/services/TerminService";
import Datepicker from '@vuepic/vue-datepicker';

export default {
  name: "AppointmentBookingView",
  components: {Datepicker},
  data: function () {
    return {
      // Data to API
      termin: {
        termingrund: null,
        uhrzeit: null,
        ausgewaehlterTermin: null,
        bemerkung: "",
        beratungsstelle: null,
        kundeninformationen: {
          vorname: null,
          nachname: null,
          bereitsMitglied: false,
          email: null,
          telefon: null,
          anrede: null
        },
      },
      step: 1,
      responseMessage: "No response yet.",

      // Data from API
      alleBeratungsstellen: [],
      alleTermingruende: [],
      // "anonymisiert"
      alleBelegtenTermine: [],
      alleAnreden: [],

      verfuegbareUhrzeitenFuerDatum: [],
      minDate: null,
      maxDate: null
    }
  },
  /**
   * Beobachter für das Feld termin.ausgewaehlterTermin. Verändert sich der Wert durch einen Klick aus den Kalender,
   * werden für dieses Datum die verfügbaren Uhrzeiten geparst und gesetzt.
   *
   *
   * Als API-Aufruf, damit ich nicht im Vorhinein für alle Daten berechnen muss, welche Uhrzeiten belegt sind und welche nicht...
   *
   */
  watch: {
    'termin.ausgewaehlterTermin'(newValue) {
      this.getAlleVerfuegbarenUhrzeiten(newValue);
    },
  },
  methods: {
    nextStep() {
      if (this.step === 2 && (this.termin.ausgewaehlterTermin == null || this.termin.uhrzeit == null)) {
        alert("Bitte geben Sie einen Termin und eine Uhrzeit an!")
        return;
      }

      this.step = this.step + 1;
      // get necessary information from api
      this.getApiInformation();
    },
    previous() {
      this.step = this.step - 1;
      // get necessary information from api
      this.getApiInformation();
    },
    submit() {
      this.step = this.step + 1;
      TerminService.legeTerminAn(this.termin);
      alert("submitted!")
    },

    /**
     * Switch case to prevent the user from having to fetch all data when the view mounts.
     * Instead, the data is being fetched piece by piece whenever it is needed.
     */
    async getApiInformation() {
      switch (this.step) {
        case 1:
          if (this.alleBeratungsstellen.length === 0) {
            this.alleBeratungsstellen = await BeratungsstellenService.getAlleBeratungsstellen();
            this.termin.beratungsstelle = this.alleBeratungsstellen[0];
          }
          if (this.alleTermingruende.length === 0) {
            this.alleTermingruende = await BeratungsstellenService.getAlleTermingruende();
            this.termin.termingrund = this.alleTermingruende[0];

          }
          break;
        case 2:
          if (this.alleBelegtenTermine.length === 0) {
            this.alleBelegtenTermine = await TerminService.getKomplettBelegteDatuemer();
            this.setMinAndMaxDate();
          }
          break;
        case 3:
          if (this.alleAnreden.length === 0) {
            this.alleAnreden = await BeratungsstellenService.getAlleAnreden();
          }
          break;
      }
    },
    setMinAndMaxDate() {
      let date = new Date();
      this.minDate = date.toDateString();
      // Man darf maximal 6 Monate im voraus buchen
      this.maxDate = new Date(date.setMonth(date.getMonth() + 6)).toDateString();
    },

    async getAlleVerfuegbarenUhrzeiten(datum) {
      this.verfuegbareUhrzeitenFuerDatum = await TerminService.getAlleVerfuegbarenUhrzeiten(datum);

    }
  }, beforeMount: function () {
    this.getApiInformation();
  }
}
</script>

<style scoped>

</style>
<template>
  <form class="mt-5">

    <!-- Step1: Grund -->
    <div v-if="step === 1" class="container">
      <div>
        <div class="h5 my-4">1. Welche Beratungsstelle möchten Sie besuchen?</div>
        <div v-for="beratungsstelle in alleBeratungsstellen" :key="beratungsstelle.id"
             class="form-check row">
          <input id="beratungsstelle" v-model="beratungsstellenId" :value="beratungsstelle.id" checked
                 class="form-check-input"
                 name="exampleRadios" type="radio">
          <label class="form-check-label" for="beratungsstelle">
            {{ beratungsstelle.formatToReadableString() }}
          </label>
        </div>
        <div>
          <div class="h5 mt-4">2. Sind Sie bereits Mitglied der VLH?</div>
          <div class="form-check form-check-inline">
            <input id="ja" v-model="kundeninformationen.bereitsMitglied" :value="true" class="form-check-input"
                   name="inlineRadioOptions"
                   type="radio">
            <label class="form-check-label" for="ja">ja</label>
          </div>
          <div class="form-check form-check-inline">
            <input id="nein" v-model="kundeninformationen.bereitsMitglied" :value="false"
                   class="form-check-input"
                   name="inlineRadioOptions" type="radio">
            <label class="form-check-label" for="nein">nein</label>
          </div>
        </div>
        <div class="h5 mt-4">Worum geht es bei Ihrem Termin?</div>
        <div v-for="tg in alleTermingruende" :key="tg" class="form-check my-2">
          <input id="termingrund" v-model="termingrund" :value="tg" checked
                 class="form-check-input"
                 name="exampleRadios" type="radio">
          <label class="form-check-label" for="termingrund">
            {{ tg }}
          </label>
        </div>
      </div>
    </div>

    <!-- Step2: Termin -->
    <div v-if="step === 2" class="container">
      <div class="h4">Wählen Sie einen Termin aus, der Ihnen passt.</div>
      <div class="row justify-content-center">
        <div class="col">Calendar</div>
        <div class="col">
          <!-- TODO Alle Termine ziehen und darstellen-->
          <input id="uhrzeit" v-model="uhrzeit" checked
                 class="form-check-input" type="radio">
          <label class="form-check-label" for="uhrzeit">Uhrzeit 1</label>
        </div>
      </div>
    </div>

    <!-- Step3: persönliche Daten -->
    <div v-if="step === 3" class="container">
      <div class="form-group row">
        <label class="col-3">Anrede:</label>
        <select v-model="kundeninformationen.anrede" class="custom-select col-6">
          <option value="1">Frau</option>
          <option value="2">Herr</option>
          <option value="3">keine Angabe</option>
        </select>
      </div>
      <div class="form-group row">
        <label class="col-3" for="vorname">Vorname:</label>
        <input id="vorname" v-model="kundeninformationen.vorname" class="form-control col-6" placeholder="Vorname"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="nachname">Nachname:</label>
        <input id="nachname" v-model="kundeninformationen.nachname" class="form-control col-6" placeholder="Nachname"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="telefon">Telefon:</label>
        <input id="telefon" v-model="kundeninformationen.telefon" class="form-control col-6"
               placeholder="01234 56789"
               type="text">
      </div>
      <div class="form-group row">
        <label class="col-3" for="email">E-Mail:</label>
        <input id="email" v-model="kundeninformationen.email" class="form-control col-6" placeholder="max@mustermann.de"
               type="email">
      </div>
      <div class="form-group row">
        <label class="col-3" for="bemerkung">Bemerkung:</label>
        <textarea id="bemerkung" v-model="kundeninformationen.bemerkung" class="form-control col-6"
                  placeholder="Gibt es noch etwas, das wir wissen sollten? Teilen Sie es uns hier mit!"
                  type="bemerkung"></textarea>
      </div>
    </div>

    <!-- Step4: Zusammenfassung -->
    <div v-if="step === 4" class="container">
      <div class="h4">Bitte überprüfen Sie Ihre Angaben und bestätigen dann mit "Buchen".</div>
      <div class="form-group my-4">
        <div class="col-6" for="vorname">Anrede: {{ this.kundeninformationen.anrede }}</div>
        <div class="col-6" for="vorname">Vorname: {{ this.kundeninformationen.vorname }}</div>
        <div class="col-6" for="vorname">Nachname: {{ this.kundeninformationen.nachname }}</div>
        <!-- ... -->
      </div>
    </div>

    <!-- Step5: Vielen Dank -->
    <div v-if="step === 5" class="container">
      <div>Vielen Dank für Ihre Buchung!</div>
    </div>


    <button v-if="step > 1 && step < 5" class="btn btn-secondary mx-2" type="button" v-on:click="previous">Zurück
    </button>
    <button v-if="step < 4" class="btn btn-primary mx-2" type="button" v-on:click="nextStep">Weiter</button>
    <button v-if="step === 4" class="btn btn-primary mx-2" type="button" v-on:click="submit">Buchen</button>
  </form>
</template>

<script>
import exampleTerminbuchung from "../assets/exampleData.json";
import myApi from "@/services/myApi";
import {BeratungsstellenService} from "@/services/BeratungsstellenService";
import {TerminService} from "@/services/TerminService";

export default {
  name: "AppointmentBookingView",
  data: function () {
    return {
      responseMessage: "No response yet.",
      step: 3,
      alleBeratungsstellen: [],
      alleTermingruende: [],
      // "anonymisiert"
      alleBelegtenTermine: [],
      alleAnreden: [],
      kundeninformationen: {
        vorname: null,
        nachname: null,
        ort: null,
        plz: null,
        bereitsMitglied: false,
        anrede: null
      },
      beratungsstellenId: null,
      termingrund: null,
      uhrzeit: null,
      datum: null
    }
  },
  methods: {

    collectData() {
      return exampleTerminbuchung;
    },
    sendData() {
      myApi.post("example/post", this.collectData()).then(response => this.responseMessage = response.data)
    },
    nextStep() {
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
      alert("submitted!")
    },
    /**
     * Switch case to prevent the user from having to fetch all data when the view mounts.
     * Instead, the data is being fetched piece by piece whenever it is needed.
     */
    async getApiInformation() {
      switch (this.step) {
        case 1:
          if (this.alleBeratungsstellen === []) {
            this.alleBeratungsstellen = await BeratungsstellenService.getAlleBeratungsstellen();
          }
          if (this.alleTermingruende === []) {
            this.alleTermingruende = await BeratungsstellenService.getAlleTermingruende();
          }
          break;
        case 2:
          if (this.alleBelegtenTermine === []) {
            this.alleBelegtenTermine = await TerminService.getAlleBelegtenTermine();
          }
          break;
        case 3:
          if (this.alleAnreden === []) {
            this.alleAnreden = await BeratungsstellenService.getAlleAnreden();
          }
          break;
      }
    },
  }, beforeMount: function () {
    this.getApiInformation();
  }
}
</script>

<style scoped>

</style>
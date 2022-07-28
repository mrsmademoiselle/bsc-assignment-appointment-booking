<template>
  <div class="justify-content-center d-flex mt-3 mb-0 container">
    <ProgressBar :active-step="this.step-1" :show-label="false" :steps="alleSchritte" is-reactive
                 reactivity-type="single-step" show-bridge
                 @onStepChanged="(step) => switchTo(step+1)"></ProgressBar>
  </div>
  <form class="mt-3 container bg-light rounded border p-4">

    <!-- Step1: Grund -->
    <div v-if="step === 1 ">
      <div class="mb-2 justify-content-center">
        <MultipleChoiceForm :options="alleBeratungsstellen" for="beratungsstelle"
                            header="1. Welche Beratungsstelle möchten Sie besuchen?"
                            @onselect="(option) => this.termin.beratungsstelle = option"></MultipleChoiceForm>
        <MultipleChoiceForm :options="['ja', 'nein']" for="bereitsMitglied"
                            header="2. Sind Sie bereits Mitglied der VLH?"
                            @onselect="(option) => termin.kundeninformationen.bereitsMitglied = option"></MultipleChoiceForm>
        <MultipleChoiceForm :hinweis="hinweistext" :options="alleTermingruende"
                            for="termingrund"
                            header="3. Worum geht es bei Ihrem Termin?"
                            @onselect="(option) => termin.termingrund= option"></MultipleChoiceForm>
      </div>
    </div>

    <!-- Step2: Termin -->
    <div v-if="step === 2">
      <TitlePrimary text="Wählen Sie einen Termin aus, der Ihnen passt."></TitlePrimary>
      <div class="row justify-content-center">
        <div class="col-5 my-4 mx-4">
          <Datepicker v-model="termin.ausgewaehlterTermin" :disabledDates="alleBelegtenTermine"
                      :disabledWeekDays="[6, 0]"
                      :enableTimePicker="false"
                      :maxDate="maxDate" :minDate="minDate"
                      autoApply
                      class="mx-4 px-4"
                      inline
                      locale="de" name="date"
                      preventMinMaxNavigation>
          </Datepicker>

        </div>
        <div class="col-auto">
          <div v-if="this.termin.ausgewaehlterTermin !== null">
            <TitleSecondary :muted="true" :text="this.termin.ausgewaehlterTermin !== null
                                ? ' Ausgewähltes Datum: ' + this.termin.ausgewaehlterTermin.toLocaleDateString('de-DE')
                                : 'Bitte wählen Sie ein Datum aus.'"></TitleSecondary>

            <TimePicker :times="verfuegbareUhrzeitenFuerDatum" class="mt-3 p-5"
                        @onselect="(option) => this.termin.uhrzeit = option"></TimePicker>

            <div v-if="verfuegbareUhrzeitenFuerDatum.length === 0">Für diesen Tag sind keine Uhrzeiten verfügbar.</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Step3: persönliche Daten -->
    <div v-if="step === 3">
      <TitleSecondary muted="true"
                      text="Bitte teilen Sie uns Ihre Daten mit, damit wir sie erreichen können."></TitleSecondary>
      <div class="pl-5 ml-5 mt-5">
        <div class="form-group row">
          <label class="col-3">Anrede:</label>
          <select v-model="termin.kundeninformationen.anrede" class="custom-select col-6">
            <option v-for="anrede in alleAnreden" :key="anrede" :value="anrede">
              {{ anrede }}
            </option>
          </select>
        </div>
        <TextInput :input="this.termin.kundeninformationen.vorname" label="Vorname" placeholder="Vorname"
                   @oninput="(input) => this.termin.kundeninformationen.vorname = input"></TextInput>
        <TextInput :input="this.termin.kundeninformationen.nachname" label="Nachname" placeholder="Nachname"
                   @oninput="(input) => this.termin.kundeninformationen.nachname = input"></TextInput>
        <TextInput :input="this.termin.kundeninformationen.telefon" label="Telefon" placeholder="01234 56789"
                   @oninput="(input) => this.termin.kundeninformationen.telefon = input"></TextInput>
        <TextInput :input="this.termin.kundeninformationen.email" label="E-Mail" placeholder="max@mustermann.de"
                   type="email"
                   @oninput="(input) => this.termin.kundeninformationen.email = input"></TextInput>

        <div class="form-group row">
          <label class="col-3" for="bemerkung">Bemerkung:</label>
          <textarea id="bemerkung" v-model="termin.bemerkung" class="form-control col-6"
                    placeholder="Gibt es noch etwas, das wir wissen sollten? Teilen Sie es uns hier mit!"></textarea>
        </div>
      </div>
    </div>

    <!-- Step4: Zusammenfassung -->
    <div v-if="step === 4 && this.termin.ausgewaehlterTermin !== null">
      <TitleSecondary muted="true"
                      text="Bitte überprüfen Sie Ihre Angaben und bestätigen dann mit 'Buchen'."></TitleSecondary>
      <div class="row container m-5 justify-content-center d-flex">
        <div class="col-6">
          <TextLabel :input="this.termin.kundeninformationen.ausgewaehlterTermin" label="Termin"></TextLabel>
          <TextLabel :input="this.termin.kundeninformationen.beratungsstelle" label="Beratungsstelle"></TextLabel>
          <TextLabel
              :input="this.termin.termingrund+ ', '+this.termin.kundeninformationen.bereitsMitglied ? 'BestandskundIn' : 'NeukundIn'"
              label="Terminart"></TextLabel>
          <TextLabel :input="this.termin.bemerkung" label="Bemerkung"></TextLabel>

        </div>
        <div class="col-6">

          <TextLabel :input="this.termin.kundeninformationen.anrede + ' '+this.termin.kundeninformationen.vorname +
                ' '+this.termin.kundeninformationen.nachname"
                     label="Name"></TextLabel>
          <TextLabel :input="this.termin.kundeninformationen.telefon" label="Telefon"></TextLabel>
          <TextLabel :input="this.termin.kundeninformationen.email" label="E-Mail"></TextLabel>


        </div>
      </div>
    </div>

    <!-- Step5: Vielen Dank -->
    <div v-if="step === 5">
      <div>Vielen Dank für Ihre Buchung!</div>
    </div>
    <ErrorBanner v-if="this.errors.length > 0" :message="getErrorMessage()"></ErrorBanner>
    <div :class="step > 1 ? 'justify-content-between' : 'justify-content-end'" class=" d-flex px-4 pt-4 mx-5 pb-2">
      <ButtonCancel v-if="step > 1 && step < 5" class="px-4" title="Zurück"
                    @onclick="switchTo(this.step-1)"></ButtonCancel>
      <ButtonSubmit v-if="step < 4" class="px-4 float-right" title="Weiter" @onclick="switchTo(step+1)"></ButtonSubmit>
      <ButtonSubmit v-if="step === 4" class="px-4" title="Buchen" @onclick="submit"></ButtonSubmit>
    </div>
  </form>
</template>

<script>
import {BeratungsstellenService} from "@/services/BeratungsstellenService";
import {TerminService} from "@/services/TerminService";
import Datepicker from '@vuepic/vue-datepicker';
import ButtonSubmit from "@/components/buttons/ButtonSubmit";
import ButtonCancel from "@/components/buttons/ButtonCancel";
import TitlePrimary from "@/components/titles/TitlePrimary";
import MultipleChoiceForm from "@/components/MultipleChoiceForm";
import TextInput from "@/components/TextInput";
import TitleSecondary from "@/components/titles/TitleSecondary";
import ProgressBar from "@/components/ProgressBar";
import TimePicker from "@/components/TimePicker";
import ErrorBanner from "@/components/ErrorBanner";
import TextLabel from "@/components/TextLabel";

export default {
  name: "AppointmentBookingView",
  components: {
    TextLabel,
    ErrorBanner,
    TimePicker,
    ProgressBar,
    TitleSecondary,
    TextInput,
    MultipleChoiceForm,
    TitlePrimary,
    Datepicker,
    ButtonCancel,
    ButtonSubmit,

  },
  data: function () {
    return {
      step: 4,

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
      responseMessage: "No response yet.",

      alleSchritte: ['Grund', 'Termin', 'Persönliche Daten', 'Zusammenfassung', 'Vielen Dank'],
      // Data from API
      alleBeratungsstellen: [],
      alleTermingruende: [],
      // "anonymisiert"
      alleBelegtenTermine: [],
      alleAnreden: [],

      hinweistext: "",
      verfuegbareUhrzeitenFuerDatum: [],
      minDate: null,
      maxDate: null,
      errors: [],
      success: []
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
    'termin.termingrund'(newValue) {
      this.aendereHinweistext(newValue);
    }
  },
  methods: {
    aendereHinweistext(ausgewaehlterTermingrund) {
      this.hinweistext = "";
      if (ausgewaehlterTermingrund === "Erstberatung") {
        this.hinweistext = "Bitte bringen Sie Ihren Personalausweis und Ihre Steuerunterlagen mit. Falls Sie verheiratet sind, benötigen wir auch den Ausweis Ihres Ehepartners.";
      }
    },
    getErrorMessage() {
      let message = "Bitte ";
      for (let i = 0; i < this.errors.length; i++) {
        message = message +
            (i === this.errors.length - 2
                ? (' ' + this.errors[i] + ' und ')
                : ((i === this.errors.length - 1)
                    ? (' ' + this.errors[i] + ' ')
                    : (' ' + this.errors[i] + ', ')));
      }
      message = message + " eingeben."
      return message;
    },
    submit() {
      this.errors = [];
      if (this.validateAllInputs()) {
        this.step = this.step + 1;
        this.termin.uhrzeit += ":00";
        TerminService.legeTerminAn(this.termin);
        this.success.push("Vielen Dank für Ihre Buchung! Der Termin wurde erfolgreich angelegt.")
      }
    },
    switchTo(step) {
      this.errors = []
      let navigatingBackwards = step < this.step;
      // should only trigger validation if navigating forward
      let isOk = navigatingBackwards || this.validatePreviousInput();
      if (isOk) {
        this.step = step;
        this.getApiInformation();
      }
    },
    validateStep1() {
      if (this.termin.beratungsstelle === null && !this.alleBeratungsstellen.find(e => e.id === this.termin.beratungsstelle.id)) {
        this.errors.push("Beratungsstelle")
      }
      if (this.termin.kundeninformationen.bereitsMitglied === null) {
        this.errors.push("Mitgliedsstatus")
      }
      if (this.termin.termingrund === null && !this.alleTermingruende.find(e => e === this.termin.termingrund)) {
        this.errors.push("Termingrund")
      }
      return this.errors.length === 0;
    },
    validateStep2() {
      if (this.termin.ausgewaehlterTermin === null) {
        this.errors.push("Datum")
      }
      if (this.termin.uhrzeit === null) {
        this.errors.push("Uhrzeit")
      }
      return this.errors.length === 0;
    },
    validateStep3() {
      if (!this.istStringVorhanden(this.termin.kundeninformationen.vorname)) {
        this.errors.push("Vornamen")
      }
      if (!this.istStringVorhanden(this.termin.kundeninformationen.nachname)) {
        this.errors.push("Nachnamen")
      }
      if (!this.istStringVorhanden(this.termin.kundeninformationen.anrede)) {
        this.errors.push("Anrede")
      }
      if (!this.istStringVorhanden(this.termin.kundeninformationen.email)) {
        this.errors.push("E-Mail-Adresse")
      }
      if (!this.istStringVorhanden(this.termin.kundeninformationen.telefon)) {
        this.errors.push("Telefonnummer")
      }

      return this.errors.length === 0;
    },
    validateAllInputs() {
      return this.validateStep1() && this.validateStep2() && this.validateStep3();
    },
    istStringVorhanden(string) {
      return string !== null && string.trim().length > 0;
    },
    validatePreviousInput() {
      let inputValid = false;
      switch (this.step) {
        case 1:
          inputValid = this.validateStep1();
          break;
        case 2:
          inputValid = this.validateStep2();
          break;
        case 3:
          inputValid = this.validateStep3();
          break;
      }
      return inputValid;
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
        case 4:
        case 5:
          if (this.alleAnreden.length === 0) {
            this.alleAnreden = await BeratungsstellenService.getAlleAnreden();
            this.termin.kundeninformationen.anrede = this.alleAnreden[0]
          }
          if (this.alleBeratungsstellen.length === 0) {
            this.alleBeratungsstellen = await BeratungsstellenService.getAlleBeratungsstellen();
            this.termin.beratungsstelle = this.alleBeratungsstellen[0];
          }
          if (this.alleTermingruende.length === 0) {
            this.alleTermingruende = await BeratungsstellenService.getAlleTermingruende();
            this.termin.termingrund = this.alleTermingruende[0];
          }
          this.termin.kundeninformationen.anrede = this.alleAnreden[0];
          this.termin.termingrund = this.alleTermingruende[0];
          this.termin.beratungsstelle = this.alleBeratungsstellen[0];
          this.termin.kundeninformationen.vorname = "Max";
          this.termin.kundeninformationen.nachname = "Max";
          this.termin.kundeninformationen.email = "Max";
          this.termin.kundeninformationen.telefon = "Max";
          this.termin.kundeninformationen.bereitsMitglied = false;
          this.termin.ausgewaehlterTermin = new Date();
          this.termin.uhrzeit = "9"
          break;
      }
    },
    customPickerOption() {
      const results = [
        {
          label: '21:15',
          value: {
            hours: 21,
            minutes: 15
          }
        },
        {
          label: '22:15',
          value: {
            hours: 22,
            minutes: 15
          }
        }
      ]
      return results
    },
    setMinAndMaxDate() {
      let date = new Date();
      this.minDate = date.toDateString();
      // Man darf maximal 6 Monate im voraus buchen
      this.maxDate = new Date(date.setMonth(date.getMonth() + 6)).toDateString();
    },

    async getAlleVerfuegbarenUhrzeiten(datum) {
      this.verfuegbareUhrzeitenFuerDatum = await TerminService.getAlleVerfuegbarenUhrzeiten(datum);
      this.termin.uhrzeit = this.verfuegbareUhrzeitenFuerDatum[0];
    }
  }, beforeMount: function () {
    this.getApiInformation();
  },
}
</script>

<style scoped>

</style>
<template>
  <div class="justify-content-center d-flex mt-3 container">
    <ProgressBar :active-step="this.step-1"
                 :show-label="false" :steps="alleSchritte"
                 is-reactive reactivity-type="single-step"
                 show-bridge
                 @onStepChanged="(step) => switchTo(step+1)"></ProgressBar>
  </div>
  <div class="py-3 container border d-flex justify-content-between rounded">
    <LogoText v-if="this.termin.ausgewaehlterTermin !== null && this.termin.uhrzeit !== null"
              :text="this.termin.ausgewaehlterTermin.toLocaleDateString('de') + ', ' + this.termin.uhrzeit + ':00 Uhr' "
              logo="fa-clock"></LogoText>
    <LogoText v-if="this.termin.beratungsgrund !== null"
              :text="this.termin.beratungsgrund + ', ' + (this.termin.kundeninformationen.bereitsMitglied ? 'BestandskundIn' : 'NeukundIn')"
              logo="fa-user"></LogoText>
    <LogoText v-if="this.termin.beratungsstelle !== null"
              :text="this.termin.beratungsstelle.formatToReadableString()"
              logo="fa-house"></LogoText>
  </div>
  <form class="mt-3 container rounded border p-4">

    <!-- Step1: Grund -->
    <div v-if="step === 1 ">
      <div class=" justify-content-center">
        <MultipleChoiceForm :options="alleBeratungsstellen" class="border-bottom pb-3"
                            for="beratungsstelle" header="1. Welche Beratungsstelle möchten Sie besuchen?"
                            @onselect="(option) => this.termin.beratungsstelle = option"></MultipleChoiceForm>
        <MultipleChoiceForm :options="['nein', 'ja']" class="border-bottom py-3"
                            for="bereitsMitglied"
                            header="2. Sind Sie bereits Mitglied der VLH?" inline="true"
                            @onselect="(option) => termin.kundeninformationen.bereitsMitglied = option"></MultipleChoiceForm>
        <MultipleChoiceForm :hinweis="hinweistext" :options="alleTermingruende"
                            class="pt-3" for="termingrund"
                            header="3. Worum geht es bei Ihrem Termin?"
                            @onselect="(option) => termin.beratungsgrund= option"></MultipleChoiceForm>
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
                                : 'Bitte wählen Sie ein Datum aus.'"
                            class="border-bottom pb-2"></TitleSecondary>

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
          <div class="h5 float-left pl-2 text-muted border-bottom">Termininformationen</div>
          <TextLabel
              :input="this.termin.ausgewaehlterTermin.toLocaleDateString('de') + ', '+this.termin.uhrzeit + ':00 Uhr'"
              label="Termin"></TextLabel>
          <TextLabel :input="this.termin.beratungsstelle.formatToReadableString()" label="Beratungsstelle"></TextLabel>
          <TextLabel
              :input="this.termin.beratungsgrund+ ', '+(this.termin.kundeninformationen.bereitsMitglied ? 'BestandskundIn' : 'NeukundIn')"
              label="Terminart"></TextLabel>
          <TextLabel :input="this.termin.bemerkung" label="Bemerkung"></TextLabel>

        </div>
        <div class="col-6">
          <div class="h5 float-left pl-2 text-muted border-bottom">Kundeninformationen</div>
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
      <div class="h5 mb-5">Ihr Termin wurde erfolgreich übermittelt.</div>
      <div class="my-2">Eine Terminbestätigung wird Ihnen innerhalb der nächsten 30 Minuten per E-Mail übermittelt.
        Sollten Sie Ihren
        Termin nicht wahrnehmen können, stornieren Sie ihn bitte über den in der E-Mail verfassten Link.
      </div>
      <div class="mt-5">Bei weiteren Fragen oder Anmerkungen rufen Sie uns gerne an:</div>
      <div>Beratungsstelle Preetz: 04342 7614690</div>
      <div>Beratungsstelle Eutin: 04342 7614690</div>
      <div class="my-2 mt-5">Wir freuen uns auf Ihren Termin!</div>
      <ButtonSubmit class="my-3" title="Zurück zur Terminbuchung" v-on:click="resetForm"></ButtonSubmit>
    </div>
    <SuccessBanner v-for="successMessage in success" :key="successMessage" :message="successMessage"></SuccessBanner>
    <ErrorBanner v-if="this.errors.length > 0" :message="getErrorMessage()"></ErrorBanner>
    <div v-if="(step > 1 && step < 5) || (step < 4)|| (step ===4)"
         :class="step > 1 ? 'justify-content-between' : 'justify-content-end'" class="d-flex px-4 pt-4 mx-5">
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
import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import ButtonCancel from "@/components/fragments/ButtonCancel";
import TitlePrimary from "@/components/fragments/TitlePrimary";
import MultipleChoiceForm from "@/components/compositions/MultipleChoiceForm";
import TextInput from "@/components/fragments/TextInput";
import TitleSecondary from "@/components/fragments/TitleSecondary";
import ProgressBar from "@/components/compositions/ProgressBar";
import TimePicker from "@/components/compositions/TimePicker";
import ErrorBanner from "@/components/fragments/ErrorBanner";
import TextLabel from "@/components/fragments/TextLabel";
import LogoText from "@/components/fragments/LogoText";
import SuccessBanner from "@/components/fragments/SuccessBanner";

/**
 *Diese Funktion wird verwendet, um nach Abschicken des Formulars die Daten zu löschen bzw. neu zu initialisieren, ohne, dass die Seite komplett neu geladen werden muss.
 */
function initialState() {
  return {
    step: 1,

    termin: {
      beratungsgrund: null,
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
    alleBeratungsstellen: [],
    alleTermingruende: [],
    alleBelegtenTermine: [],
    alleAnreden: [],

    hinweistext: "",
    verfuegbareUhrzeitenFuerDatum: [],
    minDate: null,
    maxDate: null,
    errors: [],
    success: []
  }
}

export default {
  name: "TerminbuchungView",
  components: {
    SuccessBanner,
    LogoText,
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
    return initialState();
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
    'termin.beratungsgrund'(newValue) {
      this.aendereHinweistext(newValue);
    },
    'termin.kundeninformationen.anrede'(newValue) {
      if (newValue === 'keine Angabe') {
        this.termin.kundeninformationen.anrede = '';
      }
    }
  },
  methods: {
    resetForm() {
      Object.assign(this.$data, initialState());
      this.success.push("Vielen Dank für Ihre Buchung! Der Termin wurde erfolgreich angelegt.")

      this.getApiInformation();
    },
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
        if (this.termin.uhrzeit.length === 1) this.termin.uhrzeit = '0' + this.termin.uhrzeit;
        this.termin.uhrzeit += ":00";
        this.$store.dispatch('addAppointment', this.termin);
        this.success.push("Vielen Dank für Ihre Buchung! Der Termin wurde erfolgreich angelegt.")
      }
    },
    switchTo(step) {
      this.errors = []
      let navigatingBackwards = step < this.step;
      // Wir wollen die Inputvalidierung nur triggern, wenn wir vorwärts navigieren
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
      if (this.termin.beratungsgrund === null && !this.alleTermingruende.find(e => e === this.termin.beratungsgrund)) {
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
      if (this.termin.kundeninformationen.anrede === null) {
        this.errors.push("Anrede")
      }
      let emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
      if (!this.istStringVorhanden(this.termin.kundeninformationen.email) || !emailRegex.test(this.termin.kundeninformationen.email)) {
        this.errors.push("E-Mail-Adresse")
      }
      let phoneNumberRegex = /^\d+$/;
      if (!this.istStringVorhanden(this.termin.kundeninformationen.telefon) || !phoneNumberRegex.test(this.termin.kundeninformationen.telefon)) {
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
     * Switch case to prevent the mitarbeiter from having to fetch all data when the view mounts.
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
            this.termin.beratungsgrund = this.alleTermingruende[0];
          }
          break;
        case 2:
          if (this.alleBelegtenTermine.length === 0) {
            this.alleBelegtenTermine = await TerminService.getKomplettBelegteDatuemer(this.termin.beratungsstelle.ansprechpartner.username);
            this.setMinAndMaxDate();
          }
          break;
        case 3:
          if (this.alleAnreden.length === 0) {
            this.alleAnreden = await BeratungsstellenService.getAlleAnreden();
            this.termin.kundeninformationen.anrede = this.alleAnreden[0]
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
      this.verfuegbareUhrzeitenFuerDatum = await TerminService.getAlleVerfuegbarenUhrzeiten(datum, this.termin.beratungsstelle.ansprechpartner.username);
      this.termin.uhrzeit = this.verfuegbareUhrzeitenFuerDatum[0];
    }
  }, beforeMount: function () {
    this.getApiInformation();
  },
}

</script>

<style scoped>

</style>
<template>
  <form class="mt-5 container bg-light rounded border p-4">

    <!-- Step1: Grund -->
    <div v-if="step === 1 ">
      <div class="mb-4 justify-content-center">
        <MultipleChoiceForm :options="alleBeratungsstellen" for="beratungsstelle"
                            header="1. Welche Beratungsstelle möchten Sie besuchen?"
                            @onselect="(option) => this.termin.beratungsstelle = option"></MultipleChoiceForm>
        <MultipleChoiceForm :options="['ja', 'nein']" for="bereitsMitglied"
                            header="2. Sind Sie bereits Mitglied der VLH?"
                            @onselect="(option) => termin.kundeninformationen.bereitsMitglied = option"></MultipleChoiceForm>
        <MultipleChoiceForm :options="alleTermingruende" for="termingrund"
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

        <div class="col">
          <div v-if="this.termin.ausgewaehlterTermin !== null">
            <MultipleChoiceForm :header="this.termin.ausgewaehlterTermin !== null
                                ? ' Ausgewähltes Datum:' + this.termin.ausgewaehlterTermin.toLocaleDateString('de-DE')
                                : 'Bitte wählen Sie ein Datum aus.'" :options="verfuegbareUhrzeitenFuerDatum"
                                for="verfuegbareUhrzeit"
                                @onselect="(option) => termin.uhrzeit=option"></MultipleChoiceForm>
            <!-- TODO Alle Termine ziehen und darstellen-->
            <div v-if="verfuegbareUhrzeitenFuerDatum.length === 0">Für diesen Tag sind keine Uhrzeiten verfügbar.
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Step3: persönliche Daten -->
    <div v-if="step === 3">
      <div class="form-group row">
        <label class="col-3">Anrede:</label>
        <select v-model="termin.kundeninformationen.anrede" class="custom-select col-6">
          <option v-for="anrede in alleAnreden" :key="anrede" :value="anrede">
            {{ anrede }}
          </option>
        </select>
      </div>

      <TextInput label="Vorname" placeholder="Vorname"
                 @oninput="(input) => this.termin.kundeninformationen.vorname = input"></TextInput>
      <TextInput label="Nachname" placeholder="Nachname"
                 @oninput="(input) => this.termin.kundeninformationen.nachname = input"></TextInput>
      <TextInput label="Telefon" placeholder="01234 56789"
                 @oninput="(input) => this.termin.kundeninformationen.telefon = input"></TextInput>
      <TextInput label="E-Mail" placeholder="max@mustermann.de"
                 type="email"
                 @oninput="(input) => this.termin.kundeninformationen.email = input"></TextInput>

      <div class="form-group row">
        <label class="col-3" for="bemerkung">Bemerkung:</label>
        <textarea id="bemerkung" v-model="termin.bemerkung" class="form-control col-6"
                  placeholder="Gibt es noch etwas, das wir wissen sollten? Teilen Sie es uns hier mit!"></textarea>
      </div>
    </div>

    <!-- Step4: Zusammenfassung -->
    <div v-if="step === 4">
      <TitleSecondary text="Bitte überprüfen Sie Ihre Angaben und bestätigen dann mit 'Buchen'."></TitleSecondary>
      <div class="mb-4 justify-content-center">
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
    <div v-if="step === 5">
      <div>Vielen Dank für Ihre Buchung!</div>
    </div>

    <ButtonCancel v-if="step > 1 && step < 5" title="Zurück" @onclick="previous"></ButtonCancel>
    <ButtonSubmit v-if="step < 4" title="Weiter" @onclick="nextStep"></ButtonSubmit>
    <ButtonSubmit v-if="step === 4" title="Buchen" @onclick="submit"></ButtonSubmit>
  </form>
</template>

<script>
import {BeratungsstellenService} from "@/services/BeratungsstellenService";
import {TerminService} from "@/services/TerminService";
import Datepicker from '@vuepic/vue-datepicker';
import ButtonSubmit from "@/components/ButtonSubmit";
import ButtonCancel from "@/components/ButtonCancel";
import TitlePrimary from "@/components/TitlePrimary";
import MultipleChoiceForm from "@/components/MultipleChoiceForm";
import TextInput from "@/components/TextInput";


export default {
  name: "AppointmentBookingView",
  components: {TextInput, MultipleChoiceForm, TitlePrimary, Datepicker, ButtonCancel, ButtonSubmit},
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
      step: 4,
      responseMessage: "No response yet.",

      // Data from API
      alleBeratungsstellen: [],
      alleTermingruende: [],
      // "anonymisiert"
      alleBelegtenTermine: [],
      alleAnreden: [],

      verfuegbareUhrzeitenFuerDatum: [],
      minDate: null,
      maxDate: null,
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
      this.termin.uhrzeit += ":00";
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

    }
  }, beforeMount: function () {
    this.getApiInformation();
  },
}
</script>

<style scoped>

</style>
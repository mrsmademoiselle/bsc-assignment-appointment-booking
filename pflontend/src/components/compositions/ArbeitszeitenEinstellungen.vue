<template>
  <div class="h5 d-flex justify-content-center font-weight-bold mb-2 py-2 col-auto">Arbeitszeiten anpassen
  </div>
  <div class="text-muted">Bitte wählen Sie die Uhrzeiten aus, zu denen Sie Termine
    zulassen möchten.
  </div>
  <div class=" form-group my-4 mx-3 container pl-3 col-auto">
    <div class="col-12">
      <ArbeitszeitenCheckbox :zeiten="this.zeiten.montag" titel="Montag"
                             @oninput="(val) => this.aktualisierteZeiten.montag = val"></ArbeitszeitenCheckbox>
      <ArbeitszeitenCheckbox :zeiten="this.zeiten.dienstag" titel="Dienstag"
                             @oninput="(val) => this.aktualisierteZeiten.dienstag = val"></ArbeitszeitenCheckbox>
      <ArbeitszeitenCheckbox :zeiten="this.zeiten.mittwoch" titel="Mittwoch"
                             @oninput="(val) => this.aktualisierteZeiten.mittwoch = val"></ArbeitszeitenCheckbox>
      <ArbeitszeitenCheckbox :zeiten="this.zeiten.donnerstag" titel="Donnerstag"
                             @oninput="(val) => this.aktualisierteZeiten.donnerstag = val"></ArbeitszeitenCheckbox>
      <ArbeitszeitenCheckbox :zeiten="this.zeiten.freitag" titel="Freitag"
                             @oninput="(val) => this.aktualisierteZeiten.freitag = val"></ArbeitszeitenCheckbox>
      <ButtonSubmit class="col-auto m-4" title="Aktualisieren" @onclick="updateArbeitszeiten"></ButtonSubmit>
      <ErrorBanner :message="getMessages(true)"></ErrorBanner>
      <SuccessBanner :message="getMessages(false)"></SuccessBanner>
    </div>
  </div>
</template>

<script>
import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import ArbeitszeitenCheckbox from "@/components/fragments/ArbeitszeitenCheckbox";
import ErrorBanner from "@/components/fragments/ErrorBanner";
import SuccessBanner from "@/components/fragments/SuccessBanner";

export default {
  name: "ArbeitszeitenEinstellungen",
  components: {SuccessBanner, ErrorBanner, ArbeitszeitenCheckbox, ButtonSubmit},
  props: ['arbeitszeiten'],
  data: function () {
    return {
      zeiten: null,
      aktualisierteZeiten: null,
      errors: [],
      successes: []
    }
  },
  methods: {
    async fetchApiData() {
    },
    getMessages(isError) {
      let message = "";
      let arrayToIterate = isError ? this.errors : this.successes;
      arrayToIterate.forEach(e => message = message + e)
      return message;
    },
    updateArbeitszeiten() {
      this.aktualisierteZeiten.mitarbeiterId = this.$store.getters.username;

      this.$store.dispatch("updateArbeitszeiten", {
        arbeitszeiten: this.aktualisierteZeiten,
        token: this.$store.getters.token
      }).then(() => {
        this.errors = []
        this.successes = []
        this.successes.push("Die Arbeitszeiten wurden erfolgreich aktualisiert.");
      })
          .catch(err => {
            this.errors.push(err)
          });
    }
  },
  beforeMount() {
    this.fetchApiData();
    this.zeiten = this.$store.getters.arbeitszeiten;
    this.aktualisierteZeiten = JSON.parse(JSON.stringify(this.zeiten));
  },
  errorCaptured: function (err) {
    console.log(err)
    this.$router.push("/")
  },
}
</script>

<style scoped>

</style>
<template>
  <div class="h5 d-flex justify-content-center font-weight-bold mb-2 py-2 col-auto">Beratungsstelle
    anlegen
  </div>
  <div class=" form-group my-4 mx-3 container pl-3 col-auto">
    <div class="col-12">
      <TextInput :input="this.beratungsstelle.adresse.strasse" label="Straße" placeholder="Straße"
                 @oninput="(val) => this.beratungsstelle.adresse.strasse = val"></TextInput>
      <TextInput :input="this.beratungsstelle.adresse.hausnummer" label="Hausnummer"
                 placeholder="Hausnummer"
                 @oninput="(val) => this.beratungsstelle.adresse.hausnummer = val"></TextInput>
      <TextInput :input="this.beratungsstelle.adresse.plz"
                 label="PLZ" placeholder="PLZ"
                 @oninput="(val) => this.beratungsstelle.adresse.plz = val"></TextInput>
      <TextInput :input="this.beratungsstelle.adresse.ort"
                 label="Ort" placeholder="Ort"
                 @oninput="(val) => this.beratungsstelle.adresse.ort = val"></TextInput>
      <div class="form-group row col-12">
        <div class="col-3">Ansprechpartner</div>
        <MitarbeiterListe class="col-6" @onselect="(selected) => selectMitarbeiter(selected)"></MitarbeiterListe>
      </div>
    </div>
    <ErrorBanner v-for="error in errors" :key="error" :message="error" class="mt-3"></ErrorBanner>
    <ButtonSubmit class="col-auto" title="Anlegen" @onclick="addAbwesenheit"></ButtonSubmit>
  </div>
  <div class="border-top mt-5 pt-5 h5 d-flex justify-content-center font-weight-bold my-4 py-2">Bestehende
    Beratungsstellen
  </div>
  <BeratungsstellenListe :beratungsstellen="alleBeratungsstellen"></BeratungsstellenListe>
</template>

<script>
import TextInput from "@/components/fragments/TextInput";
import MitarbeiterListe from "@/components/compositions/MitarbeiterListe";
import ErrorBanner from "@/components/fragments/ErrorBanner";
import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import BeratungsstellenListe from "@/components/compositions/BeratungsstellenListe";

export default {
  name: "BeratungsstellenEinstellungen",
  components: {BeratungsstellenListe, ButtonSubmit, ErrorBanner, MitarbeiterListe, TextInput},
  data: function () {
    return {
      errors: [],
      beratungsstelle: {
        ansprechpartner: {
          vorname: "",
          nachname: "",
          username: ""
        },
        adresse: {
          hausnummer: "",
          strasse: "",
          ort: "",
          plz: ""
        },
      }
    }
  }, computed: {
    alleBeratungsstellen: {
      get() {
        return this.$store.getters.alleBeratungsstellen;
      }
    }
  },
  methods: {
    selectMitarbeiter(mitarbeiter) {
      this.beratungsstelle.ansprechpartner = mitarbeiter;
    },
    addAbwesenheit() {
      if (this.checkInputValidity()) {

        this.$store.dispatch("addBeratungsstelle", {
          beratungsstelle: this.beratungsstelle,
          token: this.$store.getters.token
        }).catch((e) => {
          this.errors = [];
          this.errors.push(e)
        })
      } else {
        this.errors.push("Die Eingaben sind unvollständig.")
      }
    },
    checkInputValidity() {
      return this.istStringVorhanden(this.beratungsstelle.ansprechpartner.vorname)
          && this.istStringVorhanden(this.beratungsstelle.ansprechpartner.nachname)
          && this.istStringVorhanden(this.beratungsstelle.ansprechpartner.username)
          && this.istStringVorhanden(this.beratungsstelle.adresse.hausnummer)
          && this.istStringVorhanden(this.beratungsstelle.adresse.plz)
          && this.istStringVorhanden(this.beratungsstelle.adresse.ort)
          && this.istStringVorhanden(this.beratungsstelle.adresse.strasse);
    }, istStringVorhanden(string) {
      return string != null && string.trim().length > 0;
    }
  }
}
</script>

<style scoped>

</style>
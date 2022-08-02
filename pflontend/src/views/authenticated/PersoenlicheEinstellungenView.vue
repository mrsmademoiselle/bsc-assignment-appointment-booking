<template>
  <div class="d-flex justify-content-center mt-4 p-2 bg-light">
    <div :class="this.beratungsstellenAnzeigen ? 'tab-active' : ''" class="tab col-6 h5 p-2"
         title="Beratungsstellen"
         v-on:click="this.beratungsstellenAnzeigen = !this.beratungsstellenAnzeigen">Beratungsstellen
    </div>
    <div :class="this.beratungsstellenAnzeigen ? '' : 'tab-active'" class="tab col-6 h5 p-2"
         title="Arbeitszeiten"
         v-on:click="this.beratungsstellenAnzeigen = !this.beratungsstellenAnzeigen">
      Arbeitszeiten
    </div>
  </div>

  <div class="bg-light h-100 w-100 py-2">
    <div v-if="beratungsstellenAnzeigen">
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
    </div>
    <div v-else>Arbeitszeiten ändern</div>
  </div>
  <!--
  1. Arbeitszeiten pro Werktag

  2. Button auf Abwesenheitszeiten verweisen

  3. Beratungsstellen anlegen und sehen können
  -->
</template>

<script>
import BeratungsstellenListe from "@/components/BeratungsstellenListe";
import ErrorBanner from "@/components/banner/ErrorBanner";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";
import TextInput from "@/components/TextInput";
import MitarbeiterListe from "@/components/MitarbeiterListe";

export default {
  name: "PersoenlicheEinstellungenView",
  components: {MitarbeiterListe, TextInput, ButtonSubmit, ErrorBanner, BeratungsstellenListe},
  data: function () {
    return {
      beratungsstellenAnzeigen: true,
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
  },
  computed: {
    alleBeratungsstellen: {
      get() {
        return this.$store.getters.alleBeratungsstellen;
      }
    }
  },
  methods: {
    async getApiInformation() {
      await this.$store.dispatch("fetchBeratungsstellen");
    },
    selectMitarbeiter(mitarbeiter) {
      this.beratungsstelle.ansprechpartner = mitarbeiter;
    },
    addAbwesenheit() {
      alert(JSON.stringify(this.beratungsstelle))
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
  },
  beforeMount: function () {
    this.getApiInformation();
  }
}
</script>

<style scoped>
.tab-active {
  font-weight: bold;
}

.tab {
  cursor: pointer;
}
</style>
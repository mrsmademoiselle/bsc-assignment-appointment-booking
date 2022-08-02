<template>
  <div class="h5 d-flex justify-content-center font-weight-bold mb-2 py-2 col-auto">Beratungsstelle
    anlegen
  </div>
  <div class=" form-group my-4 mx-3 container pl-3 col-auto">
    <div class="col-12">
      <TextInput :input="this.arbeitszeiten.montag" label="Montag"
                 @oninput="(val) => this.arbeitszeiten.montag = val"></TextInput>
      <TextInput :input="this.arbeitszeiten.dienstag" label="Dienstag"
                 @oninput="(val) => this.arbeitszeiten.dienstag= val"></TextInput>
      <TextInput :input="this.arbeitszeiten.mittwoch"
                 label="Mittwoch"
                 @oninput="(val) => this.arbeitszeiten.mittwoch = val"></TextInput>
      <TextInput :input="this.arbeitszeiten.donnerstag"
                 label="Donnerstag"
                 @oninput="(val) =>this.arbeitszeiten.donnerstag= val"></TextInput>
      <TextInput :input="this.arbeitszeiten.freitag"
                 label="Freitag"
                 @oninput="(val) =>this.arbeitszeiten.freitag= val"></TextInput>
    </div>
    <ButtonSubmit class="col-auto" title="Aktualisieren" @onclick="updateArbeitszeiten"></ButtonSubmit>

  </div>
</template>

<script>
import TextInput from "@/components/TextInput";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";

export default {
  name: "ArbeitszeitenEinstellungen",
  components: {ButtonSubmit, TextInput},
  computed: {
    arbeitszeiten: {
      get() {
        return this.$store.getters.verfuegbareUhrzeitenFuerWochentage
      }
    }
  },
  methods: {
    async fetchApiData() {
      await this.$store.dispatch("fetchArbeitszeiten", this.$store.getters.username);
    },
    updateArbeitszeiten() {
      this.arbeitszeiten.username = this.$store.getters.username;

      this.$store.dispatch("updateArbeitszeiten", {
        arbeitszeiten: this.arbeitszeiten,
        token: this.$store.getters.token
      });
    }
  },
  beforeMount() {
    this.fetchApiData();
  },
  errorCaptured: function (err) {
    console.log(err)
    this.$router.push("/")
  }
}
</script>

<style scoped>

</style>
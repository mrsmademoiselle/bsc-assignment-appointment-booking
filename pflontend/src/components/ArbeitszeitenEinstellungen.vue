<template>
  <div class="h5 d-flex justify-content-center font-weight-bold mb-2 py-2 col-auto">Beratungsstelle
    anlegen
  </div>
  <div class=" form-group my-4 mx-3 container pl-3 col-auto">
    <div class="col-12">
      <TextInput :input="this.zeiten.montag" label="Montag"
                 @oninput="(val) => this.zeiten.montag = val"></TextInput>
      <TextInput :input="this.zeiten.dienstag" label="Dienstag"
                 @oninput="(val) => this.zeiten.dienstag= val"></TextInput>
      <TextInput :input="this.zeiten.mittwoch"
                 label="Mittwoch"
                 @oninput="(val) => this.zeiten.mittwoch = val"></TextInput>
      <TextInput :input="this.zeiten.donnerstag"
                 label="Donnerstag"
                 @oninput="(val) =>this.zeiten.donnerstag= val"></TextInput>
      <TextInput :input="this.zeiten.freitag"
                 label="Freitag"
                 @oninput="(val) =>this.zeiten.freitag= val"></TextInput>
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
  props: ['arbeitszeiten'],
  data: function () {
    return {
      zeiten: null
    }
  },
  methods: {
    async fetchApiData() {
    },
    updateArbeitszeiten() {
      this.zeiten.username = this.$store.getters.username;

      this.$store.dispatch("updateArbeitszeiten", {
        arbeitszeiten: this.zeiten,
        token: this.$store.getters.token
      })
          .catch((e) => {
            this.errors.push(e);
            this.error = true
          })
    }
  },
  beforeMount() {
    this.fetchApiData();
    this.zeiten = this.$store.getters.arbeitszeiten;
  },
  errorCaptured: function (err) {
    console.log(err)
    this.$router.push("/")
  },
}
</script>

<style scoped>

</style>
<template>
  <div class="justify-content-start d-flex m-3">
    <router-link to="/"> Zurück zur Übersicht</router-link>
    <div v-if="termin !== null && termin !== undefined">
      <div class="row container m-5 justify-content-center d-flex">
        <div class="col-6">
          <div class="h5 float-left pl-2 text-muted border-bottom">Termininformationen</div>
          <TextLabel
              :input="new Date(termin.ausgewaehlterTermin).toLocaleDateString() + ', '+termin.uhrzeit + ':00 Uhr'"
              label="Termin"></TextLabel>
          <TextLabel :input="termin.beratungsstelle.formatToReadableString()" label="Beratungsstelle"></TextLabel>
          <TextLabel
              :input="termin.beratungsgrund+ ', '+(termin.bereitsMitglied ? 'BestandskundIn' : 'NeukundIn')"
              label="Terminart"></TextLabel>
          <TextLabel :input="termin.bemerkung" label="Bemerkung"></TextLabel>

        </div>
        <div class="col-6">
          <div class="h5 float-left pl-2 text-muted border-bottom">Kundeninformationen</div>
          <TextLabel :input="termin.anrede + ' '+termin.vorname +
                ' '+termin.nachname"
                     label="Name"></TextLabel>
          <TextLabel :input="termin.telefon" label="Telefon"></TextLabel>
          <TextLabel :input="termin.email" label="E-Mail"></TextLabel>

        </div>
        <ButtonSubmit danger="true" title="Absagen" @onclick="absagen"></ButtonSubmit>
      </div>
    </div>
  </div>
</template>

<script>
import TextLabel from "@/components/fragments/TextLabel";
import {Termin} from "@/entity/Termin";
import ButtonSubmit from "@/components/fragments/ButtonSubmit";

export default {
  name: "TerminDetailsView",
  components: {ButtonSubmit, TextLabel},
  props: ['id'],
  data: function () {
    return {
      termin: null
    }
  },
  methods: {
    async fetchAppointments() {
      await this.$store.dispatch('fetchAppointments', this.$store.getters.token);
      // here we explicitly allow type coercion because the url is a string
      this.termin = this.$store.getters.alleTermine.find((e) => e.id == this.id)
      if (this.termin.anrede === undefined) {
        this.termin.anrede = ""
      }
      if (this.termin === null || this.termin === undefined || !Termin.hatKorrektesFormat(this.termin)) {
        console.log(" redirect to /uebersicht")
        await this.$router.push("/uebersicht")
      }
    },
    async absagen() {
      await this.$store.dispatch('removeAppointment', {id: this.id, token: this.$store.getters.token})
      await this.$router.push("/uebersicht")
    }
  },
  beforeMount() {
    this.fetchAppointments();
  }
}
</script>

<style scoped>

</style>
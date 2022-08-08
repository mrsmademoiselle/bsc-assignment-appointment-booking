<template>
  <select v-model="selectedMitarbeiter" aria-label="Mitarbeiterauswahl" class="custom-select custom-select mb-3">
    <option v-for="mitarbeiter in alleMitarbeiter" :key="mitarbeiter.username" :value="mitarbeiter">
      {{ mitarbeiter.vorname + ' ' + mitarbeiter.nachname }}
    </option>
  </select>
</template>

<script>
export default {
  name: "MitarbeiterListe",
  data: function () {
    return {
      selectedMitarbeiter: null,
    }
  },
  watch: {
    selectedMitarbeiter: function () {
      this.$emit("onselect", this.selectedMitarbeiter);
    }
  },
  computed: {
    alleMitarbeiter: {
      get() {
        return this.$store.getters.alleMitarbeiter;
      }
    }
  },
  methods: {
    async getAlleMitarbeiter() {
      await this.$store.dispatch("fetchMitarbeiter", this.$store.getters.token);
    },
    onselect() {
    }
  }, beforeMount() {
    this.getAlleMitarbeiter();
  }
}
</script>

<style scoped>

</style>
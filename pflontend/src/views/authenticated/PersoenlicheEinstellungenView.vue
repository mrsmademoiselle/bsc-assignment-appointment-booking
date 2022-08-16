<template>
  <div class="d-flex justify-content-center mt-4 p-2 bg-light">
    <div :class="this.beratungsstellenAnzeigen ? 'tab-active' : 'tab'" class="col-6 h5 p-2"
         title="Beratungsstellen"
         v-on:click="this.beratungsstellenAnzeigen = !this.beratungsstellenAnzeigen">Abwesenheitszeiten
    </div>
    <div :class="this.beratungsstellenAnzeigen ? 'tab' : 'tab-active'" class="col-6 h5 p-2"
         title="Arbeitszeiten"
         v-on:click="this.beratungsstellenAnzeigen = !this.beratungsstellenAnzeigen">
      Arbeitszeiten
    </div>
  </div>

  <div class="bg-light h-100 w-100 py-2">
    <div v-if="beratungsstellenAnzeigen">
      <AbwesenheitenView></AbwesenheitenView>
    </div>
    <div v-else>
      <ArbeitszeitenEinstellungen></ArbeitszeitenEinstellungen>
    </div>
  </div>
  <!--
  1. Arbeitszeiten pro Werktag
  -->
</template>

<script>
import ArbeitszeitenEinstellungen from "@/components/compositions/ArbeitszeitenEinstellungen";
import AbwesenheitenView from "@/components/compositions/AbwesenheitsEinstellungen";

export default {
  name: "PersoenlicheEinstellungenView",
  components: {
    AbwesenheitenView,
    ArbeitszeitenEinstellungen,
  },
  data: function () {
    return {
      beratungsstellenAnzeigen: true,
    }
  },

  methods: {
    async getApiInformation() {
      await this.$store.dispatch("fetchArbeitszeiten", this.$store.getters.username);
    },
  },
  beforeMount: function () {
    this.getApiInformation();
  }
}
</script>

<style scoped>
.tab-active {
  font-weight: bold;
  background-color: #edefff;
}

.tab:hover {
  border-bottom: solid #d0d0ff;
  cursor: pointer;
  color: white;
  background-color: #757fce;
}

.tab {
  cursor: pointer;
  background-color: #e0e0e0;
  color: #707070;
}
</style>
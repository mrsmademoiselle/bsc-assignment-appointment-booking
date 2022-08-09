<template>
  <div class=" mx-3">
    <div class="justify-content-center d-flex mt-3">
      <table v-if="beratungsstellen.length > 0" class="col-lg-8 table">
        <thead>
        <tr>
          <th scope="col">Nummer</th>
          <th scope="col">Ansprechpartner</th>
          <th scope="col">Adresse</th>
          <th scope="col">Archivieren (unwiderruflich)</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(beratungsstelle,i) in beratungsstellen" :key="beratungsstelle.id">
          <td>#{{ i + 1 }}</td>
          <td>{{ beratungsstelle.ansprechpartner.vorname + ' ' + beratungsstelle.ansprechpartner.nachname }}</td>
          <td>{{ beratungsstelle.adresse.formatToReadableString() }}</td>
          <td>
            <ButtonSubmit danger="true" data-target="#myModal" data-toggle="modal"
                          title="x" type="button" @onclick="selectBeratungsstelle(beratungsstelle)">
            </ButtonSubmit>
          </td>
        </tr>
        </tbody>
      </table>
      <div v-else>Es gibt derzeit keine Beratungsstellen.</div>

    </div>

    <!-- Modal -->
    <div v-if="selectedBeratungsstelle !== null" id="myModal" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header justify-content-center">
            <h4 class="modal-title">Beratungsstelle löschen</h4>
          </div>
          <div class="modal-body">
            <div>Möchten Sie diese Beratungsstelle wirklich löschen?</div>
            <div class="my-4">{{
                selectedBeratungsstelle.ansprechpartner.vorname + ' ' + selectedBeratungsstelle.ansprechpartner.nachname
              }}
            </div>
            <div class="mb-4">{{ selectedBeratungsstelle.adresse.formatToReadableString() }}</div>
            <div>Diese Aktion kann nicht rückgängig gemacht werden.</div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal" type="button">Abbrechen</button>
            <button class="btn btn-danger" data-dismiss="modal" type="button" v-on:click="deleteBeratungsstelle">Ja,
              löschen
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ButtonSubmit from "@/components/fragments/ButtonSubmit";

export default {
  name: "BeratungsstellenListe",
  components: {ButtonSubmit},
  props: ['beratungsstellen'],
  data: function () {
    return {
      selectedBeratungsstelle: null
    }
  },
  methods: {
    selectBeratungsstelle(beratungsstelle) {
      this.selectedBeratungsstelle = beratungsstelle;
    },
    deleteBeratungsstelle() {
      this.$store.dispatch("archiviereBeratungsstelle", {
        id: this.selectedBeratungsstelle.id,
        token: this.$store.getters.token
      })
    }
  }
}
</script>

<style scoped>

</style>
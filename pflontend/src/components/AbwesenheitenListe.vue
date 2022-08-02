<template>
  <div class=" mx-3">
    <div class="border-bottom h5 d-flex justify-content-start mb-3">Liste aller Abwesenheiten</div>
    <div class="justify-content-center d-flex mt-3">
      <table v-if="abwesenheitsliste.length > 0" class="col-lg-8 table">
        <thead>
        <tr>
          <th scope="col">Nummer</th>
          <th scope="col">Startdatum</th>
          <th scope="col">Enddatum</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(abwesenheit,i) in abwesenheitsliste" :key="abwesenheit.id">
          <td>#{{ i + 1 }}</td>
          <td>{{ new Date(abwesenheit.startDatum).toLocaleDateString() }}</td>
          <td>{{ new Date(abwesenheit.endDatum).toLocaleDateString() }}</td>
          <td>
            <ButtonSubmit danger="true" data-target="#myModal" data-toggle="modal"
                          title="x" type="button" @onclick="selectAbwesenheit(abwesenheit)">
            </ButtonSubmit>
          </td>
        </tr>
        </tbody>
      </table>
      <div v-else>Es gibt derzeit keine Abwesenheitseinträge.</div>

    </div>

    <!-- Modal -->
    <div v-if="selectedAbwesenheit !== null" id="myModal" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header justify-content-center">
            <h4 class="modal-title">Abwesenheit löschen</h4>
          </div>
          <div class="modal-body">
            <div>Möchten Sie diese Abwesenheit wirklich löschen?</div>
            <div class="my-4">{{ new Date(selectedAbwesenheit.startDatum).toLocaleDateString() }} -
              {{ new Date(selectedAbwesenheit.endDatum).toLocaleDateString() }}
            </div>
            <div>{{ selectedAbwesenheit.vorname }} {{ selectedAbwesenheit.nachname }}</div>
            <div>Diese Aktion kann nicht rückgängig gemacht werden.</div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal" type="button">Abbrechen</button>
            <button class="btn btn-danger" data-dismiss="modal" type="button" v-on:click="deleteAbwesenheit">Ja, löschen
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import ButtonSubmit from "@/components/buttons/ButtonSubmit";

export default {
  name: "AbwesenheitenListe",
  components: {ButtonSubmit},
  props: ['abwesenheitsliste'],
  data: function () {
    return {
      selectedAbwesenheit: null
    }
  },
  methods: {
    selectAbwesenheit(abwesenheit) {
      this.selectedAbwesenheit = abwesenheit;
    },
    deleteAbwesenheit() {
      this.$store.dispatch('removeAbwesenheit', {id: this.selectedAbwesenheit.id, token: this.$store.getters.token});
    }
  },
  mounted() {
    alert(JSON.stringify(this.abwesenheitsliste))
  }
}
</script>

<style scoped>

</style>
<template>
  <div class="container">
    <div class="form-group my-5 mx-3">
      <div class="border-bottom h5 d-flex justify-content-start mb-3">Neue Abwesenheit anlegen</div>
      <div class="row">
        <div class="col-auto"><b>Von: </b></div>
        <div class="col-auto">hi</div>
      </div>
      <div class="row">
        <div class="col-auto"><b>Bis: </b></div>
        <div class="col-auto">hi</div>
      </div>
      <ButtonSubmit class="row col-auto" title="Anlegen"></ButtonSubmit>
    </div>
    <div class="my-3">
      <AbwesenheitenListe :abwesenheitsliste="abwesenheitsliste"></AbwesenheitenListe>
    </div>
  </div>
</template>

<script>
import AbwesenheitenListe from "@/components/AbwesenheitenListe";
import ButtonSubmit from "@/components/buttons/ButtonSubmit";

export default {
  name: "AbwesenheitenView",
  components: {ButtonSubmit, AbwesenheitenListe},
  data: function () {
    return {
      abwesenheitsliste: [],
      neueAbwesenheit: {
        startDatum: null,
        endDatum: null
      }
    }
  }, beforeMount() {

  }
}
</script>

<style scoped>

</style>
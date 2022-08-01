<template>
  <div class=" mx-3">
    <div class="border-bottom h5 d-flex justify-content-start mb-3">Liste aller Abwesenheiten</div>
    <div v-for="abwesenheit in abwesenheitsliste" :key="abwesenheit">{{ abwesenheit }}</div>
  </div>
</template>

<script>
export default {
  name: "AbwesenheitenListe",
  props: ['abwesenheitsliste']
}
</script>

<style scoped>

</style>
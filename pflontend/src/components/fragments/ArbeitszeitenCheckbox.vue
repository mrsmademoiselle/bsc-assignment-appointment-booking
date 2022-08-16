<template>
  <div class="row border-bottom py-2 mx-5">
    <div class="h5 col-auto mr-auto mb-auto">{{ titel }}</div>
    <div v-for="uhrzeit in standard" :key="uhrzeit" class="form-check form-check-inline col-auto">
      <input :id="uhrzeit" v-model="ausgewahlteUhrzeiten" :value="uhrzeit" class="form-check-input" type="checkbox"
             v-on:change="emitteAusgewaehlteUhrzeiten"/>
      <label :for="uhrzeit" class="form-check-label">{{ uhrzeit }} Uhr</label>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArbeitszeitenCheckbox",
  props: ['titel', 'zeiten'],
  data: function () {
    return {
      ausgewahlteUhrzeiten: this.zeiten,
      standard: [8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
    }
  },
  methods: {
    emitteAusgewaehlteUhrzeiten() {
      console.log(this.ausgewahlteUhrzeiten)
      this.$emit('oninput', this.ausgewahlteUhrzeiten);
    }
  }
}
</script>

<style scoped>

</style>
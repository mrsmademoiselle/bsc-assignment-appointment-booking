<template>
  <div class="d-flex justify-content-start container p-2">
    <div><b>{{ label }}:</b></div>
    <div>&nbsp;{{ input }}</div>
  </div>
</template>

<script>
import {Beratungsstelle} from "@/entity/Beratungsstelle";

export default {
  name: "TextLabel",
  props: ['label', 'input'],
  methods: {
    formatInput() {
      if (Beratungsstelle.isValidBeratungsstelle(this.input)) {
        return this.input.formatToReadableString();
      } else if (this.input instanceof Date) {
        return this.input.toDateString();
      } else {
        return this.input;
      }
    }
  }
}
</script>

<style scoped>

</style>
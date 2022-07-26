<template>
  <TitleSecondary :text="header"></TitleSecondary>
  <div v-for="option in options" :key="option.id"
       class="form-check row">
    <input id="{{this.for}}" class="form-check-input" name="{{this.for}}"
           type="radio" v-on:input="() => onselect(option)">
    <label class="form-check-label" for="{{this.for}}">
      {{ displayLabel(option) }}
    </label>
  </div>
</template>

<script>
import TitleSecondary from "@/components/TitleSecondary";

export default {
  name: "MultipleChoiceForm",
  components: {TitleSecondary},
  props: ['options', 'for', 'header'],
  methods: {
    onselect(option) {
      this.$emit('onselect', option)
    },
    displayLabel(option) {
      if (this.for === 'beratungsstelle') {
        return option.formatToReadableString();
      } else {
        return option;
      }
    }
  }
}

</script>

<style scoped>

</style>
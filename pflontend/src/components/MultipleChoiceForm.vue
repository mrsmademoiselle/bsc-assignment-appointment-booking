<template>
  <div class="d-flex justify-content-start mx-3 my-1 pl-3">
    <div class="col-6 row">
      <TitleSecondary :muted="true" :text="header"></TitleSecondary>
      <div v-for="option in options" :key="option.id"
           class="form-check row col-12 justify-content-start d-flex mx-3 pb-1">
        <input id="{{this.for}}" class="form-check-input" name="{{this.for}}"
               type="radio" v-on:input="() => onselect(option)">
        <label class="form-check-label" for="{{this.for}}">
          {{ displayLabel(option) }}
        </label>
      </div>
    </div>
    <div v-if="hinweis" class="border-left col-6 hinweis-text  d-flex justify-content-start">
      <small><b>Hinweis: </b> {{ hinweis }}</small>
    </div>
  </div>
</template>

<script>
import TitleSecondary from "@/components/titles/TitleSecondary";

export default {
  name: "MultipleChoiceForm",
  components: {TitleSecondary},
  props: ['options', 'for', 'header', 'hinweis'],
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
.hinweis-text {
  padding: 30px 30px 30px 70px;
  margin: 50px 50px 30px 30px;
}
</style>
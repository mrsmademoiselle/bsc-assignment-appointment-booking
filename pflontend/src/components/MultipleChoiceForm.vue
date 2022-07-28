<template>
  <div class="d-flex justify-content-start mx-3 pl-3">
    <div class="row d-flex">
      <TitleSecondary :muted="true" :text="header"></TitleSecondary>
      <div v-for="(option,i) in options" :key="option.id"
           :class="(inline ? 'col-auto mt-3 pl-5 ml-5' : 'col-12')"
           class="form-check justify-content-start d-inline-flex mx-3">
        <input :id="this.for" :checked="i ===0" :name="this.for" class="form-check-input pb-1"
               type="radio" v-on:input="() => onselect(option)">
        <label :for="this.for" class="form-check-label pb-2">
          {{ displayLabel(option) }}
        </label>
      </div>
    </div>
    <div v-if="this.hinweis && this.hinweis.length > 0"
         class="border-left col-6 hinweis-text py-4 d-flex justify-content-start">
      <small><b>Hinweis: </b> {{ hinweis }}</small>
    </div>
  </div>
</template>

<script>
import TitleSecondary from "@/components/titles/TitleSecondary";

export default {
  name: "MultipleChoiceForm",
  components: {TitleSecondary},
  props: ['options', 'for', 'header', 'hinweis', 'inline'],
  methods: {
    onselect(option) {
      if (this.for === 'bereitsMitglied') {
        option = option === 'ja';
      }
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
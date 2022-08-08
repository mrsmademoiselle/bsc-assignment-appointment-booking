<template>
  <div class="form-group row">
    <label :for="this.label" class="col-3">{{ label }}:</label>
    <input :id="this.label" v-model="inputValue" :placeholder="this.placeholder" :type="this.type ? type : 'text'"
           class="form-control col-6"
           v-on:input="oninput">
  </div>
</template>

<script>
export default {
  name: "TextInput",
  props: ['label', 'placeholder', 'type', 'input'],
  data: function () {
    return {
      inputValue: this.input
    }
  },
  methods: {
    oninput() {
      this.$emit('oninput', this.inputValue)
    },
  }
}
</script>

<style scoped>

</style>
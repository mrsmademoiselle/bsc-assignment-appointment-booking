<template>
  <div class="form-group row">
    <label :for="this.label" class="col-3">{{ label }}:</label>
    <input :id="this.label" :placeholder="this.placeholder"
           :type="this.type ? type : 'text'" class="form-control col-6"
           v-on:input="oninput">
  </div>
</template>

<script>
export default {
  name: "TextInput",
  props: ['label', 'placeholder', 'type'],
  methods: {
    oninput(option) {
      this.$emit('oninput', option)
    },
  }
}
</script>

<style scoped>

</style>
<template>
  <div class="mt-3 px-3">
    <font-awesome-icon v-if="this.time < this.times.length-1" class="pointer" icon="fa-angle-up"
                       v-on:click="up"/>
    <div class="h3" v-on:click="openOverlay">{{ times[time] }}:00 Uhr</div>
    <font-awesome-icon v-if="this.time > 0" class="pointer" icon="fa-angle-down" v-on:click="down"/>
  </div>
</template>

<script>
export default {
  name: "TimePicker",
  props: {
    times: Array
  },
  data: function () {
    return {
      time: 0
    }
  },
  methods: {
    down() {
      if (this.time > 0) {
        this.time = this.time - 1
        this.$emit('onselect', this.times[this.time])
      }
    },
    up() {
      if (this.time < this.times.length - 1) {
        this.time = this.time + 1
        this.$emit('onselect', this.times[this.time])
      }
    },
    openOverlay() {
      alert("Overlay popped!")
    }
  }
}
</script>

<style scoped>
.pointer {
  cursor: pointer
}
</style>
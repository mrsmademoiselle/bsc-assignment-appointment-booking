<template>
  <div class="mb-4 pb-4">Bitte beachten Sie, dass nur eine stundenweise Buchung möglich ist.</div>
  <div :title="'Aktuell ausgewählte Uhrzeit: '+this.times[this.time]+'Uhr'" class="mt-3 px-3">
    <font-awesome-icon v-if="this.time < this.times.length-1" class="pointer" icon="fa-angle-up"
                       title="Nächste Stunde" v-on:click="plusEins"/>
    <div v-if="show" class="h3" v-on:click="openOverlay">{{ times[time] }}:00 Uhr</div>
    <div v-else>
      <div class="input-icons">
        <input id="time" v-model="this.input" class="input-field h5"/>
        <font-awesome-icon class="pointer col-auto font-weight-bold icon"
                           icon="fa-check"
                           v-on:click="validateTimeInput"/>
        <label class="col-auto h3 ml-5" for="time"> Uhr</label>
      </div>
    </div>

    <font-awesome-icon v-if="this.time > 0" class="pointer" icon="fa-angle-down" title="Vorherige Stunde"
                       v-on:click="minusEins"/>
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
      time: 0,
      show: true,
      input: this.times[this.time]
    }
  },
  methods: {
    minusEins() {
      if (this.time > 0) {
        this.time = this.time - 1
        this.input = this.times[this.time]
        this.$emit('onselect', this.times[this.time])
      }
    },
    plusEins() {
      if (this.time < this.times.length - 1) {
        this.time = this.time + 1
        this.input = this.times[this.time]
        this.$emit('onselect', this.times[this.time])
      }
    },
    openOverlay() {
      this.show = !this.show
    },
    validateTimeInput() {
      let timeIndex = this.times.findIndex(e => e === this.input);
      let isValid = timeIndex !== -1;
      if (isValid) {
        this.time = timeIndex;
        this.$emit('onselect', this.times[timeIndex])
        this.openOverlay();
      } else {
        console.log("error")
      }
    }
  }
}
</script>

<style scoped>
.pointer {
  cursor: pointer
}

.input-icons .icon {
  position: absolute;

}

.input-icons {
  width: 100%;
  margin-bottom: 10px;
}

.icon {
  padding: 15px;
  text-align: center;
  outline: 1px green solid;
  border-radius: 5px;
  margin-left: 5px;
  background-color: #70d070;
  color: white;
}

.input-field {
  width: 40%;
  padding: 10px;
  text-align: center;
}

h2 {
  color: green;
}
</style>
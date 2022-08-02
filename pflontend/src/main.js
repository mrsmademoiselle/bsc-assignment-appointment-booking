import {createApp} from 'vue'
import App from './App.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import store from './store'

import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import router from "@/router";
import {library} from "@fortawesome/fontawesome-svg-core";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faAngleUp} from "@fortawesome/free-solid-svg-icons/faAngleUp";
import {faAngleDown} from "@fortawesome/free-solid-svg-icons/faAngleDown";
import {faClock, faHouse, faPerson, faUser} from "@fortawesome/free-solid-svg-icons";

const app = createApp(App)
app.use(router)
app.use(store)
library.add(faAngleUp)
library.add(faAngleDown)
library.add(faClock)
library.add(faPerson)
library.add(faHouse)
library.add(faUser)
// eslint-disable-next-line vue/multi-word-component-names
app.component('Datepicker', Datepicker);
app.component("font-awesome-icon", FontAwesomeIcon)
app.mount("#app");

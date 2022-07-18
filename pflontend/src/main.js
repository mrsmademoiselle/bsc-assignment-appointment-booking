import {createApp} from 'vue'
import App from './App.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import store from './store'

import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import router from "@/router";

const app = createApp(App)
app.use(router)
app.use(store)

// eslint-disable-next-line vue/multi-word-component-names
app.component('Datepicker', Datepicker);
app.mount("#app");

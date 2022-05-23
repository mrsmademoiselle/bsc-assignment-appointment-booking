import {createApp} from 'vue'
import App from './App.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import {createRouter, createWebHistory} from "vue-router";
import HomepageClient from "@/views/HomepageClientView";
import AppointmentBooking from "@/views/AppointmentBookingView";
import LoginView from "@/views/LoginView";
import AppointmentOverview from "@/views/authenticated/AppointmentOverview";
import AppointmentDetails from "@/views/authenticated/AppointmentDetails";
import AppointmentCancellation from "@/views/AppointmentCancellation";

// 2. Define some routes
const routes = [
    {path: '/', component: AppointmentOverview},
    {path: '/home', component: HomepageClient},
    {path: '/login', component: LoginView},
    {path: '/book-appointment', component: AppointmentBooking},
    {path: '/appointment/:id', component: AppointmentDetails},
    {path: '/appointment/cancel/:id', component: AppointmentCancellation},
]

// 3. Create the router instance and pass the `routes` option
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})

const app = createApp(App)
app.use(router)
app.mount("#app");

// 2. Define some routes
import AppointmentOverview from "@/views/authenticated/AppointmentOverview";
import HomepageClient from "@/views/HomepageClientView";
import LoginView from "@/views/LoginView";
import AppointmentBookingView from "@/views/AppointmentBookingView";
import AppointmentDetails from "@/views/authenticated/AppointmentDetails";
import AppointmentCancellation from "@/views/AppointmentCancellation";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {path: '/', component: AppointmentOverview},
    {path: '/home', component: HomepageClient},
    {path: '/login', component: LoginView},
    {path: '/book-appointment', component: AppointmentBookingView},
    {path: '/appointment/:id', component: AppointmentDetails},
    {path: '/appointment/cancel/:id', component: AppointmentCancellation},
]

// 3. Create the router instance and pass the `routes` option
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})

export default router;
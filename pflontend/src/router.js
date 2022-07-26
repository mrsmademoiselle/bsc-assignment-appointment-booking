// 2. Define some routes
import AppointmentOverview from "@/views/authenticated/AppointmentOverview";
import HomepageClient from "@/views/HomepageClientView";
import LoginView from "@/views/LoginView";
import AppointmentBookingView from "@/views/AppointmentBookingView";
import AppointmentDetails from "@/views/authenticated/AppointmentDetails";
import AppointmentCancellation from "@/views/AppointmentCancellationView";
import {createRouter, createWebHistory} from "vue-router";
import store from "@/store";

const routes = [
    {path: '/', component: HomepageClient},
    {path: '/login', component: LoginView},
    {path: '/book-appointment', component: AppointmentBookingView},
    {path: '/appointment/cancel/:id', component: AppointmentCancellation},
    {path: '/overview', component: AppointmentOverview, meta: {requiresAuthentication: true}},
    {path: '/appointment/:id', component: AppointmentDetails, meta: {requiresAuthentication: true}},
]

// 3. Create the router instance and pass the `routes` option
const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes: routes,
})

// Leitet einen Nutzer auf /login weiter, wenn er dabei ist, unauthorisiert auf eine Seite zuzugreifen
router.beforeEach((to, from, next) => {
    if (to.matched.some(route => route.meta.requiresAuthentication)) {
        store.dispatch('checkValidityOfToken', store.getters.token).then(() => {
            if (!store.getters.token) {
                console.log("Is not logged in! Redirecting to /login.")
                next({path: '/login'})
            } else {
                next();
            }
        })
    } else {
        next();
    }
})

export default router;
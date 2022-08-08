import TerminUebersichtView from "@/views/authenticated/TerminUebersichtView";
import HomepageClient from "@/views/HomepageClientView";
import LoginView from "@/views/LoginView";
import TerminbuchungView from "@/views/TerminbuchungView";
import TerminDetailsView from "@/views/authenticated/TerminDetailsView";
import TerminabsageView from "@/views/TerminabsageView";
import {createRouter, createWebHistory} from "vue-router";
import store from "@/store";
import AbwesenheitenView from "@/views/authenticated/AbwesenheitenView";
import PersoenlicheEinstellungenView from "@/views/authenticated/PersoenlicheEinstellungenView";

const routes = [
    {path: '/', component: HomepageClient},
    {path: '/login', component: LoginView},
    {path: '/termin-buchen', component: TerminbuchungView},
    {path: '/termin/absage/:id', component: TerminabsageView},
    {path: '/uebersicht', component: TerminUebersichtView, meta: {requiresAuthentication: true}},
    {path: '/appointment/:id', component: TerminDetailsView, props: true, meta: {requiresAuthentication: true}},
    {path: '/abwesenheit', component: AbwesenheitenView, meta: {requiresAuthentication: true}},
    {path: '/einstellungen', component: PersoenlicheEinstellungenView, meta: {requiresAuthentication: true}},
]

// Router-Instanz erstellen
const router = createRouter({
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
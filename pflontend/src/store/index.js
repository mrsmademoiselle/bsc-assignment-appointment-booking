import {createStore} from 'vuex'
import authentication from "@/store/modules/authentication";
import appointments from "@/store/modules/appointments";
import abwesenheiten from "@/store/modules/abwesenheiten";
import beratungsstellen from "@/store/modules/beratungsstellen";

export default createStore({
    modules: {
        authentication,
        appointments,
        abwesenheiten,
        beratungsstellen
    },
})
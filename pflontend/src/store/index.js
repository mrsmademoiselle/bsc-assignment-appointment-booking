import {createStore} from 'vuex'
import authentication from "@/store/modules/authentication";
import appointments from "@/store/modules/appointments";
import abwesenheiten from "@/store/modules/abwesenheiten";

export default createStore({
    modules: {
        authentication,
        appointments,
        abwesenheiten
    },
})
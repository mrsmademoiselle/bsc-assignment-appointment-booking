import {createStore} from 'vuex'
import authentication from "@/store/modules/authentication";
import termine from "@/store/modules/termine";
import abwesenheiten from "@/store/modules/abwesenheiten";
import beratungsstellen from "@/store/modules/beratungsstellen";
import mitarbeiter from "@/store/modules/mitarbeiter";
import einstellungen from "@/store/modules/einstellungen";

export default createStore({
    modules: {
        authentication,
        appointments: termine,
        abwesenheiten,
        beratungsstellen,
        mitarbeiter,
        einstellungen
    },
})
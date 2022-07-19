import {createStore} from 'vuex'
import authentication from "@/store/modules/authentication";
import appointments from "@/store/modules/appointments";

export default createStore({
    modules: {
        authentication,
        appointments
    },
})
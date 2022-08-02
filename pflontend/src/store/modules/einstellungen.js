import apiService from "@/services/ApiService";
import {Arbeitszeiten} from "@/entity/Arbeitszeiten";

const state = () => ({
    verfuegbareUhrzeitenFuerWochentage: null
})

// getters
const getters = {
    verfuegbareUhrzeitenFuerWochentage: state => {
        return state.verfuegbareUhrzeitenFuerWochentage;
    }
}

const actions = { // asynchronous
    async fetchArbeitszeiten({commit}, username) {
        if (username.trim().length > 0) {
            console.log("Fetching arbeitszeiten vom server")
            let response = await apiService.get(username + "/uhrzeiten/get/all");

            if (response.status === 200) {
                commit('fetchArbeitszeiten', response.data);
            }
        }
    },
    updateArbeitszeiten({commit}, {arbeitszeiten, token}) {
        return new Promise((resolve, reject) => {
            console.log("addBeratungsstelle (token: " + token + "): " + JSON.stringify(arbeitszeiten))
            apiService.authenticatedPost("/admin/uhrzeiten/post", arbeitszeiten, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("arbeitszeiten erfolgreich geupdated");
                        commit('updateArbeitszeiten', new Arbeitszeiten(response.data));
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Die Arbeitszeiten konnten nicht angelegt werden.")
                })
        });
    }
}

// mutations
const mutations = { // synchronous
    fetchArbeitszeiten(state, data) {
        alert(JSON.stringify(data))
        state.verfuegbareUhrzeitenFuerWochentage = new Arbeitszeiten(data);
    },
    updateArbeitszeiten(state, zeiten) {
        state.verfuegbareUhrzeitenFuerWochentage = zeiten;
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
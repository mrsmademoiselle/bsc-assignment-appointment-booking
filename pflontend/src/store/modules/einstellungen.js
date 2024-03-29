import apiService from "@/services/ApiService";
import {Arbeitszeiten} from "@/entity/Arbeitszeiten";

const state = () => ({
    arbeitszeiten: null
})

// getters
const getters = {
    arbeitszeiten: state => {
        return state.arbeitszeiten;
    }
}

const actions = { // asynchronous
    async fetchArbeitszeiten({commit}, username) {
        if (username.trim().length > 0) {
            console.log("Hole Arbeitszeiten vom Server")
            let response = await apiService.get(username + "/uhrzeiten/get/all");

            if (response.status === 200) {
                commit('fetchArbeitszeiten', response.data);
            }
        }
    },
    updateArbeitszeiten({commit}, {arbeitszeiten, token}) {
        return new Promise((resolve, reject) => {
            console.log("Arbeitszeiten aktualisieren")
            apiService.authenticatedPost("/admin/uhrzeiten/post", arbeitszeiten, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("Arbeitszeiten erfolgreich aktualisiert");
                        commit('updateArbeitszeiten', new Arbeitszeiten(response.data));
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Die Arbeitszeiten konnten nicht aktualisiert werden.")
                })
        });
    }
}

// mutations
const mutations = { // synchronous
    fetchArbeitszeiten(state, data) {
        state.arbeitszeiten = new Arbeitszeiten(data);
    },
    updateArbeitszeiten(state, zeiten) {
        state.arbeitszeiten = zeiten;
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
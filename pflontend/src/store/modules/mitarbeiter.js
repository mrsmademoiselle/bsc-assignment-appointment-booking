import apiService from "@/services/ApiService";
import {Mitarbeiter} from "@/entity/Mitarbeiter";

const state = () => ({
    alleMitarbeiter: []
})

// getters
const getters = {
    alleMitarbeiter: state => {
        return state.alleMitarbeiter;
    }
}

const actions = { // asynchronous
    async fetchMitarbeiter({commit}, token) {
        console.log("Fetching alleMitarbeiter vom server")
        let response = await apiService.authenticatedGet("/mitarbeiter/get/all", token);

        if (response.status === 200) {
            commit('fetchMitarbeiter', response.data);
        }
    },
    addMitarbeiter({commit}, {mitarbeiter, token}) {
        return new Promise((resolve, reject) => {

            console.log("addBeratungsstelle (token: " + token + "): " + JSON.stringify(mitarbeiter))
            apiService.authenticatedPost("/mitarbeiter/add", mitarbeiter, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("Beratungsstelle erfolgreich angelegt");
                        commit('addMitarbeiter', new Mitarbeiter(response.data));
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Der Mitarbeiter konnte nicht angelegt werden.")
                })
        });
    }
}

// mutations
const mutations = { // synchronous
    fetchMitarbeiter(state, data) {
        let mitarbeitende = [];
        data.forEach(mitarbeiter => {
            let b1 = new Mitarbeiter(mitarbeiter);
            mitarbeitende.push(b1);
        });
        state.alleMitarbeiter = mitarbeitende;
    },
    addMitarbeiter(state, mitarbeiter) {
        state.alleMitarbeiter = [...state.alleMitarbeiter, mitarbeiter];
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
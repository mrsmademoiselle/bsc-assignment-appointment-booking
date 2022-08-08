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
        console.log("Hole alle Mitarbeiter vom Server")
        let response = await apiService.authenticatedGet("/mitarbeiter/get/all", token);

        if (response.status === 200) {
            commit('fetchMitarbeiter', response.data);
        }
    },
    addMitarbeiter({commit}, {mitarbeiter, token}) {
        return new Promise((resolve, reject) => {

            console.log("Fuege Mitarbeiter hinzu")
            apiService.authenticatedPost("/mitarbeiter/add", mitarbeiter, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("Mitarbeiter erfolgreich angelegt");
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
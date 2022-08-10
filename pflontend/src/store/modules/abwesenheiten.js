import apiService from "@/services/ApiService";
import {Abwesenheit} from "@/entity/Abwesenheit";

const state = () => ({
    alleAbwesenheiten: []
})

// getters
const getters = {
    alleAbwesenheiten: state => {
        return state.alleAbwesenheiten;
    }
}

const actions = { // asynchronous
    async fetchAbwesenheiten({commit}, {username, token}) {
        console.log("Hole alle Abwesenheiten vom Server")
        let response = await apiService.authenticatedGet("abwesenheit/get/all/" + username, token);

        if (response.status === 200) {
            commit('fetchAbwesenheiten', response.data);
        }
    },
    async removeAbwesenheit({commit}, {id, token}) {
        console.log("Entferne Abwesenheitseintrag mit ID " + id)

        let response = await apiService.authenticatedPost("abwesenheit/remove/" + id, {}, token);
        if (response.status === 200) {
            commit('removeAbwesenheit', id);
        }
    },
    addAbwesenheit({commit}, {abwesenheit, token}) {
        return new Promise((resolve, reject) => {

            console.log("Füge Abwesenheitseintrag hinzu")
            apiService.authenticatedPost("abwesenheit/add", abwesenheit, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("Abwesenheit erfolgreich angelegt");
                        commit('addAbw', new Abwesenheit(response.data));
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Der Abwesenheitseintrag konnte nicht angelegt werden. Bitte stellen Sie sicher, dass in dem Zeitraum keine Abwesenheitsanträge und Termine existieren.")
                })
        });
    }
}

// mutations
const mutations = { // synchronous
    fetchAbwesenheiten(state, data) {
        let abwesenheiten = [];
        data.forEach(abwesenheit => {
            let a = new Abwesenheit(abwesenheit);
            abwesenheiten.push(a);
        });
        state.alleAbwesenheiten = abwesenheiten;
    },
    removeAbwesenheit(state, id) {
        state.alleAbwesenheiten = state.alleAbwesenheiten.filter(abwesenheit => abwesenheit.id !== id);
    },
    addAbw(state, abwesenheit) {
        state.alleAbwesenheiten = [...state.alleAbwesenheiten, abwesenheit];
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
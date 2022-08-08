import apiService from "@/services/ApiService";
import {Termin} from "@/entity/Termin";

const state = () => ({
    alleTermine: []
})

// getters
const getters = {
    alleTermine: state => {
        return state.alleTermine;
    }
}

const actions = { // asynchronous
    async fetchAppointments({commit}, token) {
        console.log("Hole alle Termine vom Server")
        let response = await apiService.authenticatedGet("termin/get/all", token);

        if (response.status === 200) {
            commit('fetch', response.data);
        }
    },
    async removeAppointment({commit}, {id, token}) {
        console.log("Entferne Termin mit ID " + id)

        let response = await apiService.authenticatedPost("termin/cancel/" + id, {}, token);
        if (response.status === 200) {
            commit('remove', id);
        }
    },
    addAppointment({commit}, appointment) {
        return new Promise((resolve, reject) => {

            console.log("Füge Termin hinzu");
            apiService.post("termin/post", appointment).then(response => {
                if (response.status === 200) {
                    console.log("Termin erfolgreich angelegt");
                    commit('add', new Termin(response.data));
                }
                resolve()
            })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Der Termin konnte nicht angelegt werden.")
                })
        })
    }
}

// mutations
const mutations = { // synchronous
    fetch(state, data) {
        let termine = [];
        data.forEach(t => {
            let termin = new Termin(t);
            termine.push(termin);
        });
        state.alleTermine = termine;
    },
    remove(state, id) {
        state.alleTermine = state.alleTermine.filter(termin => termin.id !== id);
    },
    add(state, appointment) {
        state.alleTermine = [...state.alleTermine, appointment];
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
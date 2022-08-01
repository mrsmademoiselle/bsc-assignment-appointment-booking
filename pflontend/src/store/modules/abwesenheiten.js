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
    async fetchAbwesenheiten({commit}, token) {
        console.log("Fetching alle Abwesenheiten vom server")
        let response = await apiService.authenticatedGet("abwesenheit/get/all", token);

        if (response.status === 200) {
            commit('fetch', response.data);
        }
    },
    async removeAbwesenheit({commit}, {id, token}) {
        console.log("Removing abwesenheit with id " + id)

        let response = await apiService.authenticatedPost("abwesenheit/remove/" + id, {}, token);
        if (response.status === 200) {
            commit('remove', id);
        }
    },
    async addAbwesenheit({commit}, {abwesenheit, token}) {
        console.log("Adding abwesenheit");
        let response = await apiService.authenticatedPost("abwesenheit/add", abwesenheit, token);
        console.log("Response: '" + response.data + "' with Statuscode " + response.status);
        if (response.status === 200) {
            commit('add', abwesenheit);
        }
    }
}

// mutations
const mutations = { // synchronous
    fetch(state, data) {
        let alleAbwesenheiten = [];
        data.forEach(abwesenheit => {
            let appointment1 = new Abwesenheit(abwesenheit);
            alleAbwesenheiten.push(appointment1);
        });
        state.alleAbwesenheiten = alleAbwesenheiten;
    },
    remove(state, id) {
        state.alleAbwesenheiten = state.alleAbwesenheiten.filter(abwesenheit => abwesenheit.id !== id);
    },
    add(state, abwesenheit) {
        state.alleAbwesenheiten = state.alleAbwesenheiten.push(abwesenheit);
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
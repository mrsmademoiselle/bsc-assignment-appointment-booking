import apiService from "@/services/ApiService";
import {Beratungsstelle} from "@/entity/Beratungsstelle";

const state = () => ({
    alleBeratungsstellen: []
})

// getters
const getters = {
    alleBeratungsstellen: state => {
        return state.alleBeratungsstellen;
    }
}

const actions = { // asynchronous
    async fetchBeratungsstellen({commit}) {
        console.log("Fetching alle Beratungsstellen vom server")
        let response = await apiService.get("beratungsstellen/get/all");

        if (response.status === 200) {
            commit('fetchBeratungsstellen', response.data);
        }
    },
    async archiviereBeratungsstelle({commit}, {id, token}) {
        console.log("Archivieren Beratungsstelle with id " + id)

        let response = await apiService.authenticatedPost("/beratungsstellen/archiviere/" + id, {}, token);
        if (response.status === 200) {
            commit('removeBeratungsstelle', id);
        }
    },
    addBeratungsstelle({commit}, {beratungsstelle, token}) {
        return new Promise((resolve, reject) => {

            console.log("addBeratungsstelle (token: " + token + "): " + JSON.stringify(beratungsstelle))
            apiService.authenticatedPost("/beratungsstellen/add", beratungsstelle, token)
                .then(response => {
                    if (response.status === 200) {
                        console.log("Beratungsstelle erfolgreich angelegt");
                        commit('addBeratungsstelle', new Beratungsstelle(response.data));
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    reject("Die Beratungsstelle konnte nicht angelegt werden.")
                })
        });
    }
}

// mutations
const mutations = { // synchronous
    fetchBeratungsstellen(state, data) {
        let beratungsstellen = [];
        data.forEach(beratungsstelle => {
            let b1 = new Beratungsstelle(beratungsstelle);
            beratungsstellen.push(b1);
        });
        state.alleBeratungsstellen = beratungsstellen;
    },
    removeBeratungsstelle(state, id) {
        state.alleBeratungsstellen = state.alleBeratungsstellen.filter(beratungsstelle => beratungsstelle.id !== id);
    },
    addBeratungsstelle(state, beratungsstelle) {
        state.alleBeratungsstellen = [...state.alleBeratungsstellen, beratungsstelle];
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
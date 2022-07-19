import apiService from "@/services/ApiService";
import {Appointment} from "@/entity/Appointment";

const state = () => ({
    allAppointments: []
})

// getters
const getters = {
    allAppointments: state => {
        return state.allAppointments;
    }
}

const actions = { // asynchronous
    async fetchAppointments({commit}, token) {
        console.log("Fetching all appointments from server")
        let response = await apiService.authenticatedGet("termin/get/all", token);

        if (response.status === 200) {
            commit('fetch', response.data);
        }
    },
    async removeAppointment({commit}, {id}, {token}) {
        console.log("Removing appointment with id " + id)

        let response = await apiService.authenticatedPost("termin/cancel/" + id, {}, token);
        if (response.status === 200) {
            commit('remove', id);
        }
    },
    async addAppointment({commit}, {appointment}) {
        console.log("Adding appointment");
        let response = await apiService.authenticatedPost("termin/post", {appointment});
        console.log("Response: '" + response.data + "' with Statuscode " + response.status);
        if (response.status === 200) {
            commit('add', appointment);
        }
    }
}

// mutations
const mutations = { // synchronous
    fetch(state, data) {
        let allAppointments = [];
        data.forEach(appointment => {
            let appointment1 = new Appointment(appointment);
            allAppointments.push(appointment1);
        });
        state.allAppointments = allAppointments;
    },
    remove(state, id) {
        state.allAppointments = state.allAppointments.filter(app => app.id !== id);
    },
    add(state, appointment) {
        state.allAppointments = state.allAppointments.push(appointment);
    }
}

export default {
    state,
    mutations,
    actions,
    getters
}
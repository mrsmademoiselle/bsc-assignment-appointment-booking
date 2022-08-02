import apiService from "@/services/ApiService";

const state = () => ({
    loginSuccess: false,
    username: "",
    token: null
})

// getters
const getters = {
    token: state => {
        if (sessionStorage.getItem("jwt") && !state.token) {
            state.token = sessionStorage.getItem("jwt");
        }
        return state.token;
    },
    username: state => {
        if (sessionStorage.getItem("user") && !state.username) {
            state.username = sessionStorage.getItem("user");
        }
        return state.username
    }
}

const actions = { // asynchronous
    async checkValidityOfToken({commit}, token) {
        try {
            await apiService.authenticatedGet('auth/check', token);
        } catch (e) {
            console.log('Token invalid! ' + e)
            commit('logout');
        }
    },
    login({commit}, {username, password}) {
        return new Promise((resolve, reject) => {
            console.log("Accessing backend with user: " + username + " and pw " + password);

            apiService.post("auth/login", {username, password})
                .then(response => {
                    if (response.status === 200) {
                        console.log("Login successful");
                        commit('login', {username: username, token: response.data});
                    }
                    resolve()
                })
                .catch(error => {
                    console.log("Error: " + error);
                    // place the loginError state into our vuex store
                    commit('logout');
                    reject("Nutzername und Passwort stimmen nicht überein.")
                })
        })
    },
    logout({commit}) {
        commit('logout');
    }
}

// mutations
const mutations = { // synchronous
    login(state, {username, token}) {
        state.username = username;
        state.token = token;
        state.loginSuccess = true;
        sessionStorage.setItem("jwt", token);
        sessionStorage.setItem("user", username);
    },
    logout(state) {
        console.log("logging out...")
        state.token = null;
        state.loginSuccess = false;
        state.username = "";
        sessionStorage.removeItem("jwt");
        sessionStorage.removeItem("user");
    },
}

export default {
    state,
    mutations,
    actions,
    getters
}
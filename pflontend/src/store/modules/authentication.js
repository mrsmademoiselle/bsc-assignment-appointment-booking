import apiService from "@/services/ApiService";

const state = () => ({
    loginSuccess: false,
    token: null
})

// getters
const getters = {
    token: state => {
        if (sessionStorage.getItem("jwt") && !state.token) {
            state.token = sessionStorage.getItem("jwt");
        }
        return state.token;
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
            console.log("Accessing backend with user: " + username);

            apiService.post("auth/login", {username, password})
                .then(response => {
                    if (response.status === 200) {
                        console.log("Login successful");
                        commit('login', response.data);
                    }
                    resolve(response)
                })
                .catch(error => {
                    console.log("Error: " + error);
                    // place the loginError state into our vuex store
                    commit('logout');
                    reject("Invalid credentials!")
                })
        })
    },
    logout({commit}) {
        commit('logout');
    }
}

// mutations
const mutations = { // synchronous
    login(state, token) {
        state.token = token;
        state.loginSuccess = true;
        sessionStorage.setItem("jwt", token);
    },
    logout(state) {
        console.log("logging out...")
        state.token = "";
        state.loginSuccess = false;
        sessionStorage.removeItem("jwt");
    },
}

export default {
    state,
    mutations,
    actions,
    getters
}
import myApi from "@/services/myApi";

const state = () => ({
    loginSuccess: false,
    token: null
})

// getters
const getters = {
    isLoggedIn: state => state.loginSuccess,
    token: state => state.token
}

const actions = { // asynchronous
    login({commit}, {user, password}) {
        return new Promise((resolve, reject) => {
            console.log("Accessing backend with user: '" + user);
            myApi.get(user, password)
                .then(response => {
                    console.log("Response: '" + response.data + "' with Statuscode " + response.status);
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
    }
}

// mutations
const mutations = { // synchronous
    login(state, token) {
        state.token = token;
        state.loginSuccess = true;
    },
    logout(state) {
        state.token = null;
        state.loginSuccess = false;
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
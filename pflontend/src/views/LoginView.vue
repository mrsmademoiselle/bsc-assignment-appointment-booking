<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card border-1 shadow rounded-3 my-5">
          <div class="card-body p-3 p-sm-5">
            <h5 class="card-title text-center mb-5 fw-light fs-5">Anmelden</h5>
            <form>
              <div class="form-floating mb-3">
                <input id="floatingInput" v-model="username" class="form-control"
                       placeholder="Bitte Nutzernamen eingeben"
                       type="text">
              </div>
              <div class="form-floating mb-3">
                <input id="floatingPassword" v-model="password" class="form-control"
                       placeholder="Bitte Passwort eingeben" type="password" @keyup.enter="callLogin">
              </div>

              <div v-if="errors.length > 0" class="mt-4">
                <ErrorBanner v-for="err in errors" :key="err" :message="err"></ErrorBanner>
              </div>
              <div class="d-grid pt-4">
                <FullButton title="Anmelden" @onclick="callLogin"></FullButton>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import ButtonSubmit from "@/components/fragments/ButtonSubmit";
import ErrorBanner from "@/components/fragments/ErrorBanner";

export default {
  name: "LoginView",
  components: {ErrorBanner, FullButton: ButtonSubmit},
  data: function () {
    return {
      username: "",
      password: "",

      error: false,
      errors: []
    }
  },
  methods: {
    callLogin(e) {
      e.preventDefault();
      this.errors = [];
      this.$store.dispatch('login', {username: this.username, password: this.password})
          .then(() => {
            this.$store.dispatch('fetchAppointments', this.$store.getters.token);
            this.$router.push('/')
          })
          .catch((e) => {
            this.errors.push(e);
            this.error = true
          })
    }
  }
}
</script>

<style scoped>
.btn-login {
  font-size: 0.9rem;
  letter-spacing: 0.05rem;
  padding: 0.75rem 1rem;
}

</style>
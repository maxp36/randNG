// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import Vuex from "vuex";
import App from "./App";
import router from "./router";
import chartist from "vue-chartist";
// import cpa from "chartist-plugin-axistitle";
import axios from "axios";

import "materialize-css/dist/css/materialize.min.css";
import "materialize-css/dist/js/materialize.min.js";
// import "chartist-plugin-axistitle/dist/chartist-plugin-axistitle.min.js"


Vue.config.productionTip = false;

Vue.use(Vuex);
Vue.use(chartist);
// Vue.use(cpa);

const store = new Vuex.Store({
  state: {
    seed: 2147483647,
    number: 100,
    from: 0,
    to: 1000,
    column: 10,
    mean: 0.0,
    dev: 1.0
  },
  mutations: {
    setSeed (state, n) {
      state.seed = n;
    },
    setNumber (state, n) {
      state.number = n;
    },
    setFrom (state, n) {
      state.from = n;
    },
    setTo (state, n) {
      state.to = n;
    },
    setColumn (state, n) {
      state.column = n;
    },
    setMean (state, n) {
      state.mean = n;
    },
    setDev (state, n) {
      state.dev = n;
    }
  }
});

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  store,
  components: { App },
  template: "<App/>"
});

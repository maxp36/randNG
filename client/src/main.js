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
import "chartist-plugin-axistitle";
// import "chartist-plugin-targetline"

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
    dev: 1.0,

    evenIsChanged: true,
    evenCurrentNumber: 100,
    evenCount: 0,
    evenAverageMean: 0.0,
    evenAverageDev: 0.0,
    evenSumMean: 0.0,
    evenSumDev: 0.0,

    normalIsChanged: true,
    normalCurrentNumber: 100,
    normalCount: 0,
    normalAverageMean: 0.0,
    normalAverageDev: 0.0,
    normalSumMean: 0.0,
    enormalSumDev: 0.0
  },

  mutations: {
    changeEvenIsChanged(state) {
      state.evenIsChanged = !state.evenIsChanged;
    },

    changeNormalIsChanged(state) {
      state.normalIsChanged = !state.normalIsChanged;
    },

    setSeed(state, n) {
      state.seed = n;
    },
    setNumber(state, n) {
      state.number = n;
    },
    setFrom(state, n) {
      if (state.from !== n) {
        state.from = n;
        state.evenCount = 0;
        state.evenAverageMean = 0.0;
        state.evenAverageDev = 0.0;
        state.evenSumMean = 0.0;
        state.evenSumDev = 0.0;
      }
    },
    setTo(state, n) {
      if (state.to !== n) {
        state.to = n;
        state.evenCount = 0;
        state.evenAverageMean = 0.0;
        state.evenAverageDev = 0.0;
        state.evenSumMean = 0.0;
        state.evenSumDev = 0.0;
      }
    },
    setColumn(state, n) {
      state.column = n;
    },
    setMean(state, n) {
      if (state.mean !== n) {
        state.mean = n;
        state.normalCount = 0;
        state.normalAverageMean = 0.0;
        state.normalAverageDev = 0.0;
        state.normalSumMean = 0.0;
        state.normalSumDev = 0.0;
      }
    },
    setDev(state, n) {
      if (state.dev !== n) {
        state.dev = n;
        state.normalCount = 0;
        state.normalAverageMean = 0.0;
        state.normalAverageDev = 0.0;
        state.normalSumMean = 0.0;
        state.normalSumDev = 0.0;
      }
    },

    setEvenCurrentNumber(state, n) {
      if (state.evenCurrentNumber != n) {
        state.evenCurrentNumber = n;
        state.evenCount = 0;
        state.evenAverageMean = 0.0;
        state.evenAverageDev = 0.0;
        state.evenSumMean = 0.0;
        state.evenSumDev = 0.0;
      }
    },

    incrementEvenCount(state) {
      state.evenCount++;
    },
    computeEvenAverageMean(state) {
      state.evenAverageMean = state.evenSumMean / state.evenCount;
    },
    computeEvenAverageDev(state) {
      state.evenAverageDev = state.evenSumDev / state.evenCount;
    },
    computeEvenSumMean(state, n) {
      state.evenSumMean += n;
      this.commit("computeEvenAverageMean");
    },
    computeEvenSumDev(state, n) {
      state.evenSumDev += n;
      this.commit("computeEvenAverageDev");
    },

    setNormalCurrentNumber(state, n) {
      if (state.normalCurrentNumber != n) {
        state.normalCurrentNumber = n;
        state.normalCount = 0;
        state.normalAverageMean = 0.0;
        state.normalAverageDev = 0.0;
        state.normalSumMean = 0.0;
        state.normalSumDev = 0.0;
      }
    },

    incrementNormalCount(state) {
      state.normalCount++;
    },
    computeNormalAverageMean(state) {
      state.normalAverageMean = state.normalSumMean / state.normalCount;
    },
    computeNormalAverageDev(state) {
      state.normalAverageDev = state.normalSumDev / state.normalCount;
    },
    computeNormalSumMean(state, n) {
      state.normalSumMean += n;
      this.commit("computeNormalAverageMean");
    },
    computeNormalSumDev(state, n) {
      state.normalSumDev += n;
      this.commit("computeNormalAverageDev");
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

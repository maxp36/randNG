<template>
  <div class="section">
    <div class="row">
      <div class="col s12 m8 offset-m2">
        <div>Инициирующее значение: <input v-model.number="seed"></div>
        <div>Количество: <input v-model.number="number"></div>
        <div v-if="isEven">От: <input v-model.number="from"></div>
        <div v-if="isEven">До: <input v-model.number="to"></div>
        <div v-if="!isEven">Мат. ожидание: <input v-model.number="mean"></div>
        <div v-if="!isEven">Среднее отклонение: <input v-model.number="dev"></div>
        <div>Количество столбцов: <input v-model.number="column"></div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapMutations } from "vuex";

export default {
  name: "InputPanel",
  props: ["isEven"],
  data() {
    return {
      seed: 2147483647,
      number: 100,
      from: 0,
      to: 1000,
      column: 10,
      mean: 0.0,
      dev: 1.0
    };
  },
  methods: {
    ...mapMutations([
      "setSeed",
      "setNumber",
      "setFrom",
      "setTo",
      "setColumn",
      "setMean",
      "setDev"
    ])
  },
  watch: {
    seed: function(newNumber, oldNumber) {
      this.setSeed(newNumber);
    },
    number: function(newNumber, oldNumber) {
      this.setNumber(newNumber);
    },
    from: function(newNumber, oldNumber) {
      this.setFrom(newNumber);
    },
    to: function(newNumber, oldNumber) {
      this.setTo(newNumber);
    },
    column: function(newNumber, oldNumber) {
      this.setColumn(newNumber);
    },
    mean: function(newNumber, oldNumber) {
      this.setMean(newNumber);
    },
    dev: function(newNumber, oldNumber) {
      this.setDev(newNumber);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

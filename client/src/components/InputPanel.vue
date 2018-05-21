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
        <div>
          Уровень значимости критерия Пирсона:
          <select v-model.number="sLevel">
            <option value="0.005" selected>0.005</option>
            <option value="0.01">0.01</option>
            <option value="0.025">0.025</option>
            <option value="0.05">0.05</option>
            <option value="0.1">0.1</option>
            <option value="0.25">0.25</option>
            <option value="0.5">0.5</option>
            <option value="0.75">0.75</option>
            <option value="0.9">0.9</option>
            <option value="0.95">0.95</option>
            <option value="0.975">0.975</option>
            <option value="0.99">0.99</option>
            <option value="0.995">0.995</option>
          </select>
          <br>
        </div>
        <div>
          <button class="waves-effect waves-light btn-small" @click="generateEven" v-if="isEven">Сгенерировать</button>
          <button class="waves-effect waves-light btn-small" @click="generateNormal" v-if="!isEven">Сгенерировать</button>
        </div>
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
      dev: 1.0,
      sLevel: 0.005
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
      "setDev",
      "setSLevel",
      "setEvenCurrentNumber",
      "changeEvenIsChanged",
      "setNormalCurrentNumber",
      "changeNormalIsChanged"
    ]),
    generateEven: function() {
      // this.setSeed(this.seed);
      // this.setNumber(this.number);
      // this.setFrom(this.from);
      // this.setTo(this.to);
      // this.setColumn(this.column);
      // this.setMean(this.mean);
      // this.setDev(this.dev);
      this.changeEvenIsChanged();
    },
    generateNormal: function() {
      this.changeNormalIsChanged();
    }
  },
  watch: {
    seed: function(newNumber, oldNumber) {
      this.setSeed(newNumber);
    },
    number: function(newNumber, oldNumber) {
      this.setNumber(newNumber);
      this.setEvenCurrentNumber(newNumber);
      this.setNormalCurrentNumber(newNumber);
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
    },
    sLevel: function(newNumber, oldNumber) {
      this.setSLevel(newNumber);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  select {
    display: block
  }
</style>

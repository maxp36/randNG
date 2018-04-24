<template>
  <div id="app">
    <div class="container">
      <div class="section">
        <div class="row">
          <div class="col s6"><button class="waves-effect waves-light btn-large" :disabled="isEven" @click="isEven = true">Равномерное распределение</button></div>
          <div class="col s6"><button class="waves-effect waves-light btn-large" :disabled="!isEven" @click="isEven = false">Нормальное распределение</button></div>
        </div>
      </div>
      <router-view :isEven="isEven" name="inputPanel"></router-view>
    </div>
    <router-view v-if="isEven" name="evenNumbers"></router-view>
    <router-view v-else name="normalNumbers"></router-view>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      isEven: true
    };
  }
};
</script>

<style>
@import "./assets/chartist.min.css";

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

<template>
  <div class="section">
    <h3>Равномерное распределение</h3>
    <div class="row">
      <div class="col s12 m4 left-align info">
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">fiber_manual_record</i>Значение сгенерированного числа</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Математическое ожидание</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f4c63d;">remove</i>Нижняя граница сгенерированных значений</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d17905;">remove</i>Верхняя граница сгенерированных значений</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #453d3f;">remove</i>Верхняя граница отклонения от мат. ожидания на величину среднеквадратического отклонения</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #59922b;">remove</i>Нижняя граница отклонения от мат. ожидания на величину среднеквадратического отклонения</p>
      </div>
      <div class="col s12 m8">
        <chartist
          ratio="ct-minor-seventh"
          type="Line"
          :data="chartDataPoints"
          :options="chartOptionsPoints" >
        </chartist>
      </div>
    </div>
    <div class="row">
      <div class="col s12 m4 left-align info">
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">remove</i>Распределение сгенерированных значений</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Теоретическое распределение сгенерированных значений</p>
      </div>
      <div class="col s12 m8">
        <chartist
          ratio="ct-minor-seventh"
          type="Line"
          :data="chartDataDistribution"
          :options="chartOptionsDistribution" >
        </chartist>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";

export default {
  name: "EvenNumbers",
  data() {
    return {
      evenData: null,
      chartOptionsPoints: {
        lineSmooth: true,

        chartPadding: {
          top: 30,
          right: 30,
          bottom: 30,
          left: 30
        },

        axisX: {
          labelInterpolationFnc: function(value, index, labels) {
            return (index + 1) % ((labels.length - labels.length % 10) / 10) ===
              0 ||
              value == 1 ||
              index === labels.length - 1
              ? value
              : null;
          }
        },

        series: {
          manyEvenFromTo: {
            showLine: false
          },
          mean: {
            showPoint: false
          },
          from: {
            showPoint: false
          },
          to: {
            showPoint: false
          },
          plusDev: {
            showPoint: false
          },
          minusDev: {
            showPoint: false
          }
        }
      },

      chartOptionsDistribution: {
        lineSmooth: true,

        chartPadding: {
          top: 30,
          right: 30,
          bottom: 30,
          left: 30
        },

        series: {
          bars: {
            lineSmooth: this.$chartist.Interpolation.step()
          },
          theor: {
            showPoint: false
          }
        }
      }
    };
  },

  computed: {
    ...mapState(["seed", "number", "from", "to", "column"]),

    chartDataPoints: function() {
      return {
        series: [
          {
            name: "manyEvenFromTo",
            data: this.evenData !== null ? this.evenData.manyEvenFromTo : []
          },
          {
            name: "mean",
            data:
              this.evenData !== null
                ? this.toHorizontal(this.evenData.mean, this.number)
                : []
          },
          {
            name: "from",
            data: this.toHorizontal(this.from, this.number)
          },
          {
            name: "to",
            data: this.toHorizontal(this.to, this.number)
          },
          {
            name: "plusDev",
            data:
              this.evenData !== null
                ? this.toHorizontal(
                    this.evenData.mean + this.evenData.dev,
                    this.number
                  )
                : []
          },
          {
            name: "minusDev",
            data:
              this.evenData !== null
                ? this.toHorizontal(
                    this.evenData.mean - this.evenData.dev,
                    this.number
                  )
                : []
          }
        ],

        labels: this.toLabel(1, this.number + 1)
      };
    },

    chartDataDistribution: function() {
      return {
        series: [
          {
            name: "bars",
            data:
              this.evenData !== null
                ? this.evenData.distribution.concat(
                    this.evenData.distribution[
                      this.evenData.distribution.length - 1
                    ]
                  )
                : []
          },
          {
            name: "theor",
            data:
              this.evenData !== null
                ? this.toHorizontal(
                    this.evenData.theorDistribution,
                    this.column + 1
                  )
                : []
          }
        ],

        labels: this.toLabelWithColumn(this.from, this.to, this.column)
      };
    }
  },

  watch: {
    seed: function(newNumber, oldNumber) {
      this.getData();
    },
    number: function(newNumber, oldNumber) {
      this.getData();
    },
    from: function(newNumber, oldNumber) {
      this.getData();
    },
    to: function(newNumber, oldNumber) {
      this.getData();
    },
    column: function(newNumber, oldNumber) {
      this.getData();
    }
  },

  methods: {
    toHorizontal: function(data, number) {
      return new Array(number).fill(data);
    },

    toLabel: function(start, end) {
      if (start > end) {
        let temp = start;
        start = end;
        end = temp;
      }
      let length = end - start;
      let arr = new Array(length);
      for (let i = 0; i < length; i++) {
        arr[i] = start + i;
      }
      return arr;
    },

    toLabelWithColumn: function(start, end, column) {
      if (start > end) {
        let temp = start;
        start = end;
        end = temp;
      }
      let step = (end - start) / column;
      var arr = new Array(column + 1);
      for (let i = 0; i < column + 1; i++) {
        arr[i] = parseFloat((start + step * i).toFixed(1));
      }
      return arr;
    },

    getData: function() {
      var url = `http://localhost:8080/even/?seed=${this.seed}&num=${
        this.number
      }&from=${this.from}&to=${this.to}&column=${this.column}`;

      axios.get(url).then(response => {
        this.evenData = response.data;
      });
    }
  },

  created() {
    this.getData();
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.info {
  padding: 30px
}
</style>

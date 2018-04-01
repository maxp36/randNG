<template>
  <div class="section">
    <h3>Нормальное распределение</h3>
    <div class="row">
      <div class="col s12 m4 left-align info">
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">fiber_manual_record</i>Значение сгенерированного числа</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Математическое ожидание</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f4c63d;">remove</i>Верхняя граница отклонения от мат. ожидания на величину среднеквадратического отклонения</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d17905;">remove</i>Нижняя граница отклонения от мат. ожидания на величину среднеквадратического отклонения</p>
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
  name: "NormalNumbers",
  data() {
    return {
      normalData: null,

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
          manyNormal: {
            showLine: false
          },
          mean: {
            showPoint: false
          },
          plusDev: {
            showPoint: false
          },
          minusDev: {
            showPoint: false
          }
        },

        // plugins: [
        //   this.$chartist.plugins.ctAxisTitle({
        //     axisX: {
        //       axisTitle: 'x',
        //       axisClass: 'ct-axis-title',
        //       offset: {
        //         x: 0,
        //         y: 50
        //       },
        //       textAnchor: 'end'
        //     },
        //     axisY: {
        //       axisTitle: 'f(x)',
        //       axisClass: 'ct-axis-title',
        //       offset: {
        //         x: 0,
        //         y: 0
        //       },
        //       textAnchor: 'end',
        //       flipTitle: false
        //     }
        //   })
        // ]
      },

      chartOptionsDistribution: {
        lineSmooth: true,

        chartPadding: {
          top: 30,
          right: 30,
          bottom: 30,
          left: 30
        },

        axisY: {
          high: 1,
          low: 0
        },

        showPoint: false,

        series: {
          bars: {
            lineSmooth: this.$chartist.Interpolation.step()
          },
          theor: {
            lineSmooth: this.$chartist.Interpolation.step()
          }
        }
      }
    };
  },

  computed: {
    ...mapState(["seed", "number", "column", "mean", "dev"]),

    chartDataPoints: function() {
      return {
        series: [
          {
            name: "manyNormal",
            data: this.normalData !== null ? this.normalData.manyNormal : []
          },
          {
            name: "mean",
            data:
              this.normalData !== null
                ? this.toHorizontal(this.normalData.mean, this.number)
                : []
          },
          {
            name: "plusDev",
            data:
              this.normalData !== null
                ? this.toHorizontal(
                    this.normalData.mean + this.normalData.dev,
                    this.number
                  )
                : []
          },
          {
            name: "minusDev",
            data:
              this.normalData !== null
                ? this.toHorizontal(
                    this.normalData.mean - this.normalData.dev,
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
              this.normalData !== null
                ? this.normalData.distribution.concat(
                    this.normalData.distribution[
                      this.normalData.distribution.length - 1
                    ]
                  )
                : []
          },
          {
            name: "theor",
            data:
              this.normalData !== null
                ? this.normalData.theorDistribution.concat(
                    this.normalData.theorDistribution[
                      this.normalData.theorDistribution.length - 1
                    ]
                  )
                : []
          }
        ],

        labels: this.toLabelWithColumn(this.normalData.minValue, this.normalData.maxValue, this.column)
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
    column: function(newNumber, oldNumber) {
      this.getData();
    },
    mean: function(newNumber, oldNumber) {
      this.getData();
    },
    dev: function(newNumber, oldNumber) {
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
      var url = `http://localhost:8080/normal/?seed=${this.seed}&num=${
        this.number
      }&mean=${this.mean}&dev=${this.dev}&column=${this.column}`;

      axios.get(url).then(response => {
        this.normalData = response.data;
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

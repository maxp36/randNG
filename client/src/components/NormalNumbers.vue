<template>
  <div class="section">
    <h3>Нормальное распределение</h3>

    <p class="left-align info">Количество генераций: {{ this.normalCount }} <br>
    При N = {{ this.normalCurrentNumber }} <br>
    Средняя оценка математического ожидания: {{ this.normalAverageMean }} <br>
    Средняя оценка среднеквадратического отклонения: {{ this.normalAverageDev }}
    </p>

    <div class="row">
      <div class="col s12 m4 left-align info">
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">fiber_manual_record</i>Значение сгенерированного числа</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Оценка математического ожидания: {{ (this.normalData.mean).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f4c63d;">remove</i>Верхняя граница отклонения от оценки мат. ожидания на величину оценки среднеквадратического отклонения ({{ this.normalData.dev.toFixed(3) }}): {{ (this.normalData.mean + this.normalData.dev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d17905;">remove</i>Нижняя граница отклонения от оценки мат. ожидания на величину оценки среднеквадратического отклонения ({{ this.normalData.dev.toFixed(3) }}): {{ (this.normalData.mean - this.normalData.dev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #453d3f;">remove</i>Теоретическое математическое ожидание: {{ (this.normalData.theorMean).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #59922b;">remove</i>Верхняя граница отклонения от теоретического мат. ожидания на величину теоретического среднеквадратического отклонения ({{ this.normalData.theorDev.toFixed(3) }}): {{ (this.normalData.theorMean + this.normalData.theorDev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #0544d3;">remove</i>Нижняя граница отклонения от теоретического мат. ожидания на величину теоретического среднеквадратического отклонения ({{ this.normalData.theorDev.toFixed(3) }}): {{ (this.normalData.theorMean - this.normalData.theorDev).toFixed(3) }}</p>
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
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">remove</i>Оценка распределения сгенерированных значений</p>
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
import { mapMutations } from "vuex";

export default {
  name: "NormalNumbers",
  data() {
    return {
      normalData: null,

      chartOptionsPoints: {
        lineSmooth: true,
        fullWidth: true,

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
          },
          theorMean: {
            showPoint: false
          },
          plusTheorDev: {
            showPoint: false
          },
          minusTheorDev: {
            showPoint: false
          }
        },

        plugins: [
          this.$chartist.plugins.ctAxisTitle({
            axisX: {
              axisTitle: "n",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: 50
              },
              textAnchor: "center"
            },
            axisY: {
              axisTitle: "x(n)",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: 0
              },
              textAnchor: "center",
              flipTitle: false
            }
          })
        ]
      },

      chartOptionsDistribution: {
        lineSmooth: true,
        fullWidth: true,
        high: 1,
        low: 0,

        chartPadding: {
          top: 30,
          right: 30,
          bottom: 30,
          left: 30
        },

        // axisY: {
        //   high: 1,
        //   low: 0
        // },

        showPoint: false,

        series: {
          bars: {
            lineSmooth: this.$chartist.Interpolation.step(),
            showArea: true
          },
          theor: {
            lineSmooth: this.$chartist.Interpolation.step(),
            showArea: true
          }
        },

        plugins: [
          this.$chartist.plugins.ctAxisTitle({
            axisX: {
              axisTitle: "x",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: 50
              },
              textAnchor: "center"
            },
            axisY: {
              axisTitle: "f(x)",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: 0
              },
              textAnchor: "center",
              flipTitle: false
            }
          })
        ]
      }
    };
  },

  computed: {
    ...mapState([
      "normalIsChanged",
      "seed",
      "number",
      "column",
      "mean",
      "dev",
      "normalCurrentNumber",
      "normalCount",
      "normalAverageMean",
      "normalAverageDev"
    ]),

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
          },
          {
            name: "theorMean",
            data:
              this.normalData !== null
                ? this.toHorizontal(this.normalData.theorMean, this.number)
                : []
          },
          {
            name: "plusTheorDev",
            data:
              this.normalData !== null
                ? this.toHorizontal(
                    this.normalData.theorMean + this.normalData.theorDev,
                    this.number
                  )
                : []
          },
          {
            name: "minusTheorDev",
            data:
              this.normalData !== null
                ? this.toHorizontal(
                    this.normalData.theorMean - this.normalData.theorDev,
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

        labels: this.toLabelWithColumn(
          this.normalData.minValue,
          this.normalData.maxValue,
          this.column
        )
      };
    }
  },

  watch: {
    normalIsChanged: function(newNumber, oldNumber) {
      this.getData();

      setTimeout(() => {
        this.incrementNormalCount();
        this.computeNormalSumMean(this.normalData.mean);
        this.computeNormalSumDev(this.normalData.dev);
      }, 1000);
    }
  },

  methods: {
    ...mapMutations([
      "incrementNormalCount",
      "computeNormalSumMean",
      "computeNormalSumDev"
    ]),

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
    setTimeout(() => {
      this.incrementNormalCount();
      this.computeNormalSumMean(this.normalData.mean);
      this.computeNormalSumDev(this.normalData.dev);
    }, 1000);
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.info {
  padding: 30px;
}

.ct-line {
  stroke-width: 2px;
}

.ct-point {
  stroke-width: 5px;
}
</style>

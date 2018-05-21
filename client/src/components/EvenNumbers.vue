<template>
  <div class="section">
    <h3>Равномерное распределение</h3>

    <p class="left-align info">Количество генераций: {{ this.evenCount }} <br>
    При N = {{ this.evenCurrentNumber }} <br>
    Средняя оценка математического ожидания: {{ this.evenAverageMean }} <br>
    Средняя оценка среднеквадратического отклонения: {{ this.evenAverageDev }}
    </p>

    <div class="row">
      <div class="col s12 m4 left-align info">
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d70206;">fiber_manual_record</i>Значение сгенерированного числа</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Оценка математического ожидания: {{ (this.evenData.mean).toFixed(3) }}</p>
        <!-- <p class="valign-wrapper"><i class="small material-icons" style="color: #f4c63d;">remove</i>Нижняя граница сгенерированных значений</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d17905;">remove</i>Верхняя граница сгенерированных значений</p> -->
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f4c63d;">remove</i>Верхняя граница отклонения от оценки мат. ожидания на величину оценки среднеквадратического отклонения ({{ this.evenData.dev.toFixed(3) }}): {{ (this.evenData.mean + this.evenData.dev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #d17905;">remove</i>Нижняя граница отклонения от оценки мат. ожидания на величину оценки среднеквадратического отклонения ({{ this.evenData.dev.toFixed(3) }}): {{ (this.evenData.mean - this.evenData.dev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #453d3f;">remove</i>Теоретическое математическое ожидание: {{ (this.evenData.theorMean).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #59922b;">remove</i>Верхняя граница отклонения от теоретического мат. ожидания на величину теоретического среднеквадратического отклонения ({{ this.evenData.theorDev.toFixed(3) }}): {{ (this.evenData.theorMean + this.evenData.theorDev).toFixed(3) }}</p>
        <p class="valign-wrapper"><i class="small material-icons" style="color: #0544d3;">remove</i>Нижняя граница отклонения от теоретического мат. ожидания на величину теоретического среднеквадратического отклонения ({{ this.evenData.theorDev.toFixed(3) }}): {{ (this.evenData.theorMean - this.evenData.theorDev).toFixed(3) }}</p>
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
        <p class="valign-wrapper"><i class="small material-icons" style="color: #f05b4f;">remove</i>Теоретическое распределение сгенерированных значений: {{ (this.evenData.theorDistribution) }}</p>
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

    <div class="row">
      <div class="col s12">
          <h4>Оценка выборки по критерию согласия Пирсона</h4>
          <p>Число степеней свободы k = {{ this.column - 1 }}</p>
          <p>Уровень значимости критерия p = {{ this.sLevel }}</p>
          <p>Расчетный критерий: {{ (this.evenData.x2).toFixed(8) }}</p>
          <p>Табличное значение критерия при k и p: {{ this.evenData.theorX2 }}</p>
          <p v-if="this.evenData.testTheor">Расчетный критерий оказался меньшим,чем табличное значение, значит гипотеза о равенстве (согласии) частот верна.</p>
          <p v-else>Расчетный критерий оказался большим,чем табличное значение, значит гипотеза о равенстве (согласии) частот неверна.</p>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { mapMutations } from "vuex";

export default {
  name: "EvenNumbers",
  data() {
    return {
      evenData: null,

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
          manyEvenFromTo: {
            showLine: false
          },
          mean: {
            showPoint: false
          },
          // from: {
          //   showPoint: false
          // },
          // to: {
          //   showPoint: false
          // },
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
          // this.$chartist.plugins.ctTargetLine({
          //   value: 10,
          //   axis: 'x'
          //   // from: 00,
          //   // to: 1000
          // })
        ]
      },

      chartOptionsDistribution: {
        lineSmooth: true,
        fullWidth: true,
        high: 0.03,
        low: 0,

        chartPadding: {
          top: 30,
          right: 30,
          bottom: 30,
          left: 30
        },

        series: {
          bars: {
            lineSmooth: this.$chartist.Interpolation.step(),
            showArea: true
          },
          theor: {
            showPoint: false
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
      "evenIsChanged",
      "seed",
      "number",
      "from",
      "to",
      "column",
      "sLevel",
      "evenCurrentNumber",
      "evenCount",
      "evenAverageMean",
      "evenAverageDev"
    ]),

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
          // {
          //   name: "from",
          //   data: this.toHorizontal(this.from, this.number)
          // },
          // {
          //   name: "to",
          //   data: this.toHorizontal(this.to, this.number)
          // },
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
          },
          {
            name: "theorMean",
            data:
              this.evenData !== null
                ? this.toHorizontal(this.evenData.theorMean, this.number)
                : []
          },
          {
            name: "plusTheorDev",
            data:
              this.evenData !== null
                ? this.toHorizontal(
                    this.evenData.theorMean + this.evenData.theorDev,
                    this.number
                  )
                : []
          },
          {
            name: "minusTheorDev",
            data:
              this.evenData !== null
                ? this.toHorizontal(
                    this.evenData.theorMean - this.evenData.theorDev,
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
    evenIsChanged: function(newNumber, oldNumber) {
      this.getData();

      setTimeout(() => {
        this.incrementEvenCount();
        this.computeEvenSumMean(this.evenData.mean);
        this.computeEvenSumDev(this.evenData.dev);
      }, 1000);
    }
  },

  methods: {
    ...mapMutations([
      "incrementEvenCount",
      "computeEvenSumMean",
      "computeEvenSumDev"
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
      var url = `http://localhost:8080/even/?seed=${this.seed}&num=${
        this.number
      }&from=${this.from}&to=${this.to}&column=${this.column}&sLevel=${this.sLevel}`;

      axios.get(url).then(response => {
        this.evenData = response.data;
      });
    }
  },

  created() {
    this.getData();
    setTimeout(() => {
      this.incrementEvenCount();
      this.computeEvenSumMean(this.evenData.mean);
      this.computeEvenSumDev(this.evenData.dev);
    }, 1000);
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.info {
  padding: 30px;
}
/* .ct-target-line { */
/* stroke: blue; */
/* stroke-width: 2px; */
/* stroke-dasharray: 0px; */
/* shape-rendering: crispEdges; */
/* } */

.ct-line {
  stroke-width: 2px;
}

.ct-point {
  stroke-width: 5px;
}
</style>

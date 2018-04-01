import Vue from 'vue'
import Router from 'vue-router'
import InputPanel from '@/components/InputPanel'
import EvenNumbers from '@/components/EvenNumbers'
import NormalNumbers from '@/components/NormalNumbers'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      components: {
        inputPanel: InputPanel,
        evenNumbers: EvenNumbers,
        normalNumbers: NormalNumbers
      }
    }
  ]
})

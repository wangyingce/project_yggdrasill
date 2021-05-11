import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import VueCookies from 'vue-cookies'

Vue.config.productionTip = false

Vue.use(VueCookies)

new Vue({
  render: h => h(App),
}).$mount('#app')

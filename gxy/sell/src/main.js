// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueResource from 'vue-resource'
import Vant from 'vant'
import store from './store/store'
// import axios from 'axios'
import axios from './router/configaxios'
import 'vant/lib/index.css'
import 'lib-flexible/flexible'

import './common/stylus/index.styl'
import './assets/iconfont/iconfont.css'
import './assets/iconfont1/iconfont.css'

Vue.prototype.$axios = axios;

Vue.use(VueResource)
Vue.use(Vant)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  axios,
  components: { App },
  template: '<App/>'
})


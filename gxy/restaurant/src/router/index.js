import Vue from 'vue'
import Router from 'vue-router'
import Restaurant from '../components/Restaurant/Restaurant.vue'
import Login from '../components/login/login.vue'
import Register from '../components/Register/Register.vue'
import CreateRestaurant from '../components/CreateRestaurant/CreateRestaurant.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path:'/Restaurant',
      name:'Restaurant',
      component:Restaurant
    },{
      path:'/CreateRestaurant',
      name:'CreateRestaurant',
      component:CreateRestaurant
    }
  ]
})

import Vue from 'vue'
import Router from 'vue-router'
import sellgoods from '@/components/sellgoods/sellgoods'
import ratings from '@/components/ratings/ratings'
import sellers from '@/components/seller/seller'
import login from '@/components/login'
import shouye from  '@/components/shouye/shouye'
import order from '@/components/order/order'
import user from '@/components/user/user'
import homepage from '@/components/homepage/homepage'
import singlegood from '@/components/singlegood/singlegood'
import register from '@/components/register'

Vue.use(Router)

export default new Router({
  mode:"history",
  routes: [
    {
      path:'/',
      name:'login',
      component:login
    },{
      path:'/register',
      name:'register',
      component:register
    },
    {
      path:'/shouye',
      name:'shouye',
      component:shouye,
      redirect:'/shouye/homepage',
      children:[
        {
          path:'order',
          component:order
        },
        {
          path:'user',
          component:user
        },
        {
          path:'homepage',
          component:homepage
        }
      ]
    },{
      path:'/singlegood',
      name:'singlegood',
      component:singlegood,
      redirect:'/singlegood/sellgoods',
      children:[
        {
          path:'sellgoods',
          component:sellgoods
        },
        {
          path:'ratings',
          component:ratings
        },
        {
          path:'sellers',
          component:sellers
        }
      ]
    }
   
  ]
})

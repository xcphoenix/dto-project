import Vue from 'vue'
import Router from 'vue-router'
import sellgoods from '@/components/sellgoods/sellgoods'
import ratings from '@/components/ratings/ratings'
import sellers from '@/components/seller/seller'
import login from '@/components/login'
import shouye from '@/components/shouye/shouye'
import order from '@/components/order/order'
import user from '@/components/user/user'
import homepage from '@/components/homepage/homepage'
import singlegood from '@/components/singlegood/singlegood'
import register from '@/components/register'
import findpass from '@/components/findpass'

Vue.use(Router)

const router = new Router({
  mode: "history",
  routes: [{
      path: '/',
      name: 'login',
      component: login
    }, {
      path: '/register',
      name: 'register',
      component: register
    }, {
      path: '/findpass',
      name: 'findpass',
      component: findpass
    },
    {
      path: '/shouye',
      name: 'shouye',
      component: shouye,
      redirect: '/shouye/homepage',
      meta:{
        requireAuth:true
      },
      children: [{
          path: 'order',
          component: order,
          meta:{
            requireAuth:true
          },
        },
        {
          path: 'user',
          component: user,
          meta:{
            requireAuth:true
          },
        },
        {
          path: 'homepage',
          component: homepage,
          meta:{
            requireAuth:true
          },
        }
      ]
    }, {
      path: '/singlegood',
      name: 'singlegood',
      component: singlegood,
      redirect: '/singlegood/sellgoods',
      meta:{
        requireAuth:true
      },
      children: [{
          path: 'sellgoods',
          component: sellgoods,
          meta:{
            requireAuth:true
          },
        },
        {
          path: 'ratings',
          component: ratings,
          meta:{
            requireAuth:true
          },
        },
        {
          path: 'sellers',
          component: sellers,
          meta:{
            requireAuth:true
          },
        }
      ]
    }

  ],
})


// // 全局路由守卫
router.beforeEach((to,from,next)=>{
  if (to.meta.requireAuth) {     
    // console.log(to.meta.requireAuth)    
    // console.log(localStorage)
    if(localStorage.token!=null){
      next()
    }else{
      setTimeout(()=>{
        next('/')
      },100)
     
    }
  } else {                          
    next();
  }
})
export default router;
// import Vue from 'vue'
// import Vuex from 'vuex'
// Vue.use(Vuex)

// export default new Vuex.Store({
//     state:{
//         //要设置的全局访问的state对象 要设置的初始属性值
//         count:0,
//         ChangeShowCom:true
//     },
//     getters:{
//         getCount(state){
//             return state.count
//         },
//         isShow(state){
//             return state.ChangeShowCom
//         }
//     },
//     mutations:{
//         addCount(state,num){
//             state.count = state.count + num;
//         },
//         delCount(state,num){
//             if(state.count>0){
//                 state.count = state.count - num;
//             }else{
//                 state.count = 0;
//             }
//         },
//         show(state){
//             state.ChangeShowCom = true;
//         },
//         hide(state){
//             state.ChangeShowCom = false;
//         }
//     },
//     actions:{
//         getAddCount(context,num){
//             context.commit('addCount',num)
//         },
//         getDelCount(context,num){
//             context.commit('delCount',num)
//         }
//     }
// })
<template>
<div>
      <sell-header :seller="seller"></sell-header>
        <div class="tab border-1px">
      <div class="tab-item"> <router-link to = "/singlegood/sellgoods" active-class="active">商品</router-link></div>
      <div class="tab-item"> <router-link to = "/singlegood/ratings" active-class="active">评价</router-link></div>
      <div class="tab-item"><router-link to = "/singlegood/sellers" active-class="active">商家</router-link></div>
    </div>
     <!-- <div class="tab-bottom"></div> -->
    <router-view :seller="seller"></router-view>
    <div class="footer"></div>  
  </div>
</div>
</template>
<script>
import SellHeader from "./../header/header";
import './../../../static/css/reset.css';
const ERR_OK = 0;
export default {
  name: "singlegood",
  data(){
    return{
      seller:{
        
      }
    }
  },
  methods:{
  },
  components: {
    SellHeader
  },
  created(){
    this.$http.get('/seller')
        .then(response=>{
          response = response.body;
          if(response.errno == ERR_OK){
            this.seller = response.data;
          
          }
})
  }
};
</script>

<style>
.tab{
  display: flex;
  flex-direction: row;
  width: 100%;
  height:80px;
  line-height: 80px;
  position: relative;
}


.tab::after{
content:" ";
width: 100%;
position: absolute;
bottom:0;
border-bottom: 1px solid rgb(7,17,27,0.1);
}


.tab-item{
  flex:1;
  text-align: center;
}
a{
  display: block;
  text-decoration: none;
  font-size: 28px;
  color:rgb(77, 85, 93)
}
a.active{
  color:rgb(240,20,20)
}
</style>
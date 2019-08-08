<template>
  <div class="shopcart">
    <div class="shopcart-content">
      <div class="shopcart-content-left">
        <div class="shopcart-logo-wrapper">
          <div class="shopcart-logo" :class="{'changecolor1':totalCount>0}">
            <span class="icon-shopping_cart" :class="{'changecolor2':totalCount>0}" ></span>
          </div>
          <div class="num">{{totalCount}}</div>
        </div>
        <div class="shopcart-price" :class="{'changecolor3':totalCount>0}">￥{{totalPrice}}</div>
        <div class="shopcart-desc">另需配送费￥{{deliveryPrice}}元</div>
      </div>
      <div class="shopcart-content-right">
          <!-- <div class="pay">
              ￥{{minPrice}}起送
          </div> -->
          <div v-if="totalCount==0" class="pay">
               ￥{{minPrice}}起送
          </div>
          <div v-if="totalPrice>0&&totalPrice<minPrice" class="pay">
              还差￥{{minPrice-totalPrice}}起送
          </div>
          <div v-if="totalPrice>=minPrice" class="pay1">
              去结算
          </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
// 多层组件的传入 
props:{
   deliveryPrice:{
       type:Number,
       default:0
   },
   minPrice:{
       type:Number,
       default:0
   },
   selectFoods:{
    //    选择的商品数组
       type:Array,
       default(){
           return [{
               price:10,
               count:3
           }]
       }
   }
},
computed:{
    totalPrice(){
        let total = 0;
        this.selectFoods.forEach((food)=>{
            total += food.price*food.count;
        });
        return total;
    },
    totalCount(){
        let count = 0 ;
        this.selectFoods.forEach((food)=>{
            count += food.count;
        })
        return count;
    }
}
};
</script>
<style >
.shopcart {
  width: 100%;
  position: fixed;
  left: 0;
  bottom: 0;
  z-index: 60;
  /* height:50px; */
  /* background-color:rgba(23,54,232,0.4); */
}
.shopcart-content {
  display: flex;
  background: #141d27;
}
.shopcart-content-left {
  flex: 1;
}
.shopcart-logo-wrapper {
  display: inline-block;
  margin-left: 48px;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  position: relative;
  /* top:-15px; */
  bottom: 8px;
  background-color: #141d27;
  box-sizing: border-box;
}
.shopcart-logo {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  background-color: rgb(255, 255, 255, 0.4);
  position: relative;
  left: 10px;
  top: 12px;
  text-align: center;
}
.icon-shopping_cart {
  text-align: center;
  /* color:#80858a;  */
  font-size: 50px;
  line-height: 92px;
}
.shopcart-price {
  display: inline-block;
  vertical-align: top;
  margin-top: 24px;
  padding-right: 24px;
  box-sizing: border-box;
  border-right: 2px solid rgba(255,255,255,0.1);
  font-size: 32px;
  color: rgb(255, 255, 255, 0.4);
  font-weight: 700;
  line-height: 48px;
}
.shopcart-desc {
  display: inline-block;
  vertical-align: top;
  margin-top: 24px;
  margin-left: 16px;
  font-size: 26px;
  color:rgb(255, 255, 255, 0.4);
  /* font-weight: 700; */
  line-height: 48px;

}
.shopcart-content-right {
    text-align: center;
  flex: 0 0 210px;
  width: 210px;
  background-color:#2B333B;
}
.pay{
    height: 96px;
    line-height: 96px;
    text-align: center;
    font-size: 32px;
    font-weight: 700;
    color:rgb(255, 255, 255, 0.4);
}
.pay1{
     height: 96px;
    line-height: 96px;
    text-align: center;
    font-size: 32px;
    color: white;
    background-color: green;
}
.num{
    position: absolute;
    top:0;
    right:0;
    width:48px;
    height:32px;
    line-height: 32px;
    text-align: center;
    border-radius: 32px;
    font-size: 18px;
    color:white;
    background-color:rgb(240, 20, 20);
    box-shadow: 0 8px 16px rgba(0,0,0,0.4);
}
.changecolor1{
    background-color: cornflowerblue;
}
.changecolor2{
    color: white;
}
.changecolor3{
    color: white;
}
</style>
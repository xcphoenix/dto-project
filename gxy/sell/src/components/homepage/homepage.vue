<template>
  <div id="home">
    这是首页啊哈哈哈哈
    <div @click="jinrushangpin">商品1</div>
    <div>商品1</div>
    <div>商品1</div>
    <div>商品1</div>
    <div>商品1</div>
    <div>商品1</div>
    <div>商品1</div>
    <div>商品1</div>
  </div>
</template>
<script>
export default {
    methods:{
        jinrushangpin(){
            console.log('1')
            this.$router.push({path:'/singlegood/sellgoods'})
            
        }
    }
};
</script>
<style scoped>
#home div {
  margin-bottom: 20px;
  margin-top: 20px;
}
</style>
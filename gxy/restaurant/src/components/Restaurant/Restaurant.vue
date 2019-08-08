<template>
<div>
  <div v-if="exist">
      <h1>展示店铺详情</h1>
  </div>
  
  

</div>
</template>
<script>
export default{
    data(){
        return{
            exist:true
        }
    },
    created(){
        // 如果没有创建的话，弹出一个框
    }
}
</script>
<style>

</style>
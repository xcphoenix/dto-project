<template>
<div>

<div class="content">
    <router-view class="homepage" ></router-view>
</div>
<div class="foot">
    <div><router-link to="/shouye/homepage">首页</router-link> </div>
    <div><router-link to="/shouye/order">订单</router-link> </div>
    <div><router-link to="/shouye/user">我的</router-link> </div>
</div>

</div>
</template>
<script>
export default{
    name:"shouye"
}
</script>
<style>
.foot{
    position: absolute;
    bottom: 0;
}
.foot div{
    display: inline-block;
    margin-left:30px;
}
</style>
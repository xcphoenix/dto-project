<template>
<div class="rightsidebar">
    <div class="triangle">
    </div>
     <ul>
        <li>扫一扫</li>
        <li>付款码</li>
        <li>开发票</li>
        <li>火车票</li>
      </ul>
</div>
</template>
<script>
export default{
    name:"rightSideBar"
}
</script>
<style scoped>
.triangle{
 position: relative;
 left:160px;
 top:1.5px;
    width: 0;
    height: 0;
    border-width: 0 15px 15px;
    border-style: solid;
    border-color: transparent transparent gray;
}

.rightsidebar{
    position: absolute;
    right:0;
    top:80px;
}
.rightsidebar ul{
  cursor: pointer;
}
.rightsidebar li{
    background-color: gray;
    width: 200px;
    height: 90px;
    font-size: 35px;
    color:white;
    text-align: center;
    line-height: 90px;
    border-color: gray;
   
}
</style>
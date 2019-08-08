<template>
<div>
    <h1>创建店铺</h1>
    <iframe id="mapPage" width="100%" height="550" frameborder=0
    src="https://apis.map.qq.com/tools/locpicker?search=1&type=1&key=NKOBZ-4NL34-RPCUS-D7FTI-OUMBO-4TB52
&referer=myapp">
</iframe>
</div>
</template>
<script>
export default{
    data(){

    }
}
</script>
<script>
var formatted_addresses = undefined;
    window.addEventListener('message', function(event) {
        // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
        var loc = event.data;
        if (loc && loc.module == 'locationPicker') {//防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
          console.log('location', loc);
        }
    }, false);
</script>
<style>

</style>
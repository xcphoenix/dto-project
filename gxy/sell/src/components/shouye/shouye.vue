<template>
  <div id="shouye">
    <div class="content">
      <router-view class="homepage"></router-view>
    </div>
    <div class="foot">
      <div class="item" :class="{active:shows == 1}" @click="shows = 1">
        <router-link to="/shouye/homepage">
          <div>
            <span>
              <i class="iconfont icon-shouye"></i>
            </span>
          </div>
          <div>
            <span>首页</span>
          </div>
        </router-link>
      </div>
      <div class="item" :class="{active:shows == 2}" @click="shows = 2">
        <router-link to="/shouye/order">
          <div>
            <span>
              <i class="iconfont icon-dingdan"></i>
            </span>
          </div>
          <div>
            <span>订单</span>
          </div>
        </router-link>
      </div>
      <div class="item" :class="{active:shows == 3}" @click="shows = 3">
        <router-link to="/shouye/user">
          <div>
            <span>
              <i class="iconfont icon-wode"></i>
            </span>
          </div>
          <div>
            <span>我的</span>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "shouye",
  data() {
    return {
      shows: 1
    };
  }
};
</script>
<style scoped>
#shouye {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.content {
  flex: 1;
  margin-left: 0;
}
.foot {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 130px;
  border-top: 2px solid #ccc;
}
.item {
  flex: 1;
  padding-top: 10px;
}
.item span {
  display: inline-block;
  margin-top: 5px;
}
i {
  font-size: 65px;
  color: black;
}
.item div {
  text-align: center;
}
a {
  list-style-type: none;
}
.active span {
  color: rgb(6, 180, 253);
}
.active i {
  color: rgb(6, 180, 253);
}
</style>
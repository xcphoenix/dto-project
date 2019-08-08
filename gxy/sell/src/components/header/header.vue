<template>
  <div class="header">
    <div class="content-wrapper">
      <div class="avatar">
        <img :src="seller.avatar" class="avatar_1" />
      </div>
      <div class="content">
        <div class="title">
          <span class="brand"></span>
          <span class="name">{{seller.name}}</span>
        </div>
        <div class="description">{{seller.description}}/{{seller.deliverTime}}分钟送达</div>
        <div v-if="seller.supports" class="support">
          <span class="icon" :class="classMap[seller.supports[0].type]"></span>
          <span class="text">{{seller.supports[0].description}}</span>
        </div>
      </div>
      <div v-if="seller.supports" class="support-count" @click="showDetail">
        <span class="count">{{seller.supports.length}}个</span>
        <i class="icon-keyboard_arrow_right"></i>
      </div>
    </div>
    <div class="bulletin-wrapper" @click="showDetail">
      <span class="bulletin-title"></span>
      <span class="bulletin-text">{{seller.bulletin}}</span>
      <i class="icon-keyboard_arrow_right bottom"></i>
    </div>
    <div class="background">
      <img :src="seller.avatar" width="100%" height="100%" />
    </div>
   <transition name="fade">
    <div v-show="detailShow" class="detail"  >
      <!-- 内容层的包装 让最小高度撑满屏幕 -->
      <div  class="detail-wrapper clearfix">
        <!-- 承载内容 -->
        <div class="detail-main">
          <h1 class="name">{{seller.name}}</h1>
          <div class="star-wrapper">
            <star :size="48" :score="seller.score"></star>
          </div>
          <div class="main-title">
              <div class="line"></div>
              <div class="text">优惠信息</div>
              <div class="line"></div>
          </div>
          <ul v-if="seller.supports" class="supports">
              <li class="support-item" v-for="item in seller.supports">
                 <span class="icon" :class="classMap[item.type]+'_2'"></span>
                 <span class="text">{{item.description}}</span>
              </li>
          </ul>
          <div class="main-title">
              <div class="line"></div>
              <div class="text">商家公告</div>
              <div class="line"></div>
          </div>
          <div class="bulletin">
              <p class="content">{{seller.bulletin}}</p>
          </div>


        </div>
      </div>
    
     <div class="detail-close" @click="hideDetail">
        <i class="icon-close"></i>
      </div>
    
    </div>
     </transition>
   
  </div>
</template>
<script>
import star from "../star/star";
export default {
  props: {
    seller: {
      type: Object
    }
  },
  data() {
    return {
      detailShow: false
    };
  },
  methods: {
    showDetail() {
      this.detailShow = true;
    },
    hideDetail(){
        this.detailShow = false;
    }
  },
  created() {
    //   type 0，1，2，3，4 分别对应数组的下标 ,然后对应图片不同的class
    this.classMap = ["decrease", "discount", "special", "invoice", "guarantee"];
  },
  components: {
    star
  }
};
</script>
<style >
@import "../../common/style/base.css";
@import "../../common/style/mixin.css";
.header {
  color: #fff;
  position: relative;
  overflow: hidden;
}
.content-wrapper {
  padding: 48px 24px 36px 48px;
  font-size: 0;
  position: relative;
}
.avatar {
  display: inline-block;
  vertical-align: top;
  border-radius: 4px;
}
.avater img {
  border-radius: 4px;
}
.content {
  display: inline-block;
  font-size: 28px;
  margin-left: 32px;
}
.title {
  margin: 4px 0 16px 0;
}
.brand {
  /* 图片 */
  display: inline-block;
  width: 60px;
  height: 36px;
  background-size: 60px 36px;
  background-repeat: no-repeat;
}
@media screen and (-webkit-min-device-pixel-ratio: 2),
  screen and (min--moz-device-pixel-ratio: 2) {
  .brand {
    background-image: url("./brand@2x.png");
  }
}
@media screen and (-webkit-min-device-pixel-ratio: 3),
  screen and (min--moz-device-pixel-ratio: 3) {
  .brand {
    background-image: url("./brand@3x.png");
  }
}
.name {
  font-size: 32px;
  color: rgb(255, 255, 255);
  font-weight: bold;
  line-height: 36px;
  margin-left: 12px;
  vertical-align: top;
}
.description {
  font-size: 24px;
  color: rgb(255, 255, 255);
  font-weight: 200;
  line-height: 24px;
  margin-bottom: 20px;
}
.support {
  font-size: 0;
}
.icon {
  display: inline-block;
  width: 24px;
  height: 24px;
  margin-right: 8px;
  background-size: 24px 24px;
  background-repeat: no-repeat;
}
.text {
  line-height: 24px;
  font-size: 20px;
  color: rgb(255, 255, 255);
  font-weight: 200;
  vertical-align: top;
}
.support-count {
  position: absolute;
  right: 24px;
  bottom: 28px;
  padding: 0 16px;
  line-height: 48px;
  height: 48px;
  border-radius: 28px;
  background-color: rgba(0, 0, 0, 0.2);
  text-align: center;
}
.count {
  font-size: 20px;
  vertical-align: top;
}
.icon-keyboard_arrow_right {
  font-size: 20px;
  line-height: 48px;
  margin-left: 4px;
}
.bulletin-wrapper {
  height: 56px;
  line-height: 56px;
  padding: 0 44px 0 24px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  position: relative;
  background-color: rgba(7, 17, 27, 0.2);
}
.bulletin-title {
  display: inline-block;
  width: 44px;
  height: 24px;
  background-size: 44px 28px;
  background-repeat: no-repeat;
  vertical-align: top;
  margin-top: 16px;
}
.bulletin-text {
  font-size: 20px;
  font-weight: 200;
  line-height: 56px;
  margin: 0 8px;
  vertical-align: top;
}
.bottom {
  position: absolute;
  right: 20px;
  top: 6px;
  /* font-size: 10px; */
}
.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  filter: blur(10px);
  background-color: rgba(7, 17, 27, 0.5);
}
.detail {
  position: fixed;
  z-index: 100;
  width: 100%;
  height: 100%;
  /* 设置可以滚动 */
  background-color: rgba(7, 17, 27, 0.8);
  overflow: auto;
  transition: all 3s;
  top: 0;
  left: 0;
  backdrop-filter: blur(10)
}
.fade-enter,.fade-leave-to{
    opacity: 0;
   background-color: rgba(7, 17, 27, 0);
}
.fade-enter-active{
  opacity: 1;
  background-color: rgba(7, 17, 27, 0.8);
  transition: all 2s;
}
.detail-wrapper {
  /* 最小高度跟视口一样高 */
  min-height: 100%;
  width: 100%;
}
.detail-main {
  margin-top: 128px;
  padding-bottom: 128px;
}
.detail-close {
  position: relative;
  width: 64px;
  height: 64px;
  margin: -128px auto 0 auto;
  clear: both;
  font-size: 64px;
}
.detail-main .name {
  font-size: 32px;
  line-height: 32px;
  font-weight: 700;
  text-align: center;
}
.star-wrapper {
  margin-top: 36px;
  padding: 4px 0;
  text-align: center;
}
.main-title{
    display: flex;
    width: 80%;
    margin:56px auto 48px auto;
}
.line{
    flex:1;
    position:relative;
    top:-12px;
    border-bottom: 1px solid rgba(255,255,255,0.2);
}
.text{
padding:0 16px;
font-size: 28px;
font-weight: 150;
}
.supports{
    width: 80%;
    margin:0 auto; 
    padding-left: 64px;
}
li.support-item{
    padding:0 24px;
    margin-bottom: 24px;
    font-size: 0;
}
li.support-item:last-child{
    margin-bottom:0;
}
.support-item .icon{
    display: inline-block;
    width: 32px;
    height: 32px;
    vertical-align: top;
    margin-right: 32px;
    background-size: 32px 32px;
    background-repeat: no-repeat;
    vertical-align: top;
}
.support-item .text{
    display: inline-block;
    line-height: 32px;
    font-size: 24px;
}
.detail-main .bulletin{
    width:80%;
    margin:0 auto;
}
.bulletin .content{
    padding: 0 24px;
    line-height: 48px;
    font-size: 24px;
}
.avatar_1{
   width:128px;
   height:128px;
}

</style>
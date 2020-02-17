<!--我的订单-->
<template>
  <div id="order">
    <v-head goBack="true" title="订单"></v-head>
    <!--没有登录-->
    <div class="to-login-container" v-if="!username">
      <div class="avatar">
        <i class="iconfont icon">&#xe623;</i>
      </div>
      <span class="tip">您还没有登录，请登录后查看订单</span>
      <router-link to="/login">
        <span class="to-login">登录/注册</span>
      </router-link>
    </div>
    <div class="no-order" v-else-if="noOrder">
      <span>订单空空如也，快去购物吧！</span>
    </div>
    <!--登录后-->
    <article v-else ref="orderWrapper">
      <div class="container">
        <router-link v-for="item in ordersList" :key="item.orderId" tag="section" :to="'/order_detail?id='+ item.orderId">
          <div class="title">
          <span class="restaurant-picture">
            <img :src="item.exRstLogoUrl">
          </span>
            <router-link :to="'/store/menu?id='+ item.rstId" class="restaurant_name">
              {{item.exRstName}} <strong> > </strong>
            </router-link>
            <span class="order-status">&nbsp;{{item.statusDesc}}</span>
          </div>
          <div class="info-container" v-for="food in item.orderItems" :key="food.foodId">
            <div>
              <span class="foods-name">{{food.exFoodName}}</span>
              <span class="num">x{{food.quantity}}</span>
            </div>
          </div>
          <div class="price-container"><span>共{{item.orderItems.length}}件商品，实付</span><span>￥{{item.price}}</span></div>
          <div class="footer">
            <span v-if="item.status === 4" tag="span">已取消</span>
            <span v-else-if="item.status === 5" tag="span">已超时</span>
            <router-link v-else-if="item.status !== 0" :to="{path:'/store',query:{id:item.rstId}}" tag="span">再来一单</router-link>
            <router-link v-else :to="{path:'/pay',query:{order_id:item.orderId, price: item.price}}" tag="span">去支付</router-link>
          </div>
        </router-link>
      </div>
    </article>
    <v-bottom></v-bottom>
    <router-view></router-view>
  </div>
</template>

<script>
  import {orders} from '@/api/order'
  import {getInfo} from '@/utils/auth'
  import BScroll from 'better-scroll'

  export default {
    data() {
      return {
        username: null,   //用户名 判断是否登录
        ordersList: [],   //订单列表
        noOrder: false    //没有订单
      }
    },
    created() {
      this.username = getInfo();
      // 获取订单列表
      if (this.username) {
        orders().then((response) => {
          if (response.data.status === 200) {
            this.ordersList = response.data.data.orders.data;
            this.noOrder = !this.ordersList.length;
            this.$nextTick(() => {
              new BScroll(this.$refs.orderWrapper, {click: true, probeType: 3,});
            })
          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "../../style/mixin.scss";

  #order {
    height: 100%;
    overflow: hidden;
    background: #f4f4f4;
    .to-login-container {
      width: 100%;
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      text-align: center;
      .avatar {
        text-align: center;
        .icon {
          font-size: 2rem;
          color: rgb(214, 214, 214);
        }
      }
      .tip {
        font-size: 0.5rem;
        display: block;
        color: rgb(155, 153, 151);
        margin: 0.5rem 0;
        text-align: center;
      }
      .to-login {
        display: inline-block;
        background: $mtYellow;
        @include px2rem(width, 485);
        font-size: 0.5rem;
        @include px2rem(line-height, 85);
        border-radius: 0.2rem;
      }
    }

    .no-order {
      width: 100%;
      position: absolute;
      top: 50%;
      left: 50%;
      text-align: center;
      transform: translate(-50%, -50%);
      span {
        font-size: 0.4rem;
      }
    }
    article {
      height: calc(100% - 1.2rem);
      .container {
        padding-bottom: 2rem;
      }
      section {
        background: #fff;
        margin: 0.3rem 0;
        padding: 0.2rem 0;
        .title {
          display: flex;
          align-items: center;
          margin-right: 0.3rem;
          @include px2rem(height, 100);
          border-bottom: 1px solid #e4e4e4;
          .restaurant-picture {
            margin: 0 0.25rem;
            display: inline-block;
            border-radius: 50%;
            overflow: hidden;
            @include px2rem(width, 80);
            @include px2rem(height, 80);
            img {
              width: 100%;
              height: 100%;
            }
          }
          .restaurant-name {
            flex: 1;
            font-size: 0.45rem;
            font-weight: 600;
            strong {
              margin-left: 0.3rem;
            }
          }
          .order-status {
            font-size: 0.4rem;
            color: #666;
          }
        }
        .info-container {
          margin: 0.3rem;
          div:first-child {
            display: flex;
            align-items: center;
            margin: 0.3rem 0.3rem 0.3rem 1.2rem;
          }
          .foods-name, .num {
            color: #999;
          }
          .foods-name {
            font-size: 0.4rem;
            font-weight: 600;
            flex: 1;
          }
          .num {
            font-size: 0.3rem;
          }
        }
        .price-container {
          margin: 0.3rem;
          font-size: 0.3rem;
          text-align: right;
          padding-top: 0.2rem;
          border-top: 1px solid #e4e4e4;
        }
        .footer {
          margin: 0.3rem;
          text-align: right;
          span, .make-comment {
            text-align: center;
            font-size: 0.4rem;
            display: inline-block;
            @include px2rem(width, 170);
            @include px2rem(line-height, 70);
            border: 1px solid #e4e4e4;
          }
          .make-comment {
            background: $mtYellow;
            border: none;
          }
        }
      }
    }

  }
</style>

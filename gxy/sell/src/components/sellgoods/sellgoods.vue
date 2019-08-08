<template>
  <div class="goods">
    <div class="menu-wrapper" ref="menu">
        <ul>
            <li v-for="(item,index) in goods" class="menu-item" 
            :class="{'current':index==currentIndex}" @click="selectMenu(index)">
                <span v-show="item.type>0" class="icon" :class="classMap[item.type]+'_3'" ></span>
                <span class="goodstype">{{item.name}}</span>
            </li>
        </ul>
    </div>
    <div class="foods-wrapper" ref="foods">
        <ul>
            <li v-for="item in goods" class="food-list food-list-hook">
                <p class="title">{{item.name}}</p>
                <ul>
                      <li v-for = "food in item.foods" class="food-item">
                          <div class="food-icon">
                              <img  :src="food.icon">
                          </div>
                          <div class="food-content">
                               <p class="food-name">{{food.name}}</p>
                            <p class="food-desc">{{food.description}}</p>
                            <div class="food-extra">
                                <span>月售{{food.sellCount}}份</span>
                                <span>好评率{{food.rating}}%</span>
                            </div>
                            <div class="food-price">
                                <span>￥{{food.price}}</span>
                                <span v-show="food.oldPrice">￥{{food.oldPrice}}</span>
                            </div>
                          </div>
                      </li>
                </ul>
            </li>
        </ul>
    </div>
    <Shopcart :deliveryPrice="seller.deliveryPrice" :minPrice="seller.minPrice"></Shopcart>
  </div>
  
</template>
<script>
import BScroll from 'better-scroll';
import Shopcart from '../shopcart/shopcart'
const ERR_OK = 0;
export default {
    props:{
        seller:{
            type:Object
        }
    },
    components:{
        Shopcart
    },
    data(){
        return{
            goods:[],
            listHeight:[],
            scrollY:0,
            realIndex:0
        }
    },
    methods:{
        _initScroll(){
            this.meunScroll = new BScroll(this.$refs.menu,{
                click:true //解除默认事件
            })
            this.foodsScroll = new BScroll(this.$refs.foods,{
                probeType:3
            });
            console.log('123')
            this.foodsScroll.on('scroll',(pos)=>{
                this.scrollY = Math.abs(Math.round(pos.y))
                // console.log(this.scrollY)
            })
        },
        _calculateHeight(){
            // 计算高度
            console.log('计算高度')
            let foodList = this.$refs.foods.getElementsByClassName('food-list-hook');
            let height = 0;
            this.listHeight.push(height)
            for(let i = 0;i < foodList.length ; i++){
                let item = foodList[i]
                height+=item.clientHeight;
                this.listHeight.push(height)
            }
            console.log(this.listHeight)
        },
        selectMenu(index){
            console.log(index,'index')
            console.log(this.currentIndex,'currentIndex')
            console.log(this.scrollY,'监听currentIndex前')
            let foodList = this.$refs.foods.getElementsByClassName('food-list-hook')
            let el = foodList[index]
            this.foodsScroll.scrollToElement(el,500);
        }
    },
    computed:{
        // 当this.scrollY变化时，currentIndex会实时的变化
        currentIndex(){
            // console.log(this.scrollY,'111')
            // 每次的i+1对应的scrollY都是一个临界点，这一点要十分注意
            for(let i =0;i < this.listHeight.length;i++){
                let height1 = this.listHeight[i];
                let height2 = this.listHeight[i+1];
                // 落在这个区间内
                if(!height2 || (this.scrollY >= height1 && this.scrollY < height2)){
                    console.log(i,'000')
                    return i;
                }
            }
            return 0;
        }
    },
    created(){
        this.$http.get('/goods').then((response)=>{
            response = response.body;
            if(response.errno == ERR_OK){
                this.goods = response.data;
                console.log(this.goods)
                this.$nextTick(()=>{
                    this._initScroll()
                    this._calculateHeight()
                })
             
            }
            
        });
      
    //   type 0，1，2，3，4 分别对应数组的下标 ,然后对应图片不同的class
    this.classMap = ["decrease", "discount", "special", "invoice", "guarantee"];
  
    }
};
</script>
<style >
@import "../../common/style/mixin.css";

.goods {
  /* 给good设置绝对定位 */
  position: absolute;
  top: 348px;
  width: 100%;
  bottom: 92px;
  display: flex;
  overflow: hidden;
}
.menu-wrapper {
  flex: 0 0 160px;
  width: 160px;
  background-color: #f3f5f7;
}
.foods-wrapper {
  flex: 1;
}
.menu-item{
    display: table;
    height:108px;
    width: 112px;
    line-height: 28px;
    padding:0 24px;
}
.menu-item::after{
content:" ";
width: 160px;
position: absolute;
left:0;
border-bottom: 2px solid rgb(7,17,27,0.1);
}
/* .menu-item .current{
    position:relative;
    margin-top: -1px;
    z-index:10;
    background-color: #fff;
    font-weight: 700;
} */
 .current{
     background-color:white;
     margin-top: -2px;
     font-weight: 700px;
 }
.icon{
 display: inline-block;
  width: 24px;
  height: 24px;
  margin-right: 4px;
  background-size: 24px 24px;
  background-repeat: no-repeat;
   vertical-align: middle;
  
}
.goodstype{
    vertical-align: middle;
    font-size: 24px;
    display: table-cell;
    width: 112px;
    vertical-align: middle;
}
.food-list{
    /* height: 26px; */
    background-color: #f3f5f7;
 
}
.food-list p{
   
}
.food-list .title{
    margin:0;
     height: 52px;
    font-size: 24px;
    color:rgb(147,153,159);
    line-height: 52px;
    padding-left: 28px;
       box-shadow: -4px 0 0 #d9dde1;
}
.food-item{
    /* margin:0px 16px 0px 16px; */
    padding-left:32px;
    padding-right: 32px;
    padding-top: 36px;
    display: flex;
    background-color: white;
    border-bottom:1.6px solid rgb(7,17,27,0.1);
}
.food-icon{
    margin-right: 20px;
    flex: 0 0 114px;
}
.food-content{
    flex:1;
}
.food-name{
    font-size: 28px;
    color:rgb(7,17,27);
    line-height: 28px;
    margin-bottom: 12px;
}
.food-desc,.food-extra{
    font-size: 20px;
    color:rgb(147,153,159);
    line-height: 20px;
    margin-bottom:16px;
}
.food-price span:first-child{
    font-size: 28px;
    color:red;
    font-weight: 700;
    line-height: 48px;
}
.food-price span:last-child{
    font-size: 20px;
    color:rgb(147,153,159);
    font-weight: normal;
    line-height: 48px;
}
.food-desc{
    line-height: 28px;
}
.food-icon img{
    width:135px; 
    height:135px;
}
</style>
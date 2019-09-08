<template>
  <div class="swiper">
    <div class="container">
      <ul id="li_container">
        <li v-for="(index,item) in listData" class="hh">
          <img :src="listData[item].imgUrl" height="350" width="600" />
        </li>
      </ul>
      <div class="arrow">
        <span class="left" @click="cut(1,currentIndex)"><</span>
        <span class="right" @click="cut(2,currentIndex)">></span>
      </div>
      <div class="Btns">
        <ul class="btns">
          <li v-for="(index,item) in listData" @click="cut_img(index,item)"  :class="item == (currentIndex - 1) ? 'btn_active':'btn_noactive'"></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "swiper",
  data() {
    return {
      imgWidth: 600,
      imgHeight: 350,
      currentIndex: 1,
      speed: 30,
      timer:undefined,
      listData: [
        {
          id: "1",
          imgUrl: require("../../assets/img/1.jpg")
        },
        {
          id: "2",
          imgUrl: require("../../assets/img/2.jpg")
        },
        {
          id: "3",
          imgUrl: require("../../assets/img/3.jpg")
        },
        {
          id: "4",
          imgUrl: require("../../assets/img/4.jpg")
        }
      ]
    };
  },
  methods: {
    cut(direction, currentindex) {
      var ul1 = document.getElementById("li_container");
      if (direction == 1) {
        if (currentindex == 1) {
          this.currentIndex = 4;
          ul1.style.marginLeft = -(this.currentIndex - 1) * this.imgWidth + "px";
          console.log(ul1.style.marginLeft)
        } else {
          var move;
           move = parseInt(ul1.style.marginLeft)
           this.animate(1,move)
          this.currentIndex--;
        }
      } else {
        if (currentindex == 4) {
          ul1.style.marginLeft = 0 + "px";
          this.currentIndex = 1;
        } else {
          var move;
          if(this.currentIndex == 1){
              move = 0;
          }else{
            move = parseInt(ul1.style.marginLeft)
          }
          this.animate(-1,move)
          this.currentIndex++;
            }
          }
    },
    animate(b,move){
      var move = move;
      window.setTimeout(()=>{
        move+=b*10  //这里的30也需要变化
        var ul1 = document.getElementById("li_container");
        ul1.style.marginLeft = move + "px"
        if(b == -1&&move>-(this.currentIndex-1)*this.imgWidth){
            this.animate(-1,move)
        }else if(b==1&&move < -(this.currentIndex-1)*this.imgWidth){
          this.animate(1,move)
        }else{
        }
      },20)
    },
    cut_img(index, item) {
      console.log(index)
      var ul1 = document.getElementById("li_container");
      this.currentIndex = item + 1;
      ul1.style.marginLeft = -(this.currentIndex - 1) * this.imgWidth + "px";
    },
  },
  mounted(){
  this.imgWidth = document.getElementsByClassName("hh")[0].offsetWidth
  //  console.log(document.getElementsByClassName("hh")[0].offsetWidth)
   
  }
};
</script>
<style>
.swiper {
  width: 80%;
  margin: 0 auto;
  position: relative;
}
.swiper ul {
  /* display: flex; */
  width: 5000px;
  margin-left: 2px;
}
.swiper li {
  display: inline-block;
  width: 600px;
}
.swiper img {
  width: 100%;
}
.container {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 400px;
  margin: 0 auto;
}
.arrow {
  position: absolute;
  top: 150px;
  z-index: 50;
  width: 100%;
}
.arrow span {
  font-size: 45px;
  color: aqua;
}
.right {
  float: right;
}
.Btns {
  width: 100%;
  position: absolute;
  top: 295px;
}
ul.btns {
  width: 40%;
  z-index: 30px;
  margin: 0 auto;
}


.btns li {
  width: 20px;
  height: 20px;
  border: 1px solid black;
  border-radius: 50%;
  /* background-color: whitesmoke; */
  margin: 0 20px 0 15px;
  flex: 1;
}
.btn_active{
  background-color:tomato;
}
.btn_noactive{
  background-color: whitesmoke
}
</style>
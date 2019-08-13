import {Switch,Route,Redirect,Link} from 'react-router-dom'
import {Button,Cascader,Icon } from 'antd'
import React from 'react'
import QQMap from 'qqmap'
import './addshop.css'
import locationdata from '../local/location'
import api from '../httpConfig'
export default class jsdemo extends React.Component{
   constructor(props){
       super(props);
       this.state = {
           restaurant_name:'',
           restaurant_desc:'',
           restaurant_phone:'',
           contact_man:'',
           tag:'',
           bh_start:'',
           bh_end:'',
           country_id:'',
           address:'',
           addr_lng:'',
           addr_lat:'',
           store_img:'',
           instore_imgs:[],
           logo:'',
           banner_img:'',
           bulletin:'',
           delivery_price:'',
           min_price:''
       }
   }
   change_name=(e)=>{
    this.setState(
        {restaurant_name: e.target.value}, 
        () => console.log(this.state.restaurant_name))
   }
   change_desc = (e)=>{
       this.setState({
           restaurant_desc:e.target.value},
       () => console.log(this.state.restaurant_desc))
   }
   change_phone = (e)=>{
       this.setState({
           restaurant_phone:e.target.value},
           ()=>console.log(this.state.restaurant_phone))
   }
   change_contact = (e)=>{
       this.setState({
           contact_man:e.target.value},
           ()=>console.log(this.state.contact_man))
   }
   change_tag = (e)=>{
       this.setState({
           tag:e.target.value},
           ()=>console.log(this.state.tag))
   }
   changebh_start = (e)=>{
       console.log(e.target.value)
       this.setState({bh_start:e.target.value})
   }
   changebh_end = (e)=>{
    console.log(e.target.value)
    this.setState({bh_end:e.target.value})
}

   change_min = (e)=>{
       this.setState({min_price:e.target.value})
   }
   change_deliver = (e)=>{
       this.setState({delivery_price:e.target.value})
   }

   change = (value)=>{
    // console.log(value)
    this.setState({country_id:value[2]},()=>{
        console.log(this.state.country_id)
    })
   }

   change_address = (e)=>{
       this.setState({address:e.target.value})
   }
   change_bulletin = (e)=>{
       this.setState({bulletin:e.target.value})
   }
  
   
   //搜索
   searchKeyword = () => {
    //获取文本框输入的值
    let keyword = document.getElementById('keyword').value;
    let region = document.getElementById('region').value;
    // 清空上一次搜索结果
    Array.from(this.markers).forEach(marker=>{
        marker.setMap(null);
    });
    //调用腾讯地图的搜索功能
    this.searchService.setLocation(region);
    this.searchService.search(keyword);
}
   //地图初始化的方法
initQQMap = () => {
    //设置中心坐标
    let tarLat = 39.90736606309809;
    let tarLng = 116.39774322509766;
    //初始化地图
    QQMap.init('NKOBZ-4NL34-RPCUS-D7FTI-OUMBO-4TB52', ()=>{
        // 初始化经纬度，最开始的点
        let myLatlng = new QQMap.LatLng(tarLat, tarLng);
        // 设置地图属性
        let myOptions = {
            zoom: 16,
            center: myLatlng,
            mapTypeId:  QQMap.MapTypeId.ROADMAP,
        };
        // 创建地图，绑定dom
        this.map = new  QQMap.Map(
            document.getElementById('mapcontainer'),
            myOptions,
        );
        // 现实已经存在的点
        let markerlast = new  QQMap.Marker({
            position: myLatlng,
            map: this.map,
        });
        // 调用检索
        let latlngBounds = new  QQMap.LatLngBounds();
        // 调用Poi检索类
        let searchService = [];//调用百度地图的搜索服务
        let markers = [];//用户搜索后显示的点的集合
        // 调用搜索服务
        searchService = new  QQMap.SearchService({
            complete: results=> {
                let pois = results.detail.pois;
                for (let i = 0, l = pois.length; i < l; i++) {
                    let poi = pois[i];
                    latlngBounds.extend(poi.latLng);
                    let marker = new  QQMap.Marker({
                        map: this.map,
                        position: poi.latLng,
                    });
                    marker.setTitle(i + 1);
                    markers.push(marker);
                }
                this.map.fitBounds(latlngBounds);
            },
        });
        // 将服务注册到this中，方便搜索方法调用
        this.searchService = searchService;
        this.markers = markers;
        // 鼠标点击监听
        QQMap.event.addListener(
            this.map,
            'click',
            event=> {
                // 清除初始化位置
                markerlast.position = event.latLng;
                markerlast.setMap(null);
                // 获取经纬度位置
                let lat = event.latLng.getLat();
                let lng = event.latLng.getLng();
                // 赋值至文本框内
                // 经度和纬度获取
                this.setState({addr_lng:lng});
                this.setState({addr_lat:lat})


                console.log(lat,lng,'123')
                // this.props.form.setFieldsValue({ lat: lat, lng: lng });
                // 绘制点击的点
                let marker = new  QQMap.Marker({
                    position: event.latLng,
                    map: this.map,
                });
                // 添加监听事件   获取鼠标单击事件
                QQMap.event.addListener(this.map, 'click', function(event) {
                    marker.setMap(null);
                });
                // 清空上一次搜索结果
                Array.from(this.markers).forEach(marker=>{
                    marker.setMap(null);
                });
            }
        );
    });
}
   componentDidMount(){
       this.initQQMap()
   }
   changeImg(event){
   let instore_img = null; 
   let files = event.target.files[0]
   if (!event || !window.FileReader) return; // 看支持不支持FileReader
   let reader = new FileReader();
   reader.readAsDataURL(files); // 这里是最关键的一步，转换就在这里
   reader.onloadend = function () {
      
       console.log(this.result)
       instore_img = this.result
       console.log(document.getElementsByClassName("instore_img").length)
       if(document.getElementsByClassName("instore_img").length < 6){
        // console.log(instore_img,'123')
           let img = document.createElement('img')
           img.setAttribute('class','instore_img')
           img.src = instore_img;
           document.getElementById("instoreImg").appendChild(img)
       }
       let arr =  document.getElementsByClassName("instore_img");
       let arrImg = [];
       Array.from(arr).forEach(function(item){
           arrImg.push(item.src)
       })
       this.setState({instore_imgs:arrImg})

   }
   }

   changeStoreImg(event){
    let store_img = null;
    let files = event.target.files[0]
     if (!event || !window.FileReader) return; // 看支持不支持FileReader
   let reader = new FileReader();
   reader.readAsDataURL(files); // 这里是最关键的一步，转换就在这里
   reader.onloadend = function () {
       store_img = this.result
       document.getElementById('storeImg').src = store_img
       this.setState({store_img:store_img})
   }
}
changeLogoImg(event){
    let logo_img = null;
    let files = event.target.files[0]
     if (!event || !window.FileReader) return; // 看支持不支持FileReader
   let reader = new FileReader();
   reader.readAsDataURL(files); // 这里是最关键的一步，转换就在这里
   reader.onloadend = function () {
       logo_img = this.result
       document.getElementById('logoImg').src = logo_img
       this.setState({logo:logo_img})
   }
}
changeBannerImg(event){
    let banner_img = null;
    let files = event.target.files[0]
     if (!event || !window.FileReader) return; // 看支持不支持FileReader
   let reader = new FileReader();
   reader.readAsDataURL(files); // 这里是最关键的一步，转换就在这里
   reader.onloadend = function () {
       banner_img = this.result
       document.getElementById('bannerImg').src = banner_img
       this.setState({banner_img:banner_img})
   }
}
submit=()=>{
    console.log('1')
   console.log(this.state)
   this.axios.post(api.add_shop,this.state).then(res=>{
       console.log(res.data)
   },err=>{
       console.log(err)
   })

}
    render(){
      const options = locationdata
        return(
            <div>  
                <center><h2>店铺开户 </h2></center>
                 <div className="main">
                    <div className="header">
                        <p>创建店铺</p> 
                    </div>
                    <div className="contain">
                        <div className="form_index">
                        <label className="form_name">店铺名<em>*</em> </label>
                        <div className="form_input"><input type="text" onChange={this.change_name} defaultValue={this.state.restaurant_name}></input></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店铺描述</label>
                        <div className="form_input"><input type="text" onChange={this.change_desc} defaultValue={this.state.restaurant_desc}></input></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">外卖联系电话<em>*</em></label>
                        <div className="form_input"><input type="text" onChange={this.change_phone} defaultValue={this.state.restaurant_phone} ></input></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">联系人<em>*</em></label>
                        <div className="form_input"><input type="text" onChange={this.change_contact} defaultValue={this.state.contact_man} ></input></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">门店类型<em>*</em></label>
                        <div className="form_input"><input type="text" onChange={this.change_tag}  ></input></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">营业开始时间<em>*</em></label>
                        <div className="form_input"> <input id="w3cfuns_time1" name="w3cfuns.com"  type="time" onChange={this.changebh_start} /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">营业结束时间<em>*</em></label>
                        <div className="form_input"> <input id="w3cfuns_time2" name="w3cfuns.com" type="time" onChange={this.changebh_end} /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">起送价<em>*</em></label>
                        <div className="form_input" > <input  type="text" onChange={this.change_min} /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">配送价<em>*</em></label>
                        <div className="form_input"> <input  type="text" onChange={this.change_deliver}  /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">选择地点<em>*</em></label>
                
                        <div className="form_input">
                        <Cascader defaultValue={['1', '2', '3']} options={options} onChange={this.change} />
                        </div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店铺详细地址<em>*</em></label>
                        <div className="form_input"> <input  type="text" onChange={this.change_address} /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店铺公告<em>*</em></label>
                        <div className="form_input"> <input  type="textarea" onChange={this.change_bulletin} /></div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">经纬度<em>*</em></label>
                        {/* 放置地图的插件 */}
                        <div id="mapcontainer">
                        </div>
                        <div className="search">
                        <div>
                        <input id="region" defaultValue={'陕西'}  placeholder="城市" type="text"/>
                        </div>
                        <div>
                            <input id="keyword" defaultValue={'西安'} placeholder="地址" type="text"/>
                        </div>
                        <Button icon="search" className="add_search" onClick={this.searchKeyword}>Search</Button>    
                        </div>
                    </div>

                    <div className="form_index">
                        <label className="form_name">门店图片<em>*</em></label>
                        <div style={{position:'relative'}}>
                        <input type="file" class="single_img" onChange={this.changeStoreImg}></input>
                        <img src={require('../img/timg.jpg')} id="storeImg"  style={{height:'80px',width:'80px'}}></img>
                        </div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店铺Logo<em>*</em></label>
                        <div style={{position:'relative'}}>
                        <input type="file" class="single_img" onChange={this.changeLogoImg}></input>
                        <img src={require('../img/timg.jpg')} id="logoImg"  style={{height:'80px',width:'80px'}}></img>
                        </div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店铺Banner<em>*</em></label>
                        <div style={{position:'relative'}}>
                        <input type="file" class="single_img" onChange={this.changeBannerImg}></input>
                        <img src={require('../img/timg.jpg')} id="bannerImg"  style={{height:'80px',width:'80px'}}></img>
                        </div>
                    </div>
                    <div className="form_index">
                        <label className="form_name">店内照片<em>*</em></label>
                        <div id="instoreImg">
                        {/* <img id="store_img" src={require('../img/100.jpg')} style={{height:'80px',width:'80px'}}></img> */}
                        </div>
                        <div className="pic">
                            <input type="file" onChange={this.changeImg} name="filename"/>
                        </div>                       
                    </div>

                   
　　

                    </div>
                    <div className="header">
                        {/* <p>完成创建</p>  */}
                        <Button type="primary" style={{backgroundColor:'rgb(50, 197, 210)'}} onClick={this.submit} >Go forward<Icon type="right" /></Button>
                    </div>
                   
                 </div>
            </div>
        )
    }
}
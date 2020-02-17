import {_get, _put} from './index'

// [OK] 获取一定数量的商家
export const getRestaurants = (data) => {
  let req = {
    data: data,
    url: 'shop/nearby/rsts'
  };
  return _get(req);
};

// 获取某个商家具体信息
export const getRestaurant = (data) => {
  let req = {
    url: `shop/rst/${data.restaurant_id}`
    // url: `v1/restaurant/${data.restaurant_id}`
  };
  return _get(req);
};

// 获取食物
export const getFoods = (data) => {
  let req = {
    url: `shop/${data.restaurant_id}/foods`
  };
  return _get(req);
};

// 从购物车减少商品数量
export const reduceShoppingCart = (data) => {
  let req = {
    data,
    url: 'v1/cart'
  };
  return _put(req);
};

// [OK] 输入关键词搜索餐馆
export const searchRestaurant = (data) => {
  let req = {
    data,
    url: 'shop/nearby/rsts/filter'
  };
  return _get(req);
};

//获取评论
export const restaurantComment = (data) => {
  let req = {
    data: data.page,
    url: `comment/rst/${data.rstId}`
  };
  return _get(req);
};


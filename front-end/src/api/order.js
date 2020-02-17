import {_get, _post} from './index'

// [ok]
export const preOrder = (data) => {
  let req = {
    url: `order/pre/${data}`
  };
  return _get(req);
};

// [ok]提交订单
export const submitOrder = (data) => {
  let req = {
    data: data.order,
    url: `order/${data.restaurant_id}`
  };
  return _post(req);
};

// [ok]准备支付
export const initPay = (data) => {
  let req = {
    data,
    url: `pay/order/${data.order_id}`
  };
  return _post(req);
};

// [ok]获取订单信息
export const orderInfo = (data) => {
  let req = {
    url: `order/detail/${data.order_id}`
  };
  return _get(req);
};

// [ok] 获取我的订单
export const orders = (data) => {
  let req = {
    data,
    url: 'order/all/nopage'
  };
  return _get(req);
};

//订单评论
export const makeComment = (data) => {
  let req = {
    data,
    url: 'v1/comment'
  };
  return _post(req)
};

// 废弃 ------------------------------
//请求支付
export const requestPay = (data) => {
  let req = {
    data,
    url: '/https://pay.ispay.cn/core/api/request/pay/'
  };
  return _post(req)
};

//监听扫码支付状态
export const listenStatus = (data) => {
  let req = {
    data,
    url: 'v1/listen_status'
  };
  return _get(req)
};


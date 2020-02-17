import {_get, _delete, _put} from './index'

//提交订单
export const submitCart = (data) => {
  let req = {
    data,
    url: 'shop/cart'
  };
  return _put(req);
};

export const clearCart = (data) => {
  let req = {
    url: `shop/cart/${data}`
  };
  return _delete(req);
};
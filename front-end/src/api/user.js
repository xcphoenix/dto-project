import {_get, _post, _delete, _put} from './index'

// 前端他没写...
// 获取指定收货地址
export const getAddress = (data) => {
  let req = {
    data,
    url: 'admin/address'
  };
  return _get(req)
};

//添加收获地址
export const add_address = (data) => {
  let req = {
    data,
    url: 'user/address'
  };
  return _post(req);
};

export const updateAddress = (data) =>{
  let req = {
    data,
    url: 'admin/update_address'
  };
  return _post(req)
};

// OK -----------------------------------
// [OK] 获取用户所有地址
export const getAllAddress = (data) => {
  let req = {
    data,
    url: '/user/addresses'
  };
  return _get(req);
};

// [OK] 删除收货地址
export const deleteAddress = (data) => {
  let req = {
    url: 'user/address/' + data
  };
  return _delete(req)
};

// [OK] 登录
export const login = (data) => {
  let req = {
    data,
    url: 'login/login_name_pass'
  };
  return _post(req);
};

// [OK] 获取用户信息
export const userInfo = (data) => {
  let req = {
    data,
    url: 'user/avatar'
  };
  return _get(req);
};

// [OK] 改变用户头像
export const changeAvatar = (data) => {
  let req = {
    data: {
      "userAvatar": data
    },
    url: 'user/avatar'
  };
  return _put(req);
};

// 弃用 ----------------------------------------
// 获取我的评论
export const comment = (data) => {
  let req = {
    data,
    url: 'v1/my_comment'
  };
  return _get(req);
};

export const deleteComment = (data) => {
  let req = {
    data,
    url: 'v1/comment'
  };
  return _delete(req);
};


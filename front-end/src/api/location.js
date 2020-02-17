import {_get} from './index'

// [OK] 定位搜索建议
export const suggestion = (data) =>{
  let req = {
    data:data
  };
  req.url = 'location/suggestion';
  return _get(req);
};

// [OK] 定位当前位置
export const location = (data) =>{
  let req = {
    data
  };
  req.url = '/location/ip';
  return _get(req)
};

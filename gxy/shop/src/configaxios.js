import axios from 'axios'

axios.interceptors.request.use(function(config){
    console.log('请求拦截')
    if(localStorage.getItem('token')){
        // 请求时携带token到请求头
        config.headers.Authorization = localStorage.getItem('token')  
    }
    return config
},function(error){
    return Promise.reject(error)
})
axios.interceptors.response.use(function(response){
    console.log('响应拦截')
    console.log(response)
    switch(response.data.code){
        case 10004:
            localStorage.token = response.data.data.token
        case 20001:
            console.log('手机号不存在')
            break;
        case 10002:
            console.log('token已过期')
            // router.replace({
            //     path:'/'
            // })
    }
    return response;
},function(error){
    return Promise.reject(error)
})

export default  axios;
import axios from "axios";

var LoginErrMsg = "查询用户信息失败"
export default {
  isLogin(ygg1412, callback) {
    if(!ygg1412) {
      callback(false, '未登录')
    } else {
      var formData = new FormData()
      formData.append('property', ygg1412)
      axios.post('/initUserInfo', formData)
      .then(function (response) {
        let data = response.data || {}
        if(response.status == 200) {
          if(data.sus != null) {
            callback(data, data.sus)
					} else {
            callback(false, data.error || LoginErrMsg)
					}
        } else {
          callback(false || data.error || LoginErrMsg)
        }
      })
      .catch(function () {
        callback(false, LoginErrMsg)
      });
    }
  }
}
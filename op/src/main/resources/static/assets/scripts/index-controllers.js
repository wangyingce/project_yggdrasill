/***********************************************
 * 页面初始化，find用户登录状态
 ***********************************************/
var formData = new FormData()
formData.append('property', $.cookie('ygg1412'))
$('.loading-div').show()
$.ajax({
    type: 'POST',
    url: '/initUserInfo',
    data: formData,
    cache: false,
    contentType: false,
    processData: false,
    success: function (data) {
        $('.loading-div').hide()
        if(data.sus!=null) {
            // console.log(data)
            if(window.location.pathname.indexOf("login.html")!=-1){//登录成功如果当前是login也跳转index
                location.href = "index.html"
            }
        } else {
            $.removeCookie('ygg1412', {domain: location.hostname});
            location.href = "login.html#User"
            // alert(data.error || '请求失败')
        }
    },
    error: function (data) {
        $.removeCookie('ygg1412', {domain: location.hostname});
        location.href = "./login.html#User"
        $('.loading-div').hide()
        alert('请求失败')
    }
});
/***********************************************
 * 创建模版
 ***********************************************/
// $('#createTemplate').click(function() {
//     $.ajax({
//         type: 'POST',
//         url: '/initUserInfo',
//         data: formData,
//         cache: false,
//         contentType: false,
//         processData: false,
//         success: function (data) {
//             $('.loading-div').hide()
//             if(data.sus!=null) {
//                 // console.log(data)
//                 if(window.location.pathname.indexOf("login.html")!=-1){//登录成功如果当前是login也跳转index
//                     location.href = "index.html"
//                 }
//             } else {
//                 $.removeCookie('ygg1412', {domain: location.hostname});
//                 location.href = "login.html#User"
//                 // alert(data.error || '请求失败')
//             }
//         },
//         error: function (data) {
//             $.removeCookie('ygg1412', {domain: location.hostname});
//             location.href = "./login.html#User"
//             $('.loading-div').hide()
//             alert('请求失败')
//         }
//     });
// })
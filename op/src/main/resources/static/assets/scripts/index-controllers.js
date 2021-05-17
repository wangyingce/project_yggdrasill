/***********************************************
 * 页面初始化，find用户登录状态
 ***********************************************/
function setModelDiv (model) {
    let html = ''
    for (var i = 0; i < model.length; i++) {
        var it = model[i];
        html += '<div class="vlt-timeline-item">' +
            '<div class="row">' +
                '<label class="col-xl-2">' +
                    '<input style="margin-top: 0.4rem;" type="radio" name="tempList" value="' + it.name + '">' +
                '</label>' +
               '<div class="col-xl-4">' +
                '<span class="vlt-timeline-item__date">2021-01-12 12:21:33</span>' +
                    '<h5 class="vlt-timeline-item__title">' + it.name + '</h5>' +
                    '</div>' +
                '<div class="col-xl-6">' +
                    '<p class="vlt-timeline-item__text">' + it.remark + '</p>' +
                '</div>' +
            '</div>' +
        '</div>'
    }
    $('#templist').html(html)
}
var formData = new FormData()
formData.append('property', $.cookie('ygg1412'))
$('.loading-div').show()
var model = []

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
            console.log(data.model)
            model = data.model || []
            setModelDiv(model)
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
$('#gotoengine').click(function() {
    model = $("input[name='tempList']:checked").val()
    if (model) {
        window.open('engine.html?userc=' + $.cookie('ygg1412')+'&model=' + model, "_blank")
    } else {
        alert('请选择一条数据')
    }
})
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
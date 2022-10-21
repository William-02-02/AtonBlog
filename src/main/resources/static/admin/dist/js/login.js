$('#loginButton').click(function () {
    var userName = $('#userName').val();
    var password = $('#password').val();
    var verifyCode = $('#verifyCode').val();
   
    if (isNull(userName)) {
        swal("用户名不能为空", {
            icon: "error",
        });
        return;
    }
    if (isNull(password)) {
        swal("密码不能为空", {
            icon: "error",
        });
        return;
    }
    if (isNull(verifyCode)) {
        swal("验证码不能为空", {
            icon: "error",
        });
        return;
    }
    var url = '/common/kaptcha?d='+ new Date()*1;
    // var swlMessage = '保存成功';
    var data = {
        "userName": userName, "password": password, "verifyCode": verifyCode,
    };
    // if (blogId > 0) {
    //     url = '/admin/blogs/update';
    //     swlMessage = '修改成功';
    //     data = {
    //         "blogId": blogId,
    //         "blogTitle": blogTitle,
    //         "blogSubUrl": blogSubUrl,
    //         "blogCategoryId": blogCategoryId,
    //         "blogTags": blogTags,
    //         "blogContent": blogContent,
    //         "blogCoverImage": blogCoverImage,
    //         "blogStatus": blogStatus,
    //         "enableComment": enableComment
    //     };
    // }
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '返回博客列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            }
            else {
                $('#articleModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

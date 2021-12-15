layui.use(['form','jquery','jquery_cookie','table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table=layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 触发表单
     */
    //submit(addOrUpdateSaleChance)
    form.on("submit(addOrUpdateSaleChance)",function (obj){
        var dataField= obj.field;
        /**
         * 发送ajax
         */
            // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("客官请稍后,正在拼命加载中...",{
                icon:16,   //图标
                time:false, // 不关闭
                shade:0.8 // 设置遮罩的透明度
            });
        var url=ctx+"/Bus_Customer/insert";
        //获取隐藏域信息，决定添加或者是修改
        if( $("input[name=id]").val()){
            url=ctx+"/Bus_Customer/update";
        }
        console.log(obj.field+"hehe");
        $.ajax({
            type:"post",
            url:url,
             data:obj.field,
            dataType:"json",
            error:function(data){
                if(data.code == 200){
                    //添加成功了
                    layer.msg("添加成功了",{icon:6});
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe;
                    layer.closeAll("iframe");
                    //刷新父目录
                    parent.location.reload();
                }else{
                    //添加失败
                    console.log("添加失败...");
                    console.log(data.msg+".....")
                    layer.msg("参数输入错误,请重新输入",{icon: 5 });
                }
            },
            success:function(data){
                if(data.code == 200){
                    //添加成功了
                    layer.msg("添加成功了",{icon:6});
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe;
                    layer.closeAll("iframe");
                    //刷新父目录
                    parent.location.reload();
                }else{
                    //添加失败
                    console.log("添加失败...");
                    console.log(data.msg+".....")
                    layer.msg("参数输入错误,请重新输入",{icon: 5 });
                }
            }
        });
        //阻止表单提交
        return false;
    });

    /**
     * 取消
     */

    $("#closeBtn").click(function(){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
});
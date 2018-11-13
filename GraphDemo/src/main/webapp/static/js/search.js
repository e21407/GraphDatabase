/******************关联关系查询START********************/
function searchForm1(){
    var arrs = $("#searchForm").serializeArray();
    var params ={params:arrs};
    $.ajax({
        type: 'post',
        url: '/searchPath',
        contentType: 'application/json;charset=utf-8',
        data: params,
        success: function (response) { //返回json结果
            console.log(response)

        }
    });

}
/******************关联关系查询END********************/



/******************二次查询START********************/
function searchForm2(){
    var arrs = $("#secondSearchForm").serializeArray();
    var params ={params:arrs};
    console.log('params:'+params);
    $.ajax({
        type: 'post',
        url: '/secondSearch',
        contentType: 'application/json;charset=utf-8',
        data: params,
        success: function (response) { //返回json结果
           // alert(response)
            console.log(response)

        }
    })
    return false;
}
/******************二次查询END********************/



/******************导出START********************/

function exportData() {
    var arrs = $("#exportData").serializeArray();
    var value ;
    $('.common-radio input').each(function(){
        $(this).on('ifChecked', function(event){
            value =$(this).val();
            switch (value) {
                case 'single':
                    exportFun();
                    break;
                case 'multi':
                    break;
                case 'all':
                    break;
            }

            function exportFun(){
                var obj = {};
                obj.vertexIds = chooseNodes;
                obj.edgeIds = chooseNodes;
                var data = JSON.stringify(obj);
                $.ajax({
                    type:'post',
                    url:'/exportExcel',
                    contentType: 'application/json;charset=utf-8',
                    data: data,
                    success:function(response){
                        console.log("response:"+response);
                    }
                });
            }

        });
    })
}
/******************导出END********************/
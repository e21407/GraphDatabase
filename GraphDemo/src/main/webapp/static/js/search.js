/******************关联关系查询START********************/
function searchForm1(){
    var arrs = $("#searchForm").serializeArray();
    var params ={params:arrs};
    var data = JSON.stringify(params);
    $.ajax({
        type: 'post',
        url: '/searchRelationship',
        contentType: 'application/json;charset=utf-8',
        data: data,
        success: function (response){
            var nodes = response.datas;
            var links = response.links;
            if(nodes && links && nodes.length && links.length){
                setNodesAndEdges(nodes,links);
                $('#table').dataTable().fnClearTable();   //将数据清除
                $('#table').dataTable().fnAddData(nodes,true);
            }

        }
    });

}
/******************关联关系查询END********************/



/******************二次查询START********************/
function searchForm2(){
    var arrs = $("#secondSearchForm").serializeArray();
    var params ={params:arrs};
    params.chooseNodesId = [];
    if(chooseNodes && chooseNodes.length){
        for(var i=0;i<chooseNodes.length;i++){
            params.chooseNodesId.push(chooseNodes[i].data.id);
        }
    }

    var data = JSON.stringify(params);
    console.log('params:'+data);
    $.ajax({
        type: 'post',
        url: '/secondSearch',
        contentType: 'application/json;charset=utf-8',
        data: data,
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
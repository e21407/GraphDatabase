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
                reRenderDataTable(nodes);

            }

        }
    });

}
/******************关联关系查询END********************/


/******************重新渲染dataTable START********************/
function reRenderDataTable(json){
    var $table = $('#table');
    $table.dataTable().fnClearTable();   //将数据清除
    $table.dataTable().fnAddData(json,true);
}
/******************重新渲染dataTable END********************/

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
    $.ajax({
        type: 'post',
        url: '/secondSearch',
        contentType: 'application/json;charset=utf-8',
        data: data,
        success: function (response) { //返回json结果
            layer.close(secondSearchLayer);
            secondSearchRender(response);

        }
    })
    return false;
}
/******************二次查询END********************/


/******************二次查询渲染 START********************/
function secondSearchRender(response){
    if(response){
        var secondNodes =response.datas;
        var secondLinks =response.links;
        setNodesAndEdges(secondNodes,secondLinks,"svg2");
        layer.open({
                type: 1,
                //shade: false,
                title: '二次查询结果',
                area:["1920px","800px"],
                content:$("#secondResult")
            },
            function(pass, index){
                layer.close(index);
            });
    }

}
/******************二次查询渲染 END********************/



/******************之前的导出START********************/

function exportData1() {
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
/******************之前的导出END********************/
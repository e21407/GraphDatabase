
var json = {};
$(function(){

    /******************初始化START********************/

    $(".common-radio input").iCheck({
        radioClass:'iradio_square-aero'
    });

    for(var o=1;o<4;o++){
        searchCon(o);
    }

    function searchCon(r){
        $('#searchType'+r).on('changed.bs.select',function(e){
            var value = $(this).selectpicker("val");
            if(value === "0") {
                return ;
            }else{
                $.ajax({
                    type:'get',
                    url:'static/datas/search.json',
                    contentType: 'application/json;charset=utf-8',
                    success:function(response){
                        dealSearchCondition(response);
                    }
                })
            }


            function dealSearchCondition(response){
                var arr = response[value];
                var html = "";
                if(arr && arr.length){
                    for(var i=0;i<arr.length; i++){
                        html += createInput(arr[i]);
                    }
                    $("#condition"+r).html(html);
                }else{
                    return ;
                }

                function createInput(prop){
                    var html ="<div class='line row mt10'>" +
                        "<span class='col-sm-4 m-t-xs text-right mt10'>"+prop.description+"</span>"+
                        "<div class='col-sm-8'>" +
                        "<input class='text-box' name='"+prop.name+"_"+r+"' type='text' value=''/>" +
                        "</div>"+
                        "</div>";
                    return html;
                }
            }

        });

    }

    loadData();

    function loadData(){
        $.getJSON("/getGraph",function(response){
            var nodes = response.datas;
            var links = response.links;
            json = response;
            if(nodes && links && nodes.length && links.length){
                $("#svg").relationPicture({json:response})
                renderDataTable(nodes);
            }
        });
    }

    /******************初始化END********************/


    /*********渲染列表  START*********/
    function renderDataTable(nodes){

        $('#table').DataTable( {
            //"ajax": "./static/datas/data.txt",
            data:nodes,
            pageLength:6,
            lengthChange: false,
            searching: false,
            info: "",
            columns: [
                { "data": "id" },
                { "data": "name" },
                { "data": "descript" },
                { "data": "symbol" },
                { "data": "type" },
                { "data": "showLabelText" },
                { "data": "width" },
                { "data": "height" }
            ]
        } );
    }
    /*********渲染列表  END*********/


    /******************控制查询框的显示隐藏START********************/
    $("#toggle").click(function(){
        var $box =  $("#searchBox");
        var $left = $("#left");
        var  $font= $("#font")
        if($box.css('display') === "block"){
            $left.css({"width":"68px"});
            $box.hide();
            $font.addClass("icon-toggle").removeClass("icon-close");
        }else{
            $left.css({"width":"388px"});
            $box.show();
            $font.addClass("icon-close").removeClass("icon-toggle");
        }
    });

    /******************控制查询框的显示隐藏END********************/

    /******************重置select START********************/
    $("#reset").on("click",function(){
        handleReset();
    });
    /******************重置select END********************/
});



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
                $("#svg").relationPicture("setNodesAndEdges",nodes,links);
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
    var $svg =$("#svg");
    var arrs = $("#secondSearchForm").serializeArray();
    var chooseNodes = $svg.relationPicture("getChooseNodes");
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

            var index =$svg.relationPicture("getSecondLayerIndex");
            layer.close(index);
            secondSearchRender(response);
        }
    })
    return false;
}
/******************二次查询END********************/


/******************二次查询渲染 START********************/
function secondSearchRender(response){
    if(response){
        var nodes = response.datas;
        var links = response.links;
        if(nodes && links && nodes.length && links.length){
            $("#svg2").relationPicture({json:response,id:"svg2",width:600,height:600});
            layer.open({
                    type: 1,
                    //shade: false,
                    title: '二次查询结果',
                    area:["600px","600px"],
                    content:$("#secondResult")
                },
                function(pass, index){
                    layer.close(index);
                });
            }
        }


}
/******************二次查询渲染 END********************/

/*****重置操作处理  START******/
function handleReset(){
    $(".selectpicker").selectpicker("val","");
    $('.selectpicker').selectpicker('refresh');
    $(".condition .text-box").val("");
}
/*****重置操作处理  END******/



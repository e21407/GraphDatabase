function searchForm1(){
    var obj = {};
    var arrs = $("#searchForm").serializeArray();
    obj.option = "all";
    obj.vertexIds = [];
    $.each(arrs, function(index,arr) {
        if(arr.value){
            obj.vertexIds .push(arr.value);
        }
    });
    loadData(obj);

}
function searchShortPath(arr){
    var obj = {};
    obj.option = "shortest";
    obj.vertexIds = arr;
    loadData(obj);
}
function loadData(obj) {
    var data = JSON.stringify(obj);
    $.ajax({
        type: 'post',
        url: '/searchPath',
        contentType: 'application/json;charset=utf-8',
        data: data,
        success: function (response) { //返回json结果
            console.log(response)
            if(obj.option ==="all"){
                //更新节点关系图
            }else{
                //展示最短路径
            }

        }
    })
}
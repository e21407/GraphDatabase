function searchForm1(){
    var obj = {};
    var arrs = $("#searchForm").serializeArray();
    $.each(arrs, function(index,arr) {
        obj[arr.name] = arr.value;
    });
    var data = JSON.stringify(obj);
    console.log(data);
    $.ajax({
        type: 'post',
        url: 'search',
        contentType: 'application/json;charset=utf-8',
        data: data,
        success: function (response) { //返回json结果
            console.log(response)
        }
    })
}
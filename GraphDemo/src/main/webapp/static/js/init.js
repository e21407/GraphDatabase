var nodeIds = [];
var links = [];
var nodeIds2 = [];
var links2 = [];
var isClicked =false;
var params = {};
var dom= document.getElementById("echart");//获取对象
var echart = echarts.init(dom);//初始化
var option ={};
var json ={};
function eConsole(param) {
    if(param.dataType ==="edge"){
        return;
    }
    var mes = '';
    if (typeof param.seriesIndex != 'undefined') {
        mes += ' 系列id : ' + param.seriesId;
        mes += ' <br/> 系列名称 : ' + param.seriesName;
        mes += ' <br/> 系列索引 : ' + param.seriesIndex;
        mes += ' <br/>数据索引 : ' + param.dataIndex;
        mes += ' <br/> 颜色 : ' + param.color;
        mes += ' <br/> 数据索引 : ' + param.data.id;
        mes += '<br/> '+param.data.descript+': ' + param.data.name;
        mes += ' <br/> 关系数量 : ' + param.data.value;
        mes += ' <br/> 图表类型 : ' + param.seriesType;
    }
    if (param.type == 'hover') {
        document.getElementById('infoHtml').innerHTML = 'Event Console : ' + mes;
    }
    else {
        document.getElementById('infoHtml').innerHTML ="<p>基本属性</p>"+ mes;
        document.getElementById('info').style.display ="block";
    }
    console.log(param);


    getData(param);
}

function isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
        if(value === arr[i]){
            return true;
        }
    }
    return false;
}

function isInArrayLinks(arr,link){
    for(var i = 0; i < arr.length; i++){
        if(link.source === arr[i].source && link.target ===arr[i].target){
            return true;
        }
    }
    return false;
}


function getNodesById(id){
    var nodes =[];
    if(!isInArray(nodeIds,id)){
        nodeIds.push(id);
    }
    for(var j=0; j<json.links.length;j++){
        if(id === json.links[j].source){
            if(!isInArrayLinks(links,json.links[j])){
                links.push(json.links[j]);
            }
            if(!isInArray(nodeIds,json.links[j].target)){
                nodeIds.push(json.links[j].target)
            }
        }else if( id === json.links[j].target){
            if(!isInArrayLinks(links,json.links[j])){
                links.push(json.links[j]);
            }
            if(!isInArray(nodeIds,json.links[j].source)) {

                nodeIds.push(json.links[j].source)
            }
        }
    }

    for(var k=0;k<nodeIds.length;k++){
        for(var i=0; i<json.datas.length;i++){
            if(nodeIds[k]=== json.datas[i].id){
                nodes.push(json.datas[i]);
            }
        }

    }

    option.series[0].data = nodes;
    option.series[0].edges = links;
}
function getNodesById2(id){
    var nodes2 =[];
    if(!isInArray(nodeIds2,id)){
        nodeIds2.push(id);
    }
    for(var j=0; j<json.links.length;j++){
        if(id === json.links[j].source){
            if(!isInArrayLinks(links2,json.links[j])){
                links2.push(json.links[j]);
            }
            if(!isInArray(nodeIds2,json.links[j].target)){
                nodeIds2.push(json.links[j].target)
            }
        }else if( id === json.links[j].target){
            if(!isInArrayLinks(links2,json.links[j])){
                links2.push(json.links[j]);
            }
            if(!isInArray(nodeIds2,json.links[j].source)) {

                nodeIds2.push(json.links[j].source)
            }
        }
    }

    for(var k=0;k<nodeIds2.length;k++){
        for(var i=0; i<json.datas.length;i++){
            if(nodeIds2[k]=== json.datas[i].id){
                nodes2.push(json.datas[i]);
            }
        }

    }

    console.log("nodeIds2:"+nodeIds2.length)
    console.log("links2:"+links2.length)
    return nodes2;
}

function getData(param) {
    if (param.data.size === 1 && isClicked === true) {
        alert("没有下钻咯！");
        return;
    }
    isClicked = true;
    getNodesById(param.data.id);

    echart.clear();
    dom.innerHTML = "";
    dom.removeAttribute('_echarts_instance_');
    echart=echarts.init(dom);
    echart.setOption(option,true);

    //点击事件的处理
    echart.on("click", eConsole);
    echart.on('mouseup',dragFixed);

}

function deleteNodeAndLinksById(value){
    if(!isInArray(nodeIds2,value)) {
        nodeIds2.splice(value,1);
    }
    for(var i=0;i<links2.length;i++){
        if(value===links2[i].source || value===links2[i].target){
            links2.splice(links[i],1);
        }
    }
}
function initSelect(id){
    $('#'+id).on('changed.bs.select',function(e){
        var value = $(this).selectpicker("val");
        params[id] =value;
        if(value === "0") {
            for (var i in params) {
                if(i ==="id"){
                    delete params[i];
                }
            }
        }

        console.log("params:"+params)
    });
}
function loadDatas2(){
    echart.showLoading();
    var nodes;
    link2 = [];
    nodeIds2 = [];
    for(var i in params){
        nodes = getNodesById2(params[i]);
        console.log("key:"+i+"value:"+params[i]);
    }
    echart.hideLoading();
    option.series[0].data = nodes;
    option.series[0].edges =links2;
    //option.series[0].layout = 'circular';
    console.log("nodes"+nodes+"links2:"+links2);
    echart.clear();
    echart.setOption(option,true);
}

function dragFixed(params){
        var option=echart.getOption();
        option.series[0].data[params.dataIndex].x=params.event.offsetX;
        option.series[0].data[params.dataIndex].y=params.event.offsetY;
        option.series[0].data[params.dataIndex].fixed=true;
    echart.setOption(option);
}

function resetDats (json){
    for(var j=0;j<json.links.length;j++){

        json.links[j].label ={normal:{show:false} };
        json.links[j].lineStyle ={normal:{color:"#cfcfcf"}}


        if(json.links[j].target==="sq2356" && json.links[j].source==="sq2"
            ||json.links[j].target==="sq2356"&&json.links[j].source==="sq1"
            ||json.links[j].target==="sq2356"&&json.links[j].source==="sq3"
            ||json.links[j].target==="sq3141"&&json.links[j].source==="sq1"
            ||json.links[j].target==="sq3141"&&json.links[j].source==="sq2"
            ||json.links[j].target==="sq3141"&&json.links[j].source==="sq3"
            ||json.links[j].target==="sq2757"&&json.links[j].source==="sq17"
            ||json.links[j].target==="sq3542"&&json.links[j].source==="sq17"
            ||json.links[j].target==="sq2431"&&json.links[j].source==="sq8"
            ||json.links[j].target==="sq2431"&&json.links[j].source==="sq9"
            ||json.links[j].source==="sq12"&&json.links[j].target==="sq3425"
        ){
            json.links[j].lineStyle ={normal:{width:3,color:"#c23531"}};
        }
    }


    for(var k=0;k<json.datas.length;k++){
        json.datas[k].label ={emphasis:{color:"#c23531",fontWeight:"bold",fontSize:16}};
        if(json.datas[k].value>9 || json.datas[k].id ==="sq2757"|| json.datas[k].id ==="sq17"
            || json.datas[k].id ==="sq3542" || json.datas[k].id ==="sq2431" || json.datas[k].id ==="sq12" || json.datas[k].id ==="sq8"
            || json.datas[k].id ==="sq9" || json.datas[k].id ==="sq3425" ){
            json.datas[k].label = { normal:{show:true,fontSize:14},emphasis:{color:"#c23531",fontWeight:"bold",fontSize:20}}
        }
        json.datas[k].draggable= true;

    }

    return json;
}

function getLegendData(categories){
    var arr = [];
    for(var i = 0;i<categories.length;i++){
        var obj = {name:categories[i].name};
        arr.push(obj);
    }
    return arr;
}


function initEchart(){
    echart.showLoading();
    /*$.ajax({
        url:"./data/datas_0910_2.json",
        type:"get",
        dataType:"json",
        success:function (result) {*/

    var result ={"datas": [
                    {"id":"cu1","descript":"客户号","name":"600118772312","category":0,"value":9},
                    {"id":"cu783","descript":"客户电话","name":"13009040001","category":0,"value":1},
                    {"id":"cu1565","descript":"所属公司编号","name":"92440300731093239W","category":0,"value":4},
                    {"id":"zh1","descript":"账户号","name":"2998000050900158","category":1,"value":9},
                    {"id":"zh785","descript":"邮编","name":"518000","category":1,"value":1},
                    {"id":"zh1569","descript":"地址","name":"广东省深圳市龙华弓村和平小区 ","category":1,"value":1},
                    {"id":"zh2353","descript":"城市编号","name":"440300","category":1,"value":1},
                    {"id":"sq1","descript":"申请书编号","name":"sq201809041000101","category":2,"value":10},
                    {"id":"sq786","descript":"证件号码","name":"230102198002012001","category":2,"value":1},
                    {"id":"sq1571","descript":"单位电话","name":"075586941101","category":2,"value":1},
                    {"id":"sq2356","descript":"推广机构id","name":"广东省深圳市招商银行新安支行","category":3,"value":12},
                    {"id":"sq3141","descript":"推广人id","name":"07551001","category":3,"value":12},
                    {"id":"K1","descript":"卡号","name":"201809043000101","category":3,"value":8},

                    {"id":"zh2","descript":"账户号","name":"2998000050900159","category":1,"value":9},
                    {"id":"zh786","descript":"邮编","name":"518000","category":1,"value":1},
                    {"id":"zh1570","descript":"地址","name":"广东省深圳市龙华弓村和平小区 ","category":1,"value":1},
                    {"id":"zh2354","descript":"城市编号","name":"440300","category":1,"value":1},
                    {"id":"sq2","descript":"申请书编号","name":"sq201809041000102","category":2,"value":10},
                    {"id":"sq787","descript":"证件号码","name":"230102198002012001","category":2,"value":1},
                    {"id":"sq1572","descript":"单位电话","name":"075586941101","category":2,"value":1},
                    {"id":"sq3","descript":"申请书编号","name":"sq201809041000103","category":2,"value":10},
                    {"id":"sq788","descript":"证件号码","name":"230102198002012001","category":2,"value":1},
                    {"id":"sq1573","descript":"单位电话","name":"075586941101","category":2,"value":1},
                    {"id":"cu9","descript":"客户号","name":"600118772320","category":0,"value":5},
                    {"id":"cu791","descript":"客户电话","name":"13009040009","category":0,"value":1},
                    {"id":"cu1573","descript":"所属公司编号","name":"52310000MJ4925800H","category":0,"value":2},
                    {"id":"zh11","descript":"账户号","name":"2998000050900168","category":1,"value":6},
                    {"id":"zh795","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1579","descript":"地址","name":"上海市枫蓝国际中心","category":1,"value":1},
                    {"id":"zh2363","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq12","descript":"申请书编号","name":"sq201809041000901","category":2,"value":8},
                    {"id":"sq797","descript":"证件号码","name":"230102198112051005","category":2,"value":1},
                    {"id":"sq1582","descript":"单位电话","name":"02162200009","category":2,"value":1},
                    {"id":"sq2640","descript":"推广机构id","name":"广东省深圳市招商银行科发支行","category":3,"value":8},
                    {"id":"sq3425","descript":"推广人id","name":"07551003","category":3,"value":8},
                    {"id":"K10","descript":"卡号","name":"201809043000901","category":3,"value":5},
                    {"id":"cu10","descript":"客户号","name":"600118772321","category":0,"value":5},
                    {"id":"cu792","descript":"客户电话","name":"13009040010","category":0,"value":1},
                    {"id":"cu1574","descript":"所属公司编号","name":"91310109776266201K","category":0,"value":2},
                    {"id":"zh12","descript":"账户号","name":"2998000050900169","category":1,"value":6},
                    {"id":"zh796","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1580","descript":"地址","name":"上海市季景沁园","category":1,"value":1},
                    {"id":"zh2364","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq13","descript":"申请书编号","name":"sq201809041001001","category":2,"value":8},
                    {"id":"sq798","descript":"证件号码","name":"230102198112061006","category":2,"value":1},
                    {"id":"sq1583","descript":"单位电话","name":"02162200010","category":2,"value":1},
                    {"id":"K11","descript":"卡号","name":"201809043001001","category":3,"value":5},
                    {"id":"cu11","descript":"客户号","name":"600118772322","category":0,"value":5},
                    {"id":"cu793","descript":"客户电话","name":"13009040011","category":0,"value":1},
                    {"id":"cu1575","descript":"所属公司编号","name":"91310118MA1JM3K36M","category":0,"value":2},
                    {"id":"zh13","descript":"账户号","name":"2998000050900170","category":1,"value":6},
                    {"id":"zh797","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1581","descript":"地址","name":"上海市顺驰蓝调国际","category":1,"value":1},
                    {"id":"zh2365","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq14","descript":"申请书编号","name":"sq201809041001101","category":2,"value":8},
                    {"id":"sq799","descript":"证件号码","name":"230102198112071007","category":2,"value":1},
                    {"id":"sq1584","descript":"单位电话","name":"02162200011","category":2,"value":1},
                    {"id":"sq2757","descript":"推广机构id","name":"广东省深圳市招商银行科苑支行","category":3,"value":10},
                    {"id":"sq3542","descript":"推广人id","name":"07551004","category":3,"value":10},
                    {"id":"K12","descript":"卡号","name":"201809043001101","category":3,"value":5},
                    {"id":"cu12","descript":"客户号","name":"600118772323","category":0,"value":5},
                    {"id":"cu794","descript":"客户电话","name":"13009040012","category":0,"value":1},
                    {"id":"cu1576","descript":"所属公司编号","name":"91310118574170279B","category":0,"value":2},
                    {"id":"zh14","descript":"账户号","name":"2998000050900171","category":1,"value":6},
                    {"id":"zh798","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1582","descript":"地址","name":"上海市阳光斯坦福","category":1,"value":1},
                    {"id":"zh2366","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq15","descript":"申请书编号","name":"sq201809041001201","category":2,"value":8},
                    {"id":"sq800","descript":"证件号码","name":"230102198112081008","category":2,"value":1},
                    {"id":"sq1585","descript":"单位电话","name":"02162200012","category":2,"value":1},
                    {"id":"K13","descript":"卡号","name":"201809043001201","category":3,"value":5},
                    {"id":"cu13","descript":"客户号","name":"600118772324","category":0,"value":5},
                    {"id":"cu795","descript":"客户电话","name":"13009040013","category":0,"value":1},
                    {"id":"cu1577","descript":"所属公司编号","name":"52310230MJ5339125F","category":0,"value":2},
                    {"id":"zh15","descript":"账户号","name":"2998000050900172","category":1,"value":6},
                    {"id":"zh799","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1583","descript":"地址","name":"上海市西BD","category":1,"value":1},
                    {"id":"zh2367","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq16","descript":"申请书编号","name":"sq201809041001301","category":2,"value":8},
                    {"id":"sq801","descript":"证件号码","name":"230102198112091009","category":2,"value":1},
                    {"id":"sq1586","descript":"单位电话","name":"02162200013","category":2,"value":1},
                    {"id":"K14","descript":"卡号","name":"201809043001301","category":3,"value":5},
                    {"id":"cu14","descript":"客户号","name":"600118772325","category":0,"value":5},
                    {"id":"cu796","descript":"客户电话","name":"13009040014","category":0,"value":1},
                    {"id":"cu1578","descript":"所属公司编号","name":"52310105MJ50200186","category":0,"value":2},
                    {"id":"zh16","descript":"账户号","name":"2998000050900173","category":1,"value":6},
                    {"id":"zh800","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1584","descript":"地址","name":"上海市北苑家园","category":1,"value":1},
                    {"id":"zh2368","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq17","descript":"申请书编号","name":"sq201809041001401","category":2,"value":8},
                    {"id":"sq802","descript":"证件号码","name":"230102198112101010","category":2,"value":1},
                    {"id":"sq1587","descript":"单位电话","name":"02162200014","category":2,"value":1},
                    {"id":"K15","descript":"卡号","name":"201809043001401","category":3,"value":5},
                    {"id":"cu15","descript":"客户号","name":"600118772326","category":0,"value":5},
                    {"id":"cu797","descript":"客户电话","name":"13009040015","category":0,"value":1},
                    {"id":"cu1579","descript":"所属公司编号","name":"53310000MJ49517770","category":0,"value":2},
                    {"id":"zh17","descript":"账户号","name":"2998000050900174","category":1,"value":6},
                    {"id":"zh801","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1585","descript":"地址","name":"上海市威尼斯花园","category":1,"value":1},
                    {"id":"zh2369","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq18","descript":"申请书编号","name":"sq201809041001501","category":2,"value":8},
                    {"id":"sq803","descript":"证件号码","name":"230102198112111011","category":2,"value":1},
                    {"id":"sq1588","descript":"单位电话","name":"02162200015","category":2,"value":1},
                    {"id":"K16","descript":"卡号","name":"201809043001501","category":3,"value":5},
                    {"id":"K2","descript":"卡号","name":"201809043000102","category":3,"value":8},
                    {"id":"cu2","descript":"客户号","name":"600118772313","category":0,"value":5},
                    {"id":"cu784","descript":"客户电话","name":"13009040002","category":0,"value":1},
                    {"id":"cu1566","descript":"所属公司编号","name":"51440300MJL1712376","category":0,"value":2},
                    {"id":"zh3","descript":"账户号","name":"2998000050900160","category":1,"value":6},
                    {"id":"zh787","descript":"邮编","name":"518000","category":1,"value":1},
                    {"id":"zh1571","descript":"地址","name":"广东省深圳市泰宁小区 ","category":1,"value":1},
                    {"id":"zh2355","descript":"城市编号","name":"440300","category":1,"value":1},
                    {"id":"sq4","descript":"申请书编号","name":"sq201809041000201","category":2,"value":8},
                    {"id":"sq789","descript":"证件号码","name":"230102198202012002","category":2,"value":1},
                    {"id":"sq1574","descript":"单位电话","name":"075586941102","category":2,"value":1},
                    {"id":"K3","descript":"卡号","name":"201809043000201","category":3,"value":5},
                    {"id":"cu3","descript":"客户号","name":"600118772314","category":0,"value":7},
                    {"id":"cu785","descript":"客户电话","name":"13009040003","category":0,"value":1},
                    {"id":"cu1567","descript":"所属公司编号","name":"52440300MJL186084F","category":0,"value":3},
                    {"id":"zh4","descript":"账户号","name":"2998000050900161","category":1,"value":7},
                    {"id":"zh788","descript":"邮编","name":"518000","category":1,"value":1},
                    {"id":"zh1572","descript":"地址","name":"广东省深圳市科技园住宅小区 ","category":1,"value":1},
                    {"id":"zh2356","descript":"城市编号","name":"440300","category":1,"value":1},
                    {"id":"sq5","descript":"申请书编号","name":"sq201809041000301","category":2,"value":9},
                    {"id":"sq790","descript":"证件号码","name":"230102198112012032","category":2,"value":1},
                    {"id":"sq1575","descript":"单位电话","name":"075586941103","category":2,"value":1},
                    {"id":"K4","descript":"卡号","name":"201809043000301","category":3,"value":7},
                    {"id":"zh5","descript":"账户号","name":"2998000050900162","category":1,"value":7},
                    {"id":"zh789","descript":"邮编","name":"518000","category":1,"value":1},
                    {"id":"zh1573","descript":"地址","name":"广东省深圳市科技园住宅小区 ","category":1,"value":1},
                    {"id":"zh2357","descript":"城市编号","name":"440300","category":1,"value":1},
                    {"id":"sq6","descript":"申请书编号","name":"sq201809041000302","category":2,"value":9},
                    {"id":"sq791","descript":"证件号码","name":"230102198112012032","category":2,"value":1},
                    {"id":"sq1576","descript":"单位电话","name":"075586941103","category":2,"value":1},
                    {"id":"cu4","descript":"客户号","name":"600118772315","category":0,"value":5},
                    {"id":"cu786","descript":"客户电话","name":"13009040004","category":0,"value":1},
                    {"id":"cu1568","descript":"所属公司编号","name":"913101124250883359","category":0,"value":2},
                    {"id":"zh6","descript":"账户号","name":"2998000050900163","category":1,"value":6},
                    {"id":"zh790","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1574","descript":"地址","name":"上海市顶秀青溪","category":1,"value":1},
                    {"id":"zh2358","descript":"城市编号","name":"310000","category":1,"value":1},
                    {"id":"sq7","descript":"申请书编号","name":"sq201809041000401","category":2,"value":8},
                    {"id":"sq792","descript":"证件号码","name":"230102198112012033","category":2,"value":1},
                    {"id":"sq1577","descript":"单位电话","name":"02162200004","category":2,"value":1},
                    {"id":"K5","descript":"卡号","name":"201809043000401","category":3,"value":5},
                    {"id":"cu5","descript":"客户号","name":"600118772316","category":0,"value":5},
                    {"id":"cu787","descript":"客户电话","name":"13009040005","category":0,"value":1},
                    {"id":"cu1569","descript":"所属公司编号","name":"91310105134649745E","category":0,"value":2},
                    {"id":"zh7","descript":"账户号","name":"2998000050900164","category":1,"value":6},
                    {"id":"zh791","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1575","descript":"地址","name":"上海市中景濠庭","category":1,"value":1},
                    {"id":"zh2359","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq8","descript":"申请书编号","name":"sq201809041000501","category":2,"value":8},
                    {"id":"sq793","descript":"证件号码","name":"230102198112011001","category":2,"value":1},
                    {"id":"sq1578","descript":"单位电话","name":"02162200005","category":2,"value":1},
                    {"id":"sq2431","descript":"推广机构id","name":"广东省深圳市招商银行龙华支行","category":3,"value":4},
                    {"id":"sq3216","descript":"推广人id","name":"07551002","category":3,"value":4},
                    {"id":"K6","descript":"卡号","name":"201809043000501","category":3,"value":5},
                    {"id":"cu6","descript":"客户号","name":"600118772317","category":0,"value":5},
                    {"id":"cu788","descript":"客户电话","name":"13009040006","category":0,"value":1},
                    {"id":"cu1570","descript":"所属公司编号","name":"91310115771471337E","category":0,"value":2},
                    {"id":"zh8","descript":"账户号","name":"2998000050900165","category":1,"value":6},
                    {"id":"zh792","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1576","descript":"地址","name":"上海市融城","category":1,"value":1},
                    {"id":"zh2360","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq9","descript":"申请书编号","name":"sq201809041000601","category":2,"value":8},
                    {"id":"sq794","descript":"证件号码","name":"230102198112021002","category":2,"value":1},
                    {"id":"sq1579","descript":"单位电话","name":"02162200006","category":2,"value":1},
                    {"id":"K7","descript":"卡号","name":"201809043000601","category":3,"value":5},
                    {"id":"cu7","descript":"客户号","name":"600118772318","category":0,"value":5},
                    {"id":"cu789","descript":"客户电话","name":"13009040007","category":0,"value":1},
                    {"id":"cu1571","descript":"所属公司编号","name":"52310104MJ49963266","category":0,"value":2},
                    {"id":"zh9","descript":"账户号","name":"2998000050900166","category":1,"value":6},
                    {"id":"zh793","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1577","descript":"地址","name":"上海市西钓鱼台","category":1,"value":1},
                    {"id":"zh2361","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq10","descript":"申请书编号","name":"sq201809041000701","category":2,"value":8},
                    {"id":"sq795","descript":"证件号码","name":"230102198112031003","category":2,"value":1},
                    {"id":"sq1580","descript":"单位电话","name":"02162200007","category":2,"value":1},
                    {"id":"K8","descript":"卡号","name":"201809043000701","category":3,"value":5},
                    {"id":"cu8","descript":"客户号","name":"600118772319","category":0,"value":5},
                    {"id":"cu790","descript":"客户电话","name":"13009040008","category":0,"value":1},
                    {"id":"cu1572","descript":"所属公司编号","name":"52310107MJ5075334L","category":0,"value":2},
                    {"id":"zh10","descript":"账户号","name":"2998000050900167","category":1,"value":6},
                    {"id":"zh794","descript":"邮编","name":"201100","category":1,"value":1},
                    {"id":"zh1578","descript":"地址","name":"上海市绣江南","category":1,"value":1},
                    {"id":"zh2362","descript":"城市编号","name":"310000 ","category":1,"value":1},
                    {"id":"sq11","descript":"申请书编号","name":"sq201809041000801","category":2,"value":8},
                    {"id":"sq796","descript":"证件号码","name":"230102198112041004","category":2,"value":1},
                    {"id":"sq1581","descript":"单位电话","name":"02162200008","category":2,"value":1},
                    {"id":"K9","descript":"卡号","name":"201809043000801","category":3,"value":5}
                ],
                "links":[
                    {"source":"cu1","target":"cu783"},
                    {"source":"cu1","target":"cu1565"},
                    {"source":"zh1","target":"zh785"},
                    {"source":"zh1","target":"zh1569"},
                    {"source":"zh1","target":"zh2353"},
                    {"source":"sq1","target":"sq786"},
                    {"source":"sq1","target":"sq1571"},
                    {"source":"sq1","target":"cu1565"},
                    {"source":"sq1","target":"sq2356"},
                    {"source":"sq1","target":"sq3141"},
                    {"source":"K1","target":"sq2356"},
                    {"source":"K1","target":"sq3141"},
                    {"source":"cu1","target":"zh1"},
                    {"source":"cu1","target":"sq1"},
                    {"source":"cu1","target":"K1"},
                    {"source":"zh1","target":"sq1"},
                    {"source":"zh1","target":"K1"},
                    {"source":"sq1","target":"K1"},
                    {"source":"zh2","target":"zh786"},
                    {"source":"zh2","target":"zh1570"},
                    {"source":"zh2","target":"zh2354"},
                    {"source":"cu1","target":"zh2"},
                    {"source":"zh2","target":"sq1"},
                    {"source":"zh2","target":"K1"},
                    {"source":"sq2","target":"sq787"},
                    {"source":"sq2","target":"sq1572"},
                    {"source":"sq2","target":"cu1565"},
                    {"source":"sq2","target":"sq2356"},
                    {"source":"sq2","target":"sq3141"},
                    {"source":"cu1","target":"sq2"},
                    {"source":"zh1","target":"sq2"},
                    {"source":"sq2","target":"K1"},
                    {"source":"zh2","target":"sq2"},
                    {"source":"sq3","target":"sq788"},
                    {"source":"sq3","target":"sq1573"},
                    {"source":"sq3","target":"cu1565"},
                    {"source":"sq3","target":"sq2356"},
                    {"source":"sq3","target":"sq3141"},
                    {"source":"cu1","target":"sq3"},
                    {"source":"zh1","target":"sq3"},
                    {"source":"sq3","target":"K1"},
                    {"source":"zh2","target":"sq3"},
                    {"source":"K2","target":"sq2356"},
                    {"source":"K2","target":"sq3141"},
                    {"source":"cu1","target":"K2"},
                    {"source":"zh1","target":"K2"},
                    {"source":"sq1","target":"K2"},
                    {"source":"zh2","target":"K2"},
                    {"source":"sq2","target":"K2"},
                    {"source":"sq3","target":"K2"},
                    {"source":"cu2","target":"cu784"},
                    {"source":"cu2","target":"cu1566"},
                    {"source":"zh3","target":"zh787"},
                    {"source":"zh3","target":"zh1571"},
                    {"source":"zh3","target":"zh2355"},
                    {"source":"sq4","target":"sq789"},
                    {"source":"sq4","target":"sq1574"},
                    {"source":"sq4","target":"cu1566"},
                    {"source":"sq4","target":"sq2356"},
                    {"source":"sq4","target":"sq3141"},
                    {"source":"K3","target":"sq2356"},
                    {"source":"K3","target":"sq3141"},
                    {"source":"cu2","target":"zh3"},
                    {"source":"cu2","target":"sq4"},
                    {"source":"cu2","target":"K3"},
                    {"source":"zh3","target":"sq4"},
                    {"source":"zh3","target":"K3"},
                    {"source":"sq4","target":"K3"},
                    {"source":"cu3","target":"cu785"},
                    {"source":"cu3","target":"cu1567"},
                    {"source":"zh4","target":"zh788"},
                    {"source":"zh4","target":"zh1572"},
                    {"source":"zh4","target":"zh2356"},
                    {"source":"sq5","target":"sq790"},
                    {"source":"sq5","target":"sq1575"},
                    {"source":"sq5","target":"cu1567"},
                    {"source":"sq5","target":"sq2356"},
                    {"source":"sq5","target":"sq3141"},
                    {"source":"K4","target":"sq2356"},
                    {"source":"K4","target":"sq3141"},
                    {"source":"cu3","target":"zh4"},
                    {"source":"cu3","target":"sq5"},
                    {"source":"cu3","target":"K4"},
                    {"source":"zh4","target":"sq5"},
                    {"source":"zh4","target":"K4"},
                    {"source":"sq5","target":"K4"},
                    {"source":"zh5","target":"zh789"},
                    {"source":"zh5","target":"zh1573"},
                    {"source":"zh5","target":"zh2357"},
                    {"source":"cu3","target":"zh5"},
                    {"source":"zh5","target":"sq5"},
                    {"source":"zh5","target":"K4"},
                    {"source":"sq6","target":"sq791"},
                    {"source":"sq6","target":"sq1576"},
                    {"source":"sq6","target":"cu1567"},
                    {"source":"sq6","target":"sq2356"},
                    {"source":"sq6","target":"sq3141"},
                    {"source":"cu3","target":"sq6"},
                    {"source":"zh4","target":"sq6"},
                    {"source":"sq6","target":"K4"},
                    {"source":"zh5","target":"sq6"},
                    {"source":"cu4","target":"cu786"},
                    {"source":"cu4","target":"cu1568"},
                    {"source":"zh6","target":"zh790"},
                    {"source":"zh6","target":"zh1574"},
                    {"source":"zh6","target":"zh2358"},
                    {"source":"sq7","target":"sq792"},
                    {"source":"sq7","target":"sq1577"},
                    {"source":"sq7","target":"cu1568"},
                    {"source":"sq7","target":"sq2356"},
                    {"source":"sq7","target":"sq3141"},
                    {"source":"K5","target":"sq2356"},
                    {"source":"K5","target":"sq3141"},
                    {"source":"cu4","target":"zh6"},
                    {"source":"cu4","target":"sq7"},
                    {"source":"cu4","target":"K5"},
                    {"source":"zh6","target":"sq7"},
                    {"source":"zh6","target":"K5"},
                    {"source":"sq7","target":"K5"},
                    {"source":"cu5","target":"cu787"},
                    {"source":"cu5","target":"cu1569"},
                    {"source":"zh7","target":"zh791"},
                    {"source":"zh7","target":"zh1575"},
                    {"source":"zh7","target":"zh2359"},
                    {"source":"sq8","target":"sq793"},
                    {"source":"sq8","target":"sq1578"},
                    {"source":"sq8","target":"cu1569"},
                    {"source":"sq8","target":"sq2431"},
                    {"source":"sq8","target":"sq3216"},
                    {"source":"K6","target":"sq2431"},
                    {"source":"K6","target":"sq3216"},
                    {"source":"cu5","target":"zh7"},
                    {"source":"cu5","target":"sq8"},
                    {"source":"cu5","target":"K6"},
                    {"source":"zh7","target":"sq8"},
                    {"source":"zh7","target":"K6"},
                    {"source":"sq8","target":"K6"},
                    {"source":"cu6","target":"cu788"},
                    {"source":"cu6","target":"cu1570"},
                    {"source":"zh8","target":"zh792"},
                    {"source":"zh8","target":"zh1576"},
                    {"source":"zh8","target":"zh2360"},
                    {"source":"sq9","target":"sq794"},
                    {"source":"sq9","target":"sq1579"},
                    {"source":"sq9","target":"cu1570"},
                    {"source":"sq9","target":"sq2431"},
                    {"source":"sq9","target":"sq3216"},
                    {"source":"K7","target":"sq2431"},
                    {"source":"K7","target":"sq3216"},
                    {"source":"cu6","target":"zh8"},
                    {"source":"cu6","target":"sq9"},
                    {"source":"cu6","target":"K7"},
                    {"source":"zh8","target":"sq9"},
                    {"source":"zh8","target":"K7"},
                    {"source":"sq9","target":"K7"},
                    {"source":"cu7","target":"cu789"},
                    {"source":"cu7","target":"cu1571"},
                    {"source":"zh9","target":"zh793"},
                    {"source":"zh9","target":"zh1577"},
                    {"source":"zh9","target":"zh2361"},
                    {"source":"sq10","target":"sq795"},
                    {"source":"sq10","target":"sq1580"},
                    {"source":"sq10","target":"cu1571"},
                    {"source":"sq10","target":"sq2640"},
                    {"source":"sq10","target":"sq3425"},
                    {"source":"K8","target":"sq2640"},
                    {"source":"K8","target":"sq3425"},
                    {"source":"cu7","target":"zh9"},
                    {"source":"cu7","target":"sq10"},
                    {"source":"cu7","target":"K8"},
                    {"source":"zh9","target":"sq10"},
                    {"source":"zh9","target":"K8"},
                    {"source":"sq10","target":"K8"},
                    {"source":"cu8","target":"cu790"},
                    {"source":"cu8","target":"cu1572"},
                    {"source":"zh10","target":"zh794"},
                    {"source":"zh10","target":"zh1578"},
                    {"source":"zh10","target":"zh2362"},
                    {"source":"sq11","target":"sq796"},
                    {"source":"sq11","target":"sq1581"},
                    {"source":"sq11","target":"cu1572"},
                    {"source":"sq11","target":"sq2640"},
                    {"source":"sq11","target":"sq3425"},
                    {"source":"K9","target":"sq2640"},
                    {"source":"K9","target":"sq3425"},
                    {"source":"cu8","target":"zh10"},
                    {"source":"cu8","target":"sq11"},
                    {"source":"cu8","target":"K9"},
                    {"source":"zh10","target":"sq11"},
                    {"source":"zh10","target":"K9"},
                    {"source":"sq11","target":"K9"},
                    {"source":"cu9","target":"cu791"},
                    {"source":"cu9","target":"cu1573"},
                    {"source":"zh11","target":"zh795"},
                    {"source":"zh11","target":"zh1579"},
                    {"source":"zh11","target":"zh2363"},
                    {"source":"sq12","target":"sq797"},
                    {"source":"sq12","target":"sq1582"},
                    {"source":"sq12","target":"cu1573"},
                    {"source":"sq12","target":"sq2640"},
                    {"source":"sq12","target":"sq3425"},
                    {"source":"K10","target":"sq2640"},
                    {"source":"K10","target":"sq3425"},
                    {"source":"cu9","target":"zh11"},
                    {"source":"cu9","target":"sq12"},
                    {"source":"cu9","target":"K10"},
                    {"source":"zh11","target":"sq12"},
                    {"source":"zh11","target":"K10"},
                    {"source":"sq12","target":"K10"},
                    {"source":"cu10","target":"cu792"},
                    {"source":"cu10","target":"cu1574"},
                    {"source":"zh12","target":"zh796"},
                    {"source":"zh12","target":"zh1580"},
                    {"source":"zh12","target":"zh2364"},
                    {"source":"sq13","target":"sq798"},
                    {"source":"sq13","target":"sq1583"},
                    {"source":"sq13","target":"cu1574"},
                    {"source":"sq13","target":"sq2640"},
                    {"source":"sq13","target":"sq3425"},
                    {"source":"K11","target":"sq2640"},
                    {"source":"K11","target":"sq3425"},
                    {"source":"cu10","target":"zh12"},
                    {"source":"cu10","target":"sq13"},
                    {"source":"cu10","target":"K11"},
                    {"source":"zh12","target":"sq13"},
                    {"source":"zh12","target":"K11"},
                    {"source":"sq13","target":"K11"},
                    {"source":"cu11","target":"cu793"},
                    {"source":"cu11","target":"cu1575"},
                    {"source":"zh13","target":"zh797"},
                    {"source":"zh13","target":"zh1581"},
                    {"source":"zh13","target":"zh2365"},
                    {"source":"sq14","target":"sq799"},
                    {"source":"sq14","target":"sq1584"},
                    {"source":"sq14","target":"cu1575"},
                    {"source":"sq14","target":"sq2757"},
                    {"source":"sq14","target":"sq3542"},
                    {"source":"K12","target":"sq2757"},
                    {"source":"K12","target":"sq3542"},
                    {"source":"cu11","target":"zh13"},
                    {"source":"cu11","target":"sq14"},
                    {"source":"cu11","target":"K12"},
                    {"source":"zh13","target":"sq14"},
                    {"source":"zh13","target":"K12"},
                    {"source":"sq14","target":"K12"},
                    {"source":"cu12","target":"cu794"},
                    {"source":"cu12","target":"cu1576"},
                    {"source":"zh14","target":"zh798"},
                    {"source":"zh14","target":"zh1582"},
                    {"source":"zh14","target":"zh2366"},
                    {"source":"sq15","target":"sq800"},
                    {"source":"sq15","target":"sq1585"},
                    {"source":"sq15","target":"cu1576"},
                    {"source":"sq15","target":"sq2757"},
                    {"source":"sq15","target":"sq3542"},
                    {"source":"K13","target":"sq2757"},
                    {"source":"K13","target":"sq3542"},
                    {"source":"cu12","target":"zh14"},
                    {"source":"cu12","target":"sq15"},
                    {"source":"cu12","target":"K13"},
                    {"source":"zh14","target":"sq15"},
                    {"source":"zh14","target":"K13"},
                    {"source":"sq15","target":"K13"},
                    {"source":"cu13","target":"cu795"},
                    {"source":"cu13","target":"cu1577"},
                    {"source":"zh15","target":"zh799"},
                    {"source":"zh15","target":"zh1583"},
                    {"source":"zh15","target":"zh2367"},
                    {"source":"sq16","target":"sq801"},
                    {"source":"sq16","target":"sq1586"},
                    {"source":"sq16","target":"cu1577"},
                    {"source":"sq16","target":"sq2757"},
                    {"source":"sq16","target":"sq3542"},
                    {"source":"K14","target":"sq2757"},
                    {"source":"K14","target":"sq3542"},
                    {"source":"cu13","target":"zh15"},
                    {"source":"cu13","target":"sq16"},
                    {"source":"cu13","target":"K14"},
                    {"source":"zh15","target":"sq16"},
                    {"source":"zh15","target":"K14"},
                    {"source":"sq16","target":"K14"},
                    {"source":"cu14","target":"cu796"},
                    {"source":"cu14","target":"cu1578"},
                    {"source":"zh16","target":"zh800"},
                    {"source":"zh16","target":"zh1584"},
                    {"source":"zh16","target":"zh2368"},
                    {"source":"sq17","target":"sq802"},
                    {"source":"sq17","target":"sq1587"},
                    {"source":"sq17","target":"cu1578"},
                    {"source":"sq17","target":"sq2757"},
                    {"source":"sq17","target":"sq3542"},
                    {"source":"K15","target":"sq2757"},
                    {"source":"K15","target":"sq3542"},
                    {"source":"cu14","target":"zh16"},
                    {"source":"cu14","target":"sq17"},
                    {"source":"cu14","target":"K15"},
                    {"source":"zh16","target":"sq17"},
                    {"source":"zh16","target":"K15"},
                    {"source":"sq17","target":"K15"},
                    {"source":"cu15","target":"cu797"},
                    {"source":"cu15","target":"cu1579"},
                    {"source":"zh17","target":"zh801"},
                    {"source":"zh17","target":"zh1585"},
                    {"source":"zh17","target":"zh2369"},
                    {"source":"sq18","target":"sq803"},
                    {"source":"sq18","target":"sq1588"},
                    {"source":"sq18","target":"cu1579"},
                    {"source":"sq18","target":"sq2757"},
                    {"source":"sq18","target":"sq3542"},
                    {"source":"K16","target":"sq2757"},
                    {"source":"K16","target":"sq3542"},
                    {"source":"cu15","target":"zh17"},
                    {"source":"cu15","target":"sq18"},
                    {"source":"cu15","target":"K16"},
                    {"source":"zh17","target":"sq18"},
                    {"source":"zh17","target":"K16"},
                    {"source":"sq18","target":"K16"}
                ]
            };

    json = resetDats(result);
		var categories = [
			{name:"客户"},
			{name:"账户"},
			{name:"申请书"},
			{name:"卡"}];

		option = {
			title: {
				text: '知识图谱',
				show:false
			},
			tooltip: {
				show:true,
				backgroundColor:"rgba(255,255,255,0.85)",
				borderColor:"#ddd",
				borderWidth:1,
				textStyle:{
					color:"#333"
				}
				// formatter:function(params){
				//     return "<div>"+params.seriesName+"<br />"+params.data.descript+":"+params.name+"<br />关系数量："+params.data.value+"</div>"
				// }
			},
			color:['#CA8622','#61A0A8','#2E4554','#C23531'],
			legend: [{
				data:getLegendData(categories),
				z:100,
				top:10,
				itemWidth:24,
				itemHeight:24,
				itemGap:48,
				textStyle:{
					fontSize:14
				}
			}],
			series: [{
				name: '知识图谱',
				z:10,
				type: 'graph',
				top:"bottom",
				layout: 'force',
				animation:false,
				animationDuration:0,
				animationEasing:"linear",
				animationDelay:0,//默认
				focusNodeAdjacency: false,//是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点
				animationDurationUpdate: 1500,
				animationEasingUpdate: 'quinticInOut',
				roam: true,
				itemStyle: {
					normal: {
						borderColor: '#fff',
						borderWidth: 1,
						shadowBlur: 10,
						shadowColor: 'rgba(0, 0, 0, 0.3)'
					}
				},
				lineStyle: {
					normal: {
						color: 'source',
						curveness: 0
					},
					emphasis: {
						width: 5
					}
				},
				categories: categories,
				label: {
					normal: {
						show: false,
						position: 'top',//设置label显示的位置
						textStyle: {
							fontSize: '12rem',
							color:"#000"
						}
					}
				},
				symbolSize:function(value, params) {
					//根据数据params中的data来判定数据大小
					var size =parseInt(params.data.value);
					if(params.data.id ==="sq12" ||params.data.id ==="sq3425" ){
						return 32;
					}

					if(params.data.id ==="sq8" ||params.data.id ==="sq9" ||params.data.id ==="sq2431"){
						return 32;
					}
				   else if(size>9 &&size<32){
					   return 32;
				   }
				   else if(size<10){
						return size+10;
				   }



				},
				data: json.datas,
				edges:json.links,
				force: {
					layoutAnimation:true,//节点>100应该设置未true
					repulsion:400,
					//edgeLength: [50, 250],
					gravity: 0.5//节点受到的向中心的引力因子。该值越大节点越往中心点靠拢
				}
			}]
		};
		echart.setOption(option,true);
		echart.hideLoading();
		$("#box").show();

		//点击事件的处理
		echart.on("click", eConsole);
		echart.on('mouseup',dragFixed);

		document.getElementById('look').onclick = function(){
			handleReset();
		};
       /* },
        error:function(error){
            console.log(error)
        }
    });*/
}


function handleReset(){
    document.getElementById('info').style.display ="none";
    $(".selectpicker").selectpicker("val","");
    $('.selectpicker').selectpicker('refresh');
    params = {};
    nodeIds2 =[];
    links2 = [];
    nodeIds = [];
    links = [];
    echart.showLoading();
    option.series[0].data =  json.datas;
    option.series[0].edges = json.links;
    option.series[0].layout = 'force';
    echart.clear();
    echart.setOption(option,true);
    echart.hideLoading();
}


$(function(){

    //初始化select
    var domArr = ["account","customer","card","apply","tgr","tgjg","company"];
    for (var i=0; i<domArr.length; i++){
        initSelect(domArr[i]);
    }

    //初始化echart
    initEchart();
    //查询
    $("#search").on("click",function(){
        if(JSON.stringify(params) == "{}"){
            return ;
        }
        loadDatas2(domArr);
    });
    //重置select
    $("#reset").on("click",function(){
        handleReset();
    });

    //控制查询框的显示隐藏
    $("#toggle").click(function(){
        var $box =  $("#searchBox");
        var $left = $("#left");
        var $echart = $("#echart")
        if($box.css('display') === "block"){
            $left.css({"width":"68px"});
            $box.hide();
            //$echart.css({"marginLeft":"68px"});
            $("#font ").addClass("icon-toggle").removeClass("icon-close");
        }else{
            $left.css({"width":"388px"});
            $box.show();
            //$echart.css({"marginLeft":"388px"});
            $("#font").addClass("icon-close").removeClass("icon-toggle");
        }

    });

    window.onresize = function () {
        echart.resize();
    };

});


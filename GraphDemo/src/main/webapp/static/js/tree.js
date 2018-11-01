
var nodeIds = [];
var links = [];
var nodeIds2 = [];
var links2 = [];
var isClicked =false;
var params = {};
var g ;
var shortArr = [];
var json = {};
var renderNodes = [];
var renderLinks = [];
var chooseNodes = [];
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
            return arr[i];
        }
    }
    return false;
}
function isLinkLine(node, link) {
    return link.v == node|| link.w == node;
}

function isLinkNode(currNode, node) {
    if (currNode === node) {
        return true;
    }
    return isInArray(getNode(currNode),node) ;
}
function isEqualNode(currNode, node) {
    if (currNode === node) {
        return true;
    }else{
        return false;
    }
}
function getNode(id){
    var nodeIds = [];
    for(var j=0; j<json.links.length;j++){
        if(id === json.links[j].source){
            if(!isInArray(nodeIds,json.links[j].target)){
                nodeIds.push(json.links[j].target)
            }
        }else if( id === json.links[j].target){
            if(!isInArray(nodeIds,json.links[j].source)) {
                nodeIds.push(json.links[j].source)
            }
        }
    }
    return nodeIds;
}
function toggleNode(nodeCircle, currNode, isHover) {
    if (isHover) {
        // 提升节点层级
        nodeCircle.sort((a, b) => a.id === currNode.id ? 1 : -1);
        nodeCircle.style('opacity', .1).filter(node => isLinkNode(currNode, node)).style('opacity', 1).classed('node-active', true);
    } else {
        nodeCircle.style('opacity', 1).classed('node-active', false);
    }

}

function toggleLine(linkLine, currNode, isHover) {
    if (isHover) {
        // 加重连线样式
        linkLine.style('opacity', .1).filter(link => isLinkLine(currNode, link)).style('opacity', 1).classed('link-active', true);
    } else {
        // 连线恢复样式
        linkLine.style('opacity', 1).classed('link-active', false);
    }
}

function toggleLineText(lineText, currNode, isHover) {
    if (isHover) {
        // 只显示相连连线文字
        lineText
            .style('fill-opacity', link => isLinkLine(currNode, link) ? 1.0 : 0.0);
    } else {
        // 显示所有连线文字
        lineText
            .style('fill-opacity', '1.0');
    }
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

    //console.log("nodeIds2:"+nodeIds2.length)
    //console.log("links2:"+links2.length)
    return nodes2;
}


function loadDatas2(){
    var nodes;
    links2 = [];
    nodeIds2 = [];
    for(var i in params){
        nodes = getNodesById2(params[i]);
    }
    setNodesAndEdges(nodes,links2);
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
    });
}

function loadNodesDatas(){
    //$.getJSON("./datas/datas.json",function(result){

//        var result ={
//            "datas":[
//
//                {"id":"sq2356","descript":"推广机构id","symbol":"static/img/icon12@3x.png","name":"广东省深圳市招商银行新安支行","category":3,"value":12,"width":52,"height":52,"nodeSetting":{},"showLabelText":true},
//                {"id":"sq3141","descript":"推广人id","symbol":"static/img/icon13@3x.png","name":"07551001","category":3,"value":12,"width":52,"height":52,"nodeSetting":{},"showLabelText":true},
//
//                {"id":"sq1","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000101","category":2,"value":10,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq2","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000102","category":2,"value":10,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq3","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000103","category":2,"value":10,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq4","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000104","category":2,"value":8,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//                {"id":"sq5","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000105","category":2,"value":9,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq6","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000106","category":2,"value":10,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq7","descript":"申请书编号","symbol":"static/img/icon8@3x.png","name":"sq201809041000107","category":2,"value":10,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//                {"id":"K1","descript":"卡号","symbol":"static/img/icon11@3x.png","name":"201809043000101","category":3,"value":8,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"K2","descript":"卡号","symbol":"static/img/icon11@3x.png","name":"201809043000102","category":3,"value":8,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"K3","descript":"卡号","symbol":"static/img/icon11@3x.png","name":"201809043000103","category":3,"value":5,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"K4","descript":"卡号","symbol":"static/img/icon11@3x.png","name":"201809043000104","category":3,"value":7,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"K5","descript":"卡号","symbol":"static/img/icon11@3x.png","name":"201809043000105","category":3,"value":7,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//
//
//
//
//                {"id":"cu1","descript":"客户号","symbol":"static/img/icon1@3x.png","name":"600118772311","category":0,"value":9,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu2","descript":"客户号","symbol":"static/img/icon1@3x.png","name":"600118772312","category":0,"value":5,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu3","descript":"客户号3","symbol":"static/img/icon1@3x.png","name":"600118772313","category":0,"value":5,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu4","descript":"客户号4","symbol":"static/img/icon1@3x.png","name":"600118772314","category":0,"value":5,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//                {"id":"cu783","descript":"客户电话","symbol":"static/img/icon2@3x.png","name":"13009040001","category":0,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu784","descript":"客户电话","symbol":"static/img/icon2@3x.png","name":"13009040002","category":0,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu785","descript":"客户电话3","symbol":"static/img/icon2@3x.png","name":"13009040001","category":0,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu786","descript":"客户电话4","symbol":"static/img/icon2@3x.png","name":"13009040002","category":0,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//                {"id":"sq1572","descript":"单位电话","symbol":"static/img/icon10@3x.png","name":"075586941102","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq1573","descript":"单位电话","symbol":"static/img/icon10@3x.png","name":"075586941103","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//
//                {"id":"cu1565","descript":"所属公司编号1","symbol":"static/img/icon3@3x.png","name":"92440300731093239W","category":0,"value":4,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu1566","descript":"所属公司编号2","symbol":"static/img/icon3@3x.png","name":"92440300731093239X","category":0,"value":2,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu1567","descript":"所属公司编号3","symbol":"static/img/icon3@3x.png","name":"92440300731093239Y","category":0,"value":2,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"cu1568","descript":"所属公司编号4","symbol":"static/img/icon3@3x.png","name":"92440300731093239Z","category":0,"value":2,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//
//                {"id":"sq786","descript":"证件号码","symbol":"static/img/icon9@3x.png","name":"230102198002012001","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq787","descript":"证件号码","symbol":"static/img/icon9@3x.png","name":"230102198002012002","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq788","descript":"证件号码","symbol":"static/img/icon9@3x.png","name":"230102198002012003","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq789","descript":"证件号码","symbol":"static/img/icon9@3x.png","name":"230102198202012004","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq790","descript":"证件号码","symbol":"static/img/icon9@3x.png","name":"230102198202012004","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq1571","descript":"单位电话","symbol":"static/img/icon10@3x.png","name":"075586941101","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq1574","descript":"单位电话","symbol":"static/img/icon10@3x.png","name":"075586941104","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"sq1575","descript":"单位电话","symbol":"static/img/icon10@3x.png","name":"075586941105","category":2,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//
//
//                {"id":"zh1","descript":"账户号","symbol":"static/img/icon4@3x.png","name":"2998000050900151","category":1,"value":9,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh2","descript":"账户号","symbol":"static/img/icon4@3x.png","name":"2998000050900152","category":1,"value":9,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh3","descript":"账户号","symbol":"static/img/icon4@3x.png","name":"2998000050900153","category":1,"value":6,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh785","descript":"邮编","symbol":"static/img/icon5@3x.png","name":"518001","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh786","descript":"邮编","symbol":"static/img/icon5@3x.png","name":"518002","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh787","descript":"邮编","symbol":"static/img/icon5@3x.png","name":"518003","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh1569","descript":"地址","symbol":"static/img/icon6@3x.png","name":"广东省深圳市刘村和平小区 ","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh1570","descript":"地址","symbol":"static/img/icon6@3x.png","name":"广东省深圳市龙华弓村和平小区 ","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh1571","descript":"地址","symbol":"static/img/icon6@3x.png","name":"广东省深圳市泰宁小区 ","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh2353","descript":"城市编号","symbol":"static/img/icon7@3x.png","name":"440301","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh2354","descript":"城市编号","symbol":"static/img/icon7@3x.png","name":"4403002","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false},
//                {"id":"zh2355","descript":"城市编号","symbol":"static/img/icon7@3x.png","name":"440303","category":1,"value":1,"width":32,"height":32,"nodeSetting":{},"showLabelText":false}
//
//
//            ],
//            "links":[
//
//                {"target":"sq2356","source":"sq1","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"sq2","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"sq3","weight":7,"value":7,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"sq6","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"sq7","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"K1","weight":5,"value":5,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"K2","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq2356","source":"K5","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//
//
//                {"target":"sq3141","source":"sq1","weight":8,"value":8,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"sq2","weight":9,"value":9,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"sq3","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"sq4","weight":2,"value":2,"relation":"从属","linkSetting":{"style":"stroke: #f60; stroke-width: 1px;fill:#f3f3f3;fill:none","arrowheadStyle":"fill: #f60"}},
//                {"target":"sq3141","source":"sq5","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"K1","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"K2","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"K3","weight":3,"value":3,"relation":"从属","linkSetting":{}},
//                {"target":"sq3141","source":"K4","weight":3,"value":3,"relation":"从属","linkSetting":{}},
//                {"target":"K3","source":"zh2","weight":3,"value":3,"relation":"对应","linkSetting":{}},
//                {"target":"K4","source":"zh2","weight":3,"value":3,"relation":"对应","linkSetting":{}},
//
//                {"target":"sq1","source":"sq786","weight":3,"value":3,"relation":"从属","linkSetting":{}},
//                {"target":"sq1","source":"sq1571","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//
//                {"target":"sq1","source":"cu1565","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq1","source":"cu1","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//
//                {"target":"sq2","source":"zh2","weight":4,"value":4,"relation":"对应","linkSetting":{}},
//                {"target":"sq2","source":"sq787","weight":5,"value":5,"relation":"从属","linkSetting":{}},
//                {"target":"sq2","source":"sq1572","weight":7,"value":7,"relation":"从属","linkSetting":{}},
//                {"target":"sq2","source":"cu1565","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//
//                {"target":"sq3","source":"cu1567","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"cu1567","source":"cu3","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"cu3","source":"cu785","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq3","source":"sq788","weight":9,"value":9,"relation":"从属","linkSetting":{}},
//                {"target":"sq3","source":"sq1573","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"target":"sq4","source":"cu1568","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq5","source":"cu1568","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"cu1568","source":"cu4","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"cu4","source":"cu786","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq4","source":"sq789","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq4","source":"sq1574","weight":8,"value":8,"relation":"从属","linkSetting":{}},
//                {"target":"sq6","source":"cu1567","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq7","source":"cu1567","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"target":"sq7","source":"sq790","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//                {"target":"sq7","source":"sq1575","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//
//
//                {"source":"cu1","target":"cu1565","weight":3,"value":3,"relation":"从属","linkSetting":{}},
//                {"source":"cu1","target":"K1","weight":9,"value":9,"relation":"对应","linkSetting":{}},
//                {"source":"cu1","target":"zh1","weight":2,"value":2,"relation":"对应","linkSetting":{"style":"stroke: #f60; stroke-width: 1px;fill:#f3f3f3; fill:none;","arrowheadStyle":"fill: #f60"}},
//                {"source":"zh1","target":"K1","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//                {"source":"zh1","target":"K2","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//
//                {"target":"cu1","source":"cu783","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//
//
//                {"target":"sq2","source":"cu2","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//                {"target":"cu2","source":"cu784","weight":6,"value":6,"relation":"从属","linkSetting":{}},
//                {"source":"cu2","target":"cu1566","weight":5,"value":5,"relation":"从属","linkSetting":{}},
//                {"source":"cu1566","target":"sq2","weight":5,"value":5,"relation":"从属","linkSetting":{}},
//
//                {"target":"zh1","source":"zh785","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//                {"target":"zh1","source":"zh1569","weight":1,"value":1,"relation":"从属","linkSetting":{}},
//                {"target":"zh1","source":"zh2353","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"source":"zh1","target":"K3","weight":2,"value":2,"relation":"对应"},
//
//                {"source":"zh1","target":"K1","weight":4,"value":4,"relation":"对应","linkSetting":{}},
//                {"source":"zh1","target":"sq2","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//                {"target":"zh2","source":"zh786","weight":8,"value":8,"relation":"从属","linkSetting":{}},
//                {"target":"zh2","source":"zh1570","weight":4,"value":4,"relation":"从属","linkSetting":{}},
//                {"target":"zh2","source":"zh2354","weight":2,"value":2,"relation":"从属","linkSetting":{}},
//                {"source":"zh2","target":"K2","weight":3,"value":3,"relation":"对应","linkSetting":{}},
//                {"source":"zh2","target":"sq2","weight":2,"value":2,"relation":"对应","linkSetting":{}},
//                {"source":"zh3","target":"K5","weight":3,"value":3,"relation":"对应","linkSetting":{}},
//                {"target":"zh3","source":"zh787","weight":4,"value":4,"relation":"从属","linkSetting":{}},
//                {"target":"zh3","source":"zh1571","weight":5,"value":5,"relation":"从属","linkSetting":{}},
//                {"target":"zh3","source":"zh2355","weight":7,"value":7,"relation":"从属","linkSetting":{}}
//
//            ]
//        };
//
//        json = result;
//        var nodes = json.datas;
//        var links =json.links;
//        if(nodes && links && nodes.length && links.length){
//            setNodesAndEdges(nodes,links);
//        }
   // });
	
	///////liubaichuan/////////////////////////////
	$.getJSON("/getAllGraph",function(data){
		json = data;
		var nodes = json.datas;
        var links = json.links;
        if(nodes && links && nodes.length && links.length){
            setNodesAndEdges(nodes,links);
        }
    });
	//////////////////////////////////////////////
}

function bindEvent(domArr){
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
}



function setNodesAndEdges(pramaNodes,pramaLinks){
     renderNodes = pramaNodes;
     renderLinks = pramaLinks;
     g = new dagreD3.graphlib.Graph({compound:true})
        .setGraph({})
        .setDefaultEdgeLabel(function() { return {}; });

    for(var i=0;i<pramaNodes.length;i++){
        var pic= function() {
            var display = "none";
            if(pramaNodes[i].showLabelText){
                display = "block";
            }
            var div = document.createElement("div"),
                p=d3.select(div).append("p").attr("class","node-text").html(pramaNodes[i].name).attr("style","display:"+display),
                img = d3.select(div).append("img").attr("width",pramaNodes[i].width).attr("height",pramaNodes[i].height);
                img.attr("src",pramaNodes[i].symbol);
            return div;
        };


        var obj = {data:pramaNodes[i],label: pic(),index:i};
        var nodeSetting = $.extend({}, obj, pramaNodes[i].nodeSetting);
        g.setNode(pramaNodes[i].id, nodeSetting);
        //console.log("g"+g)
        //g.append("rect").classed('node-selected', true)
        // .attr("rx",0).attr("ry",0).attr("x","-35").attr("y","-35")
    }
    for(var j=0;j<pramaLinks.length;j++) {
        var obj ={label:pramaLinks[j].relation};
        var linkSetting = $.extend({}, obj, pramaLinks[j].linkSetting);
        g.setEdge(pramaLinks[j].target, pramaLinks[j].source,linkSetting);
    }
    g.nodes().forEach(function(v) {
        var node = g.node(v);
        //console.log("node"+node)
        // Round the corners of the nodes
        //node.rx = node.ry = 0;
       // node.width = node.height=node.data.width;
    });
    renderSvg();
}

function nodeIsExistInChooseNodes(node){
    for(var o = 0; o < chooseNodes.length;o++){
        if(chooseNodes[o].data.id ===node.data.id){
            return o;
        }
    }
    return -1;
}
function deleteNodeInChooseNodes(node){
    for(var o = 0; o < chooseNodes.length;o++){
        if(chooseNodes[o].data.id ===node.data.id){
            chooseNodes.splice(o,1);
        }
    }
}

function renderSvg(){
    // Create the renderer

    var width = document.body.clientWidth;
    var height = 600;
    // Set up an SVG group so that we can translate the final graph.
    var svg = d3.select("svg").attr("viewBox","0 0 "+width+" "+height).attr("preserveAspectRatio","xMidYMid meet");
    svg.select("g").remove();

    var svgGroup = svg.append("g");

    var inner =d3.select("svg g");


    var zoom = d3.zoom().on("zoom", function () { //添加鼠标滚轮放大缩小事件
        inner.attr("transform", d3.event.transform);
    });
    svg.call(zoom);


    var render = new dagreD3.render();
    // Run the renderer. This is what draws the final graph.
    render(svgGroup, g);

    // Center the graph
    var xCenterOffset = (document.body.clientWidth - g.graph().width) / 2;
    var YCenterOffset = (document.body.clientHeight - g.graph().height) / 2;

    var gNodes = svgGroup.selectAll("g.node");
    //svgGroup.attr("transform", "translate(" + xCenterOffset + ", "+YCenterOffset+")");
    svgGroup.selectAll("g.node").each(function(v) {
        var rect = $(this).select("rect").width(0);
        //.append("rect").classed('node-selected', true)
        var node = g.node(v);
        $(this).click(function(e){
            e.preventDefault();
            handleClick(node,e);
        }).mousedown(function(e){
            e.preventDefault();
            e.stopPropagation();
            handleMouseDown(svgGroup,node);
        });

    });

    function handleMouseDown(svgGroup,node){
        var $menu = $("#menu");
        $menu.data("node", node );
       console.log("nodeIsExistInChooseNodes"+nodeIsExistInChooseNodes(node))
        if(nodeIsExistInChooseNodes(node)>-1){
            $menu.find("li").eq(0).html("取消选中");
        }else {
            $menu.find("li").eq(0).html("选中节点");
        }
    }


    var linkLine = svg.selectAll("g.edgePath");
    var lineText = svg.selectAll("g.edgeLabel");
    var nodeCircle  = svg.selectAll("g.node");
    nodeCircle.append("rect").attr("x","-25").attr("y","-25").classed('node-selected', true)
    //鼠标移动上去的交互效果
    nodeCircle.on("mouseenter", function(currNode){
        toggleNode(nodeCircle, currNode, true);
        toggleLine(linkLine, currNode, true);
        toggleLineText(lineText, currNode, true);
    }).on("mouseout", function(currNode){
        toggleNode(nodeCircle, currNode, false);
        toggleLine(linkLine, currNode, false);
        toggleLineText(lineText, currNode, false);
    });


    var $svg = document.getElementById("svg");
    var menu=document.querySelector("#menu");
    $svg.oncontextmenu = function(e){
        var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;
        //根据事件对象中鼠标点击的位置，进行定位
        menu.style.display='block';
        var width = menu.clientWidth;
        menu.style.left=(e.clientX-width)+'px';
        menu.style.top=(e.clientY+scrollTop-20)+'px';
        //menu.style.left = e.target.offset.left;
        //menu.style.top = e.target.offset.top;
        return false;
    }
    $svg.onclick=function(e){
        //用户触发click事件就可以关闭了，因为绑定在window上，按事件冒泡处理，不会影响菜单的功能
        menu.style.display="none";
    }

    var $menu = $("#menu");
    $("#menu li").off().click(function(e){
        gNodes = svg.selectAll("g.node");
        e.preventDefault();
        e.stopPropagation();
        var node =$menu.data("node");
        console.log("curNode"+node)
        var forOpt = $(this).attr("for");
        switch (forOpt){
            case "chooseNode":
                chooseNode($(this));
                break ;
            case "showPath" :
                showPath($(this));
                break;
            case "secondSearch":
                secondSearch();
                break;
            case "exportData":
                exportData();
                break;
        }
        $menu.hide();


        function chooseNode($this){
            gNodes.each(function(v) {
                var _this = $(this);
                shortArr = [];
                toggleLineToGreen(linkLine, [], false);
                _this.removeClass("active");
                var currentNode = g.node(v);
                if(currentNode.data.id ===node.data.id ) {
                    var index =nodeIsExistInChooseNodes(node);
                    if(index>-1){
                        chooseNodes.splice(index,1);
                    }else{
                        chooseNodes.push(node);
                    }
                    _this.toggleClass("selected");
                }
            });
        }
        function showPath($this){
            if(shortArr.length=== 2){
                shortArr = [];
            }
            shortArr.push(node);
            if (shortArr.length === 1) {
                $this.html("显示路径");
            } else {
                $this.html("设置开始点");
            }
            if(shortArr.length ===2){
                gNodes.each(function(v) {
                    $(this).removeClass("active").removeClass("selected");
                    var currentNode = g.node(v);
                    if(currentNode.data.id ===shortArr[0].data.id ||currentNode.data.id ===shortArr[1].data.id){
                        $(this).addClass("active");
                    }
                });
                var links = showShortestPath(shortArr[0].index,shortArr[1].index);
                toggleLineToGreen(linkLine, links, true);
            }else{
                toggleLineToGreen(linkLine, [], false);
                gNodes.each(function(v) {
                    $(this).removeClass("active").removeClass("selected");
                    var currentNode = g.node(v);
                    if(currentNode.data.id ===shortArr[0].data.id){
                        $(this).addClass("active");
                    }
                });
            }
        }

        function secondSearch(){
            layer.open({
                    type: 1,
                    title: '二次查询',
                    area:["460px","400px"],
                    content:$("#secondSearch")
                },
                function(pass, index){
                    layer.close(index);
            });
        }
        function exportData(){
            layer.open({
                    type: 1,
                    //shade: false,
                    title: '导出',
                    area:["300px","300px"],
                    content:$("#exportType")
                },
                function(pass, index){
                    layer.close(index);
                });
        }
    });

    //设置放大缩小
   //var max = svg._groups[0][0].clientWidth>svg._groups[0][0].clientHeight?svg._groups[0][0].clientWidth:svg._groups[0][0].clientHeight;
    var max= 1920;
    var clientWidth = 1920;
    var clientHeight = 600;
    var initialScale = max/$(window).width(); //initialScale元素放大倍数，随着父元素宽高发生变化时改变初始渲染大小
    //var tWidth = (svg._groups[0][0].clientWidth  - this.g.graph().width * initialScale) / 2; //水平居中
    //var tHeight = (svg._groups[0][0].clientHeight  - this.g.graph().height * initialScale) / 2; //垂直居中
    var tWidth = (clientWidth  - this.g.graph().width * initialScale) / 2; //水平居中
    var tHeight = (clientHeight  - this.g.graph().height * initialScale) / 2; //垂直居中
    //alert(initialScale)
    //alert("tWidth:"+tWidth,"tHeight:"+tHeight,"initialScale:"+initialScale)
    svg.call(zoom.transform, d3.zoomIdentity.translate(tWidth, tHeight).scale(initialScale)); //元素水平垂直居中

    var styleTooltip = function(node) {
        return "<p class='name'>" + node.data.descript + "：</p>" +
               "<p class='description'>" + node.data.name + "</p>" ;
    };
    inner.selectAll("g.node").attr("title", function(v) { return styleTooltip(g.node(v)) })
        .each(function(v) { $(this).tipsy({ gravity: "w", opacity: 1, html: true }); });
}

function handleClick(node){
    shortArr = [];
    var mes = '';
    $(".tipsy").remove();
    if (typeof node!= 'undefined') {
        mes += ' 节点id : ' + node.data.id;
        mes += ' <br/> 节点描述 : ' + node.data.descript;
        mes += '<br/> '+node.data.descript+': ' + node.data.name;
        mes += ' <br/> 节点图片 : ' + node.data.symbol;
        mes += ' <br/> 关系数量 : ' + node.data.value;
    }
    document.getElementById('infoHtml').innerHTML ="<p>基本属性</p>"+ mes;
    document.getElementById('info').style.display ="block";
    document.getElementById('look').onclick = function(){
        handleReset();
    };
    getData(node);
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
    //echart.showLoading();
    setNodesAndEdges(json.datas,json.links)
}

function getData(param) {
    // if (param.data.value === 1 && isClicked === true) {
    //     alert("没有下钻咯！");
    //     return;
    // }
    isClicked = true;
    getNodesById(param.data.id);

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
    setNodesAndEdges(nodes,links);
}


function isInLink(links,currentLink){
    //console.log(currentLink)
    for(var i= 0;i<links.length;i++){
        if(currentLink.v === links[i].target &&currentLink.w===links[i].source){
            return true
        }
    }
    return false;
}
function toggleLineToGreen(linkLine, links, isHover) {
    if (isHover) {
        // 加重连线样式
        linkLine
            .filter(link => isInLink(links, link))
            .style('opacity', 1)
            .classed('link-short-active', true);
    } else {
        // 连线恢复样式
        linkLine
            .classed('link-short-active', false);
    }
}
//用邻接矩阵表示图
function showShortestPath(s,e){
    var G = new Array();
    // var Nv = json.datas.length;
    // var Ne = json.links.length
    // var graphNodes = json.datas;
    // var graphLinks = json.links;
    var Nv = renderNodes.length;
    var Ne = renderLinks.length
    var graphNodes = renderNodes;
    var graphLinks = renderLinks;
    var INF = 10000;
    var map =BuildGraph();
    var  Graph ={
        vexs:[],
        vexnum:Nv,//顶点数
        edgnum:Ne,//边数
        matirx:map
    };
    var P=[],D=[];


    short_path_floyd(Graph,P,D)

    function short_path_floyd(G, P, D){

        //初始化floyd算法的两个矩阵
        for(var v = 0; v < Nv; v++){
            D[v]=new Array(v);
            P[v]=new Array(v);
            for(var w = 0; w < G.vexnum; w++){
                D[v][w] = G.matirx[v][w];
                P[v][w] = w;
            }
        }

        //这里是弗洛伊德算法的核心部分
        //k为中间点
        for(var k = 0; k < G.vexnum; k++){
            //v为起点
            for(var v = 0 ; v < G.vexnum; v++){
                //w为终点
                for(var w =0; w < G.vexnum; w++){
                    if(D[v][w] > (D[v][k] + D[k][w])){
                        D[v][w] = D[v][k] + D[k][w];//更新最小路径
                        P[v][w] = P[v][k];//更新最小路径中间顶点
                    }
                }
            }
        }
    }


    function pfpath(start, end) { //打印最短路径
        var arr = [];
        arr.push(start)
        while(start != end) {
            console.log(start+"<<")
            start = P[start][end];
            arr.push(start)
        }
        return arr
    }

    function BuildGraph() {
        //scanf("%d", &Nv);
        for (var i = 0; i < Nv; i++){
            G[i]=new Array(i);
            for (var j = 0; j < Nv; j++){
                var link1 = {
                    target:graphNodes[i].id,
                    source:graphNodes[j].id
                }
                var link2 = {
                    target:graphNodes[j].id,
                    source:graphNodes[i].id
                };
                var linkCommon= isInArrayLinks(graphLinks,link1) ||isInArrayLinks(graphLinks,link2);
                if(linkCommon){
                    G[i][j] = linkCommon.weight;
                }else{
                    G[i][j] = (i == j ? 0 : INF);
                }

            }
        }

        return G;
    }


    var darr = pfpath(s,e);
    var empisiLinks = [];
    for(var k =0;k<(darr.length-1);k++){
        //alert(darr.length)
        console.log(json.datas[darr[k]].id+"=>");
        var link3 = {};
        var link4 = {};
            link3 = {
                // target:json.datas[darr[k]].id,
                // source:json.datas[darr[k+1]].id
                target:renderNodes[darr[k]].id,
                source:renderNodes[darr[k+1]].id
            }
            link4= {
                // target:json.datas[darr[k+1]].id,
                // source:json.datas[darr[k]].id
                target:renderNodes[darr[k+1]].id,
                source:renderNodes[darr[k]].id
            };

        var linkCommon= isInArrayLinks(graphLinks,link3) ||isInArrayLinks(graphLinks,link4);
        if(linkCommon){
            empisiLinks.push(linkCommon);
            console.log(linkCommon +"->")
        }
    }

    console.log(empisiLinks)
    return empisiLinks;
}

function init(){
    //初始化select
    var domArr = ["account","customer","card","apply","tgr","tgjg","company"];
    for (var i=0; i<domArr.length; i++){
        initSelect(domArr[i]);
    }
    //加载节点数据；
    loadNodesDatas();

    //绑定事件
    bindEvent();
}

init();
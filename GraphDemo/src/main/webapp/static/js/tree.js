
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

	///////liubaichuan/////////////////////////////
	$.getJSON("/getGraph",function(data){
	    console.log("Data:"+data)
		json = data;
		var nodes = json.datas;
        var links = json.links;
        if(nodes && links && nodes.length && links.length){
            setNodesAndEdges(nodes,links);
            renderDataTable(nodes);
        }
    });
	//////////////////////////////////////////////
}

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
                p=d3.select(div).append("p").attr("class","node-text").html(pramaNodes[i].descript).attr("style","display:"+display),
                img = d3.select(div).append("img").attr("width",pramaNodes[i].width).attr("height",pramaNodes[i].height);
                img.attr("src",pramaNodes[i].symbol);
            return div;
        };


        var obj = {data:pramaNodes[i],label: pic(),index:i};
        var nodeSetting = $.extend({}, obj, pramaNodes[i].nodeSetting);
        g.setNode(pramaNodes[i].id, nodeSetting);

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
        node.rx = node.ry = 11000;
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
    //var xCenterOffset = (document.body.clientWidth - g.graph().width) / 2;
    //var YCenterOffset = (document.body.clientHeight - g.graph().height) / 2;

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
    nodeCircle.on("mouseover", function(currNode){
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

            menu.style.display='none';
       if(e.target !== e.currentTarget){
           e.preventDefault();
           var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;
           //根据事件对象中鼠标点击的位置，进行定位
           menu.style.display='block';
           var width = menu.clientWidth;
           menu.style.left=(e.clientX-width)+'px';
           menu.style.top=(e.clientY+scrollTop-20)+'px';
          // menu.style.left = e.target.offset.top;
          // menu.style.top = e.target.offset.left;
           return false;
       }

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
                //var links = showShortestPath(shortArr[0].index,shortArr[1].index);
                searchShortPath(shortArr);

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


        /******************最短路径查询START********************/

        function searchShortPath(arr) {
            var obj = {};
            obj.option = "shortest";
            obj.vertexIds = [];
            obj.vertexIds.push(arr[0].data.id);
            obj.vertexIds.push(arr[1].data.id);
            var params = JSON.stringify(obj);
            $.ajax({
                type: 'post',
                url: '/searchPath',
                contentType: 'application/json;charset=utf-8',
                data: params,
                success: function (response) { //返回json结果


                    //return response.links;
                    var links = response.links;
                    toggleLineToGreen(linkLine, links, true);


                }
            })
        }

        /******************最短路径查询END********************/


        function secondSearch(){
            layer.open({
                    type: 1,
                    title: '二次查询',
                    area:["460px","420px"],
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
                target:renderNodes[darr[k]].id,
                source:renderNodes[darr[k+1]].id
            }
            link4= {
                target:renderNodes[darr[k+1]].id,
                source:renderNodes[darr[k]].id
            };

        var linkCommon= isInArrayLinks(graphLinks,link3) ||isInArrayLinks(graphLinks,link4);
        if(linkCommon){
            empisiLinks.push(linkCommon);
        }
    }

    return empisiLinks;
}

function init(){
    //初始化select
    var domArr = ["account","customer","card","apply","tgr","tgjg","company"];
    for (var i=0; i<domArr.length; i++){
        //initSelect(domArr[i]);
    }

    for(var o=1;o<4;o++){
        searchCon(o);
    }

    function searchCon(r){
        $('#searchType'+r).on('changed.bs.select',function(e){
            var value = $(this).selectpicker("val");
            //$("#forType"+r).val(value);
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

    //加载节点数据；
    loadNodesDatas();

    //绑定事件
    bindEvent();
}

init();


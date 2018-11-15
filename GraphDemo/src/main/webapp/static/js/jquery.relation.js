/**
 * relationPicture - yjs.relationPicture.js
 * Copyright(c) 20181114
 */
(function ($) {

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

    function isLinkNode(target,currNode, node) {
        if (currNode === node) {
            return true;
        }
        return isInArray(getNode(target,currNode),node) ;
    }

    function getNode(target,id){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        var json = opts.json;

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



    function toggleNode(target,nodeCircle, currNode, isHover) {
        if (isHover) {
            // 提升节点层级
            nodeCircle.sort((a, b) => a.id === currNode.id ? 1 : -1);
            nodeCircle.style('opacity', .1).filter(node => isLinkNode(target,currNode, node)).style('opacity', 1).classed('node-active', true);
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

    function nodeIsExistInChooseNodes(target,node){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        var chooseNodes = opts.chooseNodes;
        for(var o = 0; o < chooseNodes.length;o++){
            if(chooseNodes[o].data.id ===node.data.id){
                return o;
            }
        }
        return -1;
    }



    function handleClick(target,node){
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
            handleReset(target);
        };
        getNodesById(target,node.data.id);
    }

    function getNodesById(target,id){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        var json = opts.json;

        var nodes =[];
        if(!isInArray(opts.nodeIds,id)){
            opts.nodeIds.push(id);
        }
        for(var j=0; j<json.links.length;j++){
            if(id === json.links[j].source){
                if(!isInArrayLinks(opts.links,json.links[j])){
                    opts.links.push(json.links[j]);
                }
                if(!isInArray(opts.nodeIds,json.links[j].target)){
                    opts.nodeIds.push(json.links[j].target)
                }
            }else if( id === json.links[j].target){
                if(!isInArrayLinks(opts.links,json.links[j])){
                    opts.links.push(json.links[j]);
                }
                if(!isInArray(opts.nodeIds,json.links[j].source)) {

                    opts.nodeIds.push(json.links[j].source)
                }
            }
        }

        for(var k=0;k<opts.nodeIds.length;k++){
            for(var i=0; i<json.datas.length;i++){
                if(opts.nodeIds[k]=== json.datas[i].id){
                    nodes.push(json.datas[i]);
                }
            }
        }
        setNodesAndEdges(target,nodes,opts.links);
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





    /*****重置操作处理  START******/
    function handleReset(target){

        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        var json = opts.json;
        opts.nodeIds = [];
        opts.links = [];
        setNodesAndEdges(target,json.datas,json.links,"svg");
        reRenderDataTable(json.datas);
    }
    /*****重置操作处理  END******/



    /*****初始化  START******/
    function init(target){

        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        //初始化svg
        var width = opts.width;
        var height = opts.height;
        var id = opts.id;
        // Set up an SVG group so that we can translate the final graph.
        var svg = d3.select("#"+id).attr("viewBox","0 0 "+width+" "+height).attr("preserveAspectRatio","xMidYMid meet");

        svg.select("g").remove();

        state.svgGroup = svg.append("g");
        state.svg = svg;

        var inner =d3.select("#"+id +" g");


        var zoom = d3.zoom().on("zoom", function () { //添加鼠标滚轮放大缩小事件
            inner.attr("transform", d3.event.transform);
        });
        state.zoom = zoom;
        state.inner = inner;
        svg.call(zoom);

        var $menu = $("<ul class='operate-menu' style='display: none'>");
        $("<li class='active' for='chooseNode'>").html("选中节点").appendTo($menu);

        $("<li for='showPath'>").html("设置开始点").appendTo($menu);

        if(!opts.hideSecondSearch){
            $("<li for='secondSearch'>").html("二次查询").appendTo($menu);
        }
        $("<li for='exportData' onclick='window.open(\"/exportExcel\")' >").html("导出").appendTo($menu);

        $menu.appendTo("body");
        state.menu = $menu;

        //设置边和节点；
        setNodesAndEdges(target,opts.json.datas,opts.json.links);
    }
    /*****初始化  END******/


    function setNodesAndEdges(target,pramaNodes,pramaLinks){
        var state = $.data(target, "relationPicture" );
        var opts =state.options;
        opts.renderNodes = pramaNodes;
        opts.renderLinks = pramaLinks;

        var g = new dagreD3.graphlib.Graph({compound:true})
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
            // Round the corners of the nodes
            node.rx = node.ry = 11000;
            // node.width = node.height=node.data.width;
        });
        state.g = g;
        renderSvg(target);
    }


    function renderSvg(target){

        var state = $.data(target, "relationPicture" );
        var opts = state.options;

        var g= state.g;
        var svgGroup = state.svgGroup;
        var svg = state.svg;


        // Create the renderer
        var render = new dagreD3.render();
        // Run the renderer. This is what draws the final graph.
        render(svgGroup, g);
        //设置放大缩小
        //var max = svg._groups[0][0].clientWidth>svg._groups[0][0].clientHeight?svg._groups[0][0].clientWidth:svg._groups[0][0].clientHeight;
        var max= 1920;
        var initialScale = max/$(window).width(); //initialScale元素放大倍数，随着父元素宽高发生变化时改变初始渲染大小
        //var tWidth = (svg._groups[0][0].clientWidth  - this.g.graph().width * initialScale) / 2; //水平居中
        //var tHeight = (svg._groups[0][0].clientHeight  - this.g.graph().height * initialScale) / 2; //垂直居中
        var tWidth = (opts.width  - g.graph().width * initialScale) / 2; //水平居中
        var tHeight = (opts.height  - g.graph().height * initialScale) / 2; //垂直居中

        svg.call(state.zoom.transform, d3.zoomIdentity.translate(tWidth, tHeight).scale(initialScale)); //元素水平垂直居中

        bindEvent(target);
    }

    /*********初始化加载数据  END*********/


    /*********绑定事件  START*********/
    function bindEvent(target){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;

        var svg =state.svg ;
        var g = state.g;
        var svgGroup =state.svgGroup ;
        var inner = state.inner;

        var $menu = state.menu;

        var gNodes = svgGroup.selectAll("g.node");

        svgGroup.selectAll("g.node").each(function(v) {
            $(this).select("rect").width(0);
            var node = g.node(v);
            $(this).click(function(e){
                e.preventDefault();
                handleClick(target,node,e);
            }).mousedown(function(e){
                e.preventDefault();
                e.stopPropagation();
                handleMouseDown(target,svgGroup,node);
            });

        });

        function handleMouseDown(target,svgGroup,node){
            $menu.data("node", node );
            if(nodeIsExistInChooseNodes(target,node)>-1){
                $menu.find("li").eq(0).html("取消选中");
            }else {
                $menu.find("li").eq(0).html("选中节点");
            }
        }


        var linkLine = svg.selectAll("g.edgePath");
        var lineText = svg.selectAll("g.edgeLabel");
        var nodeCircle  = svg.selectAll("g.node");
        nodeCircle.append("rect").attr("x","-25").attr("y","-25").classed('node-selected', true);
        //鼠标移动上去的交互效果
        nodeCircle.on("mouseover", function(currNode){
            toggleNode(target,nodeCircle, currNode, true);
            toggleLine(linkLine, currNode, true);
            toggleLineText(lineText, currNode, true);
        }).on("mouseout", function(currNode){
            toggleNode(target,nodeCircle, currNode, false);
            toggleLine(linkLine, currNode, false);
            toggleLineText(lineText, currNode, false);
        });


        var $svg = document.getElementById(opts.id);
        //var menu=  document.getElementById("menu");
        var menu = state.menu[0];

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

        var $li = $menu.find("li");
        $li.off().click(function(e){
            gNodes = svg.selectAll("g.node");
            e.preventDefault();
            e.stopPropagation();
            var node =$menu.data("node");
            var forOpt = $(this).attr("for");
            switch (forOpt){
                case "chooseNode":
                    chooseNode(target,node);
                    break ;
                case "showPath" :
                    showPath(target,node,$(this));
                    break;
                case "secondSearch":

                    secondSearch(target);
                    break;
                case "exportData":
                    exportData(target);
                    break;
            }
            $menu.hide();
        });

        inner.selectAll("g.node").attr("title", function(v) { return styleTooltip(g.node(v)) })
            .each(function(v) { $(this).tipsy({ gravity: "w", opacity: 1, html: true }); });

        function styleTooltip(node) {
            return "<p class='name'>" + node.data.descript + "：</p>" +
                "<p class='description'>" + node.data.name + "</p>" ;
        };
    }
    /*********绑定事件  END*********/

    /*********导出数据  start*********/
    function exportData(target){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;

        var renderNodes = opts.renderNodes;
        var renderLinks = opts.renderLinks;


        var params = {};
        params.vertexIds = [];
        params.edgeIds = [];
        for(var i=0;i<renderNodes.length;i++){
            params.vertexIds.push(renderNodes[i].id);

        }
        for(var i=0;i<renderLinks.length;i++){
            params.edgeIds.push(renderLinks[i].id);
        }
        var data = JSON.stringify(params);
        $.ajax({
            type:'post',
            url:'/exportExcel',
            contentType: 'application/json;charset=utf-8',
            data: data,
            success:function(response){
                //console.log("response:"+response);

                // var $eleBtn1 = $("#btn1");
                // var $eleBtn2 = $("#btn2");
                //
                // //已知一个下载文件的后端接口：https://codeload.github.com/douban/douban-client/legacy.zip/master
                // //方法一：window.open()
                // $eleBtn1.click(function(){
                    window.open("/exportExcel");
                //});

            }
        });
    }
    /*********导出数据  END*********/

    /*********二次查询弹窗  start*********/
    function secondSearch(target){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;
        opts.secondSearchLayer = layer.open({
                type: 1,
                title: '二次查询',
                area:["460px","420px"],
                content:$("#secondSearch"),
                cancel:function(){
                    state.svg.empty()
                }
            });
    }
    /*********二次查询弹窗  END*********/

    function showPath(target,node,$this){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;


        var g = state.g;
        var svg = state.svg;
        var svgGroup = state.svgGroup;

        var gNodes = svgGroup.selectAll("g.node");
        var linkLine = svg.selectAll("g.edgePath");
        if(opts.shortArr.length=== 2){
            opts.shortArr = [];
        }
        opts.shortArr.push(node);
        if (opts.shortArr.length === 1) {
            $this.html("显示路径");
        } else {
            $this.html("设置开始点");
        }
        if(opts.shortArr.length ===2){
            gNodes.each(function(v) {
                $(this).removeClass("active").removeClass("selected");
                var currentNode = g.node(v);
                if(currentNode.data.id ===opts.shortArr[0].data.id ||currentNode.data.id ===opts.shortArr[1].data.id){
                    $(this).addClass("active");
                }
            });
            //var links = showShortestPath(shortArr[0].index,shortArr[1].index);
            searchShortPath(target,opts.shortArr);

        }else{
            toggleLineToGreen(linkLine, [], false);
            gNodes.each(function(v) {
                $(this).removeClass("active").removeClass("selected");
                var currentNode = g.node(v);
                if(currentNode.data.id ===opts.shortArr[0].data.id){
                    $(this).addClass("active");
                }
            });
        }
    }
    /******************最短路径查询START********************/
    function searchShortPath(target,arr) {

        var state = $.data(target, "relationPicture" );
        var svg = state.svg;

        var linkLine = svg.selectAll("g.edgePath");

        var params = {};
        params.option = "shortest";
        params.vertexIds = [];
        params.vertexIds.push(arr[0].data.id);
        params.vertexIds.push(arr[1].data.id);
        var data = JSON.stringify(params);
        $.ajax({
            type: 'post',
            url: '/searchPath',
            contentType: 'application/json;charset=utf-8',
            data: data,
            success: function (response) { //返回json结果
                var links = response.links;
                toggleLineToGreen(linkLine, links, true);
            }
        })
    }
    /******************最短路径查询END********************/


    function chooseNode(target,node){
        var state = $.data(target, "relationPicture" );
        var opts = state.options;

        var shortArr = opts.shortArr;
        var g = state.g;
        var svg = state.svg;
        var svgGroup = state.svgGroup;

        var gNodes = svgGroup.selectAll("g.node");
        var linkLine = svg.selectAll("g.edgePath");

        gNodes.each(function(v) {
            var _this = $(this);
            shortArr = [];
            toggleLineToGreen(linkLine, [], false);
            _this.removeClass("active");
            var currentNode = g.node(v);
            if(currentNode.data.id ===node.data.id ) {
                var index =nodeIsExistInChooseNodes(target,node);
                if(index>-1){
                    opts.chooseNodes.splice(index,1);
                }else{
                    opts.chooseNodes.push(node);
                }
                _this.toggleClass("selected");
            }
        });
    }

    /******************relationPicture的构造函数******************/
    $.fn.relationPicture = function (options, param,paramExt) {
        if (typeof options == 'string'){
            var method = $.fn.relationPicture.methods[options];
            if (method){
                return method(this, param,paramExt);
            }
        }

        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'relationPicture');
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, 'relationPicture', {
                    options:$.extend({}, $.fn.relationPicture.defaults, options)
                });
            }
            init(this);

        });

    };
    /******************relationPicture的构造函数******************/

    /******************relationPicture的方法 START******************/
    $.fn.relationPicture.methods = {
        options:function (jq) {
            return $.data(jq[0], 'relationPicture').options;
        },
        setNodesAndEdges:function(jq,nodes,links){
            jq.each(function(){
                setNodesAndEdges(this, nodes,links);
            });
        },
        getSecondLayerIndex:function(jq){
            return $.data(jq[0], 'relationPicture').options.secondSearchLayer;
        },
        getChooseNodes:function(jq){
            return $.data(jq[0], 'relationPicture').options.chooseNodes;
        }
    };
    /******************relationPicture的方法 END******************/

    /******************初始化属性 START******************/
    $.fn.relationPicture.defaults =  {
        id:"",
        width:document.body.clientWidth,
        height:600,
        nodeIds: [],
        links:  [],
        shortArr : [],
        json : {},
        renderNodes : [],
        renderLinks : [],
        chooseNodes : [],
        hideSecondSearch:false,
        secondSearchLayer: null
    };
    /******************初始化属性 END******************/
})(jQuery);
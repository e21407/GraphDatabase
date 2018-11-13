<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>

<meta charset="UTF-8">
<title>xx系统xx业务案件反查系统</title>
<link rel="stylesheet" type="text/css" href="./static/js/bootstrap/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="./static/js/bootstrap/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="./static/css/jquery.dataTables.min.css"/>
<link rel="stylesheet" type="text/css" href="./static/js/bootstrap/css/bootstrap-select.min.css"/>
<link rel="stylesheet" type="text/css" href="./static/js/icheck/skins/all.css"/>
<link rel="stylesheet" type="text/css" href="./static/css/base.css">
<link rel="stylesheet" type="text/css" href="./static/css/common.css">
<link rel="stylesheet" type="text/css" href="./static/css/link.css">
<link rel="stylesheet" type="text/css" href="./static/css/tipsy.css">
<body>
<div class="wrapper">
    <div class="outer">
        <div class="condition-box text-center" style="display: none;">
            <div class="condition-title text-center">关系查询</div>
            <div class="condition-con mt20 text-left">
                <div class="condition-header">查询条件</div>
                <div class="condition-content row">
                    <div class="col-sm-10">
                        <div class="row">
                            <div class="col-sm-3">
                                <div class="row">
                                    <span class="col-sm-3 text-right">客户号：</span>
                                    <span class="col-sm-9">
                                    <input class="form-control col-sm-6" id="input-@gray-base1" type="text" value="" data-var="@gray-base"/>
                                </span>
                                </div>
                            </div>
                            <div class="col-sm-3">
                                <div class="row">
                                    <span class="col-sm-3 text-right">申请书编号：</span>
                                    <span class="col-sm-9">
                                    <input class="form-control col-sm-6" id="input-@gray-base2" type="text" value="" data-var="@gray-base"/>
                                </span>
                                </div>
                            </div>
                            <div class="col-sm-3">
                                <div class="row">
                                    <span class="col-sm-3 text-right">账户号：</span>
                                    <span class="col-sm-9">
                                    <input class="form-control col-sm-6" id="input-@gray-base3" type="text" value="" data-var="@gray-base"/>
                                </span>
                                </div>
                            </div>
                            <div class="col-sm-3">
                                <div class="row">
                                    <span class="col-sm-3 text-right">卡号：</span>
                                    <span class="col-sm-9">
                                     <input class="form-control" id="input-@gray-base4" type="text" value="" data-var="@gray-base"/>
                                </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-2">
                        <button type="button" class=" btn btn-danger">查询</button>
                    </div>

                </div>

            </div>
        </div>
        <div class="table-box">
            <table id="table" class="table display mt10" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>name</th>
                        <th>descript</th>
                        <th>symbol</th>
                        <th>type</th>
                        <th>showLabelText</th>
                        <th>width</th>
                        <th>height</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="left" id="left">
        <div class="toggle-part tac"  id="toggle">
            <span id="font" class="icon-item icon-toggle"></span>
            <span class="text">案件反查系统</span>
        </div>
        <div class="search" id="searchBox">
            <div class="title">关联关系</div>
            <form id="searchForm" name="searchForm">
                <div class="condition">
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right mt10">查询类型</span>
                        <div class="col-sm-8">
                            <select id="searchType1" name="forType1" class="selectpicker show-tick form-control col-sm-9" title="请选择查询类型">
                                <option value="0">请选择查询类型</option>
                                <option value="customer">客户</option>
                                <option value="card">卡</option>
                                <option value="account">账户</option>
                                <option value="applicationForm">申请书</option>
                                <option value="telephone">电话</option>
                                <option value="ip">ip</option>
                            </select>
                        </div>
                    </div>
                    <div id="condition1" class="mt10">




                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right mt10">查询类型</span>
                        <div class="col-sm-8">
                            <select id="searchType2" name="forType2" class="selectpicker show-tick form-control col-sm-9" title="请选择查询类型">
                                <option value="0">请选择查询类型</option>
                                <option value="customer">客户</option>
                                <option value="card">卡</option>
                                <option value="account">账户</option>
                                <option value="applicationForm">申请书</option>
                                <option value="telephone">电话</option>
                                <option value="ip">ip</option>
                            </select>
                        </div>
                    </div>
                    <div id="condition2" class="mt10"></div>
                    <div class="mt20 text-center operate">
                        <button type="button" onclick="searchForm1()" class="btn btn-default btn-search">查询</button>
                        <button id="reset" type="button" class="btn btn-default btn-reset">重置</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <svg class="svg" id="svg" width=100% height=600></svg>
</div>
<div id="box"  style="display: none"></div>

<div class="info-box" id="info">
    <div id="infoHtml" class="info-inner"></div>
    <a id="look" class="look" href="javascript:;">查看所有关系</a>
</div>

<ul  id="menu" class="operate-menu" style="display: none">
    <li class="active" for="chooseNode">选中节点</li>
    <li for="showPath">设置开始点</li>
    <li for="secondSearch">二次查询</li>
    <li for="exportData">导出</li>
</ul>
<div class="export-type" id="exportType" style="display: none;">
    <from action="" id="exportData" name="exportData">
        <div class="common-radio mt20">
            <input value="single" tabindex="1" type="radio" id="single" name="export" checked="checked"/>
            <label for="single">单点导出</label>
        </div>
        <div class="common-radio mt20">
            <input value="multi" tabindex="2" type="radio" id="multi" name="export" />
            <label for="multi">多层导出</label>
        </div>
        <div class="common-radio mt20">
            <input value="all" tabindex="3" type="radio" id="all" name="export" />
            <label for="all">全部导出</label>
        </div>
    </from>
    <button class="blue-button text-center mt30" type="submit" onclick="exportData()">现在导出</button>
</div>
<div class="second-search text-center" id="secondSearch" style="display: none;">
    <form id="secondSearchForm" name="secondSearchForm">
        <div class="line row mt20">
            <span class="col-sm-4 m-t-xs text-right f14 mt5">查询类型</span>
            <div class="col-sm-8">
                <select id="searchType3" name="forType3" class="selectpicker show-tick form-control col-sm-9" title="请选择查询类型">
                    <option value="0">请选择查询类型</option>
                    <option value="customer">客户</option>
                    <option value="card">卡</option>
                    <option value="account">账户</option>
                    <option value="applicationForm">申请书</option>
                    <option value="telephone">电话</option>
                    <option value="ip">ip</option>
                </select>
            </div>
        </div>
        <div class="condition">
            <div id="condition3"></div>
            <div class="line row mt20 f14">
                <span class="col-sm-4 m-t-xs text-right f14">关系</span>
                <div class="col-sm-8 text-left">
                <span class="common-radio">
                    <input value="01" tabindex="1" type="radio" id="and" name="choice" checked="checked"/>
                    <label for="and">且</label>
                </span>
                    <span class="common-radio ml20">
                    <input value="02" tabindex="2" type="radio" id="or" name="choice" />
                    <label for="or">或</label>
                </span>
                </div>
            </div>
        </div>
        <div class="line text-left">
            <span class="col-sm-4 m-t-xs">&nbsp;</span>
            <button class="blue-button text-center mt20" onclick="searchForm2()" type="button">现在查询</button>
        </div>

    </form>


</div>
</body>
<script type="text/javascript" src="./static/js/jquery-1.12.4.min.js" charset="utf-8"></script>
<script type="text/javascript" src="./static/js/layer/layer.js" charset="utf-8"></script>
<script type="text/javascript" src="./static/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="./static/js/bootstrap/bootstrap-select.min.js"></script>
<script type="text/javascript" src="./static/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="./static/js/d3.v4.min.js" charset="utf-8"></script>
<script type="text/javascript" src="./static/js/dagre-d3.js"></script>
<script type="text/javascript" src="./static/js/tipsy.js"></script>
<script type="text/javascript" src="./static/js/tree.js"></script>
<script type="text/javascript" src="./static/js/search.js"></script>
<script type="text/javascript" src="./static/js/icheck/icheck.min.js"></script>
<script>
    $(function(){
        $(".common-radio input").iCheck({
            radioClass:'iradio_square-aero'
        });

    });

</script>
</html>


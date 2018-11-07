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
                        <th>category</th>
                        <th>value</th>
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
                        <span class="col-sm-4 m-t-xs text-right">账户号:</span>
                        <div class="col-sm-8">
                            <select id="account" name="account" class="selectpicker show-tick form-control col-sm-9" title="请选择账户号">
                                <option value="0">请选择账户号</option>
                                <option value="zh1">2998000050900151</option>
                                <option value="zh2">2998000050900152</option>
                                <option value="zh3">2998000050900153</option>
                                <!--<option value="zh4">2998000050900161</option>-->
                                <!--<option value="zh5">2998000050900162</option>-->
                                <!--<option value="zh6">2998000050900163</option>-->
                                <!--<option value="zh7">2998000050900164</option>-->
                                <!--<option value="zh8">2998000050900165</option>-->
                                <!--<option value="zh9">2998000050900166</option>-->
                                <!--<option value="zh10">2998000050900167</option>-->
                                <!--<option value="zh11">2998000050900168</option>-->
                                <!--<option value="zh12">2998000050900169</option>-->
                                <!--<option value="zh13">2998000050900170</option>-->
                                <!--<option value="zh14">2998000050900171</option>-->
                                <!--<option value="zh15">2998000050900172</option>-->
                                <!--<option value="zh16">2998000050900173</option>-->
                                <!--<option value="zh17">2998000050900174</option>-->
                            </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">客户号:</span>
                        <div class="col-sm-8"><select id="customer" name="customer" class="selectpicker show-tick form-control col-sm-9" title="请选择客户号">
                            <option value="0">请选择客户号</option>
                            <option value="cu1">600118772311</option>
                            <option value="cu2">600118772312</option>
                            <option value="cu3">600118772313</option>
                            <option value="cu4">600118772314</option>
                            <!--<option value="cu5">600118772316</option>-->
                            <!--<option value="cu6">600118772317</option>-->
                            <!--<option value="cu7">600118772318</option>-->
                            <!--<option value="cu8">600118772319</option>-->
                            <!--<option value="cu9">600118772320</option>-->
                            <!--<option value="cu10">600118772321</option>-->
                            <!--<option value="cu11">600118772322</option>-->
                            <!--<option value="cu12">600118772323</option>-->
                            <!--<option value="cu13">600118772324</option>-->
                            <!--<option value="cu14">600118772325</option>-->
                            <!--<option value="cu15">600118772326</option>-->
                        </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">卡号:</span>
                        <div class="col-sm-8"><select id="card" name="card" class="selectpicker show-tick form-control col-sm-9" title="请选择卡号">
                            <option value="0">请选择卡号</option>
                            <option value="K1">201809043000101</option>
                            <option value="K2">201809043000102</option>
                            <option value="K3">201809043000103</option>
                            <option value="K4">201809043000104</option>
                            <option value="K5">201809043000105</option>
                            <!--<option value="K6">201809043000501</option>
                            <option value="K7">201809043000601</option>-->
                            <!--<option value="K8">201809043000701</option>-->
                            <!--<option value="K9">201809043000801</option>-->
                            <!--<option value="K10">201809043000901</option>-->
                            <!--<option value="K11">201809043001001</option>-->
                            <!--<option value="K12">201809043001101</option>-->
                            <!--<option value="K13">201809043001201</option>-->
                            <!--<option value="K14">201809043001301</option>-->
                            <!--<option value="K15">201809043001401</option>-->
                            <!--<option value="K16">201809043001501</option>-->
                        </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">申请书编号:</span>
                        <div class="col-sm-8"><select id="apply" name="apply" class="selectpicker show-tick form-control col-sm-9" title="请选择申请书编号">
                            <option value="0">请选择申请书编号</option>
                            <option value="sq1">sq201809041000101</option>
                            <option value="sq2">sq201809041000102</option>
                            <option value="sq3">sq201809041000103</option>
                            <option value="sq4">sq201809041000204</option>
                            <option value="sq5">sq201809041000305</option>
                            <option value="sq6">sq201809041000306</option>
                            <option value="sq7">sq201809041000407</option>
                            <!--<option value="sq8">sq201809041000501</option>-->
                            <!--<option value="sq9">sq201809041000601</option>-->
                            <!--<option value="sq10">sq201809041000701</option>-->
                            <!--<option value="sq11">sq201809041000801</option>-->
                            <!--<option value="sq12">sq201809041000901</option>-->
                            <!--<option value="sq13">sq201809041001001</option>-->
                            <!--<option value="sq14">sq201809041001101</option>-->
                            <!--<option value="sq15">sq201809041001201</option>-->
                            <!--<option value="sq16">sq201809041001301</option>-->
                            <!--<option value="sq17">sq201809041001401</option>-->
                            <!--<option value="sq18">sq201809041001501</option>-->
                        </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">推广人id:</span>
                        <div class="col-sm-8"><select id="tgr" name="tgr" class="selectpicker show-tick form-control col-sm-9" title="请选择推广人id">
                            <option value="0">请选择推广人id:</option>
                            <option value="sq3141">07551001</option>
                            <!--<option value="sq3425">07551003</option>-->
                            <!--<option value="sq3542">07551004</option>-->
                            <!--<option value="sq3216">07551002</option>-->
                        </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">推广人机构id:</span>
                        <div class="col-sm-8"><select id="tgjg" name="tgjg"  class="selectpicker show-tick form-control col-sm-9" title="请选择推广人机构id">
                            <option value="0">请选择推广人机构id:</option>
                            <option value="sq2356">广东省深圳市招商银行新安支行</option>
                            <!--<option value="sq2640">广东省深圳市招商银行科发支行</option>-->
                            <!--<option value="sq2757">广东省深圳市招商银行科苑支行</option>-->
                            <!--<option value="sq2431">广东省深圳市招商银行龙华支行</option>-->
                        </select>
                        </div>
                    </div>
                    <div class="line row mt10">
                        <span class="col-sm-4 m-t-xs text-right">所属公司:</span>
                        <div class="col-sm-8">
                            <select id="company" name="company"  class="selectpicker show-tick form-control col-sm-9" title="请选择所属公司">
                                <option value="0">请选择所属公司</option>
                                <option value="cu1565">92440300731093239W</option>
                                <option value="cu1566">92440300731093239X</option>
                                <option value="cu1567">92440300731093239Y</option>
                                <option value="cu1568">92440300731093239Z</option>
                                <!--<option value="cu1576">91310118574170279B</option>-->
                                <!--<option value="cu1577">52310230MJ5339125F</option>-->
                                <!--<option value="cu1578">52310105MJ50200186</option>-->
                                <!--<option value="cu1579">53310000MJ49517770</option>-->
                                <!--<option value="cu1566">51440300MJL1712376</option>-->
                                <!--<option value="cu1567">52440300MJL186084F</option>-->
                                <!--<option value="cu1568">913101124250883359</option>-->
                                <!--<option value="cu1569">91310105134649745E</option>-->
                                <!--<option value="cu1570">91310115771471337E</option>-->
                                <!--<option value="cu1571">52310104MJ49963266</option>-->
                                <!--<option value="cu1572">52310107MJ5075334L</option>-->
                            </select>
                        </div>
                    </div>
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
    <from action="">
        <div>
            <div class="line row mt20">
                <span class="col-sm-4 m-t-xs text-right f14 mt5">账户号:</span>
                <div class="col-sm-8">
                    <select id="attr1" name="account" class="selectpicker show-tick form-control col-sm-9" title="请选择账户号">
                        <option value="0">请选择账户号</option>
                        <option value="zh1">2998000050900151</option>
                        <option value="zh2">2998000050900152</option>
                        <option value="zh3">2998000050900153</option>
                    </select>
                </div>
            </div>
            <div class="line row mt20">
                <span class="col-sm-4 m-t-xs text-right f14 mt5">账户号:</span>
                <div class="col-sm-8">
                    <select id="attr2" name="account" class="selectpicker show-tick form-control col-sm-9" title="请选择账户号">
                        <option value="0">请选择账户号</option>
                        <option value="zh1">2998000050900151</option>
                        <option value="zh2">2998000050900152</option>
                        <option value="zh3">2998000050900153</option>
                    </select>
                </div>
            </div>
            <div class="line row mt20">
                <span class="col-sm-4 m-t-xs text-right f14 mt5">账户号:</span>
                <div class="col-sm-8">
                    <select id="attr3" name="account" class="selectpicker show-tick form-control col-sm-9" title="请选择账户号">
                        <option value="0">请选择账户号</option>
                        <option value="zh1">2998000050900151</option>
                        <option value="zh2">2998000050900152</option>
                        <option value="zh3">2998000050900153</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="line row mt20 f14">
            <span class="col-sm-4 m-t-xs text-right f14">关系:</span>
            <div class="col-sm-8 text-left">
                <span class="common-radio">
                    <input value="01" tabindex="1" type="radio" id="and" name="relate" checked="checked"/>
                    <label for="and">且</label>
                </span>
                <span class="common-radio ml20">
                    <input value="02" tabindex="2" type="radio" id="or" name="relate" />
                    <label for="or">或</label>
                </span>
            </div>
        </div>

    </from>
    <button class="blue-button text-center mt40" >现在查询</button>
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

        //table
        $('#table').DataTable( {
            "ajax": "./static/datas/data.txt",
            "pageLength":6,
            "lengthChange": false,
            "searching": false,
            "info": "",
            "columns": [
                { "data": "id" },
                { "data": "name" },
                { "data": "descript" },
                { "data": "symbol" },
                { "data": "category" },
                { "data": "value" }
            ]
        } );
    });

</script>
</html>


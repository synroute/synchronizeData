<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" deferredSyntaxAllowedAsLiteral="true" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html style="height:100%;width: 100%">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible"  content="IE=edge,chrome=1">
    <title>同步CTI数据服务配置</title>
    <style type="text/css">
    	.item {
    		margin:0 auto;
	    	margin-bottom:10px;
	    }
	    .datagrid-header {
			position: absolute; visibility: hidden;
		}
    </style>
</head>
<body style="width:100%;height: 100%;padding:0;margin:0;">
	<main class="loaded">
		<div class="loader">
		    <div class="loader-inner ball-pulse-sync" style="text-align: center;">
		      <div></div>
		      <div></div>
		      <div></div>
		    </div>
		</div>
	</main>
	<div class="easyui-layout" style="width:100%;height:100%">
	  	<div data-options="region:'north'" style="width:100%;height:45%;">
	  		<div class="easyui-layout" style="width:100%;height:100%;">   
			    <div data-options="region:'center',title:'来源数据库'" style="width:50%;height:100%;">
					<div id="sourceDatabase" style="margin:0 auto;width:100%;height:100%;">
						<div class="item" style="width:70%;margin-top:10px;">
							<select id="sourceDatabaseType" class="easyui-combobox" name="dept" style="width:100%;" data-options="label:'数据库类型',labelAlign:'right',editable:false,">   
							    <option value="oracle">oracle</option>
							    <option value="mySQL">mySQL</option>   
							</select> 
						</div> 
						<div class="item" style="width:70%">
			    			<input id="sourceIP" class="easyui-textbox" style="width: 100%;"
									data-options="label:'IP',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="sourcePort" class="easyui-textbox" style="width: 100%;"
									data-options="label:'端口',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="sourceUser" class="easyui-textbox" style="width: 100%;"
									data-options="label:'用户名',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="sourcePassword" class="easyui-textbox" style="width: 100%;"
									data-options="label:'密码',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="sourceSID" class="easyui-textbox" style="width: 100%;"
									data-options="label:'SID',labelAlign:'right',">
			    		</div>
			    		<div class="item" style="width:70%;text-align:right;">
			    			<a id="sourceTest" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="">测试</a>
			    		</div>
					</div>
			    </div> 
			    <div data-options="region:'east',title:'目标数据库'" style="width:50%;height:100%;">
			    	<div id="targetDatabase" style="margin:0 auto;width:100%;height:100%;">
						<div class="item" style="width:70%;margin-top:10px;">
							<select id="targetDatabaseType" class="easyui-combobox" name="dept" style="width:100%;" data-options="label:'数据库类型',labelAlign:'right',editable:false,">   
							    <option value="oracle">oracle</option>
							    <option value="mySQL">mySQL</option>   
							</select> 
						</div> 
						<div class="item" style="width:70%">
			    			<input id="targetIP" class="easyui-textbox" style="width: 100%;"
									data-options="label:'IP',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="targetPort" class="easyui-textbox" style="width: 100%;"
									data-options="label:'端口',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="targetUser" class="easyui-textbox" style="width: 100%;"
									data-options="label:'用户名',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="targetPassword" class="easyui-textbox" style="width: 100%;"
									data-options="label:'密码',labelAlign:'right',">
			    		</div>
						<div class="item" style="width:70%">
			    			<input id="targetSID" class="easyui-textbox" style="width: 100%;"
									data-options="label:'SID',labelAlign:'right',">
			    		</div>
			    		<div class="item" style="width:70%;text-align:right;">
			    			<a id="targetTest" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="">测试</a>
			    		</div>
					</div>
			    </div> 
		    </div> 
	  	</div>   
	    <div data-options="region:'center'" style="width:100%;height:10%;">
	    	<div style="width:100%;height:80%;margin-top:10px;">
	    		<div class="item" style="margin:0 auto;width:30%;">
		    		<select id="synTime" class="easyui-combobox" name="dept" style="width:50%;" data-options="label:'同步时间',labelAlign:'right',editable:false,">   
					    <option value="5">5</option>
					    <option value="10">10</option>
					    <option value="15">15</option>
					    <option value="20">20</option>   
					</select> 
					<label style="margin-right:30px;">分钟</label>
					<a id="startSync" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="">开始同步</a>
	    			<a id="endSync" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="">结束同步</a>
	    		</div>
	    	</div>
	    </div> 
	    <div data-options="region:'south'" style="width:100%;height:45%;">
	    	<div class="easyui-layout" style="width:100%;height:100%;"> 
			    <div data-options="region:'center'" style="width:50%;height:100%;">
			    	<div style="width:100%;height:40px;line-height:35px;text-align:right;">
			    		<a id="detection" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="">检测</a>
			    	</div>
			    	<div id="textList" style="width:100%;position:absolute;top:40px;bottom:0;">
						<table id="courseShow" class="easyui-datagrid" style="width:100%;height:90%"   
						        data-options="singleSelect:true,fitColumns:true,checkOnSelect:false,selectOnCheck:false,onClickRow:courseShow">   
						    <thead>   
						        <tr>  
						        	<th id="ck" data-options="field:'ck',checkbox:true"></th> 
						        	<th data-options="field:'courseId',width:80">课程编号</th>
						            <th data-options="field:'courseName',width:80">课程名称</th>  
						        	<th data-options="field:'createTime',width:100">创建时间</th>  
						            <th data-options="field:'courseTypeChina',width:100">操作类型</th>
						            <th data-options="field:'isUsedChina',width:50">是否启用</th>
						            <th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th> 
						        </tr>   
						    </thead> 
						</table> 
					</div>
			    </div>   
			    <div data-options="region:'east'" style="width:50%;height:100%;">
					<ul>
						
					</ul>
			    </div> 
		    </div> 
	    </div>   
	</div>
	<script>
	<%-- $(function(){
		$("#courseShow").datagrid({
            url: '<%=tenant.getRootUrl()%>/srv/TrainController/selectCourses.srv',
            pagination: true,
            pageSize:10,
            pageList:[10,20,30],
            queryParams:{
            	courseName:'',
            	startTime:'2018-05-28 00:00',
            	endTime:'2018-06-12 23:59',
            	courseType: 0,
            	isUsed:0
            }
        }); 
	}) --%>
	function formatOper(val,row,index){
		if(row.isUsed == 0){
			return '<img src="<%=tenant.getRootUrl()%>/modules/examTrain/images/through.png">';
		}else{
			return '<img src="<%=tenant.getRootUrl()%>/modules/examTrain/images/noThrough.png">';
		}
	}
	</script>
</body>
</html>

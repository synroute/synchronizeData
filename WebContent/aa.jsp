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
    <link rel="stylesheet" type="text/css" href="./css/synrouteStyle-1.0.css">
    <link rel="stylesheet" type="text/css" href="./css/commonStyle-1.02.css">
    <link rel="stylesheet" type="text/css" href="./css/DMSetting.css">
    <link rel="stylesheet" type="text/css" href="./css/optiscroll.css">
    <link rel="stylesheet" type="text/css" href="./themes/material/easyui.css">
    <link rel="stylesheet" type="text/css" href="./themes/icon.css">
    <link rel="stylesheet" type="text/css" href="./themes/color.css">
    <script type="text/javascript" src="jquery-easyui-1.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="./js/jquery.form.js"></script>
    <script type="text/javascript" src="./js/optiscroll.js"></script>
    <script type="text/javascript" src="./js/hiagent.js"></script>
    <style type="text/css">
    	.item {
    		margin:0 auto;
	    	margin-bottom:10px;
	    }
    </style>
</head>
<body id="layout" style="width:100%;height: 100%;padding:0;margin:0;">
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
	  	<div data-options="region:'north'" style="width:100%;height:40%;">
	  		<div class="easyui-layout" style="width:99.5%;height:100%;">   
			    <div data-options="region:'center',title:'来源数据库'" style="width:30%;height:100%;">
					<div id="sourceDatabase" style="margin:0 auto;width:100%;">
						 <div class="item" style="width:80%;margin-top:10px;">
						        <select id="sourceDatabaseType" class="easyui-combobox" name="dept" style="width:90%;" data-options="label:'数据库类型',labelAlign:'right',editable:false,">   
								    <option value="oracle">oracle</option>
								    <option value="mySQL">mySQL</option>   
								</select>
						    </div>
						     <div class="item" style="width:80%">
						        <input id="sourceIP" class="easyui-textbox" style="width: 90%;" data-options="label:'IP',labelAlign:'right',">
						    </div>
						    <div class="item" style="width:80%">
						        <input id="sourcePort" class="easyui-textbox" style="width: 90%;" data-options="label:'端口',labelAlign:'right',">
						    </div>
						    <div class="item" style="width:80%">
						        <input id="sourceUser" class="easyui-textbox" style="width: 90%;" data-options="label:'用户名',labelAlign:'right',">
						    </div>
						     <div class="item" style="width:80%">
						        <input id="sourcePassword" class="easyui-textbox" style="width: 90%;" data-options="label:'密码',labelAlign:'right',">
						    </div>
						    <div class="item" style="width:80%;margin-bottom:10px;">
						        <input id="sourceSID" class="easyui-textbox" style="width: 90%;" data-options="label:'SID',labelAlign:'right',">
						    </div>
			    		<div class="item" style="width:70%;text-align:right;">
			    			<a id="sourceSaveBtn" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="sourceSaveBtn()">保存</a>
			    			<a id="sourceTest" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="sourceTest()">测试</a>
			    			<span id="sourceResult"></span>
			    		</div>
					</div>
			    </div> 
			    <div data-options="region:'east',title:'目标数据库'" style="width:70%;height:100%;margin:0;">
			    	<div class="easyui-layout" style="width:99.5%;height:100%;">  
					    <div data-options="region:'west'" style="width:60%;height:100%;position:relative;">
					    	<div style="height:40px;line-height:35px;text-align:right;">
					    		<a id="delectBtn" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="delectBtn()">删除</a>
					    		<a id="addBtn" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="addBtn()">新增</a>
					    	</div>
					    	<div style="width:100%;position:absolute;top:40px;bottom:0;">
								<table id="targetList" class="easyui-datagrid" style="width:100%;height:100%;"   
			        					data-options="fitColumns:true,singleSelect:true,onClickRow:targetList">  
								    <thead>   
								        <tr>  
								         	<th data-options="field:'tableId',width:80,"> tableId</th>
								            <th data-options="field:'dbType',width:80,">数据库类型</th>   
								            <th data-options="field:'dbUser',width:80,">用户名</th>    
								            <th data-options="field:'dbPort',width:80,">端口号</th> 
			              					<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">状态</th>  
								        </tr>   
								    </thead>   
								</table>
							</div>
					    </div>   
					    <div data-options="region:'center'" style="width:40%;height:100%;">
					    	<div style="height:40px;line-height:35px;text-align:right;">
					    		<a id="sourceAddBtn" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="sourceAddBtn()">保存</a>
					    		<a id="cancel" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="cancel()">取消</a>
					    	</div>
					    	<div id="targetDatabase" style="margin:0 auto;width:100%;">
								<div class="item" style="width:80%;margin-top:10px;">
									<select id="targetDatabaseType" class="easyui-combobox" name="dept" style="width:90%;" data-options="label:'数据库类型',labelAlign:'right',editable:false,">   
									    <option value="oracle">oracle</option>
									    <option value="mySQL">mySQL</option>   
									</select> 
								</div> 
								<div class="item" style="width:80%">
					    			<input id="targetIP" class="easyui-textbox" style="width: 90%;"
											data-options="label:'IP',labelAlign:'right',">
					    		</div>
								<div class="item" style="width:80%">
					    			<input id="targetPort" class="easyui-textbox" style="width: 90%;"
											data-options="label:'端口',labelAlign:'right',">
					    		</div>
								<div class="item" style="width:80%">
					    			<input id="targetUser" class="easyui-textbox" style="width: 90%;"
											data-options="label:'用户名',labelAlign:'right',">
					    		</div>
								<div class="item" style="width:80%">
					    			<input id="targetPassword" class="easyui-textbox" style="width: 90%;"
											data-options="label:'密码',labelAlign:'right',">
					    		</div>
								<div class="item" style="width:80%;margin-bottom:10px;">
					    			<input id="targetSID" class="easyui-textbox" style="width: 90%;"
											data-options="label:'SID',labelAlign:'right',">
					    		</div>
							</div>
				    		<div class="item" style="width:68%;text-align:right;">
				    			<a id="targetSave" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="targetSave()">保存</a>
				    			<a id="targetTest" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="targetTest()">测试</a>
				    			<span id="targetResult"></span>
				    		</div>
					    </div>   
					</div>
				</div> 
		    </div> 
	  	</div> 
	    <div data-options="region:'center'" style="width:98%;height:50%;">
	    	<div class="easyui-layout" style="width:100%;height:100%;"> 
			    <div data-options="region:'center'" style="width:50%;height:100%;">
			    	<div style="width:100%;height:40px;line-height:35px;text-align:right;">
			    		<span id="info"></span>
			    		<a id="tableListSave" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="tableListSave()">保存</a>
			    		<a id="detection" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="detection()">检测</a>
			    	</div>
			    	<div id="list" style="width:100%;position:absolute;top:40px;bottom:0;">
			    	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
					    <div data-options="region:'west'," style="width:60%;height:100%;position:ralative;">
					    	<div style="width:100%;position:absolute;top:0;bottom:0;">
					    		<table id="sourceListInfo" class="easyui-datagrid" style="width:100%;height:100%;"   
			        					data-options="fitColumns:true,singleSelect:false,selectOnCheck:true">  
								    <thead>   
								        <tr>   
								           <!--  <th data-options="field:'userId',width:100,">坐席ID</th>  -->
								            <th data-options="field:'ck',checkbox:true">  
								            <th data-options="field:'tableName',width:160,">表名称</th>
								            <th data-options="field:'fieldName',width:80,">已选标识列</th> 
			              					<th data-options="field:'_operate',width:40,align:'center',formatter:formatOper1">状态</th> 
								        </tr>   
								    </thead>   
								</table> 
					    	</div>
					    </div>   
					    <div data-options="region:'center'" style="width:40%;height:100%;position:ralative;">
					    	<div style="width:100%;position:absolute;top:0;bottom:0;">
					    		<table id="listInfo" class="easyui-datagrid" style="width:100%;height:100%;"   
			        					data-options="fitColumns:true,singleSelect:true,">  
								    <thead>   
								        <tr>   
								           <!--  <th data-options="field:'userId',width:100,">坐席ID</th>  -->  
								            <th data-options="field:'fieldName',width:100,">待选标识列</th> 
								            <th data-options="field:'fieldType',width:100,">待选标识类型</th> 
			              					<th data-options="field:'operate',width:80,align:'center',formatter:formatOper2">操作</th>    
								        </tr>   
								    </thead>   
								</table> 
					    	</div>
					    </div>   
					</div>  
			    	
					</div>
			    </div>   
			    <div data-options="region:'east'" style="width:50%;height:100%;">
					<ul id="ul">
						
					</ul>
			    </div> 
		    </div> 
	    </div>  
	    <div data-options="region:'south'" style="width:100%;height:10%;">
	    	<div style="width:100%;height:80%;margin-top:10px;">
	    		<div class="item" style="margin:0 auto;width:40%;">
		    		<select id="synTime" class="easyui-combobox" name="dept" style="width:50%;" data-options="label:'同步时间',labelAlign:'right',editable:false,">   
					    <option value="5">5</option>
					    <option value="10">10</option>
					    <option value="15">15</option>
					</select> 
					<label style="margin-right:30px;">分钟</label>
					<a id="saveTimeBtn" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="saveTimeBtn()">保存</a>
					<a id="startSync" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="startSync()">开始同步</a>
	    			<a id="endSync" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="endSync()">结束同步</a>
	    			<a id="initiatingSync" href="javascript:void(0)" style="margin-right:5px;" class="easyui-linkbutton" data-options="" onclick="initiatingSync()">初始化同步</a>
	    		</div>
	    	</div>
	    </div>  
	</div>
	<script>
	
	/* var webSocket = new WebSocket('ws://localhost:8080/websocket');
	webSocket.onerror = function(event) {
		alert("连接建立error");
        onError(event)
    };
    webSocket.onopen = function(event) {
    	alert("连接建立open");
        onOpen(event)
    };
    webSocket.onmessage = function(event) {
    	debugger;
        onMessage(event)
    };
    function onMessage(event) {
    	getCurrentTime(event.data);
    }
    function onOpen(event) {
    	getCurrentTime("socket连接建立Open");
    }
    function onError(event) {
    	getCurrentTime("socket连接失败");
    }
    function start() {
    	alert("连接建立start");
        webSocket.send('hello');
        return false;
    } */
	
	
	
	
	var bool = true,rowIndex="",tableName = "",fileName="",tableId = "",boolType = false,state = "",data="",list="";
    var dataState = "";
	$(function(){
		$("#sourceTest,#targetTest,#list,#detection,#startSync,#endSync,#tableListSave,#targetDatabase,#targetSave,#sourceAddBtn,#cancel").hide();
		$("#targetList").datagrid("hideColumn","tableId");
		//$("#sourceListInfo").datagrid("hideColumn","_operate");
		var username = getCookie("0000");
		console.log(username);
		if(!username){
			window.location.href="index.jsp";
			return;
		}
		getSourceInfo();
		getTargetList();
		getSynchronousState();
		watchAll();
		return;
		
		
	})
	
	//加载  -- 来源数据库
	function getSourceInfo(){
		$.ajax({
			url: 'SourceDbConfig',
			type: 'post',
			data: {
				action: "getSourceDbConfig"
			},
			dataType: 'json',
			success: function(res){
				//console.log(res);
				var data = res[0];
				$("#sourceDatabaseType").combobox('setValue', data.dbType);
				$("#sourceIP").textbox("setValue", data.dbIp);
				$("#sourcePort").textbox("setValue", data.dbPort);
				$("#sourceUser").textbox("setValue", data.dbUser);
				$("#sourcePassword").textbox("setValue", data.dbPassword);
				$("#sourceSID").textbox("setValue", data.dbSid); 
				if(res[0].passTest == "1"){
					$("#sourceTest").show();
					sourceTest();
					getUpdata();
				}
			}
		})
	}
	
	//来源数据库 -- 保存
	function sourceSaveBtn(){
		$("#sourceTest").show();
		var sourceDatabaseType = $("#sourceDatabaseType").combobox('getText');
		var sourceIP = $("#sourceIP").textbox('getText');
		var sourcePort = $("#sourcePort").textbox('getText');
		var sourceUser = $("#sourceUser").textbox('getText');
		var sourcePassword = $("#sourcePassword").textbox('getText');
		var sourceSID = $("#sourceSID").textbox('getText');
		if(sourceDatabaseType == "" || sourceIP == "" || sourcePort == "" || sourceUser == "" || sourcePassword == "" || sourceSID == ""){
			$.messager.alert("提示","所选项不能为空");
			return;
		}else{
			$.ajax({
				url: 'SourceDbConfig',
				type: 'post',
				data: {
					action:"saveSourceDbConfig",
					dbType:sourceDatabaseType,
					dbIp:sourceIP,
					dbPort:sourcePort,
					dbSid:sourceSID,
					dbUser:sourceUser,
					dbPassword:sourcePassword
				},
				dataType: 'text',
				success:function(res){
					//console.log(res);
					getCurrentTime (res);
				}
			})
		}
	}
	
	// 来源数据库 -- 测试
	function sourceTest(){
		$.ajax({
			url: 'SourceDbConfig',
			type: 'post',
			data: {
				action:"testSourceDbConfig"
			},
			dataType: 'text',
			//async: false,
			success: function(res){
				//console.log(res);
				if(res != "获取来源数据库连接成功"){
					bool = false;
				}
				$("#list,#detection,#tableListSave").show();
				$("#sourceResult").html(res);
				getCurrentTime (res);
				getSourseExcel();
				watchAll();
			}
		})
	}
	
	//获取来源数据库的所有表
	function getSourseExcel(){
		$.ajax({
			url: 'TableConfigController',
			type: 'post',
			data:{
				action:"getAllTableFromSource"
			},
			dataType: 'json',
			//async: false,
			success: function(res){
				//console.log(res);
				data = res;
				//console.log(data);
				$("#sourceListInfo").datagrid("loadData",res);
				$("#sourceListInfo").datagrid({
					onSelect:function(index, row){
						rowIndex = index;
						tableName = row.tableName;
						fileName = row.fileName;
						//console.log(row);
						$.ajax({
							url: 'TableConfigController',
							type: 'post',
							data: {
								action:"getAllFieldByTableName",
								tableName:row.tableName
							},
							dataType: 'json',
							success: function(res){
								//console.log(res);
								$("#listInfo").datagrid("loadData",res);
							}
						})
					}
				})
			}
		})
	}
	

	
	//  表数据  -- 保存
	function tableListSave(){
		var listArr = [];
		var rows = $('#sourceListInfo').datagrid('getSelections');
		/* listArr.push({
			tableName: rows.tableName,
			fieldName: rows.fieldName
		}) */
		//console.log(listArr);
		$.ajax({
			url: 'TableConfigController',
			type: 'post',
			data: {
				action:"saveTableAndField",
				data: JSON.stringify(rows)
			},
			dataType: 'text',
			success:function(res){
				getCurrentTime (res);
			}
		}) 
	}
	
	//获取需要更新的表结构
	function getUpdata(){
		$.ajax({
			url: 'TableConfigController',
			type: 'post',
			data: {
				action:"getTableAndField"
			},
			dataType: 'json',
			//async: false,
			success:function(res){
				//console.log(res);
				list = res;
				//console.log(data);
				//console.log(list);
				//$("#sourceListInfo").datagrid("loadData",res);
				for(var j=0; j<data.length;j++){
					for(var i=0; i<res.length; i++){
						if(data[j].tableName == res[i].tableName){
							 $("#sourceListInfo").datagrid("selectRow",j);
							 $('#sourceListInfo').datagrid('updateRow',{
								 index: j,
								 row: res[i]
							 });
						}
					}
				}
			}
		})
	}
	//检测 
	function detection(){
		var targetList = $("#targetList").datagrid("getRows");
		var rows = $("#sourceListInfo").datagrid("getChecked");
		//console.log(rows);
		//console.log(rows.tableName);
		for(var j=0; j<targetList.length;j++){
			if(targetList[j].passTest == "0"){
				$.messager.alert("提示","目标数据库有检测未通过的,请先检测通过");
				return;
			}
		}
		for(var i=0; i<rows.length; i++){
			$.ajax({
				url: 'TableConfigController',
				type:'post',
				data: {
					action:"testTable",
					tableName:rows[i].tableName
				},
				dataType: 'text',
				success:function(res){
					//console.log(res);
					getCurrentTime (res);
					//getUpdata();
					//$("#sourceListInfo").datagrid("showColumn","_operate");//
				}
			})
		}
	}
	
	//目标数据库 -- 新增 
	function addBtn(){
		cancel();
		$("#targetDatabase,#sourceAddBtn,#cancel").show();
		$("#delectBtn,#targetSave,#targetTest").hide();
	}
	//目标数据库 -- 新增 保存
	function sourceAddBtn(){
		var targetDatabaseType = $("#targetDatabaseType").combobox('getText');
		var targetIP = $("#targetIP").textbox('getText');
		var targetPort = $("#targetPort").textbox('getText');
		var targetUser = $("#targetUser").textbox('getText');
		var targetPassword = $("#targetPassword").textbox('getText');
		var targetSID = $("#targetSID").textbox('getText');
		if(targetDatabaseType == "" || targetIP == "" || targetPort == "" || targetUser == "" || targetPassword == "" || targetSID == ""){
			$.messager.alert("提示","所选项不能为空");
			return;
		}else{
			$.ajax({
				url:'TargetDbConfig',
				type:'post',
				data:{
					action:'addTargetDbConfig',
					dbType:targetDatabaseType,
					dbIp:targetIP,
					dbPort:targetPort,
					dbSid:targetSID,
					dbUser:targetUser,
					dbPassword:targetPassword
				},
				dataType: 'text',
				success:function(res){
					//console.log(res);
					getCurrentTime (res);
					getTargetList();
					cancel();
				}
			})
		}
	}
	//取消按钮
	function cancel(){
		$("#targetDatabase,#sourceAddBtn,#cancel").hide();
		$("#addBtn,#delectBtn").show();
		$("#targetDatabaseType").combobox('setValue', "oracle");
		$("#targetIP").textbox("setValue", "");
		$("#targetPort").textbox("setValue", "");
		$("#targetUser").textbox("setValue", "");
		$("#targetPassword").textbox("setValue", "");
		$("#targetSID").textbox("setValue", "");
	}
	//目标数据库  -- 列表获取
	function getTargetList(){
		$.ajax({
			url: 'TargetDbConfig',
			type: 'post',
			data: {
				action:"getTargetDbConfig"
			},
			dataType: 'json',
			success: function(res){
				//console.log(res);
				for(var i=0; i<res.length; i++){
					if(res[i].passTest == 0){
						bool = false;
					}
				}
				$("#targetList").datagrid("loadData",res);
			}
		})
	}
	function formatOper(val,row,index){
		//console.log(row);
		if(row.passTest == 0){
			return '<img src="./images/unthrouge.png">';
		}else{
			return '<img src="./images/through.png">';
		}
   } 
	
 function formatOper1(val,row,index){
	 var sourceListInfo = $("#sourceListInfo").datagrid("getChecked");
	 //console.log(dataState);
	 if(dataState == 1){
		 for(var i=0; i<data.length; i++){
			 for(var j=0; j<sourceListInfo.length; j++){
				 if(data[i].tableName == sourceListInfo[j].tableName){
					 return '<img src="./images/through.png">';
				 }
			 }
		 }
	 }
  }  
 function formatOper2(val,row,index){
	  return '<a href="javascript:void(0)" id="modify'+index+'" onclick="modify('+index+')">确定</a>';
  }
  
	// 表对应的列  
	function modify(index){
		var listInfo = $('#listInfo').datagrid('getRows');//获得所有行
        var row = listInfo[index];//根据index获得其中一行。
        //var sourceListInfo = $('#sourceListInfo').datagrid('getRows');//获得所有行
        //var fileName = sourceListInfo[rowIndex].fieldName;//根据index获得其中一行。
        getSynchronousState();
        //console.log(fileName);
        if(state == 1){
        	$.messager.alert("提示","请先结束同步操作");
        	return;
        }else{
        	var obj = {};
    		obj.tableName = tableName;
    		obj.fieldName = row.fieldName;
    		//console.log(obj);
    		$('#sourceListInfo').datagrid('updateRow',{
    			index: rowIndex,
    			row: obj
    		});
        } 
	}
	//删除 
	function delectBtn(){
		var row = $('#targetList').datagrid('getSelected');
		 var rowIndex=$('#targetList').datagrid('getRowIndex',$('#targetList').datagrid('getSelected')); 
		console.log(row);
		console.log(rowIndex);
		if(row.length == 0){
			$.messager.alert("提示","请选择你要删除的数据库");
		}else{
			 $.messager.confirm('确认','确认删除?',function(row){
	                if(row){  
	                	$.ajax({
	            			url: 'TargetDbConfig',
	            			type: 'post',
	            			data: {
	            				action: "dropTargetDbConfig",
	            				tableId: row.tableId
	            			},
	            			dataType: 'text',
	            			success: function(res){
	            				//console.log(res);
	            				$('#targetList').datagrid("deleteRow",rowIndex);
	            				getCurrentTime (res);
	            				getTargetList();
	            			}
	            		})
	                }  
	            })
		} 
	}
	// 目标数据库 -- 回显
	function targetList(index,row){
		//console.log(row);
		cancel();
		tableId = row.tableId;
		$("#targetDatabase,#targetSave").show();
		$("#targetDatabaseType").combobox('setValue', row.dbType);
		$("#targetIP").textbox("setValue", row.dbIp);
		$("#targetPort").textbox("setValue", row.dbPort);
		$("#targetUser").textbox("setValue", row.dbUser);
		$("#targetPassword").textbox("setValue", row.dbPassword);
		$("#targetSID").textbox("setValue", row.dbSid);
		$("#targetTest").hide();
		$("#targetResult").html("");
	}
	//目标数据库 -- 保存
	function targetSave(){
		$("#targetTest").show();
		var targetDatabaseType = $("#targetDatabaseType").combobox('getText');
		var targetIP = $("#targetIP").textbox('getText');
		var targetPort = $("#targetPort").textbox('getText');
		var targetUser = $("#targetUser").textbox('getText');
		var targetPassword = $("#targetPassword").textbox('getText');
		var targetSID = $("#targetSID").textbox('getText');
		if(targetDatabaseType == "" || targetIP == "" || targetPort == "" || targetUser == "" || targetPassword == "" || targetSID == ""){
			$.messager.alert("提示","所选项不能为空");
			return;
		}else{
			$.ajax({
				url:'TargetDbConfig',
				type:'post',
				data:{
					action:'modifyTargetDbConfig',
					tableId:tableId,
					dbType:targetDatabaseType,
					dbIp:targetIP,
					dbPort:targetPort,
					dbSid:targetSID,
					dbUser:targetUser,
					dbPassword:targetPassword
				},
				dataType: 'text',
				success:function(res){
					//console.log(res);
					tableId = "";
					getCurrentTime (res);
					getTargetList();
				}
			})
		}
	}
	
	// 目标数据库 -- 测试
	function targetTest(){
		var targetDatabaseType = $("#targetDatabaseType").combobox('getText');
		var targetIP = $("#targetIP").textbox('getText');
		var targetPort = $("#targetPort").textbox('getText');
		var targetUser = $("#targetUser").textbox('getText');
		var targetPassword = $("#targetPassword").textbox('getText');
		var targetSID = $("#targetSID").textbox('getText');
		$.ajax({
			url: 'TargetDbConfig',
			type: 'post',
			data: {
				action:"testTargetDbConfig",
					dbType:targetDatabaseType,
					dbIp:targetIP,
					dbPort:targetPort,
					dbSid:targetSID,
					dbUser:targetUser,
					dbPassword:targetPassword
			},
			dataType: 'text',
			success: function(res){
				//console.log(res);
				$("#targetResult").html(res);
				//getTargetList();
				getCurrentTime (res);
			}
		})
	}
	
	
	//获取开启状态
	function getSynchronousState(){
		$.ajax({
			url: 'IntervalController',
			type: 'post',
			data: {
				action:"getIntervalAndState"
			},
			dataType: 'json',
			success:function(res){
				//console.log(res);
				state = res.isStart;
				if(res.isStart == 0){
					if(bool){
						$("#startSync").show();	
					}
				}else{
					$("#endSync").show();
				}
			}
		})
	}
	
	//保存间隔时间
	function saveTimeBtn(){
		var synTime = $("#synTime").combobox('getText');
		//console.log(synTime);
		$.ajax({
			url: 'IntervalController',
			type: 'post',
			data: {
				action:"saveInterval",
				interval: synTime
			},
			dataType: 'text',
			success:function(res){
				//console.log(res);
				getCurrentTime (res);
			}
		})
	}
	// 启动或关闭服务
	function serverType(data){
		$.ajax({
			url: 'SynchronizeController',
			type: 'post',
			data: {
				action: data
			},
			success: function(res){
				//console.log(res);
				getCurrentTime (res);
			}
		})
	}
	//开始同步
	function startSync(){
		$("#startSync").hide();
		$("#endSync").show();
		serverType("start");
	}
	//结束同步
	function endSync(){
		$("#startSync").show();
		$("#endSync").hide();
		serverType("end");
		state = "";
	}
	
	// 获取系统时间
	function getCurrentTime(content) {
            var date = new Date();
            var month = date.getMonth() + 1;

            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var hours = date.getHours();
            if (hours >= 0 && hours <= 9) {
                if (hours == 0) {
                    hours = "00";
                } else {
                    hours = "0" + hours;
                }
            }
            var minutes = date.getMinutes();
            if (minutes >= 0 && minutes <= 9) {
                if (minutes == 0) {
                    minutes = "00";
                } else {
                    minutes = "0" + minutes;
                }
            }
            var seconds = date.getSeconds();
            if (seconds >= 0 && seconds <= 9) {
                if (seconds == 0) {
                    seconds = "00";
                } else {
                    seconds = "0" + seconds;
                }
            }


            var currentdate = date.getFullYear() + "." + month + "." + strDate + " " + hours + ":" + minutes +
                ":" + seconds;

            //console.log(currentdate);
            //2017.07.11 15:14:44
            //time = currentdate;
            var str = '<li class="chatRecord" style="margin-top:4px;">'
		         +'<span class="time" style="fontsize: 12px;color:#58c5c7;margin:5px 5px 5px 10px;">' + currentdate + '</span>'
		         +'<span class="content" style="font-size: 12px;color:#58c5c7;">' + content + '</span>'
		         +'</li>'
		    $("#ul").append(str);
            //return currentdate;
        }
	
		//初始化同步
		function initiatingSync(){
			 $.messager.confirm('确认','初始化同步将清空目标数据库中数据并重新同步，耗时较长，请确认',function(row){
	                if(row){  
	                	$("#startSync").hide();
	        			$("#endSync").show();
	                    $.ajax({
							url: 'SynchronizeController',
							type: 'post',
							data: {
								action:"initSynchronize"
							},
							success:function(res){
								console.log(res);
							}
						}) 
	                }  
	            })
		}
		
		//检出表是否完全通过
		function watchAll(){
			$.ajax({
				url: 'TableConfigController',
				type: 'post',
				data: {
					action:"allTablePass"
				},
				dataType: 'text',
				success:function(res){
					dataState = res;
					//console.log(res);
					if(res == 0){
						$("#info").html("有表未检测通过，请重新检测");
						return;
					}else{
						//$("#sourceListInfo").datagrid("showColumn","_operate");
					}
				}
			})
		}
		
		 ///设置cookie
	    function setCookie(c_name, value, expire) {
	        var date = new Date()
	        date.setSeconds(date.getSeconds() + expire)
	        document.cookie = c_name + "=" + escape(value) + "; expires=" + date.toGMTString()
	        // console.log(document.cookie)
	    }

	    function getCookie(c_name) {
	        if (document.cookie.length > 0) {
	            let c_start = document.cookie.indexOf(c_name + "=")
	            if (c_start != -1) {
	                c_start = c_start + c_name.length + 1
	                var c_end = document.cookie.indexOf(";", c_start)
	                if (c_end == -1) c_end = document.cookie.length
	                return unescape(document.cookie.substring(c_start, c_end))
	            }
	        }
	        return ""
	    }


	    function delCookie(c_name) {
	        setCookie(c_name, "", -1)
	    }
		
		
	</script>
</body>
</html>

<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" deferredSyntaxAllowedAsLiteral="true"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>首页</title>
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.1/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.1/themes/icon.css"/>
<script type="text/javascript" src="jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.5.1/jquery.easyui.min.js"></script>


</head>
<body>
	<a id="btnStart"  href="javascript:void(0)" class="easyui-linkbutton"  onclick="startService()">123</a>
	<a id="btnEnd"  href="javascript:void(0)" class="easyui-linkbutton"  onclick="endService()">456</a>
	
	<script type="text/javascript">
		function startService(){
			$.ajax({
				type : "post",
				url : "SynchronizeController",
				data:{
					action:"start"
					},
				success : function(data) {
				}
			});  
		}
		function endService(){
			$.ajax({
				type : "post",
				url : "SynchronizeController",
				data:{
					action:"end"
					},
				success : function(data) {
				}
			});  
		}
	</script>
</body>
</html>
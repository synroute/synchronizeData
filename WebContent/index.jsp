<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" deferredSyntaxAllowedAsLiteral="true" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>登陆</title>
	<link rel="stylesheet" href="css/reset.css" />
	<link rel="stylesheet" href="css/login.css" />
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/login.js"></script>
</head>
<body>
<div class="page">
	<div class="loginwarrp">
		<div class="logo">管理员登陆</div>
        <div class="login_form">
			<form id="Login" name="Login" method="post" onsubmit="" action="">
				<li class="login-item">
					<span>用户名：</span>
					<input type="text" id="username" name="UserName" class="login_input" >
                    <span id="count-msg" class="error"></span>
				</li>
				<li class="login-item">
					<span>密　码：</span>
					<input type="password" id="password" name="password" class="login_input" >
                     <span id="password-msg" class="error"></span>
				</li>
				<li class="login-sub">
					<input type="submit" name="Submit"  onclick="login()" value="登录" />
                    <input type="reset" name="Reset" value="重置" />
				</li>   
           </form>
		</div>
	</div>
</div>
<script type="text/javascript">
		var state = "";
		window.onload = function() {
			var config = {
				vx : 4,
				vy : 4,
				height : 2,
				width : 2,
				count : 100,
				color : "121, 162, 185",
				stroke : "100, 200, 180",
				dist : 6000,
				e_dist : 20000,
				max_conn : 10
			}
			CanvasParticle(config);
		}
		
		function login(){
			state = 1;
		    window.event.returnValue=false;
	        var username = document.getElementById('username').value;
	        var password = document.getElementById('password').value;
	        // 用户名和密码都不为空
	        if(username&&password){
	    	    if(username==0000 && password==123){
	    	    		//console.log(111);
	    	    		window.location.href="aa.jsp";
	    	    		setCookie(username,password,300);
		           /*  var url = "http://127.0.0.1:8080/synchronizeData/aa.jsp";
		            location.href = url; //从地址栏获取返回地址，实现跳转   */
	    	    } else{
		      	    alert("用户名或密码错误");
		        }
	        }else{
	     	    alert("用户名和密码都不为空");
	        }

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
	<script type="text/javascript" src="js/canvas-particle.js"></script>
</body>
</html>
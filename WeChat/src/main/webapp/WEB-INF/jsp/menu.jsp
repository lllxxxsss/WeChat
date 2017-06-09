<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript"  src="<%=request.getContextPath() %>/js/jquery.js"></script>
</head>
<body>

<table>
	<c:forEach var="button" items="${list}">
		<tr>
			<td>${button.menuName }</td>
			<td><input type="button" onclick="getButtonMenuById(${button.id})" value="修改" /></td>
		</tr>
	</c:forEach>
</table>

<form action="" method="post">
<table>
	<tr>
		<td>名称</td><td><input type="text" id="menuName" name='menuName'  /> </td>
	</tr>
	<tr>
		<td>事件</td><td><input type="text" id="eventType" name='eventType'  /> </td>
	</tr>
	<tr>
		<td>menuKey</td><td><input type="text" id="menuKey" name='menuKey'  /> </td>
	</tr>
	<tr>
		<td>parentId</td><td><input type="text" id="parentId" name='parentId'  /> </td>
	</tr>			
</table>
</form>
<script type="text/javascript">
	function getButtonMenuById(id){
		$.ajax({
			url:"<%=request.getContextPath()%>/menu/getButtonMenuById",
			data:{id:id},
			type:"post",
			dataType:"text",
			success:function(obj){
				alert(obj);
// 				$("#menuName").val(obj.menuName);
// 				$("#eventType").val(obj.eventType);
// 				$("#menuKey").val(obj.menuKey);
// 				$("#parentId").val(obj.parentId);
				
			}
		})
		
	}
</script>
</body>
</html>
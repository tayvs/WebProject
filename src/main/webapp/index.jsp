<%--
  Created by IntelliJ IDEA.
  User: tayvs
  Date: 04.12.2016
  Time: 9:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>URL Page</title>
</head>
<body>

<form action="/servlet" method="post">
    <p>First URL:<input type="text" name="URL" value="http://m.vk.com"/></p>
    <p>Second URL:<input type="text" name="URL" value="http://google.com.ua"/></p>
    <p>Third URL:<input type="text" name="URL" value="http://mail.ru"/></p>
    <input type="submit" value="PROCESS"/>
</form>

<c:if test="!empty errors"><${errors}></c:if>

<c:if test="${!empty pairs}">
    <table>
        <tr style="text-align: center">
            <th>Tag</th>
            <th>Value</th>
        </tr>
        <c:forEach items="${pairs}" var="item">
            <tr>
                <td>&lt;${item.getTag()}&gt;</td>
                <td>${item.getValue()}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

</body>
</html>

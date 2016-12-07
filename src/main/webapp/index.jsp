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
    <p><input title="First URL:" type="text" name="URL" value="http://m.vk.com"/></p>
    <p><input title="Second URL:" type="text" name="URL" value="http://google.com.ua"/></p>
    <p><input title="Third URL:" type="text" name="URL" value="http://mail.ru"/></p>
    <input type="submit" name="action" value="PROCESS"/>
</form>

<c:if test="${!empty errors}"><p>${errors}</p></c:if>

<c:if test="${!empty pairs}">
    <c:forEach items="${pairs}" var="pair">
    <details>
        <summary>${pair.getKey()}</summary>
        <table style="text-align: center">
            <tr>
                <th>Tag</th>
                <th>Value</th>
            </tr>
            <c:forEach items="${pair.getValue()}" var="tagValue">
                <tr>
                    <td>&lt;${tagValue.getTag()}&gt;</td>
                    <td>${tagValue.getValue()}</td>
                </tr>
            </c:forEach>
        </table>
    </details>
    </c:forEach>
</c:if>

</body>
</html>

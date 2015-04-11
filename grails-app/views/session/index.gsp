<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/11/2015
  Time: 11:11 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="clientBase" />
    <title>
        <g:if test="${isSession==1}">
            ${session.getAttribute("firstName")} ${session.getAttribute("lastName")} - Reverie Online
        </g:if>
    </title>
</head>

<body>
</body>
</html>
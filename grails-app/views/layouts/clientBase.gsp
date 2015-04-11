<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 4/11/2015
  Time: 11:16 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:layoutTitle default="Reverie Online - Automated Task Scheduler" /></title>
    <g:layoutHead />
    <g:javascript library="application"/>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:require modules="bootstrap"/>
    <g:javascript plugin="twitter-bootstrap"/>
    <r:layoutResources />
</head>

<body>
<div class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <g:link controller="session" action="index" class="navbar-brand">Reverie Online</g:link>
        <button class = "navbar-toggle" data-toggle = "collapse" data-target = ".navCollapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <g:if test="${isSession==0}">
            <div class="collapse navbar-collapse navCollapse">
                <ul class="nav navbar-nav">
                    <li><g:link controller="session" action="index"><i class="glyphicon glyphicon-home"></i> Home</g:link></li>
                    <li><a href="#"><i class="glyphicon glyphicon-question-sign"></i> About</a></li>
                    <li><a href="#"><i class="glyphicon glyphicon-registration-mark"></i> Sign Up</a></li>
                </ul>
                <g:form controller="session" action="login" method="post" class="navbar-form navbar-right">
                    <g:field type="text" placeholder="Username" name="uNameField" required="required" class="form-control"/>
                    <g:field type="password" placeholder="Password" name="uPassField" required="required" class="form-control"/>
                    <g:submitButton name="login" value="Log In" class="btn btn-primary" />
                </g:form>
            </div>
        </g:if>
        <g:elseif test="${isSession==1}">
            <div class="collapse navbar-collapse navHeaderCollapse">
                <ul class="nav navbar-nav">
                    <li><g:link controller="session" action="index"><i class="glyphicon glyphicon-home"></i> Home</g:link></li>
                    <li><a href="#"><i class="glyphicon glyphicon-user"></i> Profile</a></li>
                    <li><a href="#"><i class="glyphicon glyphicon-plus"></i> New</a></li>
                </ul>
            </div>
        </g:elseif>
    </div>
</div>
<g:layoutBody />
<r:layoutResources />
</body>
</html>
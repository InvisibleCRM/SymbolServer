<%@page import="io.invisible.symbol.cleanup.controller.IndexController" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% IndexController.getInstance().doDispatch(request, response); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
        <title>Symbols Manager</title>
    </head>
    <body>
        <div class="container my-4">
            <h1 class="mb-3">Symbol server maintenance tool</h1>
            <c:if test="${not empty error}">
                <div class="alert alert-danger"><strong>Error:</strong> ${error}</div>
            </c:if>
            <h3>Project list</h3>
            <c:if test="${empty projects}">
                No projects to display
            </c:if>
            <c:forEach var="project" items="${projects}">
                <div><a href="project.jsp?name=${project}">${project}</a></div>
            </c:forEach>
            <h3 class="mt-3">Trash size</h3>

            <fmt:formatNumber var="trashSizeMB" value="${trashSize / 1048576}" maxFractionDigits="2"/>
            <fmt:formatNumber var="progress" value="${trashSize / trashQuota * 100}" maxFractionDigits="0"/>
            <c:choose>
                <c:when test="${progress > 100}"><c:set var="progressClass" value="bg-danger"/><c:set var="progress" value="${100}"/></c:when>
                <c:when test="${progress >= 90}"><c:set var="progressClass" value="bg-danger"/></c:when>
                <c:when test="${progress >= 50}"><c:set var="progressClass" value="bg-warning"/></c:when>
                <c:otherwise><c:set var="progressClass" value="bg-success"/></c:otherwise>
            </c:choose>
            <div class="progress" style="height: 20px;">
                <div class="progress-bar ${progressClass}" style="width: ${progress}%;">
                    <c:if test="${progress >= 5}">
                        <span>${trashSizeMB}&nbsp;MB</span>
                    </c:if>
                </div>
                <c:if test="${progress < 5}">
                    <span class="pl-1" style="margin-top: 1px; margin-bottom: 1px;">${trashSizeMB} MB</span>
                </c:if>
            </div>
        </div>
    </body>
</html>

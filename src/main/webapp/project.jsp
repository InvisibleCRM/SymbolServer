<%@page import="io.invisible.symbols.manager.controller.ProjectController" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% ProjectController.getInstance().doDispatch(request, response); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
        <title>Symbols Manager - ${param.name}</title>
        <script lang="javascript">
            function selectAll(source) {
                var buildsForm = document.getElementById("buildsForm");
                var inputs = buildsForm.getElementsByTagName("input");
                for (var i = 0; i < inputs.length; i++) {
                    var input = inputs[i];
                    if (input.name.indexOf("build_") === 0) {
                        input.checked = source.checked;
                    }
                }
            }
        </script>
    </head>
    <body>
        <div class="container my-4">
            <h1 class="mb-3">${param.name}</h1>
            <c:if test="${not empty error}">
                <div class="alert alert-danger"><strong>Error:</strong> ${error}</div>
            </c:if>
            <div class="mb-3"><a href="index.jsp">Back to project list</a></div>
            <h3>Build list</h3>
            <c:if test="${empty builds}">
                No builds to display
            </c:if>
            <c:if test="${not empty builds}">
                <form id="buildsForm" method="post">
                    <table class="table">
                        <thead class="thead-light">
                            <tr>
                                <c:if test="${isManager}">
                                    <th width="70px">
                                        <input id="selectAllCheckbox" type="checkbox" onclick="selectAll(this)">
                                        <label class="my-0" for="selectAllCheckbox">All</label>
                                    </th>
                                </c:if>
                                <th width="10%">ID</th>
                                <th width="10%">Date</th>
                                <th width="10%">Time</th>
                                <th width="10%">Version</th>
                                <th>Comment</th>
                                <c:choose>
                                    <c:when test="${not empty param.showSize}">
                                        <th width="10%">Size</th>
                                    </c:when>
                                    <c:otherwise>
                                        <th width="10%" class="py-0" style="vertical-align: middle;">
                                            <input class="btn btn-secondary" type="submit" name="showSize" value="Show size"/>
                                        </th>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="build" items="${builds}">
                                <tr>
                                    <c:if test="${isManager}">
                                        <td><input type="checkbox" name="build_${build.id}" value="${build.id}"></td>
                                    </c:if>
                                    <td>${build.id}</td>
                                    <td>${build.date}</td>
                                    <td>${build.time}</td>
                                    <td>${build.version}</td>
                                    <td>${build.comment}</td>
                                    <c:choose>
                                        <c:when test="${not empty build.size}">
                                            <fmt:formatNumber var="sizeMB" value="${build.size / 1048576}" maxFractionDigits="2"/>
                                            <td>${sizeMB}&nbsp;MB</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td/>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${isManager}">
                        <input class="btn btn-primary" type="submit" name="delete" value="Delete selected">
                    </c:if>
                </form>
            </c:if>
        </div>
    </body>
</html>

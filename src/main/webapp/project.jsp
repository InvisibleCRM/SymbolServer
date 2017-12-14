<%@page import="io.invisible.symbol.cleanup.controller.ProjectController" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% ProjectController.getInstance().doDispatch(request, response); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
        <title>Project info</title>
    </head>
    <body>
        <div class="container my-4">
            <h1>Project info</h1>
            <div class="mb-3"><a href="index.jsp">Back to list</a></div>
            <form method="post">
                <table class="table">
                    <thead class="thead-light">
                        <tr>
                            <th>Select</th>
                            <th>ID</th>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Name</th>
                            <th>Version</th>
                            <th>Comment</th>
                            <th>Size</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="build" items="${builds}">
                            <tr>
                                <td><input type="checkbox" name="build_${build.id}" value="${build.id}"></td>
                                <td>${build.id}</td>
                                <td>${build.date}</td>
                                <td>${build.time}</td>
                                <td>${build.name}</td>
                                <td>${build.version}</td>
                                <td>${build.comment}</td>
                                <fmt:formatNumber var="sizeMB" value="${build.size / 1048576}" maxFractionDigits="2"/>
                                <td>${sizeMB} MB</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <input type="hidden" name="dir" value="${param.dir}">
                <input class="btn btn-primary" type="submit" name="delete" value="Delete selected">
            </form>
        </div>
    </body>
</html>

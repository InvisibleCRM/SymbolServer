<%@page import="io.invisible.symbol.cleanup.controller.IndexController" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% IndexController.getInstance().doDispatch(request, response); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
        <title>List of builds</title>
    </head>
    <body>
        <div class="container my-4">
            <h1>List of builds</h1>
            <c:forEach var="project" items="${projects}">
                <div><a href="project.jsp?name=${project}">${project}</a></div>
            </c:forEach>
        </div>
    </body>
</html>

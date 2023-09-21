<%--Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2--%>

<%--This page is the landing page--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h3>Type in any author to see an API for the authors Quote and Information</h3>
        <form action="getAnInterestingQuote" method="GET">
            <label>Type the author.</label>
            <input type="text" name="searchWord" value="" /><br>
            <input type="submit" value="Get A Quote" />
        </form>
    </body>
</html>


<%--
  Created by IntelliJ IDEA.
  User: dhruvikhankhoje
  Date: 4/6/23
  Time: 11:28 AM
  To change this template use File | Settings | File Templates.
--%>

<%--Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2--%>


<%--This page displays the dashboard when requested on url "/getDashboard--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
<%--    Citation: https://www.w3schools.com/html/html_table_borders.asp--%>
    <style>


        table {
            border-collapse: collapse;
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
            border: 1px solid black;
        }
    </style>
</head>
<body>
<%--/*Displaying the Operation Analytics Dashboard*/--%>
<h1>Operation Analytics</h1>
<table>
    <tr>
        <th>Total Number Of Searches</th>
        <th>Maximum Length Of A Quote</th>
        <th>Most Searched Author</th>
        <th>Number Of Searches for Most Searched Author</th>
    </tr>
        <tr>
            <td><%= request.getAttribute("totalQuotes")%></td>
            <td><%= request.getAttribute("max")%></td>
            <td><%= request.getAttribute("maxString")%></td>
            <td><%= request.getAttribute("maxSearch")%> searches for <%= request.getAttribute("maxString")%></td>
        </tr>
</table>
<%--<h1>Operation Analytics</h1>--%>
<%--<p>The total number of searches are: <strong><%= request.getAttribute("totalQuotes")%></strong></p>--%>
<%--<p>The maximum length of a quote is: <strong><%= request.getAttribute("max")%></strong> characters</p>--%>
<%--<p>The most searched author is <strong><%= request.getAttribute("maxString")%></strong> with <strong><%= request.getAttribute("maxSearch")%> </strong> searches</p>--%>
<h1>Logs Dashboard</h1>
<%--/*Displaying the Logs Dashboard*/--%>
<table>
    <tr>
        <th>User Input</th>
        <th>Input Time</th>
        <th>Output Time</th>
        <th>Number of Quotes for an Author</th>
        <th>Quote Sent</th>
        <th>Length of Quote Sent</th>
    </tr>

<%--    Citation: https://stackoverflow.com/questions/16397207/iterate-arraylist-in-jsp--%>
    <c:forEach items="${logsInfo}" var="log" >
        <tr>
            <td>${log.getUserInput()}</td>
            <td>${log.getInputTime()}</td>
            <td>${log.getOutputTime()}</td>
            <td>${log.getNumberofQuotes()}</td>
            <td>${log.getQuoteSent()}</td>
            <td>${log.getLength()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

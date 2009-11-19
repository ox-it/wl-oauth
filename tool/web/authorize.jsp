<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
    String appDesc = (String)request.getAttribute("CONS_DESC");
    String token = (String)request.getAttribute("TOKEN");
    String userName = (String)request.getAttribute("USER_NAME");
    String userId = (String)request.getAttribute("USER_ID");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your Friendly OAuth Provider</title>
    </head>
    <body>
        <jsp:include page="banner.jsp"/>
        
    <h3>"<%=appDesc%>" is trying to access your information.</h3>
    
    You are currently logged in as  <%= userName %> (<%= userId %>)<br/>
    <form name="authZForm" action="authorize" method="POST">
        <input type="hidden" name="oauth_token" value="<%= token %>"/>
        <input type="submit" name="authorize" value="Authorize"/>
        <input type="submit" name="deny" value="Deny"/>
    </form>
    
    </body>
</html>

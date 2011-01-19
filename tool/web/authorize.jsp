<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
	String appDesc = (String) request.getAttribute("CONS_DESC");
	String appName = (String) request.getAttribute("CONS_NAME");
	String servName = (String) request.getAttribute("SERV_NAME");
	String uiName = (String) request.getAttribute("UI_NAME");
	String token = (String) request.getAttribute("TOKEN");
	String userName = (String) request.getAttribute("USER_NAME");
	String userId = (String) request.getAttribute("USER_ID");
	String skinPath = (String) request.getAttribute("SKIN_PATH");
	String defaultSkin = (String) request.getAttribute("DEFAULT_SKIN");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%=skinPath%>/tool_base.css" type="text/css" rel="stylesheet" media="all" /> 
        <link href="<%=skinPath%>/<%=defaultSkin%>/tool.css" type="text/css" rel="stylesheet" media="all" />
        <link href="<%=skinPath%>/<%=defaultSkin%>/mobile.css" type="text/css" rel="stylesheet" media="handheld" />
        <link href="<%=skinPath%>/<%=defaultSkin%>/mobile.css" type="text/css" rel="stylesheet" media="only screen and (max-device-width: 420px)" /> 
        <script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
        <title><%=uiName%> : OAuth Provider</title>
        <meta name="viewport" content="width=device-width"/>  
        <style type="text/css">
        #wrapper {
            /* Max width of 420px and centre */
            margin: auto;
            max-width: 420px;
            width:expression(document.body.clientWidth > 420? "420px": "auto");
        }
        </style>
    </head>
    <body>
      <div id="wrapper">
        <table class="login" cellpadding="3px" cellspacing="0" border="0" summary="layout"> 
        <tr>
            <th><%=uiName%> : Authorisation Required</th> 
        </tr> 
        <tr>
            <td class="logo" align="center"></td>
        </tr>
        <tr>
        <td align="center">
    <h3>"<%=appName%>" would like to access your account.</h3>
    <div class="instruction" style="text-align: center"> 
    <p><%=appDesc%></p>
    <p>
    You are currently logged in as  <%=userName%> (<%=userId%>)
    </p>
    <form name="authZForm" action="authorize" method="POST">
        <input type="hidden" name="oauth_token" value="<%=token%>"/>
        <input type="submit" name="authorize" value="Authorize"/>
        <input type="submit" name="deny" value="Deny"/>
    </form>
    </div>
    </td>
    </tr>
    </table>
    </div>
    </body>
</html>

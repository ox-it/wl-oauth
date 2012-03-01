<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${requestScope.skinPath}/tool_base.css" type="text/css" rel="stylesheet" media="all"/>
    <link href="${requestScope.skinPath}/${requestScope.defaultSkin}/tool.css" type="text/css" rel="stylesheet"
          media="all"/>
    <link href="${requestScope.skinPath}/${requestScope.defaultSkin}/mobile.css" type="text/css" rel="stylesheet"
          media="handheld"/>
    <link href="${requestScope.skinPath}/${requestScope.defaultSkin}/mobile.css" type="text/css" rel="stylesheet"
          media="only screen and (max-device-width: 420px)"/>
    <script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
    <title>${requestScope.uiName} : OAuth Provider</title>
    <meta name="viewport" content="width=device-width"/>
    <style type="text/css">
        #wrapper {
            /* Max width of 420px and centre */
            margin: auto;
            max-width: 420px;
            width: expression(document.body.clientWidth > 420? "420px": "auto");
        }
    </style>
</head>
<body>
<div id="wrapper">
    <table class="login" cellpadding="3px" cellspacing="0" border="0" summary="layout">
        <tr>
            <th>${requestScope.uiName} : Authorisation Required</th>
        </tr>
        <tr>
            <td class="logo" align="center"></td>
        </tr>
        <tr>
            <td align="center">
                <h3>"${requestScope.appName}" would like to access your account.</h3>

                <div class="instruction" style="text-align: center">
                    <p>${requestScope.appDesc}</p>

                    <p>You are currently logged in as  ${requestScope.userName} (${requestScope.userId})</p>

                    <form name="authZForm" action="authorise" method="post">
                        <input type="hidden" name="oauthToken" value="${requestScope.token}"/>
                        <input type="hidden" name="oauthVerifier" value="${requestScope.oauthVerifier}"/>
                        <input type="submit" name="${requestScope.authorise}" value="${requestScope.authorise}"/>
                        <input type="submit" name="${requestScope.deny}" value="${requestScope.deny}"/>
                    </form>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>

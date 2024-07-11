<%@ page
    import="com.learnosity.quickstart.AuthoraideApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private AuthoraideApp app;
    public void jspInit() {
        app = new AuthoraideApp();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example - Authoraide API</h1>

        <!-- Authoraide API will be rendered the app into this div. -->
        <div id="aiApp"></div>

        <!-- Load the Authoraide API library. -->
        <script src="https://authoraide.learnosity.com"></script>

        <script>
            var authoraideApp = LearnosityAuthorAide.init(
                <%= app.initOptions(request.getServerName()) %>,
                "#aiApp"
            );
        </script>
    </body>
</html>

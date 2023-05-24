<%@ page
    import="com.learnosity.quickstart.App"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private App app;
    public void jspInit() {
        app = new App();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="../css/style.css"></head>
    <body>
        <h1>Standalone Authoring Example</h1>

        <!-- Author API will render the authoring app into this div. -->
        <div id="learnosity-author"></div>

        <!-- Load the Author API library. -->
        <script src="https://authorapi.learnosity.com/?v2023.1.LTS"></script>

        <!-- Initiate Author API interface rendering, using the JSON blob of signed params. -->
        <script>
            var authorApp = LearnosityAuthor.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>
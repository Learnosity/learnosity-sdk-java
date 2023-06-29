<%@ page
    import="com.learnosity.quickstart.AuthorApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private AuthorApp app;
    public void jspInit() {
        app = new AuthorApp();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example - Author API</h1>

        <!-- Items API will render the assessment app into this div. -->
        <div id="learnosity-author"></div>

        <!-- Load the Author API library. -->
        <script src="https://authorapi.learnosity.com/?latest-lts"></script>

        <!-- Initiate Author API assessment rendering, using the JSON blob of signed params. -->
        <script>
            var authorApp = LearnosityAuthor.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>

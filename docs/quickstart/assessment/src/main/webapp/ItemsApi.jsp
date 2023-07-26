<%@ page
    import="com.learnosity.quickstart.ItemsApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private ItemsApp app;
    public void jspInit() {
        app = new ItemsApp();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example - Items API</h1>

        <!-- Items API will render the assessment app into this div. -->
        <div id="learnosity_assess"></div>

        <!-- Load the Items API library. -->
        <script src="https://items.learnosity.com/?latest-lts"></script>

        <!-- Initiate Items API assessment rendering, using the JSON blob of signed params. -->
        <script>
            var itemsApp = LearnosityItems.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>

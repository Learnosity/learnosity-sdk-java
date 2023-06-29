<%@ page
    import="com.learnosity.quickstart.ReportsApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private ReportsApp app;
    public void jspInit() {
        app = new ReportsApp();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example - Reports API</h1>

        <!-- Reports API will render the author app into this span. -->
        <span class="learnosity-report" id="session-detail"></span>

        <!-- Load the Reports API library. -->
        <script src="https://reports.learnosity.com/?latest-lts"></script>

        <!-- Initiate Questions API assessment rendering, using the JSON blob of signed params. -->
        <script>
            var reportsApp = LearnosityReports.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>

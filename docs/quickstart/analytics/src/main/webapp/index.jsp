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
        <h1>Standalone Analytics Example</h1>
        <p>
            This example demonstrates our session-detail-by-item report type, just one of 
            <a href="https://reference.learnosity.com/reports-api/reporttypes"> 
                many different report types requestable via Reports API.
            </a>
        </p>

        <!-- Here, we make sure to specify a div with the same id we specified for the desired report type(s) in the request object -->
        <div id="session-detail-quickstart-report"></div>


        <!-- Load the Reports API library. -->
        <script src="https://reports.learnosity.com/?v2023.1.LTS"></script>

        <!-- Initiate Reports API instance using the JSON blob of signed params. -->
        <script>
            var reportsApp = LearnosityReports.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>
<%@ page
    import="com.learnosity.quickstart.QuestionsApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private QuestionsApp app;
    public void jspInit() {
        app = new QuestionsApp();
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head><link rel="stylesheet" type="text/css" href="css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example - Question API</h1>

        <!-- Questions API will render the Question app into this span. -->
        <span class="learnosity-response question-60001"></span>

        <!-- Load the Questions API library. -->
        <script src="https://questions.learnosity.com/?latest-lts"></script>

        <!-- Initiate Questions API assessment rendering, using the JSON blob of signed params. -->
        <script>
            var questionsApp = LearnosityApp.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>

<%@ page
    import="com.learnosity.quickstart.DataApiApp"
    language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
    private DataApiApp app;
    public void jspInit() {
        app = new DataApiApp();
        try {
            // Make initial requests so data is available on first page load
            app.getItemsData();
            app.getQuestionsData();
        } catch (Exception e) {
            // Silently fail - data will be empty on first load
        }
    }
    public void jspDestroy() {
        app = null;
    }
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <style>
            .metadata-section {
                background-color: #f0f0f0;
                border: 1px solid #ccc;
                padding: 15px;
                margin: 15px 0;
                border-radius: 5px;
            }
            .response-section {
                background-color: #f9f9f9;
                border: 1px solid #ddd;
                padding: 15px;
                margin: 15px 0;
                border-radius: 5px;
                max-height: 400px;
                overflow-y: auto;
            }
            .response-section p {
                font-size: 13px;
                margin: 5px 0;
            }
            .response-section strong {
                font-size: 13px;
            }
            pre {
                background-color: #f5f5f5;
                padding: 10px;
                border-radius: 3px;
                overflow-x: auto;
                font-size: 12px;
                font-family: 'Courier New', monospace;
                line-height: 1.4;
                margin: 10px 0;
                color: #333;
            }
            pre * {
                font-size: 12px !important;
                font-family: 'Courier New', monospace !important;
            }
            .info-box {
                background-color: #e3f2fd;
                border-left: 4px solid #2196F3;
                padding: 10px;
                margin: 10px 0;
            }
            .request-info-header {
                background-color: #2196F3;
                color: white;
                padding: 15px;
                margin: 15px 0 0 0;
                font-size: 18px;
                font-weight: bold;
            }
            .request-info-table {
                width: 100%;
                border-collapse: collapse;
                background-color: #f9f9f9;
                margin: 0 0 15px 0;
            }
            .request-info-table td {
                padding: 12px 15px;
                border-bottom: 1px solid #ddd;
            }
            .request-info-table td:first-child {
                background-color: #f0f0f0;
                font-weight: bold;
                width: 30%;
            }
            .metadata-header {
                background-color: #4CAF50;
                color: white;
                padding: 15px;
                margin: 15px 0 0 0;
                font-size: 18px;
                font-weight: bold;
            }
            .metadata-table {
                width: 100%;
                border-collapse: collapse;
                background-color: #f9f9f9;
                margin: 0 0 15px 0;
            }
            .metadata-table td {
                padding: 12px 15px;
                border-bottom: 1px solid #ddd;
            }
            .metadata-table td:first-child {
                background-color: #f0f0f0;
                font-weight: bold;
                width: 30%;
            }
            .status-code {
                display: inline-block;
                background-color: #4CAF50;
                color: white;
                padding: 4px 8px;
                border-radius: 3px;
                font-weight: bold;
            }
            .note-box {
                background-color: #b3e5fc;
                border-left: 4px solid #0288d1;
                padding: 10px;
                margin: 10px 0;
                font-size: 13px;
            }
        </style>
    </head>
    <body>
        <h1>Data API Example - With Metadata Headers</h1>
        
        <div class="info-box">
            <strong>Note:</strong> This example demonstrates the Data API with automatic metadata headers.
            Every request includes consumer, action, and SDK language:version information.
        </div>

        <h2>API Responses</h2>

        <div class="request-info-header">Request Information</div>
        <table class="request-info-table">
            <tr>
                <td>Endpoint</td>
                <td><span style="color: #e91e63;"><%= app.getLastEndpoint() %></span></td>
            </tr>
            <tr>
                <td>Action</td>
                <td><span style="color: #e91e63;"><%= app.getLastAction() %></span></td>
            </tr>
            <tr>
                <td>Status Code</td>
                <td><span class="status-code"><%= app.getLastStatusCode() %></span></td>
            </tr>
        </table>

        <div class="metadata-header">Metadata Headers (Sent Automatically)</div>
        <p style="color: #666; margin: 10px 0; padding: 0 15px;">These headers are added automatically by the SDK and are invisible to customers:</p>
        <table class="metadata-table">
            <tr>
                <td><strong>Header Name</strong></td>
                <td><strong>Header Value</strong></td>
            </tr>
            <%= app.getMetadataHeadersTable() %>
        </table>
        <div class="note-box">
            <strong>Note:</strong> The action metadata format is <span style="color: #e91e63;">{method}_{endpoint_path}</span>. For this request, it's <span style="color: #e91e63;"><%= app.getLastAction() %></span>.
        </div>

        <h3>Items API Response</h3>
        <div class="response-section">
            <p><strong>Endpoint:</strong> GET /itembank/items</p>
            <p><strong>Action Header:</strong> get_/itembank/items</p>
            <pre><%= app.getItemsData() %></pre>
        </div>

        <h3>Questions API Response</h3>
        <div class="response-section">
            <p><strong>Endpoint:</strong> GET /itembank/questions</p>
            <p><strong>Action Header:</strong> get_/itembank/questions</p>
            <pre><%= app.getQuestionsData() %></pre>
        </div>

        <h2>How It Works</h2>
        <div class="info-box">
            <p><strong>Automatic Metadata Headers:</strong></p>
            <ul>
                <li><code>X-Learnosity-Consumer</code>: Your consumer key (account identifier)</li>
                <li><code>X-Learnosity-Action</code>: The action being performed (e.g., get_/itembank/items)</li>
                <li><code>X-Learnosity-SDK</code>: SDK language and version (e.g., Java:0.17.0)</li>
            </ul>
            <p>These headers are sent with every Data API request and are used by Learnosity infrastructure for routing and monitoring.</p>
        </div>
    </body>
</html>


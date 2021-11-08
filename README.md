<p align="center"><img width="50%" height="50%" src="docs/images/image-logo-graphic.png"></p>
<h1 align="center">Learnosity SDK - Java</h1>
<p align="center">Everything you need to start building your app in Learnosity, with the Python programming language.<br> 
(Prefer another language? <a href="https://help.learnosity.com/hc/en-us/sections/360000194318-Server-side-development-SDKs">Click here</a>)<br>
An official Learnosity open-source project.</p>

[![Latest Stable Version](https://badge.fury.io/gh/Learnosity%2Flearnosity-sdk-java.svg)](https://github.com/Learnosity/learnosity-sdk-java/releases)
[![Build Status](https://travis-ci.org/Learnosity/learnosity-sdk-java.svg?branch=master)](https://app.travis-ci.com/github/Learnosity/learnosity-sdk-java)
[![License](docs/images/apache-license.svg)](LICENSE.md)
[![Downloads](docs/images/downloads.svg)](https://github.com/Learnosity/learnosity-sdk-java/releases)
---

## Table of Contents

* [Overview: what does it do?](#overview-what-does-it-do)
* [Requirements](#requirements)
* [Installation](#installation)
* [Quick start guide](#quick-start-guide)
* [Next steps: additional documentation](#next-steps-additional-documentation)
* [Contributing to this project](#contributing-to-this-project)
* [License](#license)
* [Usage tracking](#usage-tracking)
* [Further reading](#further-reading)

## Overview: what does it do?
The Learnosity Java SDK makes it simple to interact with Learnosity APIs.

![image-concept-overview.png](docs/images/image-concept-overview.png)

It provides a number of convenience features for developers, that make it simple to do the following essential tasks:
* Creating signed security requests for API initialization, and
* Interacting with the Data API.

For example, the SDK helps with creating a signed request for Learnosity:

![image-signed-request-creation.png](docs/images/image-signed-request-creation.png)

Once the SDK has created the signed request for you, your app sends that on to an API in the Learnosity cloud, which then retrieves the assessment you are asking for, as seen in the diagram below:

![image-assessment-retrieval.png](docs/images/image-assessment-retrieval.png)

This scenario is what you can see running in the Quick start guide example ([see below](#quick-start-guide)).

There's more features, besides. See the detailed list of SDK features on the [reference page](REFERENCE.md).

[(Back to top)](#table-of-contents)


NEW /\
OLD \/


# Learnosity SDK - Java

[![Build Status](https://travis-ci.com/Learnosity/learnosity-sdk-java.svg?branch=master)](https://travis-ci.com/Learnosity/learnosity-sdk-java)

Include this library into your own codebase to ease integration with any of the Learnosity APIs.

It supports current [Oracle LTS
JDKs](http://www.oracle.com/technetwork/java/javase/downloads/eol-135779.html),
as well as [OpenJDK](http://openjdk.java.net/) with matching versions.

| Version			| End of Life   |
|-------------------------------|:-------------:|
| Oracle JDK8 / OpenJDK 1.8	| 2019-01	|
| Oracle JDK11			| TBA		|

Earlier versions may still keep working. Please refer to our [build
matrix](https://travis-ci.org/Learnosity/learnosity-sdk-java) to verify (see the
`Allowed Failures` section). However, please plan to upgrade to a supported JDK
at the earliest opportunity.

## Installation
Installation can be done in three ways:

1.) Download the jar in directory learnosity-sdk-java/Dist/All/ and add to your build path. All the required libraries are included in this jar.

2.) Download the jar in learnosity-sdk-java/Dist/Light/ and add to your build path. This requires the external libraries which can be found in learnosity-sdk-java/Dist/Light/RequiredJars

3.) Download the source code and integrate into your project. This requires the external libraries which can be found in learnosity-sdk-java/Dist/Light/RequiredJars

4.) To use as a dependency in a maven project see below 'Using with a file based repository'

## Test

In order to check that you've added the required code correctly, you can download src/LearnositySdk/Test and try to run the test file. If it runs successfully, all should be fine.

The Test.java class also gives some examples on how to use the sdk.

## Usage

### Init

The Init class is used to create the necessary *security* and *request* details used to integrate with a Learnosity API. Most often this will be a JavaScript object.

The Init constructor takes either 3 arguments:

 * String  service type
 * Object  security details (**no secret**)
 * String  secret

or 4 arguments:

 * String  service type
 * Object  security details (**no secret**)
 * String  secret
 * Object  request details

The security and request Object can be of any type that can be parsed into a org.json.JSONObject. Examples are org.json.JSONOBject, a valid JSON String or a map. (See Test.java for examples). Learnosity recommends that you pass the request details as a JSONObject, or a String generated from a JSONObject to the constructor. This way, issues arising from adding/removing whitespace can be avoided.

If you have to set an action attribute (for calls to data api, if not using the DatApi class), you can use the setAction method of class Init.

```java
import learnositysdk.request.Init;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Questions {
    public static void main (String[] args) throws Exception
    {
        try {
            Init init;
            String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";

            // Create a security Map
            Map securityMap = new HashMap();
            securityMap.put("consumer_key", "yis0TYCu7U9V4o7M");
            securityMap.put("user_id", "$ANONYMIZED_USER_ID");
            securityMap.put("domain", "localhost");

            // Create some data for the questions api
            JSONObject req = new JSONObject();
            req.put("type", "local_practice");
            req.put("state", "initial");

            // Create a questions JSONArrray
            JSONArray questions = new JSONArray();

            // Create a question
            JSONObject question = new JSONObject();
            question.put("response_id", "60005");
            question.put("type", "association");
            question.put("stimulus", "Match the cities to the parent nation");

            // Add stimulus list
            JSONArray stimList = new JSONArray();
            stimList.put(0, "London");
            stimList.put(1, "Dublin");
            stimList.put(2, "Paris");
            stimList.put(3, "Sydney");
            question.put("stimulus_list", stimList);

            // Add possible responses
            JSONArray possibleResp = new JSONArray();
            possibleResp.put(0, "Australia");
            possibleResp.put(1, "France");
            possibleResp.put(2, "Ireland");
            possibleResp.put(3, "England");
            question.put("possible_responses", possibleResp);

            // Add validation
            JSONObject validation = new JSONObject();
            JSONArray validResp = new JSONArray();
            validResp.put(0, "England");
            validResp.put(1, "Ireland");
            validResp.put(2, "France");
            validResp.put(3, "Australia");
            validation.put("valid_responses", validResp);
            question.put("validation", validation);

            // Add questions to questions array
            questions.put(0, question);

            // Finally add questions to request
            req.put("questions", questions);


            // Instantiate the SDK Init class with your security and request data:
            init = new Init("questions", securityMap, consumerSecret, req);

            // Call the generate() method to retrieve a JavaScript object
            String questionJson = init.generate();

            // Pass the object to the initialisation of any Learnosity API
            // For instance in your jsp file you can have:
            // <script src="//questions.learnosity.com"></script>
            // var questionsApp = LearnosityApp.init(questionJson);

            System.out.println(questionJson);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

Raw JSON initialisation snippet:

```java
import learnositysdk.request.Init;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Questions {
    public static void main (String[] args) throws Exception
    {
        try {
            Init init;
            String consumerSecret = "74c5fd430cf1242a527f6223aebd42d30464be22";

            String secString = "{\"consumer_key\":\"yis0TYCu7U9V4o7M\","
                    +    "\"domain\": \"assess.vg.learnosity.com\","
                    +    "\"user_id\": \"$ANONYMIZED_USER_ID\"}";

            String reqString = "{\"state\":\"initial\","
                    +  "\"type\":\"local_practice\","
                    +  "\"response_id\":\"60005\","
                    +  "\"questions\":"
                    +   "[{\"stimulus_list\":"
                    +     "[\"London\","
                    +        "\"Dublin\","
                    +        "\"Paris\","
                    +      "\"Sydney\"],"
                    + "\"stimulus\":\"Match the cities to the parent nation\","
                    +   "\"type\":\"association\","
                    +   "\"possible_responses\":"
                    +   "[\"Australia\","
                    +     "\"France\","
                    +     "\"Ireland\","
                    +       "\"England\"],"
                    +   "\"validation\":"
                    +   "{\"valid_responses\":"
                    +       "[\"England\","
                    +             "\"Ireland\","
                    +       "\"France\","
                    +       "\"Australia\"]}}]}";
            init = new Init("questions", secString, consumerSecret, new JSONObject(reqString));
            String questionJson = init.generate();
            System.out.println(questionJson);

            // Now you can pass questionJson to the initialisation of Questions API
            // For instance on jsp you might have
            // <script src="//questions.learnosity.com"></script>
            // var questionsApp = LearnosityApp.init(questionJson);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

#### Arguments
**service**<br>
A string representing the Learnosity service (API) you want to integrate with. Valid options are:

* assess
* author
* data
* items
* questions
* reports

**security**<br>
An Object that includes your *consumer_key* but does not include your *secret*. The SDK sets defaults for you, but valid options are:

* consumer_key
* domain (optional)
* timestamp (optional)
* user_id (optional)

^Note – the SDK accepts all Objects which can be parsed into a org.json.JSONObject (e.g. a map, a JSONString, JSONOBject or a Java bean).

**secret**<br>
Your secret key, as provided by Learnosity.

**request**<br>
An optional associative Object of data relevant to the API being used. This will be any data minus the security details that you would normally use to initialise an API.

^Note – the SDK accepts all Objects which can be parsed into a org.json.JSONObject (e.g. a map, a JSONString, JSONOBject or a Java bean).


**action**<br>
An optional string used only if integrating with the Data API. Valid options are:

* get
* set
* update
* delete

<hr>

### Remote

The Remote class is used to make server side, cross domain requests. Think of it as a cURL wrapper.

You'll call either get() or post() with the following arguments:

* [String] URL
* [Map<String,Object>]  Data payload

```java
// Instantiate the SDK Remote class:
Remote remote = new Remote();

// Instantiate the SDK Remote class with a RequestConfig - https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/config/RequestConfig.html:
RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).build();

Remote remote = new Remote(requestConfig);
// Call get() or post() with a URL:
remote.get("http://schemas.learnosity.com/stable/questions/templates");

// getBody() gives you to body of the request
String body = response.getBody();
```

#### Arguments

**URL**<br>
A string URL, including schema and path. Eg:

```
https://schemas.learnosity.com/stable/questions/templates
```

**Data**<br>
An optional array of data to be sent as a payload. For GET it will be a URL encoded query string.

### Remote methods
The following methods are available after making a `get()` or `post()`.

**getBody()**<br>
Returns the body of the response payload.

**getHeader()**<br>
Currently only returns the *content_type* header of the response.

**getStatusCode()**<br>
Returns the HTTP status code of the response.

<hr>

### DataApi

This is a helper class for use with the Data API. It creates the initialisation packet and sends a request to the Data API, returning a JSONObject with the response data. There is also a requestRecursive function which can be called with a class implementing RequestCallback.java. The execute() function will be called for each response.

The DataApi Constructor can handle either 3 arguments;
* url
* securityPacket
* secret

or 4 arguments:
* url
* securityPacket
* secret
* action

or 5 arguments:
* url
* securityPacket
* secret
* requestPacket
* action

```java
Map<String,String> sec = new HashMap<String, String>();
sec.put("consumer_key", "yis0TYCu7U9V4o7M");
sec.put("domain","localhost");

reqData = new HashMap<String,String>();
reqData.put("limit", "10");

DataApi dataApi = new DataApi("https://data.learnosity.com/latest/itembank/items", sec, consumerSecret, reqData, "get");

// you can also configure the Data API with a requestConfig as above
RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).build();

dataApi.setRequestConfig(requestConfig);


JSONObject response = dataApi.requestJSONObject();
JSONObject res = new JSONObject(response.getString("body"));

// You can also get a Remote object like this:
Remote remote = dataApi.request();
String body = remote.getBody();
```

## Troubleshooting

We recommend that you use the Java JSON implementation which comes packaged in the sdk. In the past, we had several issues which were due to incompatible JSON implementations.


## Creating jar files (using Eclipse Luna Release 4.4.0)

The 'light' jar:

In eclipse right click the java sdk project and select 'Export'. Under 'Java', select 'JAR file'. In the following screen only select the packages in the src (excluding the library folder) and click 'Finish'.

The 'all' jar:

In eclipse right click the java sdk project and select 'Export'. Under 'Java', select 'Runnable JAR file', click 'Next' and then 'Finish'. There are warnings that some libararies are included. This can be disregarded as the libraries are publicly available.

## Building with Maven

Run ```mvn package``` in your root directory (where pom.xml is located). This will put the light and all jar in the correct folders.

## Using with a maven file based repository

1.) Download the 'light' jar and the learnosity project pom file (pom.xml) into a temporary directory (say /home/temp)

2.) Add a directory to your maven project to build the file-based repository into (e.g. ./learnosity-sdk-repo)

3.) Install the learnosity SDK with maven into that directory by running the following (modify locations as appropriate):
```bash
mvn deploy:deploy-file -DgroupId=learnositysdk -DartifactId=learnositysdk -Dversion=0.16.3 -Durl=file:./learnosity-sdk-repo/ -DrepositoryId=learnosity-sdk-repo -DupdateReleaseInfo=true -Dfile=/home/temp/learnositysdk-0.16.3.jar -DpomFile=/home/temp/pom.xml
```

4.) Add the directory as a file based repository in your project pom.xml:
```xml
<repositories>
    <repository>
        <id>learnosity-sdk-repo</id>
        <url>file://${project.basedir}/learnosity-sdk-repo</url>
    </repository>
</repositories>
```

5.) Don't forget to commit your file-based repository

## Tracking
In version v0.16.0 we introduced code to track the following information by adding it to the request being signed:
- SDK version
- SDK language
- SDK language version
- Host platform (OS)
- Platform version

We use this data to enable better support and feature planning. All subsequent versions of the SDK shall include this usage tracking.




\/ PYTHON TEMPLATE



## Requirements
1. Runtime libraries for Python 3 installed. ([instructions](https://www.python.org/downloads/))

2. The [Pip](https://pip.pypa.io/en/latest/) package manager installed. You use Pip to access the Learnosity Python SDK on [Pypi](https://pypi.org/) (the [Python Package Index](https://pypi.org/)).

3. The [Jinja](https://jinja.palletsprojects.com/)** templating library. For the tutorial on this page, you will also need [Jinja](https://jinja.palletsprojects.com/) installed. Jinja helps in rendering HTML templates, and importing Python variables into web pages. It's not actually a requirement of the SDK itself, so if your app doesn't use Jinja, no need to install it. **  Jinja is only required for the tutorial on this page.

Not using Python? See the [SDKs for other languages](https://help.learnosity.com/hc/en-us/sections/360000194318-Server-side-development-SDKs).

### Supported Python Versions
The following Python versions are tested and supported:

* 3.6.x
* 3.5.x
* 3.4.x
* 3.3.x

[(Back to top)](#table-of-contents)

## Installation
###  **Installation via Pip**
Using Pip is the recommended way to install the Learnosity SDK for Python in production. The easiest way is to run this from your parent project folder:

    pip install learnosity_sdk

Then, if you're following the tutorial on this page, also run:

    pip install Jinja2

*Note*:  Jinja is only required for the tutorial on this page.

### **Alternative method 1: download the zip file**
Download the latest version of the SDK as a self-contained ZIP file from the [GitHub Releases](https://github.com/Learnosity/learnosity-sdk-python/releases) page. The distribution ZIP file contains all the necessary dependencies. 

Note: after installation, run this command in the SDK root folder:

    pip install .

Then, if you're following the tutorial on this page, also run:

    pip install Jinja2

*Note*:  Jinja is only required for the tutorial on this page.

### **Alternative 2: development install from a git clone**
To install from the terminal, run this command:

    git clone git@github.com:Learnosity/learnosity-sdk-python.git

Note: after installation, run this command in the SDK root folder:

    pip install .

Then, if you're following the tutorial on this page, also run:

    pip install Jinja2

*Note*:  Jinja is only required for the tutorial on this page.

Note that these manual installation methods are for development and testing only.
For production use, you should install the SDK using the Pip package manager for Python, as described above.

[(Back to top)](#table-of-contents)

## Quick start guide
Let's take a look at a simple example of the SDK in action. In this example, we'll load an assessment into the browser.

### **Start up your web server and view the standalone assessment example**
To start up your Python web server, first find the following folder location under the SDK. Change directory ('cd') to this location on the command line.

If installed under Pypi, and your Python version is 3.9 (for example), you should navigate to this location:

    .../usr/local/lib/python3.9/site-packages/learnosity_sdk/docs/quickstart/assessment/

If downloaded via another method, navigate to this location:

    .../learnosity-sdk-python/docs/quickstart/assessment/

To start, run this command from that folder:

    python3 standalone-assessment.py

From this point on, we'll assume that your web server is available at this local address (it will report the port being used when you launch it, by default it's port 8000): 

http://localhost:8000/

The page will load. This is a basic example of an assessment loaded into a web page with Learnosity's assessment player. You can interact with this demo assessment to try out the various Question types.

<img width="50%" height="50%" src="docs/images/image-quickstart-examples-assessment.png">

[(Back to top)](#table-of-contents)

### **How it works**
Let's walk through the code for this standalone assessment example. The source file is included under the quickstart folder, in this location:

    /learnosity-sdk-python/docs/quickstart/assessment/standalone-assessment.py

The first section of code is Python and is executed server-side. It constructs a set of configuration options for Items API, and securely signs them using the consumer key. The second section is HTML and JavaScript and is executed client-side, once the page is loaded in the browser. It renders and runs the assessment functionality.

[(Back to top)](#table-of-contents)

### **Server-side code**
We start by including some LearnositySDK helpers - they'll make it easy to generate and sign the config options, and unique user and session IDs.

``` python
from learnosity_sdk.request import Init # Learnosity helper.
from learnosity_sdk.utils import Uuid   # Learnosity helper.
```

We also specify a few libraries to run a minimal web server, for the purposes of this example.

``` python
from http.server import BaseHTTPRequestHandler, HTTPServer # Python web server.
import time                             # Time library, for the Python web server.
from jinja2 import Template             # Jinja template library - pulls data into web pages.
```

Now we'll declare the configuration options for Items API. These specify which assessment content should be rendered, how it should be displayed, which user is taking this assessment and how their responses should be stored. 

``` python
items_request = items_request = {
    "user_id": user_id,
    "activity_template_id": "quickstart_examples_activity_template_001",
    "session_id": session_id,
    "activity_id": "quickstart_examples_activity_001",
    "rendering_type": "assess",
    "type": "submit_practice",
    "name": "Items API Quickstart",
    "state": "initial"
}
```

* `user_id`: unique student identifier. Note: we never send or save student's names or other personally identifiable information in these requests. The unique identifier should be used to look up the entry in a database of students accessible within your system only. [Learn more](https://help.learnosity.com/hc/en-us/articles/360002309578-Student-Privacy-and-Personally-Identifiable-Information-PII-).
* `activity_template_id`: reference of the Activity to retrieve from the Item bank. The Activity defines which Items will be served in this assessment.
* `session_id`: uniquely identifies this specific assessment attempt for save/resume, data retrieval and reporting purposes. Here, we're using the `Uuid` helper to auto-generate a unique session id.
* `activity_id`: a string you define, used solely for analytics to allow you run reporting and compare results of users submitting the same assessment.
* `rendering_type`: selects a rendering mode, `assess` mode is a "standalone" mode (loading a complete assessment player for navigation, as opposed to `inline` for embedding without).
* `type`: selects the context for the student response storage. `submit_practice` mode means the student responses will be stored in the Learnosity cloud, allowing for grading and review.
* `name`: human-friendly display name to be shown in reporting, via Reports API and Data API.
* `state`: Optional. Can be set to `initial`, `resume` or `review`. `initial` is the default.

**Note**: you can submit the configuration options either as a Python array as shown above, or a JSON string.

Next, we declare the Learnosity consumer credentials we'll use to authorize this request. We also construct security settings that ensure the report is initialized on the intended domain. The value provided to the domain property must match the domain from which the file is actually served. The consumer key and consumer secret in this example are for Learnosity's public "demos" account. Once Learnosity provides your own consumer credentials, your Item bank and assessment data will be tied to your own consumer key and secret.

``` python
security = {
    'consumer_key': 'yis0TYCu7U9V4o7M',
    'domain': 'localhost',
}
consumerSecret = '74c5fd430cf1242a527f6223aebd42d30464be22'
```

<i>(of course, you should never normally put passwords into version control)</i>

Now we call LearnositySDK's `Init()` helper to construct our Items API configuration parameters, and sign them securely with the `security`, `request` and `consumerSecret` parameters. `init.generate()` returns us a JSON blob of signed configuration parameters.

``` python
init = Init(
    'items', security, consumerSecret,
    request=items_request
)
generatedRequest = init.generate()
```

[(Back to top)](#table-of-contents)

### **Web page content**
We've got our set of signed configuration parameters, so now we can set up our page content for output. The page can be as simple or as complex as needed, using your own HTML, JavaScript and your frameworks of choice to render the desired product experience.

This example uses plain HTML in a Jinja template, served by the built-in Python web server. However, the Jinja template used here can be easily re-used in another framework, for example Python Flask or Django.

The following example HTML/Jinja template can be found near the bottom of the `standalone-assessment.py` file.

``` python
        template = Template("""<!DOCTYPE html>
        <html>
            <head>
                <link rel="stylesheet" type="text/css" href="../css/style.css">
            </head>
            <body>
                <h1>{{ name }}</title></h1>
                <!-- Items API will render the assessment app into this div. -->
                <div id="learnosity_assess"></div>
                <!-- Load the Items API library. -->
                <script src=\"https://items.learnosity.com/?v2021.2.LTS/\"></script>
                <!-- Initiate Items API assessment rendering, using the signed parameters. -->
                <script>
                    var itemsApp = LearnosityItems.init( {{ generatedRequest }} );
                </script>
            </body>
        </html>
        """)
```

The important parts to be aware of in this HTML are:

* A div with `id="learnosity_assess"`. This is where the Learnosity assessment player will be rendered to deliver the assessment.
* The `<script src="https://items.learnosity.com/?v2021.2.LTS"></script>` tag, which includes Learnosity's Items API on the page and makes the global `LearnosityItems` object available. The version specified as `v2021.2.LTS` will retrieve that specific [Long Term Support (LTS) version](https://help.learnosity.com/hc/en-us/articles/360001268538-Release-Cadence-and-Version-Lifecycle). In production, you should always pin to a specific LTS version to ensure version compatibility.
* The call to `LearnosityItems.init()`, which initiates Items API to inject the assessment player into the page.
* The variable `{{generatedRequest}}` dynamically sends the contents of our init options to JavaScript, so it can be passed to `init()`.

The call to `init()` returns an instance of the ItemsApp, which we can use to programmatically drive the assessment using its methods. We pull in our Learnosity configuration in a variable `{{ generatedRequest }}`, that the Jinja template will import from the Python program. The variable `{{ name }}` is the page title which can be set in the same way.

The Jinja template is rendered by the following line, which will bring in those variables.

``` python
self.wfile.write(bytes(template.render(name='Standalone Assessment Example', generatedRequest=generatedRequest), "utf-8"))  
```

There is some additional code in `standalone-assessment.py`, which runs Python's built-in web server. 

This marks the end of the quick start guide. From here, try modifying the example files yourself, you are welcome to use this code as a basis for your own projects. As mentioned earlier, the Jinja template used here can be easily re-used in another framework, for example Python Flask or Django.

Take a look at some more in-depth options and tutorials on using Learnosity assessment functionality below.

[(Back to top)](#table-of-contents)

## Next steps: additional documentation

### **SDK reference**
See a more detailed breakdown of all the SDK features, and examples of how to use more advanced or specialised features on the [SDK reference page](REFERENCE.md).

### **Additional quick start guides**
There are more quick start guides, going beyond the initial quick start topic of loading an assessment, these further tutorials show how to set up authoring and analytics:
* [Authoring Items quick start guide](https://help.learnosity.com/hc/en-us/articles/360000754958-Getting-Started-With-the-Author-API) (Author API) - create and edit new Questions and Items for your Item bank, then group your assessment Items into Activities, and
* [Analytics / student reporting quick start guide](https://help.learnosity.com/hc/en-us/articles/360000755838-Getting-Started-With-the-Reports-API) (Reports API) - view the results and scores from an assessment Activity. 

### **Learnosity demos repository**
On our [demo site](https://demos.learnosity.com/), browse through many examples of Learnosity API integration. You can also download the entire demo site source code, the code for any single demo, or browse the codebase directly on GitHub.

### **Learnosity reference documentation**
See full documentation for Learnosity API init options, methods and events in the [Learnosity reference site](https://reference.learnosity.com/).

### **Technical use-cases documentation**
Find guidance on how to select a development pattern and arrange the architecture of your application with Learnosity, in the [Technical Use-Cases Overview](https://help.learnosity.com/hc/en-us/articles/360000757777-Technical-Use-Cases-Overview).

### **Deciding what to build or integrate**
Get help deciding what application functionality to build yourself, or integrate off-the-shelf with the [Learnosity "Golden Path" documentation](https://help.learnosity.com/hc/en-us/articles/360000754578-Recommended-Deployment-Patterns-Golden-Path-).

### **Key Learnosity concepts**
Want more general information about how apps on Learnosity actually work? Take a look at our [Key Learnosity Concepts page](https://help.learnosity.com/hc/en-us/articles/360000754638-Key-Learnosity-Concepts).

### **Glossary**
Need an explanation for the unique Learnosity meanings for Item, Activity and Item bank? See our [Glossary of Learnosity-specific terms](https://help.learnosity.com/hc/en-us/articles/360000754838-Glossary-of-Learnosity-and-Industry-Terms).

[(Back to top)](#table-of-contents)

## Contributing to this project

### Adding new features or fixing bugs
Contributions are welcome. See the [contributing instructions](CONTRIBUTING.md) page for more information. You can also get in touch via our support team.

[(Back to top)](#table-of-contents)

## License
The Learnosity Python SDK is licensed under an Apache 2.0 license. [Read more](LICENSE.md).

[(Back to top)](#table-of-contents)

## Usage tracking
Our SDKs include code to track the following information by adding it to the request being signed:
- SDK version
- SDK language
- SDK language version
- Host platform (OS)
- Platform version

We use this data to enable better support and feature planning.

[(Back to top)](#table-of-contents)

## Further reading
Thanks for reading to the end! Find more information about developing an app with Learnosity on our documentation sites: 

* [help.learnosity.com](http://help.learnosity.com/hc/en-us) -- general help portal and tutorials,
* [reference.learnosity.com](http://reference.learnosity.com) -- developer reference site, and
* [authorguide.learnosity.com](http://authorguide.learnosity.com) -- authoring documentation for content creators.

[(Back to top)](#table-of-contents)
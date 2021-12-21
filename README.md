<p align="center"><img width="50%" height="50%" src="docs/images/image-logo-graphic.png"></p>
<h1 align="center">Learnosity SDK - Java</h1>
<p align="center">Everything you need to start building your app in Learnosity, with the Java programming language.<br> 
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

## Requirements


1. Runtime libraries for Java installed. We've used Eclipse Adoptium/Temurin 11 LTS (previously known as 'AdoptOpenJDK'). ([instructions](https://projects.eclipse.org/projects/adoptium.temurin/downloads))

2. Maven installed ([instructions](https://maven.apache.org/download.cgi)). We tested on Maven 3.8.4.

3. Docker installed (required for the example tutorial on this page). ([instructions](https://www.docker.com/get-started)). We tested on Docker version 3.x.

Note: just looking for a .WAR file? Look in the Maven target directory.

Not using Java? See the [SDKs for other languages](https://help.learnosity.com/hc/en-us/sections/360000194318-Server-side-development-SDKs).

### **Supported Java Versions**
The following Java versions are tested and supported:

* [OpenJDK](http://openjdk.java.net/) versions 8 and 11, tested on compatible runtime [Eclipse Adoptium/Temurin 11 LTS](https://projects.eclipse.org/projects/adoptium.temurin/downloads) (previously known as 'AdoptOpenJDK').

We aim to support current LTS versions of Java and the JDK. If you need specific support for another version, please get in touch with our Support team.

[(Back to top)](#table-of-contents)

## Installation

### **Development install from a Git clone**
To install from the terminal, run this command:

    git clone git@github.com:Learnosity/learnosity-sdk-Java.git

### **Alternative method 1: download the zip file**
Download the latest version of the SDK as a self-contained ZIP file from the [GitHub Releases](https://github.com/Learnosity/learnosity-sdk-Java/releases) page. The distribution ZIP file contains all the necessary dependencies.

[(Back to top)](#table-of-contents)

## Quick start guide
Let's take a look at a simple example of the SDK in action. In this example, we'll load an assessment into the browser.

### **Build the .WAR file**
To start, navigate to the root folder of the project on the command line, and run the following command:

``` bash
    make quickstart-assessment
```

This will compile and test the project, building the .WAR file. 

You will get the following advice on the command line, to either copy the .WAR file into your J2EE servlet container, or run the Docker below to boot the Jetty web server:

``` Bash
** Demo package complete **
Now copy docs/quickstart/assessment/webapps/quickstart-1.0.war to the webapps
directory of a servlet container like Jetty or Tomcat, or use Docker:

docker container run --rm -d -v $(pwd)/docs/quickstart/assessment/webapps:/var/lib/jetty/webapps -p 9280:8080 jetty:11.0.7-jdk11
```

Note: your working directory should be the SDK home directory. 

Note: this will load some dependencies, then run a servlet from a JSP file, which uses `App.java` as an adaptor between the servlet and the SDK. 

From this point on, we'll assume that your web server is available at this local address (it will report the port being used when you launch it, by default it's port 9280): 

http://localhost:9280/quickstart-1.0/

The page will load. This is a basic example of an assessment loaded into a web page with Learnosity's assessment player. You can interact with this demo assessment to try out the various Question types.

<img width="50%" height="50%" src="docs/images/image-quickstart-examples-assessment.png">

[(Back to top)](#table-of-contents)

### **How it works**
Let's walk through the code for this standalone assessment example. The source files are included under the quickstart folder, in the following locations:

    /learnosity-sdk-java/docs/quickstart/assessment/src/main/java/com/learnosity/quickstart/App.java
    /learnosity-sdk-java/docs/quickstart/assessment/src/main/webapp/index.jsp
    /learnosity-sdk-java/docs/quickstart/assessment/src/main/resources/config.properties

The first section of code discussed (`App.java`) and is executed server-side. It constructs a set of configuration options for Items API, and securely signs them using the consumer key. The second section is HTML and JavaScript in a JSP page (`index.jsp`) and is executed server-side, and what it generates is loaded in the browser. It renders and runs the assessment functionality.

[(Back to top)](#table-of-contents)

### **Server-side code**
Starting with `App.java`, we start by including some LearnositySDK helpers - they'll make it easy to generate and sign the config options. We also include the standard UUID library for generating unique user and session IDs.

``` Java
import learnositysdk.request.Init;
import java.util.UUID;
```

Now we'll declare the configuration options for Items API. These specify which assessment content should be rendered, how it should be displayed, which user is taking this assessment and how their responses should be stored. 

``` Java
private Map<String, String> createRequestObject() {
    var r = new HashMap();
    r.put("user_id", this.user_id);
    r.put("activity_template_id", "quickstart_examples_activity_template_001");
    r.put("session_id", this.session_id);
    r.put("activity_id", "quickstart_examples_activity_001");
    r.put("rendering_type", "assess");
    r.put("type", "submit_practice");
    r.put("name", "Items API Quickstart");
    r.put("state", "initial");
    return r;
}
```

* `user_id`: unique student identifier. Note: we never send or save student's names or other personally identifiable information in these requests. The unique identifier should be used to look up the entry in a database of students accessible within your system only. [Learn more](https://help.learnosity.com/hc/en-us/articles/360002309578-Student-Privacy-and-Personally-Identifiable-Information-PII-).
* `activity_template_id`: reference of the Activity to retrieve from the Item bank. The Activity defines which Items will be served in this assessment.
* `session_id`: uniquely identifies this specific assessment attempt for save/resume, data retrieval and reporting purposes. Here, we're using the `UUID` library to auto-generate a unique session id.
* `activity_id`: a string you define, used solely for analytics to allow you run reporting and compare results of users submitting the same assessment.
* `rendering_type`: selects a rendering mode, `assess` mode is a "standalone" mode (loading a complete assessment player for navigation, as opposed to `inline` for embedding without).
* `type`: selects the context for the student response storage. `submit_practice` mode means the student responses will be stored in the Learnosity cloud, allowing for grading and review.
* `name`: human-friendly display name to be shown in reporting, via Reports API and Data API.
* `state`: Optional. Can be set to `initial`, `resume` or `review`. `initial` is the default.

**Note**: you can submit the configuration options as a Java map as shown above, or alternatively as a JSON string, JSON object or JavaBean.

Next, we declare the Learnosity consumer credentials we'll use to authorize this request. We also construct security settings that ensure the report is initialized on the intended domain. The value provided to the domain property must match the domain from which the file is actually served. The consumer key and consumer secret in this example are for Learnosity's public "demos" account. Once Learnosity provides your own consumer credentials, your Item bank and assessment data will be tied to your own consumer key and secret.

Note: the values for the consumer key and secret are stored in the `config.properties` file:

``` Java
consumer=yis0TYCu7U9V4o7M
consumerSecret=74c5fd430cf1242a527f6223aebd42d30464be22
```

<i>(of course, you should never normally put passwords into version control)</i>

Now we call LearnositySDK's `Init()` helper to construct our Items API configuration parameters, and sign them securely with the `security`, `request` and `secret` parameters. `init.generate()` returns us a JSON blob of signed configuration parameters.

``` Java
public String initOptions(String domain) {
    Map<String, String> security = createSecurityObject(domain);
    Map<String, String> request = createRequestObject();
    String secret = config.getProperty("consumerSecret");
    try {
        Init init = new Init("items", security, secret, request);
        return init.generate();
    } catch (Exception e) {
        e.printStackTrace();
        return "";
    }
}
```

There is some additional code in `App.java`.

[(Back to top)](#table-of-contents)

### **Web page content**
We've got our set of signed configuration parameters, so now we can set up our page content for output. The page can be as simple or as complex as needed, using your own HTML, JavaScript and your frameworks of choice to render the desired product experience.

This example uses plain HTML in a JSP template, served by the servlet container.

The following example HTML/JSP code can be found near the bottom of the `index.jsp` file.

``` JSP
<html>
    <head><link rel="stylesheet" type="text/css" href="../css/style.css"></head>
    <body>
        <h1>Standalone Assessment Example</h1>

        <!-- Items API will render the assessment app into this div. -->
        <div id="learnosity_assess"></div>

        <!-- Load the Items API library. -->
        <script src="https://items.learnosity.com/?v2021.2.LTS"></script>

        <!-- Initiate Items API assessment rendering, using the JSON blob of signed params. -->
        <script>
            var itemsApp = LearnosityItems.init(
                <%= app.initOptions(request.getServerName()) %>
            );
        </script>
    </body>
</html>
```

The important parts to be aware of in this HTML are:

* A div with `id="learnosity_assess"`. This is where the Learnosity assessment player will be rendered to deliver the assessment.
* The `<script src="https://items.learnosity.com/?v2021.2.LTS"></script>` tag, which includes Learnosity's Items API on the page and makes the global `LearnosityItems` object available. The version specified as `v2021.2.LTS` will retrieve that specific [Long Term Support (LTS) version](https://help.learnosity.com/hc/en-us/articles/360001268538-Release-Cadence-and-Version-Lifecycle). In production, you should always pin to a specific LTS version to ensure version compatibility.
* The call to `LearnosityItems.init()`, which initiates Items API to inject the assessment player into the page.
* The line `<%= app.initOptions(request.getServerName()) %>` dynamically sends the contents of our init options to JavaScript, so it can be passed to `init()`.

The call to `init()` returns an instance of the ItemsApp, which we can use to programmatically drive the assessment using its methods. We pull in our Learnosity configuration in the line `<%= app.initOptions(request.getServerName()) %>`. 

There is some additional code in `index.jsp`.

Our web app is configured to start in this block in `web.xml`:

``` XML
<welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

This marks the end of the quick start guide. From here, try modifying the example files yourself, you are welcome to use this code as a basis for your own projects.

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
The Learnosity Java SDK is licensed under an Apache 2.0 license. [Read more](LICENSE.md).

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
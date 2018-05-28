# Learnosity SDK - Java

[![Build Status](https://travis-ci.org/Learnosity/learnosity-sdk-java.svg?branch=master)](https://travis-ci.org/Learnosity/learnosity-sdk-java)

Include this library into your own codebase to ease integration with any of the Learnosity APIs. Requires Java 7 or higher.

## Installation
Installation can be done in three ways:

1.) Download the jar in directory learnosity-sdk-java/Dist/All/ and add to your build path. All the required libraries are included in this jar.

2.) Download the jar in learnosity-sdk-java/Dist/Light/ and add to your build path. This requires the external libraries which can be found in learnosity-sdk-java/Dist/Light/RequiredJars

3.) Download the source code and integrate into your project. This requires the external libraries which can be found in learnosity-sdk-java/Dist/Light/RequiredJars

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

```
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
			securityMap.put("user_id", "12345678");
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

```
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
			         +    "\"user_id\": \"12345678\"}";
			 
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

```
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

```
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

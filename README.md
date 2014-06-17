# Learnosity SDK - Java

Include this library into your own codebase to ease integration with any of the Learnosity APIs.

## Installation
Installation can be done in three ways:

1.) Download the source code and integrate into your project. This requires the external libraries which can be found in Dist/Light/RequiredJars

2.) Download LearnositySdkJavaLight.jar and add to your build path. This requires the external libraries which can be found in Dist/Light/RequiredJars

3.) Download LearnositySdkJavaAll.jar and add to your build path. All the required libraries are included in this jar.

## Test

In order to check that you've added the required code correctly, you can download Test/Test.java and try to run it. If it runs successfully, all should be fine.

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
 * String  action

 or 5 arguments:

 * String  service type
 * Object  security details (**no secret**)
 * String  secret
 * Object  request details
 * String  action

The action String can be the empty String. The security and request Object can be of any type that can be parsed into a org.json.JSONObject. Examples are org.json.JSONOBject, a valid JSON String or a map. (See Test.java for examples)


```
// Create a security Map
Map securityMap = new HashMap();
securityMap.put("consumer_key", "yis0TYCu7U9V4o7M");
securityMap.put("user_id", "12345678");
securityMap.put("domain", "localhost"

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
init = new Init("questions", sec, consumerSecret, req, "");

// Call the generate() method to retrieve a JavaScript object
String questionJson = Init.generate();

// Pass the object to the initialisation of any Learnosity API
LearnosityApp.init(questionsJson);

// Let's initialise with JSON Strings
 String secString = "{\"consumer_key\":\"yis0TYCu7U9V4o7M\","
         + "\"domain\": \"assess.vg.learnosity.com\""
         +   "\"user_id\": \"12345678\"}";
 
 String reqString = "{\"state\":\"initial\","
         +  "\"type\":\"local_practice\","
         +  "\"timestamp\":\"20140617-0533\","
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
         +       "\"Australia\"]}}]
 init = new Init("questions", sec, consumerSecret, req, "");
 questionJson = init.generate();

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

**getError()**<br>
Returns an array that includes the error code and message (if an error was thrown)

**getHeader()**<br>
Currently only returns the *content_type* header of the response.

**getStatusCode()**<br>
Returns the HTTP status code of the response.

<hr>

### DataApi

This is a helper class for use with the Data API. It creates the initialisation packet and sends a request to the Data API, returning an instance of Remote. You can then interact as you would with Remote, eg ```getBody()```

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

DataApi dataApi = new DataApi("https://data.vg.learnosity.com/latest/itembank/items", sec, consumerSecret, reqData, "get");
remote = dataApi.request();
JSONObject res = new JSONObject(remote.getBody());
```

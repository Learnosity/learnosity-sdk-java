package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

public class QuestionsApp extends App
{
    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        JSONObject request = createRequestObject();
        String secret = config.getProperty("consumerSecret");

        try {
            Init init = new Init("questions", security, secret, request);
            return init.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    Map<String, String> createSecurityObject(String domain) {
        var security = new HashMap();
        security.put("domain", domain);
        security.put("consumer_key", config.getProperty("consumer"));
        security.put("user_id", this.user_id);
        return security;
    }

    JSONObject createRequestObject() {
        JSONObject request = new JSONObject();
        request.put("type", "local_practice");
        request.put("state", "initial");
        request.put("id", "questionsapi-demo");
        request.put("name", "Questions API Demo");
        request.put("course", "mycourse");

        JSONArray arr_questions = new JSONArray();
        JSONObject question = new JSONObject();
        question.put("type", "association");
        question.put("response_id", "60001");
        question.put("stimulus", "Match the cities to the parent nation.");
        question.put("instant_feedback", true);

        JSONArray stimulust_list = new JSONArray();
        stimulust_list.put("London");
        stimulust_list.put("Dublin");
        stimulust_list.put("Paris");
        stimulust_list.put("Sydney");
        question.put("stimulus_list", stimulust_list);

        JSONArray possible_responses = new JSONArray();
        possible_responses.put("Australia");
        possible_responses.put("France");
        possible_responses.put("Ireland");
        possible_responses.put("England");
        question.put("possible_responses", possible_responses);

        JSONObject validation = new JSONObject();
        JSONArray valid_responses = new JSONArray();
        valid_responses.put(new JSONArray().put("England"));
        valid_responses.put(new JSONArray().put("Ireland"));
        valid_responses.put(new JSONArray().put("France"));
        valid_responses.put(new JSONArray().put("Australia"));
        validation.put("valid_responses", valid_responses);
        question.put("validation", validation);

        arr_questions.put(question);
        request.put("questions", arr_questions);

        return request;
    }
}

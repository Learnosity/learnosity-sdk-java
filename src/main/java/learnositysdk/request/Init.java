package learnositysdk.request;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 *--------------------------------------------------------------------------
 * Learnosity SDK - Init
 *--------------------------------------------------------------------------
 *
 * Used to generate the necessary security and request data (in the
 * correct format) to integrate with any of the Learnosity API services.
 *
 */

public class Init {
    static String signaturePrefix = "$02$";
    private HmacUtils hmacUtils;

    /**
     * Which Learnosity service to generate a request packet for.
     * Valid values (see also `$validServices`):
     *  - assess
     *  - author
     *  - data
     *  - items
     *  - questions
     *  - reports
     */
    private String service;

    /**
     * The consumer secret as provided by Learnosity. This is your private key
     * known only by the client (you) and Learnosity, which must not be exposed
     * either by sending it to the browser or across the network.
     * It should never be distributed publicly.
     */
    private String secret;

    /**
     * A JSONObject of security details. This typically contains:
     *  - consumer_key
     *  - domain (optional depending on which service is being initialised)
     *  - timestamp (optional)
     *  - user_id (optional depending on which service is being initialised)
     *
     * It's important that the consumer secret is NOT a part of this array.
     */
    private JSONObject securityPacket;

    /**
     * An optional JSONObject of request parameters used as part
     * of the service (API) initialisation.
     */
    private JSONObject requestPacket;

    /**
     * If `requestPacket` is used, `requestString` will be the string
     * (JSON) representation of that. It's used to create the signature
     * and returned as part of the service initialisation data.
     */
    private String requestString = "";

    /**
     * An optional value used to define what type of request is being
     * made. This is only required for certain requests made to the
     * Data API (http://docs.learnosity.com/dataapi/)
     */
    private String action = "";

    /**
     * Currently, Data API requests cannot be altered because the full request
     * string is not returned by the `generate()` method.
     */
    private boolean preserveRequest = false;

    /**
     * Most services add the request packet (if passed) to the signature
     * for security reasons. This flag can override that behaviour for
     * services that don't require this.
     */
    private boolean signRequestData = true;

    /**
     * Key names that are valid in the securityPacket, they are also in
     * the correct order for signature generation.
     */
    private final String[] validSecurityKeys = new String[] {"consumer_key", "domain", "timestamp", "user_id"};

    /**
     * Valid strings for service
     */
    private String[] validServices = new String[] {"assess", "author", "data", "items", "questions", "reports", "events"};

    /**
     * Instantiate this class with all security and request data. It
     * will be used to create a signature.
     *
     * @param service        the service to be used
     * @param securityPacket the security information. Can be a json.org.JSONObject or an object of
     *                       any type which is valid to instantiate a json.org.JSONObject with
     * @param secret         the private key
     * @throws Exception     if any of the passed arguments are invalid
     */
    public Init (String service, Object securityPacket, String secret) throws Exception
    {
        // First validate and set the arguments
        this.validateRequiredArgs(service, securityPacket, secret);

        this.hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);

        // Set any service specific options
        this.setServiceOptions();

        // Generate the signature based on the arguments provided
        this.securityPacket.put("signature", this.generateSignature());
    }

    /**
     * Instantiate this class with all security and request data. It
     * will be used to create a signature.
     *
     * @param service        the service to be used
     * @param securityPacket the security information. Can be a json.org.JSONObject or an object of
     *                       any type which is valid to instantiate a json.org.JSONObject with
     * @param secret         the private key
     * @param requestPacket  an object which can be parsed into a JSONObject
     * @throws Exception     if any of the passed arguments are invalid
     */
    public Init (String service, Object securityPacket, String secret, Object requestPacket) throws Exception
    {
        this(service, securityPacket, secret, requestPacket, service.equals("data"));
    }

    /**
     * This Constructor is only for use within the SDK to take advantage of
     * potentially breaking changes.
     *
     * @param service         the service to be used
     * @param securityPacket  the security information. Can be a json.org.JSONObject or an object of
     *                        any type which is valid to instantiate a json.org.JSONObject with
     * @param secret          the private key
     * @param requestPacket   an object which can be parsed into a JSONObject
     * @param preserveRequest flag denoting if the passed request must not be altered
     * @throws Exception      if any of the passed arguments are invalid
     */
    Init (String service, Object securityPacket, String secret, Object requestPacket, boolean preserveRequest) throws Exception
    {
        this.preserveRequest = preserveRequest;

        // First validate and set the arguments
        this.validateRequiredArgs(service, securityPacket, secret);

        this.hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);

        this.setRequestPacket(requestPacket);

        // Set any service specific options
        this.setServiceOptions();

        // Generate the signature based on the arguments provided
        this.securityPacket.put("signature", this.generateSignature());
    }

    /**
     * Setter method for action. If an action is required, it should be set before generate() is called
     * @param action the required action (e.g. get or post)
     */
    public void setAction(String action)
    {
    	this.action = action;

    	// Re-generate the signature, as an action is now set
        this.securityPacket.put("signature", this.generateSignature());
    }

    /**
     * Generate the data necessary to make a request to one of the
     * Learnosity products/services.
     *
     * @return A JSON string
     */
    public String generate()
    {
        JSONObject output = new JSONObject();
        String outputString = "";

        if (this.service.equals("assess") ||
            this.service.equals("author") ||
            this.service.equals("data") ||
            this.service.equals("items") ||
            this.service.equals("reports")) {

            // Add the security packet (with signature) to the output
            output.put("security", this.securityPacket);

            // Add the action if necessary (Data API)
            if (!this.action.isEmpty()) {
                output.put("action", this.action);
            }

            if (this.service.equals("data")) {
                return output.getJSONObject("security").toString();
            } else if (this.service.equals("assess")) {
                return this.requestString;
            }

            outputString = output.toString();
            // Add the request packet if available
            if (StringUtils.isNotEmpty(this.requestString)) {
                outputString = outputString.substring(0, outputString.length() - 1) + ",";
                outputString = outputString + "\"request\":" + this.requestString + "}";
            }
        } else if (this.service.equals("questions")) {
            // Make a copy of security packet (with signature) to the root of output
            output = new JSONObject(this.securityPacket, JSONObject.getNames(this.securityPacket));

            // Remove the `domain` key from the security packet
            output.remove("domain");

            outputString = output.toString();
            // Merge the request packet if necessary. Note: to make sure we don't change the
            // order of key/value pairs in the json, we manipulate the json string instead of
            // the json object and then parsing into a string
            if (StringUtils.isNotEmpty(this.requestString)) {
                outputString = outputString.substring(0, outputString.length() - 1) + ",";
                outputString = outputString + this.requestString.substring(1);
            }
        } else if (this.service.equals("events")) {
        	// Add the security packet (with signature) to the output
        	output.put("security", this.securityPacket);
        	outputString = output.toString();

            // Add the request packet as key 'config' if available
            if (StringUtils.isNotEmpty(this.requestString)) {
                outputString = outputString.substring(0, outputString.length() - 1) + ",";
                outputString = outputString + "\"config\":" + this.requestString + "}";
            }
        }
        return outputString;
    }

    /**
     * Generate a signature hash for the request, this includes:
     *  - the security credentials
     *  - the `request` packet (a JSON string) if passed
     *  - the `action` value if passed
     *
     * @return A signature hash for the request authentication
     */
    public String generateSignature()
    {
        ArrayList<String> signatureArray = new ArrayList<String>();

        // Create a pre-hash string based on the security credentials
        // The order is important
        for (String key : this.validSecurityKeys) {
            if (this.securityPacket.has(key)) {
                signatureArray.add(this.securityPacket.getString(key));
            }
        }

        // Add the secret
        // Add the requestPacket if necessary
        if (this.signRequestData && !this.requestString.isEmpty()) {
            signatureArray.add(this.requestString);
        }

        // Add the action if necessary
        if (!this.action.isEmpty()) {
            signatureArray.add(this.action);
        }

        return this.hashValue(signatureArray);
    }

    public String getRequestString()
    {
        return this.requestString;
    }

    /**
     * Hash an array value
     *
     * @param  value the array to hash
     *
     * @return string The hashed string
     */
    private String hashValue(ArrayList<String> value)
    {
        String valueString = "";
        for (String entry : value) {
            if (valueString.equals("")) {
                valueString = entry;
            } else {
                valueString += "_" + entry;
            }
        }

        return signaturePrefix + this.hmacUtils.hmacHex(valueString);
    }

    /**
     * Set any options for services that aren't generic
     */
    private void setServiceOptions()
    {
        if (this.service.equals("assess") ||
            this.service.equals("questions")) {

            this.signRequestData = false;
            // The Assess API holds data for the Questions API that includes
            // security information and a signature. Retrieve the security
            // information from $this and generate a signature for the
            // Questions API
            if (this.service.equals("assess") &&
                    this.requestPacket != null &&
                    this.requestPacket.has("questionsApiActivity")) {
                JSONObject questionsApi = this.requestPacket.getJSONObject("questionsApiActivity");
                String domain = "assess.learnosity.com";
                ArrayList<String> signatureArray = new ArrayList<String>();
                if (this.securityPacket.has("domain")) {
                    domain = this.securityPacket.getString("domain");
                } else if (questionsApi.has("domain")) {
                    domain = questionsApi.getString("domain");
                }

                for (String key : new String[] {"consumer_key" , "timestamp" , "user_id"}) {
                    questionsApi.put(key, this.securityPacket.getString(key));
                }
                signatureArray.add(this.securityPacket.getString("consumer_key"));
                signatureArray.add(domain);
                signatureArray.add(this.securityPacket.getString("timestamp"));
                signatureArray.add(this.securityPacket.getString("user_id"));
                questionsApi.put("signature", this.hashValue(signatureArray));
            }
        } else if (this.service.equals("items")) {
            // The Items API requires a user_id, so we make sure it's a part
            // of the security packet as we share the signature in some cases
            if (!this.securityPacket.has("user_id") &&
                 this.requestPacket.has("user_id")) {
                this.securityPacket.put("user_id", this.requestPacket.getString("user_id"));
            }
        } else if (this.service.equals("events")) {
            this.signRequestData = false;
        	JSONObject hashedUsers = new JSONObject();
        	if (this.requestPacket.has("users")) {
        		JSONArray users = this.requestPacket.getJSONArray("users");
        		for (int i = 0; i < users.length(); i++) {
        			String user = users.getString(i);
        			String stringToHash = user + this.secret;
                    String userHash = signaturePrefix + this.hmacUtils.hmacHex(stringToHash);
        			hashedUsers.put(user, userHash);
        			}
        		this.requestPacket.put("users", hashedUsers);
        		this.updateRequestString();
        	}
        }
    }

    /**
     * Validate the required parameters of the constructor and set them if ok
     *
     * @param  service
     * @param  securityPacket
     * @param  secret
     * @throws Exception
     */
    private void validateRequiredArgs(String service, Object securityPacket, String secret) throws Exception
    {
        if (service.isEmpty()) {
            throw new Exception("The `service` argument wasn't found or was empty");
        } else if (!Arrays.asList(this.validServices).contains(service.toLowerCase())) {
            throw new Exception("The service provided " + service + " is not valid");
        }
        this.service = service;

        // In case the user gave us a securityPacket String, convert to a JSONObject
        this.validateSecurityPacket(securityPacket);

        if (secret.isEmpty()) {
            throw new Exception("The `secret` argument must be a valid string");
        }
        this.secret = secret;
    }

    /**
     * Sets the request packet, updating the stored string version as well
     *
     * @param requestPacket The request packet to store
     */
    private void setRequestPacket(Object requestPacket) throws Exception
    {
        JSONObject convertedPacket = validateRequestPacket(requestPacket);

        // Prevent updating request packet in situations where we must not do so
        if (this.preserveRequest) {
            // Only properly support using inputs provided as String
            if (this.requestPacket == null) {
                if (requestPacket instanceof String) {
                    this.requestString = (String) requestPacket;
                    this.requestPacket = convertedPacket;
                    return;
                }
                // If any other format is provided, we cannot make any
                // guarantees anyway - fall through.
            } else {
                throw new Exception("Cannot update request packet for this request");
            }
        }

        this.requestPacket = Telemetry.addToRequest(convertedPacket);
        this.updateRequestString();
    }

    /**
     * Updates the stored request string using the current request packet
     */
    private void updateRequestString() {
        this.requestString = this.requestPacket.toString();

        this.requestString = fixJson(this.requestString);
    }

    /**
     * Ensures encoded JSON is correctly formatted for the Learnosity APIs
     *
     * PHP SDK: https://github.com/Learnosity/learnosity-sdk-php/blob/42f2ffade51831966e0f92f25fa06e50c1dbecce/src/LearnositySdk/Utils/Json.php#L54
     */
    private String fixJson(String string) {
        final Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
        StringBuilder result = new StringBuilder();
        final Matcher matcher = pattern.matcher(string);
        int lastMatch = 0;

        while(matcher.find()) {
            final MatchResult matchResult = matcher.toMatchResult();
            String replacement = Character.toString((char)Integer.parseInt(matchResult.group(1), 16));
            result.append(string.substring(lastMatch, matchResult.start())).append(replacement);
            lastMatch = matchResult.end();
        }

        if (lastMatch < string.length())
            result.append(string.substring(lastMatch));

        return result.toString().replace("\\/", "/");
    }

    /**
     * Validate the security packet argument
     * @param securityPacket
     * @throws Exception
     */
    private void validateSecurityPacket (Object securityPacket) throws Exception
    {
        if (securityPacket instanceof JSONObject) {
            this.securityPacket = new JSONObject(securityPacket.toString());
        } else {
            if (securityPacket instanceof String) {
                this.securityPacket = new JSONObject((String)securityPacket);
            } else if (securityPacket instanceof Map) {
                this.securityPacket = new JSONObject((Map)securityPacket);
            } else {
                // Try to make a JSONObject out of a hopefully valid java bean
                this.securityPacket = new JSONObject(securityPacket);
            }
        }

        if (this.service.equals("questions") && !this.securityPacket.has("user_id")) {
            throw new Exception("If using the questions api, a user id needs to be specified");
        }

        if (this.securityPacket.length() == 0) {
            throw new Exception("The security packet argument cannot be empty");
        }

        Iterator<String> keyIter = this.securityPacket.keys();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            if (!Arrays.asList(this.validSecurityKeys).contains(key)) {
                throw new Exception("Invalid key found in the security packet: " + key);
            }
        }

        if (!this.securityPacket.has("timestamp")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            this.securityPacket.put("timestamp", dateFormat.format(date));
        }
    }

    /**
     * Disables telemetry.
     *
     * We use telemetry to enable better support and feature planning. It is therefore not advised to
     * disable it, because it will not interfere with any usage.
     */
    public static void disableTelemetry()
    {
        Telemetry.disableTelemetry();
    }

    /**
     * Enables telemetry.
     *
     * Telemetry is enabled by default. We use it to enable better support and feature planning.
     * It is however not advised to disable it, and it will not interfere with any usage.
     */
    public static void enableTelemetry()
    {
        Telemetry.enableTelemetry();
    }

    /**
     * Validates the request packet stored in this instance
     * @throws Exception if the requestPacket is empty or cannot be converted
     *                   into a JSONObject
     */
    static JSONObject validateRequestPacket(Object requestPacket) throws Exception
    {
        JSONObject convertedPacket;

        // Properly convert the request packet into a JSONObject
        if (requestPacket instanceof JSONObject) {
            convertedPacket = new JSONObject(requestPacket.toString());
        } else if (requestPacket instanceof String) {
            convertedPacket = new JSONObject((String) requestPacket);
        } else if (requestPacket instanceof Map) {
            convertedPacket = new JSONObject((Map) requestPacket);
        } else {
            // Try to make a JSONObject out of a hopefully valid java bean
            convertedPacket = new JSONObject(requestPacket);
        }

        if (convertedPacket.length() == 0) {
            throw new Exception("The requestPacket cannot be empty.");
        }

        return convertedPacket;
    }
}

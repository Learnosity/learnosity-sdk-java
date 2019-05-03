package learnositysdk.request;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;

class Telemetry {
	private static boolean telemetryEnabled = true;
	private static JSONObject telemetryData = null;

	// Pre-defined expected metric values
	private final static String SDK_LANGUAGE = "java";
	private final static String UNKNOWN = "unknown";
	// Java Flavors
	private final static String FLAV_OPEN_JDK = "openjdk";
	private final static String FLAV_ORACLE = "oracle";
	// Platforms
	private final static String PLAT_DARWIN = "darwin";
	private final static String PLAT_LINUX = "linux";
	private final static String PLAT_WIN = "win";

	static boolean isEnabled()
	{
		return telemetryEnabled;
	}

	static void disableTelemetry()
	{
		Telemetry.telemetryEnabled = false;
	}

	static void enableTelemetry()
	{
		Telemetry.telemetryEnabled = true;
	}

	/**
	 * Add the telemetry data to the request packet, under "meta.sdk".
	 *
	 * @param requestPacket The request to add the telemetry data to
	 */
	static JSONObject addToRequest(JSONObject requestPacket)
	{
		JSONObject meta;

		if (!Telemetry.telemetryEnabled) {
			return requestPacket;
		}

		try {
			meta = requestPacket.getJSONObject("meta");
			meta.put("sdk", Telemetry.getTelemetryData());
		} catch (JSONException e) {
			meta = new JSONObject();
			meta.put("sdk", Telemetry.getTelemetryData());
			requestPacket.put("meta", meta);
		}

		return requestPacket;
	}

	private static JSONObject getTelemetryData()
	{
		if (Telemetry.telemetryData != null) {
			return Telemetry.telemetryData;
		}

		JSONObject telemetryData = Telemetry.telemetryData = new JSONObject();
		telemetryData.put("version", Telemetry.getSdkVersion());
		telemetryData.put("lang", Telemetry.SDK_LANGUAGE);
		telemetryData.put("lang_version", System.getProperty("java.version"));
		telemetryData.put("lang_flavor", Telemetry.getLanguageFlavor());
		telemetryData.put("platform", Telemetry.getPlatform());
		telemetryData.put("platform_version", Telemetry.getPlatformVersion());

		return telemetryData;
	}

	private static String getSdkVersion()
	{
		String version = Telemetry.UNKNOWN;
		Properties properties = new Properties();
		InputStream propertiesStream = Telemetry.class.getClassLoader().getResourceAsStream("project.properties");

		if (propertiesStream != null) {
			try {
				properties.load(propertiesStream);
				version = properties.getProperty("version");
			} catch (Exception e) {
				// Fail silently, returning default
			}
		}

		return version;
	}

	private static String getLanguageFlavor()
	{
		String runtime = System.getProperty("java.runtime.name").toLowerCase(Locale.ENGLISH);

		if (runtime.contains("openjdk")) {
			return Telemetry.FLAV_OPEN_JDK;
		}

		if (runtime.contains("java(tm)")) {
			return Telemetry.FLAV_ORACLE;
		}

		return runtime;
	}

	private static String getPlatform()
	{
		String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

		if (os.contains("mac")) {
			return Telemetry.PLAT_DARWIN;
		}

		if (os.contains("linux")) {
			return Telemetry.PLAT_LINUX;
		}

		if (os.contains("windows")) {
			return Telemetry.PLAT_WIN;
		}

		return os;
	}

	private static String getPlatformVersion()
	{
		// Attempt to manually fetch the kernel version for Darwin - java's OS version returns macOS version instead
		if (Telemetry.getPlatform().equals(Telemetry.PLAT_DARWIN)) {
			String version = Telemetry.unameVersion();

			if (version != null) return version;
			// Drop through if there was an issue getting the version from uname
		}

		return System.getProperty("os.version");
	}

	private static String unameVersion()
	{
		String resultString = null;
		String command = "uname -r";

		try {
			Process proc = Runtime.getRuntime().exec(command);
			StringWriter writer = new StringWriter();

			proc.waitFor();

			IOUtils.copy(proc.getInputStream(), writer, Charset.defaultCharset());
			resultString = writer.toString().trim();
		} catch (Exception e) {
			// Fail silently, returning default
		}

		return resultString;
	}
}

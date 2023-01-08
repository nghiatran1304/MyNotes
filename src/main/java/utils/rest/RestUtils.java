package utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtils {

	private String BASE_URL = null;
	private ObjectMapper mapper = new ObjectMapper();

	public RestUtils(String BASE_URL) {
		this.BASE_URL = BASE_URL;
	}

	public JsonNode get(String path) {
		return request("GET", path, null);
	}

	public JsonNode post(String path, Object data) {
		return request("POST", path, data);
	}

	public JsonNode put(String path, Object data) {
		return request("PUT", path, data);
	}

	public JsonNode delete(String path) {
		return request("DELETE", path, null);
	}

	private JsonNode request(String method, String path, Object data) {
		try {
			URL url = new URL(BASE_URL + path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestMethod(method);

			boolean isPost = method.equalsIgnoreCase("POST");
			boolean isPut = method.equalsIgnoreCase("PUT");
			if (isPost || isPut) {
				conn.setDoOutput(true);
				mapper.writeValue(conn.getOutputStream(), data);
			}

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				return mapper.readTree(conn.getInputStream());
			}
			conn.disconnect();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

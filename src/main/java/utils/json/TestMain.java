package utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class TestMain {
	public static void main(String[] args) {
		try {
			String BASE_SOURCE = "src/main/java/utils/json/info.json";
			File source = new File(BASE_SOURCE);
			ObjectMapper mapper = new ObjectMapper();

			Info myinfo = mapper.readValue(source, Info.class);
			System.out.println(myinfo.getName());

			String json = mapper.writeValueAsString(myinfo);

			JsonNode node = mapper.readTree(json);
			System.out.println(node.toPrettyString());

//	            JsonNode node = mapper.readTree(source);
//	            String name = node.get("name").asText();
//	            boolean gender = node.get("gender").asBoolean();
//	            String email = node.get("contact").get("email").asText();
//	            String phone = node.findValue("phone").asText();
//	            int year = node.get("year").asInt();
			//
//	            System.out.println("Fullname: " + name);
//	            System.out.println("Email:" + email);
//	            System.out.println("Phone: " + phone);
//	            System.out.println("Year: " + year);
			//
//	            System.out.print("Skill: ");
//	            node.get("skill").iterator().forEachRemaining(s -> {
//	                System.out.println("\t" + s.asText());
//	            });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package utils.json;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info {
	private String name;
	private Integer year;
	private boolean gender;
	private Contact contact;
	private List<String> skill;
}

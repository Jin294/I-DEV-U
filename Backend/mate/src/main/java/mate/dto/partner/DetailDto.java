package mate.dto.partner;

import java.util.List;

import lombok.Data;

@Data
public class DetailDto {
	private int userIdx;
	private String name;
	private String nickname;
	private String intro;
	private int age;
	private String gender;
	private List<String> techList;
	private String storedFileName;
}

package mate.dto.partner;

import java.util.List;

import lombok.Data;

@Data
public class PartnerDto {
	private int userIdx;
	private String name;
	private String nickname;
	private long percent;
	private String invite;
	private List<String> languageList;
	private String storedFileName;

}

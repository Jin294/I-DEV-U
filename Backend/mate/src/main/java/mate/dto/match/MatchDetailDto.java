package mate.dto.match;

import lombok.Data;

import java.util.List;

//"imageUrl (TEXT)": "조회한 유저의 이미지",
//        "nickname (VARCHAR)": "조회한 유저의 닉네임",
//				"work(INT)" : "유저의 직무"
//				"language(LIST,VARCHAR)" : "유저가 사용하는 언어"
//				[a,b,c,d,e]
//				"location(VARCHAR)" : "유저의 거주지역"
//				"face(VARCHAR)" : "유저의 얼굴상"
//				"job(VARCHAR)" : "유저의 경력(0,1,3,5,7,9,100)"
//				"age(INT)" : "유저 나이",
//				"salary(INT)" : "유저의 연봉(0,1)"
//        "gender (VARCHAR)": "조회한 유저의 성별(MALE,FEMALE)",
//        "intro (TEXT)": "조회한 유저의 자기 소개"

@Data
public class MatchDetailDto {
    private String nickname;
    private String work;
    private List<String> language;
    private String location;
    private String face;
    private String job;
    private int age;
    private String salary;
    private String gender;
    private String intro;
    private String storedFileName;
}

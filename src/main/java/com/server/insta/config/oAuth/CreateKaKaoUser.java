package com.server.insta.config.oAuth;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.server.insta.config.exception.BusinessException;
import com.server.insta.dto.response.SnsSignInResponseDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.server.insta.config.exception.BusinessExceptionStatus.KAKAO_LOGIN;

public class CreateKaKaoUser {

    public static SnsSignInResponseDto createKaKaoUserInfo(String token){

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("id : " + id);
            System.out.println("email : " + email);

            br.close();

            return SnsSignInResponseDto.builder()
                    .email(email)
                    .id(id)  //비밀번호에 특수문자 적어도 1개 이상포함이므로 추가
                    .build();

        } catch(Exception e){
            throw new BusinessException(KAKAO_LOGIN);
        }


    }

}

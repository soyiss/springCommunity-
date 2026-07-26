package com.domain.springCommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    // 이메일: 인증번호를 임시로 저장하는 저장소
    private final Map<String,String> 인증번호저장소= new HashMap<>();

    // 인증번호 6자리 생성
    public String 인증번호생성(){

        Random random = new Random(); // 랜덤기능을 랜덤명칭에 담아 사용
        /*

        [String]
        String a = "안"
        String a = "안녕"
        String a = "안녕하"
        String a = "안녕하세"   자바의 가비지컬렉션으로 사용되지 않는 a 변수들을 제거하면서 생성
        String a = "안녕하세요"

        [StringBuilder]
        StringBuilder =  숫자나 글자들을 한 글자씩 이어붙일 문자열 조립용 개체
        String 은 값을 합칠 때마다 새 객체가 생겨서 비료율적이라, 여러번 이어붙일 땐 StringBuilder 를 사용

        */

        StringBuilder sb = new StringBuilder();
        for (int i =0; i<6;i++){ //0~5 까지 반복 (0,1,2,3,4,5)

            /*
             random.nextInt(10) = 0~9 사이의 정수 하나를 무작위로 추출
             무작위로 추출한 숫자를 sb.append 문자열 조립기에 이어붙인다.
            */
            sb.append(random.nextInt(10));
        }
        return sb.toString(); // 반복이 모두 끝나면 sb에 쌓인 6개의 숫자를 문자열로 변환해서 반환한다.
    }

    // 인증번호를 생성해서 이메일로 발송
    public void 인증번호발송(String email) {
        String 인증번호 = 인증번호생성();

        /*
        message 는 누구에게 (To) 제목(Subject) 내용(Text)만 담고
        누가 보내는지(From)은 javascript가 application.yaml에 작성되어있는 정보를 파락한 후 메일 보내기를 시도하는 것
        */
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); //누구에게 보낼지 설정하는 코드 회원가입 화면에서 사용자가 입력한 이메일주소가 여기로 들어옴
        message.setSubject("[회사이름] 이메일 인증번호 입니다."); // 메일제목
        message.setText("인증번호는 ["+ 인증번호+"] 입니다. 회원가입 화면에 입력해주세요."); // 메일본문
        javaMailSender.send(message);
        인증번호저장소.put(email,인증번호);

    }

    // 사용자가 입력한 인증번호가 맞는지 확인
    public boolean 인증번호확인(String email, String userInputCode){

        String 저장된인증번호 = 인증번호저장소.get(email);
        if(저장된인증번호 == null)return false;


        boolean 일치 = 저장된인증번호.equals(userInputCode);
        if(일치)인증번호저장소.remove(email);
        return 일치;
    }
}

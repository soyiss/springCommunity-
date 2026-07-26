package com.domain.springCommunity.controller;

import com.domain.springCommunity.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //@Controller + @ReponseBody 를 붙인 백엔드 API 상태로 사용함을 의미한다.
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;


    /*
    인증번호 보내는 기능
     /email/send 경로로 post 요청을 보내면 이 메서드가 실행되도록 매핑
     post를 쓰는 이유:  단순 조회가 아닌 메일발송 동작이므로 get 조회가 아닌 특정 데이터 발송이기 때문에 post가 더 적절하다.

     @param email : 소비자가 작성한 본인 이메일을 html → javaScript로 담아와 email 이라는 파라미터에 그대로 저장한다.
                    예를 들어, /email/send?email=acb@naver.com 요청시 email 변수에 acb@naver.com이 담김

     emailService.인증번호발송(email);
                                    : 실제 처리 (랜덤 인증번호 생성 + 메일발송 + 저장소에 저장)는 EmailService에서 담당하므로,
                                    EmailController는 그 메서드를 호출만 한다.
                                    EmailController는 "누가 무슨 요청을 보냈는지" 만 받아서 서비스에 전달하는 역할만 하고, 실제 로직은 서비스에 맡김

    @return :  @RestController 이기 때문에 이 문자열을 화면 이름이 아니라 그대로 응답 body(데이터)로 클라이언트에게 전달되며,
               프론트엔드 자바스크립트(fetch)가 이 문자열을 받아서 alert으로 보여주는 구조이다.


    */

    @PostMapping("/email/send")
    public String 인증번호발송요청(@RequestParam String email){
        emailService.인증번호발송(email);
        return "인증번호가 발송되었습니다.";

    }

    /*
    소비자가 입력한 인증번호와 자바에서 랜덤으로 보내진 인증번호가 일치하는지 확인하는 기능
    /email/verify 경로로 오는 post 요청을 처리하도록 매핑한다.
    @param email  html → javaScript → fetch로  사용자의 이메일과
    @param code 사용자가 입력한 인증번호 값을 각각 받아온다.
                예를 들어 /email/verify?email?=abc@naver.com&code=431890 와 같이 가져옴

    boolean result = emailService.인증번호확인(email, code);
                    : 인증번호확인() 메서드를 호출해서, 저장된 인증번호와 사용자가 입력한 코드가 일치하는지 true, false로 판정받는다.

    @return result ? "인증성공":"인증실패";
                    : 삼항연산자 기법      조건? 조건이 true일 경우 실행할 코드: 조건이 false면 실행할코드;
                    result가 true이면 "인증성공"
                    result가 false이면 "인증실패"

    삼항연산자를 사용하지 않을 경우
    if(emailService.인증번호확인(email,code)){
        return "인증성공";
    }else {
        return "인증실패";
    }

    */

    @PostMapping("/email/verify")
    public String 인증번호확인요청(@RequestParam String email,@RequestParam String code){

        boolean result = emailService.인증번호확인(email,code);
        return result ? "인증성공":"인증실패";


    }






}

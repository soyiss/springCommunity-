package com.domain.springCommunity.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

// application.yaml 처럼 한줄 설정으로 해결되지 않는 설정의 경우, 자바 클래스를 만들고 클래스 위에 환경설정이라는 어노테이션을 추가해준다.
// implements WebMvcConfigurer =  Spring MVC의 기본 동작을 환경설정 변경할 수 있게 해주는 인터페이스이다.
@Configuration
public class WebConfig implements WebMvcConfigurer {



    // import org.springframework.beans.factory.annotation.Value;
    @Value("${file.upload-dir}") // application.yaml에서 설정한 데이터를 가져오는 방법
    private String uploadDir;

    /*
    uploads/파일명으로 요청하면, 실제 서버 폴더에 있는 이미지 파일을 찾아서 응답

    WebMvcConfigurer 스프링부트 개발자가 만들어놓은 기능들을 WebConfig라는 자바 클래그 파일에 가져와서 사용하겠다.

    @Override = 스프링부트 개발자가 만들어놓은 기능을 우리 회사의 방식대로 만들어서 재 정의하여 덮어쓰기 하겠다라는 의미
                개발자간의 관례적인 예의 표기이다.
                누군가가 만들어놓은 기능을 개발자 또는 회사 형식에 맞도록 덮어쓰기한 기능이라는 표기를 나타냄

    ResourceHandlerRegistry = 스프링이 이 메서드를 호출할때 자동으로 넘겨주는 객체
                              정적자원(이미지, css, js 등) 요청 경로들을 등록하는 것
                              만약에 이런 URL 요청이 들어오면 아래와 같은 곳에서 데이터를 찾아서 클라이언트한테 전달해주자와 같은 규칙추가
    */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 클라이언트의 프로필사진을 업로드하는 폴더위치
        String 절대경로 = new File(uploadDir).getAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+절대경로+"/");
        /*
        file: 콜론의 유무
        프로토콜: 경로
        : 어떤 종류의 경로다를 알려주는 구분자이다.
        classpath: 프로젝트 안에서 컴파일된 파일에서 찾기
                    src/main/resources/
        file: 실제 컴퓨터의 디스크에서 데이터 찾기
        http: 인터넷 url에서 찾기

        file: 뒤에오는 문자열은 시스템 내부에 있는 파일 경로로 컴퓨터 디스크에서 해당 파일 경로의 데이터를 찾으라는 명령어이다.

        */

        // 사업자 고객이 등록한 상품 이미지를 업로드하는 폴더위치
        registry.addResourceHandler("/store/**")
                .addResourceLocations("file/사업자고객의 상품 이미지 실제 경로/");

        // 실제 나의 컴퓨터에 저장되어있는 폴더와 DB에 저장되어있는 저장위치를 다르게 함으로써 실제 데이터 위치를 안전하데 보관한다.
    }
}

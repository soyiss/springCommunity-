package com.domain.springCommunity.service;

import com.domain.springCommunity.dto.Member;
import com.domain.springCommunity.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
        private final MemberMapper memberMapper;
        private final PasswordEncoder passwordEncoder;

        // application.ymal에 file.upload_dir 값을 그대로 사용
        @Value("${file.upload-dir}")
        private String uploadDir; // application.yaml에 작성한 폴더 경로를 upLoadir 저장

        public void 회원가입기능(Member member, MultipartFile profileImg){

            // 클라이언트가 작성한 비밀번호를 암호화해서 SQL에 저장할 수 있도록 암호화 처리 작업
            // 소비자가 작성한 비밀번호를 가져와서 암호화 작업 후 암호화된 비밀번호를 암호화완료라는 변수에 넣음
            // 암호화처리된 비밀번호는 복구하여 사람의 글자로 변경 할 수 없다.
            // 비밀번호 5회 이상 틀릴 경우 계정이 잠기거나 일시정지되어 몇초 후 다시 비밀번호 입력하세요
            // BCrypto 암호화가 생성되며 비밀번호 찾기를 할 경우 에전에는 고객이 입력한 비번을 DB에서 가져올 수 있었지만
            // 암호화된 비밀번호를 복구 할 수 없기 때문에 새 비밀번호를 입력해야하는 현상이 발생함
            String 암호화완료 = passwordEncoder.encode(member.getPassword());
            member.setPassword(암호화완료); // 암호화완료된 변수로 비밀번호를 변경한 후 sql에 최종적으로 저장

            // 사용자 프로필 이미지를 첨부한 경우에만 이미지 업로드 처리
            if(profileImg != null && !profileImg.isEmpty()){
                String 저장된파일이름 = 프로필이미지저장(profileImg);
                member.setProfileImage(저장된파일이름);

            }

            memberMapper.insertMember(member);
        }

        private String 프로필이미지저장(MultipartFile file){
            // 여러사용자가 같은 이름으로 이미지를 올려도 겹치지 않도록 UUID 무작위 고유값을 파일이름에 붙여서 새파일이르믕로 생성하고 저장

            // file.getOriginalFilename(); 클라이언트가 업로드한 파일의 원래 이름 가져오기
            // UUID.randomUUID()는 절대 겹치지 않는 무작위 문자열을 생성
            // 원본파일 앞에 무작위로 생성한 문자열을 추가하여 새 이름을 만든다.(파일이름 덮어쓰기 되지 않도록 방지)
            // 시분초 타임스탬프로 랜덤이름을 부여하거나 UUID를 통해 생성한다.
            String 원본파일이름 = file.getOriginalFilename();
            String 저장파일이름 = UUID.randomUUID() + "-" + 원본파일이름;

            /*
             try-catch문:
             중간에 실패할 수 있는 코드 작업을 할 때 사용하는 블록
             동영상이나 이미지를 다운로드할 때 이미지 동영상 다운로드에 실패하였습니다.와 같이 단순 텍스트가 아닌
             파일 형태를 주고 받을 때는 실패에 따른 예외 상황 또한 개발자가 어떻게 처리할지 미리 파악하여 대비하는것
             */

            try {
                // 절대 경로 형태의 기준으로 이미지 저장하도록 세팅
                // 상대 경로의 경우 자바 웹에서 만들어지는 임시 폴더를 기준으로 해석되어서 엉뚱한 곳에 데이터가 저장 될 가능성이 있으므로,
                // 현재 프로젝트를 기준으로 고정적인 경로를 설정 해 주는 것.
                File 저장폴더 = new File(uploadDir).getAbsoluteFile();

                // 저장폴더가 없으면 새로 생성
                // 만약에 위 경로에서 지정한 위치에 폴더가 존재하지 않는게 사실이라면 mkdirs = makeDirectorys 폴더 여러개 생성하기
                // 0개 이상 무한적으로 폴더 생성이 가능하다.
                // mkdir 같은 경우 동일한 명칭의 폴더가 있으면 에러가 발생하지만
                // mkdirs 같은 경우 동일한 명칭의 폴더가 있으면 에러가 발생하는 것이 아니라 폴더 만들기 건너뛰고 다음 코딩 작업을 진행

                if(!저장폴더.exists()) 저장폴더.mkdirs();

                // 만들어진 폴더 + 클라이언트에서 가져온 파일이름에 UUID를 붙인 명칭을 합친 후
                File 저장파일 = new File(저장폴더, 저장파일이름);

                // 회사에서 프로필을 저장하는 폴더위치에 해당 파일을 저장한다.
                file.transferTo(저장파일); // 업로드 된 파일을 실제 서버 디스크에 저장

            }catch (IOException e){
                // 파일 저장하는 도중 문제가 생기면 개발자에게 문제가 생김을 고지
                throw new RuntimeException("프로필 이미지 업로드 중 오류가 발생했습니다.",e);

            }
                return 저장파일이름;
        }




        /* 로그인 기능
            아이디로 회원을 조회한 뒤 사용자가 입력한 비밀번호와 db에 암호화되어 저장된 비밀번호를 비교
            일치하면 회원정보를 반환하고, 일치하지 않으면 null을 반환한다.
        */
        public Member 로그인기능(Member member){

            //클라이언트가 작성한 id로 유저가 존재하는지 sql에서 조회
            Member db멤버 = memberMapper.findById(member.getId());

            // 아이디가 존재하지 않는 경우
            // if else for문 구문에서 {} 내부에 존재하는 코드가 한줄 일 경우 {}를 생략가능
            if (db멤버 == null) return  null;

            // id가 존재하고 입력한 비밀번호와 암호화된 비밀번호를 비교
            // matches(클라이언트가 작성한 pw, db에 저장된 암호화된 pw)
            // 클라이언트가 작성한 비밀번호를 암호화 처리하여 db에 저장된 비밀번호와 일치하는지 확인하고
            // 확인결과를 비밀번호 일치라는 변수에 담아서 저장한다.
            boolean 비밀번호일치 = passwordEncoder.matches(
                    member.getPassword() // 클라이언트가 작성한 pw
                    , db멤버.getPassword() // db에 저장되어있는 pw
            );

            // 비밀번호가 다르다면
            if(!비밀번호일치) return null;

            // 아이디에 존재하는 비밀번호가 맞다면
            return db멤버;

        }
}

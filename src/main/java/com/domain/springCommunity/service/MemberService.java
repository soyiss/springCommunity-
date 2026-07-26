package com.domain.springCommunity.service;

import com.domain.springCommunity.dto.Member;
import com.domain.springCommunity.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
        private final MemberMapper memberMapper;
        private final PasswordEncoder passwordEncoder;

        public void 회원가입기능(Member member){

            // 클라이언트가 작성한 비밀번호를 암호화해서 SQL에 저장할 수 있도록 암호화 처리 작업
            // 소비자가 작성한 비밀번호를 가져와서 암호화 작업 후 암호화된 비밀번호를 암호화완료라는 변수에 넣음
            // 암호화처리된 비밀번호는 복구하여 사람의 글자로 변경 할 수 없다.
            // 비밀번호 5회 이상 틀릴 경우 계정이 잠기거나 일시정지되어 몇초 후 다시 비밀번호 입력하세요
            // BCrypto 암호화가 생성되며 비밀번호 찾기를 할 경우 에전에는 고객이 입력한 비번을 DB에서 가져올 수 있었지만
            // 암호화된 비밀번호를 복구 할 수 없기 때문에 새 비밀번호를 입력해야하는 현상이 발생함
            String 암호화완료 = passwordEncoder.encode(member.getPassword());
            member.setPassword(암호화완료); // 암호화완료된 변수로 비밀번호를 변경한 후 sql에 최종적으로 저장
            memberMapper.insertMember(member);
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

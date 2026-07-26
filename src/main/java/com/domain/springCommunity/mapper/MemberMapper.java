package com.domain.springCommunity.mapper;

import com.domain.springCommunity.dto.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    // 회원가입 id, name, password 저장
    int insertMember (Member member);

    // id 중복 체크용 (선택 사용)
    // 이미 존재하는 id인지 확인
    int countById(String id);

    // 로그인시 아이디로 회원정보(암호화된 비밀번호 포함)조회
    Member findById(String id);

}

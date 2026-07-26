package com.domain.springCommunity.dto;

import lombok.*;

// @Data란  @Getter ~ @AllArgsConstructor 모든 기능을 포함한 어노테이션이다.
/*
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor*/

@Data
public class Member {

    private String id;
    private String name;
    private String password;


}

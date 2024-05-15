package br.com.sysmap.bootcamp.dto;

import lombok.*;

@Setter
@Getter
public class UsersDto {

    private Long id;
    private String name;
    private String email;
    private String password;

}

package br.com.sysmap.bootcamp.dto;

import br.com.sysmap.bootcamp.domain.entities.Users;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class AlbumDto {

    private Long id;
    private String name;
    private String idSpotify;
    private String artistName;
    private String imageUrl;
    private BigDecimal value;
    private Users users;

}

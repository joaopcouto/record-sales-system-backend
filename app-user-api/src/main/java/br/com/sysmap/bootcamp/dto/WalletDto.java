package br.com.sysmap.bootcamp.dto;

import br.com.sysmap.bootcamp.domain.entities.Users;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class WalletDto {

    private Long id;
    private BigDecimal balance;
    private Long points;
    private LocalDateTime lastUpdate;
    private Users users;

}

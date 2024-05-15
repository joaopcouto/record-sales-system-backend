package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.Optional;

@Named("WalletMapper")
@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);
    WalletDto toModel(Wallet returnWallet);
    default Optional<WalletDto> toModel(Optional<Wallet> wallet) {
        return wallet.map(this::toModel);
    }

}

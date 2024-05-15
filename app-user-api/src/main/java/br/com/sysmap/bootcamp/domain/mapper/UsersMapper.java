package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.dto.UsersDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Optional;

@Named("UsersMapper")
@Mapper
public interface UsersMapper {

    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersDto toModel(Users returnUsers);
    List<UsersDto> toModel(List<Users> returnAllUsers);
    default Optional<UsersDto> toModel(Optional<Users> user) {
        return user.map(this::toModel);
    }
}

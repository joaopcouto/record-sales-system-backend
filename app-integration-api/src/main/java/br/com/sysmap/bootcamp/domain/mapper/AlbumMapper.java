package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.util.List;
import java.util.Optional;

@Named("AlbumMapper")
@Mapper
public interface AlbumMapper {

    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    AlbumDto toModel(Album returnAlbum);
    List<AlbumDto> toModel(List<Album> returnAlbumsListOfUserAuthenticated);
    List<AlbumModel> toModel(AlbumSimplified[] returnAlbumSimplifieds);
    default Optional<AlbumDto> toModel(Optional<Album> album) {
        return album.map(this::toModel);
    }
}

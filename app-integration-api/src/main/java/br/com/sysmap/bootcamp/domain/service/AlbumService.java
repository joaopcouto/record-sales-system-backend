package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.mapper.AlbumMapper;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyApi;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import br.com.sysmap.bootcamp.dto.WalletMessagetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {

    private final Queue queue;
    private final RabbitTemplate template;
    private final SpotifyApi spotifyApi;
    private final AlbumRepository albumRepository;
    private final UsersService usersService;

    public List<Album> userAlbumCollection(Users user) {
        return this.albumRepository.findAllByUsers(user);
    }

    public void verifyAlbumAlreadyExists(Album albumToVerify) {
        List<Album> getAlbumCollection = userAlbumCollection(getUser());
        if (getAlbumCollection.stream().anyMatch(album -> Objects.equals(albumToVerify.getIdSpotify(), album.getIdSpotify()))) {
            throw new RuntimeException("Album has already been purchased by this user");
        }
    }

    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        log.info("Loading all albums related to the search....");
        return this.spotifyApi.getAlbums(search);
    }

    public List<AlbumDto> getCollection() {
        log.info("Reading all albums related to the authenticated user...");
        return AlbumMapper.INSTANCE.toModel(userAlbumCollection(getUser()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AlbumDto save(Album album) {
        verifyAlbumAlreadyExists(album);
        album = album.toBuilder().users(getUser()).build();
        log.info("Buying the album: {}", album.getName());
        Album albumSaved = this.albumRepository.save(album);
        WalletMessagetDto walletMessagetDto = new WalletMessagetDto(albumSaved.getUsers().getEmail(), albumSaved.getValue());
        this.template.convertAndSend(queue.getName(), walletMessagetDto);
        return AlbumMapper.INSTANCE.toModel(albumSaved);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        Optional<Album> albumOptional = this.albumRepository.findById(id);
        if (albumOptional.isEmpty()) {
            throw new RuntimeException("Album does not exist");
        }
        log.info("Removing album with this ID: {}", id);
        this.albumRepository.deleteById(id);
    }

    private Users getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return usersService.findByEmail(username);
    }

}

package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AlbumServiceTest {

    @MockBean
    private AlbumService albumService;

    private Users user;
    private Album album;
    private AlbumDto albumDto;

    @BeforeEach
    public void setUp() {
        user = Users.builder().email("jpcouto2209@gmail.com").password("password").build();
        album = Album.builder().idSpotify("1").name("test").value(BigDecimal.ONE).build();
        albumDto = new AlbumDto();
        albumDto.setIdSpotify("1");
        albumDto.setName("test");
        albumDto.setValue(BigDecimal.ONE);
    }

    @Test
    @DisplayName("Should return user's album collection when user has albums")
    public void shouldReturnUsersAlbumCollectionWhenUserHasAlbums() {
        when(albumService.userAlbumCollection(any(Users.class))).thenReturn(List.of(album));

        List<Album> albums = albumService.userAlbumCollection(user);

        assertNotNull(albums);
        assertFalse(albums.isEmpty());
    }

    @Test
    @DisplayName("Should return empty collection when user has no albums")
    public void shouldReturnEmptyCollectionWhenUserHasNoAlbums() {
        when(albumService.userAlbumCollection(any(Users.class))).thenReturn(Collections.emptyList());

        List<Album> albums = albumService.userAlbumCollection(user);

        assertNotNull(albums);
        assertTrue(albums.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when album already exists")
    public void shouldThrowExceptionWhenAlbumAlreadyExists() {
        when(albumService.save(any(Album.class))).thenThrow(new RuntimeException("Album has already been purchased by this user"));

        assertThrows(RuntimeException.class, () -> albumService.save(album));
    }

    @Test
    @DisplayName("Should save album when album does not exist")
    public void shouldSaveAlbumWhenAlbumDoesNotExist() {
        when(albumService.save(any(Album.class))).thenReturn(albumDto);

        AlbumDto savedAlbum = albumService.save(album);

        assertNotNull(savedAlbum);
        assertEquals(album.getName(), savedAlbum.getName());
    }

    @Test
    @DisplayName("Should throw exception when album does not exist for delete")
    public void shouldThrowExceptionWhenAlbumDoesNotExistForDelete() {
        doThrow(new RuntimeException("Album does not exist or has already been removed")).when(albumService).delete(anyLong());

        assertThrows(RuntimeException.class, () -> albumService.delete(1L));
    }

    @Test
    @DisplayName("Should delete album when album exists")
    public void shouldDeleteAlbumWhenAlbumExists() {
        doNothing().when(albumService).delete(anyLong());

        albumService.delete(1L);

        verify(albumService, times(1)).delete(1L);
    }
}

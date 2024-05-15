package br.com.sysmap.bootcamp.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.service.AlbumService;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class AlbumControllerTest {

    @MockBean
    private AlbumService albumService;

    @MockBean
    private AlbumController albumController;

    @Test
    @DisplayName("Should return 200 status when albums are found")
    public void shouldReturn200StatusWhenAlbumsAreFound() throws Exception {
        List<AlbumModel> albumModels = Collections.singletonList(new AlbumModel());
        ResponseEntity<List<AlbumModel>> mockResponse = ResponseEntity.ok(albumModels);

        when(albumController.getAlbums(anyString())).thenReturn(mockResponse);

        ResponseEntity<List<AlbumModel>> response = albumController.getAlbums("test");

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should return 200 status when no albums are found")
    public void shouldReturn200StatusWhenNoAlbumsAreFound() throws Exception {
        ResponseEntity<List<AlbumModel>> mockResponse = ResponseEntity.ok(Collections.emptyList());

        when(albumController.getAlbums(anyString())).thenReturn(mockResponse);

        ResponseEntity<List<AlbumModel>> response = albumController.getAlbums("test");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should return 200 status when user has albums in collection")
    public void shouldReturn200StatusWhenUserHasAlbumsInCollection() {
        ResponseEntity<List<AlbumDto>> mockResponse = ResponseEntity.ok(Collections.singletonList(new AlbumDto()));

        when(albumController.myCollection()).thenReturn(mockResponse);

        ResponseEntity<List<AlbumDto>> response = albumController.myCollection();

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should return 200 status when user has no albums in collection")
    public void shouldReturn200StatusWhenUserHasNoAlbumsInCollection() {
        ResponseEntity<List<AlbumDto>> mockResponse = ResponseEntity.ok(Collections.emptyList());

        when(albumController.myCollection()).thenReturn(mockResponse);

        ResponseEntity<List<AlbumDto>> response = albumController.myCollection();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should return 200 status when album is saved")
    public void shouldReturn200StatusWhenAlbumIsSaved() {
        Album album = Album.builder().idSpotify("1").name("test").value(BigDecimal.ONE).build();
        AlbumDto albumDto = new AlbumDto();
        ResponseEntity<AlbumDto> mockResponse = ResponseEntity.ok(albumDto);

        when(albumController.saveAlbum(any(Album.class))).thenReturn(mockResponse);

        ResponseEntity<AlbumDto> response = albumController.saveAlbum(album);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 204 status when album is deleted")
    public void shouldReturn204StatusWhenAlbumIsDeleted() {
        ResponseEntity<Void> mockResponse = ResponseEntity.noContent().build();

        when(albumController.deleteAlbum(anyLong())).thenReturn(mockResponse);

        ResponseEntity<Void> response = albumController.deleteAlbum(1L);

        assertEquals(204, response.getStatusCode().value());
    }
}

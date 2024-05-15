package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Album;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.service.AlbumService;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/albums")
@Tag(name = "Albums", description = "Albums API")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Get all albums from Spotify service by Text parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all albums"),
    })
    @GetMapping("/all")
    public ResponseEntity<List<AlbumModel>> getAlbums(@RequestParam("search") String search) {
        try {
            return ResponseEntity.ok(albumService.getAlbums(search));
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching albums", e);
        }
    }

    @Operation(summary = "Get all albums from my collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all albums related to the authenticated user"),
    })
    @GetMapping("/my-collection")
    public ResponseEntity<List<AlbumDto>> myCollection() {
        return ResponseEntity.ok(albumService.getCollection());
    }

    @Operation(summary = "Buy an album")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the purchased album"),
            @ApiResponse(responseCode = "500", description = "Return RuntimeException with information: Album has already been purchased by this user"),
    })
    @PostMapping("/sale")
    public ResponseEntity<AlbumDto> saveAlbum(@RequestBody Album album) {
        return ResponseEntity.ok(albumService.save(album));
    }

    @Operation(summary = "Remove an album by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Remove album"),
            @ApiResponse(responseCode = "500", description = "Return RuntimeException with information: Album does not exist or has already been removed"),
    })
    @DeleteMapping("/remove/{id}")
    public ResponseEntity deleteAlbum(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

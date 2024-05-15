package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.service.UsersService;
import br.com.sysmap.bootcamp.dto.AuthDto;
import br.com.sysmap.bootcamp.dto.UsersDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users API")
public class UsersController {

    private final UsersService usersService;

    @Operation(summary = "Save User", description = "Create a new user.")
    @PostMapping("/create")
    public ResponseEntity<UsersDto> save(@Valid @RequestBody Users user, UriComponentsBuilder uriBuilder) {
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(this.usersService.save(user));
    }

    @Operation(summary = "List users", description = "Search all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all users"),
    })
    @GetMapping
    public ResponseEntity<List<UsersDto>> readAll() {
        return ResponseEntity.ok(this.usersService.readAll());
    }

    @Operation(summary = "Update user", description = "Update user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the user that has been modified."),
            @ApiResponse(responseCode = "404", description = "There is no user with the provided ID")
    })
    @PutMapping("/update")
    public ResponseEntity<UsersDto> update(@Valid @RequestBody Users user) {
        Optional<UsersDto> userSaved = this.usersService.update(user);
        return userSaved.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by ID", description = "Search user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return user specified by the ID"),
            @ApiResponse(responseCode = "404", description = "There is no user with the provided ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> read(@PathVariable Long id) {
        Optional<UsersDto> user = this.usersService.readById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Auth user", description = "Sign in")
    @PostMapping("/auth")
    public ResponseEntity<AuthDto> auth(@RequestBody AuthDto user) {
        return ResponseEntity.ok(this.usersService.auth(user));
    }
}

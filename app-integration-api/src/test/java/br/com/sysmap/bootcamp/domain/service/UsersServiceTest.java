package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Users;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;

@SpringBootTest
public class UsersServiceTest {

    @MockBean
    private UsersService usersService;

    @Test
    @DisplayName("Should return UserDetails when user is found by email")
    public void shouldReturnUserDetailsWhenUserIsFoundByEmail() {
        Users user = Users.builder().email("jpcouto2209@gmail.com").password("password").build();
        UserDetails mockUserDetails = new User(user.getEmail(), user.getPassword(), new ArrayList<>());
        when(usersService.loadUserByUsername(anyString())).thenReturn(mockUserDetails);

        UserDetails userDetails = usersService.loadUserByUsername("test@test.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user is not found by email")
    public void shouldThrowUsernameNotFoundExceptionWhenUserIsNotFoundByEmail() {
        when(usersService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User with email: test@test.com not found"));

        assertThrows(UsernameNotFoundException.class, () -> usersService.loadUserByUsername("test@test.com"));
    }

    @Test
    @DisplayName("Should return Users when user is found by email")
    public void shouldReturnUsersWhenUserIsFoundByEmail() {
        Users user = Users.builder().email("jpcouto2209@gmail.com").password("password").build();
        when(usersService.findByEmail(anyString())).thenReturn(user);

        Users foundUser = usersService.findByEmail("test@test.com");

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Should return null when user is not found by email")
    public void shouldReturnNullWhenUserIsNotFoundByEmail() {
        when(usersService.findByEmail(anyString())).thenReturn(null);

        Users foundUser = usersService.findByEmail("test@test.com");

        assertNull(foundUser);
    }

}

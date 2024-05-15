package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.UsersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Users user;
    private Users savedUser;
    private Wallet wallet;

    @Test
    @DisplayName("Should save user and create wallet")
    void shouldSaveUserAndCreateWallet() {
        givenUser();
        givenSavedUser();
        givenWallet();

        when(passwordEncoder.encode(user.getPassword())).thenReturn(savedUser.getPassword());
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(usersRepository.save(any(Users.class))).thenReturn(savedUser);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        UsersDto savedDto = usersService.save(user);

        assertEquals(savedUser.getId(), savedDto.getId());
        assertEquals(savedUser.getName(), savedDto.getName());
        assertEquals(savedUser.getEmail(), savedDto.getEmail());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Should throw exception when user already exists")
    void shouldThrowExceptionWhenUserAlreadyExists() {
        givenUser();
        givenSavedUser();

        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(savedUser));

        assertThrows(RuntimeException.class, () -> usersService.save(user));
    }

    @Test
    @DisplayName("Should return all users")
    void shouldReturnAllUsers() {
        Users user1 = Users.builder().id(1L).email("jpcouto2209@gmail.com").name("Joao").password("password").build();
        Users user2 = Users.builder().id(2L).email("pedro@gmail.com").name("Pedro").password("password").build();
        List<Users> usersList = Arrays.asList(user1, user2);

        when(usersRepository.findAll()).thenReturn(usersList);

        List<UsersDto> usersDtoList = usersService.readAll();

        assertNotNull(usersDtoList);
        assertEquals(2, usersDtoList.size());
        assertEquals(user1.getName(), usersDtoList.get(0).getName());
        assertEquals(user2.getName(), usersDtoList.get(1).getName());
    }

    @Test
    @DisplayName("Should return user by id")
    void shouldReturnUserById() {
        givenUser();

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Continue your test here...
    }

    private void givenUser() {
        user = Users.builder().email("jpcouto2209@gmail.com").name("Joao").password("password").build();
    }

    private void givenSavedUser() {
        savedUser = Users.builder().id(1L).name("Joao").email("jpcouto2209@gmail.com").password("encodedPassword").build();
    }

    private void givenWallet() {
        wallet = Wallet.builder().balance(BigDecimal.valueOf(100)).lastUpdate(LocalDateTime.now()).points(0L).users(savedUser).build();
    }
}

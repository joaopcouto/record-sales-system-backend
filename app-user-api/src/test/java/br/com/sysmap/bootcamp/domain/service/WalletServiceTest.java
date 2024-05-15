package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.dto.WalletDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UsersService usersService;

    @Mock
    private WalletRepository walletRepository;


    @Test
    @DisplayName("Should return user's wallet")
    public void shouldReturnUsersWallet() {
        Users users = Users.builder().email("test").build();

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(200));
        wallet.setPoints(0L);

        when(usersService.findByEmail(anyString())).thenReturn(users);
        when(walletRepository.findByUsers(any(Users.class))).thenReturn(Optional.of(wallet));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(users);

        Optional<WalletDto> result = walletService.getUserWallet();

        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(200), result.get().getBalance());
        assertEquals(0L, result.get().getPoints());
    }

    @Test
    @DisplayName("Should add credit to user's wallet")
    public void shouldAddCreditToUsersWallet() {
        Users users = Users.builder().email("test").build();

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(200));
        wallet.setPoints(0L);

        when(usersService.findByEmail(anyString())).thenReturn(users);
        when(walletRepository.findByUsers(any(Users.class))).thenReturn(Optional.of(wallet));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(users);

        WalletDto result = walletService.addCreditToWallet(BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(300), result.getBalance());
    }

}

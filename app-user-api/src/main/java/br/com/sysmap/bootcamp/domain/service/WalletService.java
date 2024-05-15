package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.mapper.WalletMapper;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.WalletDto;
import br.com.sysmap.bootcamp.dto.WalletMessagetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalletService {

    private final UsersService usersService;
    private final WalletRepository walletRepository;

    @Transactional
    public WalletDto addCreditToWallet(BigDecimal value) {
        Optional<Wallet> userWallet = this.walletRepository.findByUsers(getUser());
        Wallet walletWithCreditAdded = userWallet.get().toBuilder()
                .balance(value.add(userWallet.get().getBalance()))
                .build();
        this.walletRepository.save(walletWithCreditAdded);
        return WalletMapper.INSTANCE.toModel(walletWithCreditAdded);
    }

    public String debit(WalletMessagetDto walletMessagetDto) {
        Users users = usersService.findByEmail(walletMessagetDto.getEmail());
        Optional<Wallet> wallet = walletRepository.findByUsers(users);

        wallet.get().setBalance(wallet.get().getBalance().subtract(walletMessagetDto.getValue()));
        wallet.get().setPoints(wallet.get().getPoints() + setPoints(LocalDate.now().getDayOfWeek()));
        wallet.get().setLastUpdate(LocalDateTime.now());

        log.info("Debiting {} from the wallet from the user {}, and add {} points to user...", walletMessagetDto.getValue(), users.getEmail(), wallet.get().getPoints());
        this.walletRepository.save(wallet.get());
        return "Sucess debiting value!";
    }

    public Optional<WalletDto> getUserWallet() {
        log.info("Reading wallet related to the authenticated user...");
        return WalletMapper.INSTANCE.toModel(walletRepository.findByUsers(getUser()));
    }

    private Users getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return usersService.findByEmail(username);
    }

    private Long setPoints(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case SUNDAY -> 25L;
            case MONDAY -> 7L;
            case TUESDAY -> 6L;
            case WEDNESDAY -> 2L;
            case THURSDAY -> 10L;
            case FRIDAY -> 15L;
            case SATURDAY -> 20L;
        };
    }
}

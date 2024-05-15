package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.Users;
import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.mapper.UsersMapper;
import br.com.sysmap.bootcamp.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.AuthDto;
import br.com.sysmap.bootcamp.dto.UsersDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    private final WalletRepository walletRepository;

    public void createWallet(Users user) {
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(100))
                .lastUpdate(LocalDateTime.now())
                .points(0L)
                .users(user)
                .build();
        this.walletRepository.save(wallet);
    }

    public void verifyUserAlreadyExists(Users userToVerify) {
        Optional<Users> usersOptional = this.usersRepository.findByEmail(userToVerify.getEmail());
        if (usersOptional.isPresent()) {
            throw new RuntimeException("User already exists");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersDto save(Users user) {
        verifyUserAlreadyExists(user);
        user = user.toBuilder().password(this.passwordEncoder.encode(user.getPassword())).build();
        log.info("Saving user: {}", user.getName());
        Users usersaved = this.usersRepository.save(user);
        createWallet(usersaved);
        return UsersMapper.INSTANCE.toModel(usersaved);
    }

    public List<UsersDto> readAll() {
        var listUsers = this.usersRepository.findAll();
        log.info("Reading all users... {}", listUsers.stream().map(Users::getName).toList());
        return UsersMapper.INSTANCE.toModel(listUsers);
    }

    public Optional<UsersDto> readById(Long id) {
        log.info("Reading user with id: {}", id);
        return UsersMapper.INSTANCE.toModel(this.usersRepository.findById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<UsersDto> update(Users user) {
        Optional<Users> userToUpdate = this.usersRepository.findById(user.getId());
        if(userToUpdate.isPresent()) {
            if(!userToUpdate.get().getEmail().equals(user.getEmail())) {
                verifyUserAlreadyExists(user);
            }
            log.info("Updating user with id: {}", user.getId());
            Users updatedUser = userToUpdate.get().toBuilder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(this.passwordEncoder.encode(user.getPassword()))
                    .build();
            this.usersRepository.save(updatedUser);
            return Optional.of(UsersMapper.INSTANCE.toModel(updatedUser));
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> usersOptional = this.usersRepository.findByEmail(username);

        return usersOptional.map(users -> new User(users.getEmail(), users.getPassword(), new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }

    public Users findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthDto auth(AuthDto authDto) {
        Users users = this.findByEmail(authDto.getEmail());

        if (!this.passwordEncoder.matches(authDto.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        StringBuilder password = new StringBuilder().append(users.getEmail()).append(":").append(users.getPassword());

        return AuthDto.builder().email(users.getEmail()).token(
                Base64.getEncoder().withoutPadding().encodeToString(password.toString().getBytes())
        ).id(users.getId()).build();
    }
}

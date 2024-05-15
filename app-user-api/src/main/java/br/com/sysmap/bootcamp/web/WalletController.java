package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.Wallet;
import br.com.sysmap.bootcamp.domain.service.WalletService;
import br.com.sysmap.bootcamp.dto.WalletDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
@Tag(name = "Wallet", description = "Wallet API")
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "My Wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Wallet related to the authenticated user"),
    })
    @GetMapping
    public ResponseEntity<Optional<WalletDto>> getMyWallet() {
        return ResponseEntity.ok(this.walletService.getUserWallet());
    }

    @Operation(summary = "Credit value in wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the wallet with the added value."),
    })
    @PostMapping("/credit/{value}")
    public ResponseEntity<WalletDto> updateWalletCredit(@PathVariable BigDecimal value) {
        return ResponseEntity.ok(this.walletService.addCreditToWallet(value));
    }
}

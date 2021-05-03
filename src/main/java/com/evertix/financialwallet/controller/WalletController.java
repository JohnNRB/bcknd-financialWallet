package com.evertix.financialwallet.controller;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.WalletRequest;
import com.evertix.financialwallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@Tag(name = "Wallet", description = "API is Ready")
@RequestMapping("api/wallet")
@RestController
public class WalletController {
    @Autowired
    WalletService walletService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Wallet", description = "View All Wallet",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> getAll() {
        return this.walletService.getAllWallet();
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Wallet by Type Wallet and Enterprise", description = "View All Wallet by Type Wallet and Enterprise",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> getAll(@RequestParam String typeWallet,
                                                  @RequestParam Long enterpriseId) {
        return this.walletService.getAllWallet(typeWallet, enterpriseId);
    }

    @GetMapping("/paged")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Wallet paginated by Type Wallet and Enterprise", description = "View All Wallet paginated by Type Wallet and Enterprise",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY
                            , description = "Page you want to retrieve (0..N)"
                            , name = "page"
                            , content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = ParameterIn.QUERY
                            , description = "Number of records per page."
                            , name = "size"
                            , content = @Content(schema = @Schema(type = "integer", defaultValue = "20"))),
            },
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> getAllPaginated(@PageableDefault @Parameter(hidden = true) Pageable pageable,
                                                           @RequestParam String typeWallet,
                                                           @RequestParam Long enterpriseId) {
        return this.walletService.getAllWalletPaginated(typeWallet, enterpriseId, pageable);
    }

    @GetMapping("/{walletId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View Wallet by ID", description = "View Wallet by ID",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> getAll(@PathVariable Long walletId) {
        return this.walletService.getWallet(walletId);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Wallet", description = "Add Wallet",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> add(@RequestBody @Valid WalletRequest wallet,
                                               @RequestParam String typeWallet,
                                               @RequestParam Long enterpriseId) {
        return this.walletService.addWallet(wallet, typeWallet, enterpriseId);
    }

    @PostMapping("/addDiscountsArray")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Discounts in Wallet with Array", description = "Add Discounts in Wallet with Array",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> addDiscountsArray(@RequestParam Long walletId,
                                                             @RequestBody List<Long> discountId) {
        return this.walletService.addDiscounts(walletId, discountId);
    }

    @PostMapping("/addDiscounts")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Discounts in Wallet", description = "Add Discounts in Wallet",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Wallet"})
    public ResponseEntity<MessageResponse> addDiscountsArray(@RequestParam Long walletId,
                                                             @RequestParam Long discountId) {
        return this.walletService.addDiscounts(walletId, discountId);
    }
}

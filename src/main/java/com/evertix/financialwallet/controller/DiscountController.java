package com.evertix.financialwallet.controller;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.DiscountRequest;
import com.evertix.financialwallet.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@Tag(name = "Discount", description = "API is Ready")
@RequestMapping("api/discount")
@RestController
public class DiscountController {
    @Autowired
    DiscountService discountService;

    @PostMapping("/add")
    // @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Discount", description = "Add Discount",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Discount"})
    public ResponseEntity<MessageResponse> add(@RequestBody @Valid DiscountRequest discount,
                                               @RequestParam Long rateId){
        return this.discountService.addDiscount(discount, rateId);
    }
}

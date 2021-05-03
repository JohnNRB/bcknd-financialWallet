package com.evertix.financialwallet.controller;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.service.EconomicActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Tag(name = "Economic Activity", description = "API is Ready")
@RequestMapping("api/economicActivity")
@RestController
public class EconomicActivityController {
    @Autowired
    EconomicActivityService economicActivityService;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Economic Activity", description = "View All Economic Activity",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Economic Activity"})
    public ResponseEntity<MessageResponse> getAll() {
        return this.economicActivityService.getAllEconomicActivities();
    }
}

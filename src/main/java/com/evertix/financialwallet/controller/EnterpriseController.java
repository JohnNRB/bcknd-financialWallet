package com.evertix.financialwallet.controller;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.EnterpriseRequest;
import com.evertix.financialwallet.service.EnterpriseService;
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

@CrossOrigin
@Tag(name = "Enterprise", description = "API is Ready")
@RequestMapping("api/enterprise")
@RestController
public class EnterpriseController {
    @Autowired
    EnterpriseService enterpriseService;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Enterprise by Manager", description = "View All Enterprise by Manager",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Business"})
    public ResponseEntity<MessageResponse> getAll(@RequestParam Long managerId) {
        return this.enterpriseService.getAllEnterprises(managerId);
    }

    @GetMapping("/paged")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Enterprise paginated by Manager", description = "View All Enterprise paginated by Manager",
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
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Enterprise"})
    public ResponseEntity<MessageResponse> getAllPaginated(@PageableDefault @Parameter(hidden = true) Pageable pageable,
                                                           @RequestParam Long managerId) {
        return this.enterpriseService.getAllEnterprisesPaginated(managerId, pageable);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Enterprise", description = "Add Enterprise",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Enterprise"})
    public ResponseEntity<MessageResponse> add(@RequestBody @Valid EnterpriseRequest enterprise,
                                               @RequestParam Long economicActivityId,
                                               @RequestParam Long managerId){
        return this.enterpriseService.addEnterprise(enterprise, economicActivityId, managerId);
    }

    @PutMapping("/{enterpriseId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Enterprise", description = "Update Enterprise",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Enterprise"})
    public ResponseEntity<MessageResponse> update(@RequestBody @Valid EnterpriseRequest enterprise,
                                                  @RequestParam(required = false) @Parameter(description = "is Optional") Long economicActivityId,
                                                  @PathVariable Long enterpriseId) {
        return this.enterpriseService.updateEnterprise(enterprise, economicActivityId, enterpriseId);
    }

    @DeleteMapping("/{enterpriseId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete Enterprise", description = "Delete Enterprise",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Enterprise"})
    public ResponseEntity<MessageResponse> delete(@PathVariable Long enterpriseId){
        return this.enterpriseService.deleteEnterprise(enterpriseId);
    }
}

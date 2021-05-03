package com.evertix.financialwallet.controller;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.model.request.RateRequest;
import com.evertix.financialwallet.service.RateService;
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
@Tag(name = "Rate", description = "API is Ready")
@RequestMapping("api/rate")
@RestController
public class RateController {
    @Autowired
    RateService rateService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Rate", description = "View All Rate",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> getAll(){
        return this.rateService.getAllRate();
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Rate by Type Rate", description = "View All Rate by Type Rate",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> getAll(@RequestParam String typeRate){
        return this.rateService.getAllRate(typeRate);
    }

    @GetMapping("/paged")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "View All Rate paginated by Type Rate", description = "View All Rate paginated by Type Rate",
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
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> getAllPaginated(@PageableDefault @Parameter(hidden = true) Pageable pageable,
                                                           @RequestParam String typeRate){
        return this.rateService.getAllRatePaginated(typeRate, pageable);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add Rate", description = "Add Rate",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> add(@RequestBody @Valid RateRequest rate,
                                               @RequestParam String typeRate) {
        return this.rateService.addRate(rate, typeRate);
    }

    @PutMapping("/{rateId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Rate", description = "Update Rate",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> update(@RequestBody @Valid RateRequest rate,
                                                  @PathVariable Long rateId) {
        return this.rateService.updateRate(rate, rateId);
    }

    @DeleteMapping("/{rateId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete Rate", description = "Delete Rate",
            security = @SecurityRequirement(name = "bearerAuth"), tags = {"Rate"})
    public ResponseEntity<MessageResponse> delete(@PathVariable Long rateId) {
        return this.rateService.deleteRate(rateId);
    }
}

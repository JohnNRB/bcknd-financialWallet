package com.evertix.financialwallet.model.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EnterpriseRequest {
    @Column(unique = true)
    @NotNull(message = "RUC cannot be null")
    @NotBlank(message = "RUC cannot be blank")
    @Size(min = 8, max = 11)
    private String ruc;

    @Column(unique = true)
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 150)
    private String name;

    @Column(unique = true)
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email")
    @Size(max = 100)
    private String email;

    @Column(unique = true)
    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200)
    private String address;

    @Column(unique = true)
    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be null")
    @Size(max = 12, min = 9)
    private String phone;
}
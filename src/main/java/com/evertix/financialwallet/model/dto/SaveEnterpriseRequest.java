package com.evertix.financialwallet.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveEnterpriseRequest {
    private String ruc;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String manageName;
    private String economicActivityName;
}

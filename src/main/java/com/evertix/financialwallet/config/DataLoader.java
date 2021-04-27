package com.evertix.financialwallet.config;

import com.evertix.financialwallet.model.EconomicActivity;
import com.evertix.financialwallet.model.Role;
import com.evertix.financialwallet.model.emuns.ERole;
import com.evertix.financialwallet.repository.EconomicActivityRepository;
import com.evertix.financialwallet.repository.RoleRepository;
import com.evertix.financialwallet.security.request.SignUpRequest;
import com.evertix.financialwallet.service.AuthService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final EconomicActivityRepository economicActivityRepository;

    public DataLoader(RoleRepository roleRepository, AuthService authService, EconomicActivityRepository economicActivityRepository) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.economicActivityRepository = economicActivityRepository;
        this.loadData();
    }

    private void loadData() {
        this.addRoles();
        this.addUsers();
        this.addEconomicActivities();
    }

    private void addEconomicActivities() {
        this.economicActivityRepository.saveAll(Arrays.asList(
                new EconomicActivity("Bebidas"),
                new EconomicActivity("Vehículos & Accesorios"),
                new EconomicActivity("Elaboración de productos Alimenticios"),
                new EconomicActivity("Agricultura, agroindustria y ganadería"),
                new EconomicActivity("Turismo, hoteles, restaurantes y entretenimiento"),
                new EconomicActivity("Servicios de sistemas, equipos de tecnología y comunicaciones")
        ));
    }

    private void addUsers() {
        SignUpRequest firstUser = new SignUpRequest("DHJesús", "password", "dh.jesus@gmail.com", "Jesus",
                "Duran Huancas", "77332215", "995588630");
        this.authService.registerUser(firstUser);

        SignUpRequest secondUser = new SignUpRequest("MSAlbert", "password", "ms.albert@gmail.com", "Albert",
                "Mayta Segovia", "71458215", "995459630");
        this.authService.registerUser(secondUser);
    }

    private void addRoles() {
        this.roleRepository.saveAll(Arrays.asList(
                new Role(ERole.ROLE_ADMIN),
                new Role(ERole.ROLE_USER)
        ));
    }
}

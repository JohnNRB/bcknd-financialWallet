package com.evertix.financialwallet.config;

import com.evertix.financialwallet.model.*;
import com.evertix.financialwallet.model.emuns.ERate;
import com.evertix.financialwallet.model.emuns.ERole;
import com.evertix.financialwallet.model.emuns.EWallet;
import com.evertix.financialwallet.repository.*;
import com.evertix.financialwallet.security.request.SignUpRequest;
import com.evertix.financialwallet.service.AuthService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final EconomicActivityRepository economicActivityRepository;
    private final TypeRateRepository typeRateRepository;
    private final RateRepository rateRepository;
    private final TypeWalletRepository typeWalletRepository;

    public DataLoader(RoleRepository roleRepository, AuthService authService, EconomicActivityRepository economicActivityRepository,
                      TypeRateRepository typeRateRepository, RateRepository rateRepository, TypeWalletRepository typeWalletRepository) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.economicActivityRepository = economicActivityRepository;
        this.typeRateRepository = typeRateRepository;
        this.rateRepository = rateRepository;
        this.typeWalletRepository = typeWalletRepository;
        this.loadData();
    }

    private void loadData() {
        this.addRoles();
        this.addUsers();
        this.addEconomicActivities();
        this.addRates();
        this.addExample();
        this.addWallets();
    }

    private void addWallets() {
        this.typeWalletRepository.saveAll(Arrays.asList(
                new TypeWallet(EWallet.WALLET_LETTERS),
                new TypeWallet(EWallet.WALLET_BILLS),
                new TypeWallet(EWallet.WALLET_RECEIPTS_OF_HONORARY)
        ));
    }

    private void addExample() {
        double rate = 14585;
        double n = 150;
        double a = rate/n;
        this.rateRepository.saveAll(Arrays.asList(
                new Rate(360, "Anual", 360, new BigDecimal(a).setScale(2, RoundingMode.HALF_EVEN),
                        "Mensual", 30, LocalDate.parse("2020-08-30"))
        ));
    }

    private void addRates() {
        this.typeRateRepository.saveAll(Arrays.asList(
                new TypeRate(ERate.RATE_NOMINAL),
                new TypeRate(ERate.RATE_EFFECTIVE)
        ));
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

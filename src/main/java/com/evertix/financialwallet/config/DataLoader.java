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
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final EconomicActivityRepository economicActivityRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final TypeRateRepository typeRateRepository;
    private final RateRepository rateRepository;
    private final TypeWalletRepository typeWalletRepository;

    public DataLoader(RoleRepository roleRepository, AuthService authService, UserRepository userRepository,
                      EconomicActivityRepository economicActivityRepository, EnterpriseRepository enterpriseRepository,
                      TypeRateRepository typeRateRepository, RateRepository rateRepository, TypeWalletRepository typeWalletRepository) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.economicActivityRepository = economicActivityRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.typeRateRepository = typeRateRepository;
        this.rateRepository = rateRepository;
        this.typeWalletRepository = typeWalletRepository;
        this.loadData();
    }

    private void loadData() {
        this.addRoles();
        this.addUsers();
        this.addEconomicActivities();
        this.addEnterprises();
        this.addTypeRates();
        this.addRates();
        this.addWallets();
    }

    private void addWallets() {
        this.typeWalletRepository.saveAll(Arrays.asList(
                new TypeWallet(EWallet.WALLET_LETTERS),
                new TypeWallet(EWallet.WALLET_BILLS),
                new TypeWallet(EWallet.WALLET_RECEIPTS_OF_HONORARY)
        ));
    }

    private void addRates() {
        TypeRate typeRateNominal = typeRateRepository.findByName(ERate.RATE_NOMINAL).orElse(null);
        Rate rateNominal = new Rate(360, "Anual", 360, new BigDecimal(25), "Mensual",
                30, LocalDate.parse("2021-04-21"));
        rateNominal.setTypeRate(typeRateNominal);
        this.rateRepository.save(rateNominal);

        TypeRate typeRateEffective = typeRateRepository.findByName(ERate.RATE_EFFECTIVE).orElse(null);
        Rate rateEffective = new Rate(360, "Anual", 360, new BigDecimal(25), "null",
                0, LocalDate.parse("2021-04-21"));
        rateEffective.setTypeRate(typeRateEffective);
        this.rateRepository.save(rateEffective);
    }

    private void addTypeRates() {
        this.typeRateRepository.saveAll(Arrays.asList(
                new TypeRate(ERate.RATE_NOMINAL),
                new TypeRate(ERate.RATE_EFFECTIVE)
        ));
    }

    private void addEnterprises() {
        Enterprise firstEnterprise = new Enterprise("145896532", "Coca Cola SAC", "coca.cola@gmail.com", "AV. Arequipa",
                "940178956");
        firstEnterprise.setEconomicActivity(economicActivityRepository.findByName("Bebidas").orElse(null));
        firstEnterprise.setManager(userRepository.findByUsername("MSAlbert").orElse(null));
        enterpriseRepository.save(firstEnterprise);
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

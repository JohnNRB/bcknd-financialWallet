package com.evertix.financialwallet.service.impl;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.controller.constants.ResponseConstants;
import com.evertix.financialwallet.model.Discount;
import com.evertix.financialwallet.model.Enterprise;
import com.evertix.financialwallet.model.TypeWallet;
import com.evertix.financialwallet.model.Wallet;
import com.evertix.financialwallet.model.dto.SaveWalletRequest;
import com.evertix.financialwallet.model.emuns.EWallet;
import com.evertix.financialwallet.model.request.WalletRequest;
import com.evertix.financialwallet.repository.DiscountRepository;
import com.evertix.financialwallet.repository.EnterpriseRepository;
import com.evertix.financialwallet.repository.TypeWalletRepository;
import com.evertix.financialwallet.repository.WalletRepository;
import com.evertix.financialwallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    TypeWalletRepository typeWalletRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    WalletRepository walletRepository;

    @Override
    public ResponseEntity<MessageResponse> getAllWallet() {
        try {
            List<Wallet> walletList = this.walletRepository.findAll();
            if (walletList.isEmpty()) { return this.getNotWalletContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(walletList)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getWallet(Long walletId) {
        try {
            Wallet wallet = this.walletRepository.findById(walletId).orElse(null);
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(wallet)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getAllWallet(String typeWallet, Long enterpriseId) {
        try {
            // Identify Type Wallet
            EWallet wallet;
            if (typeWallet == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Sorry, Type Wallet not found")
                                .build());
            } else {
                switch (typeWallet) {
                    case "WALLET_LETTERS":
                        wallet = EWallet.WALLET_LETTERS;
                        break;
                    case "WALLET_BILLS":
                        wallet = EWallet.WALLET_BILLS;
                        break;
                    case "WALLET_RECEIPTS_OF_HONORARY":
                        wallet = EWallet.WALLET_RECEIPTS_OF_HONORARY;
                        break;
                    default: throw new RuntimeException("Sorry, Type Wallet is wrong.");
                }
            }

            List<Wallet> walletList = this.walletRepository.findAllByTypeWalletNameAndEnterpriseId(wallet, enterpriseId);
            if (walletList == null || walletList.isEmpty()) { return this.getNotWalletContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(walletList)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getAllWalletPaginated(String typeWallet, Long enterpriseId, Pageable pageable) {
        try {
            // Identify Type Wallet
            EWallet wallet;
            if (typeWallet == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Sorry, Type Wallet not found")
                                .build());
            } else {
                switch (typeWallet) {
                    case "WALLET_LETTERS":
                        wallet = EWallet.WALLET_LETTERS;
                        break;
                    case "WALLET_BILLS":
                        wallet = EWallet.WALLET_BILLS;
                        break;
                    case "WALLET_RECEIPTS_OF_HONORARY":
                        wallet = EWallet.WALLET_RECEIPTS_OF_HONORARY;
                        break;
                    default: throw new RuntimeException("Sorry, Type Wallet is wrong.");
                }
            }

            Page<Wallet> walletPage = this.walletRepository.findAllByTypeWalletNameAndEnterpriseId(wallet, enterpriseId, pageable);
            if (walletPage == null || walletPage.isEmpty()) { return this.getNotWalletContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(walletPage)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addWallet(WalletRequest wallet, String typeWallet, Long enterpriseId) {
        try {
            // Validate if Enterprise Exists
            Enterprise enterprise = this.enterpriseRepository.findById(enterpriseId).orElse(null);
            if (enterprise == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists enterprise with ID: " + enterpriseId)
                                .build());
            }

            // Identify Type Wallet
            TypeWallet typeWallets;
            if (typeWallet == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Sorry, Type Wallet not found")
                                .build());
            } else {
                switch (typeWallet) {
                    case "WALLET_LETTERS":
                        typeWallets = typeWalletRepository.findAllByName(EWallet.WALLET_LETTERS)
                                .orElseThrow(() -> new RuntimeException("Sorry, Type Wallet not found"));
                        break;
                    case "WALLET_BILLS":
                        typeWallets = typeWalletRepository.findAllByName(EWallet.WALLET_BILLS)
                                .orElseThrow(() -> new RuntimeException("Sorry, Type Wallet not found"));
                        break;
                    case "WALLET_RECEIPTS_OF_HONORARY":
                        typeWallets = typeWalletRepository.findAllByName(EWallet.WALLET_RECEIPTS_OF_HONORARY)
                                .orElseThrow(() -> new RuntimeException("Sorry, Type Wallet not found"));
                        break;
                    default: throw new RuntimeException("Sorry, Type Wallet is wrong.");
                }
            }

            // Complete Validation
            Wallet saveWallet = this.convertToEntity(wallet);
            // Set Type Wallet & Enterprise
            saveWallet.setTypeWallet(typeWallets);
            saveWallet.setEnterprise(enterprise);
            // Initial Total Value Delivered & Total Days Period
            saveWallet.setValueTotalDelivered(new BigDecimal(0));
            saveWallet.setDaysTotalPeriod(0);
            // Save Wallet
            walletRepository.save(saveWallet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful creation request")
                            .data(this.convertToResource(saveWallet))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addDiscounts(Long walletId, List<Long> discountsId) {
        try {
            // Validate if Wallet Exists
            Wallet wallet = this.walletRepository.findById(walletId).orElse(null);
            if (wallet == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists wallet with ID: " + walletId)
                                .build());
            }

            List<Discount> discountList = new ArrayList<>();
            for (Long discountId: discountsId) {
                Discount discount = this.discountRepository.findById(discountId).orElse(null);
                if (!wallet.getDiscounts().contains(discount)) { discountList.add(discount); }
                // Set Array's Discount
                wallet.setDiscounts(discountList);
                if (discount != null) { wallet.setValueTotalDelivered(wallet.getValueTotalReceived().add(discount.getValueReceived())); }
                // Save All
                walletRepository.save(wallet);
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful Add Discounts")
                            .data(this.convertToResource(wallet))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addDiscounts(Long walletId, Long discountId) {
        try {
            // Validate if Wallet Exists
            Wallet wallet = this.walletRepository.findById(walletId).orElse(null);
            if (wallet == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists wallet with ID: " + walletId)
                                .build());
            }
            // List<Discount> discountList = new ArrayList<>();
            BigDecimal valueTCEA;
            Discount discount = this.discountRepository.findById(discountId).orElse(null);
            if (!wallet.getDiscounts().contains(discount)) { wallet.getDiscounts().add(discount); }
            if (discount != null) {
                // Set Days Total Period
                wallet.setDaysTotalPeriod(wallet.getDaysTotalPeriod() + discount.getDaysPeriod());
                // Set Value Total Received
                wallet.setValueTotalReceived(wallet.getValueTotalReceived().add(discount.getValueReceived()));
                // Set Value Total Delivered
                wallet.setValueTotalDelivered(wallet.getValueTotalDelivered().add(discount.getValueDelivered()));
                // Set Total TCEA
                valueTCEA = this.TotalTCEA(wallet.getValueTotalReceived(), wallet.getValueTotalDelivered(), wallet.getDaysTotalPeriod(), discount.getRate().getDaysYear());
                wallet.setValueTCEA(valueTCEA.setScale(7, RoundingMode.HALF_EVEN));
            }
            walletRepository.save(wallet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful Add Discounts")
                            .data(this.convertToResource(wallet))
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    private BigDecimal TotalTCEA(BigDecimal valueReceived, BigDecimal valueDelivered, Integer daysPeriod, Integer daysYear) {
        double firstStep = daysYear.doubleValue() / daysPeriod.doubleValue();
        double secondStep = valueDelivered.doubleValue() / valueReceived.doubleValue();
        double TCEA = (Math.pow(secondStep, firstStep) - 1) * 100;
        return new BigDecimal(TCEA);
    }

    private Wallet convertToEntity(WalletRequest wallet) { return modelMapper.map(wallet, Wallet.class); }

    private SaveWalletRequest convertToResource(Wallet wallet) {
        SaveWalletRequest resource = modelMapper.map(wallet, SaveWalletRequest.class);
        resource.setEnterpriseName(wallet.getEnterprise().getName());
        resource.setTypeWalletName(wallet.getTypeWallet().getName().toString());
        return resource;
    }

    private ResponseEntity<MessageResponse> getNotWalletContent() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .code(ResponseConstants.WARNING_CODE)
                        .message(ResponseConstants.MSG_WARNING_CONS)
                        .data(null)
                        .build());
    }
}

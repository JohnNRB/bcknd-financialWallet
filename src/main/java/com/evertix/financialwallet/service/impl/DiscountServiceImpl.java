package com.evertix.financialwallet.service.impl;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.controller.constants.ResponseConstants;
import com.evertix.financialwallet.model.Discount;
import com.evertix.financialwallet.model.Rate;
import com.evertix.financialwallet.model.dto.SaveDiscountRequest;
import com.evertix.financialwallet.model.request.DiscountRequest;
import com.evertix.financialwallet.repository.DiscountRepository;
import com.evertix.financialwallet.repository.RateRepository;
import com.evertix.financialwallet.service.DiscountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RateRepository rateRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public ResponseEntity<MessageResponse> getAllDiscount() {
        try {
            List<Discount> discountList = this.discountRepository.findAll();
            if (discountList.isEmpty()) { this.getNotDiscountContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(discountList)
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
    public ResponseEntity<MessageResponse> addDiscount(DiscountRequest discount, Long rateId) {
        try {
            // Validate if Rate Exists
            Rate rate = this.rateRepository.findById(rateId).orElse(null);
            if (rate == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists rate with ID: " + rateId)
                                .build());
            }

            // Validate Complete
            Discount saveDiscount = this.convertToEntity(discount);
            // Financial Operation
            financialOperation(discount, saveDiscount, rate);
            // Set Rate
            saveDiscount.setRate(rate);
            // Save Discount
            discountRepository.save(saveDiscount);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful creation request")
                            .data(this.convertToResource(saveDiscount))
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
    public void financialOperation(DiscountRequest request, Discount discount, Rate rate) {
        Integer daysPeriod;
        BigDecimal rateEffective, rateDiscount, TCEA;
        BigDecimal valueDiscount, valueNet, valueReceived, valueDelivered;
        switch (rate.getTypeRate().getName().toString()) {
            case "RATE_NOMINAL":
                daysPeriod = daysPeriod(request.getExpirationAt(), rate.getDiscountAt());
                discount.setDaysPeriod(daysPeriod);

                rateEffective = rateEffectiveNominal(daysPeriod, rate.getValueRate(), rate.getDaysRate(), rate.getDaysCapitalization());
                discount.setRateEffective(rateEffective.setScale(7, RoundingMode.HALF_EVEN));

                rateDiscount = rateDiscount(rateEffective);
                discount.setRateDiscount(rateDiscount.setScale(7, RoundingMode.HALF_EVEN));

                valueDiscount = valueDiscount(request.getValueNominal(), rateDiscount);
                discount.setValueDiscount(valueDiscount.setScale(2, RoundingMode.HALF_EVEN));

                valueNet = valueNet(request.getValueNominal(), valueDiscount);
                discount.setValueNet(valueNet.setScale(2, RoundingMode.HALF_EVEN));

                valueReceived = valueReceived(valueNet, request.getRetention());
                discount.setValueReceived(valueReceived.setScale(2, RoundingMode.HALF_EVEN));

                valueDelivered = valueDelivered(request.getValueNominal(), request.getRetention());
                discount.setValueDelivered(valueDelivered.setScale(2, RoundingMode.HALF_EVEN));

                TCEA = TCEA(valueReceived.setScale(2, RoundingMode.HALF_EVEN),
                        valueDelivered.setScale(2, RoundingMode.HALF_EVEN), daysPeriod, rate.getDaysYear());
                discount.setTCEA(TCEA.setScale(7, RoundingMode.HALF_EVEN));
                break;
            case "RATE_EFFECTIVE":
                daysPeriod = daysPeriod(request.getExpirationAt(), rate.getDiscountAt());
                discount.setDaysPeriod(daysPeriod);

                rateEffective = rateEffective(daysPeriod, rate.getValueRate());
                discount.setRateEffective(rateEffective.setScale(7, RoundingMode.HALF_EVEN));

                rateDiscount = rateDiscount(rateEffective);
                discount.setRateDiscount(rateDiscount.setScale(7, RoundingMode.HALF_EVEN));

                valueDiscount = valueDiscount(request.getValueNominal(), rateDiscount);
                discount.setValueDiscount(valueDiscount.setScale(2, RoundingMode.HALF_EVEN));

                valueNet = valueNet(request.getValueNominal(), valueDiscount);
                discount.setValueNet(valueNet.setScale(2, RoundingMode.HALF_EVEN));

                valueReceived = valueReceived(valueNet, request.getRetention());
                discount.setValueReceived(valueReceived.setScale(2, RoundingMode.HALF_EVEN));

                valueDelivered = valueDelivered(request.getValueNominal(), request.getRetention());
                discount.setValueDelivered(valueDelivered.setScale(2, RoundingMode.HALF_EVEN));

                TCEA = TCEA(valueReceived.setScale(2, RoundingMode.HALF_EVEN),
                        valueDelivered.setScale(2, RoundingMode.HALF_EVEN), daysPeriod, rate.getDaysYear());
                discount.setTCEA(TCEA.setScale(7, RoundingMode.HALF_EVEN));
                break;
            default: break;
        }
    }

    private Discount convertToEntity(DiscountRequest discount) { return modelMapper.map(discount, Discount.class); }

    private SaveDiscountRequest convertToResource(Discount discount) {
        SaveDiscountRequest resource = modelMapper.map(discount, SaveDiscountRequest.class);
        resource.setRateName(discount.getRate().getTypeRate().getName().toString());
        return resource;
    }

    private ResponseEntity<MessageResponse> getNotDiscountContent(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .code(ResponseConstants.WARNING_CODE)
                        .message(ResponseConstants.MSG_WARNING_CONS)
                        .data(null)
                        .build());
    }

    @Override
    public Integer daysPeriod(LocalDate expiration, LocalDate discount) {
        long daysPeriod = DAYS.between(discount, expiration);
        return (int) daysPeriod;
    }

    @Override
    public BigDecimal rateEffective(Integer daysPeriod, BigDecimal valueRate) {
        double firstStep = daysPeriod.doubleValue() / 360;
        double secondStep = (valueRate.doubleValue() / 100) + 1;
        double rateEffective = (Math.pow(secondStep, firstStep) - 1) * 100;
        return new BigDecimal(rateEffective);
    }

    @Override
    public BigDecimal rateEffectiveNominal(Integer daysPeriod, BigDecimal valueRate, Integer daysRate, Integer daysCapitalization) {
        double convertDaysRate = daysRate.doubleValue() / daysCapitalization.doubleValue();
        double convertDaysPeriod = daysPeriod.doubleValue() / daysCapitalization.doubleValue();

        double firstStep = ((valueRate.doubleValue() / 100) / convertDaysRate) + 1;
        double rateEffectiveNominal = (Math.pow(firstStep, convertDaysPeriod) - 1) * 100;
        return new BigDecimal(rateEffectiveNominal);
    }

    @Override
    public BigDecimal rateDiscount(BigDecimal rateEffective) {
        double rateDiscount = ((rateEffective.doubleValue() / 100) / (1 + (rateEffective.doubleValue() / 100))) * 100;
        return new BigDecimal(rateDiscount);
    }

    @Override
    public BigDecimal valueDiscount(BigDecimal valueNominal, BigDecimal rateDiscount) {
        double valueDiscount = valueNominal.doubleValue() * (rateDiscount.doubleValue() / 100);
        return new BigDecimal(valueDiscount);
    }

    @Override
    public BigDecimal valueNet(BigDecimal valueNominal, BigDecimal valueDiscount) {
        double valueNet = valueNominal.doubleValue() - valueDiscount.doubleValue();
        return new BigDecimal(valueNet);
    }

    @Override
    public BigDecimal valueReceived(BigDecimal valueNet, BigDecimal retention) {
        double valueReceived = valueNet.doubleValue() - retention.doubleValue();
        return new BigDecimal(valueReceived);
    }

    @Override
    public BigDecimal valueDelivered(BigDecimal valueNominal, BigDecimal retention) {
        double valueDelivered = valueNominal.doubleValue() - retention.doubleValue();
        return new BigDecimal(valueDelivered);
    }

    @Override
    public BigDecimal TCEA(BigDecimal valueReceived, BigDecimal valueDelivered, Integer daysPeriod, Integer daysYear) {
        double firstStep = daysYear.doubleValue() / daysPeriod.doubleValue();
        double secondStep = valueDelivered.doubleValue() / valueReceived.doubleValue();
        double TCEA = (Math.pow(secondStep, firstStep) - 1) * 100;
        return new BigDecimal(TCEA);
    }
}

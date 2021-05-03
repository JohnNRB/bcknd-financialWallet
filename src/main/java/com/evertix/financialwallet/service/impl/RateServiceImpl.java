package com.evertix.financialwallet.service.impl;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.controller.constants.ResponseConstants;
import com.evertix.financialwallet.model.Rate;
import com.evertix.financialwallet.model.TypeRate;
import com.evertix.financialwallet.model.dto.SaveRateRequest;
import com.evertix.financialwallet.model.emuns.ERate;
import com.evertix.financialwallet.model.request.RateRequest;
import com.evertix.financialwallet.repository.RateRepository;
import com.evertix.financialwallet.repository.TypeRateRepository;
import com.evertix.financialwallet.service.RateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TypeRateRepository typeRateRepository;

    @Autowired
    RateRepository rateRepository;

    @Override
    public ResponseEntity<MessageResponse> getAllRate() {
        try {
            List<Rate> rateList = this.rateRepository.findAll();
            if (rateList.isEmpty()) { return this.getNotRateContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(rateList)
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
    public ResponseEntity<MessageResponse> getAllRate(String typeRateName) {
        try {
            // Identify Type Rate
            ERate typeRate;
            switch (typeRateName) {
                case "RATE_NOMINAL": typeRate = ERate.RATE_NOMINAL; break;
                case "RATE_EFFECTIVE": typeRate = ERate.RATE_EFFECTIVE; break;
                default: throw new RuntimeException("Not fount Type Rate");
            }

            List<Rate> rateList = this.rateRepository.findAllByTypeRateName(typeRate);
            if (rateList == null || rateList.isEmpty()) { return this.getNotRateContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(rateList)
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
    public ResponseEntity<MessageResponse> getAllRatePaginated(String typeRateName, Pageable pageable) {
        try {
            // Identify Type Rate
            ERate typeRate;
            switch (typeRateName) {
                case "RATE_NOMINAL": typeRate = ERate.RATE_NOMINAL; break;
                case "RATE_EFFECTIVE": typeRate = ERate.RATE_EFFECTIVE; break;
                default: throw new RuntimeException("Not fount Type Rate");
            }

            Page<Rate> ratePage = this.rateRepository.findAllByTypeRateName(typeRate, pageable);
            if (ratePage == null || ratePage.isEmpty()) { return this.getNotRateContent(); }
            MessageResponse response = MessageResponse.builder()
                    .code(ResponseConstants.SUCCESS_CODE)
                    .message(ResponseConstants.MSG_SUCCESS_CONS)
                    .data(ratePage)
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
    public ResponseEntity<MessageResponse> addRate(RateRequest rate, String typeRateName) {
        try {
            // Create New Rate
            Rate saveRate = this.convertToEntity(rate);

            // Identify Type Rate
            TypeRate typeRate;
            if (typeRateName == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Sorry, Type Rate not found")
                                .build());
            } else {
                switch (typeRateName) {
                    case "RATE_NOMINAL":
                        typeRate = typeRateRepository.findByName(ERate.RATE_NOMINAL)
                                .orElseThrow(() -> new RuntimeException("Sorry, Type Rate not found"));
                        break;
                    case "RATE_EFFECTIVE":
                        typeRate = typeRateRepository.findByName(ERate.RATE_EFFECTIVE)
                                .orElseThrow(() -> new RuntimeException("Sorry, Type Rate not found"));
                        break;
                    default:
                        throw new RuntimeException("Sorry, Type Rate is wrong.");
                }
            }

            // Set Type Rate
            saveRate.setTypeRate(typeRate);
            // Save Rate
            rateRepository.save(saveRate);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful creation request")
                            .data(this.convertToResource(saveRate))
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
    public ResponseEntity<MessageResponse> updateRate(RateRequest rate, Long rateId) {
        try {
            // Validate if Rate Exists
            Rate saveRate = this.rateRepository.findById(rateId).orElse(null);
            if (saveRate == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Don't exists rate with ID: " + rateId)
                                .build());
            }

            // Update Rate Data
            saveRate.setDaysRate(rate.getDaysRate());
            saveRate.setPeriodRate(rate.getPeriodRate());
            saveRate.setDaysRate(rate.getDaysRate());
            saveRate.setValueRate(rate.getValueRate());
            saveRate.setDiscountAt(rate.getDiscountAt());
            if (saveRate.getTypeRate().getName().toString().equals("RATE_NOMINAL")){
                saveRate.setPeriodCapitalization(rate.getPeriodCapitalization());
                saveRate.setDaysCapitalization(rate.getDaysCapitalization());
            }
            // Save Update
            saveRate = rateRepository.save(saveRate);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful update")
                            .data(this.convertToResource(saveRate))
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
    public ResponseEntity<MessageResponse> deleteRate(Long rateId) {
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

            // Delete Rate
            rateRepository.delete(rate);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful delete")
                            .data(this.convertToResource(rate))
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

    private Rate convertToEntity(RateRequest rate) { return modelMapper.map(rate, Rate.class); }

    private SaveRateRequest convertToResource(Rate rate) {
        SaveRateRequest resource = modelMapper.map(rate, SaveRateRequest.class);
        resource.setTypeRateName(rate.getTypeRate().getName().toString());
        return resource;
    }

    private ResponseEntity<MessageResponse> getNotRateContent(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .code(ResponseConstants.WARNING_CODE)
                        .message(ResponseConstants.MSG_WARNING_CONS)
                        .data(null)
                        .build());
    }
}

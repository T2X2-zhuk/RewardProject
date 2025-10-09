package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.configCache.GetAllPaymentsUsingCache;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.validators.GetPaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SearchPaymentService {

    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;
    private final GetPaymentValidator validator;

    public CommonResponseForPaymentParameters execute(CommonRequestForPaymentParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            buildResponseWithoutErrors(response,request);
        }else {
            log.warn("Validation failed errors : {}" , errors);
            response.setErrors(errors);
        }
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    private void buildResponseWithoutErrors(CommonResponseForPaymentParameters response,CommonRequestForPaymentParameters request){
        List<Payment>  payments = getPaymentsByEmployeeAndAmount(request.getPaymentDTO().getEmployeeId(), request.getPaymentDTO().getAmount());
        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        for(Payment payment : payments){
            paymentDTOS.add(PaymentDTO.builder().id(payment.getId())
                    .employeeId(payment.getEmployeeId())
                    .amount(payment.getAmount()).build());
        }
        response.setPaymentDTOS(paymentDTOS);
        log.info("Payment : {} was successful received", paymentDTOS);
    }

    private List<Payment> getPaymentsByEmployeeAndAmount(Long employeeId, BigDecimal amount) {
        return getAllPaymentsUsingCache.getAllPaymentsWithCache().stream()
                .filter(p -> p.getEmployeeId().equals(employeeId)
                        && p.getAmount().compareTo(amount) == 0)
                .collect(Collectors.toList());
    }
}

package rewardPayment.services;

import rewardPayment.JPA.domain.Payment;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.responses.CommonResponseForPaymentParameters;
import rewardPayment.util.forErrors.ValidationError;
import rewardPayment.validations.validators.GetPaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchPaymentService {

    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;
    private final GetPaymentValidator validator;

    @Transactional
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
        List<Payment>  payments = getPaymentsByEmployeeAndAmount(request.getPaymentDTO().getEmployeeId());
        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        for(Payment payment : payments){
            paymentDTOS.add(PaymentDTO.builder().id(payment.getId())
                    .employeeId(payment.getEmployeeId())
                    .amount(payment.getAmount()).build());
        }
        response.setPaymentDTOS(paymentDTOS);
        log.info("Payment : {} was successful received", paymentDTOS);
    }

    private List<Payment> getPaymentsByEmployeeAndAmount(Long employeeId) {
        return getAllPaymentsUsingCache.getAllPaymentsWithCache().stream()
                .filter(p -> p.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }
}

package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.validators.GetPaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
@ToString
@Slf4j
public class SearchPaymentService {

    private final PaymentRepository paymentRepository;
    private final GetPaymentValidator validator;

    public CommonResponseForPaymentParameters execute(CommonRequestForPaymentParameters request) {
        log.info("{} is start!", this);
        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Optional<Payment>  payment = paymentRepository.findByEmployeeIdAndAmount(request.getPaymentDTO().getEmployeeId(), request.getPaymentDTO().getAmount());
            response.setPaymentDTO(PaymentDTO.builder().id(payment.get().getId())
                    .employeeId(payment.get().getEmployeeId())
                    .amount(payment.get().getAmount()).build());
            log.info("Payment : {} was successful received", payment);
        }else {
            log.warn("Validation failed errors : {}" , errors);
            response.setErrors(errors);
        }
        log.info("{} is execute!", this);
        return response;
    }



}

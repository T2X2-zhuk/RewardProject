package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.validators.GetPaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class SearchPaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private GetPaymentValidator validator;

    public CommonResponseForPaymentParameters execute(CommonRequestForPaymentParameters request) {
        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
        List<ValidationError> errors = validator.validate(request);
        if (errors.isEmpty()){
            Optional<Payment>  payment = paymentRepository.findByEmployeeIdAndAmount(request.getPaymentDTO().getEmployeeId(), request.getPaymentDTO().getAmount());
            response.setPaymentDTO(PaymentDTO.builder().id(payment.get().getId())
                    .employeeId(payment.get().getEmployeeId())
                    .amount(payment.get().getAmount()).build());
            return response;
        }
        response.setErrors(errors);
        return response;
    }



}

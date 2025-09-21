package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.responses.GetPaymentByEmployeeIdAndAmountResponse;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.validators.GetPaymentByEmployeeIdAndAmountValidator;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class SearchPaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private GetPaymentByEmployeeIdAndAmountValidator validator;

    public GetPaymentByEmployeeIdAndAmountResponse execute(Long employeeId,Double amount) {
        GetPaymentByEmployeeIdAndAmountResponse response = new GetPaymentByEmployeeIdAndAmountResponse();
        List<ValidationError> errors = validator.validate(employeeId,amount);
        if (errors.isEmpty()){
            Optional<Payment>  payment = paymentRepository.findByEmployeeIdAndAmount(employeeId,amount);
            response.setPaymentDTO(PaymentDTO.builder().id(payment.get().getId())
                    .employeeId(payment.get().getEmployeeId())
                    .amount(payment.get().getAmount()).build());
            return response;
        }
        response.setErrors(errors);
        return response;
    }



}

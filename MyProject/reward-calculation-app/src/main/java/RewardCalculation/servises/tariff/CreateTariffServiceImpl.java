package RewardCalculation.servises.tariff;

import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.dto.TariffDTO;
import RewardCalculation.requests.CommonRequestForTariffParameters;
import RewardCalculation.responses.CommonResponseForTariffParameters;
import RewardCalculation.validations.validators.tariff.CreateTariffRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class CreateTariffServiceImpl implements CreateTariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired private CreateTariffRequestValidator validator;

    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        if (validator.validate(request).isEmpty()){
            Tariff tariff = Tariff.builder().amount(request.getTariffDTO().getAmount())
                    .jobType(request.getTariffDTO().getJobType()).build();
            tariffRepository.save(tariff);
            response.setTariffDTO(TariffDTO.builder()
                    .id(tariff.getId())
                    .amount(tariff.getAmount())
                    .jobType(tariff.getJobType()).build());
        }else {
            response.setErrors(validator.validate(request));
        }
        return response;
    }
}

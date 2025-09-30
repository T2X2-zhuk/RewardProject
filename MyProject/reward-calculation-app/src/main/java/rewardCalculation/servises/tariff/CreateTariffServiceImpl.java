package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
import rewardCalculation.validations.validators.tariff.CreateTariffRequestValidator;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateTariffServiceImpl implements CreateTariffService {


    private final TariffRepository tariffRepository;
    private final CreateTariffRequestValidator validator;

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

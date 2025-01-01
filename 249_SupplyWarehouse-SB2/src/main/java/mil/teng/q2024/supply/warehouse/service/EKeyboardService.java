package mil.teng.q2024.supply.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import mil.teng.q2024.supply.warehouse.dto.EKeyboard;
import mil.teng.q2024.supply.warehouse.dto.EKeyboardResource;
import mil.teng.q2024.supply.warehouse.dto.SimpleDataResource;
import mil.teng.q2024.supply.warehouse.repository.EKeyboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@Slf4j
public class EKeyboardService {
    private final EKeyboardRepository eKeyboardRepository;

    public EKeyboardService(EKeyboardRepository eKeyboardRepository) {
        this.eKeyboardRepository = eKeyboardRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SimpleDataResource keyboardCreate(EKeyboardResource requestData) {
        log.debug("keyboardCreate: request={}",requestData);
        if (requestData.getId()!=null) {
            throw new IllegalArgumentException("id must be null");
        }
        EKeyboard ent = new EKeyboard(requestData);
        EKeyboard resp = eKeyboardRepository.save(ent);
        log.debug("saved as id={}",resp.getId());
        return new SimpleDataResource("keyboardCreate", resp.toString());
    }
}

package io.github.coffee330501.manager;

import io.github.coffee330501.entity.HolidayProcessEntity;
import org.flowable.engine.delegate.DelegateExecution;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class HolidayEntityManager {
    @PersistenceContext
    private EntityManager entityManager;

    public HolidayProcessEntity newHoliday(DelegateExecution execution) {
        HolidayProcessEntity entity = new HolidayProcessEntity();
        entity.setName(execution.getVariable("name").toString());
        entity.setStartTime((LocalDate)execution.getVariable("startTime"));
        entity.setEndTime((LocalDate)execution.getVariable("endTime"));
        entity.setReason(execution.getVariable("reason").toString());
        entity.setProcessInstanceId(execution.getProcessInstanceId());
        entityManager.persist(entity);
        return entity;
    }
}

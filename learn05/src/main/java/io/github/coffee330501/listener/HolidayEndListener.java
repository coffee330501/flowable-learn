package io.github.coffee330501.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class HolidayEndListener implements ExecutionListener {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void notify(DelegateExecution execution) {
        // 将历史变量删除
        String processInstanceId = execution.getProcessInstanceId();
        String sql = "delete from act_hi_varinst where PROC_INST_ID_ = ?";
        entityManager
                .createNativeQuery(sql)
                .setParameter(1, processInstanceId)
                .executeUpdate();
    }
}

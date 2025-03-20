package io.github.coffee330501;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {
    /**
     * 模拟发送邮件
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("您的请假申请已被驳回。");
    }
}

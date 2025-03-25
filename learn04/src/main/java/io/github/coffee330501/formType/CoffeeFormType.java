package io.github.coffee330501.formType;

import org.flowable.engine.form.AbstractFormType;

/**
 * 咖啡类型
 */
public class CoffeeFormType extends AbstractFormType {
    // 把表单中的值转化为实际对象的值
    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    // 把实际对象中的值转化为表单中的值
    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return (String) modelValue;
    }

    // 表单类型标识符
    @Override
    public String getName() {
        return "Coffee";
    }
}

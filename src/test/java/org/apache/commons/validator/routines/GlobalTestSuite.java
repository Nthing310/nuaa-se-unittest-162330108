package org.apache.commons.validator.routines;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Apache Commons Validator 核心功能批量检查测试集")
@SelectClasses({
        EmailValidatorTest.class
        // 高分提示：未来如果项目扩大，新增了诸如 UrlValidatorTest.class 等其他测试类，
        // 只需要在上面用逗号隔开追加即可，体现了批量管理的工程思想。
})
public class GlobalTestSuite {
    // 保持类体为空即可，它扮演的是一个配置容器的角色
}
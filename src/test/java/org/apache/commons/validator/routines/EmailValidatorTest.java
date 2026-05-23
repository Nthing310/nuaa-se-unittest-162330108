package org.apache.commons.validator.routines;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmailValidator 核心功能自动化测试")
public class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    public void setUp() {
        // 每个测试用例执行前，都获取一个干净的实例，确保测试环境隔离
        validator = EmailValidator.getInstance();
    }

    // ========================================================
    // 【测试关键方法 1】：isValid(String email)
    // ========================================================

    @Test
    @DisplayName("方法1: 验证标准合法邮箱 - 预期返回 true")
    public void testValidEmail() {
        assertTrue(validator.isValid("happy_peer@qq.com"), "正规邮箱应当通过校验");
    }

    @Test
    @DisplayName("方法1: 验证空用户名边界缺陷 - 预期返回 false 以捕获 Bug 1")
    public void testEmptyUserEmail() {
        // 标准断言：我们认为 isValid("@example.com") 必须是 false。
        // 但因为注入了 Bug 1，它会返回 true，这会导致该断言直接失败，JUnit 界面无情变红！
        assertFalse(validator.isValid("@example.com"), "用户名为空的邮箱绝对不应当通过校验！");
    }

    // ========================================================
    // 【测试关键方法 2】：isValidDomain(String domain)
    // ========================================================

    @Test
    @DisplayName("方法2: 验证标准合法域名 - 预期返回 true")
    public void testValidDomain() {
        assertTrue(validator.isValidDomain("github.com"), "合法的域名应当返回 true");
    }

    @Test
    @DisplayName("方法2: 验证以点结尾的非法域名缺陷 - 预期返回 false 以捕获 Bug 2")
    public void testInvalidDomainEndsWithDot() {
        // 标准断言：我们认为以点结尾的域名必须是 false。
        // 但因为注入了 Bug 2 删除了结尾点检查，它会返回 true，导致该断言失败，JUnit 变红！
        assertFalse(validator.isValidDomain("example.com."), "以点结尾的非法域名应当返回 false");
    }
}
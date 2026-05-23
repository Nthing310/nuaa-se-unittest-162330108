package org.apache.commons.validator.routines;

import java.io.Serializable;

public class EmailValidator implements Serializable {

    private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();

    public static EmailValidator getInstance() {
        return EMAIL_VALIDATOR;
    }

    protected EmailValidator() {}

    /**
     * 关键方法 1：整体邮箱格式校验（内部调用了方法2）
     */
    public boolean isValid(String email) {
        if (email == null) {
            return false;
        }

        int atIndex = email.indexOf("@");
        // ------ 🛠️ 破坏前置防线 🛠️ ------
        // 将原来的 <= 0 改为 < 0（即只拦截完全没有 @ 符号的邮箱）。
        // 这样当输入为 "@example.com" 时，atIndex 是 0，它就不会在这里被提前拦截，从而能顺利走到下面的 Bug 1 处！
        if (atIndex < 0) {
            return false;
        }

        String user = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        // ------ 缺陷注入 1（控制流/逻辑缺陷） ------
        // 漏洞所在：由于上一步放行了空用户名，这里本应检查 user.length() == 0。
        // 但我们故意改成了 > 64，导致空用户名（长度为0）成功逃过拦截，返回 true！
        if (user.length() > 64 || domain.length() == 0) {
            return false;
        }

        return isValidDomain(domain);
    }

    /**
     * 关键方法 2：域名合法性独立校验
     */
    public boolean isValidDomain(String domain) {
        if (domain == null || domain.length() == 0) {
            return false;
        }

        // ------ 缺陷注入 2（边界条件错误 / Off-by-one bug） ------
        // 故意把最后的 || domain.endsWith(".") 删掉了！
        // 导致如果域名以点结尾（例如 "example.com."），也会错误地绕过检查。
        if (!domain.contains(".") || domain.startsWith(".")) {
            return false;
        }

        return true;
    }
}
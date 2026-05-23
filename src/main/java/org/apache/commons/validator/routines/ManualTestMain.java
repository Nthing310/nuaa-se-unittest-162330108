package org.apache.commons.validator.routines;

public class ManualTestMain {
    public static void main(String[] args) {
        EmailValidator validator = EmailValidator.getInstance();

        int totalTests = 0;
        int failedTests = 0;

        System.out.println("====== 启动手工单元测试：开始全面检出注入缺陷 ======\n");

        // ========================================================
        // 【测试关键方法 1】：isValid(String email)
        // ========================================================
        System.out.println("--- 正在检测：关键方法 1 (isValid) ---");

        // 用例 1.1：分支覆盖 - 正常邮箱路径
        totalTests++;
        if (validator.isValid("test@example.com") == true) {
            System.out.println("[OK] 用例 1.1：正常合法邮箱校验通过");
        } else {
            System.out.println("[ERROR] 用例 1.1：合法邮箱被误判！");
            failedTests++;
        }

        // 用例 1.2：缺陷检出 - 空用户名边界（用来抓 Bug 1）
        totalTests++;
        // 正确的预期应该是返回 false。如果由于缺陷1返回了 true，说明触发了漏洞。
        if (validator.isValid("@example.com") == false) {
            System.out.println("[OK] 用例 1.2：空用户名邮箱被成功拦截");
        } else {
            System.out.println("[MISSED BUG] 检出失败！关键方法1未能拦截空用户名（Bug 1 逃逸）");
            failedTests++;
        }


        // ========================================================
        // 【测试关键方法 2】：isValidDomain(String domain)
        // ========================================================
        System.out.println("\n--- 正在检测：关键方法 2 (isValidDomain) ---");

        // 用例 2.1：分支覆盖 - 正常域名路径
        totalTests++;
        if (validator.isValidDomain("example.com") == true) {
            System.out.println("[OK] 用例 2.1：正常合法域名校验通过");
        } else {
            System.out.println("[ERROR] 用例 2.1：合法域名被误判！");
            failedTests++;
        }

        // 用例 2.2：缺陷检出 - 以点结尾的域名边界（用来抓 Bug 2）
        totalTests++;
        // 正确的预期应该是返回 false。如果由于缺陷2返回了 true，说明触发了漏洞。
        if (validator.isValidDomain("example.com.") == false) {
            System.out.println("[OK] 用例 2.2：以点结尾的非法域名被成功拦截");
        } else {
            System.out.println("[MISSED BUG] 检出失败！关键方法2未能拦截以点结尾的域名（Bug 2 逃逸）");
            failedTests++;
        }


        // ========================================================
        // 【测试结果汇总与断言判定】
        // ========================================================
        System.out.println("\n======================================================");
        System.out.println(String.format("测试统计：总计运行用例 %d 个，其中失败（未通过预期） %d 个。", totalTests, failedTests));
        System.out.println("======================================================");

        // 终极检出逻辑：如果有任何用例失败（即抓到了 Bug），则抛出异常中断程序
        if (failedTests > 0) {
            System.err.println("\n【质量检查报告】：代码中存在未修复的软件缺陷！");
            // 抛出运行时异常，确保在 IDEA 的控制台中能看到鲜红色的报错栈，实现完全检出
            throw new RuntimeException("单元测试未通过！成功检出 " + failedTests + " 个软件缺陷！");
        } else {
            System.out.println("\n【质量检查报告】：所有测试路径表现符合预期，代码暂无已知缺陷。");
        }
    }
}
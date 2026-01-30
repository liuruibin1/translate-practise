package com.xxx.common.core.utils.core.utils.uuid;

public class NameUtil {

    /**
     * 将各种命名格式统一转换为 kebab-case (中划线命名)
     * 步骤:
     * 1. 过滤特殊符号,只保留字母、数字、下划线
     * 2. 转换为 kebab-case 格式
     *
     * 支持格式:
     * - PascalCase (FirstName)
     * - camelCase (firstName)
     * - snake_case (first_name)
     * - 空格分隔 (first name)
     * - 已有 kebab-case (first-name)
     * - 混合格式
     */
    public static String toKebabCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 1. 去除首尾空格
        String result = input.trim();

        // 2. 过滤特殊符号,只保留字母、数字、下划线、中划线、空格(作为临时分隔符)
        result = result.replaceAll("[^a-zA-Z0-9_\\-\\s]", "");

        // 3. 将多个连续空格替换为单个空格
        result = result.replaceAll("\\s+", " ");

        // 4. 在大写字母前插入空格(用于拆分 camelCase 和 PascalCase)
        // 例如: FirstName -> First Name, firstName -> first Name
        result = result.replaceAll("([a-z])([A-Z])", "$1 $2");
        result = result.replaceAll("([A-Z])([A-Z][a-z])", "$1 $2");

        // 5. 将下划线、中划线、空格统一替换为中划线
        result = result.replaceAll("[_\\s]+", "-");
        result = result.replaceAll("-+", "-");

        // 6. 转换为小写
        result = result.toLowerCase();

        // 7. 清理首尾的中划线
        result = result.replaceAll("^-|-$", "");

        return result;
    }

    /**
     * 将各种命名格式统一转换为 PascalCase (大驼峰命名)
     * 支持格式:
     * - snake_case (first_name)
     * - camelCase (firstName)
     * - kebab-case (first-name)
     * - 空格分隔 (first name)
     * - 已有 PascalCase (FirstName)
     * - 混合格式
     */
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 去除首尾空格
        String result = input.trim();

        // 先过滤特殊符号,只保留字母、数字、下划线、中划线、空格
        result = result.replaceAll("[^a-zA-Z0-9_\\-\\s]", "");

        // 将中划线、下划线、空格统一替换为空格作为分隔符
        result = result.replaceAll("[-_\\s]+", " ");

        // 在大写字母前插入空格(用于拆分 camelCase 和 PascalCase)
        result = result.replaceAll("([a-z])([A-Z])", "$1 $2");
        result = result.replaceAll("([A-Z])([A-Z][a-z])", "$1 $2");

        // 分割单词
        String[] words = result.split("\\s+");

        // 将每个单词首字母大写,其余小写
        StringBuilder pascalCase = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                pascalCase.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    pascalCase.append(word.substring(1).toLowerCase());
                }
            }
        }

        return pascalCase.toString();
    }

    //    public static void main(String[] args) {
    //        // 测试用例
    //        String[] testCases = {
    //                "First Name",
    //                "First_Name",
    //                "FirstName",
    //                "First-Name",
    //                "first name",
    //                "first_name",
    //                "firstName",
    //                "first-name",
    //                "        firstName        ",  // 带空格
    //                "FIRST_NAME",
    //                "firstNameTest",
    //                "FirstNameTest",
    //                "first-name-test",
    //                "first@Name#123",  // 带特殊符号
    //                "user$Name&Test",  // 带特殊符号
    //                "data_2024!name"   // 带数字和特殊符号
    //        };
    //
    //        System.out.println("命名格式转换测试 (转为 PascalCase):");
    //        System.out.println("=".repeat(50));
    //
    //        for (String testCase : testCases) {
    //            String result = toPascalCase(testCase);
    //            System.out.printf("%-25s -> %s%n", "\"" + testCase + "\"", result);
    //        }
    //    }

    public static void main(String[] args) {
        // 测试用例
        String[] testCases = {
                "First Name Age",
                "First_Name_Age",
                "FirstNameAge",
                "First-Name-Age",
                "first name age",
                "first_name_age",
                "firstName Age",
                "firstNameAge",
                "first-name-age",
                "        firstName  Age        ",  // 带多余空格
                "FIRST_NAME_AGE",
                "first@Name#Age123",  // 带特殊符号
                "user$Name&Age_Test",  // 带特殊符号
                "data_2024!name-age",  // 混合格式
                "First__Name--Age"  // 多个分隔符
        };

        System.out.println("命名格式转换测试 (转为 kebab-case):");
        System.out.println("=".repeat(60));

        for (String testCase : testCases) {
            String result = toKebabCase(testCase);
            System.out.printf("%-30s -> %s%n", "\"" + testCase + "\"", result);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("转换规则:");
        System.out.println("1. 过滤特殊符号,只保留字母、数字、下划线");
        System.out.println("2. 拆分驼峰命名(camelCase/PascalCase)");
        System.out.println("3. 将空格、下划线统一转为中划线");
        System.out.println("4. 全部转为小写");
        System.out.println("5. 清理多余的中划线");
    }

}

package com.xxx.common.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 用户密码加密工具类
 * 使用SHA-256算法和用户独立的Salt进行密码加密
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";

    private static final int SALT_LENGTH = 16; // 16字节的Salt

    public static final String PASSWORD6_REGEX = "^[0-9]{6}$";

    /**
     * 包含至少一个小写字母、一个大写字母、一个数字和一个特殊字符，并且密码长度至少为8个字符
     */
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";

    public static boolean validate(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    /**
     * 生成随机Salt
     * @return Base64编码的Salt字符串
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 使用Salt加密密码
     * @param password 原始密码
     * @param salt Salt值
     * @return 加密后的密码（Base64编码）
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            // 将salt和password组合
            String saltedPassword = password + salt;

            // 进行hash计算
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());

            // 转换为Base64字符串
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法不存在", e);
        }
    }

    /**
     * 验证密码是否正确
     * @param inputPassword 用户输入的密码
     * @param storedHash 数据库中存储的hash值
     * @param storedSalt 数据库中存储的salt值
     * @return 密码是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) {
        String hashOfInput = hashPassword(inputPassword, storedSalt);
        return hashOfInput.equals(storedHash);
    }

    /*public static void main(String[] args) {
        String password = "Example1!";
        boolean isValid = validatePassword(password);
        System.out.println("Password is valid: " + isValid);
    }*/

    public static void main(String[] args) {
        //for (int i = 0; i < 27; i++) {
        //    System.out.println(generateSalt());
        //}
        System.out.println(hashPassword("123456", "USXAUUPR80bK85fHr+gnEQ=="));
    }

    //    /**
    //     * 用户实体类示例
    //     */
    //    public static class User {
    //        private Long id;
    //        private String username;
    //        private String passwordHash;  // 存储加密后的密码
    //        private String salt;          // 存储用户独立的Salt
    //
    //        public User(String username, String password) {
    //            this.username = username;
    //            this.salt = generateSalt();
    //            this.passwordHash = hashPassword(password, this.salt);
    //        }
    //
    //        // Getters and Setters
    //        public Long getId() { return id; }
    //        public void setId(Long id) { this.id = id; }
    //
    //        public String getUsername() { return username; }
    //        public void setUsername(String username) { this.username = username; }
    //
    //        public String getPasswordHash() { return passwordHash; }
    //        public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    //
    //        public String getSalt() { return salt; }
    //        public void setSalt(String salt) { this.salt = salt; }
    //
    //        /**
    //         * 验证密码
    //         */
    //        public boolean checkPassword(String password) {
    //            return verifyPassword(password, this.passwordHash, this.salt);
    //        }
    //
    //        /**
    //         * 更新密码
    //         */
    //        public void updatePassword(String newPassword) {
    //            this.salt = generateSalt(); // 生成新的salt
    //            this.passwordHash = hashPassword(newPassword, this.salt);
    //        }
    //
    //        @Override
    //        public String toString() {
    //            return "User{" +
    //                    "id=" + id +
    //                    ", username='" + username + '\'' +
    //                    ", passwordHash='" + passwordHash + '\'' +
    //                    ", salt='" + salt + '\'' +
    //                    '}';
    //        }
    //    }
    //
    //    /**
    //     * 使用示例
    //     */
    //    public static void main(String[] args) {
    //        System.out.println("=== 密码加密示例 ===\n");
    //
    //        // 1. 用户注册 - 创建新用户
    //        System.out.println("1. 用户注册:");
    //        User user1 = new User("zhang_san", "MyPassword123!");
    //        System.out.println("用户名: " + user1.getUsername());
    //        System.out.println("Salt: " + user1.getSalt());
    //        System.out.println("密码Hash: " + user1.getPasswordHash());
    //
    //        // 2. 用户登录 - 验证密码
    //        System.out.println("\n2. 用户登录验证:");
    //        String inputPassword = "MyPassword123!";
    //        boolean isValid = user1.checkPassword(inputPassword);
    //        System.out.println("输入密码: " + inputPassword);
    //        System.out.println("验证结果: " + (isValid ? "成功✓" : "失败✗"));
    //
    //        // 3. 错误密码验证
    //        System.out.println("\n3. 错误密码验证:");
    //        String wrongPassword = "WrongPassword";
    //        boolean isWrong = user1.checkPassword(wrongPassword);
    //        System.out.println("输入密码: " + wrongPassword);
    //        System.out.println("验证结果: " + (isWrong ? "成功✓" : "失败✗"));
    //
    //        // 4. 创建第二个用户，展示不同的Salt
    //        System.out.println("\n4. 创建第二个用户（相同密码）:");
    //        User user2 = new User("li_si", "MyPassword123!");
    //        System.out.println("用户1 Salt: " + user1.getSalt());
    //        System.out.println("用户2 Salt: " + user2.getSalt());
    //        System.out.println("用户1 Hash: " + user1.getPasswordHash());
    //        System.out.println("用户2 Hash: " + user2.getPasswordHash());
    //        System.out.println("Hash相同? " + (user1.getPasswordHash().equals(user2.getPasswordHash()) ? "是" : "否（正确）"));
    //
    //        // 5. 修改密码
    //        System.out.println("\n5. 修改密码:");
    //        System.out.println("原Salt: " + user1.getSalt());
    //        System.out.println("原Hash: " + user1.getPasswordHash());
    //        user1.updatePassword("NewPassword456!");
    //        System.out.println("新Salt: " + user1.getSalt());
    //        System.out.println("新Hash: " + user1.getPasswordHash());
    //        System.out.println("旧密码验证: " + (user1.checkPassword("MyPassword123!") ? "成功" : "失败（正确）"));
    //        System.out.println("新密码验证: " + (user1.checkPassword("NewPassword456!") ? "成功（正确）" : "失败"));
    //    }

}
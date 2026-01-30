DROP TABLE IF EXISTS `user`;
CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      username VARCHAR(64) UNIQUE NOT NULL,
                      password VARCHAR(1024) NOT NULL,
                      level INT NOT NULL DEFAULT 1,
                      avg_score INT NOT NULL DEFAULT 0,
                      create_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sentence_pool`;
CREATE TABLE sentence_pool (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               zh TEXT NOT NULL COMMENT '中文句子',
                               zh_md5 CHAR(32) NOT NULL COMMENT '中文 hash，用于索引和去重',
                               level VARCHAR(64) COMMENT '句子等级：A1,A2,B1,B2,C1',
                               create_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_zh_md5 (zh_md5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `translation_pool`;
CREATE TABLE translation_pool (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  sentence_id BIGINT NOT NULL,
                                  user_answer TEXT NOT NULL,
                                  user_answer_md5 CHAR(32) NOT NULL COMMENT '用户翻译 hash',
                                  ai_feedback TEXT NOT NULL,
                                  ai_score VARCHAR(2000) NOT NULL,
                                  overall_score INT NOT NULL,
                                  create_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (id),
                                  UNIQUE KEY uk_sentence_user_answer (sentence_id, user_answer_md5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_translations`;
CREATE TABLE user_translations (
                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                   user_id BIGINT NOT NULL,
                                   sentence_id BIGINT NOT NULL,
                                   translation_id BIGINT NOT NULL,
                                   create_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   UNIQUE KEY uk_user_sentence_translation (user_id, sentence_id, translation_id),
                                   PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `daily_sentences`;
CREATE TABLE daily_sentences (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 date VARCHAR(64) UNIQUE,
                                 sentence_ids VARCHAR(255) COMMENT '逗号分隔的句子ID',
                                 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `id` int NOT NULL,
                             `parent_id` int NOT NULL,
                             `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `sort` int NOT NULL DEFAULT 0,
                             `update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '总部', 1, '2024-01-28 06:10:20');
INSERT INTO `sys_dept` VALUES (11, 1, '开发', 1, '2024-01-31 19:26:51');

DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
                                      `id` bigint NOT NULL,
                                      `entity_id` int NOT NULL,
                                      `action_type` int NOT NULL,
                                      `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
                                      `operator_type` int NOT NULL,
                                      `operator_id` bigint NOT NULL,
                                      `create_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` int NOT NULL,
                                   `parent_id` int NOT NULL DEFAULT 0,
                                   `type` int NOT NULL,
                                   `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                   `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `sort` int NOT NULL DEFAULT 0,
                                   `url_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `url_query` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `page_component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `is_cache` tinyint(1) NOT NULL DEFAULT 0,
                                   `is_visible` tinyint(1) NOT NULL DEFAULT 1,
                                   `is_page_frame` tinyint(1) NOT NULL DEFAULT 1,
                                   `is_enabled` tinyint(1) NOT NULL DEFAULT 1,
                                   `update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_permission` VALUES (1, 0, 1, '顶级', NULL, 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-03 07:01:34');
INSERT INTO `sys_permission` VALUES (2, 1, 1, '系统管理', NULL, 111, 'sys', NULL, NULL, 'SettingOutlined', 1, 1, 1, 1, '2025-08-24 04:12:42');
INSERT INTO `sys_permission` VALUES (3, 2, 3, '系统用户', NULL, 1, 'sys-user', NULL, 'Sys/SysUser/index', NULL, 1, 1, 1, 1, '2024-01-30 08:04:49');
INSERT INTO `sys_permission` VALUES (4, 2, 3, '系统部门', NULL, 3, 'sys-dept', NULL, 'Sys/SysDept/index', NULL, 1, 1, 1, 1, '2024-01-30 08:17:55');
INSERT INTO `sys_permission` VALUES (5, 2, 3, '系统角色', NULL, 2, 'sys-role', NULL, 'Sys/SysRole/index', NULL, 1, 1, 1, 1, '2024-01-30 08:17:55');
INSERT INTO `sys_permission` VALUES (6, 2, 3, '系统权限', NULL, 5, 'sys-permission', NULL, 'Sys/SysPermission/index', NULL, 1, 1, 1, 1, '2024-01-30 08:17:55');
INSERT INTO `sys_permission` VALUES (31, 3, 5, '系统用户-查看', 'sys_user:view', 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:58:18');
INSERT INTO `sys_permission` VALUES (32, 3, 5, '系统用户-删除', 'sys_user:delete', 4, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (33, 3, 5, '系统用户-保存', 'sys_user:save', 2, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (34, 3, 5, '系统用户-重置密码', 'sys_user:resetPassword', 3, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (35, 3, 5, '系统用户角色-保存', 'sys_user_role:save', 11, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2026-01-29 01:23:32');
INSERT INTO `sys_permission` VALUES (41, 4, 5, '系统部门-查看', 'sys_dept:view', 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:58:18');
INSERT INTO `sys_permission` VALUES (42, 4, 5, '系统部门-删除', 'sys_dept:delete', 3, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (43, 4, 5, '系统部门-保存', 'sys_dept:save', 2, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (51, 5, 5, '系统角色-查看', 'sys_role:view', 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:58:18');
INSERT INTO `sys_permission` VALUES (52, 5, 5, '系统角色-删除', 'sys_role:delete', 3, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (53, 5, 5, '系统角色-保存', 'sys_role:save', 2, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (54, 5, 5, '系统角色权限-保存', 'sys_role_permission:save', 6, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-08-07 09:11:01');
INSERT INTO `sys_permission` VALUES (61, 6, 5, '系统菜单-查看', 'sys_permission:view', 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:58:18');
INSERT INTO `sys_permission` VALUES (62, 6, 5, '系统菜单-删除', 'sys_permission:delete', 3, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (63, 6, 5, '系统菜单-保存', 'sys_permission:save', 2, NULL, NULL, NULL, NULL, 1, 1, 0, 1, '2024-02-01 00:59:02');
INSERT INTO `sys_permission` VALUES (70, 1, 1, '用户管理', NULL, 13, 'user', NULL, NULL, 'UserOutlined', 1, 1, 1, 1, '2025-12-30 01:22:34');
INSERT INTO `sys_permission` VALUES (71, 70, 3, '用户列表', NULL, 1, 'user', NULL, 'User/User/index', NULL, 1, 1, 1, 1, '2024-08-06 17:58:11');
INSERT INTO `sys_permission` VALUES (72, 71, 5, '用户列表-查看', 'user:view', 1, NULL, NULL, NULL, NULL, 0, 1, 1, 1, '2026-01-27 04:22:04');
INSERT INTO `sys_permission` VALUES (595, 3, 5, '系统用户-强制退出', 'sys_user:forceLogout', 5, NULL, NULL, NULL, NULL, 0, 1, 1, 1, '2026-01-29 01:23:45');

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` int NOT NULL,
                             `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `sort` int NOT NULL DEFAULT 0,
                             `is_enabled` tinyint(1) NOT NULL DEFAULT 1,
                             `update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, 1, '2024-01-31 11:58:03');

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` int NOT NULL,
                                        `role_id` int NOT NULL,
                                        `permission_id` int NOT NULL,
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_role_permission` VALUES (2752, 1, 70);
INSERT INTO `sys_role_permission` VALUES (2753, 1, 71);
INSERT INTO `sys_role_permission` VALUES (2754, 1, 72);
INSERT INTO `sys_role_permission` VALUES (2943, 1, 87);
INSERT INTO `sys_role_permission` VALUES (2944, 1, 88);
INSERT INTO `sys_role_permission` VALUES (2966, 1, 90);
INSERT INTO `sys_role_permission` VALUES (2970, 1, 91);
INSERT INTO `sys_role_permission` VALUES (2988, 1, 2);
INSERT INTO `sys_role_permission` VALUES (2989, 1, 3);
INSERT INTO `sys_role_permission` VALUES (2990, 1, 31);
INSERT INTO `sys_role_permission` VALUES (2991, 1, 33);
INSERT INTO `sys_role_permission` VALUES (2992, 1, 34);
INSERT INTO `sys_role_permission` VALUES (2994, 1, 32);
INSERT INTO `sys_role_permission` VALUES (2995, 1, 35);
INSERT INTO `sys_role_permission` VALUES (2996, 1, 5);
INSERT INTO `sys_role_permission` VALUES (2997, 1, 51);
INSERT INTO `sys_role_permission` VALUES (2998, 1, 53);
INSERT INTO `sys_role_permission` VALUES (2999, 1, 52);
INSERT INTO `sys_role_permission` VALUES (3002, 1, 54);
INSERT INTO `sys_role_permission` VALUES (3003, 1, 4);
INSERT INTO `sys_role_permission` VALUES (3004, 1, 41);
INSERT INTO `sys_role_permission` VALUES (3005, 1, 43);
INSERT INTO `sys_role_permission` VALUES (3006, 1, 42);
INSERT INTO `sys_role_permission` VALUES (3007, 1, 6);
INSERT INTO `sys_role_permission` VALUES (3008, 1, 61);
INSERT INTO `sys_role_permission` VALUES (3009, 1, 63);
INSERT INTO `sys_role_permission` VALUES (3010, 1, 62);
INSERT INTO `sys_role_permission` VALUES (3024, 1, 95);
INSERT INTO `sys_role_permission` VALUES (3025, 1, 97);
INSERT INTO `sys_role_permission` VALUES (3046, 1, 94);
INSERT INTO `sys_role_permission` VALUES (3075, 1, 595);

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint NOT NULL,
                             `dept_id` int NOT NULL,
                             `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `merchant_user_id` bigint NULL DEFAULT NULL COMMENT '商户用户ID',
                             `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                             `phone_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                             `telegram_id` bigint NULL DEFAULT NULL,
                             `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
                             `is_enabled` tinyint(1) NOT NULL DEFAULT 1,
                             `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
                             `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                             `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `sys_user_ui`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

INSERT INTO `sys_user` VALUES (1, 1, 'admin', 1, '$2a$10$NcXMXkIeX4d7l7X8OaqKDun0U1QxfbQM4l1Vy92iNYtzyCn6w9ueW', 'System Administrator', 'xxx@gmail.com', '', NULL, '', 1, 0, '', '2024-10-29 20:29:31', '2025-12-28 01:54:33');


DROP TABLE IF EXISTS `sys_user_login_log`;
CREATE TABLE `sys_user_login_log`  (
                                       `id` bigint NOT NULL,
                                       `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                       `is_successful` tinyint(1) NOT NULL DEFAULT 1,
                                       `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                       `create_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` int NOT NULL,
                                  `sys_user_id` bigint NULL DEFAULT NULL COMMENT '系统用户ID',
                                  `role_id` int NOT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `sys_user_role_uk1`(`sys_user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

INSERT INTO `sys_user_role` VALUES (108, 1, 1);
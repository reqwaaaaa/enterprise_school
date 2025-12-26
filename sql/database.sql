-- 创建数据库
CREATE DATABASE IF NOT EXISTS school_enterprise_platform
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

-- 使用数据库
USE school_enterprise_platform;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 用户表 (t_user) - 统一用户管理系统基础
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                        username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
                        phone VARCHAR(20) UNIQUE COMMENT '手机号',
                        password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
                        salt VARCHAR(50) COMMENT '盐值',
                        user_type ENUM('admin', 'school_user', 'enterprise_user', 'student', 'teacher') NOT NULL COMMENT '用户类型: admin管理员, school_user高校用户, enterprise_user企业用户, student学生, teacher教师',
                        organization_id BIGINT COMMENT '所属组织ID (学校或企业)',
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        status ENUM('active', 'inactive', 'banned') DEFAULT 'active' COMMENT '状态: active活跃, inactive未激活, banned禁用',
                        FOREIGN KEY (organization_id) REFERENCES t_organization(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表 - 统一用户管理系统';

-- 2. 用户信息表 (t_user_info) - 扩展用户信息
DROP TABLE IF EXISTS t_user_info;
CREATE TABLE t_user_info (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                             user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
                             real_name VARCHAR(50) COMMENT '真实姓名',
                             id_card VARCHAR(20) COMMENT '身份证号',
                             avatar VARCHAR(255) COMMENT '头像URL',
                             gender ENUM('male', 'female', 'other') COMMENT '性别: male男, female女, other其他',
                             birth_date DATE COMMENT '出生日期',
                             address VARCHAR(255) COMMENT '地址',
                             bio TEXT COMMENT '个人简介',
                             create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详细信息表 - 扩展用户管理系统';

-- 3. 组织机构表 (t_organization) - 统一组织机构管理系统
DROP TABLE IF EXISTS t_organization;
CREATE TABLE t_organization (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                name VARCHAR(100) NOT NULL COMMENT '组织名称 (学校/企业/部门)',
                                type ENUM('school', 'enterprise', 'department') NOT NULL COMMENT '类型: school高校, enterprise企业, department部门',
                                parent_id BIGINT COMMENT '上级组织ID (树形结构)',
                                code VARCHAR(50) UNIQUE COMMENT '组织编码',
                                description TEXT COMMENT '描述',
                                contact_phone VARCHAR(20) COMMENT '联系电话',
                                contact_email VARCHAR(100) COMMENT '联系邮箱',
                                address VARCHAR(255) COMMENT '地址',
                                create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                FOREIGN KEY (parent_id) REFERENCES t_organization(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织机构表 - 学校、企业、部门等';

-- 4. 角色表 (t_role) - 统一权限管理系统
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                        name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
                        description TEXT COMMENT '角色描述',
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表 - 权限管理基础';

-- 5. 权限表 (t_permission) - 统一权限管理系统
DROP TABLE IF EXISTS t_permission;
CREATE TABLE t_permission (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                              name VARCHAR(50) NOT NULL UNIQUE COMMENT '权限名称 (e.g., user:read, course:edit)',
                              description TEXT COMMENT '权限描述',
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表 - 细粒度权限控制';

-- 6. 用户角色关联表 (t_user_role) - 多对多关联
DROP TABLE IF EXISTS t_user_role;
CREATE TABLE t_user_role (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                             user_id BIGINT NOT NULL COMMENT '用户ID',
                             role_id BIGINT NOT NULL COMMENT '角色ID',
                             create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             UNIQUE KEY uniq_user_role (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
                             FOREIGN KEY (role_id) REFERENCES t_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表 - RBAC权限模型';

-- 7. 角色权限关联表 (t_role_permission) - 多对多关联
DROP TABLE IF EXISTS t_role_permission;
CREATE TABLE t_role_permission (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                   role_id BIGINT NOT NULL COMMENT '角色ID',
                                   permission_id BIGINT NOT NULL COMMENT '权限ID',
                                   create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   UNIQUE KEY uniq_role_permission (role_id, permission_id),
                                   FOREIGN KEY (role_id) REFERENCES t_role(id) ON DELETE CASCADE,
                                   FOREIGN KEY (permission_id) REFERENCES t_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表 - RBAC权限模型';

-- 8. 内容表 (t_content) - 网站群平台内容管理
DROP TABLE IF EXISTS t_content;
CREATE TABLE t_content (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                           title VARCHAR(200) NOT NULL COMMENT '标题',
                           content TEXT NOT NULL COMMENT '内容',
                           type ENUM('news', 'announcement', 'policy', 'article') NOT NULL COMMENT '类型: news新闻, announcement公告, policy政策, article文章',
                           author_id BIGINT COMMENT '作者ID',
                           channel_id BIGINT COMMENT '所属频道ID',
                           status ENUM('draft', 'published', 'archived') DEFAULT 'draft' COMMENT '状态: draft草稿, published发布, archived归档',
                           view_count INT DEFAULT 0 COMMENT '浏览量',
                           like_count INT DEFAULT 0 COMMENT '点赞量',
                           create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           FOREIGN KEY (author_id) REFERENCES t_user(id) ON DELETE SET NULL,
                           FOREIGN KEY (channel_id) REFERENCES t_channel(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表 - 新闻、公告等内容管理';

-- 9. 频道/栏目表 (t_channel) - 网站群平台频道管理
DROP TABLE IF EXISTS t_channel;
CREATE TABLE t_channel (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                           name VARCHAR(100) NOT NULL COMMENT '频道名称',
                           code VARCHAR(50) UNIQUE COMMENT '频道编码',
                           parent_id BIGINT COMMENT '上级频道ID (树形结构)',
                           description TEXT COMMENT '描述',
                           create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           FOREIGN KEY (parent_id) REFERENCES t_channel(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='频道表 - 网站群栏目管理';

-- 10. 模版表 (t_template) - 网站群平台模版管理
DROP TABLE IF EXISTS t_template;
CREATE TABLE t_template (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                            name VARCHAR(100) NOT NULL COMMENT '模版名称',
                            content TEXT NOT NULL COMMENT '模版内容 (HTML)',
                            type ENUM('page', 'component') COMMENT '类型: page页面, component组件',
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模版表 - 页面模版管理';

-- 11. 在线反馈表 (t_feedback) - 网站群平台在线反馈
DROP TABLE IF EXISTS t_feedback;
CREATE TABLE t_feedback (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                            user_id BIGINT COMMENT '用户ID',
                            content TEXT NOT NULL COMMENT '反馈内容',
                            contact VARCHAR(100) COMMENT '联系方式',
                            status ENUM('pending', 'processed', 'closed') DEFAULT 'pending' COMMENT '状态: pending待处理, processed已处理, closed关闭',
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线反馈表 - 用户反馈管理';

-- 12. 日志表 (t_log) - 系统日志管理
DROP TABLE IF EXISTS t_log;
CREATE TABLE t_log (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                       user_id BIGINT COMMENT '用户ID',
                       action VARCHAR(100) NOT NULL COMMENT '操作类型 (e.g., login, insert)',
                       module VARCHAR(50) COMMENT '模块 (e.g., user, learning)',
                       details TEXT COMMENT '详情',
                       ip VARCHAR(50) COMMENT 'IP地址',
                       create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表 - 系统审计和日志';

-- 13. 课程表 (t_course) - 在线学习平台培训系统
DROP TABLE IF EXISTS t_course;
CREATE TABLE t_course (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                          title VARCHAR(200) NOT NULL COMMENT '课程标题',
                          description TEXT COMMENT '课程描述',
                          teacher_id BIGINT COMMENT '讲师ID',
                          organization_id BIGINT COMMENT '所属组织ID',
                          type ENUM('online', 'offline', 'mixed') COMMENT '类型: online在线, offline线下, mixed混合',
                          start_time DATETIME COMMENT '开始时间',
                          end_time DATETIME COMMENT '结束时间',
                          status ENUM('draft', 'published', 'ongoing', 'completed') DEFAULT 'draft' COMMENT '状态',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          FOREIGN KEY (teacher_id) REFERENCES t_user(id) ON DELETE SET NULL,
                          FOREIGN KEY (organization_id) REFERENCES t_organization(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表 - 在线学习平台';

-- 14. 课件表 (t_courseware) - 资源及知识管理系统
DROP TABLE IF EXISTS t_courseware;
CREATE TABLE t_courseware (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                              course_id BIGINT NOT NULL COMMENT '所属课程ID',
                              title VARCHAR(200) COMMENT '课件标题',
                              type ENUM('video', 'document', 'audio', 'quiz') COMMENT '类型: video视频, document文档, audio音频, quiz测验',
                              url VARCHAR(255) COMMENT '资源URL',
                              description TEXT COMMENT '描述',
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              FOREIGN KEY (course_id) REFERENCES t_course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课件表 - 课程资源管理';

-- 15. 学习记录表 (t_learning_record) - 统计分析系统
DROP TABLE IF EXISTS t_learning_record;
CREATE TABLE t_learning_record (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                   user_id BIGINT NOT NULL COMMENT '用户ID',
                                   course_id BIGINT NOT NULL COMMENT '课程ID',
                                   progress DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度 (%)',
                                   start_time DATETIME COMMENT '开始学习时间',
                                   last_access_time DATETIME COMMENT '最后访问时间',
                                   status ENUM('enrolled', 'in_progress', 'completed') DEFAULT 'enrolled' COMMENT '状态',
                                   create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
                                   FOREIGN KEY (course_id) REFERENCES t_course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表 - 学习进度统计';

-- 16. 考试表 (t_exam) - 考评系统
DROP TABLE IF EXISTS t_exam;
CREATE TABLE t_exam (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                        course_id BIGINT COMMENT '所属课程ID',
                        title VARCHAR(200) NOT NULL COMMENT '考试标题',
                        type ENUM('quiz', 'final') COMMENT '类型: quiz测验, final期末考',
                        duration INT COMMENT '时长 (分钟)',
                        start_time DATETIME COMMENT '开始时间',
                        end_time DATETIME COMMENT '结束时间',
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        FOREIGN KEY (course_id) REFERENCES t_course(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试表 - 考评系统';

-- 17. 成绩表 (t_score) - 考评系统
DROP TABLE IF EXISTS t_score;
CREATE TABLE t_score (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                         user_id BIGINT NOT NULL COMMENT '用户ID',
                         exam_id BIGINT NOT NULL COMMENT '考试ID',
                         score DECIMAL(5,2) COMMENT '分数',
                         pass_status ENUM('pass', 'fail') COMMENT '通过状态',
                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
                         FOREIGN KEY (exam_id) REFERENCES t_exam(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表 - 成绩管理';

-- 18. 招聘信息表 (t_job) - 招聘系统
DROP TABLE IF EXISTS t_job;
CREATE TABLE t_job (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                       title VARCHAR(200) NOT NULL COMMENT '职位标题',
                       description TEXT COMMENT '职位描述',
                       enterprise_id BIGINT NOT NULL COMMENT '企业ID',
                       salary VARCHAR(100) COMMENT '薪资范围',
                       location VARCHAR(100) COMMENT '工作地点',
                       requirements TEXT COMMENT '任职要求',
                       status ENUM('open', 'closed') DEFAULT 'open' COMMENT '状态',
                       view_count INT DEFAULT 0 COMMENT '浏览量',
                       create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       FOREIGN KEY (enterprise_id) REFERENCES t_organization(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘信息表 - 企业招聘发布';

-- 19. 简历投递表 (t_resume_application) - 招聘系统
DROP TABLE IF EXISTS t_resume_application;
CREATE TABLE t_resume_application (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                      user_id BIGINT NOT NULL COMMENT '求职者ID',
                                      job_id BIGINT NOT NULL COMMENT '职位ID',
                                      resume_url VARCHAR(255) COMMENT '简历URL',
                                      status ENUM('submitted', 'viewed', 'interview', 'hired', 'rejected') DEFAULT 'submitted' COMMENT '状态',
                                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
                                      FOREIGN KEY (job_id) REFERENCES t_job(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历投递表 - 求职申请';

-- 20. 业务申报表 (t_application) - 业务申报系统
DROP TABLE IF EXISTS t_application;
CREATE TABLE t_application (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                               user_id BIGINT NOT NULL COMMENT '申报用户ID',
                               type ENUM('training_subsidy', 'project_fund', 'startup_fund', 'studio') NOT NULL COMMENT '类型',
                               details TEXT NOT NULL COMMENT '申报详情',
                               status ENUM('submitted', 'under_review', 'approved', 'rejected') DEFAULT 'submitted' COMMENT '状态',
                               review_comment TEXT COMMENT '审核意见',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务申报表 - 补贴/项目申报';

-- 21. 消息通知表 (t_notification) - 消息通知系统
DROP TABLE IF EXISTS t_notification;
CREATE TABLE t_notification (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                user_id BIGINT NOT NULL COMMENT '接收用户ID',
                                title VARCHAR(200) NOT NULL COMMENT '标题',
                                content TEXT NOT NULL COMMENT '内容',
                                type ENUM('system', 'course', 'job', 'application') COMMENT '类型',
                                is_read TINYINT DEFAULT 0 COMMENT '是否已读: 0未读, 1已读',
                                create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表 - 消息推送';

-- 22. 统计报表表 (t_report) - 统一报表平台
DROP TABLE IF EXISTS t_report;
CREATE TABLE t_report (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                          title VARCHAR(200) NOT NULL COMMENT '报表标题',
                          type ENUM('learning_stats', 'job_stats', 'application_stats', 'user_stats') COMMENT '类型',
                          data JSON COMMENT '报表数据 (JSON格式)',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表表 - 统计分析';

-- 23. 学习互动表 (t_course_interaction) - Bilibili式互动 (点赞/评论/收藏)
DROP TABLE IF EXISTS t_course_interaction;
CREATE TABLE t_course_interaction (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                      course_id BIGINT NOT NULL COMMENT '课程ID',
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      type ENUM('like', 'comment', 'collect') NOT NULL COMMENT '类型: like点赞, comment评论, collect收藏',
                                      content TEXT COMMENT '内容 (评论时用)',
                                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      FOREIGN KEY (course_id) REFERENCES t_course(id) ON DELETE CASCADE,
                                      FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程互动表 - 点赞/评论/收藏';

-- 24. 推荐表 (t_recommendation) - 个性化推荐 (Bilibili式)
DROP TABLE IF EXISTS t_recommendation;
CREATE TABLE t_recommendation (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                  user_id BIGINT NOT NULL COMMENT '用户ID',
                                  recommended_item_id BIGINT NOT NULL COMMENT '推荐项ID (课程/职位/内容)',
                                  type ENUM('course', 'job', 'content') NOT NULL COMMENT '类型',
                                  score DECIMAL(5,2) COMMENT '推荐分数',
                                  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐表 - 课程/职位推荐';

SET FOREIGN_KEY_CHECKS = 1;
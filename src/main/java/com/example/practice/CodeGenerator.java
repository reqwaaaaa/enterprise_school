package com.example.practice;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/school_enterprise_platform?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false",
                        "root",
                        "#020728Ceq"
                )
                .globalConfig(builder -> {
                    builder.author("Naiweilanlan")
                            .outputDir(System.getProperty("user.dir") + "/src/main/java")
                            // .enableSwagger()
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.practice")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper"
                            ));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "t_user", "t_user_info", "t_organization", "t_role", "t_permission",
                                    "t_user_role", "t_role_permission", "t_content", "t_channel", "t_template",
                                    "t_feedback", "t_log", "t_course", "t_courseware", "t_learning_record",
                                    "t_exam", "t_score", "t_job", "t_resume_application", "t_application",
                                    "t_notification", "t_report", "t_course_interaction", "t_recommendation"
                            )
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableChainModel()
                            .enableFileOverride()

                            .mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .enableFileOverride()

                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .enableFileOverride()

                            .controllerBuilder()
                            .enableRestStyle()
                            .enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
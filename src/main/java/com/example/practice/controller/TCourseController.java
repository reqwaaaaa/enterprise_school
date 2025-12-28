package com.example.practice.controller;

import com.example.practice.entity.TCourse;
import com.example.practice.entity.TCourseInteraction;
import com.example.practice.service.TCourseInteractionService;
import com.example.practice.service.TCourseService;
import com.example.practice.service.util.JsonResponse;
import com.example.practice.api.support.UserSupport;
import com.example.practice.dao.common.annotation.ApiLimitedRole;
import com.example.practice.dao.common.constant.AuthRoleConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "课程管理")
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private TCourseService tCourseService;

    @Autowired
    private TCourseInteractionService tCourseInteractionService;

    @Autowired
    private UserSupport userSupport;

    @PostMapping
    @Operation(summary = "添加课程")
    public JsonResponse<String> addCourse(@RequestBody TCourse course) {
        Long userId = userSupport.getCurrentUserId();
        course.setTeacherId(userId);
        tCourseService.addCourse(course);
        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取课程详情")
    public JsonResponse<TCourse> getCourse(@PathVariable Long id) {
        TCourse course = tCourseService.getById(id);
        return new JsonResponse<>(course);
    }

    @PostMapping("/interaction")
    @Operation(summary = "添加课程互动 (点赞/评论/收藏)")
    public JsonResponse<String> addInteraction(@RequestBody TCourseInteraction interaction) {
        Long userId = userSupport.getCurrentUserId();
        interaction.setUserId(userId);
        tCourseInteractionService.addInteraction(interaction);
        return JsonResponse.success();
    }

    @GetMapping("/interactions")
    @Operation(summary = "获取订阅课程互动")
    public JsonResponse<List<TCourseInteraction>> getSubscribedInteractions() {
        Long userId = userSupport.getCurrentUserId();
        List<TCourseInteraction> list = tCourseInteractionService.getSubscribedInteractions(userId);
        return new JsonResponse<>(list);
    }

    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_ADMIN})
    @GetMapping("/unpassed-comments")
    @Operation(summary = "获取未通过评论 - 管理员")
    public JsonResponse<PageResult<TCourseInteraction>> getUnpassedComments(@RequestParam Integer page, @RequestParam Integer size) {
        PageResult<TCourseInteraction> result = tCourseInteractionService.getUnpassedComments(page, size);
        return new JsonResponse<>(result);
    }

    // 类似审核/删除接口
}
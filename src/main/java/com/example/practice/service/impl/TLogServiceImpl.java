package com.example.practice.service.impl;

import com.example.practice.entity.TLog;
import com.example.practice.mapper.TLogMapper;
import com.example.practice.service.TLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 - 系统审计和日志 服务实现类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Service
public class TLogServiceImpl extends ServiceImpl<TLogMapper, TLog> implements TLogService {

}

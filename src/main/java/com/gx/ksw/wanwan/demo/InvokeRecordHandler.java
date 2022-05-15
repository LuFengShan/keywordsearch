package com.gx.ksw.wanwan.demo;


import com.alibaba.fastjson.JSON;
import com.gx.ksw.wanwan.BaseMethodAdviceHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
public class InvokeRecordHandler extends BaseMethodAdviceHandler<Object> {

    /**
     * 记录方法出入参和调用时长
     */
    @Override
    public void onComplete(ProceedingJoinPoint point, long startTime, boolean permitted, boolean thrown, Object result) {
        String methodDesc = getMethodDesc(point);
        Object[] args = point.getArgs();
        long costTime = System.currentTimeMillis() - startTime;
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault());
        String format = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

//        logger.info("\n{} 执行结束，耗时={}ms，入参={}, 出参={}",
//                methodDesc, costTime,
//                JSON.toJSONString(args, true),
//                JSON.toJSONString(result, true));
        logger.info("{} 执行结束, 耗时={}ms, 请求开始时间={}, 入参={}",
                methodDesc,
                costTime,
                format,
                JSON.toJSONString(args, true));
    }

    @Override
    protected String getMethodDesc(ProceedingJoinPoint point) {
        Method targetMethod = getTargetMethod(point);
        // 获得方法上的 InvokeRecordAnno
        InvokeRecordAnno anno = targetMethod.getAnnotation(InvokeRecordAnno.class);
        String description = anno.value();

        // 如果没有指定方法说明，那么使用默认的方法说明
        if (StringUtils.isEmpty(description)) {
            description = super.getMethodDesc(point);
        }

        return description;
    }
}

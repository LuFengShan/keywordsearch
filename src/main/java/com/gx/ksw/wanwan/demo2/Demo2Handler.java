package com.gx.ksw.wanwan.demo2;

import com.gx.ksw.wanwan.BaseMethodAdviceHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class Demo2Handler extends BaseMethodAdviceHandler<Object> {

    /**
     * 记录方法出入参和调用时长
     */
    @Override
    public void onComplete(ProceedingJoinPoint point, long startTime, boolean permitted, boolean thrown, Object result) {
        logger.info("Demo2Handler");
    }

    @Override
    protected String getMethodDesc(ProceedingJoinPoint point) {
        return "Demo2Handler.getMethodDesc";
    }
}
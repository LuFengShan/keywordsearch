package com.gx.ksw.wanwan.demo2;

import com.gx.ksw.wanwan.BaseMethodAspect;
import com.gx.ksw.wanwan.MethodAdviceHandler;
import com.gx.ksw.wanwan.demo.InvokeRecordHandler;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(2)
@Component
public class Demo2Aspect extends BaseMethodAspect {

    /**
     * 指定切点（处理打上 InvokeRecordAnno 的方法）
     */
    @Override
    @Pointcut("@annotation(com.gx.ksw.wanwan.demo2.Demo2)")
    protected void pointcut() {
    }

    /**
     * 指定该切面绑定的方法切面处理器为 Demo2Handler
     */
    @Override
    protected Class<? extends MethodAdviceHandler<?>> getAdviceHandlerType() {
        return Demo2Handler.class;
    }

}

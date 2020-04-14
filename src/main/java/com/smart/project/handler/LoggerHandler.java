package com.smart.project.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.smart.project.annotation.Log;
import com.smart.project.core.AbstractService;
import com.smart.project.dao.TLoggerMapper;
import com.smart.project.model.TUser;
import com.smart.project.model.to.SystemLog;
import com.smart.project.service.LoggerService;
import com.smart.project.utils.HttpUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class LoggerHandler extends AbstractService<SystemLog> implements LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerHandler.class);

    @Resource
    private TLoggerMapper tLoggerMapper;

    @Pointcut("execution(* com.smart.project.web..*.*(..))")
    private void controllerAspect(){}

    @Before("controllerAspect()")
    public void before(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("before = " + joinPoint);
        }
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
    /*@Around("controllerAspect()")
    public void around(JoinPoint joinPoint){
        long start = System.currentTimeMillis();
        try {
            ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            if(logger.isInfoEnabled()){
                logger.info("around " + joinPoint + "\t Use time : " + (end - start) + " ms!");
            }
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            if(logger.isInfoEnabled()){
                logger.info("around " + joinPoint + "\t Use time : " + (end - start) + " ms with exception : " + e.getMessage());
            }
        }
    }*/



    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint 切点
     */
    @AfterReturning(value = "controllerAspect()", returning = "object")
    public void after(JoinPoint joinPoint, Object object) {

        try {
            Log log = getAnnotationLog(joinPoint);
            if (log == null) {
                return;
            }
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();


            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            String operationType = log.operateType();
            String description = log.description();
            boolean console = log.console();

            TUser user = new TUser();
            user.setId(1);
            user.setUsername("张三");
            String ip = HttpUtil.getIpAddress(request);
            //*========控制台输出=========*//
            System.out.println("=====controller后置通知开始=====");
            System.out.println("请求方法:" + (targetName + "." + methodName + "()"));
            System.out.println("方法描述:" + description);
            System.out.println("请求人:" + user.getUsername());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + getRequestParams(request));
            //*========数据库日志=========*//
            SystemLog systemLog = new SystemLog();
            systemLog.setId(IdUtil.fastSimpleUUID());
            systemLog.setDescription(description);
            systemLog.setMethod((targetName + "." + methodName + "()"));
            systemLog.setLogType(NumberUtils.toLong(operationType));
            systemLog.setRequestIp(ip);
            systemLog.setExceptioncode( null);
            systemLog.setExceptionDetail( null);
            systemLog.setParams( null);
            systemLog.setCreater(user.getUsername());
            systemLog.setTime(System.currentTimeMillis());
            //保存数据库
//            tLoggerMapper.insert(log);
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==后置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    /*@AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint){
        System.out.println("=====执行controller后置返回通知=====");
        if(logger.isInfoEnabled()){
            logger.info("afterReturn " + joinPoint);
        }
    }*/

    /**
     * 异常通知 用于拦截记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            Log log = getAnnotationLog(joinPoint);
            if (log == null) {
                return;
            }
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //获取用户请求方法的参数并序列化为JSON格式字符串

            String ip = HttpUtil.getIpAddress(request);

            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String params = getRequestParams(request);

            String operationType = "";
            String description = "";
            boolean console = false;


            TUser user = new TUser();
            user.setId(1);
            user.setUsername("张三");
            /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (targetName + "." + methodName + "()"));
            System.out.println("方法描述:" + description);
            System.out.println("请求人:" + user.getUsername());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
            /*==========数据库日志=========*/
            SystemLog systemLog = new SystemLog();
            systemLog.setId(IdUtil.fastSimpleUUID());
            systemLog.setDescription(description);
            systemLog.setExceptioncode(e.getClass().getName());
            systemLog.setLogType((long)1);
            systemLog.setExceptionDetail(e.getMessage());
            systemLog.setMethod((targetName + "." + methodName + "()"));
            systemLog.setParams(params);
            systemLog.setLogType(NumberUtils.toLong(operationType));
            systemLog.setTime(System.currentTimeMillis());
            systemLog.setRequestIp(ip);
            //保存数据库
//            tLoggerMapper.insert(log);
            System.out.println("=====异常通知结束=====");

            /*==========记录本地异常日志==========*/
            logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }

    }

    private static Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    private String getRequestParams(HttpServletRequest request) {
        return JSONUtil.toJsonStr(request.getParameterMap());
    }
}

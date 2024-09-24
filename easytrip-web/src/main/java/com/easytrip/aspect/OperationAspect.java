package com.easytrip.aspect;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.component.RedisUtils;
import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.RequestFrequencyTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.exception.BusinessException;
import com.easytrip.utils.JWTUtil;
import com.easytrip.utils.StringTools;
import com.easytrip.utils.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component("operationAspect")
public class OperationAspect {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JWTUtil<UserLoginDto> jwtUtil;
    private Logger logger = LoggerFactory.getLogger(OperationAspect.class);
    private static final String[] BASE_TYPE_ARRAY = new String[]{"java.lang.String", "java.lang.Integer", "java.lang.Long"};

    @Before("@annotation(com.easytrip.annotation.GlobalInterceptor)")
    public void intercepterDo(JoinPoint point) {
        Object[] arguments = point.getArgs();//获取参数
        Object target = point.getTarget();//获取Ioc容器目标对象
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (interceptor == null) {
            return;
        }
        /*
         * 校验登录
         * */
        if (interceptor.checkLogin()) {
            checkLogin();
        }
        /*
         * 校验参数
         * */
        if (interceptor.checkParams()) {
            validateParams(method, arguments);
        }
        /*
         * 频次校验
         * */
        if (interceptor.frequencyType() != RequestFrequencyTypeEnum.NO_LIMIT && interceptor.requestFrequencyThreshold() != 0) {
            String fullMethodName = target.getClass().getName() + "." + method.getName();
            checkRequestFrequency(fullMethodName, interceptor.frequencyType(), interceptor.requestFrequencyThreshold());
        }
    }

    private void validateParams(Method method, Object[] arguments) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);//拿到规则
            if (verifyParam == null) {
                continue;
            }
            String paramTypeName = parameter.getParameterizedType().getTypeName();
            if (ArrayUtils.contains(BASE_TYPE_ARRAY, paramTypeName)) {
                checkValue(value, verifyParam);
            } else {
                checkobjValue(parameter, value);
            }

        }
    }

    /*
     * 登录校验
     * */
    void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Token");
        UserLoginDto userLoginDto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, UserLoginDto.class);
        if (userLoginDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
    }
    private void checkValue(Object value, VerifyParam verifyParam) throws BusinessException {
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString()); //true 表示值为空
        Integer length = value == null ? 0 : value.toString().length();

        /*
         * 校验空
         * */
        if (isEmpty && verifyParam.required()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /*
         * 校验长度
         * */
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /*
         * 校验正则
         * */
        if (!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtils.verify(verifyParam.regex(), String.valueOf(value))) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

    }

    private void checkobjValue(Parameter parameter, Object value) {
        try {
            String typeName = parameter.getParameterizedType().getTypeName();
            Class classz = Class.forName(typeName);
            Field[] fields = classz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldVerifyParam = field.getAnnotation(VerifyParam.class);
                if (fieldVerifyParam == null) {
                    continue;
                }
                field.setAccessible(true);
                Object resultValue = field.get(value);
                checkValue(resultValue, fieldVerifyParam);
            }
        } catch (BusinessException e) {
            logger.error("校验参数失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("校验参数失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    /*
     *频次校验
     * */
    void checkRequestFrequency(String fullMethodName, RequestFrequencyTypeEnum frequencyType, Integer requestFrequencyThreshold) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getIpAddr(request);
        ip = ip.replace(":", "");
        String redisKey = fullMethodName + ip;
        Integer curCount = (Integer) redisUtils.get(redisKey);
        if (curCount != null && curCount > requestFrequencyThreshold - 1) {
            throw new BusinessException(ResponseCodeEnum.CODE_903);
        }
        redisUtils.increment(redisKey, 1, frequencyType.getSeconds());
    }

    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

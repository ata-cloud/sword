package com.youyu.common.util;

import com.youyu.common.constant.CaiyiCommonsWebConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

/**
 * Description:
 * <p/>
 * <p> ServletContextHolder
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
public final class ServletContextHolder {

    private ServletContextHolder() {
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String fetchRequestId() {
        String requestId = (String) getRequest().getAttribute(CaiyiCommonsWebConstant.X_REQUEST_ID);
        if (requestId == null) {
            requestId = Optional.ofNullable(getRequest().getHeader(CaiyiCommonsWebConstant.X_REQUEST_ID)).orElse(UUID.randomUUID().toString());
            getRequest().setAttribute(CaiyiCommonsWebConstant.X_REQUEST_ID, requestId);
        }
        return requestId;
    }
}

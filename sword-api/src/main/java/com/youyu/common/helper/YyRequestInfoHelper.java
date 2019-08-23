package com.youyu.common.helper;

import com.youyu.common.api.AppInfo;
import com.youyu.common.constant.ApplicationInfo;
import com.youyu.common.constant.RequestHeaderConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * http 请求信息帮助类
 *
 * @author WangSongJun
 * @date 2019-05-16
 */
@Slf4j
public class YyRequestInfoHelper {

    /**
     * 从请求头获取到当前用户ID
     *
     * @return
     */
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userId = ObjectUtils.isEmpty(attributes) ? null : attributes.getRequest().getHeader(RequestHeaderConst.USER_ID_HEADER);
        return StringUtils.isEmpty(userId) ? null : Long.valueOf(userId);
    }

    /**
     * 从请求头中获取当前用户名
     *
     * @return
     */
    public static String getCurrentUserName() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String currentUserName = ObjectUtils.isEmpty(attributes) ? null : attributes.getRequest().getHeader(RequestHeaderConst.USER_NAME_HEADER);
        if (!ObjectUtils.isEmpty(currentUserName)) {
            try {
                currentUserName = URLDecoder.decode(currentUserName, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                log.warn("getCurrentUserName:" + currentUserName, e);
            }
        }
        return currentUserName;
    }

    /**
     * 从请求头中获取当前用户真实姓名
     *
     * @return
     */
    public static String getCurrentUserRealName() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String currentUserRealName = ObjectUtils.isEmpty(attributes) ? null : attributes.getRequest().getHeader(RequestHeaderConst.REAL_NAME_HEADER);
        if (!ObjectUtils.isEmpty(currentUserRealName)) {
            try {
                currentUserRealName = URLDecoder.decode(currentUserRealName, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                log.warn("getCurrentUserRealName:" + currentUserRealName, e);
            }
        }
        return currentUserRealName;
    }

    /**
     * 获取当前请求的应用信息
     *
     * @return
     */
    public static AppInfo getAppInfo() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ObjectUtils.isEmpty(attributes) ? null : ApplicationInfo.getAppInfoFromHttpContext(attributes.getRequest());
    }
}

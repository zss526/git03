package com.xxx.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Tony on 2016/8/23.
 */
public class LoginUserUtil {

    /**
     * 从cookie中获取userId
     * @param request
     * @return
     */
    public static int releaseUserIdFromCookie(HttpServletRequest request) {
        String userIdString = CookieUtil.getCookieValue(request, "userid");
        if (StringUtils.isBlank(userIdString)) {
            return 0;
        }
        Integer userId = UserIDBase64.decoderUserID(userIdString);
        return userId;
    }

    public static String releaseLoginNameFromCookie(HttpServletRequest request) {
        String loginNameString = CookieUtil.getCookieValue(request, "loginname");
        if (StringUtils.isBlank(loginNameString)) {
            return "";
        }
        return loginNameString;
    }
}

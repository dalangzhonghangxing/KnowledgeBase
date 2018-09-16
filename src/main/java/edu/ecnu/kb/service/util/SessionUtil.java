package edu.ecnu.kb.service.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 对session操作的util类
 */
public class SessionUtil {
    /**
     * 获取HTTPSession
     * @return
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    /**
     * 往Session中添加一对KV
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 查询Session
     * @param key
     * @return
     */
    public static Object get(String key) {
        return getSession().getAttribute(key);
    }

    /**
     * 移除某个key
     * @param key
     */
    public static void remove(String key){
        getSession().removeAttribute(key);
    }
}

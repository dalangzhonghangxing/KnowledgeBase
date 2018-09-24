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
     *
     * @return
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    /**
     * 往Session中添加一对KV
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 查询Session
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return getSession().getAttribute(key);
    }

    /**
     * 移除某个key
     *
     * @param key
     */
    public static void remove(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 设置进度条，算法为base + cur * progressLength / total
     *
     * @param tag            进度条的key，由前端传过来
     * @param base           基准为止
     * @param cur            当前处理到的index
     * @param total          当前循环的最大循环数
     * @param progressLength 当前循环所占进度条总数
     */
    public static void setProgress(String tag, int base, int cur, int total, int progressLength) {
        set(tag, base + cur * progressLength / total);
    }
}

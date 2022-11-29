package io.github.talelin.latticy.common;

import io.github.talelin.latticy.model.UserDO;

/**
 * 线程安全的当前登录用户，如果用户未登录，则得到 null
 *
 * @author pedro@TaleLin
 */
public class LocalUser {

    private LocalUser() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<UserDO> LOCAL = new ThreadLocal<>();

    /**
     * 得到当前登录用户
     *
     * @return user | null
     */
    public static UserDO getLocalUser() {
        return LocalUser.LOCAL.get();
    }

    /**
     * 设置登录用户
     *
     * @param user user
     */
    public static void setLocalUser(UserDO user) {
        LocalUser.LOCAL.set(user);
    }

    public static <T> T getLocalUser(Class<T> clazz) {
        return (T) LOCAL.get();
    }

    /**
     * 清理当前用户
     */
    public static void clearLocalUser() {
        LOCAL.remove();
    }
}

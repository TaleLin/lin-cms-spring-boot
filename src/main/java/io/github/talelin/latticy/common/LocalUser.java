package io.github.talelin.latticy.common;

import io.github.talelin.latticy.model.UserDO;

/**
 * 线程安全的当前登录用户，如果用户为登录，则得到 null
 *
 * @author pedro@TaleLin
 */
public class LocalUser {

    private static ThreadLocal<UserDO> local = new ThreadLocal<>();

    /**
     * 得到当前登录用户
     *
     * @return user | null
     */
    public static UserDO getLocalUser() {
        return LocalUser.local.get();
    }

    /**
     * 设置登录用户
     *
     * @param user user
     */
    public static void setLocalUser(UserDO user) {
        LocalUser.local.set(user);
    }

    public static <T> T getLocalUser(Class<T> clazz) {
        return (T) local.get();
    }

    /**
     * 清理当前用户
     */
    public static void clearLocalUser() {
        LocalUser.local.remove();
    }
}

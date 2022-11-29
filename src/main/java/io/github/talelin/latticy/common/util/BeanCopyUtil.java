package io.github.talelin.latticy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Gadfly
 */
@Slf4j
public class BeanCopyUtil extends BeanUtils {
    public static void copyNonNullProperties(Object source, Object target) {
        String[] properties = Arrays.stream(ReflectionUtils.getDeclaredMethods(source.getClass()))
                .map(method -> {
                    if (method.getName().startsWith("get")) {
                        Object fieldValue = null;
                        try {
                            fieldValue = method.invoke(source);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        if (fieldValue == null) {
                            String fieldName = method.getName().substring(3);
                            return com.baomidou.mybatisplus.core.toolkit.StringUtils.firstToLowerCase(fieldName);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        copyProperties(source, target, properties);
    }

    /**
     * 集合数据的拷贝
     *
     * @param source: 数据源类
     * @param target: 目标类::new(eg: UserVO::new)
     * @return 拷贝后的集合
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }

    /**
     * 集合数据的拷贝
     *
     * @param sources: 数据源类
     * @param target:  目标类::new(eg: UserVO::new)
     * @return 拷贝后的集合
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param sources:  数据源类
     * @param target:   目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @return 拷贝后的集合
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target,
                                                    BeanCopyUtilCallBack<S, T> callBack) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
            if (null != callBack) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

    public static <S, T> T copySingleProperties(S source, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        T t = target.get();
        copyProperties(source, t);
        if (null != callBack) {
            // 回调
            callBack.callBack(source, t);
        }
        return t;
    }

    /**
     * 将集合对象中的类型转换成另一种类型
     *
     * @param collection 集合
     * @param clazz      目标对象
     * @return 转换后的集合
     */
    public static <S, T> Collection<T> covertObject(Collection<S> collection, Class<T> clazz,
                                                    BeanCopyUtilCallBack<S, T> callBack) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream().map(oldObject -> {
            T instance = null;
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                copyProperties(oldObject, instance);
                if (null != callBack) {
                    // 回调
                    callBack.callBack(oldObject, instance);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return instance;
        }).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface BeanCopyUtilCallBack<S, T> {

        /**
         * 定义默认回调方法
         *
         * @param t target
         * @param s source
         */
        void callBack(S s, T t);
    }
}

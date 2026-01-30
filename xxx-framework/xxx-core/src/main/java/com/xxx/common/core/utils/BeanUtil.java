package com.xxx.common.core.utils;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiezm
 */
public class BeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 将源对象拷贝到新集合
     *
     * @param originalList 源对象集合
     * @param targetClass  目标对象类
     * @return
     */
    public static <T> List<T> copyToNewList(List<?> originalList, Class<T> targetClass) {
        if (originalList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>(originalList.size());
        for (Object originalObject : originalList) {

            T targetObject = instantiate(targetClass);
            copyBean(originalObject, targetObject);
            targetList.add(targetObject);
        }
        return targetList;
    }

    /**
     * 将源对象的属性拷贝到新对象中，只拷贝源对象非空的属性
     *
     * @param originalObject 源对象
     * @param targetClass    目标对象类
     */
    public static <T> T copyToNewBean(Object originalObject, Class<T> targetClass) {
        if (originalObject == null) {
            return null;
        }
        T targetObject = instantiate(targetClass);
        copyBean(originalObject, targetObject);
        return targetObject;
    }

    /**
     * 将源对象的属性拷贝到目标对象中，只拷贝源对象非空的属性
     *
     * @param originalObject 源对象
     * @param targetObject   目标对象
     */
    public static <T> void copyBean(Object originalObject, T targetObject) {
        if (originalObject == null) {
            targetObject = null;
        }
        try {
            if (originalObject instanceof DynaBean) {
                DynaProperty[] origDescriptors = ((DynaBean) originalObject).getDynaClass().getDynaProperties();
                for (DynaProperty origDescriptor : origDescriptors) {
                    String name = origDescriptor.getName();
                    if (PropertyUtils.isWriteable(targetObject, name)) {
                        Object value = ((DynaBean) originalObject).get(name);
                        copyPropertyNotNull(targetObject, name, value);
                    }
                }
            } else if (originalObject instanceof Map) {
                for (Object o : ((Map<?, ?>) originalObject).keySet()) {
                    String name = (String) o;
                    if (PropertyUtils.isWriteable(targetObject, name)) {
                        Object value = ((Map<?, ?>) originalObject).get(name);
                        copyPropertyNotNull(targetObject, name, value);
                    }
                }
            } else {
                PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(originalObject);
                for (PropertyDescriptor origDescriptor : origDescriptors) {
                    String name = origDescriptor.getName();
                    if ("class".equals(name)) {
                        continue;
                    }
                    if (!PropertyUtils.isReadable(originalObject, name) || !PropertyUtils.isWriteable(targetObject, name)) {
                        continue;
                    }
                    Object value = PropertyUtils.getSimpleProperty(originalObject, name);
                    copyPropertyNotNull(targetObject, name, value);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 拷贝非null 的属性
     */
    private static <T> void copyPropertyNotNull(T bean, String propertyName, Object propertyValue) throws InvocationTargetException, IllegalAccessException {
        if (null != propertyValue) {
            org.apache.commons.beanutils.BeanUtils.copyProperty(bean, propertyName, propertyValue);
        }
    }

    public static <T> T instantiate(Class<T> clazz) throws RuntimeException {
        if (clazz.isInterface()) {
            throw new RuntimeException(String.format("Failed to instantiate [%s]:  Specified class is an interface", clazz.getName()));
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(String.format("Failed to instantiate [%s]: Is it an abstract class?", clazz.getName()));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(String.format("Failed to instantiate [%s]: Is the constructor accessible?", clazz.getName()));
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(String.format("Failed to instantiate [%s]: Is the constructor invocation?", clazz.getName()));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(String.format("Failed to instantiate [%s]: Is the constructor no such method?", clazz.getName()));
        }
    }

}
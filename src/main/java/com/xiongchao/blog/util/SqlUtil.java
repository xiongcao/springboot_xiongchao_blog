package com.xiongchao.blog.util;

import com.alibaba.druid.util.StringUtils;
import com.xiongchao.blog.bean.BaseEntity;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by gavin on 5/10/2018.
 */
public class SqlUtil {

    public static String sqlGenerate(String prefix, Class clazz) {
        if (StringUtils.isEmpty(prefix)) {
            throw new RuntimeException("表名前缀不能为空");
        }
        prefix += ".";

        Field[] fields = clazz.getDeclaredFields();
        Class clazzSuper = clazz.getSuperclass();
        if (clazzSuper != null) {
            fields = (Field[]) addAll(clazzSuper.getDeclaredFields(), fields);
        }
        if (fields.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            String name = field.getName();
            sb.append(",").append(prefix).append(camelToUnderline(name));
        }
        return sb.subSequence(1, sb.length()).toString();
    }

    public static <T> T toBean(Object[] objects, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Class clazzSuper = clazz.getSuperclass();
            if (clazzSuper != null) {
                fields = (Field[]) addAll(clazzSuper.getDeclaredFields(), fields);
            }

            for (int i = 0, j = 0, lengthi = fields.length; i < lengthi; i++) {//忽略第一个serialVersionUID字段
                if (Modifier.isFinal(fields[i].getModifiers())) {
                    continue;
                }
                String name = fields[i].getName();
                Class paramType = fields[i].getType();
                Method method = clazz.getMethod("set" + firstUp(name), paramType);
                method.invoke(t, objects[j]);
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    private static String firstUp(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static final char UNDERLINE = '_';

    //驼峰变下划线
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0 && Character.isLowerCase(param.charAt(i - 1))) {
                    sb.append(UNDERLINE);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //下划线变驼峰
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //合并非null属性
    public static <T> T mergeObject(T origin, T destination) {
        if (origin == null || destination == null)
            return destination;
        if (!origin.getClass().equals(destination.getClass()))
            return destination;

        Field[] fields = origin.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(origin);
                if (null != value) {
                    field.set(destination, value);
                }
                field.setAccessible(false);
            } catch (Exception ignored) {
            }
        }
        return destination;
    }

    //根据属性名和属性值设定参数
    private static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    public static <T> String setField(T t, String field, String value) {
        if (StringUtils.isEmpty(field) || StringUtils.isEmpty(value) || null == t) {
            throw new RuntimeException("字段名,字段值,实体类不能为空");
        }
        String propertyClassName;
        try {
            Class clazz = t.getClass();
            Field property;
            try {
                property = clazz.getDeclaredField(field);
            }catch (NoSuchFieldException e) {
                property = BaseEntity.class.getDeclaredField(field);
            }
            property.setAccessible(true);
            Class propertyClazz = property.getType();
            propertyClassName = propertyClazz.getName();
            Object value2;
            switch (propertyClassName) {
                case "java.lang.String":
                    value2 = value;
                    break;
                case "java.lang.Integer":
                    if (value.endsWith("万")) {
                        value = value.substring(0, value.length() -1);
                        Float value3 = Float.parseFloat(value) * 10000;
                        value2 = value3.intValue();
                    }else {
                        value2 = Integer.parseInt(value);
                    }
                    break;
                case "java.math.BigDecimal":
                    value2 = BigDecimal.valueOf(Double.parseDouble(value)).setScale(0, BigDecimal.ROUND_HALF_UP);
                    break;
                case "java.util.Date":
                    value2 = sdf.parse(value);
                    break;
                case "java.lang.Float":
                    value2 = Float.parseFloat(value);
                    break;
                default:
                    throw new RuntimeException("目前不支持该类型数据");
            }
//            String methodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
//            Method method = clazz.getMethod(methodName, propertyClazz);
//            method.invoke(t, value2);
            property.set(t, value2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("参数设置异常");
        }
        return propertyClassName.substring(propertyClassName.lastIndexOf(".") + 1, propertyClassName.length());
    }

    private static Object[] addAll(Object[] array1, Object[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            Object[] joinedArray = (Object[]) ((Object[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length));
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);

            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return joinedArray;
            } catch (ArrayStoreException var6) {
                Class type1 = array1.getClass().getComponentType();
                Class type2 = array2.getClass().getComponentType();
                if (!type1.isAssignableFrom(type2)) {
                    throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName());
                } else {
                    throw var6;
                }
            }
        }
    }

    private static Object[] clone(Object[] array) {
        return array == null ? null : (Object[]) ((Object[]) array.clone());
    }
}

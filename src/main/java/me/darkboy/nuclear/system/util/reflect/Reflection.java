package me.darkboy.nuclear.system.util.reflect;

import java.lang.reflect.*;

/**
 * The {@link Reflection} class provides thread-safe reflection
 * utility methods.
 * <p>
 * Note that some results may be due to exceptions being thrown
 * rather than the expected.
 *
 * @author NotStxnks_
 */
public final class Reflection {

    // Prevent us from creating a new empty class/object every time the method is called.
    private static final Class<?>[] CLASSES = new Class<?>[] {};
    private static final Object[] OBJECTS = new Object[] {};

    // Suppresses default constructor.
    private Reflection() {}

    /**
     * Attempts to create an instance of the given class.
     *
     * @param clazz class to create an instance of.
     * @param <T>   class type.
     * @return an initialised class object.
     */
    public static <T> T newInstance(Class<T> clazz) {
        return newInstance(clazz, CLASSES);
    }

    /**
     * @param clazz          class to create an instance of.
     * @param parameterTypes constructor parameter types.
     * @param initargs       constructor arguments objects.
     * @param <T>            class type.
     * @return an initialised class object.
     */
    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... initargs) {
        T instance = null;

        try {
            instance = getConstructor(clazz, parameterTypes).newInstance(initargs);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            // Ignored
        }

        return instance;
    }

    // Getters: Class

    /**
     * @param name the fully qualified name of the desired class.
     * @return the {@code Class} object for the class with the specified name.
     */
    public static Class<?> getClass(String name) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            // Ignored
        }

        return clazz;
    }

    // Getters: Constructor

    /**
     * @param clazz          class containing the constructor.
     * @param parameterTypes parameter types of the constructor.
     * @param <T>            constructor type.
     * @return the class's constructor with the matching parameter types.
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        Constructor<T> constructor = null;

        try {
            constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // Ignored
        }

        return constructor;
    }

    // Getters: Field

    /**
     * @param clazz class to check.
     * @param name  field name.
     * @return whether the class contains a field with the specified name.
     */
    public static boolean hasField(Class<?> clazz, String name) {
        return getField(clazz, name) != null;
    }

    /**
     * Gets the field in the class and allows accessibility before returning.
     *
     * @param clazz class containing the field.
     * @param name  field name.
     * @return the field.
     */
    public static Field getField(Class<?> clazz, String name) {
        Field field = null;
        Class<?> c = clazz;

        while (c != null && c != Object.class) {
            try {
                if (c.getDeclaredField(name) != null) {
                    field = c.getDeclaredField(name);
                    setAccessible(field, true);
                    break;
                }
            } catch (NoSuchFieldException | NullPointerException | SecurityException e) {
                // Ignored
            }

            c = c.getSuperclass();
        }

        return field;
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object. The value is automatically wrapped in an
     * object if it has a primitive type.
     *
     * @param field  the field to retrieve the value from.
     * @param object object from which the represented field's value is
     *               to be extracted
     * @param <T>    value type.
     * @return the value of the represented field in object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object object) {
        setAccessible(field, true);
        T value = null;

        try {
            value = (T) field.get(object);
        } catch (IllegalAccessException e) {
            // Ignored
        }

        return value;
    }

    /**
     * @param clazz  class containing the field.
     * @param name   field name.
     * @param object the object we want.
     * @param <T>    object type.
     * @return value of the object.
     */
    public static <T> T getFieldValue(Class<?> clazz, String name, Object object) {
        return getFieldValue(getField(clazz, name), object);
    }


    // Getters: Method

    /**
     * @param clazz          class containing the method.
     * @param name           method name.
     * @param parameterTypes method parameter types.
     * @return method with the same name and parameter types.
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        Method method = null;

        try {
            method = clazz.getMethod(name, parameterTypes);
            setAccessible(method, true);
        } catch (NoSuchMethodException | NullPointerException | SecurityException  e) {
            // Ignored
        }

        return method;
    }

    /**
     * @param clazz class containing the method.
     * @param name  method name.
     * @return method with the same name with no parameters.
     */
    public static Method getMethod(Class<?> clazz, String name) {
        return getMethod(clazz, name, CLASSES);
    }

    /**
     * By passing an Object argument rather than a Class, we're able to determine
     * if the methods have been implemented.
     * <p>
     * If the super class has a method, but it has not been implemented in the
     * object provided then {@code false} is returned.
     *
     * @param object         the object containing the method.
     * @param name           method name.
     * @param parameterTypes method parameter types.
     * @return whether the class contains the method.
     */
    public static boolean hasMethod(Object object, String name, Class<?>... parameterTypes) {
        Class<?> clazz = object.getClass();
        return getMethod(clazz, name, parameterTypes).getDeclaringClass().equals(clazz);
    }

    /**
     * @param clazz          class containing the method.
     * @param name           method name.
     * @param parameterTypes method parameter types.
     * @return whether the class contains the method.
     */
    public static boolean hasMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return getMethod(clazz, name, parameterTypes) != null;
    }

    /**
     * @param clazz class containing the method.
     * @param name  method name.
     * @return whether the class contains the method.
     */
    public static boolean hasMethod(Class<?> clazz, String name) {
        return hasMethod(clazz, name, CLASSES);
    }

    /**
     * @param object         the object containing the method.
     * @param name           method name.
     * @return whether the class contains the method.
     */
    public static boolean hasMethod(Object object, String name) {
        return hasMethod(object, name, CLASSES);
    }

    /**
     * @param method to invoke.
     * @param object instance containing the method.
     * @param args   method arguments.
     * @param <T>    return type.
     * @return result from invoking the method.
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Method method, Object object, Object... args) {
        T result = null;

        try {
            result = (T) method.invoke(object, args);
        } catch (Exception e) {
            // Ignored
        }

        return result;
    }

    /**
     * @param method to invoke.
     * @param object instance containing the method.
     * @param <T>    return type.
     * @return result from invoking the method.
     */
    public static <T> T invokeMethod(Method method, Object object) {
        return invokeMethod(method, object, OBJECTS);
    }

    /**
     * @param methodName method's name to invoke.
     * @param object     instance containing the method.
     * @param args       method arguments.
     * @param <T>        return type.
     * @return result from invoking the method.
     */
    public static <T> T invokeMethod(String methodName, Object object, Object... args) {
        return invokeMethod(getMethod(object.getClass(), methodName), object, args);
    }


    // Getters: Singleton

    /**
     * @param clazz class to check.
     * @return if the class is a singleton.
     */
    public static boolean isSingleton(Class<?> clazz) {
        return hasMethod(clazz, "getInstance") || hasMethod(clazz, "get");
    }

    /**
     * @param clazz class to get the instance from.
     * @param <T>   instance type.
     * @return the singleton's instance.
     */
    public static <T> T getSingleton(Class<?> clazz) {
        if (!isSingleton(clazz)) {
            return null;
        }

        Method getInstanceMethod = getMethod(clazz, "getInstance");

        if (getInstanceMethod == null) {
            getInstanceMethod = getMethod(clazz, "get");
        }

        return invokeMethod(getInstanceMethod, null);
    }

    // Setters

    /**
     * Sets the accessibility of a field. If access is allowed,
     * the final modifier will be removed.
     *
     * @param field      the field to modify.
     * @param accessible whether the field should be accessible.
     */
    public static void setAccessible(Field field, boolean accessible) {
        try {
            field.setAccessible(accessible);

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(accessible);

            if (accessible) {
                modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
        } catch (Exception e) {
            // Ignored
        }
    }

    /**
     * @param method     the method to modify.
     * @param accessible whether the method should be accessible.
     */
    public static void setAccessible(Method method, boolean accessible) {
        try {
            method.setAccessible(accessible);
        } catch (SecurityException e) {
            // Ignored
        }
    }

    /**
     * @param field  the field to modify.
     * @param object the object containing the field.
     * @param value  new value for the field.
     */
    public static void setFieldValue(Field field, Object object, Object value) {
        setAccessible(field, true);

        try {
            field.set(object, value);
        } catch (Exception e) {
            // Ignored
        }
    }

    /**
     * @param fieldName name of the field.
     * @param object    the object containing the field.
     * @param value     new value for the field.
     */
    public static void setFieldValue(String fieldName, Object object, Object value) {
        setFieldValue(getField(object.getClass(), fieldName), object, value);
    }
}
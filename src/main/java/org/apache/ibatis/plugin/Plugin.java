/**
 * Copyright 2009-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.reflection.ExceptionUtil;

/**
 * @author Clinton Begin
 */
public class Plugin implements InvocationHandler {

    private final Object target;
    private final Interceptor interceptor;
    private final Map<Class<?>, Set<Method>> signatureMap;

    private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        // 获取自定义Interceptor实现类上的@Signature注解信息，
        // 这里的getSignatureMap()方法会解析@Signature注解，得到要拦截的类以及要拦截的方法集合
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        //System.err.println("type: " + type.toString());
        // 检查当前传入的target对象是否为@Signature注解要拦截的类型，如果是的话，就
        // 使用JDK动态代理的方式创建代理对象
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        if (interfaces.length > 0) {
            // 创建JDK动态代理
            return Proxy.newProxyInstance(
                    type.getClassLoader(),
                    interfaces,
                    // 这里使用的InvocationHandler就是Plugin本身
                    new Plugin(target, interceptor, signatureMap));
        }
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Set<Method> methods = signatureMap.get(method.getDeclaringClass());
            if (methods != null && methods.contains(method)) {
                return interceptor.intercept(new Invocation(target, method, args));
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }

    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        // issue #251
        if (interceptsAnnotation == null) {
            throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }
        Signature[] sigs = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
        for (Signature sig : sigs) {
            Set<Method> methods = signatureMap.computeIfAbsent(sig.type(), k -> new HashSet<>());
            try {
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
            }
        }
        return signatureMap;
    }

    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        /**
         *interfaces.toArray 下面使用的是java数组的动态初始化：new Class<?>[0] 初始的数组长度为0
         *动态初始化：初始化时由程序员指定数组的长度，由系统初始化每个数组元素的默认值。
         *type[] arrayName = new type[length];
         *示例：
         *int[] price = new int[4];
         */
        /**
         * 觉得此处的new Class<?>[0] 写成 new Class<?>[interfaces.size()] 会减少歧义；
         * new Class<?>[0]初始的数组长度为0，居然够对interfaces集合进行转储的原因在于：
         * @see AbstractCollection  toArray(T[] a)方法
         * AbstractCollection中重写toArray方法中对数组的长度根据集合的大小进行调整，如下代码：
         *         // Estimate size of array; be prepared to see more or fewer elements
         *         int size = size();
         *         T[] r = a.length >= size ? a :
         *                   (T[])java.lang.reflect.Array
         *                   .newInstance(a.getClass().getComponentType(), size);
         */
        return interfaces.toArray(new Class<?>[0]);
    }

}

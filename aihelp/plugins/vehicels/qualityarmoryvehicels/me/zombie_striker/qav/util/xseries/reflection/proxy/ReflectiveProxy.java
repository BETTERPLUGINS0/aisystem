/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ClassOverloadedMethods;
import me.zombie_striker.qav.util.xseries.reflection.proxy.OverloadedMethod;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.MappedType;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ProxyMethodInfo;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ReflectiveProxy<T extends ReflectiveProxyObject>
implements InvocationHandler {
    private static final Map<Class<?>, ReflectiveProxy<?>> PROXIFIED_CLASS_LOADER0 = new IdentityHashMap();
    private static final ClassLoader CLASS_LOADER = ReflectiveProxy.class.getClassLoader();
    private final Class<?> targetClass;
    private final Class<T> proxyClass;
    private T proxy;
    private final Object instance;
    private final Map<Method, ProxifiedObject> handles;
    private final ClassOverloadedMethods<ProxifiedObject> nameMapped;

    public static <T extends ReflectiveProxyObject> ReflectiveProxy<T> proxify(Class<T> clazz) {
        ReflectiveProxy<?> reflectiveProxy = PROXIFIED_CLASS_LOADER0.get(clazz);
        if (reflectiveProxy != null) {
            return reflectiveProxy;
        }
        ReflectiveAnnotationProcessor reflectiveAnnotationProcessor = new ReflectiveAnnotationProcessor(clazz);
        reflectiveAnnotationProcessor.process(ReflectiveProxy::descriptorProcessor);
        Set<Map.Entry<String, OverloadedMethod<ProxyMethodInfo>>> set = reflectiveAnnotationProcessor.getMapped().mappings().entrySet();
        IdentityHashMap<Method, ProxifiedObject> identityHashMap = new IdentityHashMap<Method, ProxifiedObject>(set.size());
        OverloadedMethod.Builder<ProxifiedObject> builder = new OverloadedMethod.Builder<ProxifiedObject>(ReflectiveProxy::descriptorProcessor);
        ReflectiveProxy<T> reflectiveProxy2 = new ReflectiveProxy<T>(reflectiveAnnotationProcessor.getTargetClass(), clazz, null, identityHashMap, builder.build());
        PROXIFIED_CLASS_LOADER0.put(clazz, reflectiveProxy2);
        for (Map.Entry<String, OverloadedMethod<ProxyMethodInfo>> entry : set) {
            for (ProxyMethodInfo object2 : entry.getValue().getOverloads()) {
                ReflectedObject reflectedObject = object2.handle.jvm().unreflect();
                MethodHandle methodHandle = (MethodHandle)object2.handle.unreflect();
                methodHandle = ReflectiveProxy.createDynamicProxy(null, methodHandle);
                ProxifiedObject proxifiedObject = new ProxifiedObject(methodHandle, object2, reflectedObject.accessFlags().contains((Object)XAccessFlag.STATIC), reflectedObject.type() == ReflectedObject.Type.CONSTRUCTOR, object2.rType.isDifferent() ? ReflectiveProxy.proxify(object2.rType.synthetic) : null, Arrays.stream(object2.pTypes).anyMatch(MappedType::isDifferent) ? (ReflectiveProxy[])Arrays.stream(object2.pTypes).map(mappedType -> mappedType.isDifferent() ? ReflectiveProxy.proxify(mappedType.synthetic) : null).toArray(ReflectiveProxy[]::new) : null);
                identityHashMap.put(object2.interfaceMethod, proxifiedObject);
                builder.add(proxifiedObject, entry.getKey());
            }
        }
        builder.build(reflectiveProxy2.nameMapped.mappings());
        reflectiveProxy2.proxy = reflectiveProxy2.createProxy();
        for (Method method : reflectiveProxy2.proxy.getClass().getDeclaredMethods()) {
            ProxifiedObject proxifiedObject = reflectiveProxy2.nameMapped.get(method.getName(), () -> ReflectiveProxy.descriptorProcessor(method), true);
            if (proxifiedObject == null) continue;
            identityHashMap.put(method, proxifiedObject);
        }
        for (GenericDeclaration genericDeclaration : reflectiveProxy2.proxy.getClass().getInterfaces()) {
            for (Method method : ((Class)genericDeclaration).getDeclaredMethods()) {
                ProxifiedObject proxifiedObject = reflectiveProxy2.nameMapped.get(method.getName(), () -> ReflectiveProxy.descriptorProcessor(method), true);
                if (proxifiedObject == null) continue;
                identityHashMap.put(method, proxifiedObject);
            }
        }
        return reflectiveProxy2;
    }

    private static MethodHandle createDynamicProxy(@Nullable Object object, MethodHandle methodHandle) {
        int n;
        int n2 = methodHandle.type().parameterCount();
        int n3 = n = object != null ? 1 : 0;
        if (object != null) {
            methodHandle = methodHandle.bindTo(object);
        }
        if (n2 == n) {
            return methodHandle.asType(MethodType.methodType(Object.class));
        }
        return methodHandle.asSpreader(Object[].class, n2 - n).asType(MethodType.methodType(Object.class, Object[].class));
    }

    private static String descriptorProcessor(ProxifiedObject proxifiedObject) {
        return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(((ProxifiedObject)proxifiedObject).proxyMethodInfo.pTypes));
    }

    private static String descriptorProcessor(ProxyMethodInfo proxyMethodInfo) {
        return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(proxyMethodInfo.pTypes));
    }

    private static String descriptorProcessor(Method method) {
        return OverloadedMethod.getParameterDescriptor(method.getParameterTypes());
    }

    private ReflectiveProxy(Class<?> clazz, Class<T> clazz2, Object object, Map<Method, ProxifiedObject> map, ClassOverloadedMethods<ProxifiedObject> classOverloadedMethods) {
        this.targetClass = clazz;
        this.proxyClass = clazz2;
        this.instance = object;
        this.handles = map;
        this.nameMapped = classOverloadedMethods;
    }

    public static void checkInterfaceClass(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Interface class is null");
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Cannot proxify non-interface class: " + clazz);
        }
        if (!ReflectiveProxyObject.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The provided interface class must extend ReflectiveProxyObject interface");
        }
    }

    @ApiStatus.Internal
    @NotNull
    public T createProxy() {
        return (T)((ReflectiveProxyObject)Proxy.newProxyInstance(CLASS_LOADER, new Class[]{this.proxyClass}, this));
    }

    @NotNull
    public T proxy() {
        return this.proxy;
    }

    @Nullable
    public Object instance() {
        return this.instance;
    }

    @NotNull
    public T bindTo(@NotNull Object object) {
        if (this.instance != null) {
            throw new IllegalStateException("This proxy object already has an instance bound to it: " + this);
        }
        Objects.requireNonNull(object, "Instance cannot be null");
        if (!this.targetClass.isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException("The given instance doesn't match the target class: " + object + " -> " + this);
        }
        IdentityHashMap<Method, ProxifiedObject> identityHashMap = new IdentityHashMap<Method, ProxifiedObject>(this.handles.size());
        IdentityHashMap<ProxifiedObject, ProxifiedObject> identityHashMap2 = new IdentityHashMap<ProxifiedObject, ProxifiedObject>(this.nameMapped.mappings().size());
        OverloadedMethod.Builder<ProxifiedObject> builder = new OverloadedMethod.Builder<ProxifiedObject>(ReflectiveProxy::descriptorProcessor);
        for (Map.Entry<String, OverloadedMethod<ProxifiedObject>> entry2 : this.nameMapped.mappings().entrySet()) {
            for (ProxifiedObject proxifiedObject : entry2.getValue().getOverloads()) {
                ProxifiedObject proxifiedObject2 = proxifiedObject;
                ProxifiedObject proxifiedObject3 = (ProxifiedObject)identityHashMap2.get(proxifiedObject2);
                if (proxifiedObject3 == null) {
                    MethodHandle methodHandle;
                    if (proxifiedObject2.isStatic || proxifiedObject2.isConstructor) {
                        builder.add(proxifiedObject2, entry2.getKey());
                        continue;
                    }
                    try {
                        methodHandle = (MethodHandle)((ProxifiedObject)proxifiedObject2).proxyMethodInfo.handle.unreflect();
                        if (methodHandle.type().parameterCount() == 0) {
                            throw new IllegalStateException("Non-static, non-constructor with 0 arguments found: " + methodHandle);
                        }
                        methodHandle = ReflectiveProxy.createDynamicProxy(object, methodHandle);
                    } catch (Exception exception) {
                        throw new IllegalStateException("Failed to bind " + object + " to " + entry2.getKey() + " -> " + proxifiedObject2.handle + " (static=" + proxifiedObject2.isStatic + ", constructor=" + proxifiedObject2.isConstructor + ')', exception);
                    }
                    proxifiedObject3 = new ProxifiedObject(methodHandle, proxifiedObject2.proxyMethodInfo, proxifiedObject2.isStatic, proxifiedObject2.isConstructor, proxifiedObject2.rType, proxifiedObject2.pTypes);
                    identityHashMap2.put(proxifiedObject2, proxifiedObject3);
                }
                builder.add(proxifiedObject3, entry2.getKey());
            }
        }
        for (Map.Entry<Object, Object> entry2 : this.handles.entrySet()) {
            ProxifiedObject proxifiedObject;
            ProxifiedObject proxifiedObject4 = (ProxifiedObject)entry2.getValue();
            if (proxifiedObject4.isStatic || proxifiedObject4.isConstructor) {
                identityHashMap.put((Method)entry2.getKey(), proxifiedObject4);
                continue;
            }
            proxifiedObject = (ProxifiedObject)identityHashMap2.get(proxifiedObject4);
            if (proxifiedObject == null) {
                throw new IllegalStateException("Cannot find bound method for " + entry2.getKey() + " (" + proxifiedObject4 + "::" + proxifiedObject4.hashCode() + ") in " + builder.build() + " - " + identityHashMap2.entrySet().stream().map(entry -> entry.getKey() + "::" + entry.hashCode()).collect(Collectors.toList()));
            }
            identityHashMap.put((Method)entry2.getKey(), proxifiedObject);
        }
        ReflectiveProxy<T> reflectiveProxy = new ReflectiveProxy<T>(this.targetClass, this.proxyClass, object, identityHashMap, builder.build());
        return reflectiveProxy.createProxy();
    }

    private static String getMethodList(Class<?> clazz, boolean bl) {
        return Arrays.stream(bl ? clazz.getDeclaredMethods() : clazz.getMethods()).map(method -> method.getName() + "::" + System.identityHashCode(method)).collect(Collectors.toList()).toString();
    }

    @Override
    public Object invoke(Object object2, Method method, @Nullable Object[] objectArray) {
        int n = method.getParameterCount();
        Object object3 = method.getName();
        if (n == 0) {
            switch (object3) {
                case "instance": {
                    return this.instance;
                }
                case "toString": {
                    return this.instance == null ? this.proxyClass.toString() : this.instance.toString();
                }
                case "hashCode": {
                    return this.instance == null ? this.proxyClass.hashCode() : this.instance.hashCode();
                }
                case "notify": {
                    if (this.instance == null) {
                        this.proxyClass.notify();
                    } else {
                        this.instance.notify();
                    }
                    return null;
                }
                case "notifyAll": {
                    if (this.instance == null) {
                        this.proxyClass.notifyAll();
                    } else {
                        this.instance.notifyAll();
                    }
                    return null;
                }
                case "wait": {
                    if (this.instance == null) {
                        this.proxyClass.wait();
                    } else {
                        this.instance.wait();
                    }
                    return null;
                }
                case "getTargetClass": {
                    return this.targetClass;
                }
            }
        } else if (n == 1) {
            switch (object3) {
                case "bindTo": {
                    return this.bindTo(objectArray[0]);
                }
                case "isInstance": {
                    return this.targetClass.isInstance(objectArray[0]);
                }
                case "equals": {
                    return this.instance == null ? this.proxyClass == objectArray[0] : this.instance.equals(objectArray[0]);
                }
                case "wait": {
                    if (this.instance == null) {
                        this.proxyClass.wait((Long)objectArray[0]);
                    } else {
                        this.instance.wait((Long)objectArray[0]);
                    }
                    return null;
                }
            }
        } else if (n == 2 && ((String)object3).equals("wait")) {
            if (this.instance == null) {
                this.proxyClass.wait((Long)objectArray[0], (Integer)objectArray[1]);
            } else {
                this.instance.wait((Long)objectArray[0], (Integer)objectArray[1]);
            }
            return null;
        }
        ProxifiedObject proxifiedObject = this.handles.get(method);
        if (proxifiedObject == null) {
            proxifiedObject = this.nameMapped.get(method.getName(), () -> ReflectiveProxy.descriptorProcessor(method));
            this.handles.put(method, proxifiedObject);
        }
        if (!proxifiedObject.isStatic && !proxifiedObject.isConstructor && this.instance == null) {
            throw new IllegalStateException("Cannot invoke non-static non-constructor member handle with when no instance is set");
        }
        if (proxifiedObject.isConstructor && this.instance != null) {
            throw new IllegalStateException("Cannot invoke constructor twice");
        }
        if (proxifiedObject.pTypes != null && objectArray != null) {
            for (int i = 0; i < objectArray.length; ++i) {
                Object object4 = objectArray[i];
                if (!(object4 instanceof ReflectiveProxyObject)) continue;
                objectArray[i] = ((ReflectiveProxyObject)object4).instance();
            }
        }
        try {
            object3 = objectArray == null ? proxifiedObject.handle.invokeExact() : proxifiedObject.handle.invoke(objectArray);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to execute " + method + " -> " + proxifiedObject.handle + " with args " + (objectArray == null ? "null" : Arrays.stream(objectArray).map(object -> object == null ? "null" : object + " (" + object.getClass().getSimpleName() + ')')), throwable);
        }
        if (proxifiedObject.rType != null) {
            object3 = proxifiedObject.rType.bindTo(object3);
        }
        return object3;
    }

    public String toString() {
        return "ReflectiveProxy(proxyClass=" + this.proxyClass + ", proxy=" + this.proxy + ", instance=" + this.instance + ", nameMapped=" + this.nameMapped + ')';
    }

    public static final class ProxifiedObject {
        private final MethodHandle handle;
        private final ProxyMethodInfo proxyMethodInfo;
        private final boolean isStatic;
        private final boolean isConstructor;
        private final ReflectiveProxy<?> rType;
        private final ReflectiveProxy<?>[] pTypes;

        public ProxifiedObject(MethodHandle methodHandle, ProxyMethodInfo proxyMethodInfo, boolean bl, boolean bl2, ReflectiveProxy<?> reflectiveProxy, ReflectiveProxy<?>[] reflectiveProxyArray) {
            this.handle = methodHandle;
            this.proxyMethodInfo = proxyMethodInfo;
            this.isStatic = bl;
            this.isConstructor = bl2;
            this.rType = reflectiveProxy;
            this.pTypes = reflectiveProxyArray;
        }

        public String toString() {
            return this.getClass().getSimpleName() + '(' + this.proxyMethodInfo.interfaceMethod + ')';
        }
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.processors;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.StaticReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.aggregate.VersionHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NameableReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ClassOverloadedMethods;
import me.zombie_striker.qav.util.xseries.reflection.proxy.OverloadedMethod;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxy;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Field;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Final;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Ignore;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.MappedMinecraftName;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Private;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Protected;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Proxify;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.ReflectMinecraftPackage;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.ReflectName;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Static;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.MappedType;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ProxyMethodInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class ReflectiveAnnotationProcessor {
    private final Class<? extends ReflectiveProxyObject> interfaceClass;
    private ClassOverloadedMethods<ProxyMethodInfo> mapped;
    private OverloadedMethod.Builder<ProxyMethodInfo> mappedHandles;
    private Class<?> targetClass;

    public ReflectiveAnnotationProcessor(Class<? extends ReflectiveProxyObject> clazz) {
        ReflectiveProxy.checkInterfaceClass(clazz);
        this.interfaceClass = clazz;
    }

    private void error(String string) {
        this.error(string, null);
    }

    private void error(String string, Throwable throwable) {
        throw new IllegalStateException(string + " (Proxified Interface: " + this.interfaceClass + ')', throwable);
    }

    protected static boolean isAnnotationInherited(Class<?> clazz, Method method, Class<? extends Annotation> clazz2) {
        try {
            Method method2 = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (method2.isAnnotationPresent(clazz2)) {
                return true;
            }
        } catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        for (Class<?> clazz3 : clazz.getInterfaces()) {
            if (!ReflectiveAnnotationProcessor.isAnnotationInherited(clazz3, method, clazz2)) continue;
            return true;
        }
        return false;
    }

    public void loadDependencies(Function<Class<?>, Boolean> function) {
        for (OverloadedMethod<ProxyMethodInfo> overloadedMethod : this.mapped.mappings().values()) {
            for (ProxyMethodInfo proxyMethodInfo : overloadedMethod.getOverloads()) {
                this.loadDependency(proxyMethodInfo.rType, function);
                for (MappedType mappedType : proxyMethodInfo.pTypes) {
                    this.loadDependency(mappedType, function);
                }
            }
        }
    }

    private void loadDependency(MappedType mappedType, Function<Class<?>, Boolean> function) {
        if (ReflectiveProxyObject.class.isAssignableFrom(mappedType.synthetic) && mappedType.synthetic != this.interfaceClass && !function.apply(mappedType.synthetic).booleanValue()) {
            XReflection.proxify(mappedType.synthetic);
        }
    }

    public void process(Function<ProxyMethodInfo, String> function) {
        ClassHandle classHandle = this.processTargetClass();
        Method[] methodArray = this.interfaceClass.getMethods();
        this.mappedHandles = new OverloadedMethod.Builder<ProxyMethodInfo>(function);
        for (Method method : methodArray) {
            Object object;
            Object object2;
            MappedType mappedType;
            Class<?> clazz;
            if (ReflectiveAnnotationProcessor.isAnnotationInherited(this.interfaceClass, method, Ignore.class)) continue;
            boolean bl = method.isAnnotationPresent(Static.class);
            boolean bl2 = method.isAnnotationPresent(Final.class);
            MappedType[] mappedTypeArray = new MappedType[]{};
            if (method.isAnnotationPresent(me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Constructor.class)) {
                clazz = method.getReturnType();
                if (clazz != this.targetClass && clazz != this.interfaceClass && clazz != Object.class) {
                    this.error("Method marked with @Constructor must return Object.class, " + this.targetClass + " or " + this.interfaceClass);
                }
                mappedType = this.unwrap(clazz);
                mappedTypeArray = this.unwrap(method.getParameterTypes());
                object2 = classHandle.constructor(MappedType.getRealTypes(mappedTypeArray));
                if (bl) {
                    this.error("Constructor cannot be static: " + method);
                }
                if (bl2) {
                    this.error("Constructor cannot be final: " + method);
                }
            } else if (method.isAnnotationPresent(Field.class)) {
                clazz = classHandle.field();
                if (method.getReturnType() == Void.TYPE) {
                    ((FieldMemberHandle)((Object)clazz)).setter();
                    if (method.getParameterCount() != 1) {
                        this.error("Field setter method must have only one parameter: " + method);
                    }
                    object = this.unwrap(method.getParameterTypes()[0]);
                    mappedType = new MappedType(Void.TYPE, Void.TYPE);
                    mappedTypeArray = new MappedType[]{object};
                    ((FieldMemberHandle)((Object)clazz)).returns((Class)((MappedType)object).real);
                } else {
                    ((FieldMemberHandle)((Object)clazz)).getter();
                    if (method.getParameterCount() != 0) {
                        this.error("Field getter method must not have any parameters: " + method);
                    }
                    mappedType = this.unwrap(method.getReturnType());
                    ((FieldMemberHandle)((Object)clazz)).returns((Class)mappedType.real);
                }
                if (bl) {
                    ((FieldMemberHandle)((Object)clazz)).asStatic();
                }
                if (bl2) {
                    ((FieldMemberHandle)((Object)clazz)).asFinal();
                }
                object2 = clazz;
            } else {
                mappedType = this.unwrap(method.getReturnType());
                mappedTypeArray = this.unwrap(method.getParameterTypes());
                clazz = ((MethodMemberHandle)classHandle.method().returns((Class)mappedType.real)).parameters(MappedType.getRealTypes(mappedTypeArray));
                if (bl) {
                    clazz = ((MethodMemberHandle)((Object)clazz)).asStatic();
                }
                if (bl2) {
                    this.error("Declaring method as final has no effect: " + method);
                }
                object2 = clazz;
            }
            boolean bl3 = false;
            if (method.isAnnotationPresent(Private.class)) {
                bl3 = true;
                ((MemberHandle)object2).getAccessFlags().add(XAccessFlag.PRIVATE);
            }
            if (method.isAnnotationPresent(Protected.class)) {
                if (bl3) {
                    this.error("Cannot have two visibility modifier private and protected for " + method);
                }
                ((MemberHandle)object2).getAccessFlags().add(XAccessFlag.PRIVATE);
            }
            if (object2 instanceof NameableReflectiveHandle) {
                ((NameableReflectiveHandle)object2).named(method.getName());
                this.reflectNames((NameableReflectiveHandle)object2, method);
            }
            object = object2.cached();
            try {
                object.reflect();
            } catch (ReflectiveOperationException reflectiveOperationException) {
                this.error("Failed to map " + method, reflectiveOperationException);
            }
            ProxyMethodInfo proxyMethodInfo = new ProxyMethodInfo((ReflectiveHandle<?>)object, method, mappedType, mappedTypeArray);
            this.mappedHandles.add(proxyMethodInfo, method.getName());
        }
        this.mapped = this.mappedHandles.build();
    }

    @NotNull
    public ClassHandle processTargetClass() {
        DynamicClassHandle dynamicClassHandle;
        boolean bl;
        ClassHandle classHandle;
        Proxify proxify = this.interfaceClass.getAnnotation(Proxify.class);
        ReflectMinecraftPackage reflectMinecraftPackage = this.interfaceClass.getAnnotation(ReflectMinecraftPackage.class);
        if (proxify == null && reflectMinecraftPackage == null) {
            this.error("Proxy interface is not annotated with @Class or @ReflectMinecraftPackage");
        }
        if (proxify != null && reflectMinecraftPackage != null) {
            this.error("Proxy interface cannot contain both @Class or @ReflectMinecraftPackage");
        }
        if (proxify != null) {
            if (proxify.target() != Void.TYPE) {
                classHandle = XReflection.of(proxify.target());
                bl = true;
            } else {
                dynamicClassHandle = XReflection.classHandle();
                classHandle = dynamicClassHandle;
                dynamicClassHandle.inPackage(proxify.packageName());
                bl = proxify.ignoreCurrentName();
            }
        } else {
            dynamicClassHandle = XReflection.ofMinecraft();
            classHandle = dynamicClassHandle;
            dynamicClassHandle.inPackage(reflectMinecraftPackage.type(), reflectMinecraftPackage.packageName());
            bl = reflectMinecraftPackage.ignoreCurrentName();
        }
        if (classHandle instanceof DynamicClassHandle) {
            dynamicClassHandle = (DynamicClassHandle)classHandle;
            if (!bl) {
                dynamicClassHandle.named(this.interfaceClass.getSimpleName());
            }
            this.reflectNames(dynamicClassHandle, this.interfaceClass);
        }
        this.targetClass = (Class)classHandle.unreflect();
        MappedType.LOOK_AHEAD.put(this.interfaceClass, this.targetClass);
        return classHandle;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public ClassOverloadedMethods<ProxyMethodInfo> getMapped() {
        return this.mapped;
    }

    private void reflectNames(NameableReflectiveHandle nameableReflectiveHandle, AnnotatedElement annotatedElement) {
        MappedMinecraftName[] mappedMinecraftNameArray = (MappedMinecraftName[])annotatedElement.getDeclaredAnnotationsByType(MappedMinecraftName.class);
        ReflectName[] reflectNameArray = (ReflectName[])annotatedElement.getDeclaredAnnotationsByType(ReflectName.class);
        for (MappedMinecraftName mappedMinecraftName : mappedMinecraftNameArray) {
            this.reflectNames0(nameableReflectiveHandle, mappedMinecraftName.names());
        }
        this.reflectNames0(nameableReflectiveHandle, reflectNameArray);
    }

    private void reflectDefaults() {
        try {
            Object object;
            Object object2;
            MethodHandle methodHandle = (MethodHandle)XReflection.of(MethodHandles.class).method("public static Lookup privateLookupIn(Class<?> targetClass, Lookup caller) throws IllegalAccessException").reflectOrNull();
            if (methodHandle == null) {
                object2 = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
                ((AccessibleObject)object2).setAccessible(true);
                MethodHandles.Lookup lookup = ((MethodHandles.Lookup)((Constructor)object2).newInstance(this.interfaceClass)).in(this.interfaceClass);
                Method[] methodArray = this.interfaceClass.getMethods();
                int n = methodArray.length;
                for (int i = 0; i < n; ++i) {
                    object = methodArray[i];
                    if (!((Method)object).isDefault()) continue;
                    this.handleDefaultMethod((Method)object, lookup.unreflectSpecial((Method)object, this.interfaceClass));
                }
            }
            object2 = methodHandle.invokeExact(this.interfaceClass, MethodHandles.lookup());
            for (Method method : this.interfaceClass.getMethods()) {
                if (!method.isDefault()) continue;
                object = ((MethodHandles.Lookup)object2).findSpecial(this.interfaceClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()), this.interfaceClass);
                this.handleDefaultMethod(method, (MethodHandle)object);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void handleDefaultMethod(Method method, MethodHandle methodHandle) {
        this.mappedHandles.add(new ProxyMethodInfo(new StaticReflectiveHandle<MethodHandle>(methodHandle, ReflectedObject.of(method)), method, this.unwrap(method.getReturnType()), this.unwrap(method.getParameterTypes())), method.getName());
    }

    private void reflectNames0(NameableReflectiveHandle nameableReflectiveHandle, ReflectName[] reflectNameArray) {
        if (reflectNameArray.length == 0) {
            return;
        }
        VersionHandle<String[]> versionHandle = null;
        String[] stringArray = null;
        int n = 0;
        for (ReflectName reflectName : reflectNameArray) {
            ++n;
            if (stringArray != null) {
                this.error("Cannot contain more tha one @ReflectName if no version is specified");
            }
            if (!reflectName.version().isEmpty()) {
                if (n == reflectNameArray.length) {
                    this.error("Last @ReflectName should not contain version");
                }
                int[] nArray = Arrays.stream(reflectName.version().split("\\.")).mapToInt(Integer::parseInt).toArray();
                if (versionHandle == null) {
                    if (nArray.length == 1) {
                        versionHandle = XReflection.v(nArray[0], reflectName.value());
                    }
                    if (nArray.length == 2) {
                        versionHandle = XReflection.v(nArray[1], reflectName.value());
                    }
                    if (nArray.length != 3) continue;
                    versionHandle = XReflection.v(nArray[1], nArray[2], reflectName.value());
                    continue;
                }
                if (nArray.length == 1) {
                    versionHandle.v(nArray[0], reflectName.value());
                }
                if (nArray.length == 2) {
                    versionHandle.v(nArray[1], reflectName.value());
                }
                if (nArray.length != 3) continue;
                versionHandle.v(nArray[1], nArray[2], reflectName.value());
                continue;
            }
            if (versionHandle != null) {
                if (n != reflectNameArray.length) {
                    this.error("One of @ReflectName doesn't contain a version.");
                    continue;
                }
                stringArray = versionHandle.orElse(reflectName.value());
                continue;
            }
            stringArray = reflectName.value();
        }
        nameableReflectiveHandle.named(stringArray);
    }

    private MappedType[] unwrap(Class<?>[] classArray) {
        MappedType[] mappedTypeArray = new MappedType[classArray.length];
        for (int i = 0; i < classArray.length; ++i) {
            Class<?> clazz = classArray[i];
            mappedTypeArray[i] = this.unwrap(clazz);
        }
        return mappedTypeArray;
    }

    private MappedType unwrap(Class<?> clazz) {
        if (clazz == this.interfaceClass || clazz == ReflectiveProxyObject.class) {
            return new MappedType(this.interfaceClass, this.targetClass);
        }
        if (ReflectiveProxyObject.class.isAssignableFrom(clazz)) {
            Class<?> clazz2 = MappedType.getMappedTypeOrCreate(clazz);
            return new MappedType(clazz, clazz2);
        }
        return new MappedType(clazz, clazz);
    }
}


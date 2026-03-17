/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.processors;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.jvm.ConstructorMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxy;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.Ignore;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ReflectiveHandleProxyProcessor {
    public static <T extends ReflectiveProxyObject> T proxify(@NotNull Class<T> clazz, @NotNull ClassHandle classHandle, @NotNull ReflectiveHandle<?> ... reflectiveHandleArray) {
        Set set = Collections.newSetFromMap(new IdentityHashMap());
        set.addAll(Arrays.asList(reflectiveHandleArray));
        Method[] methodArray = clazz.getMethods();
        IdentityHashMap<Method, ReflectiveProxy.ProxifiedObject> identityHashMap = new IdentityHashMap<Method, ReflectiveProxy.ProxifiedObject>(methodArray.length);
        block2: for (Method method : methodArray) {
            if (ReflectiveAnnotationProcessor.isAnnotationInherited(clazz, method, Ignore.class)) continue;
            String string = method.getName();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                AccessibleObject accessibleObject;
                MemberHandle memberHandle;
                ReflectiveHandle reflectiveHandle = (ReflectiveHandle)iterator.next();
                if (reflectiveHandle instanceof FieldMemberHandle) {
                    memberHandle = (FieldMemberHandle)reflectiveHandle;
                    if (!((NamedMemberHandle)memberHandle).getPossibleNames().stream().anyMatch(string2 -> string2.equals(string))) continue;
                    iterator.remove();
                    identityHashMap.put(method, new ReflectiveProxy.ProxifiedObject((MethodHandle)memberHandle.unreflect(), null, memberHandle.getAccessFlags().contains((Object)XAccessFlag.STATIC), false, null, null));
                    continue block2;
                }
                if (reflectiveHandle instanceof MethodMemberHandle) {
                    memberHandle = (MethodMemberHandle)reflectiveHandle;
                    if (!((NamedMemberHandle)memberHandle).getPossibleNames().stream().anyMatch(string2 -> string2.equals(string))) continue;
                    iterator.remove();
                    identityHashMap.put(method, new ReflectiveProxy.ProxifiedObject((MethodHandle)memberHandle.unreflect(), null, memberHandle.getAccessFlags().contains((Object)XAccessFlag.STATIC), false, null, null));
                    continue block2;
                }
                if (!(reflectiveHandle instanceof ConstructorMemberHandle) || method.getReturnType() != clazz || !string.equals(clazz.getName()) || ((ConstructorMemberHandle)(memberHandle = (ConstructorMemberHandle)reflectiveHandle)).getParameterTypes().length != method.getParameterCount()) continue;
                try {
                    accessibleObject = ((ConstructorMemberHandle)memberHandle).reflectJvm();
                } catch (ReflectiveOperationException reflectiveOperationException) {
                    throw new IllegalStateException("Failed to map " + method, reflectiveOperationException);
                }
                int n = 0;
                for (Parameter parameter : method.getParameters()) {
                    if (((Executable)accessibleObject).getParameters()[n].getType() != parameter.getType()) {
                        n = -1;
                        break;
                    }
                    ++n;
                }
                if (n == -1) continue;
                iterator.remove();
                identityHashMap.put(method, new ReflectiveProxy.ProxifiedObject((MethodHandle)memberHandle.unreflect(), null, false, true, null, null));
                continue block2;
            }
        }
        return null;
    }
}


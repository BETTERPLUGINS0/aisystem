/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 *  org.jetbrains.annotations.Unmodifiable
 */
package me.zombie_striker.qav.util.xseries.reflection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveNamespace;
import me.zombie_striker.qav.util.xseries.reflection.aggregate.AggregateReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.aggregate.AggregateReflectiveSupplier;
import me.zombie_striker.qav.util.xseries.reflection.aggregate.VersionHandle;
import me.zombie_striker.qav.util.xseries.reflection.asm.XReflectASM;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.StaticClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftPackage;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxy;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.Unmodifiable;

public final class XReflection {
    @Nullable
    @ApiStatus.Internal
    public static final String NMS_VERSION;
    @ApiStatus.Internal
    public static final String XSERIES_VERSION = "13.0.0";
    @TestOnly
    public static final String DISABLE_MINECRAFT_CAPABILITIES_PROPERTY = "xseries.xreflection.disable.minecraft";
    @ApiStatus.Internal
    public static final boolean SUPPORTS_ASM;
    public static final int MAJOR_NUMBER;
    public static final int MINOR_NUMBER;
    public static final int PATCH_NUMBER;
    @ApiStatus.Internal
    public static final String CRAFTBUKKIT_PACKAGE;
    @ApiStatus.Internal
    public static final String NMS_PACKAGE;
    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static final @Unmodifiable Set<MinecraftMapping> SUPPORTED_MAPPINGS;
    private static final Map<Class<?>, ReflectiveProxyObject> PROXIFIED_CLASSES;

    @Nullable
    @ApiStatus.Internal
    public static String findNMSVersionString() {
        String string = null;
        for (Package package_ : Package.getPackages()) {
            String string2 = package_.getName();
            if (!string2.startsWith("org.bukkit.craftbukkit.v")) continue;
            string = package_.getName().split("\\.")[3];
            try {
                Class.forName("org.bukkit.craftbukkit." + string + ".entity.CraftPlayer");
                break;
            } catch (ClassNotFoundException classNotFoundException) {
                string = null;
            }
        }
        return string;
    }

    private static String isMinecraftDisabled() {
        try {
            return System.getProperty(DISABLE_MINECRAFT_CAPABILITIES_PROPERTY);
        } catch (SecurityException securityException) {
            return null;
        }
    }

    @NotNull
    @Contract(pure=true)
    public static String getVersionInformation() {
        return "(NMS: " + (NMS_VERSION == null ? "Unknown NMS" : NMS_VERSION) + " | Parsed: " + MAJOR_NUMBER + '.' + MINOR_NUMBER + '.' + PATCH_NUMBER + " | Minecraft: " + Bukkit.getVersion() + " | Bukkit: " + Bukkit.getBukkitVersion() + ')';
    }

    @Nullable
    @Contract(pure=true)
    public static Integer getLatestPatchNumberOf(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Minor version must be positive: " + n);
        }
        int[] nArray = new int[]{1, 5, 2, 7, 2, 4, 10, 8, 4, 2, 2, 2, 2, 4, 2, 5, 1, 2, 4, 6, 4};
        if (n > nArray.length) {
            return null;
        }
        return nArray[n - 1];
    }

    private XReflection() {
    }

    @NotNull
    @Contract(value="_, _ -> new", pure=true)
    public static <T> VersionHandle<T> v(int n, T t) {
        return new VersionHandle<T>(n, t);
    }

    @NotNull
    @Contract(value="_, _, _ -> new", pure=true)
    public static <T> VersionHandle<T> v(int n, int n2, T t) {
        return new VersionHandle<T>(n, n2, t);
    }

    @NotNull
    @Contract(value="_, _ -> new", pure=true)
    public static <T> VersionHandle<T> v(int n, Callable<T> callable) {
        return new VersionHandle<T>(n, callable);
    }

    @NotNull
    @Contract(value="_, _, _ -> new", pure=true)
    public static <T> VersionHandle<T> v(int n, int n2, Callable<T> callable) {
        return new VersionHandle<T>(n, n2, callable);
    }

    @Contract(pure=true)
    public static boolean supports(int n) {
        return MINOR_NUMBER >= n;
    }

    @Contract(pure=true)
    public static boolean supports(int n, int n2, int n3) {
        if (n != 1) {
            throw new IllegalArgumentException("Invalid major number: " + n);
        }
        return XReflection.supports(n2, n3);
    }

    @Contract(pure=true)
    public static boolean supports(int n, int n2) {
        return MINOR_NUMBER == n ? PATCH_NUMBER >= n2 : XReflection.supports(n);
    }

    @Deprecated
    @Contract(pure=true)
    public static boolean supportsPatch(int n) {
        return PATCH_NUMBER >= n;
    }

    @Deprecated
    @NotNull
    public static Class<?> getNMSClass(@Nullable String string, @NotNull String string2) {
        if (string != null && XReflection.supports(17)) {
            string2 = string + '.' + string2;
        }
        try {
            return Class.forName(NMS_PACKAGE + '.' + string2);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException(classNotFoundException);
        }
    }

    @Deprecated
    @NotNull
    public static Class<?> getNMSClass(@NotNull String string) {
        return XReflection.getNMSClass(null, string);
    }

    @Deprecated
    @NotNull
    public static Class<?> getCraftClass(@NotNull String string) {
        try {
            return Class.forName(CRAFTBUKKIT_PACKAGE + '.' + string);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException(classNotFoundException);
        }
    }

    @NotNull
    @Contract(pure=true)
    public static Class<?> toArrayClass(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz, "Class is null");
        try {
            return Class.forName("[L" + clazz.getName() + ';');
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException("Cannot find array class for class: " + clazz, classNotFoundException);
        }
    }

    @NotNull
    @Contract(value="-> new", pure=true)
    public static MinecraftClassHandle ofMinecraft() {
        return new MinecraftClassHandle(new ReflectiveNamespace());
    }

    @NotNull
    @Contract(value="-> new", pure=true)
    public static DynamicClassHandle classHandle() {
        return new DynamicClassHandle(new ReflectiveNamespace());
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static StaticClassHandle of(Class<?> clazz) {
        return new StaticClassHandle(new ReflectiveNamespace(), clazz);
    }

    @NotNull
    @Contract(value="-> new", pure=true)
    public static ReflectiveNamespace namespaced() {
        return new ReflectiveNamespace();
    }

    @SafeVarargs
    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static <T, H extends ReflectiveHandle<T>> AggregateReflectiveHandle<T, H> any(H ... HArray) {
        return new AggregateReflectiveHandle(Arrays.stream(HArray).map(reflectiveHandle -> () -> reflectiveHandle).collect(Collectors.toList()));
    }

    @SafeVarargs
    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static <T, H extends ReflectiveHandle<T>> AggregateReflectiveHandle<T, H> anyOf(Callable<H> ... callableArray) {
        return new AggregateReflectiveHandle(Arrays.asList(callableArray));
    }

    @ApiStatus.Experimental
    @NotNull
    @Contract(value="_, _ -> new", pure=true)
    public static <H extends ReflectiveHandle<?>, O> AggregateReflectiveSupplier<H, O> supply(H h, O o) {
        AggregateReflectiveSupplier<H, O> aggregateReflectiveSupplier = new AggregateReflectiveSupplier<H, O>();
        aggregateReflectiveSupplier.or(h, o);
        return aggregateReflectiveSupplier;
    }

    @ApiStatus.Experimental
    @NotNull
    @Contract(value="_, _ -> new", pure=true)
    public static <H extends ReflectiveHandle<?>, O> AggregateReflectiveSupplier<H, O> supply(H h, Supplier<O> supplier) {
        AggregateReflectiveSupplier<H, O> aggregateReflectiveSupplier = new AggregateReflectiveSupplier<H, O>();
        aggregateReflectiveSupplier.or(h, supplier);
        return aggregateReflectiveSupplier;
    }

    @ApiStatus.Experimental
    @NotNull
    @Contract(value="_ -> param1", mutates="param1")
    public static <T extends Throwable> T relativizeSuppressedExceptions(@NotNull T t) {
        Objects.requireNonNull(t, "Cannot relativize null exception");
        StackTraceElement[] stackTraceElementArray = new StackTraceElement[]{};
        StackTraceElement[] stackTraceElementArray2 = t.getStackTrace();
        for (Throwable throwable : t.getSuppressed()) {
            StackTraceElement[] stackTraceElementArray3 = throwable.getStackTrace();
            ArrayList<StackTraceElement> arrayList = new ArrayList<StackTraceElement>(10);
            for (int i = 0; i < stackTraceElementArray3.length; ++i) {
                if (stackTraceElementArray2.length <= i) {
                    arrayList = null;
                    break;
                }
                StackTraceElement stackTraceElement = stackTraceElementArray2[i];
                StackTraceElement stackTraceElement2 = stackTraceElementArray3[i];
                if (stackTraceElement.equals(stackTraceElement2)) break;
                arrayList.add(stackTraceElement2);
            }
            if (arrayList == null) continue;
            throwable.setStackTrace(arrayList.toArray(stackTraceElementArray));
        }
        return t;
    }

    @Contract(value="_ -> fail", pure=true)
    private static <T extends Throwable> void throwException(Throwable throwable) {
        throw throwable;
    }

    @Contract(value="_ -> fail", pure=true)
    public static RuntimeException throwCheckedException(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable, "Cannot throw null exception");
        XReflection.throwException(throwable);
        return null;
    }

    @ApiStatus.Experimental
    @Contract(value="_ -> new", mutates="param1")
    public static <T> CompletableFuture<T> stacktrace(@NotNull CompletableFuture<T> completableFuture) {
        StackTraceElement[] stackTraceElementArray = Thread.currentThread().getStackTrace();
        return completableFuture.whenComplete((object, throwable) -> {
            if (throwable == null) {
                completableFuture.complete(object);
                return;
            }
            try {
                StackTraceElement[] stackTraceElementArray2;
                StackTraceElement[] stackTraceElementArray3 = throwable.getStackTrace();
                if (stackTraceElementArray3.length >= 3) {
                    stackTraceElementArray2 = new ArrayList(Arrays.asList(stackTraceElementArray3));
                    Collections.reverse(stackTraceElementArray2);
                    Iterator iterator = stackTraceElementArray2.iterator();
                    List<String> list = Arrays.asList("java.util.concurrent.CompletableFuture", "java.util.concurrent.ThreadPoolExecutor", "java.util.concurrent.ForkJoinTask", "java.util.concurrent.ForkJoinWorkerThread", "java.util.concurrent.ForkJoinPool");
                    List<String> list2 = Arrays.asList("postComplete", "encodeThrowable", "completeThrowable", "tryFire", "run", "runWorker", "scan", "exec", "doExec", "topLevelExec", "uniWhenComplete");
                    while (iterator.hasNext()) {
                        StackTraceElement stackTraceElement = (StackTraceElement)iterator.next();
                        String string = stackTraceElement.getClassName();
                        String string2 = stackTraceElement.getMethodName();
                        if (string.equals(Thread.class.getName())) continue;
                        if (!list.stream().anyMatch(string::startsWith)) break;
                        if (!list2.stream().anyMatch(string2::equals)) break;
                        iterator.remove();
                    }
                    Collections.reverse(stackTraceElementArray2);
                    stackTraceElementArray3 = stackTraceElementArray2.toArray(new StackTraceElement[0]);
                }
                stackTraceElementArray2 = (StackTraceElement[])Arrays.stream(stackTraceElementArray).skip(2L).toArray(StackTraceElement[]::new);
                throwable.setStackTrace(XReflection.concatenate(stackTraceElementArray3, stackTraceElementArray2));
            } catch (Throwable throwable2) {
                throwable.addSuppressed(throwable2);
            } finally {
                completableFuture.completeExceptionally((Throwable)throwable);
            }
        });
    }

    @ApiStatus.Internal
    @Contract(value="_, _ -> new", pure=true)
    public static <T> T[] concatenate(T[] TArray, T[] TArray2) {
        int n = TArray.length;
        int n2 = TArray2.length;
        Object[] objectArray = (Object[])Array.newInstance(TArray.getClass().getComponentType(), n + n2);
        System.arraycopy(TArray, 0, objectArray, 0, n);
        System.arraycopy(TArray2, 0, objectArray, n, n2);
        return objectArray;
    }

    @ApiStatus.Experimental
    @NotNull
    public static <T extends ReflectiveProxyObject> T proxify(@NotNull Class<T> clazz) {
        ReflectiveProxy.checkInterfaceClass(clazz);
        ReflectiveProxyObject reflectiveProxyObject = PROXIFIED_CLASSES.get(clazz);
        if (reflectiveProxyObject != null) {
            return (T)reflectiveProxyObject;
        }
        T t = SUPPORTS_ASM ? XReflectASM.proxify(clazz).create() : ReflectiveProxy.proxify(clazz).proxy();
        PROXIFIED_CLASSES.put(clazz, (ReflectiveProxyObject)t);
        return t;
    }

    static {
        Matcher matcher;
        boolean bl;
        NMS_VERSION = XReflection.findNMSVersionString();
        try {
            Class.forName("org.objectweb.asm.ClassWriter");
            Class.forName("org.objectweb.asm.MethodVisitor");
            Class.forName("org.objectweb.asm.FieldVisitor");
            Class.forName("org.objectweb.asm.Constants");
            Class.forName("org.objectweb.asm.Opcodes");
            bl = true;
        } catch (ClassNotFoundException classNotFoundException) {
            bl = false;
        }
        SUPPORTS_ASM = bl;
        String string = XReflection.isMinecraftDisabled();
        if (string != null) {
            System.out.println("[XSeries/XReflection] Testing with hardcoded server version: " + (string.isEmpty() ? "Disabled Minecraft Capabilities" : string));
            if (string.isEmpty() || string.equals("true")) {
                string = "1.21.4-R0.1-SNAPSHOT";
            }
        }
        if ((matcher = Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?").matcher(string != null ? string : Bukkit.getBukkitVersion())).find()) {
            try {
                String string2 = matcher.group("patch");
                MAJOR_NUMBER = Integer.parseInt(matcher.group("major"));
                MINOR_NUMBER = Integer.parseInt(matcher.group("minor"));
                PATCH_NUMBER = Integer.parseInt(string2 == null || string2.isEmpty() ? "0" : string2);
            } catch (Throwable throwable) {
                throw new IllegalStateException("Failed to parse minor number: " + matcher + ' ' + XReflection.getVersionInformation(), throwable);
            }
        } else {
            throw new IllegalStateException("Cannot parse server version: \"" + Bukkit.getBukkitVersion() + '\"');
        }
        CRAFTBUKKIT_PACKAGE = XReflection.isMinecraftDisabled() != null ? null : Bukkit.getServer().getClass().getPackage().getName();
        NMS_PACKAGE = XReflection.v(17, "net.minecraft").orElse("net.minecraft.server." + NMS_VERSION);
        SUPPORTED_MAPPINGS = XReflection.isMinecraftDisabled() != null ? Collections.unmodifiableSet(EnumSet.noneOf(MinecraftMapping.class)) : Collections.unmodifiableSet(XReflection.supply(XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerPlayer"), () -> EnumSet.of(MinecraftMapping.MOJANG)).or(XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "EntityPlayer"), () -> EnumSet.of(MinecraftMapping.SPIGOT, MinecraftMapping.OBFUSCATED)).get());
        PROXIFIED_CLASSES = new IdentityHashMap();
    }
}


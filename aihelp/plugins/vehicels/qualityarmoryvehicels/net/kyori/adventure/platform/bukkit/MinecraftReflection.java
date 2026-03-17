/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MinecraftReflection {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final String PREFIX_NMS = "net.minecraft.server";
    private static final String PREFIX_MC = "net.minecraft.";
    private static final String PREFIX_CRAFTBUKKIT = "org.bukkit.craftbukkit";
    private static final String CRAFT_SERVER = "CraftServer";
    @Nullable
    private static final String VERSION;

    private MinecraftReflection() {
    }

    @Nullable
    public static Class<?> findClass(@Nullable String @NotNull ... stringArray) {
        for (String string : stringArray) {
            if (string == null) continue;
            try {
                Class<?> clazz = Class.forName(string);
                return clazz;
            } catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return null;
    }

    @NotNull
    public static Class<?> needClass(@Nullable String @NotNull ... stringArray) {
        return Objects.requireNonNull(MinecraftReflection.findClass(stringArray), "Could not find class from candidates" + Arrays.toString(stringArray));
    }

    public static boolean hasClass(@NotNull String ... stringArray) {
        return MinecraftReflection.findClass(stringArray) != null;
    }

    @Nullable
    public static MethodHandle findMethod(@Nullable Class<?> clazz, String string, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        return MinecraftReflection.findMethod(clazz, new String[]{string}, clazz2, classArray);
    }

    @Nullable
    public static MethodHandle findMethod(@Nullable Class<?> clazz, @Nullable String @NotNull [] stringArray, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        if (clazz == null || clazz2 == null) {
            return null;
        }
        for (Class<?> object : classArray) {
            if (object != null) continue;
            return null;
        }
        for (String string : stringArray) {
            if (string == null) continue;
            try {
                return LOOKUP.findVirtual(clazz, string, MethodType.methodType(clazz2, classArray));
            } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
                // empty catch block
            }
        }
        return null;
    }

    public static MethodHandle searchMethod(@Nullable Class<?> clazz, @Nullable Integer n, String string, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        return MinecraftReflection.searchMethod(clazz, n, new String[]{string}, clazz2, classArray);
    }

    public static MethodHandle searchMethod(@Nullable Class<?> clazz, @Nullable Integer n, @Nullable String @NotNull [] stringArray, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        if (clazz == null || clazz2 == null) {
            return null;
        }
        for (Class<?> object : classArray) {
            if (object != null) continue;
            return null;
        }
        for (String string : stringArray) {
            if (string == null) continue;
            try {
                if (n != null && Modifier.isStatic(n)) {
                    return LOOKUP.findStatic(clazz, string, MethodType.methodType(clazz2, classArray));
                }
                return LOOKUP.findVirtual(clazz, string, MethodType.methodType(clazz2, classArray));
            } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
                // empty catch block
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (n == null || (method.getModifiers() & n) == 0 || !Arrays.equals(method.getParameterTypes(), classArray)) continue;
            try {
                if (Modifier.isStatic(n)) {
                    return LOOKUP.findStatic(clazz, method.getName(), MethodType.methodType(clazz2, classArray));
                }
                return LOOKUP.findVirtual(clazz, method.getName(), MethodType.methodType(clazz2, classArray));
            } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
                // empty catch block
            }
        }
        return null;
    }

    @Nullable
    public static MethodHandle findStaticMethod(@Nullable Class<?> clazz, String string, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        return MinecraftReflection.findStaticMethod(clazz, new String[]{string}, clazz2, classArray);
    }

    @Nullable
    public static MethodHandle findStaticMethod(@Nullable Class<?> clazz, String[] stringArray, @Nullable Class<?> clazz2, Class<?> ... classArray) {
        if (clazz == null || clazz2 == null) {
            return null;
        }
        for (Class<?> clazz3 : classArray) {
            if (clazz3 != null) continue;
            return null;
        }
        for (String string : stringArray) {
            try {
                return LOOKUP.findStatic(clazz, string, MethodType.methodType(clazz2, classArray));
            } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            }
        }
        return null;
    }

    public static boolean hasField(@Nullable Class<?> clazz, Class<?> clazz2, String ... stringArray) {
        if (clazz == null) {
            return false;
        }
        for (String string : stringArray) {
            try {
                Field field = clazz.getDeclaredField(string);
                if (field.getType() != clazz2) continue;
                return true;
            } catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
        return false;
    }

    public static boolean hasMethod(@Nullable Class<?> clazz, String string, Class<?> ... classArray) {
        return MinecraftReflection.hasMethod(clazz, new String[]{string}, classArray);
    }

    public static boolean hasMethod(@Nullable Class<?> clazz, String[] stringArray, Class<?> ... classArray) {
        if (clazz == null) {
            return false;
        }
        for (Class<?> clazz2 : classArray) {
            if (clazz2 != null) continue;
            return false;
        }
        for (String string : stringArray) {
            try {
                clazz.getMethod(string, classArray);
                return true;
            } catch (NoSuchMethodException noSuchMethodException) {
            }
        }
        return false;
    }

    @Nullable
    public static MethodHandle findConstructor(@Nullable Class<?> clazz, @Nullable Class<?> ... classArray) {
        if (clazz == null) {
            return null;
        }
        for (Class<?> clazz2 : classArray) {
            if (clazz2 != null) continue;
            return null;
        }
        try {
            return LOOKUP.findConstructor(clazz, MethodType.methodType(Void.TYPE, classArray));
        } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            return null;
        }
    }

    @NotNull
    public static Field needField(@NotNull Class<?> clazz, @NotNull String string) {
        Field field = clazz.getDeclaredField(string);
        field.setAccessible(true);
        return field;
    }

    @Nullable
    public static Field findField(@Nullable Class<?> clazz, @NotNull String ... stringArray) {
        return MinecraftReflection.findField(clazz, null, stringArray);
    }

    @Nullable
    public static Field findField(@Nullable Class<?> clazz, @Nullable Class<?> clazz2, @NotNull String ... stringArray) {
        if (clazz == null) {
            return null;
        }
        for (String string : stringArray) {
            Field field;
            try {
                field = clazz.getDeclaredField(string);
            } catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
            field.setAccessible(true);
            if (clazz2 != null && !clazz2.isAssignableFrom(field.getType())) continue;
            return field;
        }
        return null;
    }

    @Nullable
    public static MethodHandle findSetterOf(@Nullable Field field) {
        if (field == null) {
            return null;
        }
        try {
            return LOOKUP.unreflectSetter(field);
        } catch (IllegalAccessException illegalAccessException) {
            return null;
        }
    }

    @Nullable
    public static MethodHandle findGetterOf(@Nullable Field field) {
        if (field == null) {
            return null;
        }
        try {
            return LOOKUP.unreflectGetter(field);
        } catch (IllegalAccessException illegalAccessException) {
            return null;
        }
    }

    @Nullable
    public static Object findEnum(@Nullable Class<?> clazz, @NotNull String string) {
        return MinecraftReflection.findEnum(clazz, string, Integer.MAX_VALUE);
    }

    @Nullable
    public static Object findEnum(@Nullable Class<?> clazz, @NotNull String string, int n) {
        if (clazz == null || !Enum.class.isAssignableFrom(clazz)) {
            return null;
        }
        try {
            return Enum.valueOf(clazz.asSubclass(Enum.class), string);
        } catch (IllegalArgumentException illegalArgumentException) {
            ?[] objArray = clazz.getEnumConstants();
            if (objArray.length > n) {
                return objArray[n];
            }
            return null;
        }
    }

    public static boolean isCraftBukkit() {
        return VERSION != null;
    }

    @Nullable
    public static String findCraftClassName(@NotNull String string) {
        return MinecraftReflection.isCraftBukkit() ? PREFIX_CRAFTBUKKIT + VERSION + string : null;
    }

    @Nullable
    public static Class<?> findCraftClass(@NotNull String string) {
        String string2 = MinecraftReflection.findCraftClassName(string);
        if (string2 == null) {
            return null;
        }
        return MinecraftReflection.findClass(string2);
    }

    @Nullable
    public static <T> Class<? extends T> findCraftClass(@NotNull String string, @NotNull Class<T> clazz) {
        Class<?> clazz2 = MinecraftReflection.findCraftClass(string);
        if (clazz2 == null || !Objects.requireNonNull(clazz, "superClass").isAssignableFrom(clazz2)) {
            return null;
        }
        return clazz2.asSubclass(clazz);
    }

    @NotNull
    public static Class<?> needCraftClass(@NotNull String string) {
        return Objects.requireNonNull(MinecraftReflection.findCraftClass(string), "Could not find org.bukkit.craftbukkit class " + string);
    }

    @Nullable
    public static String findNmsClassName(@NotNull String string) {
        return MinecraftReflection.isCraftBukkit() ? PREFIX_NMS + VERSION + string : null;
    }

    @Nullable
    public static Class<?> findNmsClass(@NotNull String string) {
        String string2 = MinecraftReflection.findNmsClassName(string);
        if (string2 == null) {
            return null;
        }
        return MinecraftReflection.findClass(string2);
    }

    @NotNull
    public static Class<?> needNmsClass(@NotNull String string) {
        return Objects.requireNonNull(MinecraftReflection.findNmsClass(string), "Could not find net.minecraft.server class " + string);
    }

    @Nullable
    public static String findMcClassName(@NotNull String string) {
        return MinecraftReflection.isCraftBukkit() ? PREFIX_MC + string : null;
    }

    @Nullable
    public static Class<?> findMcClass(@NotNull String ... stringArray) {
        for (String string : stringArray) {
            Class<?> clazz;
            String string2 = MinecraftReflection.findMcClassName(string);
            if (string2 == null || (clazz = MinecraftReflection.findClass(string2)) == null) continue;
            return clazz;
        }
        return null;
    }

    @NotNull
    public static Class<?> needMcClass(@NotNull String ... stringArray) {
        return Objects.requireNonNull(MinecraftReflection.findMcClass(stringArray), "Could not find net.minecraft class from candidates" + Arrays.toString(stringArray));
    }

    public static @NotNull MethodHandles.Lookup lookup() {
        return LOOKUP;
    }

    static {
        Class<?> clazz = Bukkit.getServer().getClass();
        if (!clazz.getSimpleName().equals(CRAFT_SERVER)) {
            VERSION = null;
        } else if (clazz.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            VERSION = ".";
        } else {
            String string = clazz.getName();
            string = string.substring(PREFIX_CRAFTBUKKIT.length());
            VERSION = string = string.substring(0, string.length() - CRAFT_SERVER.length());
        }
    }
}


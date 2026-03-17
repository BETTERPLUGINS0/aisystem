/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  com.mojang.authlib.properties.Property
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.profiles;

import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.lang.invoke.MethodHandle;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import me.zombie_striker.qav.util.xseries.profiles.ProfileLogger;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveNamespace;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ProfilesCore {
    public static final Object USER_CACHE;
    public static final Object MINECRAFT_SESSION_SERVICE;
    public static final Proxy PROXY;
    public static final LoadingCache<Object, Object> YggdrasilMinecraftSessionService_insecureProfiles;
    public static final Map<String, Object> UserCache_profilesByName;
    public static final Map<UUID, Object> UserCache_profilesByUUID;
    public static final MethodHandle MinecraftSessionService_fillProfileProperties;
    public static final MethodHandle GameProfileCache_get$profileByName$;
    public static final MethodHandle GameProfileCache_get$profileByUUID$;
    public static final MethodHandle CACHE_PROFILE;
    public static final MethodHandle CraftMetaSkull_profile$getter;
    public static final MethodHandle CraftMetaSkull_profile$setter;
    public static final MethodHandle CraftSkull_profile$setter;
    public static final MethodHandle CraftSkull_profile$getter;
    public static final MethodHandle Property_getValue;
    public static final MethodHandle UserCache_getNextOperation;
    public static final MethodHandle UserCacheEntry_getProfile;
    public static final MethodHandle UserCacheEntry_setLastAccess;
    public static final MethodHandle ResolvableProfile$constructor;
    public static final MethodHandle ResolvableProfile_gameProfile;
    public static final boolean ResolvableProfile$bukkitSupports;
    public static final boolean NULLABILITY_RECORD_UPDATE;

    private static /* synthetic */ MethodMemberHandle lambda$static$3(MethodMemberHandle methodMemberHandle) {
        return methodMemberHandle.signature("public Optional<GameProfile> get(UUID id)");
    }

    private static /* synthetic */ MethodMemberHandle lambda$static$2(MethodMemberHandle methodMemberHandle) {
        return methodMemberHandle.signature("public          GameProfile  get(UUID id)");
    }

    private static /* synthetic */ MethodMemberHandle lambda$static$1(MethodMemberHandle methodMemberHandle) {
        return methodMemberHandle.signature("public Optional<GameProfile> get(String username)");
    }

    private static /* synthetic */ MethodMemberHandle lambda$static$0(MethodMemberHandle methodMemberHandle) {
        return methodMemberHandle.signature("public          GameProfile  get(String username)");
    }

    static {
        Proxy proxy;
        MethodHandle methodHandle;
        MethodHandle methodHandle2;
        MethodHandle methodHandle3;
        Object object;
        Object object2;
        MinecraftClassHandle minecraftClassHandle;
        MethodHandle methodHandle4;
        MethodHandle methodHandle5;
        ReflectiveHandle<Class<?>> reflectiveHandle;
        MinecraftClassHandle minecraftClassHandle2;
        NULLABILITY_RECORD_UPDATE = XReflection.supports(1, 20, 2);
        Object object3 = null;
        MethodHandle methodHandle6 = null;
        MethodHandle methodHandle7 = null;
        MethodHandle methodHandle8 = null;
        boolean bl = false;
        ReflectiveNamespace reflectiveNamespace = XReflection.namespaced().imports(GameProfile.class, MinecraftSessionService.class, LoadingCache.class);
        MinecraftClassHandle minecraftClassHandle3 = reflectiveNamespace.ofMinecraft("package nms.server.players; public class GameProfileCache").map(MinecraftMapping.SPIGOT, "UserCache");
        try {
            minecraftClassHandle2 = reflectiveNamespace.ofMinecraft("package cb.inventory; class CraftMetaSkull extends CraftMetaItem implements SkullMeta");
            reflectiveHandle = reflectiveNamespace.ofMinecraft("package nms.world.item.component; public class ResolvableProfile");
            if (reflectiveHandle.exists()) {
                methodHandle7 = ((ClassHandle)reflectiveHandle).constructor("public ResolvableProfile(GameProfile gameProfile)").reflect();
                methodHandle8 = ((ClassHandle)reflectiveHandle).method("public GameProfile gameProfile()").map(MinecraftMapping.OBFUSCATED, "f").reflect();
                bl = minecraftClassHandle2.field("private ResolvableProfile profile").exists();
            }
            methodHandle5 = (MethodHandle)XReflection.any((ReflectiveHandle[])new FieldMemberHandle[]{minecraftClassHandle2.field("private ResolvableProfile profile"), minecraftClassHandle2.field("private GameProfile       profile")}).modify(FieldMemberHandle::getter).reflect();
            methodHandle4 = (MethodHandle)XReflection.any((ReflectiveHandle[])new FlaggedNamedMemberHandle[]{minecraftClassHandle2.method("private void setProfile(ResolvableProfile profile)"), minecraftClassHandle2.method("private void setProfile(GameProfile       profile)"), minecraftClassHandle2.field("private                 GameProfile       profile ").setter()}).reflect();
            minecraftClassHandle = reflectiveNamespace.ofMinecraft("package nms.server; public abstract class MinecraftServer");
            Object object4 = minecraftClassHandle.method("public static MinecraftServer getServer()").reflect().invoke();
            object2 = minecraftClassHandle.method("public MinecraftSessionService getSessionService()").named("aq", "ay", "getMinecraftSessionService", "az", "ao", "am", "aD", "ar", "ap").reflect().invoke(object4);
            FlaggedNamedMemberHandle flaggedNamedMemberHandle = reflectiveNamespace.ofMinecraft("package com.mojang.authlib.yggdrasil;public class YggdrasilMinecraftSessionService implements MinecraftSessionService").field().getter();
            if (NULLABILITY_RECORD_UPDATE) {
                flaggedNamedMemberHandle.signature("private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles");
            } else {
                flaggedNamedMemberHandle.signature("private final LoadingCache<GameProfile, GameProfile> insecureProfiles");
            }
            Object object5 = (MethodHandle)flaggedNamedMemberHandle.reflectOrNull();
            if (object5 != null) {
                object3 = object5.invoke(object2);
            }
            object = minecraftClassHandle.method("public GameProfileCache getProfileCache()").named("at", "ar", "ao", "ap", "au").map(MinecraftMapping.OBFUSCATED, "getUserCache").reflect().invoke(object4);
            if (!NULLABILITY_RECORD_UPDATE) {
                methodHandle6 = reflectiveNamespace.of(MinecraftSessionService.class).method("public GameProfile fillProfileProperties(GameProfile profile, boolean flag)").reflect();
            }
            UserCache_getNextOperation = (MethodHandle)minecraftClassHandle3.method("private long getNextOperation()").map(MinecraftMapping.OBFUSCATED, XReflection.v(21, "e").v(16, "d").orElse("d")).reflectOrNull();
            flaggedNamedMemberHandle = minecraftClassHandle3.method().named("getProfile", "a");
            object5 = minecraftClassHandle3.method().named("getProfile", "a");
            methodHandle3 = (MethodHandle)XReflection.anyOf(() -> ProfilesCore.lambda$static$0((MethodMemberHandle)flaggedNamedMemberHandle), () -> ProfilesCore.lambda$static$1((MethodMemberHandle)flaggedNamedMemberHandle)).reflect();
            methodHandle2 = (MethodHandle)XReflection.anyOf(() -> ProfilesCore.lambda$static$2((MethodMemberHandle)object5), () -> ProfilesCore.lambda$static$3((MethodMemberHandle)object5)).reflect();
            methodHandle = minecraftClassHandle3.method("public void add(GameProfile profile)").map(MinecraftMapping.OBFUSCATED, "a").reflect();
            try {
                proxy = minecraftClassHandle.field("protected final java.net.Proxy proxy").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(20, 5, "h").v(20, 3, "i").v(19, "j").v(18, 2, "n").v(18, "o").v(17, "m").v(14, "proxy").v(13, "c").orElse("e")).reflect().invoke(object4);
            } catch (Throwable throwable) {
                ProfileLogger.LOGGER.error("Failed to initialize server proxy settings", throwable);
                proxy = null;
            }
        } catch (Throwable throwable) {
            throw XReflection.throwCheckedException(throwable);
        }
        minecraftClassHandle2 = reflectiveNamespace.ofMinecraft("package cb.block; public class CraftSkull extends CraftBlockEntityState implements Skull");
        reflectiveHandle = (FieldMemberHandle)XReflection.any((ReflectiveHandle[])new FieldMemberHandle[]{minecraftClassHandle2.field("private ResolvableProfile profile"), minecraftClassHandle2.field("private GameProfile profile")}).getHandle();
        Property_getValue = NULLABILITY_RECORD_UPDATE ? null : (MethodHandle)reflectiveNamespace.of(Property.class).method("public String getValue()").unreflect();
        PROXY = proxy;
        USER_CACHE = object;
        CACHE_PROFILE = methodHandle;
        MINECRAFT_SESSION_SERVICE = object2;
        YggdrasilMinecraftSessionService_insecureProfiles = (LoadingCache)object3;
        MinecraftSessionService_fillProfileProperties = methodHandle6;
        GameProfileCache_get$profileByName$ = methodHandle3;
        GameProfileCache_get$profileByUUID$ = methodHandle2;
        CraftMetaSkull_profile$setter = methodHandle4;
        CraftMetaSkull_profile$getter = methodHandle5;
        CraftSkull_profile$setter = (MethodHandle)((FieldMemberHandle)reflectiveHandle).setter().unreflect();
        CraftSkull_profile$getter = (MethodHandle)((FieldMemberHandle)reflectiveHandle).getter().unreflect();
        ResolvableProfile$constructor = methodHandle7;
        ResolvableProfile_gameProfile = methodHandle8;
        ResolvableProfile$bukkitSupports = bl;
        minecraftClassHandle = minecraftClassHandle3.inner("private static class GameProfileInfo").map(MinecraftMapping.SPIGOT, "UserCacheEntry");
        UserCacheEntry_getProfile = (MethodHandle)minecraftClassHandle.method("public GameProfile getProfile()").map(MinecraftMapping.OBFUSCATED, "a").makeAccessible().unreflect();
        UserCacheEntry_setLastAccess = (MethodHandle)minecraftClassHandle.method("public void setLastAccess(long i)").map(MinecraftMapping.OBFUSCATED, "a").reflectOrNull();
        try {
            UserCache_profilesByName = minecraftClassHandle3.field("private final Map<String, UserCache.UserCacheEntry> profilesByName").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(17, "e").v(16, 2, "c").v(9, "d").orElse("c")).reflect().invoke(object);
            UserCache_profilesByUUID = minecraftClassHandle3.field("private final Map<UUID, UserCache.UserCacheEntry> profilesByUUID").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(17, "f").v(16, 2, "d").v(9, "e").orElse("d")).reflect().invoke(object);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to initialize ProfilesCore", throwable);
        }
    }
}


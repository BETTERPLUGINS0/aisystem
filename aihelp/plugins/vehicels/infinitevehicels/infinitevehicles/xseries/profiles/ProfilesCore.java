package me.PM2.infinitevehicles.xseries.profiles;

import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.lang.invoke.MethodHandle;
import java.net.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MethodMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ProfilesCore {
   public static final Object USER_CACHE;
   public static final Object MINECRAFT_SESSION_SERVICE;
   public static final Proxy PROXY;
   public static final LoadingCache<Object, Object> YggdrasilMinecraftSessionService_insecureProfiles;
   public static final boolean SUPPORTS_BUKKIT_PlayerProfile;
   public static final Map<String, Object> UserCache_profilesByName;
   public static final Map<UUID, Object> UserCache_profilesByUUID;
   public static final MethodHandle MinecraftSessionService_fillProfileProperties;
   public static final MethodHandle GameProfileCache_get$profileByName$;
   public static final MethodHandle GameProfileCache_get$profileByUUID$;
   public static final MethodHandle CachedUserNameToIdResolver_add;
   public static final MethodHandle CraftMetaSkull_profile$getter;
   public static final MethodHandle CraftMetaSkull_profile$setter;
   public static final MethodHandle CraftSkull_profile$setter;
   public static final MethodHandle CraftSkull_profile$getter;
   public static final MethodHandle Property_getValue;
   public static final MethodHandle UserCache_getNextOperation;
   public static final MethodHandle GameProfileInfo_getProfile;
   public static final MethodHandle GameProfileInfo_setLastAccess;
   public static final MethodHandle GameProfileInfo_nameAndId;
   public static final MethodHandle NameAndId_id;
   public static final MethodHandle NameAndId_name;
   public static final MethodHandle NameAndId$ctor_GameProfile;
   public static final MethodHandle ResolvableProfile$constructor;
   public static final MethodHandle ResolvableProfile_gameProfile;
   public static final boolean ResolvableProfile$bukkitSupports;
   public static final boolean NULLABILITY_RECORD_UPDATE = XReflection.supports(1, 20, 2);

   static {
      Object var2 = null;
      MethodHandle var4 = null;
      MethodHandle var10 = null;
      MethodHandle var11 = null;
      MethodHandle var12 = null;
      boolean var13 = false;
      ReflectiveNamespace var14 = XReflection.namespaced().imports(GameProfile.class, MinecraftSessionService.class, LoadingCache.class);
      MinecraftClassHandle var15 = var14.ofMinecraft("package nms.server.players; public class CachedUserNameToIdResolver").map(MinecraftMapping.MOJANG, (String)XReflection.v(21, 9, (Object)"CachedUserNameToIdResolver").orElse((Object)"GameProfileCache")).map(MinecraftMapping.SPIGOT, "UserCache");
      SUPPORTS_BUKKIT_PlayerProfile = var14.ofMinecraft("package org.bukkit; public interface OfflinePlayer").method("org.bukkit.profile.PlayerProfile getPlayerProfile()").exists();
      MinecraftClassHandle var16 = var15.inner("private static class GameProfileInfo").map(MinecraftMapping.SPIGOT, (String)XReflection.v(21, 9, (Object)"a").orElse((Object)"UserCacheEntry")).map(MinecraftMapping.OBFUSCATED, "bay$a");
      MinecraftClassHandle var17 = var14.ofMinecraft("package nms.server.players; public class NameAndId").map(MinecraftMapping.OBFUSCATED, "bbb");

      Object var0;
      Object var1;
      Proxy var3;
      MethodHandle var5;
      MethodHandle var6;
      MethodHandle var7;
      MethodHandle var8;
      MethodHandle var9;
      MinecraftClassHandle var18;
      try {
         var18 = var14.ofMinecraft("package cb.inventory; class CraftMetaSkull extends CraftMetaItem implements SkullMeta");
         MinecraftClassHandle var19 = var14.ofMinecraft("package nms.world.item.component; public class ResolvableProfile");
         if (var19.exists()) {
            var10 = (MethodHandle)XReflection.any(var19.method("public static ResolvableProfile createResolved(GameProfile gameProfile)").map(MinecraftMapping.OBFUSCATED, "a"), var19.constructor("public ResolvableProfile(GameProfile gameProfile)")).reflect();
            var11 = var19.method("public GameProfile partialProfile()").map(MinecraftMapping.MOJANG, (String)XReflection.v(21, 9, (Object)"partialProfile").orElse((Object)"gameProfile")).map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, 9, (Object)"b").v(21, 6, (Object)"g").orElse((Object)"f")).reflect();
            var13 = var18.field("private ResolvableProfile profile").exists();
         }

         var9 = (MethodHandle)XReflection.any(var18.field("private ResolvableProfile profile"), var18.field("private GameProfile       profile")).modify(FieldMemberHandle::getter).reflect();
         var8 = (MethodHandle)XReflection.any(var18.method("private void setProfile(ResolvableProfile profile)"), var18.method("private void setProfile(GameProfile       profile)"), var18.field("private                 GameProfile       profile ").setter()).reflect();
         MinecraftClassHandle var20 = var14.ofMinecraft("package nms.server; public abstract class MinecraftServer");
         Object var21 = var20.method("public static MinecraftServer getServer()").reflect().invoke();
         MinecraftClassHandle var22 = var14.ofMinecraft("package nms.server; public class Services");
         boolean var23 = XReflection.supports(1, 21, 9) && var22.exists();
         Object var24 = null;
         if (var23) {
            var24 = var20.method("public Services services()").map(MinecraftMapping.OBFUSCATED, "av").reflect().invoke(var21);
            var1 = var22.method("public com.mojang.authlib.minecraft.MinecraftSessionService sessionService()").map(MinecraftMapping.OBFUSCATED, "c").reflect().invoke(var24);
         } else {
            var1 = var20.method("public MinecraftSessionService getSessionService()").named("aq", "ay", "getMinecraftSessionService", "az", "ao", "am", "aD", "ar", "ap").reflect().invoke(var21);
         }

         MinecraftClassHandle var25 = var14.ofMinecraft("package com.mojang.authlib.yggdrasil;public class YggdrasilMinecraftSessionService implements MinecraftSessionService");
         FieldMemberHandle var26 = var25.field().getter();
         if (NULLABILITY_RECORD_UPDATE) {
            var26.signature("private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles");
         } else {
            var26.signature("private final LoadingCache<GameProfile, GameProfile> insecureProfiles");
         }

         MethodHandle var27 = (MethodHandle)var26.reflectOrNull();
         if (var27 != null) {
            var2 = var27.invoke(var1);
         }

         if (var23) {
            var14.ofMinecraft("package nms.server.players; public interface UserNameToIdResolver").map(MinecraftMapping.OBFUSCATED, "bbm");
            var0 = var22.method("public UserNameToIdResolver nameToIdCache()").map(MinecraftMapping.OBFUSCATED, "f").reflect().invoke(var24);
         } else {
            var0 = var20.method("public CachedUserNameToIdResolver getProfileCache()").named("at", "ar", "ao", "ap", "au").map(MinecraftMapping.OBFUSCATED, "getUserCache").reflect().invoke(var21);
         }

         if (!NULLABILITY_RECORD_UPDATE) {
            var4 = var14.of(MinecraftSessionService.class).method("public GameProfile fillProfileProperties(GameProfile profile, boolean flag)").reflect();
         }

         UserCache_getNextOperation = (MethodHandle)var15.method("private long getNextOperation()").map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, (Object)"e").v(16, "d").orElse((Object)"d")).reflectOrNull();
         MethodMemberHandle var32 = var15.method().named("getProfile", "a");
         MethodMemberHandle var33 = var15.method().named("getProfile", "a");
         var5 = (MethodHandle)XReflection.anyOf(() -> {
            return var32.signature("public          GameProfile  get(String username)");
         }, () -> {
            return var32.signature("public Optional<GameProfile> get(String username)");
         }).reflect();
         var6 = (MethodHandle)XReflection.anyOf(() -> {
            return var33.signature("public          GameProfile  get(UUID id)");
         }, () -> {
            return var33.signature("public Optional<GameProfile> get(UUID id)");
         }).reflect();
         if (var23) {
            var12 = var17.constructor("public NameAndId(GameProfile profile)").reflect();
            var7 = var15.method("private void add(NameAndId nameAndId)").parameters(var17).map(MinecraftMapping.OBFUSCATED, "a").reflect();
         } else {
            var7 = var15.method("public void add(GameProfile profile)").map(MinecraftMapping.OBFUSCATED, "a").reflect();
         }

         try {
            var3 = var20.field("protected final java.net.Proxy proxy").getter().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(21, 9, (Object)"i").v(20, 5, (Object)"h").v(20, 3, (Object)"i").v(19, "j").v(18, 2, (Object)"n").v(18, "o").v(17, "m").v(14, "proxy").v(13, "c").orElse((Object)"e")).reflect().invoke(var21);
         } catch (Throwable var29) {
            ProfileLogger.LOGGER.error("Failed to initialize server proxy settings", var29);
            var3 = null;
         }
      } catch (Throwable var30) {
         throw XReflection.throwCheckedException(var30);
      }

      var18 = var14.ofMinecraft("package cb.block; public class CraftSkull extends CraftBlockEntityState implements Skull");
      FieldMemberHandle var31 = (FieldMemberHandle)XReflection.any(var18.field("private ResolvableProfile profile"), var18.field("private GameProfile profile")).getHandle();
      Property_getValue = NULLABILITY_RECORD_UPDATE ? null : (MethodHandle)var14.of(Property.class).method("public String getValue()").unreflect();
      PROXY = var3;
      USER_CACHE = var0;
      CachedUserNameToIdResolver_add = var7;
      MINECRAFT_SESSION_SERVICE = var1;
      NameAndId$ctor_GameProfile = var12;
      Objects.requireNonNull(var2, () -> {
         return "Couldn't find Mojang's insecureProfiles cache " + XReflection.getVersionInformation();
      });
      YggdrasilMinecraftSessionService_insecureProfiles = (LoadingCache)var2;
      MinecraftSessionService_fillProfileProperties = var4;
      GameProfileCache_get$profileByName$ = var5;
      GameProfileCache_get$profileByUUID$ = var6;
      CraftMetaSkull_profile$setter = var8;
      CraftMetaSkull_profile$getter = var9;
      CraftSkull_profile$setter = (MethodHandle)var31.setter().unreflect();
      CraftSkull_profile$getter = (MethodHandle)var31.getter().unreflect();
      ResolvableProfile$constructor = var10;
      ResolvableProfile_gameProfile = var11;
      ResolvableProfile$bukkitSupports = var13;
      GameProfileInfo_getProfile = (MethodHandle)var16.method("public GameProfile getProfile()").map(MinecraftMapping.OBFUSCATED, "a").makeAccessible().unwrap().reflectOrNull();
      if (GameProfileInfo_getProfile == null) {
         GameProfileInfo_nameAndId = (MethodHandle)var16.method("public NameAndId nameAndId()").map(MinecraftMapping.OBFUSCATED, "a").makeAccessible().unwrap().unreflect();
         NameAndId_id = (MethodHandle)var17.method("public UUID id()").map(MinecraftMapping.OBFUSCATED, "a").makeAccessible().unwrap().unreflect();
         NameAndId_name = (MethodHandle)var17.method("public String name()").map(MinecraftMapping.OBFUSCATED, "b").makeAccessible().unwrap().unreflect();
      } else {
         GameProfileInfo_nameAndId = null;
         NameAndId_id = null;
         NameAndId_name = null;
      }

      GameProfileInfo_setLastAccess = (MethodHandle)var16.method("public void setLastAccess(long lastAccess)").map(MinecraftMapping.OBFUSCATED, "a").reflectOrNull();

      try {
         UserCache_profilesByName = var15.field("private final Map<String, UserCache.UserCacheEntry> profilesByName").getter().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(17, (Object)"e").v(16, 2, (Object)"c").v(9, "d").orElse((Object)"c")).reflect().invoke(var0);
         UserCache_profilesByUUID = var15.field("private final Map<UUID, UserCache.UserCacheEntry> profilesByUUID").getter().map(MinecraftMapping.OBFUSCATED, (String)XReflection.v(17, (Object)"f").v(16, 2, (Object)"d").v(9, "e").orElse((Object)"d")).reflect().invoke(var0);
      } catch (Throwable var28) {
         throw new IllegalStateException("Failed to initialize ProfilesCore", var28);
      }
   }
}

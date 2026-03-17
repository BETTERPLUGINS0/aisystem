package me.PM2.infinitevehicles.xseries.messages;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftConnection;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Titles {
   private static final Object TITLE_ACTION_TITLE;
   private static final Object TITLE_ACTION_SUBTITLE;
   private static final Object TITLE_ACTION_TIMES;
   private static final Object TITLE_ACTION_CLEAR;
   private static final MethodHandle PACKET_PLAY_OUT_TITLE;
   private static final MethodHandle CHAT_COMPONENT_TEXT;
   private static final MethodHandle ClientboundSetTitlesAnimationPacket;
   private static final MethodHandle ClientboundSetTitleTextPacket;
   private static final MethodHandle ClientboundSetSubtitleTextPacket;
   @Nullable
   private MessageComponents.MessageText title;
   @Nullable
   private MessageComponents.MessageText subtitle;
   private final int fadeIn;
   private final int stay;
   private final int fadeOut;
   private static final boolean SUPPORTS_TITLES;
   private static final boolean USE_TEXT_COMPONENTS;

   public Titles(MessageComponents.MessageText var1, MessageComponents.MessageText var2, int var3, int var4, int var5) {
      this.title = var1;
      this.subtitle = var2;
      this.fadeIn = var3;
      this.stay = var4;
      this.fadeOut = var5;
   }

   public Titles(BaseComponent var1, BaseComponent var2, int var3, int var4, int var5) {
      this((MessageComponents.MessageText)MessageComponents.ofNullable(var1), (MessageComponents.MessageText)MessageComponents.ofNullable(var2), var3, var4, var5);
   }

   public Titles(String var1, String var2, int var3, int var4, int var5) {
      this((MessageComponents.MessageText)MessageComponents.ofNullable(var1), (MessageComponents.MessageText)MessageComponents.ofNullable(var2), var3, var4, var5);
   }

   public Titles copy() {
      return new Titles(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
   }

   public void send(Player var1) {
      sendTitle(var1, this.fadeIn, this.stay, this.fadeOut, this.title, this.subtitle);
   }

   public static void sendTitle(@NotNull Player var0, int var1, int var2, int var3, @Nullable String var4, @Nullable String var5) {
      sendTitle(var0, var1, var2, var3, (MessageComponents.MessageText)MessageComponents.ofNullable(var4), (MessageComponents.MessageText)MessageComponents.ofNullable(var5));
   }

   public static void sendTitle(@NotNull Player var0, int var1, int var2, int var3, @Nullable MessageComponents.MessageText var4, @Nullable MessageComponents.MessageText var5) {
      Objects.requireNonNull(var0, "Cannot send title to null player");
      if (var4 != null || var5 != null) {
         ArrayList var6;
         if (USE_TEXT_COMPONENTS) {
            var6 = new ArrayList(3);

            try {
               var6.add(ClientboundSetTitlesAnimationPacket.invoke(var1, var2, var3));
               if (var4 != null) {
                  var6.add(ClientboundSetTitleTextPacket.invoke(MessageComponents.bungeeToVanilla(var4.asComponent())));
               }

               if (var5 != null) {
                  var6.add(ClientboundSetSubtitleTextPacket.invoke(MessageComponents.bungeeToVanilla(var5.asComponent())));
               }
            } catch (Throwable var10) {
               throw new IllegalStateException("Failed to create packets with title: " + var4 + " and subtitle: " + var5, var10);
            }

            MinecraftConnection.sendPacket(var0, var6.toArray(new Object[0]));
         } else if (SUPPORTS_TITLES) {
            var0.sendTitle(var4.asString(), var5.asString(), var1, var2, var3);
         } else {
            try {
               var6 = new ArrayList(3);
               Object var7 = CHAT_COMPONENT_TEXT.invoke(var4 == null ? null : var4.asString());
               var6.add(PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_TIMES, var7, var1, var2, var3));
               if (var4 != null) {
                  var6.add(PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_TITLE, var7, var1, var2, var3));
               }

               if (var5 != null) {
                  Object var8 = CHAT_COMPONENT_TEXT.invoke(var5.asString());
                  var6.add(PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_SUBTITLE, var8, var1, var2, var3));
               }

               MinecraftConnection.sendPacket(var0, var6.toArray(new Object[0]));
            } catch (Throwable var9) {
               throw new IllegalStateException("Failed to send packets for title: " + var4 + " and subtitle: " + var5, var9);
            }
         }
      }
   }

   public static void sendTitle(@NotNull Player var0, @NotNull String var1, @NotNull String var2) {
      sendTitle(var0, 10, 20, 10, (String)var1, (String)var2);
   }

   public static Titles sendTitle(@NotNull Player var0, @NotNull ConfigurationSection var1) {
      Titles var2 = parseTitle(var1, (Function)null);
      var2.send(var0);
      return var2;
   }

   public static Titles parseTitle(@NotNull ConfigurationSection var0) {
      return parseTitle(var0, (Function)null);
   }

   public static Titles parseTitle(@NotNull ConfigurationSection var0, @Nullable Function<String, String> var1) {
      String var2 = var0.getString("title");
      String var3 = var0.getString("subtitle");
      if (var1 != null) {
         var2 = (String)var1.apply(var2);
         var3 = (String)var1.apply(var3);
      }

      int var4 = var0.getInt("fade-in");
      int var5 = var0.getInt("stay");
      int var6 = var0.getInt("fade-out");
      if (var4 < 1) {
         var4 = 10;
      }

      if (var5 < 1) {
         var5 = 20;
      }

      if (var6 < 1) {
         var6 = 10;
      }

      return new Titles(var2, var3, var4, var5, var6);
   }

   public MessageComponents.MessageText getTitle() {
      return this.title;
   }

   public MessageComponents.MessageText getSubtitle() {
      return this.subtitle;
   }

   public void setTitle(String var1) {
      this.title = MessageComponents.ofNullable(var1);
   }

   public void setSubtitle(String var1) {
      this.subtitle = MessageComponents.ofNullable(var1);
   }

   public void setTitle(BaseComponent var1) {
      this.title = MessageComponents.ofNullable(var1);
   }

   public void setSubtitle(BaseComponent var1) {
      this.subtitle = MessageComponents.ofNullable(var1);
   }

   public static void clearTitle(@NotNull Player var0) {
      Objects.requireNonNull(var0, "Cannot clear title from null player");
      if (XReflection.supports(11)) {
         var0.resetTitle();
      } else {
         Object var1;
         try {
            var1 = PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_CLEAR, (Void)null, -1, -1, -1);
         } catch (Throwable var3) {
            var3.printStackTrace();
            return;
         }

         MinecraftConnection.sendPacket(var0, var1);
      }
   }

   public static void sendTabList(@NotNull String var0, @NotNull String var1, Player... var2) {
      Objects.requireNonNull(var2, "Cannot send tab title to null players");
      Objects.requireNonNull(var0, "Tab title header cannot be null");
      Objects.requireNonNull(var1, "Tab title footer cannot be null");
      if (XReflection.supports(13)) {
         Player[] var16 = var2;
         int var17 = var2.length;

         for(int var18 = 0; var18 < var17; ++var18) {
            Player var19 = var16[var18];
            var19.setPlayerListHeaderFooter(var0, var1);
         }

      } else {
         try {
            Class var3 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent").unreflect();
            Class var4 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("PacketPlayOutPlayerListHeaderFooter").unreflect();
            Method var5 = var3.getDeclaredClasses()[0].getMethod("a", String.class);
            Object var6 = var5.invoke((Object)null, "{\"text\":\"" + var0 + "\"}");
            Object var7 = var5.invoke((Object)null, "{\"text\":\"" + var1 + "\"}");
            Object var8 = var4.getConstructor().newInstance();
            Field var9 = var4.getDeclaredField("a");
            Field var10 = var4.getDeclaredField("b");
            var9.setAccessible(true);
            var9.set(var8, var6);
            var10.setAccessible(true);
            var10.set(var8, var7);
            Player[] var11 = var2;
            int var12 = var2.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               Player var14 = var11[var13];
               MinecraftConnection.sendPacket(var14, var8);
            }

         } catch (Exception var15) {
            throw new IllegalStateException("Failed to send tablist: " + var0 + " - " + var1, var15);
         }
      }
   }

   static {
      MethodHandle var0 = null;
      MethodHandle var1 = null;
      MethodHandle var2 = null;
      MethodHandle var3 = null;
      MethodHandle var4 = null;
      Object var5 = null;
      Object var6 = null;
      Object var7 = null;
      Object var8 = null;
      MinecraftClassHandle var9 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent");

      boolean var11;
      try {
         var2 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetTitlesAnimationPacket").constructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}).reflect();
         var3 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetTitleTextPacket").constructor(new ClassHandle[]{var9}).reflect();
         var4 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetSubtitleTextPacket").constructor(new ClassHandle[]{var9}).reflect();
         var11 = true;
      } catch (Throwable var23) {
         var11 = false;
      }

      boolean var10;
      try {
         Player.class.getDeclaredMethod("sendTitle", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
         var10 = true;
      } catch (NoSuchMethodException var22) {
         var10 = false;
      }

      SUPPORTS_TITLES = var10;
      if (!SUPPORTS_TITLES) {
         MinecraftClassHandle var12 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("ChatComponentText");
         MinecraftClassHandle var13 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutTitle");
         Class var14 = ((Class)var13.unreflect()).getDeclaredClasses()[0];
         Object[] var15 = var14.getEnumConstants();
         int var16 = var15.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            Object var18 = var15[var17];
            String var19 = var18.toString();
            byte var20 = -1;
            switch(var19.hashCode()) {
            case -1277871080:
               if (var19.equals("SUBTITLE")) {
                  var20 = 2;
               }
               break;
            case 64208429:
               if (var19.equals("CLEAR")) {
                  var20 = 3;
               }
               break;
            case 79826726:
               if (var19.equals("TIMES")) {
                  var20 = 0;
               }
               break;
            case 79833656:
               if (var19.equals("TITLE")) {
                  var20 = 1;
               }
            }

            switch(var20) {
            case 0:
               var5 = var18;
               break;
            case 1:
               var6 = var18;
               break;
            case 2:
               var7 = var18;
               break;
            case 3:
               var8 = var18;
            }
         }

         try {
            var1 = var12.constructor(new Class[]{String.class}).reflect();
            var0 = var13.constructor(new Class[]{var14, (Class)var9.unreflect(), Integer.TYPE, Integer.TYPE, Integer.TYPE}).reflect();
         } catch (ReflectiveOperationException var21) {
            var21.printStackTrace();
         }
      }

      TITLE_ACTION_TITLE = var6;
      TITLE_ACTION_SUBTITLE = var7;
      TITLE_ACTION_TIMES = var5;
      TITLE_ACTION_CLEAR = var8;
      PACKET_PLAY_OUT_TITLE = var0;
      CHAT_COMPONENT_TEXT = var1;
      USE_TEXT_COMPONENTS = var11;
      ClientboundSetTitlesAnimationPacket = var2;
      ClientboundSetTitleTextPacket = var3;
      ClientboundSetSubtitleTextPacket = var4;
   }
}

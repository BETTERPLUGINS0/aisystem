package me.PM2.infinitevehicles.xseries.messages;

import com.google.common.base.Strings;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Callable;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftConnection;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ActionBar {
   private static final boolean USE_SPIGOT_API = XReflection.of(Player.class).inner("public static class Spigot").method("public void sendMessage(net.md_5.bungee.api.ChatMessageType position,net.md_5.bungee.api.chat.BaseComponent component)").exists();
   private static final MethodHandle CHAT_COMPONENT_TEXT;
   private static final MethodHandle PACKET_PLAY_OUT_CHAT;
   private static final Object CHAT_MESSAGE_TYPE;
   private static final char TIME_SPECIFIER_START = '^';
   private static final char TIME_SPECIFIER_END = '|';

   private ActionBar() {
   }

   public static void sendActionBar(@NotNull Plugin var0, @NotNull Player var1, @Nullable String var2) {
      if (!Strings.isNullOrEmpty(var2) && var2.charAt(0) == '^') {
         int var3 = var2.indexOf(124);
         if (var3 != -1) {
            int var4 = 0;

            try {
               var4 = Integer.parseInt(var2.substring(1, var3)) * 20;
            } catch (NumberFormatException var6) {
            }

            if (var4 >= 0) {
               sendActionBar(var0, var1, MessageComponents.fromLegacy(var2.substring(var3 + 1)), (long)var4);
            }
         }
      }

      sendActionBar(var1, var2);
   }

   public static void sendActionBar(@NotNull Player var0, @Nullable String var1) {
      Objects.requireNonNull(var0, "Cannot send action bar to null player");
      Objects.requireNonNull(var1, "Cannot send null actionbar message");
      if (USE_SPIGOT_API) {
         var0.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(var1));
      } else {
         try {
            Object var2 = CHAT_COMPONENT_TEXT.invoke("{\"text\":\"" + var1.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
            Object var3 = PACKET_PLAY_OUT_CHAT.invoke(var2, CHAT_MESSAGE_TYPE);
            MinecraftConnection.sendPacket(var0, var3);
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

      }
   }

   public static void sendActionBar(@NotNull Player var0, @Nullable BaseComponent var1) {
      Objects.requireNonNull(var0, "Cannot send action bar to null player");
      Objects.requireNonNull(var1, "Cannot send null actionbar message");
      if (USE_SPIGOT_API) {
         var0.spigot().sendMessage(ChatMessageType.ACTION_BAR, var1);
      } else {
         try {
            Object var2 = CHAT_COMPONENT_TEXT.invoke("{\"text\":\"" + var1.toPlainText().replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
            Object var3 = PACKET_PLAY_OUT_CHAT.invoke(var2, CHAT_MESSAGE_TYPE);
            MinecraftConnection.sendPacket(var0, var3);
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

      }
   }

   public static void sendActionBar(@NotNull Plugin var0, @NotNull final Player var1, @Nullable final BaseComponent var2, final long var3) {
      if (var3 >= 1L) {
         Objects.requireNonNull(var0, "Cannot send consistent actionbar with null plugin");
         Objects.requireNonNull(var1, "Cannot send actionbar to null player");
         Objects.requireNonNull(var2, "Cannot send null actionbar message");
         (new BukkitRunnable() {
            long repeater = var3;

            public void run() {
               ActionBar.sendActionBar(var1, var2);
               this.repeater -= 40L;
               if (this.repeater - 40L < -20L) {
                  this.cancel();
               }

            }
         }).runTaskTimerAsynchronously(var0, 0L, 40L);
      }
   }

   /** @deprecated */
   @Deprecated
   public static void sendPlayersActionBar(@Nullable String var0) {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player var2 = (Player)var1.next();
         sendActionBar(var2, var0);
      }

   }

   public static void clearActionBar(@NotNull Player var0) {
      sendActionBar(var0, " ");
   }

   /** @deprecated */
   @Deprecated
   public static void clearPlayersActionBar() {
      Iterator var0 = Bukkit.getOnlinePlayers().iterator();

      while(var0.hasNext()) {
         Player var1 = (Player)var0.next();
         clearActionBar(var1);
      }

   }

   public static void sendActionBarWhile(@NotNull Plugin var0, @NotNull final Player var1, @Nullable final BaseComponent var2, @NotNull final Callable<Boolean> var3) {
      (new BukkitRunnable() {
         public void run() {
            try {
               if (!(Boolean)var3.call()) {
                  this.cancel();
                  return;
               }
            } catch (Exception var2x) {
               var2x.printStackTrace();
            }

            ActionBar.sendActionBar(var1, var2);
         }
      }).runTaskTimerAsynchronously(var0, 0L, 40L);
   }

   public static void sendActionBarWhile(@NotNull Plugin var0, @NotNull final Player var1, @Nullable final Callable<BaseComponent> var2, @NotNull final Callable<Boolean> var3) {
      (new BukkitRunnable() {
         public void run() {
            try {
               if (!(Boolean)var3.call()) {
                  this.cancel();
                  return;
               }

               ActionBar.sendActionBar(var1, (BaseComponent)var2.call());
            } catch (Exception var2x) {
               var2x.printStackTrace();
            }

         }
      }).runTaskTimerAsynchronously(var0, 0L, 40L);
   }

   static {
      MethodHandle var0 = null;
      MethodHandle var1 = null;
      Object var2 = null;
      if (!USE_SPIGOT_API) {
         Lookup var3 = MethodHandles.lookup();
         Class var4 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("PacketPlayOutChat").unreflect();
         Class var5 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent").unreflect();
         Class var6 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent$ChatSerializer").unreflect();

         try {
            var1 = var3.findStatic(var6, "a", MethodType.methodType(var5, String.class));
            Class var7 = Class.forName(XReflection.NMS_PACKAGE + (String)XReflection.v(17, (Object)"network.chat").orElse((Object)"") + "ChatMessageType");
            MethodType var8 = MethodType.methodType(Void.TYPE, var5, var7);
            Object[] var9 = var7.getEnumConstants();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Object var12 = var9[var11];
               String var13 = var12.toString();
               if (var13.equals("GAME_INFO") || var13.equalsIgnoreCase("ACTION_BAR")) {
                  var2 = var12;
                  break;
               }
            }

            var0 = var3.findConstructor(var4, var8);
         } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException var15) {
            try {
               var2 = 2;
               var0 = var3.findConstructor(var4, MethodType.methodType(Void.TYPE, var5, Byte.TYPE));
            } catch (IllegalAccessException | NoSuchMethodException var14) {
               var14.printStackTrace();
            }
         }
      }

      CHAT_MESSAGE_TYPE = var2;
      CHAT_COMPONENT_TEXT = var1;
      PACKET_PLAY_OUT_CHAT = var0;
   }
}

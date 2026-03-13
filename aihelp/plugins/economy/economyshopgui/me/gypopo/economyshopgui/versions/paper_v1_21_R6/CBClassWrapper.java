package me.gypopo.economyshopgui.versions.paper_v1_21_R6;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.gypopo.economyshopgui.methodes.SendMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CBClassWrapper {
   private static Class<?> craftChatMessage;
   private static Method chatMessageFromString;
   private static Method chatMessageFromStringOrNull;
   private static Method chatMessageFromComponent;
   private static Method chatMessageToJSON;
   private static Method chatMessageFromJSON;
   private static Method chatMessageFromJSONOrString;
   private static Class<?> craftItemStack;
   private static Method craftStackAsNMSCopy;
   private static Method craftStackAsBukkitCopy;

   public static Component[] chatMessageFromString(String s, boolean b) {
      try {
         return (Component[])chatMessageFromString.invoke((Object)null, s, b);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Component chatMessageFromStringOrNull(String s) {
      try {
         return (Component)chatMessageFromStringOrNull.invoke((Object)null, s);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static String chatMessageFromComponent(Component s) {
      try {
         return (String)chatMessageFromComponent.invoke((Object)null, s);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static String chatMessageToJSON(Component s) {
      try {
         return (String)chatMessageToJSON.invoke((Object)null, s);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Component chatMessageFromJSON(String s) {
      try {
         return (Component)chatMessageFromJSON.invoke((Object)null, s);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Component chatMessageFromJSONOrString(String s) {
      try {
         return (Component)chatMessageFromJSONOrString.invoke((Object)null, s);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static ItemStack craftStackAsNMSCopy(org.bukkit.inventory.ItemStack item) {
      try {
         return (ItemStack)craftStackAsNMSCopy.invoke((Object)null, item);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static org.bukkit.inventory.ItemStack craftStackAsBukkitCopy(ItemStack item) {
      try {
         return (org.bukkit.inventory.ItemStack)craftStackAsBukkitCopy.invoke((Object)null, item);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   static {
      try {
         craftChatMessage = Class.forName("org.bukkit.craftbukkit.util.CraftChatMessage");
         chatMessageFromString = craftChatMessage.getDeclaredMethod("fromString", String.class, Boolean.TYPE);
         chatMessageFromString.setAccessible(true);
         chatMessageFromStringOrNull = craftChatMessage.getDeclaredMethod("fromStringOrNull", String.class);
         chatMessageFromStringOrNull.setAccessible(true);
         chatMessageFromComponent = craftChatMessage.getDeclaredMethod("fromComponent", Component.class);
         chatMessageFromComponent.setAccessible(true);
         chatMessageToJSON = craftChatMessage.getDeclaredMethod("toJSON", Component.class);
         chatMessageToJSON.setAccessible(true);
         chatMessageFromJSON = craftChatMessage.getDeclaredMethod("fromJSON", String.class);
         chatMessageFromJSON.setAccessible(true);
         chatMessageFromJSONOrString = craftChatMessage.getDeclaredMethod("fromJSONOrString", String.class);
         chatMessageFromJSONOrString.setAccessible(true);
         craftItemStack = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
         craftStackAsNMSCopy = craftItemStack.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
         craftStackAsNMSCopy.setAccessible(true);
         craftStackAsBukkitCopy = craftItemStack.getDeclaredMethod("asBukkitCopy", ItemStack.class);
         craftStackAsBukkitCopy.setAccessible(true);
      } catch (NoSuchMethodException | ClassNotFoundException var1) {
         SendMessage.errorMessage("Failed to initialize CraftBukkit class wrapper, this is a bug!");
         var1.printStackTrace();
      }

   }
}

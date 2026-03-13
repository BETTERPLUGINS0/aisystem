package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.spigot.text.SpigotText;
import com.nisovin.shopkeepers.text.HoverEventText;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.text.TextBuilder;
import com.nisovin.shopkeepers.tradelog.data.PlayerRecord;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TextUtils {
   public static final int LORE_MAX_LENGTH = 32;
   private static final DecimalFormat DECIMAL_FORMAT;
   private static final DecimalFormat DECIMAL_FORMAT_PRECISE;
   private static final String UNKNOWN_PLAYER = "[unknown]";
   public static final char COLOR_CHAR_ALTERNATIVE = '&';
   private static final Pattern STRIP_COLOR_ALTERNATIVE_PATTERN;
   private static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
   public static final char HEX_CHAR = '#';
   private static final String HEX_DIGITS = "0123456789AaBbCcDdEeFf";

   public static String format(double number) {
      return (String)Unsafe.assertNonNull(DECIMAL_FORMAT.format(number));
   }

   public static String formatPrecise(double number) {
      return (String)Unsafe.assertNonNull(DECIMAL_FORMAT_PRECISE.format(number));
   }

   public static String getLocationString(Location location) {
      return getLocationString(LocationUtils.getWorld(location).getName(), location.getX(), location.getY(), location.getZ());
   }

   public static String getLocationString(Block block) {
      Validate.notNull(block, (String)"block is null");
      return getLocationString(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public static String getLocationString(BlockLocation blockLocation) {
      Validate.notNull(blockLocation, (String)"blockLocation is null");
      return getLocationString(blockLocation.getWorldName(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
   }

   public static String getLocationString(BlockLocation blockLocation, double yaw) {
      Validate.notNull(blockLocation, (String)"blockLocation is null");
      return getLocationString(blockLocation.getWorldName(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), yaw);
   }

   public static String getLocationString(@Nullable String worldName, int x, int y, int z) {
      return worldName + "," + x + "," + y + "," + z;
   }

   public static String getLocationString(@Nullable String worldName, int x, int y, int z, double yaw) {
      return worldName + "," + x + "," + y + "," + z + "," + DECIMAL_FORMAT.format(yaw);
   }

   public static String getLocationString(@Nullable String worldName, double x, double y, double z) {
      return worldName + "," + DECIMAL_FORMAT.format(x) + "," + DECIMAL_FORMAT.format(y) + "," + DECIMAL_FORMAT.format(z);
   }

   public static String getChunkString(ChunkCoords chunk) {
      Validate.notNull(chunk, (String)"chunk is null");
      return getChunkString(chunk.getWorldName(), chunk.getChunkX(), chunk.getChunkZ());
   }

   public static String getChunkString(Chunk chunk) {
      Validate.notNull(chunk, (String)"chunk is null");
      return getChunkString(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
   }

   public static String getChunkString(String worldName, int cx, int cz) {
      return worldName + "," + cx + "," + cz;
   }

   public static String getPlayerString(Player player) {
      Validate.notNull(player, (String)"player is null");
      return getPlayerString(player.getName(), player.getUniqueId());
   }

   public static String getPlayerString(User user) {
      Validate.notNull(user, (String)"user is null");
      return getPlayerString(user.getLastKnownName(), user.getUniqueId());
   }

   public static String getPlayerString(@Nullable String playerName, @Nullable UUID playerUUID) {
      if (playerName != null) {
         return playerName + (playerUUID == null ? "" : " (" + String.valueOf(playerUUID) + ")");
      } else {
         return playerUUID != null ? playerUUID.toString() : "[unknown]";
      }
   }

   public static String getPlayerNameOrUUID(@Nullable String playerName, @Nullable UUID playerUUID) {
      if (playerName != null) {
         return playerName;
      } else {
         return playerUUID != null ? playerUUID.toString() : "[unknown]";
      }
   }

   public static String getPlayerNameOrUnknown(@Nullable String playerName) {
      return getPlayerNameOrUUID(playerName, (UUID)null);
   }

   public static boolean isAnyColorChar(char c) {
      return c == 167 || c == '&';
   }

   public static boolean containsColorChar(String text) {
      return StringUtils.contains(text, 167);
   }

   public static String translateColorCodesToAlternative(char altColorChar, String text) {
      Validate.notNull(text, (String)"text is null");
      char[] c = text.toCharArray();

      for(int i = 0; i < c.length - 1; ++i) {
         if (c[i] == 167 && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(c[i + 1]) > -1) {
            c[i] = altColorChar;
            c[i + 1] = Character.toLowerCase(c[i + 1]);
         }
      }

      return new String(c);
   }

   public static String stripColor(String text) {
      Validate.notNull(text, (String)"text is null");
      if (text.isEmpty()) {
         return text;
      } else {
         String uncolored = (String)Unsafe.assertNonNull(ChatColor.stripColor(text));
         uncolored = STRIP_COLOR_ALTERNATIVE_PATTERN.matcher(uncolored).replaceAll("");
         return uncolored;
      }
   }

   public static String decolorize(String text) {
      Validate.notNull(text, (String)"text is null");
      return text.isEmpty() ? text : translateColorCodesToAlternative('&', text);
   }

   public static List<String> decolorize(List<? extends String> list) {
      Validate.notNull(list, (String)"list is null");
      List<String> decolored = new ArrayList(list.size());
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         String string = (String)var2.next();
         decolored.add(translateColorCodesToAlternative('&', string));
      }

      return decolored;
   }

   public static List<?> decolorizeUnknown(List<?> list) {
      Validate.notNull(list, (String)"list is null");
      List<Object> decolored = new ArrayList(list.size());

      Object decolorizedEntry;
      for(Iterator var2 = list.iterator(); var2.hasNext(); decolored.add(decolorizedEntry)) {
         Object entry = var2.next();
         decolorizedEntry = entry;
         if (entry instanceof String) {
            decolorizedEntry = translateColorCodesToAlternative('&', (String)entry);
         }
      }

      return decolored;
   }

   public static String colorize(String text) {
      Validate.notNull(text, (String)"text is null");
      return text.isEmpty() ? text : ChatColor.translateAlternateColorCodes('&', text);
   }

   public static List<String> colorize(List<? extends String> list) {
      Validate.notNull(list, (String)"list is null");
      List<String> colored = new ArrayList(list.size());
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         String text = (String)var2.next();
         colored.add(colorize(text));
      }

      return colored;
   }

   public static List<?> colorizeUnknown(List<?> list) {
      Validate.notNull(list, (String)"list is null");
      List<Object> colored = new ArrayList(list.size());

      Object colorizedEntry;
      for(Iterator var2 = list.iterator(); var2.hasNext(); colored.add(colorizedEntry)) {
         Object entry = var2.next();
         colorizedEntry = entry;
         if (entry instanceof String) {
            colorizedEntry = colorize((String)entry);
         }
      }

      return colored;
   }

   private static boolean isHexDigit(char c) {
      return "0123456789AaBbCcDdEeFf".indexOf(c) != -1;
   }

   public static boolean isHexCode(String string) {
      if (string.length() != 7) {
         return false;
      } else if (string.charAt(0) != '#') {
         return false;
      } else {
         for(int i = 1; i < 7; ++i) {
            char c = string.charAt(i);
            if (!isHexDigit(c)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isBukkitHexCode(String string) {
      if (string.length() != 14) {
         return false;
      } else if (!isAnyColorChar(string.charAt(0))) {
         return false;
      } else {
         char c1 = string.charAt(1);
         if (c1 != 'x' && c1 != 'X') {
            return false;
         } else {
            for(int i = 2; i < 14; i += 2) {
               if (!isAnyColorChar(string.charAt(i))) {
                  return false;
               }

               if (!isHexDigit(string.charAt(i + 1))) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static String toBukkitHexCode(String hexCode, char formattingCode) {
      Validate.notEmpty(hexCode, "hexCode is null or empty");
      Validate.isTrue(hexCode.length() == 7, () -> {
         return "Invalid hexCode: " + hexCode;
      });
      StringBuilder bukkitHexCode = new StringBuilder(14);
      bukkitHexCode.append(formattingCode).append('x');

      for(int i = 1; i < 7; ++i) {
         char c = hexCode.charAt(i);
         bukkitHexCode.append(formattingCode).append(c);
      }

      return bukkitHexCode.toString();
   }

   public static String convertHexColorsToBukkit(String text) {
      Validate.notNull(text, (String)"text is null");
      StringBuilder result = new StringBuilder(text);

      for(int i = 0; i < result.length() - 1; ++i) {
         char colorChar = result.charAt(i);
         if (isAnyColorChar(colorChar) && result.charAt(i + 1) == '#' && i + 7 < result.length() && isHexCode(result.substring(i + 1, i + 8))) {
            result.setCharAt(i, colorChar);
            result.setCharAt(i + 1, 'x');

            for(int j = 2; j < 14; j += 2) {
               result.insert(i + j, colorChar);
            }

            i += 13;
         }
      }

      return result.toString();
   }

   public static String fromBukkitHexCode(String bukkitHexCode) {
      Validate.notEmpty(bukkitHexCode, "bukkitHexCode is null or empty");
      Validate.isTrue(bukkitHexCode.length() == 14, () -> {
         return "Invalid bukkitHexCode: " + bukkitHexCode;
      });
      StringBuilder hexCode = new StringBuilder(7);
      hexCode.append('#');

      for(int i = 3; i < 14; i += 2) {
         char c = bukkitHexCode.charAt(i);
         hexCode.append(c);
      }

      return hexCode.toString();
   }

   public static void wrap(List<String> source, int maxVisibleLength) {
      ListIterator<String> iterator = source.listIterator();
      StringBuilder activeFormat = new StringBuilder();
      StringBuilder currentLine = new StringBuilder();

      while(true) {
         String line;
         do {
            if (!iterator.hasNext()) {
               return;
            }

            line = (String)Unsafe.assertNonNull((String)iterator.next());
         } while(getVisibleLength(line) <= maxVisibleLength);

         iterator.remove();
         activeFormat.setLength(0);
         currentLine.setLength(0);
         int visibleLength = 0;
         int lastWhitespaceIndex = -1;
         int lastWhitespaceVisibleLength = -1;

         for(int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (c == 167) {
               currentLine.append(c);
               activeFormat.append(c);
               ++i;
               if (i < line.length()) {
                  char code = line.charAt(i);
                  ChatColor chatColor = ChatColor.getByChar(code);
                  if (chatColor != null && (chatColor.isColor() || chatColor == ChatColor.RESET)) {
                     activeFormat.setLength(0);
                     activeFormat.append(c);
                  }

                  currentLine.append(code);
                  activeFormat.append(code);
               }
            } else {
               if (Character.isWhitespace(c)) {
                  lastWhitespaceIndex = currentLine.length();
                  lastWhitespaceVisibleLength = visibleLength;
               }

               currentLine.append(c);
               ++visibleLength;
               if (visibleLength >= maxVisibleLength) {
                  if (lastWhitespaceIndex != -1) {
                     iterator.add(currentLine.substring(0, lastWhitespaceIndex));
                     String remainder = currentLine.substring(lastWhitespaceIndex + 1);
                     currentLine.setLength(0);
                     currentLine.append(activeFormat).append(remainder);
                     visibleLength -= lastWhitespaceVisibleLength + 1;
                     lastWhitespaceIndex = -1;
                     lastWhitespaceVisibleLength = -1;
                  } else {
                     iterator.add(currentLine.toString());
                     currentLine.setLength(0);
                     currentLine.append(activeFormat);
                     visibleLength = 0;
                  }
               }
            }
         }

         if (currentLine.length() > 0) {
            iterator.add(currentLine.toString());
         }
      }
   }

   private static int getVisibleLength(String text) {
      int length = 0;

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (c == 167) {
            ++i;
         } else {
            ++length;
         }
      }

      return length;
   }

   public static void sendMessage(CommandSender recipient, String message) {
      Validate.notNull(recipient, (String)"recipient is null");
      Validate.notNull(message, (String)"message is null");
      if (!message.isEmpty()) {
         String[] var2 = StringUtils.splitLines(message, true);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            recipient.sendMessage(line);
         }

      }
   }

   public static void sendMessage(CommandSender recipient, String message, Map<? extends String, ?> arguments) {
      sendMessage(recipient, StringUtils.replaceArguments(message, arguments));
   }

   public static void sendMessage(CommandSender recipient, String message, Object... argumentPairs) {
      sendMessage(recipient, StringUtils.replaceArguments(message, argumentPairs));
   }

   public static Text getPlayerText(Player player) {
      Validate.notNull(player, (String)"player is null");
      return getPlayerText(player.getName(), player.getUniqueId());
   }

   public static Text getPlayerText(PlayerRecord playerRecord) {
      Validate.notNull(playerRecord, (String)"playerRecord is null");
      return getPlayerText(playerRecord.getName(), playerRecord.getUniqueId());
   }

   public static Text getPlayerText(User user) {
      Validate.notNull(user, (String)"user is null");
      return getPlayerText(user.getName(), user.getUniqueId());
   }

   public static Text getPlayerText(@Nullable User user, String fallback) {
      return user != null ? getPlayerText(user) : getPlayerText((String)fallback, (UUID)null);
   }

   public static Text getPlayerText(@Nullable String playerName, @Nullable UUID playerUUID) {
      if (playerName != null) {
         if (playerUUID != null) {
            String playerUUIDString = playerUUID.toString();
            return Text.hoverEvent(Text.of(playerUUIDString)).childInsertion(playerUUIDString).childText(playerName).buildRoot();
         } else {
            return Text.of(playerName);
         }
      } else {
         return playerUUID != null ? Text.of(playerUUID.toString()) : Text.of("[unknown]");
      }
   }

   public static Text getShopText(Shopkeeper shopkeeper) {
      return getShopText(Text.of(shopkeeper.getDisplayName()), shopkeeper.getUniqueId());
   }

   public static Text getShopText(Text displayName, UUID uniqueId) {
      String uniqueIdString = uniqueId.toString();
      return Text.hoverEvent(Text.of(uniqueIdString)).childInsertion(uniqueIdString).childPlaceholder("displayName").buildRoot().setPlaceholderArguments("displayName", displayName);
   }

   public static Text getItemText(@Nullable UnmodifiableItemStack itemStack) {
      return getItemText(ItemUtils.asItemStackOrNull(itemStack));
   }

   public static Text getItemText(@Nullable @ReadOnly ItemStack itemStack) {
      return (Text)(itemStack == null ? Text.text("") : getItemHover(itemStack).child(getItemNameForDisplay(itemStack)).getRoot());
   }

   public static TextBuilder getItemHover(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      UnmodifiableItemStack unmodifiableItem = UnmodifiableItemStack.ofNonNull(itemStack);
      return Text.hoverEvent((HoverEventText.Content)(new HoverEventText.ItemContent(unmodifiableItem)));
   }

   public static Text getMaterialNameForDisplay(@Nullable Material material) {
      if (material == null) {
         return Text.EMPTY;
      } else {
         Text formattedName = Text.of(ItemUtils.formatMaterialName(material));
         String translationKey = material.getTranslationKey();
         return Text.translatable(translationKey).child(formattedName).getRoot();
      }
   }

   public static Text getMaterialNameForDisplay(@Nullable ItemStack itemStack) {
      return getMaterialNameForDisplay(itemStack != null ? itemStack.getType() : null);
   }

   public static Text getItemNameForDisplay(@Nullable @ReadOnly ItemStack itemStack) {
      String displayName = ItemUtils.getDisplayNameOrEmpty(itemStack);
      return !displayName.isEmpty() ? Text.of(displayName) : getMaterialNameForDisplay(itemStack);
   }

   public static void sendMessage(CommandSender recipient, Text message) {
      SpigotText.sendMessage(recipient, message);
   }

   public static void sendMessage(CommandSender recipient, Text message, MessageArguments arguments) {
      Validate.notNull(recipient, (String)"recipient is null");
      Validate.notNull(message, (String)"message is null");
      message.setPlaceholderArguments(arguments);
      sendMessage(recipient, message);
   }

   public static void sendMessage(CommandSender recipient, Text message, Map<? extends String, ?> arguments) {
      Validate.notNull(recipient, (String)"recipient is null");
      Validate.notNull(message, (String)"message is null");
      message.setPlaceholderArguments(arguments);
      sendMessage(recipient, message);
   }

   public static void sendMessage(CommandSender recipient, Text message, Object... argumentPairs) {
      Validate.notNull(recipient, (String)"recipient is null");
      Validate.notNull(message, (String)"message is null");
      message.setPlaceholderArguments(argumentPairs);
      sendMessage(recipient, message);
   }

   private TextUtils() {
   }

   static {
      DECIMAL_FORMAT = new DecimalFormat("0.##", new DecimalFormatSymbols(Locale.US));
      DECIMAL_FORMAT_PRECISE = new DecimalFormat("0.####", new DecimalFormatSymbols(Locale.US));
      DECIMAL_FORMAT.setGroupingUsed(false);
      DECIMAL_FORMAT_PRECISE.setGroupingUsed(false);
      STRIP_COLOR_ALTERNATIVE_PATTERN = Pattern.compile("(?i)&[0-9A-FK-ORX]");
   }
}

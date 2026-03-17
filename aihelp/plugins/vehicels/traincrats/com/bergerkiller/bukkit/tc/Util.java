package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.MaterialTypeProperty;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.EntityPropertyUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil.ItemSynchronizer;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.AveragedItemParser;
import com.bergerkiller.bukkit.tc.utils.BlockPhysicsEventDataAccessor;
import com.bergerkiller.bukkit.tc.utils.BoundingRange;
import com.bergerkiller.bukkit.tc.utils.FormattedSpeed;
import com.bergerkiller.bukkit.tc.utils.QuoteEscapedString;
import com.bergerkiller.bukkit.tc.utils.TrackMovingPoint;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityEquipmentHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityEquipmentHandle.OwnerType;
import com.bergerkiller.generated.net.minecraft.server.level.EntityTrackerEntryStateHandle;
import com.bergerkiller.generated.net.minecraft.server.network.PlayerConnectionHandle;
import com.bergerkiller.generated.net.minecraft.world.level.chunk.ChunkHandle;
import com.bergerkiller.generated.net.minecraft.world.phys.AxisAlignedBBHandle;
import com.bergerkiller.mountiplex.reflection.util.FastMethod;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

public class Util {
   public static final MaterialTypeProperty ISVERTRAIL;
   public static final MaterialTypeProperty ISTCRAIL;
   private static final String SEPARATOR_REGEX = "[|/\\\\]";
   private static List<Block> blockbuff;
   private static final NumberFormat numberBox_NumberFormat;
   private static final NumberFormat animationodeTime_NumberFormat1000;
   private static final NumberFormat animationodeTime_NumberFormat100;
   private static final NumberFormat animationodeTime_NumberFormat10;
   private static final NumberFormat animationodeTime_NumberFormat1;
   private static final double SQ_COS_22_5;
   private static final Util.TeleportPositionMethod TELEPORT_POSITION_METHOD;
   private static final EnumMap<ChatColor, Color> COLOR_TO_RGB;
   private static final Color UNKNOWN_CHAT_COLOR;

   public static NumberFormat createNumberFormat(int min_fractionDigits, int max_fractionDigits) {
      NumberFormat fmt = NumberFormat.getNumberInstance(Locale.ENGLISH);
      fmt.setMinimumFractionDigits(min_fractionDigits);
      fmt.setMaximumFractionDigits(max_fractionDigits);
      fmt.setGroupingUsed(false);
      return fmt;
   }

   public static int minStringIndex(int a, int b) {
      if (a != -1 && b != -1) {
         return a < b ? a : b;
      } else {
         return a > b ? a : b;
      }
   }

   public static String[] splitBySeparator(String text) {
      return text.split("[|/\\\\]");
   }

   public static BlockFace getVerticalFace(boolean up) {
      return up ? BlockFace.UP : BlockFace.DOWN;
   }

   public static BlockFace snapFace(BlockFace face) {
      switch(face) {
      case NORTH_NORTH_EAST:
         return BlockFace.NORTH_EAST;
      case EAST_NORTH_EAST:
         return BlockFace.EAST;
      case EAST_SOUTH_EAST:
         return BlockFace.SOUTH_EAST;
      case SOUTH_SOUTH_EAST:
         return BlockFace.SOUTH;
      case SOUTH_SOUTH_WEST:
         return BlockFace.SOUTH_WEST;
      case WEST_SOUTH_WEST:
         return BlockFace.WEST;
      case WEST_NORTH_WEST:
         return BlockFace.NORTH_WEST;
      case NORTH_NORTH_WEST:
         return BlockFace.NORTH;
      default:
         return face;
      }
   }

   /** @deprecated */
   @Deprecated
   public static List<Block> getSignsFromRails(Block railsblock) {
      return getSignsFromRails(blockbuff, railsblock);
   }

   /** @deprecated */
   @Deprecated
   public static List<Block> getSignsFromRails(List<Block> rval, Block railsblock) {
      rval.clear();
      addSignsFromRails(rval, railsblock);
      return rval;
   }

   /** @deprecated */
   @Deprecated
   public static void addSignsFromRails(List<Block> rval, Block railsBlock) {
      RailType railType = RailType.getType(railsBlock);
      if (railType != RailType.NONE) {
         RailLookup.TrackedSign[] var3 = RailPiece.create(railType, railsBlock).signs();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            RailLookup.TrackedSign trackedSign = var3[var5];
            rval.add(trackedSign.signBlock);
         }

      }
   }

   public static boolean hasAttachedSigns(Block middle) {
      return addAttachedSigns(middle, (Collection)null);
   }

   public static boolean addAttachedSigns(Block middle, Collection<Block> rval) {
      boolean found = false;
      BlockFace[] var3 = FaceUtil.AXIS;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         Block b = middle.getRelative(face);
         if ((Boolean)MaterialUtil.ISSIGN.get(b) && BlockUtil.getAttachedFace(b) == face.getOppositeFace()) {
            found = true;
            if (rval != null) {
               rval.add(b);
            }
         }
      }

      return found;
   }

   /** @deprecated */
   @Deprecated
   public static Block getRailsFromSign(Block signblock) {
      return RailLookup.discoverRailPieceFromSign(signblock).block();
   }

   /** @deprecated */
   @Deprecated
   public static Block findRailsVertical(Block from, BlockFace mode) {
      RailPiece piece = findNextRailPiece(from, mode);
      return piece == null ? null : piece.block();
   }

   public static RailPiece findNextRailPiece(Block from, BlockFace mode) {
      int maxSteps = 1024;
      World world = from.getWorld();
      Block block = from;
      int max;
      int y;
      RailType type;
      if (mode == BlockFace.DOWN) {
         max = WorldUtil.getWorldMinimumHeight(world);
         y = from.getY();

         while(true) {
            --y;
            if (y < max) {
               break;
            }

            --maxSteps;
            if (maxSteps <= 0) {
               break;
            }

            block = block.getRelative(mode);
            type = RailType.getType(block);
            if (type != RailType.NONE) {
               return RailPiece.create(type, block);
            }
         }
      } else if (mode == BlockFace.UP) {
         max = WorldUtil.getWorldMaximumHeight(world);
         y = from.getY();

         while(true) {
            ++y;
            if (y >= max) {
               break;
            }

            --maxSteps;
            if (maxSteps <= 0) {
               break;
            }

            block = block.getRelative(mode);
            type = RailType.getType(block);
            if (type != RailType.NONE) {
               return RailPiece.create(type, block);
            }
         }
      } else {
         while(true) {
            --maxSteps;
            if (maxSteps <= 0) {
               break;
            }

            block = block.getRelative(mode);
            RailType type = RailType.getType(block);
            if (type != RailType.NONE) {
               return RailPiece.create(type, block);
            }
         }
      }

      return null;
   }

   public static ItemParser[] getParsers(String... items) {
      return getParsers(StringUtil.join(";", items));
   }

   public static ItemParser[] getParsers(String items) {
      List<ItemParser> parsers = new ArrayList();
      int multiplier = -1;
      String[] var4 = items.split(";");
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String type = var4[var6];
         type = type.trim();
         if (!type.isEmpty()) {
            int multiIndex = type.indexOf(35);
            if (multiIndex != -1) {
               multiplier = ParseUtil.parseInt(type.substring(0, multiIndex), -1);
               type = type.substring(multiIndex + 1);
            }

            int amount = -1;
            int idx = StringUtil.firstIndexOf(type, new String[]{"x", "X", " ", "*"});
            if (idx > 0) {
               amount = ParseUtil.parseInt(type.substring(0, idx), -1);
               if (amount != -1) {
                  type = type.substring(idx + 1);
               }
            }

            ItemParser[] keyparsers = TrainCarts.plugin.getParsers(type, amount);
            if (multiIndex != -1) {
               for(int i = 0; i < keyparsers.length; ++i) {
                  keyparsers[i] = new AveragedItemParser(keyparsers[i], multiplier);
               }
            }

            parsers.addAll(Arrays.asList(keyparsers));
         }
      }

      if (parsers.isEmpty()) {
         parsers.add(new ItemParser((Material)null));
      }

      return (ItemParser[])parsers.toArray(new ItemParser[0]);
   }

   public static Block getRailsBlock(Block from) {
      if ((Boolean)ISTCRAIL.get(from)) {
         return from;
      } else {
         from = from.getRelative(BlockFace.DOWN);
         return (Boolean)ISTCRAIL.get(from) ? from : null;
      }
   }

   public static String getTimeString(long time) {
      if (time == 0L) {
         return "00:00:00";
      } else {
         time = (long)Math.ceil(0.001D * (double)time);
         int seconds = (int)(time % 60L);
         int minutes = (int)(time % 3600L / 60L);
         int hours = (int)(time / 3600L);
         StringBuilder rval = new StringBuilder(8);
         if (hours < 10) {
            rval.append('0');
         }

         rval.append(hours).append(':');
         if (minutes < 10) {
            rval.append('0');
         }

         rval.append(minutes).append(':');
         if (seconds < 10) {
            rval.append('0');
         }

         rval.append(seconds);
         return rval.toString();
      }
   }

   private static boolean isRailsAt(Block block, BlockFace direction) {
      return getRailsBlock(block.getRelative(direction)) != null;
   }

   public static BlockFace getPlateDirection(Block plate) {
      boolean s = isRailsAt(plate, BlockFace.NORTH) || isRailsAt(plate, BlockFace.SOUTH);
      boolean w = isRailsAt(plate, BlockFace.EAST) || isRailsAt(plate, BlockFace.WEST);
      if (s && w) {
         return BlockFace.SELF;
      } else if (w) {
         return BlockFace.EAST;
      } else {
         return s ? BlockFace.SOUTH : BlockFace.SELF;
      }
   }

   public static boolean isSloped(int railsData) {
      railsData &= 7;
      return railsData >= 2 && railsData <= 5;
   }

   public static boolean isVerticalAbove(Block rails, BlockFace direction) {
      BlockData blockData = WorldUtil.getBlockData(rails.getWorld(), rails.getX(), rails.getY() + 1, rails.getZ());
      return ISVERTRAIL.get(blockData) && getVerticalRailDirection(blockData.getRawData()) == direction;
   }

   public static boolean isVerticalBelow(Block rails, BlockFace direction) {
      BlockData blockData = WorldUtil.getBlockData(rails.getWorld(), rails.getX(), rails.getY() - 1, rails.getZ());
      return ISVERTRAIL.get(blockData) && getVerticalRailDirection(blockData.getRawData()) == direction;
   }

   public static BlockFace getVerticalRailDirection(Block railsBlock) {
      return getVerticalRailDirection(MaterialUtil.getRawData(railsBlock));
   }

   public static BlockFace getVerticalRailDirection(int raildata) {
      switch(raildata) {
      case 2:
         return BlockFace.SOUTH;
      case 3:
         return BlockFace.NORTH;
      case 4:
         return BlockFace.EAST;
      case 5:
      default:
         return BlockFace.WEST;
      }
   }

   public static int getOperatorIndex(String text) {
      for(int i = 0; i < text.length(); ++i) {
         if (isOperator(text.charAt(i))) {
            return i;
         }
      }

      return -1;
   }

   public static boolean isOperator(char character) {
      return LogicUtil.containsChar(character, new char[]{'!', '=', '<', '>'});
   }

   public static boolean canBePassenger(Entity entity) {
      return entity instanceof LivingEntity;
   }

   public static boolean matchText(Collection<String> textValues, String expression) {
      if (expression.startsWith("!")) {
         return !matchText(textValues, expression.substring(1));
      } else if (!expression.isEmpty() && !textValues.isEmpty()) {
         String[] elements = expression.split("\\*");
         boolean first = expression.startsWith("*");
         boolean last = expression.endsWith("*");
         Iterator var5 = textValues.iterator();

         String text;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            text = (String)var5.next();
         } while(!matchText(text, elements, first, last));

         return true;
      } else {
         return false;
      }
   }

   public static boolean matchText(String text, String expression) {
      if (expression.isEmpty()) {
         return false;
      } else if (expression.startsWith("!")) {
         return !matchText(text, expression.substring(1));
      } else {
         return matchText(text, expression.split("\\*"), expression.startsWith("*"), expression.endsWith("*"));
      }
   }

   public static boolean matchText(String text, String[] elements, boolean firstAny, boolean lastAny) {
      if (elements != null && elements.length != 0) {
         int index = 0;
         boolean has = true;
         boolean first = true;
         String[] var7 = elements;
         int var8 = elements.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String element = var7[var9];
            if (element.length() != 0) {
               index = text.indexOf(element, index);
               if (index == -1 || first && !firstAny && index != 0) {
                  has = false;
                  break;
               }

               index += element.length();
               first = false;
            }
         }

         return has && (lastAny || index == text.length());
      } else {
         return true;
      }
   }

   public static boolean evaluate(double value, String text) {
      if (text != null && !text.isEmpty()) {
         int idx = getOperatorIndex(text);
         if (idx == -1) {
            return value > 0.0D;
         } else {
            text = text.substring(idx);
            if (!text.startsWith(">=") && !text.startsWith("=>")) {
               if (!text.startsWith("<=") && !text.startsWith("=<")) {
                  if (text.startsWith("==")) {
                     return value == ParseUtil.parseDouble(text.substring(2), 0.0D);
                  } else if (!text.startsWith("!=") && !text.startsWith("<>") && !text.startsWith("><")) {
                     if (text.startsWith(">")) {
                        return value > ParseUtil.parseDouble(text.substring(1), 0.0D);
                     } else if (text.startsWith("<")) {
                        return value < ParseUtil.parseDouble(text.substring(1), 0.0D);
                     } else if (text.startsWith("=")) {
                        return value == ParseUtil.parseDouble(text.substring(1), 0.0D);
                     } else {
                        return false;
                     }
                  } else {
                     return value != ParseUtil.parseDouble(text.substring(2), 0.0D);
                  }
               } else {
                  return value <= ParseUtil.parseDouble(text.substring(2), 0.0D);
               }
            } else {
               return value >= ParseUtil.parseDouble(text.substring(2), 0.0D);
            }
         }
      } else {
         return false;
      }
   }

   public static boolean canInstantlyBreakMinecart(Entity entity) {
      if (TCConfig.instantCreativeDestroy && canInstantlyBuild(entity)) {
         return !(entity instanceof Player) || !((Player)entity).isSneaking();
      } else {
         return false;
      }
   }

   public static boolean canInstantlyBuild(Entity entity) {
      return entity instanceof HumanEntity && EntityPropertyUtil.getAbilities((HumanEntity)entity).canInstantlyBuild();
   }

   public static boolean isSignSupported(Block block, BlockData blockDataOfBlock) {
      BlockFace face = blockDataOfBlock.getAttachedFace();
      return WorldUtil.getBlockData(block.getWorld(), block.getX() + face.getModX(), block.getY() + face.getModY(), block.getZ() + face.getModZ()).isSolid();
   }

   public static boolean isSignSupported(Block block) {
      return isSignSupported(block, WorldUtil.getBlockData(block));
   }

   public static Rails getRailsRO(Block block) {
      MaterialData data = WorldUtil.getBlockData(block).getMaterialData();
      return data instanceof Rails ? (Rails)data : null;
   }

   public static boolean isValidEntity(String entityName) {
      try {
         return EntityType.valueOf(entityName) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public static Vector parseVector(String text, Vector def) {
      String[] offsettext = splitBySeparator(text);
      Vector offset = new Vector();
      if (offsettext.length == 3) {
         offset.setX(ParseUtil.parseDouble(offsettext[0], 0.0D));
         offset.setY(ParseUtil.parseDouble(offsettext[1], 0.0D));
         offset.setZ(ParseUtil.parseDouble(offsettext[2], 0.0D));
      } else if (offsettext.length == 2) {
         offset.setX(ParseUtil.parseDouble(offsettext[0], 0.0D));
         offset.setZ(ParseUtil.parseDouble(offsettext[1], 0.0D));
      } else {
         if (offsettext.length != 1) {
            return def;
         }

         offset.setY(ParseUtil.parseDouble(offsettext[0], 0.0D));
      }

      return offset;
   }

   public static double parseAcceleration(String accelerationString, double defaultValue) {
      if (accelerationString.isEmpty()) {
         return defaultValue;
      } else {
         accelerationString = accelerationString.toLowerCase(Locale.ENGLISH);
         accelerationString = accelerationString.replace("kmh", "kmph");
         accelerationString = accelerationString.replace("kmph", "km/h");
         accelerationString = accelerationString.replace("miph", "mph");
         accelerationString = accelerationString.replace("mph", "mi/h");
         int slashIndex = accelerationString.indexOf(47);
         double factor;
         if (slashIndex == -1) {
            char lastChar = accelerationString.charAt(accelerationString.length() - 1);
            if (lastChar == 'g') {
               String g_value_str = accelerationString.substring(0, accelerationString.length() - 1);
               factor = ParseUtil.parseDouble(g_value_str, Double.NaN);
               return Double.isNaN(factor) ? defaultValue : 0.024525D * factor;
            } else {
               return ParseUtil.parseDouble(accelerationString, defaultValue);
            }
         } else {
            factor = 1.0D;
            StringBuilder valueStr = new StringBuilder(slashIndex + 1);

            int i;
            char c;
            for(i = 0; i < slashIndex; ++i) {
               c = accelerationString.charAt(i);
               if (!Character.isDigit(c) && c != '.' && c != ',' && c != '-') {
                  if (c == 'k') {
                     factor = 1000.0D;
                  } else if (c == 'f' && accelerationString.charAt(i + 1) == 't') {
                     factor = 0.3048780487804878D;
                     ++i;
                  } else if (c == 'm' && accelerationString.charAt(i + 1) == 'i') {
                     factor = 1609.344D;
                     ++i;
                  }
               } else {
                  valueStr.append(c);
               }
            }

            double value = ParseUtil.parseDouble(valueStr.toString(), Double.NaN);
            if (Double.isNaN(value)) {
               return defaultValue;
            } else {
               value *= factor;
               int num_units = 0;
               double factor = 1.0D;

               for(i = slashIndex + 1; i < accelerationString.length() && num_units < 2; ++i) {
                  c = accelerationString.charAt(i);
                  if (c == 's') {
                     factor *= 20.0D;
                     ++num_units;
                  } else if (c == 'm') {
                     factor *= 1200.0D;
                     ++num_units;
                  } else if (c == 'h') {
                     factor *= 72000.0D;
                     ++num_units;
                  }
               }

               if (num_units == 1) {
                  factor *= factor;
               }

               return value / factor;
            }
         }
      }
   }

   public static double parseVelocity(String velocityString, double defaultValue) {
      FormattedSpeed speed = FormattedSpeed.parse(velocityString, (FormattedSpeed)null);
      return speed != null ? speed.getValue() : defaultValue;
   }

   public static double calculateStraightLength(Block railsBlock, BlockFace direction) {
      TrackWalkingPoint p = new TrackWalkingPoint(railsBlock, direction);
      Vector start_dir = null;

      while(p.movedTotal < 20.0D && p.move(0.1D)) {
         if (start_dir == null) {
            start_dir = p.state.motionVector();
         } else if (p.state.position().motDot(start_dir) < 0.75D) {
            break;
         }
      }

      return p.movedTotal;
   }

   public static int parseTimeTicks(String text) {
      text = text.toLowerCase(Locale.ENGLISH);
      double ticks = -1.0D;
      if (text.endsWith("ms")) {
         ticks = 0.02D * ParseUtil.parseDouble(text.substring(0, text.length() - 2), -1.0D);
      } else if (text.endsWith("m")) {
         ticks = 1200.0D * ParseUtil.parseDouble(text.substring(0, text.length() - 1), -1.0D);
      } else if (text.endsWith("s")) {
         ticks = 20.0D * ParseUtil.parseDouble(text.substring(0, text.length() - 1), -1.0D);
      } else if (text.endsWith("t")) {
         ticks = (double)ParseUtil.parseInt(text.substring(0, text.length() - 1), -1);
      }

      return ticks < 0.0D ? -1 : (int)ticks;
   }

   public static String getUnicode(char unicode) {
      return "\\u" + Integer.toHexString(unicode | 65536).substring(1);
   }

   public static String getCleanLine(SignChangeEvent event, int line) {
      return event == null ? "" : cleanSignLine(event.getLine(line));
   }

   public static String getCleanLine(Sign sign, int line) {
      return sign == null ? "" : cleanSignLine(sign.getLine(line));
   }

   public static String cleanSignLine(String line) {
      if (line == null) {
         return "";
      } else {
         for(int i = 0; i < line.length(); ++i) {
            if (isInvalidCharacter(line.charAt(i))) {
               StringBuilder clear = new StringBuilder(line.length() - 1);
               clear.append(line, 0, i);

               for(int j = i + 1; j < line.length(); ++j) {
                  char c = line.charAt(j);
                  if (!isInvalidCharacter(c)) {
                     clear.append(c);
                  }
               }

               return clear.toString();
            }
         }

         return line;
      }
   }

   public static String[] cleanSignLines(String[] lines) {
      if (lines == null) {
         return new String[]{"", "", "", ""};
      } else {
         boolean hasInvalid = false;
         if (lines.length != 4) {
            hasInvalid = true;
            String[] newLines = new String[]{"", "", "", ""};

            for(int i = 0; i < Math.min(lines.length, 4); ++i) {
               newLines[i] = lines[i];
            }

            lines = newLines;
         }

         for(int i = 0; i < lines.length; ++i) {
            String oldLine = lines[i];
            String newLine = cleanSignLine(oldLine);
            if (oldLine != newLine) {
               if (!hasInvalid) {
                  hasInvalid = true;
                  lines = (String[])lines.clone();
               }

               lines[i] = newLine;
            }
         }

         return lines;
      }
   }

   public static boolean isInvalidCharacter(char c) {
      return Character.getType(c) == 18;
   }

   public static boolean isProtocolRotationGlitched(float angleOld, float angleNew) {
      int protOld = EntityTrackerEntryStateHandle.getProtocolRotation(angleOld);
      int protNew = EntityTrackerEntryStateHandle.getProtocolRotation(angleNew);
      return Math.abs(protNew - protOld) > 128;
   }

   public static float atOppositeRotationGlitchBoundary(float angle) {
      return angle >= 180.0F ? 179.0F : 181.0F;
   }

   public static void spawnParticle(Location loc, Particle particle) {
      loc.getWorld().spawnParticle(particle, loc, 1);
   }

   public static void spawnBubble(Location loc) {
      spawnParticle(loc, Particle.WATER_BUBBLE);
   }

   public static void spawnDustParticle(Location loc, Color color) {
      spawnDustParticle(loc, (double)color.getRed() / 255.0D, (double)color.getGreen() / 255.0D, (double)color.getBlue() / 255.0D);
   }

   public static void spawnDustParticle(Location loc, double red, double green, double blue) {
      int c_red = (int)MathUtil.clamp(255.0D * red, 0.0D, 255.0D);
      int c_green = (int)MathUtil.clamp(255.0D * green, 0.0D, 255.0D);
      int c_blue = (int)MathUtil.clamp(255.0D * blue, 0.0D, 255.0D);
      Color color = Color.fromRGB(c_red, c_green, c_blue);
      Vector position = loc.toVector();
      Iterator var12 = loc.getWorld().getPlayers().iterator();

      while(var12.hasNext()) {
         Player player = (Player)var12.next();
         if (!(player.getLocation().distanceSquared(loc) > 65536.0D)) {
            PlayerUtil.spawnDustParticles(player, position, color);
         }
      }

   }

   public static Location invertRotation(Location loc) {
      Quaternion q = Quaternion.fromYawPitchRoll((double)loc.getPitch(), (double)loc.getYaw(), 0.0D);
      q.rotateYFlip();
      Vector ypr_new = q.getYawPitchRoll();
      loc.setYaw((float)ypr_new.getY());
      loc.setPitch((float)ypr_new.getX());
      return loc;
   }

   public static BlockFace vecToFace(Vector vector, boolean useSubCardinalDirections) {
      return vecToFace(vector.getX(), vector.getY(), vector.getZ(), useSubCardinalDirections);
   }

   public static BlockFace vecToFace(double dx, double dy, double dz, boolean useSubCardinalDirections) {
      double sqlenxz = dx * dx + dz * dz;
      double sqleny = dy * dy;
      return sqleny > sqlenxz + 1.0E-6D ? FaceUtil.getVertical(dy) : FaceUtil.getDirection(dx, dz, useSubCardinalDirections);
   }

   public static Vector lerpOrientation(Vector up0, Vector up1, double theta) {
      Quaternion qa = Quaternion.fromLookDirection(up0);
      Quaternion qb = Quaternion.fromLookDirection(up1);
      Quaternion q = Quaternion.slerp(qa, qb, theta);
      return q.forwardVector();
   }

   public static Vector getArmorStandPose(Quaternion rotation) {
      double qx = rotation.getX();
      double qy = rotation.getY();
      double qz = rotation.getZ();
      double qw = rotation.getW();
      double rx = 1.0D + 2.0D * (-qy * qy - qz * qz);
      double ry = 2.0D * (qx * qy + qz * qw);
      double rz = 2.0D * (qx * qz - qy * qw);
      double uz = 2.0D * (qy * qz + qx * qw);
      double fz = 1.0D + 2.0D * (-qx * qx - qy * qy);
      if (Math.abs(rz) < 0.999999999999999D) {
         return new Vector(MathUtil.atan2(uz, fz), fastAsin(rz), MathUtil.atan2(-ry, rx));
      } else {
         double sign = rz < 0.0D ? -1.0D : 1.0D;
         return new Vector(0.0D, sign * 90.0D, -sign * 2.0D * (double)MathUtil.atan2(qx, qw));
      }
   }

   public static float fastAsin(double x) {
      return MathUtil.atan(x / Math.sqrt(1.0D - x * x));
   }

   public static Block getNextPos(Block railBlock, BlockFace direction) {
      TrackMovingPoint p = new TrackMovingPoint(railBlock, direction);
      if (!p.hasNext()) {
         return null;
      } else {
         p.next();
         if (!p.hasNext()) {
            return null;
         } else {
            p.next(false);
            return p.currentLocation.getBlock();
         }
      }
   }

   public static final void markChunkDirty(Chunk chunk) {
      ChunkHandle.fromBukkit(chunk).markDirty();
   }

   public static RailJunction faceToJunction(List<RailJunction> junctions, BlockFace face) {
      return (RailJunction)RailJunction.findBest(junctions, FaceUtil.faceToVector(face)).orElse((Object)null);
   }

   public static void loadInventoryFromConfig(Inventory inventory, ConfigurationNode config) {
      inventory.clear();
      if (config.isNode("contents")) {
         ConfigurationNode contents = config.getNode("contents");
         Iterator var3 = contents.getKeys().iterator();

         while(var3.hasNext()) {
            String indexStr = (String)var3.next();

            int index;
            try {
               index = Integer.parseInt(indexStr);
            } catch (NumberFormatException var7) {
               continue;
            }

            ItemStack item = (ItemStack)contents.get(indexStr, ItemStack.class);
            if (!ItemUtil.isEmpty(item)) {
               inventory.setItem(index, item.clone());
            }
         }
      }

   }

   public static void saveInventoryToConfig(Inventory inventory, ConfigurationNode config) {
      ConfigurationNode contents = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         ItemStack item = inventory.getItem(i);
         if (!ItemUtil.isEmpty(item)) {
            if (contents == null) {
               contents = config.getNode("contents");
            }

            contents.set(Integer.toString(i), item.clone());
         }
      }

   }

   public static void setVector(Vector v, Vector v2) {
      v.setX(v2.getX());
      v.setY(v2.getY());
      v.setZ(v2.getZ());
   }

   public static boolean isOrientationInverted(Vector vel, Quaternion q) {
      double x = q.getX();
      double y = q.getY();
      double z = q.getZ();
      double w = q.getW();
      double px = vel.getX();
      double py = vel.getY();
      double pz = vel.getZ();
      return px * (x * z + y * w) + py * (y * z - x * w) - pz * (x * x + y * y - 0.5D) <= 0.0D;
   }

   public static double fastGetRotationYaw(Quaternion rotation) {
      double x = rotation.getX();
      double y = rotation.getY();
      double z = rotation.getZ();
      double w = rotation.getW();
      double test = 2.0D * (w * x - y * z);
      double yaw;
      if (Math.abs(test) < 0.999999999999999D) {
         double x2 = x * x;
         double y2 = y * y;
         double z2 = z * z;
         yaw = (double)MathUtil.atan2(2.0D * (w * y + z * x), 1.0D - 2.0D * (x2 + y2));
         if (x2 + z2 > 0.5D) {
            yaw += yaw < 0.0D ? 180.0D : -180.0D;
         }
      } else {
         yaw = 2.0D * (double)MathUtil.atan2(z, w);
         if (test >= 0.0D) {
            yaw = -yaw;
         }
      }

      if (yaw > 180.0D) {
         yaw -= 360.0D;
      } else if (yaw < -180.0D) {
         yaw += 360.0D;
      }

      return -yaw;
   }

   public static void checkMainThread(String what) {
      if (!CommonUtil.isMainThread()) {
         TrainCarts.plugin.log(Level.WARNING, what + " called from a thread other than the main thread!");
         Thread.dumpStack();
      }

   }

   public static void correctTeleportPosition(Location loc) {
      Block locBlock = loc.getBlock();
      Vector rel = loc.toVector();
      rel.setX(rel.getX() - (double)locBlock.getX());
      rel.setY(rel.getY() - (double)locBlock.getY());
      rel.setZ(rel.getZ() - (double)locBlock.getZ());
      AxisAlignedBBHandle bounds = WorldUtil.getBlockData(locBlock).getBoundingBox(locBlock);
      if (bounds != null && rel.getX() >= bounds.getMinX() && rel.getX() <= bounds.getMaxX() && rel.getY() >= bounds.getMinY() && rel.getY() <= bounds.getMaxY() && rel.getZ() >= bounds.getMinZ() && rel.getZ() <= bounds.getMaxZ()) {
         loc.setY((double)locBlock.getY() + bounds.getMaxY() + 1.0E-5D);
      }

   }

   public static float getNextEntityYaw(float old_yaw, double yaw_change) {
      if (!(yaw_change < -90.0D) && !(yaw_change > 90.0D)) {
         int prot_yaw_rot_old = EntityTrackerEntryStateHandle.getProtocolRotation(old_yaw);
         int prot_yaw_rot_new = EntityTrackerEntryStateHandle.getProtocolRotation((float)((double)old_yaw + yaw_change));
         if (prot_yaw_rot_new != prot_yaw_rot_old) {
            float new_yaw = EntityTrackerEntryStateHandle.getRotationFromProtocol(prot_yaw_rot_new);
            double new_yaw_change = MathUtil.wrapAngle((double)new_yaw - (double)old_yaw);
            if (yaw_change < 0.0D) {
               if (new_yaw_change < yaw_change) {
                  ++prot_yaw_rot_new;
                  new_yaw = EntityTrackerEntryStateHandle.getRotationFromProtocol(prot_yaw_rot_new);
               }
            } else if (new_yaw_change > yaw_change) {
               --prot_yaw_rot_new;
               new_yaw = EntityTrackerEntryStateHandle.getRotationFromProtocol(prot_yaw_rot_new);
            }

            return new_yaw;
         } else {
            return old_yaw;
         }
      } else {
         return old_yaw;
      }
   }

   public static String stringifyNumberBoxValue(double value) {
      return numberBox_NumberFormat.format(value);
   }

   public static String stringifyAnimationNodeTime(double time) {
      if (time >= 9999.0D) {
         return "9999";
      } else if (time >= 999.95D) {
         return animationodeTime_NumberFormat1000.format(time);
      } else if (time >= 99.995D) {
         return animationodeTime_NumberFormat100.format(time);
      } else if (time >= 9.9995D) {
         return animationodeTime_NumberFormat10.format(time);
      } else {
         return time >= 5.0E-4D ? animationodeTime_NumberFormat1.format(time) : "0.0";
      }
   }

   public static boolean isDiagonal(Vector direction) {
      double sq_x = direction.getX() * direction.getX();
      double sq_z = direction.getZ() * direction.getZ();
      double sq_xz = sq_x + sq_z;
      return sq_xz >= 1.0E-10D && sq_x / sq_xz < SQ_COS_22_5 && sq_z / sq_xz < SQ_COS_22_5;
   }

   public static boolean isConnectedRailsFrom(RailPiece rails, BlockFace direction) {
      if (rails != null && rails.block() != null) {
         RailJunction junction = faceToJunction(rails.type().getJunctions(rails.block()), direction);
         if (junction == null) {
            return false;
         } else {
            RailState state = rails.type().takeJunction(rails.block(), junction);
            if (state == null) {
               return false;
            } else {
               state.setMotionVector(state.motionVector().multiply(-1.0D));
               state.initEnterDirection();
               TrackWalkingPoint wp = new TrackWalkingPoint(state);
               wp.skipFirst();
               if (!wp.moveFull()) {
                  return false;
               } else {
                  return wp.state.railType() == rails.type() && wp.state.railBlock().equals(rails.block());
               }
            }
         }
      } else {
         return false;
      }
   }

   public static boolean isConnectedRails(RailPiece rails, BlockFace direction) {
      if (rails != null && rails.block() != null) {
         RailJunction junction = faceToJunction(rails.type().getJunctions(rails.block()), direction);
         if (junction == null) {
            return false;
         } else {
            RailState state = rails.type().takeJunction(rails.block(), junction);
            if (state == null) {
               return false;
            } else {
               state.initEnterDirection();
               TrackWalkingPoint wp = new TrackWalkingPoint(state);
               wp.skipFirst();
               return wp.moveFull();
            }
         }
      } else {
         return false;
      }
   }

   public static boolean isUpsideDownRailSupport(Block block) {
      BlockData blockdata = WorldUtil.getBlockData(block);
      if (blockdata == BlockData.AIR) {
         return false;
      } else if (blockdata.isSuffocating(block)) {
         return true;
      } else {
         return TCConfig.upsideDownSupportedByAll && blockdata.canSupportOnFace(block, BlockFace.DOWN);
      }
   }

   public static int getDefaultDisplayedBlockOffset() {
      return 6;
   }

   public static Optional<Set<String>> getConfigStringSetOptional(ConfigurationNode config, String key) {
      if (config.contains(key)) {
         List<String> configList = config.getList(key, String.class);
         Set<String> resultSet = new HashSet(configList);
         return Optional.of(Collections.unmodifiableSet(resultSet));
      } else {
         return Optional.empty();
      }
   }

   public static Optional<List<String>> getConfigStringListOptional(ConfigurationNode config, String key) {
      if (config.contains(key)) {
         List<String> configList = config.getList(key, String.class);
         List<String> listCopy = new ArrayList(configList);
         return Optional.of(Collections.unmodifiableList(listCopy));
      } else {
         return Optional.empty();
      }
   }

   public static void setConfigStringCollectionOptional(ConfigurationNode config, String key, Optional<? extends Collection<String>> value) {
      if (value.isPresent()) {
         LogicUtil.synchronizeList(config.getList(key, String.class), (Collection)value.get(), new ItemSynchronizer<String, String>() {
            public boolean isItem(String item, String value) {
               return Objects.equals(item, value);
            }

            public String onAdded(String value) {
               return value;
            }

            public void onRemoved(String item) {
            }
         });
      } else {
         config.remove(key);
      }

   }

   public static <T> Optional<T> getConfigOptional(ConfigurationNode config, String key, Class<T> type) {
      return config.contains(key) ? Optional.ofNullable(config.get(key, type, (Object)null)) : Optional.empty();
   }

   public static void setConfigOptional(ConfigurationNode config, String key, Optional<?> value) {
      if (value.isPresent()) {
         config.set(key, value.get());
      } else if (config.contains(key)) {
         config.remove(key);

         for(YamlPath parentYamlPath = YamlPath.create(key).parent(); parentYamlPath != YamlPath.ROOT; parentYamlPath = parentYamlPath.parent()) {
            String parentPath = parentYamlPath.toString();
            if (!config.isNode(parentPath)) {
               break;
            }

            ConfigurationNode parent = config.getNode(parentPath);
            if (!parent.isEmpty()) {
               break;
            }

            parent.remove();
         }
      }

   }

   public static BlockData getBlockDataOfPhysicsEvent(BlockPhysicsEvent event) {
      return BlockPhysicsEventDataAccessor.INSTANCE.get(event);
   }

   public static Player findPlayer(CommandSender sender, String name) {
      if (name.equals("@p")) {
         BoundingRange.Axis axis = BoundingRange.Axis.forSender(sender);
         if (axis.world == null) {
            sender.sendMessage(ChatColor.RED + "Can only use @p executing as a Player or CommandBlock");
            return null;
         } else {
            Iterator<Player> iter = axis.world.getPlayers().iterator();
            if (!iter.hasNext()) {
               sender.sendMessage(ChatColor.RED + "There is no player nearby");
               return null;
            } else {
               Player result = (Player)iter.next();
               Location tmpLoc = result.getLocation();
               double lowestDistance = axis.distanceSquared(tmpLoc);

               while(iter.hasNext()) {
                  Player p = (Player)iter.next();
                  double distance = axis.distanceSquared(p.getLocation(tmpLoc));
                  if (distance < lowestDistance) {
                     lowestDistance = distance;
                     result = p;
                  }
               }

               return result;
            }
         }
      } else {
         Player p = Bukkit.getPlayer(name);
         if (p == null) {
            sender.sendMessage(ChatColor.RED + "Failed to find player with name " + name + ": not online");
         }

         return p;
      }
   }

   public static boolean hasPaperViewDistanceSupport() {
      try {
         Player.class.getMethod("setViewDistance", Integer.TYPE);
         Player.class.getMethod("setNoTickViewDistance", Integer.TYPE);
         Player.class.getMethod("setSendViewDistance", Integer.TYPE);
         return true;
      } catch (Throwable var1) {
         return false;
      }
   }

   public static boolean hasPaperCustomTrackingRangeSupport() {
      try {
         Entity.class.getMethod("setCustomTrackingRange", Integer.TYPE);
         return true;
      } catch (Throwable var1) {
         return false;
      }
   }

   public static double absMaxAxis(Vector v) {
      return Math.max(Math.max(Math.abs(v.getX()), Math.abs(v.getY())), Math.abs(v.getZ()));
   }

   public static double absMinAxis(Vector v) {
      return Math.min(Math.min(Math.abs(v.getX()), Math.abs(v.getY())), Math.abs(v.getZ()));
   }

   public static Location getRealEyeLocation(Player player) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(player.getVehicle());
      if (member != null) {
         CartAttachmentSeat seat = member.getAttachments().findSeat(player);
         if (seat != null) {
            Location eye = seat.getFirstPersonEyeLocation();
            if (eye != null) {
               return eye;
            }
         }
      }

      return player.getEyeLocation();
   }

   public static PacketPlayOutEntityEquipmentHandle createPlayerEquipmentPacket(int entityId, EquipmentSlot slot, ItemStack itemStack) {
      return PacketPlayOutEntityEquipmentHandle.createNew(OwnerType.PLAYER, entityId, slot, itemStack);
   }

   public static PacketPlayOutEntityEquipmentHandle createNonPlayerEquipmentPacket(int entityId, EquipmentSlot slot, ItemStack itemStack) {
      return PacketPlayOutEntityEquipmentHandle.createNew(OwnerType.NON_PLAYER, entityId, slot, itemStack);
   }

   public static <T> List<T> filterList(List<T> list, Predicate<T> filter) {
      return filterAndMapList(list, filter, (Function)null);
   }

   public static <I, O> List<O> filterAndMapList(List<I> list, Predicate<I> filter, Function<I, O> mapper) {
      return filterAndMultiMapList(list, filter, mapper == null ? null : (i) -> {
         return Collections.singletonList(mapper.apply(i));
      });
   }

   public static <I, O> List<O> filterAndMultiMapList(List<I> list, Predicate<I> filter, Function<I, Collection<O>> mapper) {
      int numItems = list.size();
      if (numItems == 0) {
         return Collections.emptyList();
      } else if (numItems == 1) {
         I first = list.get(0);
         if (!filter.test(first)) {
            return Collections.emptyList();
         } else if (mapper != null) {
            Collection<O> results = (Collection)mapper.apply(first);
            int numResults = results.size();
            if (numResults == 0) {
               return Collections.emptyList();
            } else {
               return numResults == 1 ? Collections.singletonList(results.iterator().next()) : Collections.unmodifiableList(new ArrayList(results));
            }
         } else {
            return Collections.singletonList(first);
         }
      } else if (mapper != null) {
         List<O> result = new ArrayList(numItems);

         for(int i = 0; i < numItems; ++i) {
            I input = list.get(i);
            if (filter.test(input)) {
               result.addAll((Collection)mapper.apply(input));
            }
         }

         return Collections.unmodifiableList(result);
      } else {
         for(int i = 0; i < numItems; ++i) {
            I input = list.get(i);
            if (!filter.test(input)) {
               List<O> result = new ArrayList(numItems - 1);

               int j;
               for(j = 0; j < i; ++j) {
                  result.add(list.get(j));
               }

               for(j = i + 1; j < numItems; ++j) {
                  input = list.get(j);
                  if (filter.test(input)) {
                     result.add(input);
                  }
               }

               return Collections.unmodifiableList(result);
            }
         }

         return Collections.unmodifiableList(list);
      }
   }

   public static byte[] readByteArray(InputStream stream) throws IOException {
      byte[] data = new byte[readVariableLengthInt(stream)];
      if (stream instanceof DataInputStream) {
         ((DataInputStream)stream).readFully(data);
      } else {
         int remaining = data.length;

         int numRead;
         for(int offset = 0; remaining > 0; remaining -= numRead) {
            numRead = stream.read(data, offset, remaining);
            if (numRead <= 0) {
               throw new EOFException();
            }

            offset += numRead;
         }
      }

      return data;
   }

   public static void writeByteArray(OutputStream stream, byte[] array) throws IOException {
      writeVariableLengthInt(stream, array.length);
      stream.write(array);
   }

   public static int readVariableLengthInt(InputStream stream) throws IOException {
      int value = 0;

      int b;
      do {
         b = stream.read();
         if (b == -1) {
            throw new EOFException("Unexpected end of stream");
         }

         value <<= 7;
         value |= b & 127;
      } while((b & 128) != 0);

      return value;
   }

   public static void writeVariableLengthInt(OutputStream stream, int value) throws IOException {
      for(int numExtraBits = (32 - Integer.numberOfLeadingZeros(value)) / 7 * 7; numExtraBits > 0; numExtraBits -= 7) {
         stream.write(128 | value >> numExtraBits & 127);
      }

      stream.write(value & 127);
   }

   private static Util.TeleportPositionMethod findRelativeTeleportMethod() {
      try {
         Class<?> flagsClass = Class.forName("io.papermc.paper.entity.TeleportFlag");
         Class<?> relativeFlagsClass = Class.forName("io.papermc.paper.entity.TeleportFlag$Relative");
         Object[] relativeRotFlags = LogicUtil.createArray(flagsClass, 2);
         relativeRotFlags[0] = relativeFlagsClass.getField("YAW").get((Object)null);
         relativeRotFlags[1] = relativeFlagsClass.getField("PITCH").get((Object)null);
         FastMethod<Boolean> teleportWithFlagsMethod = new FastMethod();
         teleportWithFlagsMethod.init(Entity.class.getMethod("teleport", Location.class, relativeRotFlags.getClass()));
         teleportWithFlagsMethod.forceInitialization();
         return (entity, to) -> {
            return entity instanceof Player ? (Boolean)teleportWithFlagsMethod.invoke(entity, to, relativeRotFlags) : entity.teleport(to);
         };
      } catch (Throwable var4) {
         return Entity::teleport;
      }
   }

   public static boolean teleportPosition(Entity entity, Location to) {
      Location toCorrected;
      if (entity instanceof LivingEntity) {
         toCorrected = ((LivingEntity)entity).getEyeLocation();
      } else {
         toCorrected = entity.getLocation();
      }

      toCorrected.setWorld(to.getWorld());
      toCorrected.setX(to.getX());
      toCorrected.setY(to.getY());
      toCorrected.setZ(to.getZ());
      return TELEPORT_POSITION_METHOD.teleportPosition(entity, toCorrected);
   }

   public static void resetPlayerAwaitingTeleport(Player player) {
      PlayerConnectionHandle connection = PlayerConnectionHandle.forPlayer(player);
      if (connection != null) {
         connection.resetAwaitTeleport();
      }

   }

   public static String unescapeString(String str) {
      int len = str.length();
      if (len != 0 && str.charAt(0) == '"') {
         StringBuilder newStr = new StringBuilder(len - 1);
         boolean escaped = false;

         for(int i = 1; i < len; ++i) {
            char c = str.charAt(i);
            if (escaped) {
               escaped = false;
               newStr.append(c);
            } else if (c == '\\') {
               escaped = true;
            } else {
               if (c == '"') {
                  break;
               }

               newStr.append(c);
            }
         }

         return newStr.toString();
      } else {
         return str;
      }
   }

   /** @deprecated */
   @Deprecated
   public static String escapeQuotedArgument(String text) {
      return QuoteEscapedString.quoteEscape(text).getEscaped();
   }

   public static Color toColor(ChatColor chatColor) {
      return (Color)COLOR_TO_RGB.getOrDefault(chatColor, UNKNOWN_CHAT_COLOR);
   }

   static {
      ISVERTRAIL = new MaterialTypeProperty(new Material[]{Material.LADDER});
      ISTCRAIL = new MaterialTypeProperty(new MaterialTypeProperty[]{ISVERTRAIL, MaterialUtil.ISRAILS, MaterialUtil.ISPRESSUREPLATE});
      blockbuff = new ArrayList();
      numberBox_NumberFormat = createNumberFormat(1, 4);
      animationodeTime_NumberFormat1000 = createNumberFormat(0, 0);
      animationodeTime_NumberFormat100 = createNumberFormat(1, 1);
      animationodeTime_NumberFormat10 = createNumberFormat(1, 2);
      animationodeTime_NumberFormat1 = createNumberFormat(1, 3);
      SQ_COS_22_5 = Math.pow(Math.cos(0.39269908169872414D), 2.0D);
      TELEPORT_POSITION_METHOD = findRelativeTeleportMethod();
      COLOR_TO_RGB = new EnumMap(ChatColor.class);
      UNKNOWN_CHAT_COLOR = Color.fromRGB(252, 252, 252);
      COLOR_TO_RGB.put(ChatColor.BLACK, Color.fromRGB(0, 0, 0));
      COLOR_TO_RGB.put(ChatColor.DARK_BLUE, Color.fromRGB(0, 0, 168));
      COLOR_TO_RGB.put(ChatColor.DARK_GREEN, Color.fromRGB(0, 168, 0));
      COLOR_TO_RGB.put(ChatColor.DARK_AQUA, Color.fromRGB(0, 168, 168));
      COLOR_TO_RGB.put(ChatColor.DARK_RED, Color.fromRGB(168, 0, 0));
      COLOR_TO_RGB.put(ChatColor.DARK_PURPLE, Color.fromRGB(168, 0, 168));
      COLOR_TO_RGB.put(ChatColor.GOLD, Color.fromRGB(252, 168, 0));
      COLOR_TO_RGB.put(ChatColor.GRAY, Color.fromRGB(168, 168, 168));
      COLOR_TO_RGB.put(ChatColor.DARK_GRAY, Color.fromRGB(84, 84, 84));
      COLOR_TO_RGB.put(ChatColor.BLUE, Color.fromRGB(84, 84, 252));
      COLOR_TO_RGB.put(ChatColor.GREEN, Color.fromRGB(84, 252, 84));
      COLOR_TO_RGB.put(ChatColor.AQUA, Color.fromRGB(84, 252, 252));
      COLOR_TO_RGB.put(ChatColor.RED, Color.fromRGB(252, 84, 84));
      COLOR_TO_RGB.put(ChatColor.LIGHT_PURPLE, Color.fromRGB(252, 84, 252));
      COLOR_TO_RGB.put(ChatColor.YELLOW, Color.fromRGB(252, 252, 84));
      COLOR_TO_RGB.put(ChatColor.WHITE, Color.fromRGB(252, 252, 252));
   }

   private interface TeleportPositionMethod {
      boolean teleportPosition(Entity var1, Location var2);
   }
}

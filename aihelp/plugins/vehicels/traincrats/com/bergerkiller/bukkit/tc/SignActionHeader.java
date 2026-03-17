package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.rails.direction.RailEnterDirection;
import com.bergerkiller.bukkit.tc.signactions.SignActionMode;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import java.util.Locale;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class SignActionHeader {
   private boolean is_converted = false;
   private boolean is_empty = false;
   private String rc_name = "";
   private SignRedstoneMode redstoneMode;
   private SignActionMode mode;
   private String directions_str;
   private String modeText;
   private RailPiece rail_enter_dirs_rail;
   private BlockFace rail_enter_dirs_fwd;
   private RailEnterDirection[] rail_enter_dirs;

   public SignActionHeader() {
      this.redstoneMode = SignRedstoneMode.ON;
      this.mode = SignActionMode.NONE;
      this.directions_str = null;
      this.modeText = "";
      this.rail_enter_dirs_rail = null;
      this.rail_enter_dirs_fwd = null;
      this.rail_enter_dirs = null;
   }

   public boolean isValid() {
      return this.mode != SignActionMode.NONE;
   }

   /** @deprecated */
   @Deprecated
   public boolean isLegacyConverted() {
      return this.is_converted;
   }

   public boolean isEmpty() {
      return this.is_empty;
   }

   public boolean isInverted() {
      return this.redstoneMode.isInverted();
   }

   public boolean isAlwaysOn() {
      return this.redstoneMode == SignRedstoneMode.ALWAYS;
   }

   public boolean isAlwaysOff() {
      return this.redstoneMode == SignRedstoneMode.NEVER;
   }

   public boolean onPowerRising() {
      return this.redstoneMode.isRisingPulse();
   }

   public boolean onPowerFalling() {
      return this.redstoneMode.isFallingPulse();
   }

   public SignRedstoneMode getRedstoneMode() {
      return this.redstoneMode;
   }

   public void setRedstoneMode(SignRedstoneMode mode) {
      this.redstoneMode = mode;
   }

   public SignActionMode getMode() {
      return this.mode;
   }

   public String getModeText() {
      return this.modeText;
   }

   public void setMode(SignActionMode mode) {
      this.mode = mode;
   }

   public String getRemoteName() {
      return this.rc_name;
   }

   public void setRemoteName(String name) {
      this.rc_name = name;
   }

   public boolean isMode(SignActionMode mode) {
      return this.mode == mode;
   }

   public boolean isTrain() {
      return this.mode == SignActionMode.TRAIN;
   }

   public boolean isCart() {
      return this.mode == SignActionMode.CART;
   }

   public boolean isRC() {
      return this.mode == SignActionMode.RCTRAIN;
   }

   /** @deprecated */
   @Deprecated
   public boolean hasDirections() {
      return this.directions_str != null;
   }

   /** @deprecated */
   @Deprecated
   public Direction[] getDirections() {
      return this.directions_str == null ? null : Direction.parseAll(this.directions_str);
   }

   public boolean hasEnterDirections() {
      return this.directions_str != null;
   }

   public RailEnterDirection[] getEnterDirections(RailPiece rail, BlockFace forwardDirection) {
      if (this.directions_str == null) {
         return null;
      } else if (this.rail_enter_dirs_rail == rail && this.rail_enter_dirs_fwd == forwardDirection) {
         return this.rail_enter_dirs;
      } else {
         this.rail_enter_dirs_rail = rail;
         this.rail_enter_dirs_fwd = forwardDirection;
         return this.rail_enter_dirs = RailEnterDirection.parseAll(rail, forwardDirection, this.directions_str);
      }
   }

   public void setEnterDirectionsText(String text) {
      this.directions_str = text;
      this.rail_enter_dirs_rail = null;
      this.rail_enter_dirs_fwd = null;
      this.rail_enter_dirs = null;
   }

   public void setEnterDirections(RailEnterDirection[] directions) {
      if (directions == null) {
         this.setEnterDirectionsText((String)null);
      } else if (directions.length == 0) {
         this.setEnterDirectionsText("");
      } else if (directions.length == 1) {
         this.setEnterDirectionsText(directions[0].name());
      } else {
         StringBuilder str = new StringBuilder(directions.length * 2);
         RailEnterDirection[] var3 = directions;
         int var4 = directions.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            RailEnterDirection dir = var3[var5];
            str.append(dir.name());
         }

         this.setEnterDirectionsText(str.toString());
      }

   }

   /** @deprecated */
   @Deprecated
   public void setDirections(Direction[] directions) {
      if (directions == null) {
         this.setEnterDirectionsText((String)null);
      } else if (directions.length == 0) {
         this.setEnterDirectionsText("");
      } else if (directions.length == 1) {
         Direction d = directions[0];
         this.setEnterDirectionsText(this.isValidDirection(d) ? d.aliases()[0] : "");
      } else {
         StringBuilder str = new StringBuilder();
         Direction[] var3 = directions;
         int var4 = directions.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Direction d = var3[var5];
            if (this.isValidDirection(d)) {
               str.append(d.aliases()[0]);
            }
         }

         this.setEnterDirectionsText(str.toString());
      }

   }

   private boolean isValidDirection(Direction direction) {
      return direction != Direction.NONE && direction != Direction.CONTINUE && direction != Direction.REVERSE;
   }

   /** @deprecated */
   @Deprecated
   public BlockFace[] getFaces(BlockFace absoluteDirection) {
      return this.directions_str == null ? FaceUtil.AXIS : RailEnterDirection.toFacesOnly(this.getEnterDirections(RailPiece.NONE, absoluteDirection));
   }

   public SignActionType getRedstoneAction(boolean newPowerState) {
      return this.redstoneMode.getRedstoneAction(newPowerState);
   }

   public boolean isActionFiltered(SignActionType type) {
      if (type == SignActionType.NONE) {
         return false;
      } else if (!this.redstoneMode.isRespondingToRedstone() && (type == SignActionType.REDSTONE_ON || type == SignActionType.REDSTONE_OFF)) {
         return true;
      } else {
         return (this.redstoneMode.isRisingPulse() || this.redstoneMode.isFallingPulse()) && !type.isRedstone();
      }
   }

   public String toString() {
      if (!this.isValid()) {
         return "";
      } else {
         String prefix = "[" + this.redstoneMode.getPattern();
         String postfix = "";
         if (this.directions_str != null) {
            postfix = postfix + ":" + this.directions_str;
         }

         postfix = postfix + "]";
         if (this.mode == SignActionMode.TRAIN) {
            return prefix + "train" + postfix;
         } else if (this.mode == SignActionMode.CART) {
            return prefix + "cart" + postfix;
         } else if (this.mode == SignActionMode.RCTRAIN) {
            postfix = this.rc_name + "]";
            return postfix.length() + prefix.length() >= 10 ? prefix + "t " + postfix : prefix + "train " + postfix;
         } else {
            return prefix + "?" + postfix;
         }
      }
   }

   public static SignActionHeader parseFromEvent(SignActionEvent event) {
      return parse(event.getLine(0));
   }

   public static SignActionHeader parseFromEvent(SignChangeEvent event) {
      return parse(Util.getCleanLine((SignChangeEvent)event, 0));
   }

   public static SignActionHeader parseFromSign(Sign sign) {
      return parse(Util.getCleanLine((Sign)sign, 0));
   }

   public static SignActionHeader parse(String line) {
      SignActionHeader header = new SignActionHeader();
      if (line != null && !line.isEmpty()) {
         boolean validStart = line.charAt(0) == '[';
         boolean validEnd = line.charAt(line.length() - 1) == ']';
         if (TCConfig.allowParenthesesFormat) {
            validStart |= line.charAt(0) == '(';
            validEnd |= line.charAt(line.length() - 1) == ')';
         }

         if (TCConfig.parseOldSigns && !validStart && !validEnd) {
            String s = line.toLowerCase(Locale.ENGLISH);
            if (s.startsWith("!") || s.startsWith("+")) {
               s = s.substring(1);
            }

            if (s.startsWith("train") || s.startsWith("t ") || s.startsWith("cart")) {
               header.is_converted = true;
               line = String.format("[%s]", line);
               validStart = true;
               validEnd = true;
            }
         }

         if (validStart && validEnd) {
            SignRedstoneMode.ParseResult redstoneParseResult = SignRedstoneMode.parse(line, 1);
            header.setRedstoneMode(redstoneParseResult.mode);
            int idx = redstoneParseResult.endIndex;
            String token = line.substring(idx, line.length() - 1).toLowerCase(Locale.ENGLISH);
            String after_token = "";
            header.modeText = token;
            if (token.startsWith("train ") && token.length() > 6) {
               header.mode = SignActionMode.RCTRAIN;
               after_token = line.substring(idx + 6, line.length() - 1);
            } else if (token.startsWith("t ") && token.length() > 2) {
               header.mode = SignActionMode.RCTRAIN;
               after_token = line.substring(idx + 2, line.length() - 1);
            } else if (token.startsWith("train")) {
               header.mode = SignActionMode.TRAIN;
               after_token = line.substring(idx + 5, line.length() - 1);
            } else {
               if (!token.startsWith("cart")) {
                  header.mode = SignActionMode.NONE;
                  return header;
               }

               header.mode = SignActionMode.CART;
               after_token = line.substring(idx + 4, line.length() - 1);
            }

            if (header.mode == SignActionMode.RCTRAIN) {
               header.rc_name = after_token;
            } else if (after_token.startsWith(":")) {
               after_token = after_token.substring(1);
               header.directions_str = after_token;
            }

            return header;
         } else {
            header.mode = SignActionMode.NONE;
            return header;
         }
      } else {
         header.mode = SignActionMode.NONE;
         header.is_empty = true;
         return header;
      }
   }
}

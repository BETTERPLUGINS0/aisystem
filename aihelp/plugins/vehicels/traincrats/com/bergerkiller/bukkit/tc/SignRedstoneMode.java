package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.tc.signactions.SignActionType;

public enum SignRedstoneMode {
   ON("", true, false, false, false),
   OFF("!", true, true, false, false),
   ALWAYS("+", false, false, false, false),
   NEVER("-", false, false, false, false),
   PULSE_ON("/", true, false, true, false),
   PULSE_OFF("\\", true, false, false, true),
   PULSE_ALWAYS("/\\", true, false, true, true),
   INVERTED_PULSE_ON("!/", true, true, true, false),
   INVERTED_PULSE_OFF("!\\", true, true, false, true),
   INVERTED_PULSE_ALWAYS("!/\\", true, true, true, true);

   private final String pattern;
   private final boolean respondsToRedstone;
   private final boolean isInverted;
   private final boolean isRisingPulse;
   private final boolean isFallingPulse;

   private SignRedstoneMode(String pattern, boolean respondsToRedstone, boolean isInverted, boolean isRisingPulse, boolean isFallingPulse) {
      this.pattern = pattern;
      this.respondsToRedstone = respondsToRedstone;
      this.isInverted = isInverted;
      this.isRisingPulse = isRisingPulse;
      this.isFallingPulse = isFallingPulse;
   }

   public String getPattern() {
      return this.pattern;
   }

   public boolean isRespondingToRedstone() {
      return this.respondsToRedstone;
   }

   public boolean isRisingPulse() {
      return this.isRisingPulse;
   }

   public boolean isFallingPulse() {
      return this.isFallingPulse;
   }

   public boolean isInverted() {
      return this.isInverted;
   }

   public SignActionType getRedstoneAction(boolean newPowerState) {
      switch(this) {
      case ALWAYS:
      case NEVER:
         return SignActionType.NONE;
      case PULSE_ALWAYS:
         return SignActionType.REDSTONE_ON;
      case PULSE_ON:
         return newPowerState ? SignActionType.REDSTONE_ON : SignActionType.NONE;
      case PULSE_OFF:
         return newPowerState ? SignActionType.NONE : SignActionType.REDSTONE_ON;
      case INVERTED_PULSE_ALWAYS:
         return SignActionType.REDSTONE_OFF;
      case INVERTED_PULSE_ON:
         return newPowerState ? SignActionType.REDSTONE_OFF : SignActionType.NONE;
      case INVERTED_PULSE_OFF:
         return newPowerState ? SignActionType.NONE : SignActionType.REDSTONE_OFF;
      case OFF:
         return newPowerState ? SignActionType.REDSTONE_OFF : SignActionType.REDSTONE_ON;
      default:
         return newPowerState ? SignActionType.REDSTONE_ON : SignActionType.REDSTONE_OFF;
      }
   }

   public static SignRedstoneMode.ParseResult parse(String input, int startIndex) {
      boolean power_inverted = false;
      boolean power_always_on = false;
      boolean power_always_off = false;
      boolean power_rising = false;
      boolean power_falling = false;
      int len = input.length();

      int idx;
      for(idx = startIndex; idx < len; ++idx) {
         char c = input.charAt(idx);
         if (c == '!') {
            power_inverted = true;
         } else if (c == '+') {
            power_always_on = true;
         } else if (c == '-') {
            power_always_off = true;
         } else if (c == '/') {
            power_rising = true;
         } else {
            if (c != '\\') {
               break;
            }

            power_falling = true;
         }
      }

      if (power_always_on) {
         return new SignRedstoneMode.ParseResult(idx, ALWAYS);
      } else if (power_always_off) {
         return new SignRedstoneMode.ParseResult(idx, NEVER);
      } else if (power_rising && power_falling) {
         return new SignRedstoneMode.ParseResult(idx, PULSE_ALWAYS);
      } else if (power_rising) {
         return new SignRedstoneMode.ParseResult(idx, power_inverted ? INVERTED_PULSE_ON : PULSE_ON);
      } else if (power_falling) {
         return new SignRedstoneMode.ParseResult(idx, power_inverted ? INVERTED_PULSE_OFF : PULSE_OFF);
      } else {
         return power_inverted ? new SignRedstoneMode.ParseResult(idx, OFF) : new SignRedstoneMode.ParseResult(idx, ON);
      }
   }

   // $FF: synthetic method
   private static SignRedstoneMode[] $values() {
      return new SignRedstoneMode[]{ON, OFF, ALWAYS, NEVER, PULSE_ON, PULSE_OFF, PULSE_ALWAYS, INVERTED_PULSE_ON, INVERTED_PULSE_OFF, INVERTED_PULSE_ALWAYS};
   }

   public static class ParseResult {
      public final int endIndex;
      public final SignRedstoneMode mode;

      public ParseResult(int endIndex, SignRedstoneMode mode) {
         this.endIndex = endIndex;
         this.mode = mode;
      }
   }
}

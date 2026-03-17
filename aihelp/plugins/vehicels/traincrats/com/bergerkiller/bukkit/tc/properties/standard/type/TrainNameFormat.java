package com.bergerkiller.bukkit.tc.properties.standard.type;

import com.bergerkiller.bukkit.common.config.yaml.YamlPath;
import com.bergerkiller.bukkit.tc.Localization;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TrainNameFormat {
   public static final TrainNameFormat DEFAULT = new TrainNameFormat("train", "", false);
   private static final Pattern NAME_GUESS_PATTERN = Pattern.compile("^(.*?)\\d+([^\\d]*)$");
   private final String _prefix;
   private final String _postfix;
   private final boolean _optionalNumber;

   private TrainNameFormat(String prefix, String postfix, boolean optionalNumber) {
      this._prefix = prefix;
      this._postfix = postfix;
      this._optionalNumber = optionalNumber;
   }

   public boolean hasOptionalNumber() {
      return this._optionalNumber;
   }

   public String generate(int number) {
      if (this._optionalNumber && number <= 1) {
         return this._prefix;
      } else {
         StringBuilder str = new StringBuilder(this._prefix.length() + this._postfix.length() + 5);
         str.append(this._prefix);
         str.append(number);
         str.append(this._postfix);
         return str.toString();
      }
   }

   public String search(Predicate<String> filter) {
      int number = 1;

      while(true) {
         String name = this.generate(number);
         if (filter.test(name)) {
            return name;
         }

         ++number;
      }
   }

   public boolean matches(String trainName) {
      if (this._optionalNumber && trainName.equals(this._prefix)) {
         return true;
      } else if (trainName.startsWith(this._prefix) && trainName.endsWith(this._postfix)) {
         int end = trainName.length() - this._postfix.length();
         if (end == this._prefix.length()) {
            return false;
         } else {
            for(int i = this._prefix.length(); i < end; ++i) {
               if (!Character.isDigit(trainName.charAt(i))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public TrainNameFormat.VerifyResult verify() {
      if (this._optionalNumber && this._prefix.isEmpty() && this._postfix.isEmpty()) {
         return TrainNameFormat.VerifyResult.ERR_EMPTY;
      } else {
         TrainNameFormat.VerifyResult result = verify(this._prefix);
         if (result != TrainNameFormat.VerifyResult.OK && result != TrainNameFormat.VerifyResult.ERR_EMPTY) {
            return result;
         } else {
            result = verify(this._postfix);
            return result != TrainNameFormat.VerifyResult.OK && result != TrainNameFormat.VerifyResult.ERR_EMPTY ? result : TrainNameFormat.VerifyResult.OK;
         }
      }
   }

   public static TrainNameFormat.VerifyResult verify(String name) {
      if (name.isEmpty()) {
         return TrainNameFormat.VerifyResult.ERR_EMPTY;
      } else {
         YamlPath path = YamlPath.create(name);
         return path.depth() == 1 && !path.isListElement() ? TrainNameFormat.VerifyResult.OK : TrainNameFormat.VerifyResult.ERR_INVALID_CHAR;
      }
   }

   public static TrainNameFormat parse(String format) {
      int lastHashIndex = format.lastIndexOf(35);
      return lastHashIndex == -1 ? new TrainNameFormat(format, "", true) : new TrainNameFormat(format.substring(0, lastHashIndex), format.substring(lastHashIndex + 1), false);
   }

   public static TrainNameFormat guess(String trainName) {
      Matcher matcher = NAME_GUESS_PATTERN.matcher(trainName);
      return matcher.find() ? new TrainNameFormat(matcher.group(1), matcher.group(2), false) : new TrainNameFormat(trainName, "", true);
   }

   public boolean equals(Object o) {
      if (!(o instanceof TrainNameFormat)) {
         return false;
      } else {
         TrainNameFormat other = (TrainNameFormat)o;
         return this._prefix.equals(other._prefix) && this._postfix.equals(other._postfix) && this._optionalNumber == other._optionalNumber;
      }
   }

   public String toString() {
      return this._optionalNumber ? this._prefix + this._postfix : this._prefix + "#" + this._postfix;
   }

   public static enum VerifyResult {
      OK((Localization)null, (Localization)null),
      ERR_EMPTY(Localization.COMMAND_INPUT_NAME_EMPTY, Localization.COMMAND_MODEL_CONFIG_INPUT_NAME_EMPTY),
      ERR_INVALID_CHAR(Localization.COMMAND_INPUT_NAME_INVALID, Localization.COMMAND_MODEL_CONFIG_INPUT_NAME_INVALID);

      private final Localization message;
      private final Localization modelMessage;

      private VerifyResult(Localization message, Localization modelMessage) {
         this.message = message;
         this.modelMessage = modelMessage;
      }

      public Localization getMessage() {
         return this.message;
      }

      public Localization getModelMessage() {
         return this.modelMessage;
      }

      // $FF: synthetic method
      private static TrainNameFormat.VerifyResult[] $values() {
         return new TrainNameFormat.VerifyResult[]{OK, ERR_EMPTY, ERR_INVALID_CHAR};
      }
   }
}

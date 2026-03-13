package com.nisovin.shopkeepers.util.text;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

final class CommonMessageArguments {
   private CommonMessageArguments() {
   }

   static class PrefixedMessageArguments implements MessageArguments {
      private final MessageArguments arguments;
      private final String keyPrefix;

      public PrefixedMessageArguments(MessageArguments arguments, String keyPrefix) {
         Validate.notNull(arguments, (String)"arguments is null");
         Validate.notNull(keyPrefix, (String)"keyPrefix is null");
         this.arguments = arguments;
         this.keyPrefix = keyPrefix;
      }

      @Nullable
      public Object get(String key) {
         if (!key.startsWith(this.keyPrefix)) {
            return null;
         } else {
            String suffixKey = key.substring(this.keyPrefix.length());
            return this.arguments.get(suffixKey);
         }
      }
   }

   static class CombinedMessageArguments implements MessageArguments {
      private final MessageArguments first;
      private final MessageArguments second;

      public CombinedMessageArguments(MessageArguments first, MessageArguments second) {
         Validate.notNull(first, (String)"first is null");
         Validate.notNull(second, (String)"second is null");
         this.first = first;
         this.second = second;
      }

      @Nullable
      public Object get(String key) {
         Object argument = this.first.get(key);
         return argument != null ? argument : this.second.get(key);
      }
   }

   static class MapMessageArguments implements MessageArguments {
      private final Map<? extends String, ?> arguments;

      public MapMessageArguments(Map<? extends String, ?> arguments) {
         Validate.notNull(arguments, (String)"arguments is null");
         this.arguments = arguments;
      }

      @Nullable
      public Object get(String key) {
         return this.arguments.get(key);
      }
   }
}

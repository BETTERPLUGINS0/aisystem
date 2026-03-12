package com.nisovin.shopkeepers.util.text;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface MessageArguments {
   static MessageArguments ofMap(Map<? extends String, ?> arguments) {
      return new CommonMessageArguments.MapMessageArguments(arguments);
   }

   @Nullable
   Object get(String var1);

   default MessageArguments combinedWith(MessageArguments other) {
      return new CommonMessageArguments.CombinedMessageArguments(this, other);
   }

   default MessageArguments prefixed(String keyPrefix) {
      Validate.notNull(keyPrefix, (String)"keyPrefix is null");
      return (MessageArguments)(keyPrefix.isEmpty() ? this : new CommonMessageArguments.PrefixedMessageArguments(this, keyPrefix));
   }
}

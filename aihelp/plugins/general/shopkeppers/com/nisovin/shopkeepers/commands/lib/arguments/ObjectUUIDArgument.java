package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import java.util.UUID;

public abstract class ObjectUUIDArgument extends ObjectIdArgument<UUID> {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 3;

   public ObjectUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ObjectUUIDArgument(String name, ArgumentFilter<? super UUID> filter) {
      this(name, filter, 3);
   }

   public ObjectUUIDArgument(String name, ArgumentFilter<? super UUID> filter, int minimumCompletionInput) {
      super(name, new UUIDArgument(name + ":uuid"), filter, minimumCompletionInput);
   }

   protected String toString(UUID id) {
      return id.toString();
   }
}

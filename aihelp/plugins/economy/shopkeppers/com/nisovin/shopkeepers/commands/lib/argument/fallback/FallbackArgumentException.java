package com.nisovin.shopkeepers.commands.lib.argument.fallback;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FallbackArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = -2141058556443273342L;
   private final ArgumentParseException originalException;

   public FallbackArgumentException(FallbackArgument<?> argument, ArgumentParseException originalException) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), ((ArgumentParseException)Validate.notNull(originalException, (String)"originalException is null")).getMessageText(), originalException.getCause());
      this.originalException = originalException;
   }

   @NonNull
   public FallbackArgument<?> getArgument() {
      return (FallbackArgument)Unsafe.castNonNull(super.getArgument());
   }

   public ArgumentParseException getOriginalException() {
      return this.originalException;
   }

   public ArgumentParseException getRootException() {
      return this.originalException instanceof FallbackArgumentException ? ((FallbackArgumentException)this.originalException).getRootException() : this.originalException;
   }
}

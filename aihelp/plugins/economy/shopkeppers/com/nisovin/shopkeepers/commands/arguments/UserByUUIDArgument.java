package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.UserArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UserByUUIDArgument extends ObjectByIdArgument<UUID, User> {
   public UserByUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public UserByUUIDArgument(String name, ArgumentFilter<? super User> filter) {
      this(name, filter, 3);
   }

   public UserByUUIDArgument(String name, ArgumentFilter<? super User> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
   }

   protected ObjectIdArgument<UUID> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new UserUUIDArgument(name, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return UserByUUIDArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandPlayerArgumentInvalid;
   }

   @Nullable
   protected User getObject(CommandInput input, CommandContextView context, UUID uuid) throws ArgumentParseException {
      return UserArgumentUtils.findUser(uuid);
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return UserUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}

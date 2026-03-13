package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TargetEntityArgument extends CommandArgument<Entity> {
   private final TargetEntityArgument.TargetEntityFilter filter;

   public TargetEntityArgument(String name) {
      this(name, TargetEntityArgument.TargetEntityFilter.ANY);
   }

   public TargetEntityArgument(String name, TargetEntityArgument.TargetEntityFilter filter) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
   }

   public boolean isOptional() {
      return true;
   }

   public Entity parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      Player player = (Player)ObjectUtils.castOrNull(input.getSender(), Player.class);
      if (player == null) {
         throw this.requiresPlayerError();
      } else {
         Entity targetedEntity = EntityUtils.getTargetedEntity(player);
         if (targetedEntity == null) {
            throw new ArgumentParseException(this, this.filter.getNoTargetErrorMsg());
         } else if (!this.filter.test(targetedEntity)) {
            throw new ArgumentParseException(this, this.filter.getInvalidTargetErrorMsg(targetedEntity));
         } else {
            return targetedEntity;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }

   public interface TargetEntityFilter extends Predicate<Entity> {
      TargetEntityArgument.TargetEntityFilter ANY = new TargetEntityArgument.TargetEntityFilter() {
         public boolean test(Entity entity) {
            return true;
         }

         public Text getNoTargetErrorMsg() {
            return Messages.mustTargetEntity;
         }

         public Text getInvalidTargetErrorMsg(Entity entity) {
            return Text.EMPTY;
         }
      };

      Text getNoTargetErrorMsg();

      Text getInvalidTargetErrorMsg(Entity var1);
   }
}

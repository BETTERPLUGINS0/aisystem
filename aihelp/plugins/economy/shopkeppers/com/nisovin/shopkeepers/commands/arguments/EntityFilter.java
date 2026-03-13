package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.arguments.TargetEntityArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopobjects.ShopkeeperMetadata;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EntityFilter {
   public static final ArgumentFilter<Entity> ANY = ArgumentFilter.acceptAny();
   public static final ArgumentFilter<Entity> VILLAGER = new ArgumentFilter<Entity>() {
      public boolean test(CommandInput input, CommandContextView context, @Nullable Entity value) {
         return EntityFilter.isRegularVillager(value);
      }

      public Text getInvalidArgumentErrorMsg(CommandArgument<?> argument, String argumentInput, @Nullable Entity value) {
         Validate.notNull(argumentInput, (String)"argumentInput is null");
         Text text = Messages.commandEntityArgumentNoVillager;
         text.setPlaceholderArguments(argument.getDefaultErrorMsgArgs());
         text.setPlaceholderArguments("argument", argumentInput);
         return text;
      }
   };
   public static final TargetEntityArgument.TargetEntityFilter VILLAGER_TARGET = new TargetEntityArgument.TargetEntityFilter() {
      public boolean test(Entity entity) {
         return EntityFilter.isRegularVillager(entity);
      }

      public Text getNoTargetErrorMsg() {
         return Messages.mustTargetVillager;
      }

      public Text getInvalidTargetErrorMsg(Entity entity) {
         return Messages.targetEntityIsNoVillager;
      }
   };

   public static boolean isRegularVillager(@Nullable Entity entity) {
      if (!(entity instanceof AbstractVillager)) {
         return false;
      } else {
         return !ShopkeeperMetadata.isTagged(entity) && !CitizensUtils.isNPC(entity);
      }
   }

   private EntityFilter() {
   }
}

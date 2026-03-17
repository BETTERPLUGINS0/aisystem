package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.cloud.parsers.QuotedArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.commands.argument.AttachmentsByName;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AttachmentByNameParser<T extends Attachment> implements QuotedArgumentParser<CommandSender, AttachmentsByName<T>>, Strings<CommandSender> {
   private final boolean forTrain;
   private final Predicate<Attachment> filter;
   private final Localization emptyMessage;

   public static <T extends Attachment> AttachmentByNameParser<T> seats(boolean forTrain) {
      return new AttachmentByNameParser(forTrain, (a) -> {
         return a instanceof CartAttachmentSeat;
      }, Localization.COMMAND_INPUT_ATTACHMENTS_NO_SEATS);
   }

   public static <T extends Attachment> AttachmentByNameParser<T> effects(boolean forTrain) {
      return new AttachmentByNameParser(forTrain, (a) -> {
         return a instanceof Attachment.EffectAttachment;
      }, Localization.COMMAND_INPUT_ATTACHMENTS_NO_EFFECTS);
   }

   public AttachmentByNameParser(boolean forTrain, Predicate<Attachment> filter, Localization emptyMessage) {
      this.forTrain = forTrain;
      this.filter = filter;
      this.emptyMessage = emptyMessage;
   }

   public List<T> parse(CommandContext<CommandSender> context, String name) {
      List<T> result = this.lookup(context).get(name, this.filter);
      if (result.isEmpty()) {
         throw new CloudLocalizedException(context, this.emptyMessage, new String[]{name});
      } else {
         return result;
      }
   }

   private List<String> names(MinecartMember<?> member) {
      return member.getAttachments().getRootAttachment().getNameLookup().names(this.filter);
   }

   private AttachmentNameLookup lookup(CommandContext<CommandSender> context) {
      try {
         if (this.forTrain) {
            TrainProperties properties = (TrainProperties)context.inject(TrainProperties.class).get();
            MinecartGroup group = properties.getHolder();
            return group == null ? AttachmentNameLookup.EMPTY : group.getAttachments().getNameLookup();
         } else {
            CartProperties properties = (CartProperties)context.inject(CartProperties.class).get();
            MinecartMember<?> member = properties.getHolder();
            return member == null ? AttachmentNameLookup.EMPTY : member.getAttachments().getNameLookup();
         }
      } catch (RuntimeException var4) {
         return AttachmentNameLookup.EMPTY;
      }
   }

   @NonNull
   public ArgumentParseResult<AttachmentsByName<T>> parseQuotedString(@NonNull CommandContext<CommandSender> commandContext, @NonNull String name) {
      return ArgumentParseResult.success(new AttachmentsByName(name, this, commandContext));
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput input) {
      if (!(commandContext.sender() instanceof Player)) {
         return Collections.emptyList();
      } else {
         CartProperties props = ((TrainCarts)commandContext.inject(TrainCarts.class).get()).getPlayer((Player)commandContext.sender()).getEditedCart();
         MinecartMember member;
         if (props != null && (member = props.getHolder()) != null) {
            AttachmentNameLookup lookup;
            if (this.forTrain) {
               lookup = member.getGroup().getAttachments().getNameLookup();
            } else {
               lookup = member.getAttachments().getNameLookup();
            }

            return lookup.names(this.filter);
         } else {
            return Collections.emptyList();
         }
      }
   }
}

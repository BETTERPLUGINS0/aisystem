package com.bergerkiller.bukkit.tc.commands.selector.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorException;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorHandler;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorHandlerConditionOption;
import com.bergerkiller.bukkit.tc.commands.selector.TCSelectorHandlerRegistry;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayersInTrainSelector implements SelectorHandler {
   private final TCSelectorHandlerRegistry registry;

   public PlayersInTrainSelector(TCSelectorHandlerRegistry registry) {
      this.registry = registry;
   }

   public Collection<String> handle(CommandSender sender, String selector, List<SelectorCondition> conditions) throws SelectorException {
      List<SelectorCondition> seatConditions = new ArrayList(2);
      Iterator var5 = conditions.iterator();

      while(true) {
         SelectorCondition condition;
         do {
            do {
               if (!var5.hasNext()) {
                  Stream<MinecartMember<?>> matchedCarts = this.registry.matchTrains(sender, conditions).stream().map(TrainProperties::getHolder).filter(Objects::nonNull).flatMap(Collection::stream);
                  List playerNames;
                  if (seatConditions.isEmpty()) {
                     playerNames = (List)matchedCarts.flatMap((member) -> {
                        return ((CommonMinecart)member.getEntity()).getPlayerPassengers().stream();
                     }).map(Player::getName).collect(Collectors.toList());
                     if (playerNames.isEmpty()) {
                        throw new SelectorException("No player passengers are inside any of the matched trains");
                     }
                  } else {
                     playerNames = (List)matchedCarts.flatMap((member) -> {
                        AttachmentNameLookup nameLookup = member.getAttachments().getNameLookup();
                        return seatConditions.stream().flatMap((condition) -> {
                           return nameLookup.matchSeatSelector(sender, condition).filter((e) -> {
                              return e instanceof Player;
                           });
                        });
                     }).distinct().map((p) -> {
                        return ((Player)p).getName();
                     }).collect(Collectors.toList());
                     if (playerNames.isEmpty()) {
                        throw new SelectorException("No player passengers are inside any of the matched seats");
                     }
                  }

                  return playerNames;
               }

               condition = (SelectorCondition)var5.next();
            } while(!condition.getKey().equalsIgnoreCase("seat"));
         } while(condition.isBoolean() && !condition.getBoolean());

         seatConditions.add(condition);
      }
   }

   public List<SelectorHandlerConditionOption> options(CommandSender sender, String selector, List<SelectorCondition> conditions) {
      return this.registry.matchOptions(sender, conditions);
   }
}

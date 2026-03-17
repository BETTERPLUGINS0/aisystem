package com.bergerkiller.bukkit.tc.commands.selector;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.commands.selector.type.PlayersInTrainSelector;
import com.bergerkiller.bukkit.tc.commands.selector.type.TrainNameSelector;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.api.IPropertySelectorCondition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TCSelectorHandlerRegistry extends SelectorHandlerRegistry {
   private final Map<String, IPropertySelectorCondition> conditions = new HashMap();
   private final List<SelectorHandlerConditionOption> options = new ArrayList();

   public TCSelectorHandlerRegistry(TrainCarts plugin) {
      super(plugin);
      String[] var2 = new String[]{"world", "x", "y", "z", "dx", "dy", "dz", "distance", "sort", "limit"};
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String s = var2[var4];
         this.options.add(SelectorHandlerConditionOption.optionString(s));
      }

   }

   public void enable() {
      super.enable();
      this.register("ptrain", new PlayersInTrainSelector(this));
      this.register("train", new TrainNameSelector(this));
      IPropertySelectorCondition speedCondition = (sender, properties, condition) -> {
         MinecartGroup group = properties.getHolder();
         double speed = group != null && !group.isEmpty() ? group.head().getRealSpeedLimited() : 0.0D;
         return condition.matchesNumber(speed);
      };
      this.registerCondition("speed", speedCondition);
      this.registerCondition("velocity", speedCondition);
      this.registerCondition("passengers", (sender, properties, condition) -> {
         MinecartGroup group = properties.getHolder();
         int passengers = 0;
         MinecartMember member;
         if (group != null) {
            for(Iterator var5 = group.iterator(); var5.hasNext(); passengers += ((CommonMinecart)member.getEntity()).getPassengers().size()) {
               member = (MinecartMember)var5.next();
            }
         }

         return condition.matchesNumber((long)passengers);
      });
      this.registerCondition("playerpassengers", (sender, properties, condition) -> {
         MinecartGroup group = properties.getHolder();
         if (!condition.isNumber()) {
            return group != null ? condition.matchesAnyText(group.stream().flatMap((m) -> {
               return ((CommonMinecart)m.getEntity()).getPassengers().stream();
            }).filter((e) -> {
               return e instanceof Player;
            }).map((e) -> {
               return ((Player)e).getName();
            })) : condition.matchesAnyText((Collection)Collections.emptyList());
         } else {
            int passengers = 0;
            MinecartMember member;
            if (group != null) {
               for(Iterator var5 = group.iterator(); var5.hasNext(); passengers += ((CommonMinecart)member.getEntity()).getPlayerPassengers().size()) {
                  member = (MinecartMember)var5.next();
               }
            }

            return condition.matchesNumber((long)passengers);
         }
      });
      this.registerCondition("derailed", (sender, properties, condition) -> {
         MinecartGroup group = properties.getHolder();
         if (group == null) {
            return false;
         } else {
            boolean derailed = false;
            Iterator var5 = group.iterator();

            while(var5.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var5.next();
               if (member.isDerailed()) {
                  derailed = true;
                  break;
               }
            }

            return condition.matchesBoolean(derailed);
         }
      });
      this.registerCondition("unloaded", (sender, properties, condition) -> {
         return condition.matchesBoolean(!properties.isLoaded());
      });
      this.registerCondition("seat", (sender, properties, condition) -> {
         MinecartGroup group = properties.getHolder();
         if (group == null) {
            return false;
         } else {
            Stream<Entity> matchingEntities = group.getAttachments().getNameLookup().matchSeatSelector(sender, condition);
            if (condition.isBoolean()) {
               return matchingEntities.findAny().isPresent() == condition.getBoolean();
            } else {
               return matchingEntities.anyMatch((e) -> {
                  return e instanceof Player;
               });
            }
         }
      });
   }

   public void registerCondition(String name, IPropertySelectorCondition condition) {
      if (this.conditions.put(name, condition) != null) {
         this.removeOption(name);
      }

      if (!name.equals("train")) {
         this.options.add(SelectorHandlerConditionOption.optionString(name));
      }

   }

   public void unregisterCondition(String name) {
      if (this.conditions.remove(name) != null) {
         this.removeOption(name);
      }

   }

   private void removeOption(String name) {
      Iterator iter = this.options.iterator();

      while(iter.hasNext()) {
         if (((SelectorHandlerConditionOption)iter.next()).name().equals(name)) {
            iter.remove();
            break;
         }
      }

   }

   public Collection<TrainProperties> matchTrains(CommandSender sender, List<SelectorCondition> conditions) throws SelectorException {
      if (conditions.isEmpty()) {
         throw new SelectorException("No selector conditions were specified");
      } else {
         List<SelectorCondition> conditions = new ArrayList(conditions);
         Stream<TrainProperties> stream = TrainPropertiesStore.getAll().stream();
         TCSelectorLocationFilter locationFilter = new TCSelectorLocationFilter();
         locationFilter.read(sender, conditions);
         if (locationFilter.hasFilters()) {
            Objects.requireNonNull(locationFilter);
            stream = stream.filter(locationFilter::filter);
         }

         TCSelectorSortLimitFilter sortLimitFilter = new TCSelectorSortLimitFilter();
         sortLimitFilter.read(sender, conditions);

         SelectorCondition selectorCondition;
         IPropertySelectorCondition condition;
         for(Iterator var6 = conditions.iterator(); var6.hasNext(); stream = stream.filter((properties) -> {
            return condition.matches(sender, properties, selectorCondition);
         })) {
            selectorCondition = (SelectorCondition)var6.next();
            condition = (IPropertySelectorCondition)this.conditions.get(selectorCondition.getKey());
            if (condition == null) {
               throw new SelectorException("Unknown condition: " + selectorCondition.getKey());
            }
         }

         stream = sortLimitFilter.apply(stream);
         List<TrainProperties> result = (List)stream.collect(Collectors.toList());
         if (result.isEmpty()) {
            throw new SelectorException("No trains matched these conditions");
         } else {
            return result;
         }
      }
   }

   public List<SelectorHandlerConditionOption> matchOptions(CommandSender sender, List<SelectorCondition> conditions) {
      return this.options;
   }
}

package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import java.util.Iterator;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class OnlyOwnersCanEnterProperty extends FieldBackedStandardCartProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("onlyownerscanenter")
   @Command("train onlyownerscanenter <state>")
   @CommandDescription("Sets whether only owners can enter the train")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("state") boolean state) {
      properties.setCanOnlyOwnersEnter(state);
      this.getProperty(sender, properties);
   }

   @Command("train onlyownerscanenter")
   @CommandDescription("Displays whether only owners can enter the train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Only owners can enter the train: " + Localization.boolStr(properties.getCanOnlyOwnersEnter()));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("onlyownerscanenter")
   @Command("cart onlyownerscanenter <state>")
   @CommandDescription("Sets whether only owners can enter the cart")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("state") boolean state) {
      properties.setCanOnlyOwnersEnter(state);
      this.getProperty(sender, properties);
   }

   @Command("cart onlyownerscanenter")
   @CommandDescription("Displays whether only owners can enter the cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Only owners can enter the cart: " + Localization.boolStr(properties.getCanOnlyOwnersEnter()));
   }

   @PropertyParser("onlyownerscanenter")
   public boolean parseCanEnter(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean isListed() {
      return false;
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_ONLYOWNERSCANENTER.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Boolean getData(FieldBackedProperty.CartInternalData data) {
      return data.canOnlyOwnersEnter;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Boolean value) {
      data.canOnlyOwnersEnter = value;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return config.contains("public") ? Optional.of(!(Boolean)config.get("public", true)) : Util.getConfigOptional(config, "onlyOwnersCanEnter", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      config.remove("public");
      Util.setConfigOptional(config, "onlyOwnersCanEnter", value);
   }

   public Boolean get(TrainProperties properties) {
      Iterator var2 = properties.iterator();

      CartProperties cProp;
      do {
         if (!var2.hasNext()) {
            return Boolean.TRUE;
         }

         cProp = (CartProperties)var2.next();
      } while((Boolean)this.get((CartProperties)cProp));

      return Boolean.FALSE;
   }
}

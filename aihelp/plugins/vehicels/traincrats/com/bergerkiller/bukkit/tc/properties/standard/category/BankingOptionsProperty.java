package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.BankingOptions;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class BankingOptionsProperty extends FieldBackedStandardTrainProperty<BankingOptions> {
   @CommandTargetTrain
   @PropertyCheckPermission("banking")
   @Command("train banking <strength> <smoothness>")
   @CommandDescription("Sets a new train banking strength and smoothness")
   private void trainSetBanking(CommandSender sender, TrainProperties properties, @Argument("strength") double strength, @Argument("smoothness") double smoothness) {
      properties.setBanking(strength, smoothness);
      this.trainGetBankingInfo(sender, properties);
   }

   @Command("train banking")
   @CommandDescription("Displays the current train banking settings")
   private void trainGetBankingInfo(CommandSender sender, TrainProperties properties) {
      if (properties.getBankingStrength() == 0.0D) {
         sender.sendMessage(ChatColor.YELLOW + "Train banking " + ChatColor.RED + "is inactive. " + ChatColor.YELLOW + "Change strength to enable.");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train banking " + ChatColor.BLUE + "strength " + ChatColor.WHITE + properties.getBankingStrength() + ChatColor.BLUE + " smoothness " + ChatColor.WHITE + properties.getBankingSmoothness());
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("banking")
   @Command("train banking strength <strength>")
   @CommandDescription("Sets a new train banking strength")
   private void trainSetBankingStrength(CommandSender sender, TrainProperties properties, @Argument("strength") double strength) {
      properties.setBankingStrength(strength);
      this.trainGetBankingStrength(sender, properties);
   }

   @Command("train banking strength")
   @CommandDescription("Displays the currently configured train banking strength")
   private void trainGetBankingStrength(CommandSender sender, TrainProperties properties) {
      if (properties.getBankingStrength() == 0.0D) {
         sender.sendMessage(ChatColor.YELLOW + "Train banking strength: " + ChatColor.WHITE + "0 " + ChatColor.RED + "(Inactive)");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train banking strength: " + ChatColor.WHITE + properties.getBankingStrength());
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("banking")
   @Command("train banking smoothness <strength>")
   @CommandDescription("Sets a new train banking smoothness")
   private void trainSetBankingSmoothness(CommandSender sender, TrainProperties properties, @Argument("strength") double strength) {
      properties.setBankingSmoothness(strength);
      this.trainGetBankingSmoothness(sender, properties);
   }

   @Command("train banking smoothness")
   @CommandDescription("Displays the currently configured train banking smoothness")
   private void trainGetBankingSmoothness(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train banking smoothness: " + ChatColor.WHITE + properties.getBankingSmoothness());
   }

   @PropertyParser("banking")
   public BankingOptions parseBanking(PropertyParseContext<BankingOptions> context) {
      String[] args = context.input().trim().split(" ");
      double newStrength;
      double newSmoothness;
      if (args.length >= 2) {
         newStrength = ParseUtil.parseDouble(args[0], Double.NaN);
         newSmoothness = ParseUtil.parseDouble(args[1], Double.NaN);
      } else {
         newStrength = ParseUtil.parseDouble(context.input(), Double.NaN);
         newSmoothness = ((BankingOptions)context.current()).smoothness();
      }

      if (Double.isNaN(newStrength)) {
         throw new PropertyInvalidInputException("Banking strength is not a number");
      } else if (Double.isNaN(newSmoothness)) {
         throw new PropertyInvalidInputException("Banking smoothness is not a number");
      } else {
         return BankingOptions.create(newStrength, newSmoothness);
      }
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_BANKING.has(sender);
   }

   public BankingOptions getDefault() {
      return BankingOptions.DEFAULT;
   }

   public BankingOptions getData(FieldBackedProperty.TrainInternalData data) {
      return data.bankingOptionsData;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, BankingOptions value) {
      data.bankingOptionsData = value;
   }

   public Optional<BankingOptions> readFromConfig(ConfigurationNode config) {
      if (!config.isNode("banking")) {
         return Optional.empty();
      } else {
         ConfigurationNode banking = config.getNode("banking");
         return Optional.of(BankingOptions.create((Double)banking.get("strength", 0.0D), (Double)banking.get("smoothness", 0.0D)));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<BankingOptions> value) {
      if (value.isPresent()) {
         BankingOptions data = (BankingOptions)value.get();
         ConfigurationNode banking = config.getNode("banking");
         banking.set("strength", data.strength());
         banking.set("smoothness", data.smoothness());
      } else {
         config.remove("banking");
      }

   }
}

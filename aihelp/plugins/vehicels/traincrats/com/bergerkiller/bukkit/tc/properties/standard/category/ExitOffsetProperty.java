package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.type.ExitOffset;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public final class ExitOffsetProperty implements ICartProperty<ExitOffset> {
   @CommandTargetTrain
   @PropertyCheckPermission("exitoffset")
   @Command("train exit offset <dx> <dy> <dz>")
   @CommandDescription("Sets an offset relative to the cart where players exit it")
   private void trainSetOffsetProperty(CommandSender sender, TrainProperties properties, @Argument("dx") double dx, @Argument("dy") double dy, @Argument("dz") double dz) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(dx, dy, dz, old.getYaw(), old.getPitch()));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitoffset")
   @Command("train exit location <posX> <posY> <posZ>")
   @CommandDescription("Sets world coordinates where players are teleported to when exiting")
   private void trainSetLocationProperty(CommandSender sender, TrainProperties properties, @Argument("posX") double posX, @Argument("posY") double posY, @Argument("posZ") double posZ) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.createAbsolute(posX, posY, posZ, old.getYaw(), old.getPitch()));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("train exit rotation <yaw> <pitch>")
   @CommandDescription("Sets the rotation of the player relative to the cart where players exit it")
   private void trainSetRotationProperty(CommandSender sender, TrainProperties properties, @Argument("yaw") float yaw, @Argument("pitch") float pitch) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), yaw, pitch));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitoffset")
   @Command("cart exit location <posX> <posY> <posZ>")
   @CommandDescription("Sets world coordinates where players are teleported to when exiting")
   private void cartSetlocationProperty(CommandSender sender, CartProperties properties, @Argument("posX") double posX, @Argument("posY") double posY, @Argument("posZ") double posZ) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.createAbsolute(posX, posY, posZ, old.getYaw(), old.getPitch()));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitoffset")
   @Command("cart exit offset <dx> <dy> <dz>")
   @CommandDescription("Sets an offset relative to the cart where players exit it")
   private void cartSetOffsetProperty(CommandSender sender, CartProperties properties, @Argument("dx") double dx, @Argument("dy") double dy, @Argument("dz") double dz) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(dx, dy, dz, old.getYaw(), old.getPitch()));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("cart exit rotation <yaw> <pitch>")
   @CommandDescription("Sets the rotation of the player relative to the cart where players exit it")
   private void cartSetRotationProperty(CommandSender sender, CartProperties properties, @Argument("yaw") float yaw, @Argument("pitch") float pitch) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), yaw, pitch));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("cart exit yaw <yaw>")
   @CommandDescription("Sets the yaw rotation relative to the cart exiting players are positioned at")
   private void cartSetRotationYawProperty(CommandSender sender, CartProperties properties, @Argument("yaw") float yaw) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), yaw, old.getPitch()));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("cart exit yaw free")
   @CommandDescription("Sets the yaw orientation of the player after exiting remains as it was before")
   private void cartSetRotationYawFreeProperty(CommandSender sender, CartProperties properties) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), Float.NaN, old.getPitch()));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("cart exit pitch <pitch>")
   @CommandDescription("Sets the pitch rotation relative to the cart exiting players are positioned at")
   private void cartSetRotationPitchProperty(CommandSender sender, CartProperties properties, @Argument("pitch") float pitch) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), old.getYaw(), pitch));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("cart exit pitch free")
   @CommandDescription("Sets the pitch orientation of the player after exiting remains as it was before")
   private void cartSetRotationPitchFreeProperty(CommandSender sender, CartProperties properties) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), old.getYaw(), Float.NaN));
      this.cartGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("train exit yaw <yaw>")
   @CommandDescription("Sets the yaw rotation relative to the cart exiting players are positioned at")
   private void trainSetRotationYawProperty(CommandSender sender, TrainProperties properties, @Argument("yaw") float yaw) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), yaw, old.getPitch()));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("train exit yaw free")
   @CommandDescription("Sets the yaw orientation of the player after exiting remains as it was before")
   private void trainSetRotationYawFreeProperty(CommandSender sender, TrainProperties properties) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), Float.NaN, old.getPitch()));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("train exit pitch <pitch>")
   @CommandDescription("Sets the pitch rotation relative to the cart exiting players are positioned at")
   private void trainSetRotationPitchProperty(CommandSender sender, TrainProperties properties, @Argument("pitch") float pitch) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), old.getYaw(), pitch));
      this.trainGetProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("exitrotation")
   @Command("train exit pitch free")
   @CommandDescription("Sets the pitch orientation of the player after exiting remains as it was before")
   private void trainSetRotationPitchFreeProperty(CommandSender sender, TrainProperties properties) {
      ExitOffset old = (ExitOffset)properties.get(this);
      properties.set(this, ExitOffset.create(old.getPosition(), old.getYaw(), Float.NaN));
      this.trainGetProperty(sender, properties);
   }

   @Command("train exit")
   @CommandDescription("Displays the current exit offset and rotation set for the train")
   private void trainGetProperty(CommandSender sender, TrainProperties properties) {
      this.showProperty(sender, "Train", (ExitOffset)properties.get(this));
   }

   @Command("cart exit")
   @CommandDescription("Displays the current exit offset and rotation set for the cart")
   private void cartGetProperty(CommandSender sender, CartProperties properties) {
      this.showProperty(sender, "Cart", (ExitOffset)properties.get(this));
   }

   private void showProperty(CommandSender sender, String prefix, ExitOffset offset) {
      MessageBuilder builder = new MessageBuilder();
      if (offset.isAbsolute()) {
         builder.yellow(new Object[]{prefix + " exit coordinates are set to:"});
         builder.newLine().yellow(new Object[]{"  Location X: "}).white(new Object[]{offset.getX()});
         builder.newLine().yellow(new Object[]{"  Location Y: "}).white(new Object[]{offset.getY()});
         builder.newLine().yellow(new Object[]{"  Location Z: "}).white(new Object[]{offset.getZ()});
      } else {
         builder.yellow(new Object[]{prefix + " exit offset is set to:"});
         builder.newLine().yellow(new Object[]{"  Relative X: "}).white(new Object[]{offset.getX()});
         builder.newLine().yellow(new Object[]{"  Relative Y: "}).white(new Object[]{offset.getY()});
         builder.newLine().yellow(new Object[]{"  Relative Z: "}).white(new Object[]{offset.getZ()});
      }

      if (offset.hasLockedYaw()) {
         builder.newLine().yellow(new Object[]{"  Yaw: "}).white(new Object[]{offset.getYaw()});
      } else {
         builder.newLine().yellow(new Object[]{"  Yaw: "}).green(new Object[]{"Not set (free)"});
      }

      if (offset.hasLockedPitch()) {
         builder.newLine().yellow(new Object[]{"  Pitch: "}).white(new Object[]{offset.getPitch()});
      } else {
         builder.newLine().yellow(new Object[]{"  Pitch: "}).green(new Object[]{"Not set (free)"});
      }

      builder.send(sender);
   }

   public String getPermissionName() {
      return "exit offset";
   }

   @PropertyParser(
      value = "exitlocation",
      processPerCart = true
   )
   public ExitOffset parseLocation(PropertyParseContext<ExitOffset> context) {
      Vector vec = Util.parseVector(context.input(), (Vector)null);
      if (vec == null) {
         throw new PropertyInvalidInputException("Not a vector");
      } else {
         return ExitOffset.createAbsolute(vec, ((ExitOffset)context.current()).getYaw(), ((ExitOffset)context.current()).getPitch());
      }
   }

   @PropertyParser(
      value = "exitoffset",
      processPerCart = true
   )
   public ExitOffset parseOffset(PropertyParseContext<ExitOffset> context) {
      Vector vec = Util.parseVector(context.input(), (Vector)null);
      if (vec == null) {
         throw new PropertyInvalidInputException("Not a vector");
      } else {
         if (vec.length() > TCConfig.maxEjectDistance) {
            vec.normalize().multiply(TCConfig.maxEjectDistance);
         }

         return ExitOffset.create(vec, ((ExitOffset)context.current()).getYaw(), ((ExitOffset)context.current()).getPitch());
      }
   }

   @PropertyParser(
      value = "exityaw",
      processPerCart = true
   )
   public ExitOffset parseYaw(PropertyParseContext<ExitOffset> context) {
      return ExitOffset.create(((ExitOffset)context.current()).isAbsolute(), ((ExitOffset)context.current()).getPosition(), context.inputFloatOrNaN(), ((ExitOffset)context.current()).getPitch());
   }

   @PropertyParser(
      value = "exitpitch",
      processPerCart = true
   )
   public ExitOffset parsePitch(PropertyParseContext<ExitOffset> context) {
      return ExitOffset.create(((ExitOffset)context.current()).isAbsolute(), ((ExitOffset)context.current()).getPosition(), ((ExitOffset)context.current()).getYaw(), context.inputFloatOrNaN());
   }

   @PropertyParser(
      value = "exitrot|exitrotation",
      processPerCart = true
   )
   public ExitOffset parseRotation(PropertyParseContext<ExitOffset> context) {
      String[] angletext = Util.splitBySeparator(context.input());
      float new_yaw;
      float new_pitch;
      if (angletext.length == 2) {
         new_yaw = ParseUtil.parseFloat(angletext[0], Float.NaN);
         new_pitch = ParseUtil.parseFloat(angletext[1], Float.NaN);
      } else if (angletext.length == 1) {
         new_yaw = ParseUtil.parseFloat(angletext[0], Float.NaN);
         new_pitch = Float.NaN;
      } else {
         new_yaw = Float.NaN;
         new_pitch = Float.NaN;
      }

      return ExitOffset.create(((ExitOffset)context.current()).isAbsolute(), ((ExitOffset)context.current()).getPosition(), new_yaw, new_pitch);
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_EXIT_OFFSET.has(sender);
   }

   public ExitOffset getDefault() {
      return ExitOffset.DEFAULT;
   }

   public Optional<ExitOffset> readFromConfig(ConfigurationNode config) {
      if (!config.contains("exitOffset") && !config.contains("exitYaw") && !config.contains("exitPitch")) {
         return Optional.empty();
      } else {
         Vector absoluteCoords = (Vector)config.getOrDefault("exitLocation", Vector.class, (Object)null);
         Vector offset = absoluteCoords == null ? (Vector)config.getOrDefault("exitOffset", new Vector()) : null;
         float yaw = (Float)config.getOrDefault("exitYaw", Float.NaN);
         float pitch = (Float)config.getOrDefault("exitPitch", Float.NaN);
         if (!(Boolean)config.getOrDefault("exitYawLocked", false)) {
            yaw = Float.NaN;
         }

         if (!(Boolean)config.getOrDefault("exitPitchLocked", false)) {
            pitch = Float.NaN;
         }

         return absoluteCoords != null ? Optional.of(ExitOffset.createAbsolute(absoluteCoords, yaw, pitch)) : Optional.of(ExitOffset.create(offset, yaw, pitch));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<ExitOffset> value) {
      if (value.isPresent()) {
         ExitOffset data = (ExitOffset)value.get();
         if (data.isAbsolute()) {
            config.set("exitLocation", data.getPosition());
            config.remove("exitOffset");
         } else {
            config.set("exitOffset", data.getPosition());
            config.remove("exitLocation");
         }

         if (data.hasLockedYaw()) {
            config.set("exitYawLocked", true);
            config.set("exitYaw", data.getYaw());
         } else {
            config.set("exitYawLocked", false);
            config.set("exitYaw", 0.0F);
         }

         if (data.hasLockedPitch()) {
            config.set("exitPitchLocked", true);
            config.set("exitPitch", data.getPitch());
         } else {
            config.set("exitPitchLocked", false);
            config.set("exitPitch", 0.0F);
         }
      } else {
         config.remove("exitOffset");
         config.remove("exitLocation");
         config.remove("exitYaw");
         config.remove("exitYawLocked");
         config.remove("exitPitch");
         config.remove("exitPitchLocked");
      }

   }
}

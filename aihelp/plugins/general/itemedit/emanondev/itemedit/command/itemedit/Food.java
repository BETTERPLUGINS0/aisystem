package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Keys;
import emanondev.itemedit.ParsedItem;
import emanondev.itemedit.Util;
import emanondev.itemedit.UtilLegacy;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Food extends SubCmd {
   private static final String[] foodSub = VersionUtils.isVersionAfter(1, 21, 2) ? new String[]{"clear", "canalwayseat", "eatticks", "nutrition", "saturation", "addeffect", "removeeffect", "cleareffects", "info", "convertto", "consumeparticles", "animation", "sound"} : (VersionUtils.isVersionAfter(1, 21) ? new String[]{"clear", "canalwayseat", "eatticks", "nutrition", "saturation", "addeffect", "removeeffect", "cleareffects", "info", "convertto"} : new String[]{"clear", "canalwayseat", "eatticks", "nutrition", "saturation", "addeffect", "removeeffect", "cleareffects", "info"});

   public Food(ItemEditCommand cmd) {
      super("food", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         String var6 = args[1].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -1606364903:
            if (var6.equals("canalwayseat")) {
               var7 = 1;
            }
            break;
         case -744934882:
            if (var6.equals("eatticks")) {
               var7 = 2;
            }
            break;
         case -351387883:
            if (var6.equals("removeeffect")) {
               var7 = 6;
            }
            break;
         case -349729938:
            if (var6.equals("convertto")) {
               var7 = 12;
            }
            break;
         case -265651304:
            if (var6.equals("nutrition")) {
               var7 = 3;
            }
            break;
         case -230491182:
            if (var6.equals("saturation")) {
               var7 = 4;
            }
            break;
         case 3237038:
            if (var6.equals("info")) {
               var7 = 11;
            }
            break;
         case 94746189:
            if (var6.equals("clear")) {
               var7 = 0;
            }
            break;
         case 109627663:
            if (var6.equals("sound")) {
               var7 = 10;
            }
            break;
         case 502887794:
            if (var6.equals("addeffect")) {
               var7 = 5;
            }
            break;
         case 772571669:
            if (var6.equals("cleareffects")) {
               var7 = 7;
            }
            break;
         case 1118509956:
            if (var6.equals("animation")) {
               var7 = 9;
            }
            break;
         case 1123033649:
            if (var6.equals("consumeparticles")) {
               var7 = 8;
            }
         }

         switch(var7) {
         case 0:
            this.foodClear(p, item, alias, args);
            return;
         case 1:
            this.foodCanAlwaysEat(p, item, alias, args);
            return;
         case 2:
            this.foodEatTicks(p, item, alias, args);
            return;
         case 3:
            this.foodNutrition(p, item, alias, args);
            return;
         case 4:
            this.foodSaturation(p, item, alias, args);
            return;
         case 5:
            this.foodAddEffect(p, item, alias, args);
            return;
         case 6:
            this.foodRemoveEffect(p, item, alias, args);
            return;
         case 7:
            this.foodClearEffects(p, item, alias, args);
            return;
         case 8:
            if (!VersionUtils.isVersionAfter(1, 21, 2)) {
               this.onFail(p, alias);
               return;
            }

            this.foodConsumeParticles(p, item, alias, args);
            return;
         case 9:
            if (!VersionUtils.isVersionAfter(1, 21, 2)) {
               this.onFail(p, alias);
               return;
            }

            this.foodAnimation(p, item, alias, args);
            return;
         case 10:
            if (!VersionUtils.isVersionAfter(1, 21, 2)) {
               this.onFail(p, alias);
               return;
            }

            this.foodSound(p, item, alias, args);
            return;
         case 11:
            this.foodInfo(p, item, alias, args);
            return;
         case 12:
            if (!VersionUtils.isVersionAfter(1, 21)) {
               this.onFail(p, alias);
               return;
            }

            this.foodConvertTo(p, item, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   private void foodSound(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "sound");
            return;
         }

         Sound value = (Sound)Aliases.SOUND.convertAlias(args[2]);
         ParsedItem parsed = new ParsedItem(item);
         parsed.set(value.getKey(), Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "sound");
         p.getInventory().setItemInMainHand(parsed.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "sound");
      }

   }

   private void foodAnimation(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "animation");
            return;
         }

         String value = (String)Aliases.ANIMATION.convertAlias(args[2]);
         if (value == null) {
            this.onWrongAlias("wrong-animation", p, Aliases.ANIMATION, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "animation");
            return;
         }

         ParsedItem parsed = new ParsedItem(item);
         parsed.set(value, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "animation");
         p.getInventory().setItemInMainHand(parsed.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "animation");
      }

   }

   private void foodConsumeParticles(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "consumeparticles");
            return;
         }

         Boolean value = args.length == 2 ? !this.consumeParticles(item) : (Boolean)Aliases.BOOLEAN.convertAlias(args[2]);
         ParsedItem parsed = new ParsedItem(item);
         parsed.set(value, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "has_consume_particles");
         p.getInventory().setItemInMainHand(parsed.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "consumeparticles");
      }

   }

   public void onFail(@NotNull CommandSender target, @NotNull String alias) {
      Util.sendMessage(target, (new ComponentBuilder(this.getLanguageString("help-header", "", target, new String[0]))).create());
      String[] var3 = foodSub;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String sub = var3[var5];
         Util.sendMessage(target, (new ComponentBuilder(ChatColor.DARK_GREEN + "/" + alias + " " + this.getName() + ChatColor.GREEN + " " + sub + " " + this.getLanguageString(sub + ".params", "", target, new String[0]))).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + alias + " " + this.getName() + " " + sub + " ")).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(String.join("\n", this.getLanguageStringList(sub + ".description", (List)null, target, new String[0])))})).create());
      }

   }

   private void foodClear(Player p, ItemStack item, String alias, String[] args) {
      if (item.hasItemMeta()) {
         ItemMeta meta = ItemUtils.getMeta(item);
         if (meta.hasFood()) {
            ParsedItem parsed = new ParsedItem(item);
            parsed.remove(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
            parsed.remove(Keys.Component.FOOD.toString());
            p.getInventory().setItemInMainHand(parsed.toItemStack());
            this.updateView(p);
         }
      }
   }

   private void foodCanAlwaysEat(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "canalwayseat");
            return;
         }

         Boolean value = args.length == 2 ? !this.canAlwaysEat(item) : (Boolean)Aliases.BOOLEAN.convertAlias(args[2]);
         ParsedItem parsed = new ParsedItem(item);
         parsed.loadEmptyMap(Keys.Component.CONSUMABLE.toString());
         parsed.set(value, Keys.Component.FOOD.toString(), "can_always_eat");
         parsed.load(0, Keys.Component.FOOD.toString(), "nutrition");
         parsed.load(0.0F, Keys.Component.FOOD.toString(), "saturation");
         p.getInventory().setItemInMainHand(parsed.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "canalwayseat");
      }

   }

   private ItemStack setFoodEffects(ItemStack stack, List<Food.FoodPojo> value) {
      ParsedItem parsed = new ParsedItem(stack);
      Map<String, Object> componentMap = value == null ? ParsedItem.getMap(parsed.getMap(), Keys.Component.CROSS_VERSION_CONSUMABLE.toString()) : ParsedItem.loadMap(parsed.getMap(), Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
      if (componentMap == null) {
         return stack;
      } else {
         List effects;
         if (VersionUtils.isVersionUpTo(1, 21, 1)) {
            if (value == null) {
               componentMap.remove("effects");
            }

            effects = parsed.loadEmptyList(Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "effects");
            value.forEach((foodPojo) -> {
               LinkedHashMap<String, Object> potionMap = new LinkedHashMap();
               PotionEffect pot = foodPojo.getPotionEffect();
               potionMap.put("id", pot.getType().getKey());
               potionMap.put("duration", pot.getDuration());
               potionMap.put("amplifier", pot.getAmplifier());
               potionMap.put("ambient", pot.isAmbient());
               potionMap.put("show_particles", pot.hasParticles());
               potionMap.put("show_icon", pot.hasIcon());
               LinkedHashMap<String, Object> effectMap = new LinkedHashMap();
               effectMap.put("effect", potionMap);
               effectMap.put("probability", foodPojo.getProbability());
               effects.add(effectMap);
            });
            parsed.set(effects, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "effects");
            return parsed.toItemStack();
         } else {
            effects = parsed.readList(Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "on_consume_effects");
            List<Map<String, Object>> consumeEffects = effects == null ? new ArrayList() : effects;
            ((List)consumeEffects).removeIf((map) -> {
               return Objects.equals(ParsedItem.readNamespacedKey(map, "type"), Keys.EffectType.APPLY_EFFECTS);
            });
            value.forEach((food) -> {
               LinkedHashMap<String, Object> map = new LinkedHashMap();
               map.put("type", Keys.EffectType.APPLY_EFFECTS);
               map.put("probability", food.getProbability());
               PotionEffect pot = food.getPotionEffect();
               LinkedHashMap<String, Object> subMap = new LinkedHashMap();
               subMap.put("id", pot.getType().getKey());
               subMap.put("duration", pot.getDuration());
               subMap.put("amplifier", pot.getAmplifier());
               subMap.put("ambient", pot.isAmbient());
               subMap.put("show_particles", pot.hasParticles());
               subMap.put("show_icon", pot.hasIcon());
               ArrayList<Map<String, Object>> list = new ArrayList();
               list.add(subMap);
               map.put("effects", list);
               consumeEffects.add(map);
            });
            parsed.set((List)consumeEffects, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "on_consume_effects");
            return parsed.toItemStack();
         }
      }
   }

   private List<Food.FoodPojo> getFoodEffects(ItemStack stack) {
      ParsedItem parsed = new ParsedItem(stack);
      Map<String, Object> componentMap = ParsedItem.getMap(parsed.getMap(), Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
      if (componentMap == null) {
         return new ArrayList();
      } else {
         List consumeEffects;
         ArrayList list;
         if (VersionUtils.isVersionUpTo(1, 21, 1)) {
            consumeEffects = ParsedItem.getListOfMap(componentMap, "effects");
            if (consumeEffects == null) {
               return new ArrayList();
            } else {
               list = new ArrayList();
               consumeEffects.forEach((map) -> {
                  Map<String, Object> subMap = ParsedItem.getMap(map, "effect");
                  list.add(new Food.FoodPojo(new PotionEffect((PotionEffectType)Registry.EFFECT.get(ParsedItem.readNamespacedKey(subMap, "id")), ParsedItem.readInt(subMap, "duration", 30), ParsedItem.readInt(subMap, "amplifier", 0), ParsedItem.readBoolean(subMap, "ambient", false), ParsedItem.readBoolean(subMap, "show_particles", true), ParsedItem.readBoolean(subMap, "show_icon", true)), ParsedItem.readFloat(map, "probability", 1.0F)));
               });
               return list;
            }
         } else {
            consumeEffects = ParsedItem.getListOfMap(componentMap, "on_consume_effects");
            if (consumeEffects != null && !consumeEffects.isEmpty()) {
               list = new ArrayList();
               consumeEffects.forEach((map) -> {
                  if (Objects.equals(ParsedItem.readNamespacedKey(map, "type"), Keys.EffectType.APPLY_EFFECTS)) {
                     float probability = ParsedItem.readFloat(map, "probability", 1.0F);
                     ParsedItem.loadListOfMap(map, "effects").forEach((effect) -> {
                        list.add(new Food.FoodPojo(new PotionEffect((PotionEffectType)Registry.EFFECT.get(ParsedItem.readNamespacedKey(effect, "id")), ParsedItem.readInt(effect, "duration", 30), ParsedItem.readInt(effect, "amplifier", 0), ParsedItem.readBoolean(effect, "ambient", false), ParsedItem.readBoolean(effect, "show_particles", true), ParsedItem.readBoolean(effect, "show_icon", true)), probability));
                     });
                  }
               });
               return list;
            } else {
               return new ArrayList();
            }
         }
      }
   }

   private boolean canAlwaysEat(ItemStack stack) {
      ParsedItem parsed = new ParsedItem(stack);
      return parsed.readBoolean(false, Keys.Component.FOOD.toString(), "can_always_eat");
   }

   private void foodEatTicks(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "eatticks");
            return;
         }

         int val = Integer.parseInt(args[2]);
         if (val < 0) {
            val = 0;
         }

         ParsedItem parsedItem = new ParsedItem(item);
         parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
         parsedItem.set((float)val / 20.0F, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "consume_seconds");
         p.getInventory().setItemInMainHand(parsedItem.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "eatticks");
      }

   }

   private float getEastSeconds(ItemStack stack) {
      ParsedItem parsed = new ParsedItem(stack);
      return parsed.readFloat(1.6F, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "consume_seconds");
   }

   private void foodNutrition(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "nutrition");
            return;
         }

         int val = Integer.parseInt(args[2]);
         ParsedItem parsedItem = new ParsedItem(item);
         parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
         parsedItem.set(val, Keys.Component.FOOD.toString(), "nutrition");
         parsedItem.load(0.0F, Keys.Component.FOOD.toString(), "saturation");
         p.getInventory().setItemInMainHand(parsedItem.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "nutrition");
      }

   }

   private void foodSaturation(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "saturation");
            return;
         }

         float val = Float.parseFloat(args[2]);
         ParsedItem parsedItem = new ParsedItem(item);
         parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
         parsedItem.set(0, Keys.Component.FOOD.toString(), "nutrition");
         parsedItem.load(val, Keys.Component.FOOD.toString(), "saturation");
         p.getInventory().setItemInMainHand(parsedItem.toItemStack());
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "saturation");
      }

   }

   private void foodAddEffect(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 4 || args.length > 9) {
            this.sendFailFeedbackForSub(p, alias, "addeffect");
            return;
         }

         PotionEffectType effect = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         if (effect == null) {
            this.onWrongAlias("wrong-effect", p, Aliases.POTION_EFFECT, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "addeffect");
            return;
         }

         int duration = UtilLegacy.readPotionEffectDurationSecondsToTicks(args[3]);
         int level = 0;
         if (args.length >= 5) {
            level = Integer.parseInt(args[4]) - 1;
            if (level < 0 || level > 127) {
               throw new IllegalArgumentException();
            }
         }

         boolean particles = true;
         if (args.length >= 6) {
            particles = (Boolean)Aliases.BOOLEAN.convertAlias(args[5]);
         }

         boolean ambient = false;
         if (args.length >= 7) {
            ambient = (Boolean)Aliases.BOOLEAN.convertAlias(args[6]);
         }

         boolean icon = true;
         if (args.length >= 8) {
            icon = (Boolean)Aliases.BOOLEAN.convertAlias(args[7]);
         }

         float chance = 1.0F;
         if (args.length == 9) {
            chance = Float.parseFloat(args[8]) / 100.0F;
            if (chance <= 0.0F || chance > 1.0F) {
               throw new IllegalArgumentException();
            }
         }

         if (!p.hasPermission(this.getPermission() + ".bypass_limits")) {
            level = Math.min(level, 1);
         }

         List<Food.FoodPojo> values = this.getFoodEffects(item);
         values.add(new Food.FoodPojo(new PotionEffect(effect, duration, level, ambient, particles, icon), chance));
         p.getInventory().setItemInMainHand(this.setFoodEffects(item, values));
         this.updateView(p);
      } catch (Exception var13) {
         var13.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "addeffect");
      }

   }

   private void foodRemoveEffect(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3 && args.length != 4) {
            this.sendFailFeedbackForSub(p, alias, "removeeffect");
            return;
         }

         PotionEffectType type = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         if (type == null) {
            this.onWrongAlias("wrong-effect", p, Aliases.POTION_EFFECT, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "removeeffect");
            return;
         }

         Integer level;
         if (args.length == 4) {
            level = Integer.parseInt(args[3]) - 1;
            if (level < 0 || level > 127) {
               throw new IllegalArgumentException();
            }
         } else {
            level = null;
         }

         List<Food.FoodPojo> values = this.getFoodEffects(item);
         values.removeIf((effect) -> {
            return effect.getPotionEffect().getType().equals(type) && (level == null || effect.getPotionEffect().getAmplifier() == level);
         });
         p.getInventory().setItemInMainHand(this.setFoodEffects(item, values));
         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "removeeffect");
      }

   }

   private void foodClearEffects(Player p, ItemStack item, String alias, String[] args) {
      try {
         p.getInventory().setItemInMainHand(this.setFoodEffects(item, (List)null));
         this.updateView(p);
      } catch (Exception var6) {
         this.sendFailFeedbackForSub(p, alias, "cleareffects");
      }

   }

   private void foodInfo(Player p, ItemStack item, String alias, String[] args) {
      try {
         ParsedItem parsedItem = new ParsedItem(item);
         if (!item.hasItemMeta() || !parsedItem.getMap().containsKey(Keys.Component.CROSS_VERSION_CONSUMABLE.toString()) && !parsedItem.getMap().containsKey("use_remainer")) {
            this.sendLanguageString("info.not_food", "", p, new String[0]);
            return;
         }

         ItemStack remainer = VersionUtils.isVersionAfter(1, 21) ? this.getUseRemainder(item) : null;
         ArrayList<String> list = new ArrayList(this.getLanguageStringList("info.message", Collections.emptyList(), p, new String[]{"%eatseconds%", UtilsString.formatNumber((double)this.getEastSeconds(item), 2, false), "%eatticks%", UtilsString.formatNumber((double)(this.getEastSeconds(item) * 20.0F), 0, false), "%saturation%", UtilsString.formatNumber((double)this.getSaturation(item), 2, false), "%nutrition%", String.valueOf(this.getNutrition(item)), "%canalwayseat%", Aliases.BOOLEAN.getDefaultName(this.canAlwaysEat(item)), "%convertto%", VersionUtils.isVersionAfter(1, 21) ? (remainer == null ? Aliases.BOOLEAN.getDefaultName(false) : remainer.getType().name() + (remainer.hasItemMeta() ? " [...]" : "") + (remainer.getAmount() == 1 ? "" : " x" + remainer.getAmount())) : "&c1.21+", "%consumeparticles%", Aliases.BOOLEAN.getDefaultName(this.consumeParticles(item)), "%animation%", Aliases.ANIMATION.getDefaultName(this.animation(item)), "%sound%", Aliases.SOUND.getDefaultName(this.sound(item))}));
         int appliedEffects = this.getFoodEffects(item).size();
         if (appliedEffects > 0) {
            list.addAll(this.getLanguageStringList("info.apply_effect_prefix", Collections.emptyList(), p, new String[]{"%effects%", String.valueOf(this.getFoodEffects(item).size())}));
            int index = 1;

            for(Iterator var10 = this.getFoodEffects(item).iterator(); var10.hasNext(); ++index) {
               Food.FoodPojo foodEffect = (Food.FoodPojo)var10.next();
               PotionEffect effect = foodEffect.getPotionEffect();
               list.addAll(this.getLanguageStringList("info.apply_effect", Collections.emptyList(), p, new String[]{"%index%", String.valueOf(index), "%type%", Aliases.POTION_EFFECT.getDefaultName(effect.getType()), "%level%", String.valueOf(effect.getAmplifier() + 1), "%duration_s%", effect.getDuration() == -1 ? "∞" : UtilsString.formatNumber((double)effect.getDuration() / 20.0D, 2, true), "%hasparticle%", Aliases.BOOLEAN.getDefaultName(effect.hasParticles()), "%isambient%", Aliases.BOOLEAN.getDefaultName(effect.isAmbient()), "%hasicon%", Aliases.BOOLEAN.getDefaultName(!VersionUtils.isVersionAfter(1, 13) || effect.hasIcon()), "%duration_ticks%", effect.getDuration() == -1 ? "∞" : String.valueOf(effect.getDuration()), "%chance_perc%", UtilsString.formatNumber((double)(foodEffect.getProbability() * 100.0F), 2, true)}));
            }
         }

         Util.sendMessage(p, (String)String.join("\n", list));
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   private String animation(ItemStack item) {
      ParsedItem parsedItem = new ParsedItem(item);
      return parsedItem.readString("eat", Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "animation");
   }

   private Sound sound(ItemStack item) {
      ParsedItem parsedItem = new ParsedItem(item);
      String val = parsedItem.readString((String)null, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "sound");
      if (val == null) {
         return Sound.ENTITY_GENERIC_EAT;
      } else {
         try {
            Sound value = (Sound)Registry.SOUNDS.get(new NamespacedKey(val.split(":")[0], val.split(":")[1]));
            return value == null ? Sound.ENTITY_GENERIC_EAT : value;
         } catch (Exception var5) {
            return Sound.ENTITY_GENERIC_EAT;
         }
      }
   }

   private boolean consumeParticles(ItemStack item) {
      ParsedItem parsedItem = new ParsedItem(item);
      return parsedItem.readBoolean(true, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "has_consume_particles");
   }

   private float getSaturation(ItemStack item) {
      ParsedItem parsedItem = new ParsedItem(item);
      Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
      return food != null && food.containsKey("saturation") ? ParsedItem.readFloat(food, "saturation", 0.0F) : 0.0F;
   }

   private int getNutrition(ItemStack item) {
      ParsedItem parsedItem = new ParsedItem(item);
      Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
      return food != null && food.containsKey("nutrition") ? ParsedItem.readInt(food, "nutrition", 0) : 0;
   }

   private void foodConvertTo(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length > 4) {
            this.sendFailFeedbackForSub(p, alias, "convertto");
            return;
         }

         ItemStack target = null;
         if (args.length >= 3) {
            String name = args[2];
            target = ItemEdit.get().getServerStorage().getItem(name);
            if (target == null) {
               try {
                  Material mat = Material.valueOf(name.toUpperCase(Locale.ENGLISH));
                  if (!ItemUtils.isItem(mat)) {
                     throw new IllegalArgumentException();
                  }

                  target = new ItemStack(mat);
               } catch (IllegalArgumentException var8) {
                  this.sendFailFeedbackForSub(p, alias, "convertto");
                  return;
               }
            }
         }

         int amount = 1;
         if (args.length == 4) {
            amount = Integer.parseInt(args[3]);
         }

         if (target.getType().isAir()) {
            target = null;
         }

         if (target != null) {
            target.setAmount(amount);
         }

         this.setUseRemainder(item, target);
         this.updateView(p);
      } catch (Exception var9) {
         this.sendFailFeedbackForSub(p, alias, "convertto");
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return Collections.emptyList();
      } else {
         String var4;
         byte var5;
         switch(args.length) {
         case 2:
            List<String> list = CompleteUtility.complete(args[1], foodSub);
            if (!VersionUtils.isVersionAfter(1, 21)) {
               list.remove("convertto");
            }

            return list;
         case 3:
            var4 = args[1].toLowerCase(Locale.ENGLISH);
            var5 = -1;
            switch(var4.hashCode()) {
            case -1606364903:
               if (var4.equals("canalwayseat")) {
                  var5 = 0;
               }
               break;
            case -744934882:
               if (var4.equals("eatticks")) {
                  var5 = 4;
               }
               break;
            case -351387883:
               if (var4.equals("removeeffect")) {
                  var5 = 8;
               }
               break;
            case -349729938:
               if (var4.equals("convertto")) {
                  var5 = 9;
               }
               break;
            case -265651304:
               if (var4.equals("nutrition")) {
                  var5 = 6;
               }
               break;
            case -230491182:
               if (var4.equals("saturation")) {
                  var5 = 5;
               }
               break;
            case 109627663:
               if (var4.equals("sound")) {
                  var5 = 3;
               }
               break;
            case 502887794:
               if (var4.equals("addeffect")) {
                  var5 = 7;
               }
               break;
            case 1118509956:
               if (var4.equals("animation")) {
                  var5 = 2;
               }
               break;
            case 1123033649:
               if (var4.equals("consumeparticles")) {
                  var5 = 1;
               }
            }

            switch(var5) {
            case 0:
               return CompleteUtility.complete(args[2], (IAliasSet)Aliases.BOOLEAN);
            case 1:
               if (VersionUtils.isVersionAfter(1, 21, 2)) {
                  return CompleteUtility.complete(args[2], (IAliasSet)Aliases.BOOLEAN);
               }

               return Collections.emptyList();
            case 2:
               if (VersionUtils.isVersionAfter(1, 21, 2)) {
                  return CompleteUtility.complete(args[2], (IAliasSet)Aliases.ANIMATION);
               }

               return Collections.emptyList();
            case 3:
               if (VersionUtils.isVersionAfter(1, 21, 2)) {
                  return CompleteUtility.complete(args[2], (IAliasSet)Aliases.SOUND);
               }

               return Collections.emptyList();
            case 4:
               return CompleteUtility.complete(args[2], "1", "20", "40");
            case 5:
               return CompleteUtility.complete(args[2], "0", "1", "1.5", "2", "10");
            case 6:
               return CompleteUtility.complete(args[2], "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
            case 7:
               return CompleteUtility.complete(args[2], (IAliasSet)Aliases.POTION_EFFECT);
            case 8:
               ItemStack item = this.getItemInHand((Player)sender);
               if (item != null && item.hasItemMeta()) {
                  HashSet<PotionEffectType> types = new HashSet();
                  Iterator var8 = this.getFoodEffects(item).iterator();

                  while(var8.hasNext()) {
                     Food.FoodPojo food = (Food.FoodPojo)var8.next();
                     types.add(food.getPotionEffect().getType());
                  }

                  List<String> list2 = new ArrayList();
                  Iterator var13 = types.iterator();

                  while(var13.hasNext()) {
                     PotionEffectType type = (PotionEffectType)var13.next();
                     list2.add(Aliases.POTION_EFFECT.getDefaultName(type));
                  }

                  return CompleteUtility.complete(args[2], (Collection)list2);
               }

               return Collections.emptyList();
            case 9:
               if (VersionUtils.isVersionAfter(1, 21)) {
                  List<String> list2 = CompleteUtility.complete(args[2], Material.class);
                  list2.addAll(CompleteUtility.complete(args[2], (Collection)ItemEdit.get().getServerStorage().getIds()));
                  return list2;
               }
            default:
               return Collections.emptyList();
            }
         case 4:
            var4 = args[1].toLowerCase(Locale.ENGLISH);
            var5 = -1;
            switch(var4.hashCode()) {
            case -351387883:
               if (var4.equals("removeeffect")) {
                  var5 = 1;
               }
               break;
            case -349729938:
               if (var4.equals("convertto")) {
                  var5 = 2;
               }
               break;
            case 502887794:
               if (var4.equals("addeffect")) {
                  var5 = 0;
               }
            }

            switch(var5) {
            case 0:
               return CompleteUtility.complete(args[3], "infinite", "instant", "1", "90", "180", "480");
            case 1:
               return CompleteUtility.complete(args[3], "1", "2", "3", "4", "5");
            case 2:
               if (VersionUtils.isVersionAfter(1, 21)) {
                  return CompleteUtility.complete(args[3], "1", "10", "64");
               }
            default:
               return Collections.emptyList();
            }
         case 5:
            if (args[1].toLowerCase(Locale.ENGLISH).equals("addeffect")) {
               return CompleteUtility.complete(args[4], "1", "2", "3", "4", "5");
            }

            return Collections.emptyList();
         case 6:
         case 7:
         case 8:
            if (args[1].toLowerCase(Locale.ENGLISH).equals("addeffect")) {
               return CompleteUtility.complete(args[args.length - 1], (IAliasSet)Aliases.BOOLEAN);
            }

            return Collections.emptyList();
         case 9:
            if (args[1].toLowerCase(Locale.ENGLISH).equals("addeffect")) {
               return CompleteUtility.complete(args[8], "0.01", "10.0", "50.0", "100.0");
            }

            return Collections.emptyList();
         default:
            return Collections.emptyList();
         }
      }
   }

   private void setUseRemainder(ItemStack origin, ItemStack d) {
      ItemMeta meta = ItemUtils.getMeta(origin);
      if (VersionUtils.isVersionUpTo(1, 21, 1)) {
         FoodComponent food = meta.getFood();

         try {
            food.getClass().getMethod("setUsingConvertsTo", ItemStack.class).invoke(food, d);
         } catch (Exception var6) {
            throw new RuntimeException(var6);
         }

         meta.setFood(food);
         origin.setItemMeta(meta);
      } else {
         meta.setUseRemainder(d);
         origin.setItemMeta(meta);
      }
   }

   @Nullable
   private ItemStack getUseRemainder(ItemStack origin) {
      if (VersionUtils.isVersionUpTo(1, 21, 1)) {
         FoodComponent food = ItemUtils.getMeta(origin).getFood();

         try {
            return (ItemStack)food.getClass().getMethod("getUsingConvertsTo").invoke(food);
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      } else {
         return ItemUtils.getMeta(origin).getUseRemainder();
      }
   }

   private static class FoodPojo {
      private PotionEffect potionEffect;
      private float probability;

      public FoodPojo(PotionEffect potionEffect, float probability) {
         this.potionEffect = potionEffect;
         this.probability = probability;
      }

      public PotionEffect getPotionEffect() {
         return this.potionEffect;
      }

      public void setPotionEffect(PotionEffect potionEffect) {
         this.potionEffect = potionEffect;
      }

      public float getProbability() {
         return this.probability;
      }

      public void setProbability(float probability) {
         this.probability = probability;
      }
   }
}

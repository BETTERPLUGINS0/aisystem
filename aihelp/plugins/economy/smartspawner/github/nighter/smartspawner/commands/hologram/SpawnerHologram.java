package github.nighter.smartspawner.commands.hologram;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.ColorUtil;
import github.nighter.smartspawner.language.LanguageManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class SpawnerHologram {
   private final SmartSpawner plugin = SmartSpawner.getInstance();
   private final LanguageManager languageManager;
   private final AtomicReference<TextDisplay> textDisplay = new AtomicReference((Object)null);
   private final Location spawnerLocation;
   private int stackSize;
   private EntityType entityType;
   private int currentExp;
   private int maxExp;
   private int currentItems;
   private int maxSlots;
   private static final String HOLOGRAM_IDENTIFIER = "SmartSpawner-Holo";
   private final String uniqueIdentifier;
   private static final Vector3f SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
   private static final Vector3f TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);
   private static final AxisAngle4f ROTATION = new AxisAngle4f(0.0F, 0.0F, 0.0F, 0.0F);

   public SpawnerHologram(Location location) {
      this.spawnerLocation = location;
      this.languageManager = this.plugin.getLanguageManager();
      this.uniqueIdentifier = this.generateUniqueIdentifier(location);
   }

   private String generateUniqueIdentifier(Location location) {
      String var10000 = location.getWorld().getName();
      return "SmartSpawner-Holo-" + var10000 + "-" + location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
   }

   public void createHologram() {
      if (this.spawnerLocation != null && this.spawnerLocation.getWorld() != null) {
         this.cleanupExistingHologram();
         double offsetX = this.plugin.getConfig().getDouble("hologram.offset_x", 0.5D);
         double offsetY = this.plugin.getConfig().getDouble("hologram.offset_y", 0.5D);
         double offsetZ = this.plugin.getConfig().getDouble("hologram.offset_z", 0.5D);
         Location holoLoc = this.spawnerLocation.clone().add(offsetX, offsetY, offsetZ);
         Scheduler.runLocationTask(holoLoc, () -> {
            try {
               TextDisplay display = (TextDisplay)this.spawnerLocation.getWorld().spawn(holoLoc, TextDisplay.class, (td) -> {
                  td.setBillboard(Billboard.CENTER);
                  String alignmentStr = this.plugin.getConfig().getString("hologram.alignment", "CENTER");

                  TextAlignment alignment;
                  try {
                     alignment = TextAlignment.valueOf(alignmentStr.toUpperCase());
                  } catch (IllegalArgumentException var5) {
                     alignment = TextAlignment.CENTER;
                     this.plugin.getLogger().warning("Invalid hologram alignment in config: " + alignmentStr + ". Using CENTER as default.");
                  }

                  td.setAlignment(alignment);
                  td.setViewRange(16.0F);
                  td.setShadowed(this.plugin.getConfig().getBoolean("hologram.shadowed_text", true));
                  td.setDefaultBackground(false);
                  td.setTransformation(new Transformation(TRANSLATION, ROTATION, SCALE, ROTATION));
                  td.setSeeThrough(this.plugin.getConfig().getBoolean("hologram.see_through", false));
                  boolean transparentBg = this.plugin.getConfig().getBoolean("hologram.transparent_background", false);
                  if (transparentBg) {
                     td.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                  }

                  td.setCustomName(this.uniqueIdentifier);
                  td.setCustomNameVisible(false);
                  td.setPersistent(false);
               });
               this.textDisplay.set(display);
               this.updateText();
            } catch (Exception var3) {
               this.plugin.getLogger().severe("Error creating hologram: " + var3.getMessage());
               var3.printStackTrace();
            }

         });
      }
   }

   public void updateText() {
      TextDisplay display = (TextDisplay)this.textDisplay.get();
      if (display != null && this.entityType != null) {
         String entityTypeName = this.languageManager.getFormattedMobName(this.entityType);
         Map<String, String> replacements = new HashMap();
         replacements.put("entity", entityTypeName);
         replacements.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityTypeName));
         replacements.put("stack_size", String.valueOf(this.stackSize));
         replacements.put("current_exp", this.languageManager.formatNumber((double)this.currentExp));
         replacements.put("max_exp", this.languageManager.formatNumber((double)this.maxExp));
         replacements.put("used_slots", this.languageManager.formatNumber((double)this.currentItems));
         replacements.put("max_slots", this.languageManager.formatNumber((double)this.maxSlots));
         double percentStorageDecimal = this.maxSlots > 0 ? (double)this.currentItems / (double)this.maxSlots * 100.0D : 0.0D;
         String formattedPercentStorage = String.format("%.1f", percentStorageDecimal);
         int percentStorageRounded = (int)Math.round(percentStorageDecimal);
         replacements.put("percent_storage_decimal", formattedPercentStorage);
         replacements.put("percent_storage_rounded", String.valueOf(percentStorageRounded));
         double percentExpDecimal = this.maxExp > 0 ? (double)this.currentExp / (double)this.maxExp * 100.0D : 0.0D;
         String formattedPercentExp = String.format("%.1f", percentExpDecimal);
         int percentExpRounded = (int)Math.round(percentExpDecimal);
         replacements.put("percent_exp_decimal", formattedPercentExp);
         replacements.put("percent_exp_rounded", String.valueOf(percentExpRounded));
         String hologramText = this.languageManager.getHologramText();

         Entry entry;
         for(Iterator var13 = replacements.entrySet().iterator(); var13.hasNext(); hologramText = hologramText.replace("{" + (String)entry.getKey() + "}", (CharSequence)entry.getValue())) {
            entry = (Entry)var13.next();
         }

         String finalText = ColorUtil.translateHexColorCodes(hologramText);
         Scheduler.runEntityTask(display, () -> {
            if (display.isValid()) {
               display.setText(finalText);
            }

         });
      }
   }

   public void updateData(int stackSize, EntityType entityType, int currentExp, int maxExp, int currentItems, int maxSlots) {
      this.stackSize = stackSize;
      this.entityType = entityType;
      this.currentExp = currentExp;
      this.maxExp = maxExp;
      this.currentItems = currentItems;
      this.maxSlots = maxSlots;
      TextDisplay display = (TextDisplay)this.textDisplay.get();
      if (display == null) {
         this.createHologram();
      } else {
         Scheduler.runEntityTask(display, () -> {
            if (!display.isValid()) {
               this.textDisplay.set((Object)null);
               this.createHologram();
            } else {
               this.updateText();
            }

         });
      }

   }

   public void remove() {
      TextDisplay display = (TextDisplay)this.textDisplay.get();
      if (display != null) {
         Scheduler.runEntityTask(display, () -> {
            if (display.isValid()) {
               display.remove();
            }

         });
         this.textDisplay.set((Object)null);
      }

      this.cleanupExistingHologram();
   }

   public void cleanupExistingHologram() {
      if (this.spawnerLocation != null && this.spawnerLocation.getWorld() != null) {
         TextDisplay display = (TextDisplay)this.textDisplay.get();
         if (display != null) {
            Scheduler.runEntityTask(display, () -> {
               if (display.isValid()) {
                  display.remove();
               }

            });
            this.textDisplay.set((Object)null);
         }

         Scheduler.runLocationTask(this.spawnerLocation, () -> {
            double searchRadius = 2.0D;
            this.spawnerLocation.getWorld().getNearbyEntities(this.spawnerLocation, searchRadius, searchRadius, searchRadius).stream().filter((entity) -> {
               return entity instanceof TextDisplay && entity.getCustomName() != null;
            }).filter((entity) -> {
               return entity.getCustomName().equals(this.uniqueIdentifier);
            }).forEach((entity) -> {
               Objects.requireNonNull(entity);
               Scheduler.runEntityTask(entity, entity::remove);
            });
         });
      }
   }
}

package github.nighter.smartspawner.logging.discord;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.logging.SpawnerEventType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.bukkit.configuration.ConfigurationSection;

public class DiscordWebhookConfig {
   private final SmartSpawner plugin;
   private boolean enabled;
   private String webhookUrl;
   private boolean showPlayerHead;
   private String embedTitle;
   private String embedDescription;
   private String embedFooter;
   private Map<String, Integer> eventColors;
   private List<DiscordWebhookConfig.EmbedField> customFields;
   private Set<SpawnerEventType> enabledEvents;
   private boolean logAllEvents;

   public DiscordWebhookConfig(SmartSpawner plugin) {
      this.plugin = plugin;
      this.loadConfig();
   }

   public void loadConfig() {
      ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("logging.discord");
      if (section == null) {
         this.enabled = false;
      } else {
         this.enabled = section.getBoolean("enabled", false);
         this.webhookUrl = section.getString("webhook_url", "");
         this.showPlayerHead = section.getBoolean("show_player_head", true);
         this.embedTitle = section.getString("embed.title", "{description}");
         this.embedDescription = section.getString("embed.description", "{description}");
         this.embedFooter = section.getString("embed.footer", "SmartSpawner • {time}");
         this.logAllEvents = section.getBoolean("log_all_events", false);
         this.eventColors = new HashMap();
         ConfigurationSection colorsSection = section.getConfigurationSection("embed.colors");
         if (colorsSection != null) {
            Iterator var3 = colorsSection.getKeys(false).iterator();

            while(var3.hasNext()) {
               String key = (String)var3.next();
               String colorHex = colorsSection.getString(key, "#5865F2");
               this.eventColors.put(key.toUpperCase(), this.parseColor(colorHex));
            }
         }

         this.customFields = new ArrayList();
         List<Map<?, ?>> fieldsList = section.getMapList("embed.fields");
         Iterator var10 = fieldsList.iterator();

         while(var10.hasNext()) {
            Map<?, ?> fieldMap = (Map)var10.next();
            String name = (String)fieldMap.get("name");
            String value = (String)fieldMap.get("value");
            boolean inline = fieldMap.containsKey("inline") ? (Boolean)fieldMap.get("inline") : false;
            if (name != null && value != null) {
               this.customFields.add(new DiscordWebhookConfig.EmbedField(name, value, inline));
            }
         }

         this.enabledEvents = this.parseEnabledEvents(section);
      }
   }

   private Set<SpawnerEventType> parseEnabledEvents(ConfigurationSection section) {
      if (this.logAllEvents) {
         return EnumSet.allOf(SpawnerEventType.class);
      } else {
         List<String> eventList = section.getStringList("logged_events");
         EnumSet events;
         if (eventList.isEmpty()) {
            events = EnumSet.noneOf(SpawnerEventType.class);
            events.add(SpawnerEventType.SPAWNER_PLACE);
            events.add(SpawnerEventType.SPAWNER_BREAK);
            events.add(SpawnerEventType.SPAWNER_EXPLODE);
            events.add(SpawnerEventType.SPAWNER_STACK_HAND);
            events.add(SpawnerEventType.SPAWNER_STACK_GUI);
            events.add(SpawnerEventType.SPAWNER_DESTACK_GUI);
            events.add(SpawnerEventType.COMMAND_EXECUTE_PLAYER);
            events.add(SpawnerEventType.COMMAND_EXECUTE_CONSOLE);
            events.add(SpawnerEventType.COMMAND_EXECUTE_RCON);
            return events;
         } else {
            events = EnumSet.noneOf(SpawnerEventType.class);
            Iterator var4 = eventList.iterator();

            while(var4.hasNext()) {
               String eventName = (String)var4.next();

               try {
                  events.add(SpawnerEventType.valueOf(eventName.trim().toUpperCase()));
               } catch (IllegalArgumentException var7) {
               }
            }

            return events;
         }
      }
   }

   private int parseColor(String colorHex) {
      try {
         if (colorHex.startsWith("#")) {
            colorHex = colorHex.substring(1);
         }

         return Integer.parseInt(colorHex, 16);
      } catch (NumberFormatException var3) {
         return 5793266;
      }
   }

   public boolean isEventEnabled(SpawnerEventType eventType) {
      return this.enabled && this.enabledEvents.contains(eventType);
   }

   public int getColorForEvent(SpawnerEventType eventType) {
      Integer color = (Integer)this.eventColors.get(eventType.name());
      return color != null ? color : (Integer)this.eventColors.getOrDefault("DEFAULT", 5793266);
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public String getWebhookUrl() {
      return this.webhookUrl;
   }

   @Generated
   public boolean isShowPlayerHead() {
      return this.showPlayerHead;
   }

   @Generated
   public String getEmbedTitle() {
      return this.embedTitle;
   }

   @Generated
   public String getEmbedDescription() {
      return this.embedDescription;
   }

   @Generated
   public String getEmbedFooter() {
      return this.embedFooter;
   }

   @Generated
   public Map<String, Integer> getEventColors() {
      return this.eventColors;
   }

   @Generated
   public List<DiscordWebhookConfig.EmbedField> getCustomFields() {
      return this.customFields;
   }

   @Generated
   public Set<SpawnerEventType> getEnabledEvents() {
      return this.enabledEvents;
   }

   @Generated
   public boolean isLogAllEvents() {
      return this.logAllEvents;
   }

   public static class EmbedField {
      private final String name;
      private final String value;
      private final boolean inline;

      public EmbedField(String name, String value, boolean inline) {
         this.name = name;
         this.value = value;
         this.inline = inline;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public String getValue() {
         return this.value;
      }

      @Generated
      public boolean isInline() {
         return this.inline;
      }
   }
}

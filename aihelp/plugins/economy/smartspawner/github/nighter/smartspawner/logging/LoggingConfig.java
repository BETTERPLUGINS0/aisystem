package github.nighter.smartspawner.logging;

import github.nighter.smartspawner.SmartSpawner;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public class LoggingConfig {
   private final SmartSpawner plugin;
   private boolean enabled;
   private boolean jsonFormat;
   private boolean consoleOutput;
   private Set<SpawnerEventType> enabledEvents;
   private String logDirectory;
   private int maxLogFiles;
   private long maxLogSizeMB;
   private boolean logAllEvents;
   private List<String> loggedEvents;

   public LoggingConfig(SmartSpawner plugin) {
      this.plugin = plugin;
      this.loadConfig();
   }

   public void loadConfig() {
      this.enabled = this.plugin.getConfig().getBoolean("enabled", true);
      this.jsonFormat = this.plugin.getConfig().getBoolean("json_format", false);
      this.consoleOutput = this.plugin.getConfig().getBoolean("console_output", false);
      this.logDirectory = this.plugin.getConfig().getString("log_directory", "logs");
      this.maxLogFiles = this.plugin.getConfig().getInt("max_log_files", 10);
      this.maxLogSizeMB = this.plugin.getConfig().getLong("max_log_size_mb", 10L);
      this.logAllEvents = this.plugin.getConfig().getBoolean("log_all_events", false);
      this.loggedEvents = this.plugin.getConfig().getStringList("logged_events");
      this.enabledEvents = this.parseEnabledEvents();
   }

   private Set<SpawnerEventType> parseEnabledEvents() {
      Set<SpawnerEventType> events = EnumSet.noneOf(SpawnerEventType.class);
      if (this.logAllEvents) {
         return EnumSet.allOf(SpawnerEventType.class);
      } else if (this.loggedEvents != null && !this.loggedEvents.isEmpty()) {
         Iterator var2 = this.loggedEvents.iterator();

         while(var2.hasNext()) {
            String eventName = (String)var2.next();

            try {
               events.add(SpawnerEventType.valueOf(eventName.trim().toUpperCase()));
            } catch (IllegalArgumentException var5) {
            }
         }

         return events;
      } else {
         events.add(SpawnerEventType.SPAWNER_PLACE);
         events.add(SpawnerEventType.SPAWNER_BREAK);
         events.add(SpawnerEventType.SPAWNER_EXPLODE);
         events.add(SpawnerEventType.SPAWNER_STACK_HAND);
         events.add(SpawnerEventType.SPAWNER_STACK_GUI);
         events.add(SpawnerEventType.SPAWNER_DESTACK_GUI);
         events.add(SpawnerEventType.SPAWNER_EXP_CLAIM);
         events.add(SpawnerEventType.SPAWNER_SELL_ALL);
         events.add(SpawnerEventType.SPAWNER_ITEM_TAKE_ALL);
         events.add(SpawnerEventType.SPAWNER_ITEMS_SORT);
         events.add(SpawnerEventType.SPAWNER_ITEM_FILTER);
         events.add(SpawnerEventType.SPAWNER_DROP_PAGE_ITEMS);
         events.add(SpawnerEventType.COMMAND_EXECUTE_PLAYER);
         events.add(SpawnerEventType.COMMAND_EXECUTE_CONSOLE);
         events.add(SpawnerEventType.COMMAND_EXECUTE_RCON);
         return events;
      }
   }

   public Set<SpawnerEventType> getEnabledEvents() {
      return new HashSet(this.enabledEvents);
   }

   public boolean isEventEnabled(SpawnerEventType eventType) {
      return this.enabled && this.enabledEvents.contains(eventType);
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public boolean isJsonFormat() {
      return this.jsonFormat;
   }

   @Generated
   public boolean isConsoleOutput() {
      return this.consoleOutput;
   }

   @Generated
   public String getLogDirectory() {
      return this.logDirectory;
   }

   @Generated
   public int getMaxLogFiles() {
      return this.maxLogFiles;
   }

   @Generated
   public long getMaxLogSizeMB() {
      return this.maxLogSizeMB;
   }

   @Generated
   public boolean isLogAllEvents() {
      return this.logAllEvents;
   }

   @Generated
   public List<String> getLoggedEvents() {
      return this.loggedEvents;
   }
}

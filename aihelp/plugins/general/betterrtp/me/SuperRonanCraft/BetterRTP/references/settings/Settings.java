package me.SuperRonanCraft.BetterRTP.references.settings;

import me.SuperRonanCraft.BetterRTP.references.file.FileOther;

public class Settings {
   private boolean debug;
   private boolean delayEnabled;
   private int delayTime;
   private boolean rtpOnFirstJoin_Enabled;
   private String rtpOnFirstJoin_World;
   private boolean rtpOnFirstJoin_SetAsRespawn;
   private boolean statusMessages;
   private int preloadRadius;
   private final SoftDepends depends = new SoftDepends();
   private boolean protocolLibSounds;
   private boolean locationEnabled;
   private boolean useLocationIfAvailable;
   private boolean locationNeedPermission;
   private boolean useLocationsInSameWorld;
   private boolean permissionGroupEnabled;
   private boolean queueEnabled;
   private String placeholder_true;
   private String placeholder_nopermission;
   private String placeholder_cooldown;
   private String placeholder_balance;
   private String placeholder_hunger;
   private String placeholder_timeDays;
   private String placeholder_timeHours;
   private String placeholder_timeMinutes;
   private String placeholder_timeSeconds;
   private String placeholder_timeZero;
   private String placeholder_timeInf;
   private String placeholder_timeSeparator_middle;
   private String placeholder_timeSeparator_last;

   public void load() {
      FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
      this.debug = config.getBoolean("Settings.Debugger");
      this.delayEnabled = config.getBoolean("Settings.Delay.Enabled");
      this.delayTime = config.getInt("Settings.Delay.Time");
      this.rtpOnFirstJoin_Enabled = config.getBoolean("Settings.RtpOnFirstJoin.Enabled");
      this.rtpOnFirstJoin_World = config.getString("Settings.RtpOnFirstJoin.World");
      this.rtpOnFirstJoin_SetAsRespawn = config.getBoolean("Settings.RtpOnFirstJoin.SetAsRespawn");
      this.preloadRadius = config.getInt("Settings.PreloadRadius");
      this.statusMessages = config.getBoolean("Settings.StatusMessages");
      this.permissionGroupEnabled = config.getBoolean("PermissionGroup.Enabled");
      this.queueEnabled = config.getBoolean("Settings.Queue.Enabled");
      this.protocolLibSounds = FileOther.FILETYPE.EFFECTS.getBoolean("Sounds.ProtocolLibSound");
      this.locationEnabled = FileOther.FILETYPE.LOCATIONS.getBoolean("Enabled");
      this.useLocationIfAvailable = FileOther.FILETYPE.LOCATIONS.getBoolean("UseLocationIfAvailable");
      this.locationNeedPermission = FileOther.FILETYPE.LOCATIONS.getBoolean("RequirePermission");
      this.useLocationsInSameWorld = FileOther.FILETYPE.LOCATIONS.getBoolean("UseLocationsInSameWorld");
      this.placeholder_true = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Success");
      this.placeholder_nopermission = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.NoPermission");
      this.placeholder_cooldown = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Cooldown");
      this.placeholder_balance = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Price");
      this.placeholder_hunger = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.CanRTP.Hunger");
      this.placeholder_timeDays = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Days");
      this.placeholder_timeHours = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Hours");
      this.placeholder_timeMinutes = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Minutes");
      this.placeholder_timeSeconds = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Seconds");
      this.placeholder_timeZero = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.ZeroAll");
      this.placeholder_timeInf = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Infinite");
      this.placeholder_timeSeparator_middle = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Middle");
      this.placeholder_timeSeparator_last = FileOther.FILETYPE.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Last");
      this.depends.load();
   }

   public SoftDepends getsDepends() {
      return this.depends;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public boolean isDelayEnabled() {
      return this.delayEnabled;
   }

   public int getDelayTime() {
      return this.delayTime;
   }

   public boolean isRtpOnFirstJoin_Enabled() {
      return this.rtpOnFirstJoin_Enabled;
   }

   public String getRtpOnFirstJoin_World() {
      return this.rtpOnFirstJoin_World;
   }

   public boolean isRtpOnFirstJoin_SetAsRespawn() {
      return this.rtpOnFirstJoin_SetAsRespawn;
   }

   public boolean isStatusMessages() {
      return this.statusMessages;
   }

   public int getPreloadRadius() {
      return this.preloadRadius;
   }

   public boolean isProtocolLibSounds() {
      return this.protocolLibSounds;
   }

   public boolean isLocationEnabled() {
      return this.locationEnabled;
   }

   public boolean isUseLocationIfAvailable() {
      return this.useLocationIfAvailable;
   }

   public boolean isLocationNeedPermission() {
      return this.locationNeedPermission;
   }

   public boolean isUseLocationsInSameWorld() {
      return this.useLocationsInSameWorld;
   }

   public boolean isPermissionGroupEnabled() {
      return this.permissionGroupEnabled;
   }

   public boolean isQueueEnabled() {
      return this.queueEnabled;
   }

   public String getPlaceholder_true() {
      return this.placeholder_true;
   }

   public String getPlaceholder_nopermission() {
      return this.placeholder_nopermission;
   }

   public String getPlaceholder_cooldown() {
      return this.placeholder_cooldown;
   }

   public String getPlaceholder_balance() {
      return this.placeholder_balance;
   }

   public String getPlaceholder_hunger() {
      return this.placeholder_hunger;
   }

   public String getPlaceholder_timeDays() {
      return this.placeholder_timeDays;
   }

   public String getPlaceholder_timeHours() {
      return this.placeholder_timeHours;
   }

   public String getPlaceholder_timeMinutes() {
      return this.placeholder_timeMinutes;
   }

   public String getPlaceholder_timeSeconds() {
      return this.placeholder_timeSeconds;
   }

   public String getPlaceholder_timeZero() {
      return this.placeholder_timeZero;
   }

   public String getPlaceholder_timeInf() {
      return this.placeholder_timeInf;
   }

   public String getPlaceholder_timeSeparator_middle() {
      return this.placeholder_timeSeparator_middle;
   }

   public String getPlaceholder_timeSeparator_last() {
      return this.placeholder_timeSeparator_last;
   }
}

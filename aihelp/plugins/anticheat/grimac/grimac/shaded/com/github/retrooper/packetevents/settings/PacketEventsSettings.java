package ac.grim.grimac.shaded.com.github.retrooper.packetevents.settings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.TimeStampMode;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.InputStream;
import java.util.function.Function;

public class PacketEventsSettings {
   private TimeStampMode timestampMode;
   private boolean defaultReencode;
   private boolean checkForUpdates;
   private boolean downsampleColors;
   private boolean debugEnabled;
   private boolean fullStackTraceEnabled;
   private boolean kickOnPacketExceptionEnabled;
   private boolean kickIfTerminated;
   private boolean preViaInjection;
   private Function<String, InputStream> resourceProvider;

   public PacketEventsSettings() {
      this.timestampMode = TimeStampMode.MILLIS;
      this.defaultReencode = true;
      this.checkForUpdates = true;
      this.downsampleColors = false;
      this.debugEnabled = false;
      this.fullStackTraceEnabled = false;
      this.kickOnPacketExceptionEnabled = true;
      this.kickIfTerminated = true;
      this.preViaInjection = false;
      this.resourceProvider = (path) -> {
         return PacketEventsSettings.class.getClassLoader().getResourceAsStream(path);
      };
   }

   @ApiStatus.Internal
   public PacketEventsSettings timeStampMode(TimeStampMode timeStampMode) {
      this.timestampMode = timeStampMode;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings reEncodeByDefault(boolean reEncodeByDefault) {
      this.defaultReencode = reEncodeByDefault;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
      this.checkForUpdates = checkForUpdates;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings downsampleColors(boolean downsampleColors) {
      this.downsampleColors = downsampleColors;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public PacketEventsSettings bStats(boolean bStatsEnabled) {
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings debug(boolean debugEnabled) {
      this.debugEnabled = debugEnabled;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings fullStackTrace(boolean fullStackTraceEnabled) {
      this.fullStackTraceEnabled = fullStackTraceEnabled;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings kickOnPacketException(boolean kickOnPacketExceptionEnabled) {
      this.kickOnPacketExceptionEnabled = kickOnPacketExceptionEnabled;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings kickIfTerminated(boolean kickIfTerminated) {
      this.kickIfTerminated = kickIfTerminated;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings preViaInjection(boolean preViaInjection) {
      this.preViaInjection = preViaInjection;
      return this;
   }

   @ApiStatus.Internal
   public PacketEventsSettings customResourceProvider(Function<String, InputStream> resourceProvider) {
      this.resourceProvider = resourceProvider;
      return this;
   }

   public boolean reEncodeByDefault() {
      return this.defaultReencode;
   }

   public boolean shouldCheckForUpdates() {
      return this.checkForUpdates;
   }

   public boolean shouldDownsampleColors() {
      return this.downsampleColors;
   }

   /** @deprecated */
   @Deprecated
   public boolean isbStatsEnabled() {
      return true;
   }

   public boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   public boolean isFullStackTraceEnabled() {
      return this.fullStackTraceEnabled;
   }

   public boolean isKickOnPacketExceptionEnabled() {
      return this.kickOnPacketExceptionEnabled;
   }

   public boolean isKickIfTerminated() {
      return this.kickIfTerminated;
   }

   public boolean isPreViaInjection() {
      return this.preViaInjection;
   }

   public Function<String, InputStream> getResourceProvider() {
      return this.resourceProvider;
   }

   public TimeStampMode getTimeStampMode() {
      return this.timestampMode;
   }
}

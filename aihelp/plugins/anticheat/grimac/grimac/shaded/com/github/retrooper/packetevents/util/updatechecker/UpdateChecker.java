package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.updatechecker;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.ColorUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ApiStatus.Internal
public class UpdateChecker {
   @ApiStatus.Internal
   public String checkLatestReleasedVersion() {
      try {
         URLConnection connection = (new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest")).openConnection();
         connection.addRequestProperty("User-Agent", "Mozilla/4.0");
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String jsonResponse = reader.readLine();
         reader.close();
         JsonObject jsonObject = (JsonObject)AdventureSerializer.serializer().gson().serializer().fromJson(jsonResponse, JsonObject.class);
         return jsonObject.get("name").getAsString();
      } catch (IOException var5) {
         throw new IllegalStateException("Failed to parse packetevents version!", var5);
      }
   }

   @ApiStatus.Internal
   public UpdateChecker.UpdateCheckerStatus checkForUpdate(@Nullable Consumer<PEVersion> latestVersionHolder) {
      PEVersion localVersion = PacketEvents.getAPI().getVersion();

      PEVersion newVersion;
      try {
         newVersion = PEVersion.fromString(this.checkLatestReleasedVersion());
         if (latestVersionHolder != null) {
            latestVersionHolder.accept(newVersion);
         }
      } catch (Exception var5) {
         PacketEvents.getAPI().getLogManager().warn("Failed to check for updates. " + (var5.getCause() != null ? var5.getCause().getClass().getName() + ": " + var5.getCause().getMessage() : var5.getMessage()));
         return UpdateChecker.UpdateCheckerStatus.FAILED;
      }

      if (localVersion.isOlderThan(newVersion)) {
         PacketEvents.getAPI().getLogManager().warn("There is an update available for PacketEvents! Your build: (" + ColorUtil.toString(NamedTextColor.YELLOW) + localVersion + ColorUtil.toString(NamedTextColor.WHITE) + ") | Latest release: (" + ColorUtil.toString(NamedTextColor.GREEN) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.OUTDATED;
      } else if (localVersion.isNewerThan(newVersion)) {
         PacketEvents.getAPI().getLogManager().info("You are running a development build of PacketEvents. Your build: (" + ColorUtil.toString(NamedTextColor.AQUA) + localVersion + ColorUtil.toString(NamedTextColor.WHITE) + ") | Latest release: (" + ColorUtil.toString(NamedTextColor.DARK_AQUA) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.PRE_RELEASE;
      } else if (localVersion.equals(newVersion)) {
         PacketEvents.getAPI().getLogManager().info("You are running the latest release of PacketEvents. Your build: (" + ColorUtil.toString(NamedTextColor.GREEN) + newVersion + ColorUtil.toString(NamedTextColor.WHITE) + ")");
         return UpdateChecker.UpdateCheckerStatus.UP_TO_DATE;
      } else {
         PacketEvents.getAPI().getLogManager().warn("Failed to check for updates. Your build: (" + localVersion + ")");
         return UpdateChecker.UpdateCheckerStatus.FAILED;
      }
   }

   @ApiStatus.Internal
   public UpdateChecker.UpdateCheckerStatus checkForUpdate() {
      return this.checkForUpdate((Consumer)null);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.Internal
   public void handleUpdateCheck(@Nullable Runnable updateCheckCallback) {
      Thread thread = new Thread(() -> {
         PacketEvents.getAPI().getLogManager().info("Checking for updates, please wait...");
         UpdateChecker.UpdateCheckerStatus status = this.checkForUpdate();
         if (updateCheckCallback != null) {
            updateCheckCallback.run();
         }

      }, "packetevents-update-check-thread");
      thread.start();
   }

   @ApiStatus.Internal
   public void handleUpdateCheck(@Nullable BiConsumer<PEVersion, UpdateChecker.UpdateCheckerStatus> updateResultHolder) {
      Thread thread = new Thread(() -> {
         PacketEvents.getAPI().getLogManager().info("Checking for updates, please wait...");
         AtomicReference<PEVersion> latestVersion = new AtomicReference();
         Objects.requireNonNull(latestVersion);
         Consumer<PEVersion> latestVersionHolder = latestVersion::set;
         UpdateChecker.UpdateCheckerStatus status = this.checkForUpdate(latestVersionHolder);
         if (updateResultHolder != null) {
            updateResultHolder.accept((PEVersion)latestVersion.get(), status);
         }

      }, "packetevents-update-check-thread");
      thread.start();
   }

   @ApiStatus.Internal
   public void handleUpdateCheck() {
      this.handleUpdateCheck((Runnable)null);
   }

   public static enum UpdateCheckerStatus {
      OUTDATED,
      PRE_RELEASE,
      UP_TO_DATE,
      FAILED;

      // $FF: synthetic method
      private static UpdateChecker.UpdateCheckerStatus[] $values() {
         return new UpdateChecker.UpdateCheckerStatus[]{OUTDATED, PRE_RELEASE, UP_TO_DATE, FAILED};
      }
   }
}

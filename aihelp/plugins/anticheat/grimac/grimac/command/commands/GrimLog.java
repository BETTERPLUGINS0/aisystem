package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.manager.init.start.SuperDebug;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.IntegerParser;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class GrimLog implements BuildableCommand {
   public static void sendLogAsync(Sender sender, String log, Consumer<String> consumer, String type) {
      String success = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("upload-log", "%prefix% &fUploaded debug to: %url%");
      String failure = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("upload-log-upload-failure", "%prefix% &cSomething went wrong while uploading this log, see console for more information.");
      String uploading = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("upload-log-start", "%prefix% &fUploading log... please wait");
      uploading = MessageUtil.replacePlaceholders(sender, uploading);
      sender.sendMessage(MessageUtil.miniMessage(uploading));
      GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
         try {
            sendLog(sender, log, success, failure, consumer, type);
         } catch (Exception var8) {
            String message = MessageUtil.replacePlaceholders(sender, failure);
            sender.sendMessage(MessageUtil.miniMessage(message));
            LogUtil.error("Failed to send log", var8);
         }

      });
   }

   private static void sendLog(Sender sender, String log, String success, String failure, Consumer<String> consumer, String type) throws IOException {
      URL mUrl = new URL((String)CommonGrimArguments.PASTE_URL.value() + "data/post");
      HttpURLConnection urlConn = (HttpURLConnection)mUrl.openConnection();

      try {
         urlConn.setDoOutput(true);
         urlConn.setRequestMethod("POST");
         urlConn.addRequestProperty("User-Agent", "GrimAC/" + GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
         urlConn.addRequestProperty("Content-Type", type);
         urlConn.setRequestProperty("Content-Length", Integer.toString(log.length()));
         OutputStream stream = urlConn.getOutputStream();

         try {
            stream.write(log.getBytes(StandardCharsets.UTF_8));
         } catch (Throwable var16) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }
            }

            throw var16;
         }

         if (stream != null) {
            stream.close();
         }

         int response = urlConn.getResponseCode();
         String responseURL;
         if (response == 201) {
            responseURL = urlConn.getHeaderField("Location");
            String var10002 = (String)CommonGrimArguments.PASTE_URL.value();
            String message = success.replace("%url%", var10002 + responseURL);
            consumer.accept(message);
            message = MessageUtil.replacePlaceholders(sender, message);
            sender.sendMessage(MessageUtil.miniMessage(message));
         } else {
            responseURL = MessageUtil.replacePlaceholders(sender, failure);
            sender.sendMessage(MessageUtil.miniMessage(responseURL));
            LogUtil.error("Returned response code " + response + ": " + urlConn.getResponseMessage());
         }
      } finally {
         urlConn.disconnect();
      }

   }

   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      Command<Sender> command = commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("log", "logs").permission("grim.log").required("flagId", IntegerParser.integerParser()).handler(this::handleLog).manager(commandManager).build();
      commandManager.command(command).command(commandManager.commandBuilder("gl", new String[0]).proxies(command));
   }

   private void handleLog(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      int flagId = (Integer)context.get("flagId");
      StringBuilder builder = SuperDebug.getFlag(flagId);
      if (builder == null) {
         sender.sendMessage(MessageUtil.getParsedComponent(sender, "upload-log-not-found", "%prefix% &cUnable to find that log"));
      } else {
         sendLogAsync(sender, builder.toString(), (string) -> {
         }, "text/yaml");
      }
   }
}

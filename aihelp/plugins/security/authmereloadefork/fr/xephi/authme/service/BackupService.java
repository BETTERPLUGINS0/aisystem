package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.BackupSettings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import org.bukkit.command.CommandSender;

public class BackupService {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(EmailService.class);
   private final File dataFolder;
   private final File backupFolder;
   private final Settings settings;

   @Inject
   public BackupService(@DataFolder File dataFolder, Settings settings) {
      this.dataFolder = dataFolder;
      this.backupFolder = new File(dataFolder, "backups");
      this.settings = settings;
   }

   public void doBackup(BackupService.BackupCause cause) {
      this.doBackup(cause, (CommandSender)null);
   }

   public void doBackup(BackupService.BackupCause cause, CommandSender sender) {
      if (!(Boolean)this.settings.getProperty(BackupSettings.ENABLED)) {
         if (cause == BackupService.BackupCause.COMMAND || cause == BackupService.BackupCause.OTHER) {
            Utils.logAndSendWarning(sender, "Can't perform a backup: disabled in configuration. Cause of the backup: " + cause.name());
         }

      } else if ((BackupService.BackupCause.START != cause || (Boolean)this.settings.getProperty(BackupSettings.ON_SERVER_START)) && (BackupService.BackupCause.STOP != cause || (Boolean)this.settings.getProperty(BackupSettings.ON_SERVER_STOP))) {
         if (this.doBackup()) {
            Utils.logAndSendMessage(sender, "A backup has been performed successfully. Cause of the backup: " + cause.name());
         } else {
            Utils.logAndSendWarning(sender, "Error while performing a backup! Cause of the backup: " + cause.name());
         }

      }
   }

   private boolean doBackup() {
      DataSourceType dataSourceType = (DataSourceType)this.settings.getProperty(DatabaseSettings.BACKEND);
      switch(dataSourceType) {
      case MYSQL:
         return this.performMySqlBackup();
      case SQLITE:
         String dbName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
         return this.performFileBackup(dbName + ".db");
      case H2:
         String h2dbName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
         return this.performFileBackup(h2dbName + ".mv.db");
      default:
         this.logger.warning("Unknown data source type '" + dataSourceType + "' for backup");
         return false;
      }
   }

   private boolean performMySqlBackup() {
      FileUtils.createDirectory(this.backupFolder);
      File sqlBackupFile = this.constructBackupFile("sql");
      String backupWindowsPath = (String)this.settings.getProperty(BackupSettings.MYSQL_WINDOWS_PATH);
      boolean isUsingWindows = this.useWindowsCommand(backupWindowsPath);
      String backupCommand = isUsingWindows ? backupWindowsPath + "\\bin\\mysqldump.exe" + this.buildMysqlDumpArguments(sqlBackupFile) : "mysqldump" + this.buildMysqlDumpArguments(sqlBackupFile);

      try {
         Process runtimeProcess = Runtime.getRuntime().exec(backupCommand);
         int processComplete = runtimeProcess.waitFor();
         if (processComplete == 0) {
            this.logger.info("Backup created successfully. (Using Windows = " + isUsingWindows + ")");
            return true;
         }

         this.logger.warning("Could not create the backup! (Using Windows = " + isUsingWindows + ")");
      } catch (InterruptedException | IOException var7) {
         this.logger.logException("Error during backup (using Windows = " + isUsingWindows + "):", var7);
      }

      return false;
   }

   private boolean performFileBackup(String filename) {
      FileUtils.createDirectory(this.backupFolder);
      File backupFile = this.constructBackupFile("db");

      try {
         copy(new File(this.dataFolder, filename), backupFile);
         return true;
      } catch (IOException var4) {
         this.logger.logException("Encountered an error during file backup:", var4);
         return false;
      }
   }

   private boolean useWindowsCommand(String windowsPath) {
      String isWin = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      if (isWin.contains("win")) {
         if ((new File(windowsPath + "\\bin\\mysqldump.exe")).exists()) {
            return true;
         } else {
            this.logger.warning("Mysql Windows Path is incorrect. Please check it");
            return false;
         }
      } else {
         return false;
      }
   }

   private String buildMysqlDumpArguments(File sqlBackupFile) {
      String dbUsername = (String)this.settings.getProperty(DatabaseSettings.MYSQL_USERNAME);
      String dbPassword = (String)this.settings.getProperty(DatabaseSettings.MYSQL_PASSWORD);
      String dbName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_DATABASE);
      String tableName = (String)this.settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      return " -u " + dbUsername + " -p" + dbPassword + " " + dbName + " --tables " + tableName + " -r " + sqlBackupFile.getPath() + ".sql";
   }

   private File constructBackupFile(String fileExtension) {
      String dateString = FileUtils.createCurrentTimeString();
      return new File(this.backupFolder, "backup" + dateString + "." + fileExtension);
   }

   private static void copy(File src, File dst) throws IOException {
      FileInputStream in = new FileInputStream(src);

      try {
         FileOutputStream out = new FileOutputStream(dst);

         try {
            byte[] buf = new byte[1024];

            int len;
            while((len = in.read(buf)) > 0) {
               out.write(buf, 0, len);
            }
         } catch (Throwable var8) {
            try {
               out.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         out.close();
      } catch (Throwable var9) {
         try {
            in.close();
         } catch (Throwable var6) {
            var9.addSuppressed(var6);
         }

         throw var9;
      }

      in.close();
   }

   public static enum BackupCause {
      START,
      STOP,
      COMMAND,
      OTHER;

      // $FF: synthetic method
      private static BackupService.BackupCause[] $values() {
         return new BackupService.BackupCause[]{START, STOP, COMMAND, OTHER};
      }
   }
}

package advancedplugins.pm2.cv.models.api.model.rpc.generator;

import java.io.File;

public interface ModelGenerator {
   default void importModels() {
      this.importModels(false);
   }

   boolean isInitialized();

   void importModels(boolean var1);

   void generateAssets(boolean var1);

   void zipResourcePack(boolean var1);

   void updateConfig();

   void queueTask(ModelGenerator.Phase var1, Runnable var2);

   void addFolderToProcess(File var1);

   File getZippedResourcePack();

   public static enum Phase {
      PRE_IMPORT,
      POST_IMPORT,
      PRE_ASSETS,
      POST_ASSETS,
      PRE_ZIPPING,
      POST_ZIPPING,
      FINISHED;

      private static ModelGenerator.Phase[] $values() {
         return new ModelGenerator.Phase[]{PRE_IMPORT, POST_IMPORT, PRE_ASSETS, POST_ASSETS, PRE_ZIPPING, POST_ZIPPING, FINISHED};
      }

      // $FF: synthetic method
      private static ModelGenerator.Phase[] $values$() {
         return new ModelGenerator.Phase[]{PRE_IMPORT, POST_IMPORT, PRE_ASSETS, POST_ASSETS, PRE_ZIPPING, POST_ZIPPING, FINISHED};
      }
   }
}

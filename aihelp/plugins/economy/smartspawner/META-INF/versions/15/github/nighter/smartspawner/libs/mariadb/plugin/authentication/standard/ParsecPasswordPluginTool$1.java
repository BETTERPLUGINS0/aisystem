package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import java.security.SecureRandom;

class ParsecPasswordPluginTool$1 extends SecureRandom {
   // $FF: synthetic field
   final byte[] val$rawPrivateKey;

   ParsecPasswordPluginTool$1(byte[] var1) {
      this.val$rawPrivateKey = var1;
   }

   public void nextBytes(byte[] bytes) {
      System.arraycopy(this.val$rawPrivateKey, 0, bytes, 0, this.val$rawPrivateKey.length);
   }
}

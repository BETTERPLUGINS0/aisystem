package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FilePersistenceStrategy implements PersistenceStrategy {
   private String path;

   public FilePersistenceStrategy(String path) {
      this.path = path;
   }

   public Identity persistIdentity(Identity identity) {
      ObjectOutputStream oos = null;
      FileOutputStream fos = null;

      Identity var5;
      try {
         File file = new File(this.path + File.separator + identity.getName());
         if (!file.exists()) {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(identity);
            var5 = identity;
            return var5;
         }

         var5 = null;
      } catch (Exception var9) {
         throw new RuntimeException(var9);
      } finally {
         this.safeClose((OutputStream)oos);
         this.safeClose((OutputStream)fos);
      }

      return var5;
   }

   public Identity getIdentity(String name) {
      ObjectInputStream ois = null;
      FileInputStream fis = null;

      Identity var5;
      try {
         fis = new FileInputStream(this.path + File.separator + name);
         ois = new ObjectInputStream(fis);
         Identity identity = (Identity)ois.readObject();
         var5 = identity;
      } catch (Exception var9) {
         throw new RuntimeException(var9);
      } finally {
         this.safeClose((InputStream)ois);
         this.safeClose((InputStream)fis);
      }

      return var5;
   }

   public boolean removeIdentity(Identity identity) {
      File file = new File(this.path + File.separator + identity.getName());
      return file.delete();
   }

   public Identity updateIdentity(Identity identity) {
      return this.removeIdentity(identity) ? this.persistIdentity(identity) : null;
   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }

   private void safeClose(OutputStream os) {
      try {
         if (os != null) {
            os.close();
         }
      } catch (Exception var3) {
      }

   }
}

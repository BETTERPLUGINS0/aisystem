package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FilePassword {
   private File passwordFile;

   public FilePassword(String file) {
      URL url = null;

      try {
         url = new URL(file);
      } catch (MalformedURLException var10) {
      }

      if (url == null) {
         this.passwordFile = new File(file);
      } else {
         FileOutputStream fos = null;
         InputStream is = null;

         try {
            is = url.openStream();
            this.passwordFile = File.createTempFile("temp", (String)null);
            this.passwordFile.deleteOnExit();
            fos = new FileOutputStream(this.passwordFile);

            int b;
            while((b = is.read()) >= 0) {
               fos.write(b);
            }
         } catch (IOException var11) {
         } finally {
            this.safeClose((OutputStream)fos);
            safeClose(is);
         }
      }

   }

   public char[] toCharArray() throws IOException {
      RandomAccessFile raf = null;

      char[] var3;
      try {
         raf = new RandomAccessFile(this.passwordFile, "r");
         char[] password = decode(raf);
         var3 = password;
      } catch (Exception var7) {
         throw new IOException(var7.getMessage());
      } finally {
         this.safeClose(raf);
      }

      return var3;
   }

   static char[] decode(RandomAccessFile passwordFile) throws Exception {
      byte[] salt = new byte[8];
      passwordFile.readFully(salt);
      int count = passwordFile.readInt();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int b;
      while((b = passwordFile.read()) >= 0) {
         baos.write(b);
      }

      passwordFile.close();
      byte[] secret = baos.toByteArray();
      PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
      PBEKeySpec keySpec = new PBEKeySpec("78aac249a60a13d5e882927928043ebb".toCharArray());
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
      SecretKey cipherKey = factory.generateSecret(keySpec);
      Cipher cipher = Cipher.getInstance("PBEwithMD5andDES");
      cipher.init(2, cipherKey, cipherSpec);
      byte[] decode = cipher.doFinal(secret);
      return (new String(decode, "UTF-8")).toCharArray();
   }

   static void encode(RandomAccessFile passwordFile, byte[] salt, int count, byte[] secret) throws Exception {
      PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
      PBEKeySpec keySpec = new PBEKeySpec("78aac249a60a13d5e882927928043ebb".toCharArray());
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
      SecretKey cipherKey = factory.generateSecret(keySpec);
      Cipher cipher = Cipher.getInstance("PBEwithMD5andDES");
      cipher.init(1, cipherKey, cipherSpec);
      byte[] encode = cipher.doFinal(secret);
      passwordFile.write(salt);
      passwordFile.writeInt(count);
      passwordFile.write(encode);
      passwordFile.close();
   }

   private static void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var2) {
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

   private void safeClose(RandomAccessFile raf) {
      try {
         if (raf != null) {
            raf.close();
         }
      } catch (Exception var3) {
      }

   }

   public static void main(String[] args) throws Exception {
      if (args.length != 4) {
         System.err.println(PicketBoxMessages.MESSAGES.filePasswordUsageMessage());
      }

      byte[] salt = args[0].substring(0, 8).getBytes();
      int count = Integer.parseInt(args[1]);
      byte[] passwordBytes = args[2].getBytes("UTF-8");
      RandomAccessFile passwordFile = new RandomAccessFile(args[3], "rws");
      encode(passwordFile, salt, count, passwordBytes);
   }
}

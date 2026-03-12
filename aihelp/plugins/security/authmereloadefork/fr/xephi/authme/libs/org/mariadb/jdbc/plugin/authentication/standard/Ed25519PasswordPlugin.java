package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.GroupElement;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ed25519.Ed25519ScalarOps;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.spec.EdDSANamedCurveTable;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.spec.EdDSAParameterSpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

public class Ed25519PasswordPlugin implements AuthenticationPlugin {
   private String authenticationData;
   private byte[] seed;

   private static byte[] ed25519SignWithPassword(String password, byte[] seed) throws SQLException {
      try {
         byte[] bytePwd = password.getBytes(StandardCharsets.UTF_8);
         MessageDigest hash = MessageDigest.getInstance("SHA-512");
         int mlen = seed.length;
         byte[] sm = new byte[64 + mlen];
         byte[] az = hash.digest(bytePwd);
         az[0] &= -8;
         az[31] = (byte)(az[31] & 63);
         az[31] = (byte)(az[31] | 64);
         System.arraycopy(seed, 0, sm, 64, mlen);
         System.arraycopy(az, 32, sm, 32, 32);
         byte[] buff = Arrays.copyOfRange(sm, 32, 96);
         hash.reset();
         byte[] nonce = hash.digest(buff);
         Ed25519ScalarOps scalar = new Ed25519ScalarOps();
         EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("Ed25519");
         GroupElement elementAvalue = spec.getB().scalarMultiply(az);
         byte[] elementAarray = elementAvalue.toByteArray();
         System.arraycopy(elementAarray, 0, sm, 32, elementAarray.length);
         nonce = scalar.reduce(nonce);
         GroupElement elementRvalue = spec.getB().scalarMultiply(nonce);
         byte[] elementRarray = elementRvalue.toByteArray();
         System.arraycopy(elementRarray, 0, sm, 0, elementRarray.length);
         hash.reset();
         byte[] hram = hash.digest(sm);
         hram = scalar.reduce(hram);
         byte[] tt = scalar.multiplyAndAdd(hram, az, nonce);
         System.arraycopy(tt, 0, sm, 32, tt.length);
         return Arrays.copyOfRange(sm, 0, 64);
      } catch (NoSuchAlgorithmException var17) {
         throw new SQLException("Could not use SHA-512, failing", var17);
      }
   }

   public String type() {
      return "client_ed25519";
   }

   public void initialize(String authenticationData, byte[] seed, Configuration conf) {
      this.seed = seed;
      this.authenticationData = authenticationData;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws SQLException, IOException {
      if (this.authenticationData == null) {
         out.writeEmptyPacket();
      } else {
         out.writeBytes(ed25519SignWithPassword(this.authenticationData, this.seed));
         out.flush();
      }

      return in.readReusablePacket();
   }
}

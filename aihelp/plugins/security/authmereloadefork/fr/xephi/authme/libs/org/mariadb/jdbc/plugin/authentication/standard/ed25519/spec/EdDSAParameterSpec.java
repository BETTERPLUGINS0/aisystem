package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.spec;

import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.Curve;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.GroupElement;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ScalarOps;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class EdDSAParameterSpec implements AlgorithmParameterSpec, Serializable {
   private static final long serialVersionUID = 8274987108472012L;
   private final Curve curve;
   private final String hashAlgo;
   private final ScalarOps sc;
   private final GroupElement B;

   public EdDSAParameterSpec(Curve curve, String hashAlgo, ScalarOps sc, GroupElement B) {
      try {
         MessageDigest hash = MessageDigest.getInstance(hashAlgo);
         if (curve.getField().getb() / 4 != hash.getDigestLength()) {
            throw new IllegalArgumentException("Hash output is not 2b-bit");
         }
      } catch (NoSuchAlgorithmException var6) {
         throw new IllegalArgumentException("Unsupported hash algorithm");
      }

      this.curve = curve;
      this.hashAlgo = hashAlgo;
      this.sc = sc;
      this.B = B;
   }

   public Curve getCurve() {
      return this.curve;
   }

   public String getHashAlgorithm() {
      return this.hashAlgo;
   }

   public ScalarOps getScalarOps() {
      return this.sc;
   }

   public GroupElement getB() {
      return this.B;
   }

   public int hashCode() {
      return this.hashAlgo.hashCode() ^ this.curve.hashCode() ^ this.B.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EdDSAParameterSpec)) {
         return false;
      } else {
         EdDSAParameterSpec s = (EdDSAParameterSpec)o;
         return this.hashAlgo.equals(s.getHashAlgorithm()) && this.curve.equals(s.getCurve()) && this.B.equals(s.getB());
      }
   }
}

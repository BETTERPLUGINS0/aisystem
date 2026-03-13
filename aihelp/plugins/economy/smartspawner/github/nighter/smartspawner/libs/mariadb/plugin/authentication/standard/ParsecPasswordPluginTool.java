package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

public class ParsecPasswordPluginTool {
   public static byte[] process(byte[] rawPrivateKey) throws SQLException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
      Ed25519PrivateKeyParameters privateKeyRebuild = new Ed25519PrivateKeyParameters(rawPrivateKey, 0);
      Ed25519PublicKeyParameters publicKeyRebuild = privateKeyRebuild.generatePublicKey();
      return publicKeyRebuild.getEncoded();
   }
}

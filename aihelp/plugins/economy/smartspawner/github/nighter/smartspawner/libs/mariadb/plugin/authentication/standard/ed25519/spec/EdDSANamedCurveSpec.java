package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.spec;

import github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math.Curve;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math.GroupElement;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard.ed25519.math.ScalarOps;

public class EdDSANamedCurveSpec extends EdDSAParameterSpec {
   private static final long serialVersionUID = -4080022735829727073L;
   private final String name;

   public EdDSANamedCurveSpec(String name, Curve curve, String hashAlgo, ScalarOps sc, GroupElement B) {
      super(curve, hashAlgo, sc, B);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}

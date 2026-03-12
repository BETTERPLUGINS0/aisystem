package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.spec;

import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.Curve;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.Field;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ed25519.Ed25519LittleEndianEncoding;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ed25519.Ed25519ScalarOps;
import java.util.HashMap;
import java.util.Locale;

public class EdDSANamedCurveTable {
   public static final String ED_25519 = "Ed25519";
   private static final Field ed25519field = new Field(256, Utils.hexToBytes("edffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"), new Ed25519LittleEndianEncoding());
   private static final Curve ed25519curve;
   public static final EdDSANamedCurveSpec ED_25519_CURVE_SPEC;
   private static volatile HashMap<String, EdDSANamedCurveSpec> curves;

   private static synchronized void putCurve(String name, EdDSANamedCurveSpec curve) {
      HashMap<String, EdDSANamedCurveSpec> newCurves = new HashMap(curves);
      newCurves.put(name, curve);
      curves = newCurves;
   }

   public static void defineCurve(EdDSANamedCurveSpec curve) {
      putCurve(curve.getName().toLowerCase(Locale.ENGLISH), curve);
   }

   public static EdDSANamedCurveSpec getByName(String name) {
      return (EdDSANamedCurveSpec)curves.get(name.toLowerCase(Locale.ENGLISH));
   }

   static {
      ed25519curve = new Curve(ed25519field, Utils.hexToBytes("a3785913ca4deb75abd841414d0a700098e879777940c78c73fe6f2bee6c0352"), ed25519field.fromByteArray(Utils.hexToBytes("b0a00e4a271beec478e42fad0618432fa7d7fb3d99004d2b0bdfc14f8024832b")));
      ED_25519_CURVE_SPEC = new EdDSANamedCurveSpec("Ed25519", ed25519curve, "SHA-512", new Ed25519ScalarOps(), ed25519curve.createPoint(Utils.hexToBytes("5866666666666666666666666666666666666666666666666666666666666666"), true));
      curves = new HashMap();
      defineCurve(ED_25519_CURVE_SPEC);
   }
}

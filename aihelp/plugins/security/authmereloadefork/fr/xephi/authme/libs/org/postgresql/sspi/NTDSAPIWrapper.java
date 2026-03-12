package fr.xephi.authme.libs.org.postgresql.sspi;

import com.sun.jna.LastErrorException;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import org.checkerframework.checker.nullness.qual.Nullable;

public class NTDSAPIWrapper {
   static final NTDSAPIWrapper instance = new NTDSAPIWrapper();

   public String DsMakeSpn(String serviceClass, String serviceName, @Nullable String instanceName, short instancePort, @Nullable String referrer) throws LastErrorException {
      IntByReference spnLength = new IntByReference(2048);
      char[] spn = new char[spnLength.getValue()];
      int ret = NTDSAPI.instance.DsMakeSpnW(new WString(serviceClass), new WString(serviceName), instanceName == null ? null : new WString(instanceName), instancePort, referrer == null ? null : new WString(referrer), spnLength, spn);
      if (ret != 0) {
         throw new RuntimeException("NTDSAPI DsMakeSpn call failed with " + ret);
      } else {
         return new String(spn, 0, spnLength.getValue());
      }
   }
}

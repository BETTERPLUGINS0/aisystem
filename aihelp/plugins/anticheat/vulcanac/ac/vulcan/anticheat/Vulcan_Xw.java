package ac.vulcan.anticheat;

import java.io.Serializable;

public class Vulcan_Xw implements Serializable {
   private static final long serialVersionUID = 7092611880189329093L;

   Vulcan_Xw() {
   }

   private Object readResolve() {
      return Vulcan_e8.Vulcan_A;
   }
}

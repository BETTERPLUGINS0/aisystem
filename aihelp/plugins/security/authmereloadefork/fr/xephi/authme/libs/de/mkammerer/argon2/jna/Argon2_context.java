package fr.xephi.authme.libs.de.mkammerer.argon2.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder({"out", "outlen", "pwd", "pwdlen", "salt", "saltlen", "secret", "secretlen", "ad", "adlen", "t_cost", "m_cost", "lanes", "threads", "version", "allocate_cbk", "free_cbk", "flags"})
public class Argon2_context extends Structure {
   public Pointer out = null;
   public JnaUint32 outlen = new JnaUint32(0);
   public Pointer pwd = null;
   public JnaUint32 pwdlen = new JnaUint32(0);
   public Pointer salt = null;
   public JnaUint32 saltlen = new JnaUint32(0);
   public Pointer secret;
   public JnaUint32 secretlen;
   public Pointer ad;
   public JnaUint32 adlen;
   public JnaUint32 t_cost;
   public JnaUint32 m_cost;
   public JnaUint32 lanes;
   public JnaUint32 threads;
   public Argon2_version version;
   public Pointer allocate_cbk;
   public Pointer free_cbk;
   public JnaUint32 flags;

   public Argon2_context() {
      this.secret = Pointer.NULL;
      this.secretlen = new JnaUint32(0);
      this.ad = Pointer.NULL;
      this.adlen = new JnaUint32(0);
      this.allocate_cbk = Pointer.NULL;
      this.free_cbk = Pointer.NULL;
      this.flags = new JnaUint32(0);
   }

   public static class ByReference extends Argon2_context implements Structure.ByReference {
   }
}

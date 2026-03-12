package fr.xephi.authme.libs.de.mkammerer.argon2;

import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2Library;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Argon2_context;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.JnaUint32;
import fr.xephi.authme.libs.de.mkammerer.argon2.jna.Size_t;

class Argon2id extends BaseArgon2 {
   Argon2id(int defaultSaltLength, int defaultHashLength) {
      super(defaultSaltLength, defaultHashLength);
   }

   public final Argon2Factory.Argon2Types getType() {
      return Argon2Factory.Argon2Types.ARGON2id;
   }

   protected int callLibraryHash(byte[] pwd, byte[] salt, JnaUint32 iterations, JnaUint32 memory, JnaUint32 parallelism, byte[] encoded) {
      return Argon2Library.INSTANCE.argon2id_hash_encoded(iterations, memory, parallelism, pwd, new Size_t((long)pwd.length), salt, new Size_t((long)salt.length), new Size_t((long)this.getDefaultHashLength()), encoded, new Size_t((long)encoded.length));
   }

   protected int callLibraryRawHash(byte[] pwd, byte[] salt, JnaUint32 iterations, JnaUint32 memory, JnaUint32 parallelism, byte[] hash) {
      return Argon2Library.INSTANCE.argon2id_hash_raw(iterations, memory, parallelism, pwd, new Size_t((long)pwd.length), salt, new Size_t((long)salt.length), hash, new Size_t((long)hash.length));
   }

   protected int callLibraryVerify(byte[] encoded, byte[] pwd) {
      return Argon2Library.INSTANCE.argon2id_verify(encoded, pwd, new Size_t((long)pwd.length));
   }

   protected int callLibraryContext(Argon2_context.ByReference context) {
      return Argon2Library.INSTANCE.argon2id_ctx(context);
   }

   protected int callLibraryVerifyContext(Argon2_context.ByReference context, byte[] rawHash) {
      return Argon2Library.INSTANCE.argon2id_verify_ctx(context, rawHash);
   }
}

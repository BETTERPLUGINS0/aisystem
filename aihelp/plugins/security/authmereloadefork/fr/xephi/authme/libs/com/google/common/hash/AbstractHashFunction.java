package fr.xephi.authme.libs.com.google.common.hash;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractHashFunction implements HashFunction {
   public <T> HashCode hashObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
      return this.newHasher().putObject(instance, funnel).hash();
   }

   public HashCode hashUnencodedChars(CharSequence input) {
      int len = input.length();
      return this.newHasher(len * 2).putUnencodedChars(input).hash();
   }

   public HashCode hashString(CharSequence input, Charset charset) {
      return this.newHasher().putString(input, charset).hash();
   }

   public HashCode hashInt(int input) {
      return this.newHasher(4).putInt(input).hash();
   }

   public HashCode hashLong(long input) {
      return this.newHasher(8).putLong(input).hash();
   }

   public HashCode hashBytes(byte[] input) {
      return this.hashBytes(input, 0, input.length);
   }

   public HashCode hashBytes(byte[] input, int off, int len) {
      Preconditions.checkPositionIndexes(off, off + len, input.length);
      return this.newHasher(len).putBytes(input, off, len).hash();
   }

   public HashCode hashBytes(ByteBuffer input) {
      return this.newHasher(input.remaining()).putBytes(input).hash();
   }

   public Hasher newHasher(int expectedInputSize) {
      Preconditions.checkArgument(expectedInputSize >= 0, "expectedInputSize must be >= 0 but was %s", expectedInputSize);
      return this.newHasher();
   }
}

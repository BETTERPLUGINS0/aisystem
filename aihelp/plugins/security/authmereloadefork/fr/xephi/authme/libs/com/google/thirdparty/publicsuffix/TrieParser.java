package fr.xephi.authme.libs.com.google.thirdparty.publicsuffix;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Joiner;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Queues;
import java.util.Deque;

@GwtCompatible
final class TrieParser {
   private static final Joiner PREFIX_JOINER = Joiner.on("");

   static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
      ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
      int encodedLen = encoded.length();

      for(int idx = 0; idx < encodedLen; idx += doParseTrieToBuilder(Queues.newArrayDeque(), encoded, idx, builder)) {
      }

      return builder.buildOrThrow();
   }

   private static int doParseTrieToBuilder(Deque<CharSequence> stack, CharSequence encoded, int start, ImmutableMap.Builder<String, PublicSuffixType> builder) {
      int encodedLen = encoded.length();
      int idx = start;

      char c;
      for(c = 0; idx < encodedLen; ++idx) {
         c = encoded.charAt(idx);
         if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
            break;
         }
      }

      stack.push(reverse(encoded.subSequence(start, idx)));
      if (c == '!' || c == '?' || c == ':' || c == ',') {
         String domain = PREFIX_JOINER.join((Iterable)stack);
         if (domain.length() > 0) {
            builder.put(domain, PublicSuffixType.fromCode(c));
         }
      }

      ++idx;
      if (c != '?' && c != ',') {
         label67: {
            do {
               if (idx >= encodedLen) {
                  break label67;
               }

               idx += doParseTrieToBuilder(stack, encoded, idx, builder);
            } while(encoded.charAt(idx) != '?' && encoded.charAt(idx) != ',');

            ++idx;
         }
      }

      stack.pop();
      return idx - start;
   }

   private static CharSequence reverse(CharSequence s) {
      return (new StringBuilder(s)).reverse();
   }
}

package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.saslprep;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.stringprep.StringPrep;
import java.nio.CharBuffer;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SaslPrep {
   private static final int MAX_UTF = 65535;

   public static String saslPrep(String value, boolean storedString) {
      List<Integer> valueBuilder = new ArrayList();
      List<Integer> codePoints = new ArrayList();

      for(int i = 0; i < value.length(); ++i) {
         int codePoint = value.codePointAt(i);
         codePoints.add(codePoint);
         if (codePoint > 65535) {
            ++i;
         }

         if (!StringPrep.prohibitionNonAsciiSpace(codePoint)) {
            valueBuilder.add(codePoint);
         }
      }

      StringBuilder stringBuilder = new StringBuilder();
      Iterator var9 = codePoints.iterator();

      int i;
      while(var9.hasNext()) {
         i = (Integer)var9.next();
         if (!StringPrep.mapToNothing(i)) {
            char[] characters = Character.toChars(i);
            stringBuilder.append(characters);
         }
      }

      String normalized = Normalizer.normalize(CharBuffer.wrap(stringBuilder.toString().toCharArray()), Form.NFKC);
      valueBuilder = new ArrayList();

      int character;
      for(i = 0; i < normalized.length(); ++i) {
         character = normalized.codePointAt(i);
         codePoints.add(character);
         if (character > 65535) {
            ++i;
         }

         if (!StringPrep.prohibitionNonAsciiSpace(character)) {
            valueBuilder.add(character);
         }
      }

      Iterator var12 = valueBuilder.iterator();

      do {
         if (!var12.hasNext()) {
            StringPrep.bidirectional(valueBuilder);
            return normalized;
         }

         character = (Integer)var12.next();
         if (StringPrep.prohibitionNonAsciiSpace(character) || StringPrep.prohibitionAsciiControl(character) || StringPrep.prohibitionNonAsciiControl(character) || StringPrep.prohibitionPrivateUse(character) || StringPrep.prohibitionNonCharacterCodePoints(character) || StringPrep.prohibitionSurrogateCodes(character) || StringPrep.prohibitionInappropriatePlainText(character) || StringPrep.prohibitionInappropriateCanonicalRepresentation(character) || StringPrep.prohibitionChangeDisplayProperties(character) || StringPrep.prohibitionTaggingCharacters(character)) {
            throw new IllegalArgumentException("Prohibited character " + String.valueOf(Character.toChars(character)));
         }
      } while(!storedString || !StringPrep.unassignedCodePoints(character));

      throw new IllegalArgumentException("Prohibited character " + String.valueOf(Character.toChars(character)));
   }
}

package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util;

public class AbstractCharAttributeValue extends AbstractStringWritable implements CharAttributeValue {
   private final CharAttribute charAttribute;
   private final String value;

   public AbstractCharAttributeValue(CharAttribute charAttribute, String value) throws IllegalArgumentException {
      this.charAttribute = (CharAttribute)Preconditions.checkNotNull(charAttribute, "attribute");
      if (null != value && value.isEmpty()) {
         throw new IllegalArgumentException("Value should be either null or non-empty");
      } else {
         this.value = value;
      }
   }

   public char getChar() {
      return this.charAttribute.getChar();
   }

   public String getValue() {
      return this.value;
   }

   public StringBuffer writeTo(StringBuffer sb) {
      sb.append(this.charAttribute.getChar());
      if (null != this.value) {
         sb.append('=').append(this.value);
      }

      return sb;
   }
}

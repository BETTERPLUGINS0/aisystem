package fr.xephi.authme.libs.at.favre.lib.bytes;

import java.util.Iterator;
import java.util.List;

public interface BytesValidator {
   boolean validate(byte[] var1);

   public static final class Logical implements BytesValidator {
      private final List<BytesValidator> validatorList;
      private final BytesValidator.Logical.Operator operator;

      public Logical(List<BytesValidator> validatorList, BytesValidator.Logical.Operator operator) {
         if (validatorList.isEmpty()) {
            throw new IllegalArgumentException("must contain at least 1 element");
         } else if (operator == BytesValidator.Logical.Operator.NOT && validatorList.size() != 1) {
            throw new IllegalArgumentException("not operator can only be applied to single element");
         } else {
            this.validatorList = validatorList;
            this.operator = operator;
         }
      }

      public boolean validate(byte[] byteArrayToValidate) {
         if (this.operator == BytesValidator.Logical.Operator.NOT) {
            return !((BytesValidator)this.validatorList.get(0)).validate(byteArrayToValidate);
         } else {
            boolean bool = this.operator != BytesValidator.Logical.Operator.OR;
            Iterator var3 = this.validatorList.iterator();

            while(var3.hasNext()) {
               BytesValidator bytesValidator = (BytesValidator)var3.next();
               switch(this.operator) {
               case AND:
                  bool &= bytesValidator.validate(byteArrayToValidate);
                  break;
               case OR:
               default:
                  bool |= bytesValidator.validate(byteArrayToValidate);
               }
            }

            return bool;
         }
      }

      static enum Operator {
         OR,
         AND,
         NOT;
      }
   }

   public static final class PrePostFix implements BytesValidator {
      private final byte[] pfix;
      private final boolean startsWith;

      public PrePostFix(boolean startsWith, byte... pfix) {
         this.pfix = pfix;
         this.startsWith = startsWith;
      }

      public boolean validate(byte[] byteArrayToValidate) {
         if (this.pfix.length > byteArrayToValidate.length) {
            return false;
         } else {
            for(int i = 0; i < this.pfix.length; ++i) {
               if (this.startsWith && this.pfix[i] != byteArrayToValidate[i]) {
                  return false;
               }

               if (!this.startsWith && this.pfix[i] != byteArrayToValidate[byteArrayToValidate.length - this.pfix.length + i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static final class IdenticalContent implements BytesValidator {
      private final byte refByte;
      private final BytesValidator.IdenticalContent.Mode mode;

      IdenticalContent(byte refByte, BytesValidator.IdenticalContent.Mode mode) {
         this.refByte = refByte;
         this.mode = mode;
      }

      public boolean validate(byte[] byteArrayToValidate) {
         byte[] var2 = byteArrayToValidate;
         int var3 = byteArrayToValidate.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            if (this.mode == BytesValidator.IdenticalContent.Mode.NONE_OF && b == this.refByte) {
               return false;
            }

            if (this.mode == BytesValidator.IdenticalContent.Mode.ONLY_OF && b != this.refByte) {
               return false;
            }

            if (this.mode == BytesValidator.IdenticalContent.Mode.NOT_ONLY_OF && b != this.refByte) {
               return true;
            }
         }

         return this.mode == BytesValidator.IdenticalContent.Mode.NONE_OF || this.mode == BytesValidator.IdenticalContent.Mode.ONLY_OF;
      }

      static enum Mode {
         ONLY_OF,
         NONE_OF,
         NOT_ONLY_OF;
      }
   }

   public static final class Length implements BytesValidator {
      private final int refLength;
      private final BytesValidator.Length.Mode mode;

      public Length(int refLength, BytesValidator.Length.Mode mode) {
         this.refLength = refLength;
         this.mode = mode;
      }

      public boolean validate(byte[] byteArrayToValidate) {
         switch(this.mode) {
         case GREATER_OR_EQ_THAN:
            return byteArrayToValidate.length >= this.refLength;
         case SMALLER_OR_EQ_THAN:
            return byteArrayToValidate.length <= this.refLength;
         case EXACT:
         default:
            return byteArrayToValidate.length == this.refLength;
         }
      }

      static enum Mode {
         SMALLER_OR_EQ_THAN,
         GREATER_OR_EQ_THAN,
         EXACT;
      }
   }
}

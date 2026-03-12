package ac.grim.grimac.shaded.snakeyaml.scanner;

import ac.grim.grimac.shaded.snakeyaml.tokens.Token;

public interface Scanner {
   boolean checkToken(Token.ID... var1);

   Token peekToken();

   Token getToken();

   void resetDocumentIndex();
}

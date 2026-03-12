package fr.xephi.authme.libs.org.jboss.security.auth.login;

import java.io.IOException;
import java.io.PrintStream;

public class SunConfigParserTokenManager implements SunConfigParserConstants {
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[]{65, 66, 67, 69, 70, 73, 74, 75, 57, 58, 11, 13, 14, 6, 8, 9, 1, 2, 4, 60, 61, 63, 41, 49, 53, 54};
   public static final String[] jjstrLiteralImages = new String[]{"", null, null, null, null, null, null, "{", "}", ";", "=", null, null, null, null, null, null, null, null, null, null, null};
   public static final String[] lexStateNames = new String[]{"DEFAULT"};
   static final long[] jjtoToken = new long[]{1302401L};
   static final long[] jjtoSkip = new long[]{126L};
   static final long[] jjtoSpecial = new long[]{96L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   protected char curChar;
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream ds) {
      this.debugStream = ds;
   }

   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
      switch(pos) {
      default:
         return -1;
      }
   }

   private final int jjStartNfa_0(int pos, long active0) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0), pos + 1);
   }

   private int jjStopAtPos(int pos, int kind) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;
      return pos + 1;
   }

   private int jjMoveStringLiteralDfa0_0() {
      switch(this.curChar) {
      case ';':
         return this.jjStopAtPos(0, 9);
      case '=':
         return this.jjStopAtPos(0, 10);
      case '{':
         return this.jjStartNfaWithStates_0(0, 7, 33);
      case '}':
         return this.jjStartNfaWithStates_0(0, 8, 33);
      default:
         return this.jjMoveNfa_0(0, 0);
      }
   }

   private int jjStartNfaWithStates_0(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_0(state, pos + 1);
   }

   private int jjMoveNfa_0(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 78;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if ((-2882303765812094465L & l) != 0L) {
                     if (kind > 20) {
                        kind = 20;
                     }

                     this.jjCheckNAdd(33);
                  }

                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 11) {
                        kind = 11;
                     }

                     this.jjCheckNAddStates(0, 4);
                  } else if (this.curChar == '$') {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAddStates(5, 7);
                  } else if (this.curChar == '/') {
                     this.jjAddStates(8, 9);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAddTwoStates(51, 55);
                  } else if (this.curChar == '"') {
                     this.jjCheckNAddStates(10, 12);
                  } else if (this.curChar == '\'') {
                     this.jjCheckNAddStates(13, 15);
                  } else if (this.curChar == '#') {
                     this.jjCheckNAddStates(16, 18);
                  }
                  break;
               case 1:
                  if ((-9217L & l) != 0L) {
                     this.jjCheckNAddStates(16, 18);
                  }
                  break;
               case 2:
                  if ((9216L & l) != 0L && kind > 5) {
                     kind = 5;
                  }
                  break;
               case 3:
                  if (this.curChar == '\n' && kind > 5) {
                     kind = 5;
                  }
                  break;
               case 4:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 3;
                  }
                  break;
               case 5:
               case 7:
                  if (this.curChar == '\'') {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 6:
                  if ((-549755823105L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 8:
                  if (this.curChar == '\'') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 9:
                  if (this.curChar == '\'' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 10:
               case 12:
                  if (this.curChar == '"') {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 11:
                  if ((-17179878401L & l) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 13:
                  if (this.curChar == '"') {
                     this.jjstateSet[this.jjnewStateCnt++] = 12;
                  }
                  break;
               case 14:
                  if (this.curChar == '"' && kind > 14) {
                     kind = 14;
                  }
               case 15:
               case 16:
               case 17:
               case 18:
               case 19:
               case 20:
               case 21:
               case 22:
               case 23:
               case 24:
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 48:
               case 49:
               case 52:
               default:
                  break;
               case 33:
                  if ((-2882303765812094465L & l) != 0L) {
                     if (kind > 20) {
                        kind = 20;
                     }

                     this.jjCheckNAdd(33);
                  }
                  break;
               case 50:
                  if (this.curChar == '.') {
                     this.jjCheckNAddTwoStates(51, 55);
                  }
                  break;
               case 51:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAddTwoStates(51, 52);
                  }
                  break;
               case 53:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(54);
                  }
                  break;
               case 54:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAdd(54);
                  }
                  break;
               case 55:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAdd(55);
                  }
                  break;
               case 56:
                  if (this.curChar == '/') {
                     this.jjAddStates(8, 9);
                  }
                  break;
               case 57:
                  if (this.curChar == '/') {
                     this.jjCheckNAddStates(16, 18);
                  }
                  break;
               case 58:
                  if (this.curChar == '*') {
                     this.jjCheckNAddTwoStates(59, 60);
                  }
                  break;
               case 59:
                  if ((-4398046511105L & l) != 0L) {
                     this.jjCheckNAddTwoStates(59, 60);
                  }
                  break;
               case 60:
                  if (this.curChar == '*') {
                     this.jjCheckNAddStates(19, 21);
                  }
                  break;
               case 61:
                  if ((-145135534866433L & l) != 0L) {
                     this.jjCheckNAddTwoStates(62, 60);
                  }
                  break;
               case 62:
                  if ((-4398046511105L & l) != 0L) {
                     this.jjCheckNAddTwoStates(62, 60);
                  }
                  break;
               case 63:
                  if (this.curChar == '/' && kind > 6) {
                     kind = 6;
                  }
                  break;
               case 64:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 11) {
                        kind = 11;
                     }

                     this.jjCheckNAddStates(0, 4);
                  }
                  break;
               case 65:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 11) {
                        kind = 11;
                     }

                     this.jjCheckNAdd(65);
                  }
                  break;
               case 66:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(66, 67);
                  }
                  break;
               case 67:
                  if (this.curChar == '.') {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAddTwoStates(68, 52);
                  }
                  break;
               case 68:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAddTwoStates(68, 52);
                  }
                  break;
               case 69:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(69, 70);
                  }
                  break;
               case 70:
                  if (this.curChar == '.') {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAdd(71);
                  }
                  break;
               case 71:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 12) {
                        kind = 12;
                     }

                     this.jjCheckNAdd(71);
                  }
                  break;
               case 72:
                  if (this.curChar == '$') {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAddStates(5, 7);
                  }
                  break;
               case 73:
                  if ((287984154266566656L & l) != 0L) {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAdd(73);
                  }
                  break;
               case 74:
                  if ((287948969894477824L & l) != 0L) {
                     if (kind > 17) {
                        kind = 17;
                     }

                     this.jjCheckNAddTwoStates(74, 75);
                  }
                  break;
               case 75:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 76;
                  }
                  break;
               case 76:
                  if (this.curChar == '$') {
                     if (kind > 17) {
                        kind = 17;
                     }

                     this.jjCheckNAddTwoStates(75, 77);
                  }
                  break;
               case 77:
                  if ((287948969894477824L & l) != 0L) {
                     if (kind > 17) {
                        kind = 17;
                     }

                     this.jjCheckNAddTwoStates(75, 77);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (kind > 20) {
                     kind = 20;
                  }

                  this.jjCheckNAdd(33);
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAddStates(5, 7);
                  }

                  if ((1125899907104768L & l) != 0L) {
                     this.jjAddStates(22, 23);
                  } else if ((140737488388096L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 31;
                  } else if ((2251799814209536L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 23;
                  }
                  break;
               case 1:
                  this.jjAddStates(16, 18);
               case 2:
               case 3:
               case 4:
               case 5:
               case 7:
               case 8:
               case 9:
               case 10:
               case 12:
               case 13:
               case 14:
               case 50:
               case 51:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 60:
               case 63:
               case 64:
               case 65:
               case 66:
               case 67:
               case 68:
               case 69:
               case 70:
               case 71:
               case 75:
               default:
                  break;
               case 6:
                  this.jjAddStates(13, 15);
                  break;
               case 11:
                  this.jjAddStates(10, 12);
                  break;
               case 15:
                  if ((4503599628419072L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 16:
                  if ((70368744194048L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 15;
                  }
                  break;
               case 17:
                  if ((137438953504L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 16;
                  }
                  break;
               case 18:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 17;
                  }
                  break;
               case 19:
                  if ((34359738376L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 18;
                  }
                  break;
               case 20:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 19;
                  }
                  break;
               case 21:
                  if ((274877907008L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 20;
                  }
                  break;
               case 22:
                  if ((274877907008L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 21;
                  }
                  break;
               case 23:
                  if ((9007199256838144L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 22;
                  }
                  break;
               case 24:
                  if ((2251799814209536L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 23;
                  }
                  break;
               case 25:
                  if ((17592186048512L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 26:
                  if ((8589934594L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 25;
                  }
                  break;
               case 27:
                  if ((70368744194048L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 26;
                  }
                  break;
               case 28:
                  if ((140737488388096L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 27;
                  }
                  break;
               case 29:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 28;
                  }
                  break;
               case 30:
                  if ((4503599628419072L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 29;
                  }
                  break;
               case 31:
                  if ((281474976776192L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 30;
                  }
                  break;
               case 32:
                  if ((140737488388096L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 31;
                  }
                  break;
               case 33:
                  if (kind > 20) {
                     kind = 20;
                  }

                  this.jjCheckNAdd(33);
                  break;
               case 34:
                  if ((1125899907104768L & l) != 0L) {
                     this.jjAddStates(22, 23);
                  }
                  break;
               case 35:
                  if ((68719476752L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 36:
                  if ((137438953504L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 35;
                  }
                  break;
               case 37:
                  if ((1125899907104768L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 36;
                  }
                  break;
               case 38:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 37;
                  }
                  break;
               case 39:
                  if ((9007199256838144L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 38;
                  }
                  break;
               case 40:
                  if ((562949953552384L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 39;
                  }
                  break;
               case 41:
                  if ((137438953504L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 40;
                  }
                  break;
               case 42:
                  if ((137438953504L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 43:
                  if ((4503599628419072L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 42;
                  }
                  break;
               case 44:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 43;
                  }
                  break;
               case 45:
                  if ((2251799814209536L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 44;
                  }
                  break;
               case 46:
                  if ((2199023256064L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 45;
                  }
                  break;
               case 47:
                  if ((9007199256838144L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 46;
                  }
                  break;
               case 48:
                  if ((562949953552384L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 47;
                  }
                  break;
               case 49:
                  if ((137438953504L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 48;
                  }
                  break;
               case 52:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(24, 25);
                  }
                  break;
               case 59:
                  this.jjCheckNAddTwoStates(59, 60);
                  break;
               case 61:
               case 62:
                  this.jjCheckNAddTwoStates(62, 60);
                  break;
               case 72:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAddStates(5, 7);
                  }
                  break;
               case 73:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 16) {
                        kind = 16;
                     }

                     this.jjCheckNAdd(73);
                  }
                  break;
               case 74:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 17) {
                        kind = 17;
                     }

                     this.jjCheckNAddTwoStates(74, 75);
                  }
                  break;
               case 76:
               case 77:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 17) {
                        kind = 17;
                     }

                     this.jjCheckNAddTwoStates(75, 77);
                  }
               }
            } while(i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
               case 33:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     if (kind > 20) {
                        kind = 20;
                     }

                     this.jjCheckNAdd(33);
                  }
                  break;
               case 1:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     this.jjAddStates(16, 18);
                  }
                  break;
               case 6:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     this.jjAddStates(13, 15);
                  }
                  break;
               case 11:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     this.jjAddStates(10, 12);
                  }
                  break;
               case 59:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     this.jjCheckNAddTwoStates(59, 60);
                  }
                  break;
               case 61:
               case 62:
                  if ((jjbitVec0[i2] & l2) != 0L) {
                     this.jjCheckNAddTwoStates(62, 60);
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 78 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   public SunConfigParserTokenManager(SimpleCharStream stream) {
      this.debugStream = System.out;
      this.jjrounds = new int[78];
      this.jjstateSet = new int[156];
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.input_stream = stream;
   }

   public SunConfigParserTokenManager(SimpleCharStream stream, int lexState) {
      this(stream);
      this.SwitchTo(lexState);
   }

   public void ReInit(SimpleCharStream stream) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = stream;
      this.ReInitRounds();
   }

   private void ReInitRounds() {
      this.jjround = -2147483647;

      for(int i = 78; i-- > 0; this.jjrounds[i] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(SimpleCharStream stream, int lexState) {
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void SwitchTo(int lexState) {
      if (lexState < 1 && lexState >= 0) {
         this.curLexState = lexState;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      String im = jjstrLiteralImages[this.jjmatchedKind];
      String curTokenImage = im == null ? this.input_stream.GetImage() : im;
      int beginLine = this.input_stream.getBeginLine();
      int beginColumn = this.input_stream.getBeginColumn();
      int endLine = this.input_stream.getEndLine();
      int endColumn = this.input_stream.getEndColumn();
      Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
      t.beginLine = beginLine;
      t.endLine = endLine;
      t.beginColumn = beginColumn;
      t.endColumn = endColumn;
      return t;
   }

   public Token getNextToken() {
      Token specialToken = null;
      boolean var3 = false;

      while(true) {
         Token matchedToken;
         label76:
         while(true) {
            try {
               this.curChar = this.input_stream.BeginToken();
            } catch (IOException var9) {
               this.jjmatchedKind = 0;
               matchedToken = this.jjFillToken();
               matchedToken.specialToken = specialToken;
               return matchedToken;
            }

            try {
               this.input_stream.backup(0);

               while(true) {
                  if (this.curChar > ' ' || (4294977024L & 1L << this.curChar) == 0L) {
                     break label76;
                  }

                  this.curChar = this.input_stream.BeginToken();
               }
            } catch (IOException var11) {
            }
         }

         this.jjmatchedKind = Integer.MAX_VALUE;
         this.jjmatchedPos = 0;
         int curPos = this.jjMoveStringLiteralDfa0_0();
         if (this.jjmatchedKind == Integer.MAX_VALUE) {
            int error_line = this.input_stream.getEndLine();
            int error_column = this.input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;

            try {
               this.input_stream.readChar();
               this.input_stream.backup(1);
            } catch (IOException var10) {
               EOFSeen = true;
               error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
               if (this.curChar != '\n' && this.curChar != '\r') {
                  ++error_column;
               } else {
                  ++error_line;
                  error_column = 0;
               }
            }

            if (!EOFSeen) {
               this.input_stream.backup(1);
               error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
            }

            throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
         }

         if (this.jjmatchedPos + 1 < curPos) {
            this.input_stream.backup(curPos - this.jjmatchedPos - 1);
         }

         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
            matchedToken = this.jjFillToken();
            matchedToken.specialToken = specialToken;
            return matchedToken;
         }

         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
            matchedToken = this.jjFillToken();
            if (specialToken == null) {
               specialToken = matchedToken;
            } else {
               matchedToken.specialToken = specialToken;
               specialToken = specialToken.next = matchedToken;
            }
         }
      }
   }

   private void jjCheckNAdd(int state) {
      if (this.jjrounds[state] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = state;
         this.jjrounds[state] = this.jjround;
      }

   }

   private void jjAddStates(int start, int end) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
      } while(start++ != end);

   }

   private void jjCheckNAddTwoStates(int state1, int state2) {
      this.jjCheckNAdd(state1);
      this.jjCheckNAdd(state2);
   }

   private void jjCheckNAddStates(int start, int end) {
      do {
         this.jjCheckNAdd(jjnextStates[start]);
      } while(start++ != end);

   }
}

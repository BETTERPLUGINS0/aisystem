/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.KeywordTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Token;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;

public class Lex
implements TokenId {
    private int lastChar = -1;
    private StringBuilder textBuffer = new StringBuilder();
    private Token currentToken = new Token();
    private Token lookAheadTokens = null;
    private String input;
    private int position;
    private int maxlen;
    private int lineNumber;
    private static final int[] equalOps = new int[]{350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0};
    private static final KeywordTable ktable = new KeywordTable();

    public Lex(String string) {
        this.input = string;
        this.position = 0;
        this.maxlen = string.length();
        this.lineNumber = 0;
    }

    public int get() {
        Token token;
        if (this.lookAheadTokens == null) {
            return this.get(this.currentToken);
        }
        this.currentToken = token = this.lookAheadTokens;
        this.lookAheadTokens = this.lookAheadTokens.next;
        return token.tokenId;
    }

    public int lookAhead() {
        return this.lookAhead(0);
    }

    public int lookAhead(int n) {
        Token token = this.lookAheadTokens;
        if (token == null) {
            this.lookAheadTokens = token = this.currentToken;
            token.next = null;
            this.get(token);
        }
        while (n-- > 0) {
            if (token.next == null) {
                Token token2;
                token.next = token2 = new Token();
                this.get(token2);
            }
            token = token.next;
        }
        this.currentToken = token;
        return token.tokenId;
    }

    public String getString() {
        return this.currentToken.textValue;
    }

    public long getLong() {
        return this.currentToken.longValue;
    }

    public double getDouble() {
        return this.currentToken.doubleValue;
    }

    private int get(Token token) {
        int n;
        while ((n = this.readLine(token)) == 10) {
        }
        token.tokenId = n;
        return n;
    }

    private int readLine(Token token) {
        int n = this.getNextNonWhiteChar();
        if (n < 0) {
            return n;
        }
        if (n == 10) {
            ++this.lineNumber;
            return 10;
        }
        if (n == 39) {
            return this.readCharConst(token);
        }
        if (n == 34) {
            return this.readStringL(token);
        }
        if (48 <= n && n <= 57) {
            return this.readNumber(n, token);
        }
        if (n == 46) {
            n = this.getc();
            if (48 <= n && n <= 57) {
                StringBuilder stringBuilder = this.textBuffer;
                stringBuilder.setLength(0);
                stringBuilder.append('.');
                return this.readDouble(stringBuilder, n, token);
            }
            this.ungetc(n);
            return this.readSeparator(46);
        }
        if (Character.isJavaIdentifierStart((char)n)) {
            return this.readIdentifier(n, token);
        }
        return this.readSeparator(n);
    }

    private int getNextNonWhiteChar() {
        int n;
        block0: do {
            if ((n = this.getc()) != 47) continue;
            n = this.getc();
            if (n == 47) {
                while ((n = this.getc()) != 10 && n != 13 && n != -1) {
                }
                continue;
            }
            if (n == 42) {
                while ((n = this.getc()) != -1) {
                    if (n != 42) continue;
                    n = this.getc();
                    if (n == 47) {
                        n = 32;
                        continue block0;
                    }
                    this.ungetc(n);
                }
            } else {
                this.ungetc(n);
                n = 47;
            }
        } while (Lex.isBlank(n));
        return n;
    }

    private int readCharConst(Token token) {
        int n;
        int n2 = 0;
        while ((n = this.getc()) != 39) {
            if (n == 92) {
                n2 = this.readEscapeChar();
                continue;
            }
            if (n < 32) {
                if (n == 10) {
                    ++this.lineNumber;
                }
                return 500;
            }
            n2 = n;
        }
        token.longValue = n2;
        return 401;
    }

    private int readEscapeChar() {
        int n = this.getc();
        if (n == 110) {
            n = 10;
        } else if (n == 116) {
            n = 9;
        } else if (n == 114) {
            n = 13;
        } else if (n == 102) {
            n = 12;
        } else if (n == 10) {
            ++this.lineNumber;
        }
        return n;
    }

    private int readStringL(Token token) {
        int n;
        StringBuilder stringBuilder = this.textBuffer;
        stringBuilder.setLength(0);
        while (true) {
            if ((n = this.getc()) != 34) {
                if (n == 92) {
                    n = this.readEscapeChar();
                } else if (n == 10 || n < 0) {
                    ++this.lineNumber;
                    return 500;
                }
                stringBuilder.append((char)n);
                continue;
            }
            while (true) {
                if ((n = this.getc()) == 10) {
                    ++this.lineNumber;
                    continue;
                }
                if (!Lex.isBlank(n)) break;
            }
            if (n != 34) break;
        }
        this.ungetc(n);
        token.textValue = stringBuilder.toString();
        return 406;
    }

    private int readNumber(int n, Token token) {
        long l = 0L;
        int n2 = this.getc();
        if (n == 48) {
            if (n2 == 88 || n2 == 120) {
                while (true) {
                    if (48 <= (n = this.getc()) && n <= 57) {
                        l = l * 16L + (long)(n - 48);
                        continue;
                    }
                    if (65 <= n && n <= 70) {
                        l = l * 16L + (long)(n - 65 + 10);
                        continue;
                    }
                    if (97 > n || n > 102) break;
                    l = l * 16L + (long)(n - 97 + 10);
                }
                token.longValue = l;
                if (n == 76 || n == 108) {
                    return 403;
                }
                this.ungetc(n);
                return 402;
            }
            if (48 <= n2 && n2 <= 55) {
                l = n2 - 48;
                while (48 <= (n = this.getc()) && n <= 55) {
                    l = l * 8L + (long)(n - 48);
                }
                token.longValue = l;
                if (n == 76 || n == 108) {
                    return 403;
                }
                this.ungetc(n);
                return 402;
            }
        }
        l = n - 48;
        while (48 <= n2 && n2 <= 57) {
            l = l * 10L + (long)n2 - 48L;
            n2 = this.getc();
        }
        token.longValue = l;
        if (n2 == 70 || n2 == 102) {
            token.doubleValue = l;
            return 404;
        }
        if (n2 == 69 || n2 == 101 || n2 == 68 || n2 == 100 || n2 == 46) {
            StringBuilder stringBuilder = this.textBuffer;
            stringBuilder.setLength(0);
            stringBuilder.append(l);
            return this.readDouble(stringBuilder, n2, token);
        }
        if (n2 == 76 || n2 == 108) {
            return 403;
        }
        this.ungetc(n2);
        return 402;
    }

    private int readDouble(StringBuilder stringBuilder, int n, Token token) {
        if (n != 69 && n != 101 && n != 68 && n != 100) {
            stringBuilder.append((char)n);
            while (48 <= (n = this.getc()) && n <= 57) {
                stringBuilder.append((char)n);
            }
        }
        if (n == 69 || n == 101) {
            stringBuilder.append((char)n);
            n = this.getc();
            if (n == 43 || n == 45) {
                stringBuilder.append((char)n);
                n = this.getc();
            }
            while (48 <= n && n <= 57) {
                stringBuilder.append((char)n);
                n = this.getc();
            }
        }
        try {
            token.doubleValue = Double.parseDouble(stringBuilder.toString());
        } catch (NumberFormatException numberFormatException) {
            return 500;
        }
        if (n == 70 || n == 102) {
            return 404;
        }
        if (n != 68 && n != 100) {
            this.ungetc(n);
        }
        return 405;
    }

    private int readSeparator(int n) {
        int n2;
        if (33 <= n && n <= 63) {
            int n3 = equalOps[n - 33];
            if (n3 == 0) {
                return n;
            }
            n2 = this.getc();
            if (n == n2) {
                switch (n) {
                    case 61: {
                        return 358;
                    }
                    case 43: {
                        return 362;
                    }
                    case 45: {
                        return 363;
                    }
                    case 38: {
                        return 369;
                    }
                    case 60: {
                        int n4 = this.getc();
                        if (n4 == 61) {
                            return 365;
                        }
                        this.ungetc(n4);
                        return 364;
                    }
                    case 62: {
                        int n5 = this.getc();
                        if (n5 == 61) {
                            return 367;
                        }
                        if (n5 == 62) {
                            n5 = this.getc();
                            if (n5 == 61) {
                                return 371;
                            }
                            this.ungetc(n5);
                            return 370;
                        }
                        this.ungetc(n5);
                        return 366;
                    }
                }
            } else if (n2 == 61) {
                return n3;
            }
        } else if (n == 94) {
            n2 = this.getc();
            if (n2 == 61) {
                return 360;
            }
        } else if (n == 124) {
            n2 = this.getc();
            if (n2 == 61) {
                return 361;
            }
            if (n2 == 124) {
                return 368;
            }
        } else {
            return n;
        }
        this.ungetc(n2);
        return n;
    }

    private int readIdentifier(int n, Token token) {
        StringBuilder stringBuilder = this.textBuffer;
        stringBuilder.setLength(0);
        do {
            stringBuilder.append((char)n);
        } while (Character.isJavaIdentifierPart((char)(n = this.getc())));
        this.ungetc(n);
        String string = stringBuilder.toString();
        int n2 = ktable.lookup(string);
        if (n2 >= 0) {
            return n2;
        }
        token.textValue = string;
        return 400;
    }

    private static boolean isBlank(int n) {
        return n == 32 || n == 9 || n == 12 || n == 13 || n == 10;
    }

    private static boolean isDigit(int n) {
        return 48 <= n && n <= 57;
    }

    private void ungetc(int n) {
        this.lastChar = n;
    }

    public String getTextAround() {
        int n;
        int n2 = this.position - 10;
        if (n2 < 0) {
            n2 = 0;
        }
        if ((n = this.position + 10) > this.maxlen) {
            n = this.maxlen;
        }
        return this.input.substring(n2, n);
    }

    private int getc() {
        if (this.lastChar < 0) {
            if (this.position < this.maxlen) {
                return this.input.charAt(this.position++);
            }
            return -1;
        }
        int n = this.lastChar;
        this.lastChar = -1;
        return n;
    }

    static {
        ktable.append("abstract", 300);
        ktable.append("boolean", 301);
        ktable.append("break", 302);
        ktable.append("byte", 303);
        ktable.append("case", 304);
        ktable.append("catch", 305);
        ktable.append("char", 306);
        ktable.append("class", 307);
        ktable.append("const", 308);
        ktable.append("continue", 309);
        ktable.append("default", 310);
        ktable.append("do", 311);
        ktable.append("double", 312);
        ktable.append("else", 313);
        ktable.append("extends", 314);
        ktable.append("false", 411);
        ktable.append("final", 315);
        ktable.append("finally", 316);
        ktable.append("float", 317);
        ktable.append("for", 318);
        ktable.append("goto", 319);
        ktable.append("if", 320);
        ktable.append("implements", 321);
        ktable.append("import", 322);
        ktable.append("instanceof", 323);
        ktable.append("int", 324);
        ktable.append("interface", 325);
        ktable.append("long", 326);
        ktable.append("native", 327);
        ktable.append("new", 328);
        ktable.append("null", 412);
        ktable.append("package", 329);
        ktable.append("private", 330);
        ktable.append("protected", 331);
        ktable.append("public", 332);
        ktable.append("return", 333);
        ktable.append("short", 334);
        ktable.append("static", 335);
        ktable.append("strictfp", 347);
        ktable.append("super", 336);
        ktable.append("switch", 337);
        ktable.append("synchronized", 338);
        ktable.append("this", 339);
        ktable.append("throw", 340);
        ktable.append("throws", 341);
        ktable.append("transient", 342);
        ktable.append("true", 410);
        ktable.append("try", 343);
        ktable.append("void", 344);
        ktable.append("volatile", 345);
        ktable.append("while", 346);
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.Reader;
import java.io.StringReader;
import org.hjson.HjsonDsf;
import org.hjson.HjsonOptions;
import org.hjson.IHjsonDsfProvider;
import org.hjson.JsonArray;
import org.hjson.JsonNumber;
import org.hjson.JsonObject;
import org.hjson.JsonString;
import org.hjson.JsonValue;
import org.hjson.ParseException;

class HjsonParser {
    private static final int MIN_BUFFER_SIZE = 10;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final String buffer;
    private Reader reader;
    private int index;
    private int line;
    private int lineOffset;
    private int current;
    private StringBuilder captureBuffer;
    private StringBuilder peek;
    private boolean capture;
    private boolean legacyRoot;
    private static final int MAX_DEPTH = 1000;
    private IHjsonDsfProvider[] dsfProviders;

    HjsonParser(String string, HjsonOptions hjsonOptions) {
        this.buffer = string;
        this.reset();
        if (hjsonOptions != null) {
            this.dsfProviders = hjsonOptions.getDsfProviders();
            this.legacyRoot = hjsonOptions.getParseLegacyRoot();
        } else {
            this.dsfProviders = new IHjsonDsfProvider[0];
            this.legacyRoot = true;
        }
    }

    HjsonParser(Reader reader, HjsonOptions hjsonOptions) {
        this(HjsonParser.readToEnd(reader), hjsonOptions);
    }

    static String readToEnd(Reader reader) {
        int n;
        char[] cArray = new char[8192];
        StringBuilder stringBuilder = new StringBuilder();
        while ((n = reader.read(cArray, 0, cArray.length)) != -1) {
            stringBuilder.append(cArray, 0, n);
        }
        return stringBuilder.toString();
    }

    void reset() {
        this.current = 0;
        this.lineOffset = 0;
        this.index = 0;
        this.line = 1;
        this.peek = new StringBuilder();
        this.reader = new StringReader(this.buffer);
        this.capture = false;
        this.captureBuffer = null;
    }

    JsonValue parse() {
        this.read();
        this.skipWhiteSpace();
        if (this.legacyRoot) {
            switch (this.current) {
                case 91: 
                case 123: {
                    return this.checkTrailing(this.readValue());
                }
            }
            try {
                return this.checkTrailing(this.readObject(true));
            } catch (Exception exception) {
                this.reset();
                this.read();
                this.skipWhiteSpace();
                try {
                    return this.checkTrailing(this.readValue());
                } catch (Exception exception2) {
                    throw exception;
                }
            }
        }
        return this.checkTrailing(this.readValue());
    }

    JsonValue checkTrailing(JsonValue jsonValue) {
        this.skipWhiteSpace();
        if (!this.isEndOfText()) {
            throw this.error("Extra characters in input: " + this.current);
        }
        return jsonValue;
    }

    private JsonValue readValue() {
        return this.readValue(0);
    }

    private JsonValue readValue(int n) {
        if (n > 1000) {
            throw this.error("The passed json has exhausted the depth supported of 1000.");
        }
        switch (this.current) {
            case 34: 
            case 39: {
                return this.readString();
            }
            case 91: {
                return this.readArray(n + 1);
            }
            case 123: {
                return this.readObject(false, n + 1);
            }
        }
        return this.readTfnns();
    }

    private JsonValue readTfnns() {
        StringBuilder stringBuilder = new StringBuilder();
        int n = this.current;
        if (JsonValue.isPunctuatorChar(n)) {
            throw this.error("Found a punctuator character '" + (char)n + "' when expecting a quoteless string (check your syntax)");
        }
        stringBuilder.append((char)this.current);
        while (true) {
            boolean bl;
            this.read();
            boolean bl2 = bl = this.current < 0 || this.current == 13 || this.current == 10;
            if (bl || this.current == 44 || this.current == 125 || this.current == 93 || this.current == 35 || this.current == 47 && (this.peek() == 47 || this.peek() == 42)) {
                switch (n) {
                    case 102: 
                    case 110: 
                    case 116: {
                        String string = stringBuilder.toString().trim();
                        if (string.equals("false")) {
                            return JsonValue.FALSE;
                        }
                        if (string.equals("null")) {
                            return JsonValue.NULL;
                        }
                        if (!string.equals("true")) break;
                        return JsonValue.TRUE;
                    }
                    default: {
                        JsonValue jsonValue;
                        if (n != 45 && (n < 48 || n > 57) || (jsonValue = HjsonParser.tryParseNumber(stringBuilder, false)) == null) break;
                        return jsonValue;
                    }
                }
                if (bl) {
                    return HjsonDsf.parse(this.dsfProviders, stringBuilder.toString().trim());
                }
            }
            stringBuilder.append((char)this.current);
        }
    }

    private JsonArray readArray(int n) {
        JsonArray jsonArray;
        block3: {
            this.read();
            jsonArray = new JsonArray();
            this.skipWhiteSpace();
            if (this.readIf(']')) {
                return jsonArray;
            }
            do {
                this.skipWhiteSpace();
                jsonArray.add(this.readValue(n));
                this.skipWhiteSpace();
                if (this.readIf(',')) {
                    this.skipWhiteSpace();
                }
                if (this.readIf(']')) break block3;
            } while (!this.isEndOfText());
            throw this.error("End of input while parsing an array (did you forget a closing ']'?)");
        }
        return jsonArray;
    }

    private JsonObject readObject(boolean bl) {
        return this.readObject(bl, 0);
    }

    private JsonObject readObject(boolean bl, int n) {
        if (!bl) {
            this.read();
        }
        JsonObject jsonObject = new JsonObject();
        this.skipWhiteSpace();
        while (true) {
            if (bl) {
                if (this.isEndOfText()) {
                    break;
                }
            } else {
                if (this.isEndOfText()) {
                    throw this.error("End of input while parsing an object (did you forget a closing '}'?)");
                }
                if (this.readIf('}')) break;
            }
            String string = this.readName();
            this.skipWhiteSpace();
            if (!this.readIf(':')) {
                throw this.expected("':'");
            }
            this.skipWhiteSpace();
            jsonObject.add(string, this.readValue(n));
            this.skipWhiteSpace();
            if (!this.readIf(',')) continue;
            this.skipWhiteSpace();
        }
        return jsonObject;
    }

    private String readName() {
        if (this.current == 34 || this.current == 39) {
            return this.readStringInternal(false);
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n = -1;
        int n2 = this.index;
        while (true) {
            if (this.current == 58) {
                if (stringBuilder.length() == 0) {
                    throw this.error("Found ':' but no key name (for an empty key name use quotes)");
                }
                if (n >= 0 && n != stringBuilder.length()) {
                    this.index = n2 + n;
                    throw this.error("Found whitespace in your key name (use quotes to include)");
                }
                return stringBuilder.toString();
            }
            if (HjsonParser.isWhiteSpace(this.current)) {
                if (n < 0) {
                    n = stringBuilder.length();
                }
            } else {
                if (this.current < 32) {
                    throw this.error("Name is not closed");
                }
                if (JsonValue.isPunctuatorChar(this.current)) {
                    throw this.error("Found '" + (char)this.current + "' where a key name was expected (check your syntax or use quotes if the key name includes {}[],: or whitespace)");
                }
                stringBuilder.append((char)this.current);
            }
            this.read();
        }
    }

    private String readMlString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        int n2 = this.index - this.lineOffset - 4;
        while (HjsonParser.isWhiteSpace(this.current) && this.current != 10) {
            this.read();
        }
        if (this.current == 10) {
            this.read();
            this.skipIndent(n2);
        }
        while (true) {
            if (this.current < 0) {
                throw this.error("Bad multiline string");
            }
            if (this.current == 39) {
                this.read();
                if (++n != 3) continue;
                if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                return stringBuilder.toString();
            }
            while (n > 0) {
                stringBuilder.append('\'');
                --n;
            }
            if (this.current == 10) {
                stringBuilder.append('\n');
                this.read();
                this.skipIndent(n2);
                continue;
            }
            if (this.current != 13) {
                stringBuilder.append((char)this.current);
            }
            this.read();
        }
    }

    private void skipIndent(int n) {
        while (n-- > 0 && HjsonParser.isWhiteSpace(this.current) && this.current != 10) {
            this.read();
        }
    }

    private JsonValue readString() {
        return new JsonString(this.readStringInternal(true));
    }

    private String readStringInternal(boolean bl) {
        int n = this.current;
        this.read();
        this.startCapture();
        while (this.current != n) {
            if (this.current == 92) {
                this.readEscape();
                continue;
            }
            if (this.current < 32) {
                throw this.expected("valid string character");
            }
            this.read();
        }
        String string = this.endCapture();
        this.read();
        if (bl && n == 39 && this.current == 39 && string.length() == 0) {
            this.read();
            return this.readMlString();
        }
        return string;
    }

    private void readEscape() {
        this.pauseCapture();
        this.read();
        switch (this.current) {
            case 34: 
            case 39: 
            case 47: 
            case 92: {
                this.captureBuffer.append((char)this.current);
                break;
            }
            case 98: {
                this.captureBuffer.append('\b');
                break;
            }
            case 102: {
                this.captureBuffer.append('\f');
                break;
            }
            case 110: {
                this.captureBuffer.append('\n');
                break;
            }
            case 114: {
                this.captureBuffer.append('\r');
                break;
            }
            case 116: {
                this.captureBuffer.append('\t');
                break;
            }
            case 117: {
                char[] cArray = new char[4];
                for (int i = 0; i < 4; ++i) {
                    this.read();
                    if (!this.isHexDigit()) {
                        throw this.expected("hexadecimal digit");
                    }
                    cArray[i] = (char)this.current;
                }
                this.captureBuffer.append((char)Integer.parseInt(new String(cArray), 16));
                break;
            }
            default: {
                throw this.expected("valid escape sequence");
            }
        }
        this.capture = true;
        this.read();
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    static JsonValue tryParseNumber(StringBuilder stringBuilder, boolean bl) {
        char c;
        char c2;
        int n = 0;
        int n2 = stringBuilder.length();
        if (n < n2 && stringBuilder.charAt(n) == '-') {
            ++n;
        }
        if (n >= n2) {
            return null;
        }
        if (!HjsonParser.isDigit(c2 = stringBuilder.charAt(n++))) {
            return null;
        }
        if (c2 == '0' && n < n2 && HjsonParser.isDigit(stringBuilder.charAt(n))) {
            return null;
        }
        while (n < n2 && HjsonParser.isDigit(stringBuilder.charAt(n))) {
            ++n;
        }
        if (n < n2 && stringBuilder.charAt(n) == '.') {
            if (++n >= n2 || !HjsonParser.isDigit(stringBuilder.charAt(n++))) {
                return null;
            }
            while (n < n2 && HjsonParser.isDigit(stringBuilder.charAt(n))) {
                ++n;
            }
        }
        if (n < n2 && Character.toLowerCase(stringBuilder.charAt(n)) == 'e') {
            if (++n < n2 && (stringBuilder.charAt(n) == '+' || stringBuilder.charAt(n) == '-')) {
                ++n;
            }
            if (n >= n2 || !HjsonParser.isDigit(stringBuilder.charAt(n++))) {
                return null;
            }
            while (n < n2 && HjsonParser.isDigit(stringBuilder.charAt(n))) {
                ++n;
            }
        }
        int n3 = n;
        while (n < n2 && HjsonParser.isWhiteSpace(stringBuilder.charAt(n))) {
            ++n;
        }
        boolean bl2 = false;
        if (n < n2 && bl && ((c = stringBuilder.charAt(n)) == ',' || c == '}' || c == ']' || c == '#' || c == '/' && n2 > n + 1 && (stringBuilder.charAt(n + 1) == '/' || stringBuilder.charAt(n + 1) == '*'))) {
            bl2 = true;
        }
        if (n < n2 && !bl2) {
            return null;
        }
        return new JsonNumber(Double.parseDouble(stringBuilder.substring(0, n3)));
    }

    static JsonValue tryParseNumber(String string, boolean bl) {
        return HjsonParser.tryParseNumber(new StringBuilder(string), bl);
    }

    private boolean readIf(char c) {
        if (this.current != c) {
            return false;
        }
        this.read();
        return true;
    }

    private void skipWhiteSpace() {
        while (!this.isEndOfText()) {
            while (this.isWhiteSpace()) {
                this.read();
            }
            if (this.current == 35 || this.current == 47 && this.peek() == 47) {
                do {
                    this.read();
                } while (this.current >= 0 && this.current != 10);
                continue;
            }
            if (this.current != 47 || this.peek() != 42) break;
            this.read();
            do {
                this.read();
            } while (this.current >= 0 && (this.current != 42 || this.peek() != 47));
            this.read();
            this.read();
        }
    }

    private int peek(int n) {
        while (n >= this.peek.length()) {
            int n2 = this.reader.read();
            if (n2 < 0) {
                return n2;
            }
            this.peek.append((char)n2);
        }
        return this.peek.charAt(n);
    }

    private int peek() {
        return this.peek(0);
    }

    private boolean read() {
        if (this.current == 10) {
            ++this.line;
            this.lineOffset = this.index;
        }
        if (this.peek.length() > 0) {
            this.current = this.peek.charAt(0);
            this.peek.deleteCharAt(0);
        } else {
            this.current = this.reader.read();
        }
        if (this.current < 0) {
            return false;
        }
        ++this.index;
        if (this.capture) {
            this.captureBuffer.append((char)this.current);
        }
        return true;
    }

    private void startCapture() {
        if (this.captureBuffer == null) {
            this.captureBuffer = new StringBuilder();
        }
        this.capture = true;
        this.captureBuffer.append((char)this.current);
    }

    private void pauseCapture() {
        int n = this.captureBuffer.length();
        if (n > 0) {
            this.captureBuffer.deleteCharAt(n - 1);
        }
        this.capture = false;
    }

    private String endCapture() {
        String string;
        this.pauseCapture();
        if (this.captureBuffer.length() > 0) {
            string = this.captureBuffer.toString();
            this.captureBuffer.setLength(0);
        } else {
            string = "";
        }
        this.capture = false;
        return string;
    }

    private ParseException expected(String string) {
        if (this.isEndOfText()) {
            return this.error("Unexpected end of input");
        }
        return this.error("Expected " + string);
    }

    private ParseException error(String string) {
        int n = this.index - this.lineOffset;
        int n2 = this.isEndOfText() ? this.index : this.index - 1;
        return new ParseException(string, n2, this.line, n - 1);
    }

    static boolean isWhiteSpace(int n) {
        return n == 32 || n == 9 || n == 10 || n == 13;
    }

    private boolean isWhiteSpace() {
        return HjsonParser.isWhiteSpace((char)this.current);
    }

    private boolean isHexDigit() {
        return this.current >= 48 && this.current <= 57 || this.current >= 97 && this.current <= 102 || this.current >= 65 && this.current <= 70;
    }

    private boolean isEndOfText() {
        return this.current == -1;
    }
}


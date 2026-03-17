/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.Reader;
import java.io.StringReader;
import org.hjson.JsonArray;
import org.hjson.JsonNumber;
import org.hjson.JsonObject;
import org.hjson.JsonString;
import org.hjson.JsonValue;
import org.hjson.ParseException;

class JsonParser {
    private static final int MIN_BUFFER_SIZE = 10;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Reader reader;
    private final char[] buffer;
    private int bufferOffset;
    private int index;
    private int fill;
    private int line;
    private int lineOffset;
    private int current;
    private StringBuilder captureBuffer;
    private int captureStart;
    private static final int MAX_DEPTH = 1000;

    JsonParser(String string) {
        this(new StringReader(string), Math.max(10, Math.min(1024, string.length())));
    }

    JsonParser(Reader reader) {
        this(reader, 1024);
    }

    JsonParser(Reader reader, int n) {
        this.reader = reader;
        this.buffer = new char[n];
        this.line = 1;
        this.captureStart = -1;
    }

    JsonValue parse() {
        this.read();
        this.skipWhiteSpace();
        JsonValue jsonValue = this.readValue();
        this.skipWhiteSpace();
        if (!this.isEndOfText()) {
            throw this.error("Unexpected character");
        }
        return jsonValue;
    }

    private JsonValue readValue() {
        return this.readValue(0);
    }

    private JsonValue readValue(int n) {
        if (n > 1000) {
            throw this.error("The passed json has exhausted the maximum supported depth of 1000.");
        }
        switch (this.current) {
            case 110: {
                return this.readNull();
            }
            case 116: {
                return this.readTrue();
            }
            case 102: {
                return this.readFalse();
            }
            case 34: {
                return this.readString();
            }
            case 91: {
                return this.readArray(n + 1);
            }
            case 123: {
                return this.readObject(n + 1);
            }
            case 45: 
            case 48: 
            case 49: 
            case 50: 
            case 51: 
            case 52: 
            case 53: 
            case 54: 
            case 55: 
            case 56: 
            case 57: {
                return this.readNumber();
            }
        }
        throw this.expected("value");
    }

    private JsonArray readArray(int n) {
        this.read();
        JsonArray jsonArray = new JsonArray();
        this.skipWhiteSpace();
        if (this.readIf(']')) {
            return jsonArray;
        }
        do {
            this.skipWhiteSpace();
            jsonArray.add(this.readValue(n));
            this.skipWhiteSpace();
        } while (this.readIf(','));
        if (!this.readIf(']')) {
            throw this.expected("',' or ']'");
        }
        return jsonArray;
    }

    private JsonObject readObject(int n) {
        this.read();
        JsonObject jsonObject = new JsonObject();
        this.skipWhiteSpace();
        if (this.readIf('}')) {
            return jsonObject;
        }
        do {
            this.skipWhiteSpace();
            String string = this.readName();
            this.skipWhiteSpace();
            if (!this.readIf(':')) {
                throw this.expected("':'");
            }
            this.skipWhiteSpace();
            jsonObject.add(string, this.readValue(n));
            this.skipWhiteSpace();
        } while (this.readIf(','));
        if (!this.readIf('}')) {
            throw this.expected("',' or '}'");
        }
        return jsonObject;
    }

    private String readName() {
        if (this.current != 34) {
            throw this.expected("name");
        }
        return this.readStringInternal();
    }

    private JsonValue readNull() {
        this.read();
        this.readRequiredChar('u');
        this.readRequiredChar('l');
        this.readRequiredChar('l');
        return JsonValue.NULL;
    }

    private JsonValue readTrue() {
        this.read();
        this.readRequiredChar('r');
        this.readRequiredChar('u');
        this.readRequiredChar('e');
        return JsonValue.TRUE;
    }

    private JsonValue readFalse() {
        this.read();
        this.readRequiredChar('a');
        this.readRequiredChar('l');
        this.readRequiredChar('s');
        this.readRequiredChar('e');
        return JsonValue.FALSE;
    }

    private void readRequiredChar(char c) {
        if (!this.readIf(c)) {
            throw this.expected("'" + c + "'");
        }
    }

    private JsonValue readString() {
        return new JsonString(this.readStringInternal());
    }

    private String readStringInternal() {
        this.read();
        this.startCapture();
        while (this.current != 34) {
            if (this.current == 92) {
                this.pauseCapture();
                this.readEscape();
                this.startCapture();
                continue;
            }
            if (this.current < 32) {
                throw this.expected("valid string character");
            }
            this.read();
        }
        String string = this.endCapture();
        this.read();
        return string;
    }

    private void readEscape() {
        this.read();
        switch (this.current) {
            case 34: 
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
        this.read();
    }

    private JsonValue readNumber() {
        this.startCapture();
        this.readIf('-');
        int n = this.current;
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        if (n != 48) {
            while (this.readDigit()) {
            }
        }
        this.readFraction();
        this.readExponent();
        return new JsonNumber(Double.parseDouble(this.endCapture()));
    }

    private boolean readFraction() {
        if (!this.readIf('.')) {
            return false;
        }
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        while (this.readDigit()) {
        }
        return true;
    }

    private boolean readExponent() {
        if (!this.readIf('e') && !this.readIf('E')) {
            return false;
        }
        if (!this.readIf('+')) {
            this.readIf('-');
        }
        if (!this.readDigit()) {
            throw this.expected("digit");
        }
        while (this.readDigit()) {
        }
        return true;
    }

    private boolean readIf(char c) {
        if (this.current != c) {
            return false;
        }
        this.read();
        return true;
    }

    private boolean readDigit() {
        if (!this.isDigit()) {
            return false;
        }
        this.read();
        return true;
    }

    private void skipWhiteSpace() {
        while (this.isWhiteSpace()) {
            this.read();
        }
    }

    private void read() {
        if (this.index == this.fill) {
            if (this.captureStart != -1) {
                this.captureBuffer.append(this.buffer, this.captureStart, this.fill - this.captureStart);
                this.captureStart = 0;
            }
            this.bufferOffset += this.fill;
            this.fill = this.reader.read(this.buffer, 0, this.buffer.length);
            this.index = 0;
            if (this.fill == -1) {
                this.current = -1;
                return;
            }
        }
        if (this.current == 10) {
            ++this.line;
            this.lineOffset = this.bufferOffset + this.index;
        }
        this.current = this.buffer[this.index++];
    }

    private void startCapture() {
        if (this.captureBuffer == null) {
            this.captureBuffer = new StringBuilder();
        }
        this.captureStart = this.index - 1;
    }

    private void pauseCapture() {
        int n = this.current == -1 ? this.index : this.index - 1;
        this.captureBuffer.append(this.buffer, this.captureStart, n - this.captureStart);
        this.captureStart = -1;
    }

    private String endCapture() {
        String string;
        int n;
        int n2 = n = this.current == -1 ? this.index : this.index - 1;
        if (this.captureBuffer.length() > 0) {
            this.captureBuffer.append(this.buffer, this.captureStart, n - this.captureStart);
            string = this.captureBuffer.toString();
            this.captureBuffer.setLength(0);
        } else {
            string = new String(this.buffer, this.captureStart, n - this.captureStart);
        }
        this.captureStart = -1;
        return string;
    }

    private ParseException expected(String string) {
        if (this.isEndOfText()) {
            return this.error("Unexpected end of input");
        }
        return this.error("Expected " + string);
    }

    private ParseException error(String string) {
        int n = this.bufferOffset + this.index;
        int n2 = n - this.lineOffset;
        int n3 = this.isEndOfText() ? n : n - 1;
        return new ParseException(string, n3, this.line, n2 - 1);
    }

    private boolean isWhiteSpace() {
        return this.current == 32 || this.current == 9 || this.current == 10 || this.current == 13;
    }

    private boolean isDigit() {
        return this.current >= 48 && this.current <= 57;
    }

    private boolean isHexDigit() {
        return this.current >= 48 && this.current <= 57 || this.current >= 97 && this.current <= 102 || this.current >= 65 && this.current <= 70;
    }

    private boolean isEndOfText() {
        return this.current == -1;
    }
}


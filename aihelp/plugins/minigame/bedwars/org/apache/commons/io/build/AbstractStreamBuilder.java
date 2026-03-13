/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.build;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.build.AbstractOriginSupplier;
import org.apache.commons.io.file.PathUtils;

public abstract class AbstractStreamBuilder<T, B extends AbstractStreamBuilder<T, B>>
extends AbstractOriginSupplier<T, B> {
    private static final OpenOption[] DEFAULT_OPEN_OPTIONS = PathUtils.EMPTY_OPEN_OPTION_ARRAY;
    private int bufferSize = 8192;
    private int bufferSizeDefault = 8192;
    private Charset charset = Charset.defaultCharset();
    private Charset charsetDefault = Charset.defaultCharset();
    private OpenOption[] openOptions = DEFAULT_OPEN_OPTIONS;

    protected int getBufferSize() {
        return this.bufferSize;
    }

    protected int getBufferSizeDefault() {
        return this.bufferSizeDefault;
    }

    protected CharSequence getCharSequence() throws IOException {
        return this.checkOrigin().getCharSequence(this.getCharset());
    }

    public Charset getCharset() {
        return this.charset;
    }

    protected Charset getCharsetDefault() {
        return this.charsetDefault;
    }

    protected InputStream getInputStream() throws IOException {
        return this.checkOrigin().getInputStream(this.getOpenOptions());
    }

    protected OpenOption[] getOpenOptions() {
        return this.openOptions;
    }

    protected OutputStream getOutputStream() throws IOException {
        return this.checkOrigin().getOutputStream(this.getOpenOptions());
    }

    protected Path getPath() {
        return this.checkOrigin().getPath();
    }

    protected Writer getWriter() throws IOException {
        return this.checkOrigin().getWriter(this.getCharset(), this.getOpenOptions());
    }

    public B setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize > 0 ? bufferSize : this.bufferSizeDefault;
        return (B)((AbstractStreamBuilder)this.asThis());
    }

    public B setBufferSize(Integer bufferSize) {
        this.setBufferSize(bufferSize != null ? bufferSize : this.bufferSizeDefault);
        return (B)((AbstractStreamBuilder)this.asThis());
    }

    protected B setBufferSizeDefault(int bufferSizeDefault) {
        this.bufferSizeDefault = bufferSizeDefault;
        return (B)((AbstractStreamBuilder)this.asThis());
    }

    public B setCharset(Charset charset) {
        this.charset = Charsets.toCharset(charset, this.charsetDefault);
        return (B)((AbstractStreamBuilder)this.asThis());
    }

    public B setCharset(String charset) {
        return this.setCharset(Charsets.toCharset(charset, this.charsetDefault));
    }

    protected B setCharsetDefault(Charset defaultCharset) {
        this.charsetDefault = defaultCharset;
        return (B)((AbstractStreamBuilder)this.asThis());
    }

    public B setOpenOptions(OpenOption ... openOptions) {
        this.openOptions = openOptions != null ? openOptions : DEFAULT_OPEN_OPTIONS;
        return (B)((AbstractStreamBuilder)this.asThis());
    }
}


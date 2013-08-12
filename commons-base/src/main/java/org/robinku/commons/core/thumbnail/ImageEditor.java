package org.robinku.commons.core.thumbnail;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface ImageEditor {
    public int getWidth();

    public int getHeight();

    public String getFormat();

    /**
     * 剪切图片
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void cut(int x, int y, int w, int h);

    /**
     * 缩放图片
     * @param width
     * @param height
     * @throws IOException
     */
    public void scale(int width, int height) throws IOException;

    public void write(File file) throws IOException;

    public void write(File file, String format) throws IOException;

    public void write(OutputStream outputStream) throws IOException;

    public void write(OutputStream outputStream, String format) throws IOException;

    public byte[] getBytes() throws IOException;
}

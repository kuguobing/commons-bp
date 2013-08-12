package org.robinku.commons.core.thumbnail;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.sun.imageio.plugins.bmp.BMPImageWriter;
import com.sun.imageio.plugins.bmp.BMPImageWriterSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

/**
 * @author: Ku Guobing
 * @Email: kuguobing@gmail.com
 * @Date: 2010-3-31
 */
@SuppressWarnings("restriction")
public class AllEditor implements ImageEditor {
    private BufferedImage image = null;

    private String format;

    protected AllEditor() {
    }

    protected void setInputStream(InputStream inputStream) throws IOException {
        image = ImageIO.read(inputStream);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    protected void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public void cut(int x, int y, int w, int h) {
        BufferedImage newImage = image.getSubimage(x, y, w, h);
        image = newImage;
    }

    public void scale(int width, int height) throws IOException {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB /*image.getType()*/);
        Graphics g = newImage.getGraphics();
        g.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        image = newImage;
    }

    private void save(OutputStream outputStream, ImageWriter imageWriter) throws IOException {
        MemoryCacheImageOutputStream mios = new MemoryCacheImageOutputStream(outputStream);
        imageWriter.setOutput(mios);
        imageWriter.write(image);
        imageWriter.dispose();
        mios.close();
    }

    public void write(File file) throws IOException {
        write(file, format);
    }

    public void write(File file, String formatName) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        write(fos, formatName);
        fos.close();
    }

    public void write(OutputStream outputStream) throws IOException {
        write(outputStream, format);
    }

    public void write(OutputStream outputStream, String formatName) throws IOException {
        if ("PNG".equalsIgnoreCase(formatName))
            save(outputStream, new PNGImageWriter(new PNGImageWriterSpi()));
        else if ("JPG".equalsIgnoreCase(formatName) || "JPEG".equalsIgnoreCase(formatName))
            save(outputStream, new JPEGImageWriter(new JPEGImageWriterSpi()));
        else if ("BMP".equalsIgnoreCase(formatName))
            save(outputStream, new BMPImageWriter(new BMPImageWriterSpi()));
        else
            throw new UnsupportedOperationException("No supported image format(" + formatName + ").");
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        write(byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bytes;
    }
}

package org.robinku.commons.core.thumbnail;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.sun.imageio.plugins.bmp.BMPImageWriter;
import com.sun.imageio.plugins.bmp.BMPImageWriterSpi;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
//WARN: Must using com.sun.imageio.plugins in JDK1.6+
import com.sun.imageio.plugins.gif.GIFImageWriter;
import com.sun.imageio.plugins.gif.GIFImageWriterSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

@SuppressWarnings("restriction")
public class GifEditor implements ImageEditor {
    private ByteArrayInputStream byteArrayInputStream;

    private ByteArrayOutputStream byteArrayOutputStream;

    private ImageInputStream imageInputStream;

    private BufferedImage image = null;

    private static final String format = "GIF";

    protected GifEditor() {
    }

    public String getFormat() {
        return format;
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getWidth() {
        return image.getWidth();
    }

    protected void setInputStream(InputStream inputStream) throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10 * 1024];
        int count = 0;
        do {
            byteArrayOutputStream.write(buffer, 0, count);
            count = inputStream.read(buffer, 0, buffer.length);
        } while (count != -1);
        inputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        image = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.reset();
    }

    @Override
    public void cut(int x, int y, int w, int h) {
        BufferedImage newImage = image.getSubimage(x, y, w, h);
        image = newImage;
    }

    public void scale(int width, int height) throws IOException {
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        imageInputStream = new MemoryCacheImageInputStream(byteArrayInputStream);
        byteArrayOutputStream.reset();
        image = resize(image, width, height);
        GIFImageReader imageReader = new GIFImageReader(new GIFImageReaderSpi());
        imageReader.setInput(imageInputStream);
        int numImages = imageReader.getNumImages(true);
        ImageReadParam imageReadParam = imageReader.getDefaultReadParam();

        GIFImageWriter imageWriter = new GIFImageWriter(new GIFImageWriterSpi());
        MemoryCacheImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(byteArrayOutputStream);
        imageWriter.setOutput(imageOutputStream);
        ImageWriteParam imageWriteParam = new ImageWriteParam(Locale.CHINA);
        imageWriter.prepareWriteSequence(imageWriter.getDefaultStreamMetadata(imageWriteParam));

        for (int i = imageReader.getMinIndex(); i < numImages; i++) {
            IIOImage iio = imageReader.readAll(i, imageReadParam);
            RenderedImage renderedImage = iio.getRenderedImage();
            renderedImage = resize((BufferedImage) renderedImage, width, height);
            iio.setRenderedImage(renderedImage);
            imageWriter.writeToSequence(iio, imageWriteParam);
        }
        imageWriter.endWriteSequence();
        imageReader.dispose();
        imageWriter.dispose();
        imageOutputStream.close();
    }

    private BufferedImage resize(BufferedImage oldImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(oldImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return newImage;
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
            outputStream.write(byteArrayOutputStream.toByteArray());
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        write(byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bytes;
    }
}
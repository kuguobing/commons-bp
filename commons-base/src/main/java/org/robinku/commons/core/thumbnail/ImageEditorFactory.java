package org.robinku.commons.core.thumbnail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class ImageEditorFactory {
	private static Pattern supportedFormat = Pattern.compile(
			"BMP|JPE|JPEG|GIF|PNG", Pattern.CASE_INSENSITIVE);

	public static ImageEditor read(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		return read(fis);
	}

	public static ImageEditor read(byte[] bytes) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		String formatName = null;
		ImageInputStream imageInputStream = new MemoryCacheImageInputStream(
				byteArrayInputStream);
		Iterator<?> iter = ImageIO.getImageReaders(imageInputStream);
		if (iter.hasNext()) {
			ImageReader reader = (ImageReader) iter.next();
			formatName = reader.getFormatName().toUpperCase();
		}
		if (formatName == null || !supportedFormat.matcher(formatName).find()) {
			imageInputStream.close();
			throw new UnsupportedOperationException(
					"No supported image format(" + formatName + ").");
		}
		byteArrayInputStream.reset();
		if ("GIF".equalsIgnoreCase(formatName)) {
			GifEditor editor = new GifEditor();
			editor.setInputStream(byteArrayInputStream);
			return editor;
		} else {
			AllEditor editor = new AllEditor();
			editor.setFormat(formatName);
			editor.setInputStream(byteArrayInputStream);
			return editor;
		}
	}

	public static ImageEditor read(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int count = 0;
		do {
			byteArrayOutputStream.write(buffer, 0, count);
			count = inputStream.read(buffer, 0, buffer.length);
		} while (count != -1);
		inputStream.close();
		return read(byteArrayOutputStream.toByteArray());
	}

	public static void main(String[] args) {
		try {
			ImageEditor editor = ImageEditorFactory
					.read(new File("e:/test.jpg"));
//			editor.resize(editor.getWidth() / 2, editor.getHeight() / 2);
//			editor.write(new File("e:/test1.jpg"), "jpg");
//			editor.resize(editor.getWidth() / 2, editor.getHeight() / 2);
//			editor.write(new File("e:/test2.gif"));
			
			editor.cut(10, 10, 400, 500);
			editor.write(new File("e:/test1.png"),"png");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

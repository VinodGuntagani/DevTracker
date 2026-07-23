package service.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PDFThemeLoader {

	private PDFThemeLoader() {
	}

	public static String load() throws IOException {
		System.out.println(PDFThemeLoader.class.getClassLoader().getResource("pdf/pdf-theme.css"));
		try (InputStream is = PDFThemeLoader.class.getClassLoader().getResourceAsStream("pdf/pdf-theme.css")) {

			if (is == null) {
				throw new IOException("pdf-theme.css not found.");
			}

			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
	}
}
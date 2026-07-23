package service.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

public class PDFHtmlCleaner {

	public static String clean(String html) {

		Document document = Jsoup.parse(html);

		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml)
				.prettyPrint(true);

		return document.html();
	}

}
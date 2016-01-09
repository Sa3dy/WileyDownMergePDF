package wileyDownloader;

/* Written by Mostafa Saady, sa3dy_update@yahoo.com, Twitter: @DrSa3dy*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WileyDownMergePDF {

	public static void main(String[] args) throws IOException {

		String url = "";

		Scanner reader = new Scanner(System.in);
		System.out.println("Paste Book URL: ");

		url = reader.nextLine();

		ArrayList<String> pdfsURLs = new ArrayList<String>();
		ArrayList<String> pdfsDirectURLs = new ArrayList<String>();
		ArrayList<String> pdfsDownloadedFiles = new ArrayList<String>();
		print("Fetching from: %s...", url);

		Document doc = Jsoup.connect(url).get();

		Element productTitle = doc.getElementById("productTitle");
		Elements links = doc.select("a[href]");

		for (Element link : links) {
			if (link.attr("abs:href").contains("/pdf")) {
				pdfsURLs.add(link.attr("abs:href"));
			}
		}

		System.out.println("Fetching " + pdfsURLs.size() + " PDF links to download..");

		for (int i = 0; i < pdfsURLs.size(); i++) {

			System.out.println("Fetching the link no.: " + (i + 1));

			Document doc1 = Jsoup.connect(pdfsURLs.get(i)).get();
			Element pdfDirectLink = doc1.getElementById("pdfDocument");

			pdfsDirectURLs.add(pdfDirectLink.attr("src"));

		}

		System.out.println("All PDF links are fetched..");

		File file = new File(
				System.getProperty("user.dir") + "/" + (productTitle.ownText()).replaceAll("[-+.^:,]", ""));
		System.out.println("Creating Directory " + "\"" + file.getPath() + "\"" + " for files..");

		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		String saveDir = System.getProperty("user.dir") + "/" + (productTitle.ownText()).replaceAll("[-+.^:,]", "");

		System.out.println("Downloading the " + pdfsURLs.size() + " PDF files..");

		for (int i = 0; i < pdfsDirectURLs.size(); i++) {

			System.out.println("Downloading the file no.: " + (i + 1));

			downloadFile(pdfsDirectURLs.get(i), saveDir, i + ".pdf");

			System.out.println("File no.: " + (i + 1) + " is successfully downloaded.");

			pdfsDownloadedFiles.add(saveDir + "/" + i + ".pdf");
		}

		System.out.println("All files are downloaded successfully..");

		PDFMergerUtility ut = new PDFMergerUtility();

		System.out.println("Start merging..");

		for (int i = 0; i < pdfsDownloadedFiles.size(); i++) {

			ut.addSource(pdfsDownloadedFiles.get(i));

		}

		ut.setDestinationFileName(
				System.getProperty("user.dir") + "/" + (productTitle.ownText()).replaceAll("[-+.^:,]", "") + ".pdf");
		ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

		System.out.println("All files are merged successfully in the file:");
		System.out.println(ut.getDestinationFileName());

	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	public static void downloadFile(String fileURL, String saveDir, String name) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = name;
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			// System.out.println("Content-Type = " + contentType);
			// System.out.println("Content-Disposition = " + disposition);
			// System.out.println("Content-Length = " + contentLength);
			// System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

			// System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

}

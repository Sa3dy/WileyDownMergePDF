package wileyDownloader;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JScrollPane;

public class WileyDownMergeBySa3dy {

	private JFrame frmFreeWileydownmergePdf;
	private JTextField textFieldBookURL;
	private JLabel lblLogger;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WileyDownMergeBySa3dy window = new WileyDownMergeBySa3dy();
					window.frmFreeWileydownmergePdf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WileyDownMergeBySa3dy() {
		initialize();
	}
	
	public void appendString(JTextPane jTextPane, String string) {
		StyledDocument doc = jTextPane.getStyledDocument();
		
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.GREEN);
		//StyleConstants.setBackground(keyWord, Color.BLACK);
		StyleConstants.setBold(keyWord, true);

		try {
			doc.insertString(doc.getLength(), string + "\n", keyWord);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFreeWileydownmergePdf = new JFrame();
		frmFreeWileydownmergePdf.setResizable(false);
		frmFreeWileydownmergePdf.setTitle("Free WileyDownMerge PDF By Sa3dy");
		frmFreeWileydownmergePdf.setBounds(100, 100, 577, 360);
		frmFreeWileydownmergePdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFreeWileydownmergePdf.getContentPane().setLayout(null);
		
		JLabel lblPutTheBook = new JLabel("Put the Book here:");
		lblPutTheBook.setBounds(10, 11, 551, 14);
		frmFreeWileydownmergePdf.getContentPane().add(lblPutTheBook);
		
		textFieldBookURL = new JTextField();
		textFieldBookURL.setBounds(10, 36, 551, 20);
		frmFreeWileydownmergePdf.getContentPane().add(textFieldBookURL);
		textFieldBookURL.setColumns(10);
		
		lblLogger = new JLabel("Logger:");
		lblLogger.setBounds(10, 101, 551, 14);
		frmFreeWileydownmergePdf.getContentPane().add(lblLogger);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 126, 551, 195);
		frmFreeWileydownmergePdf.getContentPane().add(scrollPane);
		
		JTextPane textPaneLogger = new JTextPane();
		textPaneLogger.setBackground(Color.BLACK);
		appendString(textPaneLogger, "Logger Started...");
		scrollPane.setViewportView(textPaneLogger);
		
		JButton btnTryToDownload = new JButton("Try to Download :D");
		btnTryToDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (!textFieldBookURL.getText().isEmpty()) {
					
					//System.out.println("textFieldBookURL: " + textFieldBookURL.getText());
					
					String url = "http://onlinelibrary.wiley.com/book/10.1002/9781118347584";
			        ArrayList<String> pdfsURLs = new ArrayList<String>();
			        ArrayList<String> pdfsDirectURLs = new ArrayList<String>();
			        ArrayList<String> pdfsDownloadedFiles = new ArrayList<String>();
			        appendString(textPaneLogger, "Fetching..: " + url);

			        Document doc;
					try {
						
						doc = Jsoup.connect(url).get();
						
						Element productTitle = doc.getElementById("productTitle");
				        Elements links = doc.select("a[href]");

				        for (Element link : links) {
				        	if (link.attr("abs:href").contains("/pdf")) {
				        		pdfsURLs.add(link.attr("abs:href"));
							}
				        }
				        
				        for (String pdfsURL : pdfsURLs) {
				        	appendString(textPaneLogger, "Fetching..: " + pdfsURL);
				        	
				        	Document doc1 = Jsoup.connect(pdfsURL).get();
				            Element pdfDirectLink = doc1.getElementById("pdfDocument");
				        	
				            pdfsDirectURLs.add(pdfDirectLink.attr("src"));
				            
				            appendString(textPaneLogger, pdfDirectLink.attr("src"));
				            
							//System.out.println(pdfsURL);
						}
				        
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			        
					
					//appendString(textPaneLogger, "textFieldBookURL: " + textFieldBookURL.getText());
				}
				
			}
		});
		btnTryToDownload.setBounds(10, 67, 551, 23);
		frmFreeWileydownmergePdf.getContentPane().add(btnTryToDownload);
		
		
	}
}

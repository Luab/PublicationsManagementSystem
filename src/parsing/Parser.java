package parsing;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Yuriy on 9/22/2015.
 */

public class Parser {
	public static void main(String[] args)
			throws IOException, SAXException, ParserConfigurationException {
		int fileNo = 1073; // Setting how many files programm will parse
		long t1 = 0;
		String path;
		for (int i = 1; i <= fileNo; i++) {
			path = "records/" + i;
			System.out.println("Trying " + path);

			t1 = System.nanoTime();
			if (ParseFromFile.openFile(path)) {
				try {
					ParseFromFile.Parse();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			System.out.println(new DecimalFormat("#0.000")
					.format((double) (System.nanoTime() - t1) / 1000000000));
		}

	}
}

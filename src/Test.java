import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.pumas.Author;
import ru.pumas.DbHelper;
import ru.pumas.Publication;
import ru.pumas.Subject;

public class Test {

	public static void main(String[] args) throws SQLException {

		String doi = "2ELITE4DOI";
		String link = "gugel.com";
		Date dateCreated = new Date(1337);
		Date dateUpdated = null;
		String venue = "Undermemes";
		String title = "Definition of 'Skelental' and its origin";
		String description = "I was expecting a cackling Papyrus to"
				+ " show Frisk a poorly written creepypasta involving"
				+ " all caps, hyperrealistic spaghetti sauce coming out"
				+ " of his computer screen and a terrible jumpscare.";
		
		List<String> authors = new ArrayList<String>();
		authors.add("Papyrus");
		authors.add("Sans");
		
		List<String> subjects = new ArrayList<String>();
		subjects.add("Games");
		subjects.add("Memology");

		int id = DbHelper.makePublication(doi, link, dateCreated, dateUpdated, venue,
				title, description, authors, subjects);
		
		Publication p = DbHelper.getPublicationById(id);
		
		System.out.println(p.getTitle());
		ResultSet rs = p.getAuthorsSet();
		while(rs.next()) {
			System.out.println("    " + Author.from(rs).getName());
		}

	}

}

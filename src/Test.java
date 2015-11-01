import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import ru.pumas.Author;
import ru.pumas.DbHelper;
import ru.pumas.Publication;
import ru.pumas.Subject;

public class Test {

	public static void main(String[] args) throws SQLException {
		ResultSet rs = DbHelper.searchPublicationsByTitleSubstring("square");

		while (rs.next()) {
			Publication p = Publication.from(rs);
			System.out.println(p.getTitle());
		}
	}

}

import java.sql.SQLException;

import ru.pumas.DbHelper;
import ru.pumas.PublicationSet;

public class Test {

	public static void main(String[] args) throws SQLException {
		PublicationSet rs = DbHelper.searchPublicationsByVenueSubstsring("octarine", 1, 2);
		
		while(rs.next()) {
			System.out.println(rs.getPublication());
		}
	}

}

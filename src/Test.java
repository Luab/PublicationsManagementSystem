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
		ResultSet rs = DbHelper.getPublicationSetByVenueId(3);
		
		while(rs.next()) {
			System.out.println(Publication.from(rs).getTitle());
		}
	}

}

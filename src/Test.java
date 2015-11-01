import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.pumas.Author;
import ru.pumas.DbHelper;
import ru.pumas.Publication;
import ru.pumas.PublicationSet;
import ru.pumas.Subject;

public class Test {

	public static void main(String[] args) throws SQLException {
		PublicationSet rs = DbHelper.getPublicationSetByVenueId(1);
		
		while(rs.next()) {
			System.out.println(rs.getPublication());
		}
	}

}

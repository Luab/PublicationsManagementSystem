import java.sql.SQLException;

import ru.pumas.DbHelper;
import ru.pumas.PublicationSet;

public class Test {

	public static void main(String[] args) throws SQLException {
		DbHelper.deletePublicationById(124);
		PublicationSet rs = DbHelper.getPublicationSetById(124);
		
		while(rs.next()) {
			System.out.println(rs.getPublication());
		}
	}

}

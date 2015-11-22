import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import ru.pumas.DbHelper;
import ru.pumas.PublicationSet;

public class Test {

	public static void main(String[] args) throws SQLException {
//		int a = DbHelper.addUser("sans7", "12", true);
		System.err.println(DbHelper.getUserByLoginPassword("sans7", "12"));
	}

}

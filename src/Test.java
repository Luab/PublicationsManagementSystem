import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import ru.pumas.DbHelper;
import ru.pumas.PublicationSet;

public class Test {

	public static void main(String[] args) throws SQLException {
		ArrayList<String> a = new ArrayList<String>();
		a.add("a1");
		ArrayList<String> s = new ArrayList<String>();
		s.add("s1");
		System.err.println(DbHelper.makePublication("qwe", "lenk", new Date(0), null, "ven", "tit", "de", a, s));
	}

}

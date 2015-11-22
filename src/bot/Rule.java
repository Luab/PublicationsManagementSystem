package bot;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Lua_b on 01.11.2015.
 */
public abstract class Rule {

    abstract public boolean check(String txt, Message msg) throws IOException, SQLException;

}

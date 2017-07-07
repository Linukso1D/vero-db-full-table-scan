import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by PC on 5/8/2017.
 */
public class mysql {
    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    public mysql() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://verodbhost:3306/vero_app", "root", "root");
        stmt = con.createStatement();
    }

    public ArrayList getTables() throws SQLException {
        this.rs = stmt.executeQuery("SHOW tables");
        ArrayList tables = new ArrayList<String>();
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        return tables;
    }

    public ArrayList getFieldsByTable(String tableName) throws SQLException {
        this.rs = stmt.executeQuery("show COLUMNS from " + tableName);
        ArrayList tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        return tables;
    }

    public void generateSQL(boolean like, LinkedHashMap collection, String searched) {
        collection.forEach((k, v) -> {
            String sql = "SELECT * FROM " + k + " WHERE ";
            ArrayList<String> fields = (ArrayList) v;
            String lastEl = fields.get(fields.size() - 1);
            String qPart = like ? " like " : " like ";
            String search = like ? "\"%" + searched + "%\"" : "\"" + searched + "\"";
            for (String field : fields) {
                if (lastEl.equals(field)) {
                    sql += " " + field + qPart + search + " ;";
                } else {
                    sql += " " + field + qPart + search + " OR ";
                }
            }
            try {
                outer(excecuteQ(sql, fields), String.valueOf(k));
            } catch (SQLException e) {
                System.out.println("Error: " + k);
            }
        });
    }

    public ArrayList<LinkedHashMap<String, String>> excecuteQ(String sql, ArrayList<String> columns) throws SQLException {
        this.rs = stmt.executeQuery(sql);
        ArrayList<LinkedHashMap<String, String>> rows = new ArrayList<>();
        if (!rs.wasNull())
            while (rs.next()) {
                columns.forEach((c) -> {
                    try {
                        LinkedHashMap<String, String> result = new LinkedHashMap();
                        result.put(c, rs.getString(c));
                        rows.add(result);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

            }
        return rows;
    }

    public void outer(ArrayList<LinkedHashMap<String, String>> result, String title) {
        if (!result.isEmpty()) {
            System.out.println();
            System.out.println(title);
            System.out.println("===========");
            result.forEach((v) -> v.forEach((key, value) -> {
                System.out.println("      " + key + " : " + value);
            }));

        }
    }

}

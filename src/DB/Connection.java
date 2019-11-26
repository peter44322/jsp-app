package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Connection {
    static String URL = "jdbc:mysql://localhost/ia_assignment";
    static String DB_USER_NAME = "root";
    static String DB_PASSWORD = "peter";
    private java.sql.Connection myConnection;
    private ResultSet resultSet;
    ArrayList<String> selects;
    ArrayList<String> joins;
    ArrayList<ResultSet> result;
    String union = "";
    String from;
    String orderBy = "";
    String limit = "";
    ArrayList<String> wheres;


    public Connection() {
        wheres = new ArrayList<>();
        selects = new ArrayList<>();
        joins = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConnection = DriverManager.getConnection(URL, DB_USER_NAME, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection union(Connection connection) {
        union = connection.execute(false);
        return this;
    }

    public Connection from(String table) {
        from = table;
        return this;
    }

    public Connection from(Connection connection) {
        from = " (" + connection.execute(false) + ")a ";
        return this;
    }

    public Connection limit(String limit) {
        this.limit = limit;
        return this;
    }

    public Connection orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }


    public Connection join(String table, String c1, String c2) {
        joins.add(" LEFT JOIN " + table + " ON " + c1 + " = " + c2);
        return this;
    }

    public Connection where(String column, String op, String value) {
        wheres.add(column + op + '"' + value + '"');
        return this;
    }

    private String execute(boolean force) {
        String where = wheres.size() > 0 ? " WHERE " + wheres.stream().collect(Collectors.joining(" AND ")) : "";
        String join = joins.size() > 0 ? joins.stream().collect(Collectors.joining(" ")) : "";
        String select = selects.size() > 0 ? selects.stream().collect(Collectors.joining(",")) : " * ";
        String order = orderBy.length() > 0 ? " ORDER BY " + orderBy : "";
        String limit = this.limit.length() > 0 ? " LIMIT " + this.limit : "";
        String union = this.union.length() > 0 ? " UNION " + this.union : "";
        String query = " ( SELECT " + select + " FROM " + from + join + where + order + limit + " ) " + union;
        if (resultSet == null || force) {
            System.out.println(query);
            try {
                Statement statement = myConnection.createStatement();
                resultSet = statement.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(query);
        return query;
    }

    public ResultSet getResultSet() {
        execute(false);
        return resultSet;
    }

    public void insert(String into, ArrayList<String> columns, ArrayList<String> values) throws SQLException {
            Statement statement = myConnection.createStatement();
            statement.executeUpdate(
                    "insert into " + into + "( "+String.join(", ", columns)+" ) VALUES (  "
                            + values.stream().map(v->"'"+v+"'").collect(Collectors.joining(",")) + ")"
            );
    }

    public int count() {
        selects = new ArrayList<>();
        selects.add(" count(*) as count ");
        execute(false);
        try {
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

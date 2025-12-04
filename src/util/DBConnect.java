package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static final String URL = "jdbc:mysql://yamabiko.proxy.rlwy.net:37413/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "ioJPGNLqAxQCJVoUsKlGgluXdnvzbGjJ";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("KET NOI THANH CONG!");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ LOI KET NOI DATABASE:");
            System.out.println("URL = " + URL);
            System.out.println("USER = " + USER);
            e.printStackTrace();
            return null;
        }
    }
}

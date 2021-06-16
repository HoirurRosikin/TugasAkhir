package PembelianBuku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class JDBC {
	 public static Connection koneksi() {
			
		    try {
			    String driver = "com.mysql.cj.jdbc.Driver";
			    String url = "jdbc:mysql://localhost:3306/buku";
			    String user = "root";
			    String password = "";
			    
			    try {
					Class.forName(driver);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			    
			    return DriverManager.getConnection(url, user, password);
			
		    } catch (SQLException e) {
			    JOptionPane.showMessageDialog(null, "Gagal Koneksi ke Database...", "Pemberitahuan", 0);
		    }
		    
		    return koneksi();
		    
	    }
}

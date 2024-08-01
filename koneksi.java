/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package familycinema;

/**
 *
 * @author a s u s
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private Connection connect;
    private String driverName = "com.mysql.jdbc.Driver"; //Driver untuk Koneksi ke MySQL
    private String jdbc = "jdbc:mysql://";
    private String host = "localhost:"; //Bisa menggunakan IP Anda, contoh : 127.0.0.1
    private String port = "3306/"; //Port ini port mysql
    private String database = "bioskop"; //Ini Database yang telah kita buat tadi yang akan digunakan
    private String url = jdbc + host + port + database;
    private String username = "root"; //username default mysql
    private String password = "";
    public Connection getKoneksi() throws SQLException {
	if (connect == null) {
            try {
		Class.forName(driverName);
                System.out.println("Class Driver Ditemukan");
		try{
                    connect = DriverManager.getConnection(url,username,password);
                    System.out.println("Koneksi Database Sukses");
		} catch (SQLException se) {
                    System.out.println("Koneksi Database Gagal : " + se);
                    System.exit(0);
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class Driver Tidak Ditemukan, Terjadi Kesalahan pada : " + cnfe);
                System.exit(0);
            }
	}
        return connect;
    }
}

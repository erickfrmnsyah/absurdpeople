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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class crud {
    private int id_makan, id_minum, jl_makan, jl_minum, id_bioskop;
    private int filmId,id_film,idUser;
    private String username,pw,nama,lvl, newPW,notel;
    private String lokasi,kota;
    private String  judul;
    private String id_jadwal, studio, tanggal,waktu;
    private String j_tanggal, j_waktu, j_studio;
    private int s_id_show,id_show;
    private int bookingId, jumlahTiket, totalHarga;
    private Connection CRUDKoneksi;
    private PreparedStatement CRUDpsmt;
    private Statement CRUDstat;
    private ResultSet CRUDhasil;
    private String CRUDquery;
    
    public crud(){
        try {
            koneksi connection = new koneksi();
            CRUDKoneksi = connection.getKoneksi();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getNewPW() {
        return newPW;
    }

    public void setNewPW(String newPW) {
        this.newPW = newPW;
    }
    
    public int getIdUser(){
        return idUser;
    }
    
    public void setInt(int userId){
        this.idUser = userId;
    }

    public String getNotel() {
        return notel;
    }

    public void setNotel(String notel) {
        this.notel = notel;
    }
    
    //LOGREG
     public boolean isNotelRegistered(String notel) {
        String checkQuery = "SELECT COUNT(*) FROM user WHERE notel = ?";
        try {
            CRUDpsmt = CRUDKoneksi.prepareStatement(checkQuery);
            CRUDpsmt.setString(1, notel);
            ResultSet rs = CRUDpsmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            CRUDpsmt.close();
            return count > 0;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
     
    public void regist(String nama, String username, String pw, String notel){
        // Cek apakah nomor telepon sudah terdaftar
        if (isNotelRegistered(notel)) {
            throw new IllegalArgumentException("Nomor telepon sudah digunakan. Silahkan gunakan nomor lain.");
        } 
        // set user_lvl otomatis dengan 1 sesuai REVISI SURYA
        CRUDquery = "INSERT INTO user (nama, username, pw, notel, user_lvl) VALUES (?, ?, ?, ?,1)";
        try{
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setString(1, nama);
            CRUDpsmt.setString(2, username);
            CRUDpsmt.setString(3, pw);
            CRUDpsmt.setString(4, notel);
            CRUDpsmt.executeUpdate();
            CRUDpsmt.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public boolean change(String username, String newPW) {
        CRUDquery = "SELECT COUNT(*) FROM user WHERE username = ?";
        try {
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setString(1, username);
            ResultSet resultSet = CRUDpsmt.executeQuery();
            
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                CRUDquery = "UPDATE user SET pw = ? WHERE username = ?";
                CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
                CRUDpsmt.setString(1, newPW);
                CRUDpsmt.setString(2, username);

                System.out.println("Executing query: " + CRUDquery);
                System.out.println("Parameters: newPW=" + newPW + ", username=" + username);

                int rowsAffected = CRUDpsmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);

                resultSet.close();
                CRUDpsmt.close();

                return rowsAffected > 0;
            } else {
                System.out.println("Username tidak ditemukan.");
                resultSet.close();
                CRUDpsmt.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengubah password: " + e.getMessage());
        }
    }
    
     public String login(String username, String pw) {
        CRUDquery = "SELECT user_lvl FROM user WHERE BINARY username = ? AND BINARY pw = ?";
        try {
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setString(1, username);
            CRUDpsmt.setString(2, pw);
            CRUDhasil = CRUDpsmt.executeQuery();

            if (CRUDhasil.next()) {
                String lvl = CRUDhasil.getString("user_lvl");
                CRUDhasil.close();
                CRUDpsmt.close();
                return lvl;
            } else {
                CRUDhasil.close();
                CRUDpsmt.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal melakukan login: " + e.getMessage());
        }
    }
     
    
    //// MENU BOOKING
    //booking juga
    //untuk dapet informasi bookingan terakhir
    public int getLatestBookingId(int userId, int filmId) {
        int bookingId = -1;
        CRUDquery = "SELECT id_booking FROM booking WHERE id_user = ? AND id_film = ? ORDER BY id_booking DESC LIMIT 1";
        try {
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setInt(1, userId);
            CRUDpsmt.setInt(2, filmId);
            CRUDhasil = CRUDpsmt.executeQuery();
            if (CRUDhasil.next()) {
                bookingId = CRUDhasil.getInt("id_booking");
            }
            CRUDhasil.close();
            CRUDpsmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingId;
    }
    // Metode untuk memperbarui informasi booking
    public void updateBooking(int bookingId, int idShow, int jmlSeat, String seat1, String seat2, String seat3, String seat4) {
    CRUDquery = "UPDATE booking SET id_show = ?, jml_seat = ?, seat1 = ?, seat2 = ?, seat3 = ?, seat4 = ? WHERE id_booking = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, idShow);
        CRUDpsmt.setInt(2, jmlSeat);
        CRUDpsmt.setString(3, seat1);
        CRUDpsmt.setString(4, seat2);
        CRUDpsmt.setString(5, seat3);
        CRUDpsmt.setString(6, seat4);
        CRUDpsmt.setInt(7, bookingId);
        CRUDpsmt.executeUpdate();
        CRUDpsmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
     
    //// MENU ADMIN 
        public ResultSet dataFilm() {
        CRUDquery = "SELECT id_film,judul FROM film";
        try {
            CRUDstat = CRUDKoneksi.createStatement();
            CRUDhasil = CRUDstat.executeQuery(CRUDquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CRUDhasil;
    }
        
    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
    
   
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
    
    public ResultSet dataJadwal() {
        CRUDquery = "SELECT * FROM jadwal";
        try {
            CRUDstat = CRUDKoneksi.createStatement();
            CRUDhasil = CRUDstat.executeQuery(CRUDquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CRUDhasil;
    }
        // setget jadwal

    public String getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(String id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void simpanJadwal(String id_film, String tanggal , String waktu, String Studio) {
        CRUDquery = "INSERT INTO jadwal (id_film, tanggal, waktu, studio) VALUES (?, ?, ?, ?)";
        try{
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setString(1, id_film);
            CRUDpsmt.setString(2, tanggal);
            CRUDpsmt.setString(3, waktu);
            CRUDpsmt.setString(4, studio);
            CRUDpsmt.executeUpdate();
            CRUDpsmt.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void ubahJadwal(String id_jadwal, int id_film, String tanggal, String waktu, String studio) {
    CRUDquery = "UPDATE jadwal SET id_film = ?, tanggal = ?, waktu = ?, studio = ? WHERE id_jadwal = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, id_film); 
        CRUDpsmt.setString(2, tanggal);
        CRUDpsmt.setString(3, waktu);
        CRUDpsmt.setString(4, studio);
        CRUDpsmt.setString(5, id_jadwal);
        System.out.println("Executing query: " + CRUDpsmt.toString()); // Debugging
        CRUDpsmt.executeUpdate();
        CRUDpsmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e);
    }
    }
    
    public void hapusJadwal(String id_jadwal) {
        CRUDquery = "delete from jadwal where id_jadwal=?";
        try{
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setString(1, id_jadwal);
            CRUDpsmt.executeUpdate();
            CRUDpsmt.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    // untuk tabel showtime di show manager
    public ResultSet showList() {
        CRUDquery = "SELECT * FROM showtime";
        try {
            CRUDstat = CRUDKoneksi.createStatement();
            CRUDhasil = CRUDstat.executeQuery(CRUDquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CRUDhasil;
    }

    public int getId_show() {
        return id_show;
    }

    public void setId_show(int id_show) {
        this.id_show = id_show;
    }
    
    public void simpanShow(int id_bioskop , String id_jadwal) {
        CRUDquery = "INSERT INTO showtime (id_bioskop, id_jadwal) VALUES (?, ?)";
        try{
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setInt(1, id_bioskop);
            CRUDpsmt.setString(2, id_jadwal);
            CRUDpsmt.executeUpdate();
            CRUDpsmt.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    public void ubahShow(int id_show, int id_bioskop , String id_jadwal) {
    CRUDquery = "UPDATE showtime SET id_bioskop = ?, id_jadwal = ? WHERE id_show = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, id_bioskop); // Perbaiki di sini
        CRUDpsmt.setString(2, id_jadwal);
        CRUDpsmt.setInt(3, id_show);
        System.out.println("Executing query: " + CRUDpsmt.toString()); // Debugging
        CRUDpsmt.executeUpdate();
        CRUDpsmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e);
    }
}
    public void hapusShow(int id_show) {
        CRUDquery = "delete from showtime where id_show=?";
        try{
            CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
            CRUDpsmt.setInt(1, id_show);
            CRUDpsmt.executeUpdate();
            CRUDpsmt.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //  integrasi akun    
    public void saveBookingToDatabase(int userId, int id_film) {
    // Simpan ke database
    String query = "INSERT INTO booking (id_user, id_film) VALUES (?, ?)";
    try {
        PreparedStatement psmt = CRUDKoneksi.prepareStatement(query);
        psmt.setInt(1, userId);
        psmt.setInt(2, id_film);
        psmt.executeUpdate();
        psmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Gagal menyimpan booking: " + e.getMessage());
    }
}
    public int getIDFromDatabase(String username) {
        int id = -1; // Default value jika tidak ditemukan

        String query = "SELECT id_user FROM user WHERE username = ?";
        try {
            PreparedStatement psmt = CRUDKoneksi.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_user");
            }

            rs.close();
            psmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengambil ID pengguna: " + e.getMessage());
        }
        return id;
    }
    
    // menu fnb

    public int getId_makan() {
        return id_makan;
    }

    public void setId_makan(int id_makan) {
        this.id_makan = id_makan;
    }

    public int getId_minum() {
        return id_minum;
    }

    public void setId_minum(int id_minum) {
        this.id_minum = id_minum;
    }

    public int getJl_makan() {
        return jl_makan;
    }

    public void setJl_makan(int jl_makan) {
        this.jl_makan = jl_makan;
    }

    public int getJl_minum() {
        return jl_minum;
    }

    public void setJl_minum(int jl_minum) {
        this.jl_minum = jl_minum;
    }
    
    public void simpanFnb(int userId,int bookingId, int id_makan, int id_minum, int jl_makan, int jl_minum) {
    CRUDquery = "INSERT INTO orderan (id_user,id_booking, id_makanan, id_minuman, jml_makan, jmlh_minum) VALUES (?,?, ?, ?, ?, ?)";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, userId);
        CRUDpsmt.setInt(2, bookingId);
        CRUDpsmt.setInt(3, id_makan);
        CRUDpsmt.setInt(4, id_minum);
        CRUDpsmt.setInt(5, jl_makan);
        CRUDpsmt.setInt(6, jl_minum);
        CRUDpsmt.executeUpdate();
        CRUDpsmt.close();
    } catch (Exception e) {
        System.out.println(e);
    }
}
    
    //data bioskop
    public ResultSet dataBioskop() {
        CRUDquery = "SELECT id_bioskop,lokasi FROM bioskop";
        try {
            CRUDstat = CRUDKoneksi.createStatement();
            CRUDhasil = CRUDstat.executeQuery(CRUDquery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CRUDhasil;
    }
    //setget databioskop
    public int getId_bioskop() {
        return id_bioskop;
    }

    public void setId_bioskop(int id_bioskop) {
        this.id_bioskop = id_bioskop;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }
    
    //data showtime
    public ResultSet dataShow(int id_bioskop, int id_film) {
    CRUDquery = "SELECT s.id_show, j.tanggal, j.waktu, j.studio FROM showtime s JOIN jadwal j ON s.id_jadwal = j.id_jadwal JOIN film f ON j.id_film = f.id_film WHERE s.id_bioskop = ? AND f.id_film = ?;";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, id_bioskop);
        CRUDpsmt.setInt(2, id_film);
        CRUDhasil = CRUDpsmt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return CRUDhasil;
    }
    
    //setget datashow
    public String getJ_tanggal() {
        return j_tanggal;
    }

    public void setJ_tanggal(String j_tanggal) {
        this.j_tanggal = j_tanggal;
    }

    public String getJ_waktu() {
        return j_waktu;
    }

    public void setJ_waktu(String j_waktu) {
        this.j_waktu = j_waktu;
    }

    public String getJ_studio() {
        return j_studio;
    }

    public void setJ_studio(String j_studio) {
        this.j_studio = j_studio;
    }

    public int getS_id_show() {
        return s_id_show;
    }

    public void setS_id_show(int s_id_show) {
        this.s_id_show = s_id_show;
    }
    
    /// menu konfir order
    public ResultSet dataBooking(int userId, int bookingId) {
    CRUDquery = "SELECT b.id_booking, f.judul AS judul_film, bio.lokasi AS lokasi_bioskop, j.tanggal, j.waktu, j.studio, "
             + "b.jml_seat AS jumlah_seat, "
             + "CONCAT(b.seat1, ', ', b.seat2, ', ', b.seat3, ', ', b.seat4) AS seats, "
             + "(b.jml_seat * f.harga) AS harga_total "
             + "FROM booking b "
             + "JOIN showtime s ON b.id_show = s.id_show "
             + "JOIN jadwal j ON s.id_jadwal = j.id_jadwal "
             + "JOIN film f ON b.id_film = f.id_film "
             + "JOIN bioskop bio ON s.id_bioskop = bio.id_bioskop "
             + "WHERE b.id_user = ? AND b.id_booking = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, userId);
        CRUDpsmt.setInt(2, bookingId);
        CRUDhasil = CRUDpsmt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return CRUDhasil;
    }

    public ResultSet dataOrder(int userId,int bookingId) {
    CRUDquery = "SELECT o.id_order, m.nama AS nama_makanan, o.jml_makan, "
             + "mi.nama_minuman, o.jmlh_minum, "
             + "(o.jml_makan * m.harga_makanan + o.jmlh_minum * mi.harga_minuman) AS harga_total "
             + "FROM orderan o "
             + "JOIN user u ON o.id_user = u.id_user "
             + "JOIN makanan m ON o.id_makanan = m.id_makanan "
             + "JOIN minuman mi ON o.id_minuman = mi.id_minuman "
             + "WHERE u.id_user = ? AND o.id_booking = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, userId);
        CRUDpsmt.setInt(2, bookingId);
        CRUDhasil = CRUDpsmt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return CRUDhasil;
    }
    
    // my order
    public ResultSet myBook(int userId) {
    CRUDquery = "SELECT b.id_booking, f.judul AS judul_film, bio.lokasi AS lokasi_bioskop, j.tanggal, j.waktu, j.studio, "
             + "b.jml_seat AS jumlah_seat, "
             + "CONCAT(b.seat1, ', ', b.seat2, ', ', b.seat3, ', ', b.seat4) AS seats "
             + "FROM booking b "
             + "JOIN user u ON b.id_user = u.id_user "
             + "JOIN showtime s ON b.id_show = s.id_show "
             + "JOIN jadwal j ON s.id_jadwal = j.id_jadwal "
             + "JOIN film f ON b.id_film = f.id_film "
             + "JOIN bioskop bio ON s.id_bioskop = bio.id_bioskop "
             + "WHERE u.id_user = ?"; 
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, userId);
        CRUDhasil = CRUDpsmt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return CRUDhasil;
    }
    
    public ResultSet myOrder(int userId) {
    CRUDquery = "SELECT o.id_order, m.nama AS nama_makanan, o.jml_makan, "
             + "mi.nama_minuman, o.jmlh_minum "
             + "FROM orderan o "
             + "JOIN user u ON o.id_user = u.id_user "
             + "JOIN makanan m ON o.id_makanan = m.id_makanan "
             + "JOIN minuman mi ON o.id_minuman = mi.id_minuman "
             + "WHERE u.id_user = ?";
    try {
        CRUDpsmt = CRUDKoneksi.prepareStatement(CRUDquery);
        CRUDpsmt.setInt(1, userId);
        CRUDhasil = CRUDpsmt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return CRUDhasil;
    }
    
}


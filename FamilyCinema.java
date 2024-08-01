/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package familycinema;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class FamilyCinema {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        koneksi k = new koneksi();
//        try {
//            k.getKoneksi();
//        } catch (SQLException ex) {
//            Logger.getLogger(FamilyCinema.class.getName()).log(Level.SEVERE, null, ex);
//        }
////    }
        try{
        new Login().setVisible(true);
//   
//        new admin().setVisible(true);
          
//        new booking().setVisible(true);

//          new fnb().setVisible(true);

//        new booking().setVisible(true);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
    
}

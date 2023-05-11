package be.nadtum.etum.Utility.Modules;

import org.bukkit.entity.Player;

import java.sql.*;

public class MysqlGestion {





    //méthode permettant de lié le compte du joueur au compte du site web
    public static void insertUuidForPlayer(Player player, String uuid) {

        Connection cn = null;
        Statement st = null;
        String sql = null;
        ResultSet resultSet = null;
        String foundType = null;


        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://akiabebnadtum.mysql.db/akiabebnadtum?user=akiabebnadtum&password=Diabolos2002");



            st = cn.createStatement();

            resultSet = st.executeQuery("SELECT Pseudo FROM Membres WHERE Pseudo='" + player.getName() + "'");

            if(resultSet.next()){
                foundType = resultSet.getString(1);
            }
            if(foundType == null){
                player.sendMessage(PrefixMessage.erreur() + "vous n'êtes pas inscrit sur le site");
                st.close();
                cn.close();
                return;
            }

            resultSet = st.executeQuery("SELECT Pseudo FROM Membres WHERE UUID='" + uuid + "'");
            if(resultSet.next()){
                foundType = resultSet.getString(1);
            }
            if(resultSet.getRow() != 0){
                player.sendMessage(PrefixMessage.erreur() + "vous êtes déjà lié avec votre compte");
                st.close();
                cn.close();
                return;
            }


            sql = "UPDATE Membres SET UUID='" + uuid + "' WHERE Pseudo='" + player.getName() + "'";
            st.executeUpdate(sql);

            st.close();
            cn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Boolean estLier(String player){

        Connection cn = null;
        Statement st = null;
        String sql = null;
        ResultSet resultSet = null;
        String foundType = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://minecraft3048.omgserv.com/minecraft_305596?" +
                    "user=minecraft_305596&password=diable");
            st = cn.createStatement();


            resultSet = st.executeQuery("SELECT Pseudo FROM Membres WHERE UUID='" + PlayerGestion.getUUIDFromName(player) + "'");
            if(resultSet.next()){
                foundType = resultSet.getString(1);
            }
            if(foundType != null){

                st.close();
                cn.close();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setNewPseudo(){

    }

}


package dss.mediacenterapp.data;

import dss.mediacenterapp.model.utilizadores.Utilizador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe que implementa um DAO para aceder às tabelas de utilizadores
 * da base de dados.
 * 
 * @author Grupo 1
 */
public class UtilizadorDAO implements Map<String, Utilizador> {
    
    //**************************************************************************
    
    /**
     * instancia de UtilizadorDAO
     */
    private UtilizadorDAO inst = null;
    
    //**************************************************************************

    /**
     * Construtor vazio do DAO
     */
    public UtilizadorDAO () {
        
        try {
        
            Class.forName("com.mysql.cj.jdbc.Driver");       
            
        } catch (ClassNotFoundException e) {
            
            throw new NullPointerException(e.getMessage());
        }
    }
    
    /**
     * Verifica se um dado utilizador identificado por um email existe na BD
     * @param key email
     * @return true caso o email exista
     */
    @Override
    public boolean containsKey(Object key) {
        
        Connection conn;
        
        try {
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement();
            
            String sql = "SELECT * FROM Utilizador u WHERE u.email = '"+(String)key+"'";
            
            ResultSet rs = stm.executeQuery(sql);
            
            if (rs.next()) {
                
                return rs.getString(1).equals((String) key);
            }
            
            return false;
        
        } catch (Exception e) {
        
            System.out.println(e.getMessage());
        }        
        
        return false;
    }

    /**
     * Retorna o numero de utilizadores existentes na base de dados.
     * @return nr de utilizadores
     */    
    @Override
    public int size() {
        
        Connection conn;
        
        try {
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql = "SELECT COUNT(*) FROM Utilizador";
            
            ResultSet rs = stm.executeQuery(sql);

            rs.next();
            
            return rs.getInt(1);
        
        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }        
        
    }
    
    /**
     * Retorna o utilizador associado a um determinado email
     * Assume que o utilizador existe
     * 
     * @param key email
     * @return o Utilizador
     */
    @Override
    public Utilizador get(Object key) {

        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Utilizador user = null;
            
            Statement stm = conn.createStatement();
            
            String sql = "SELECT * FROM Utilizador WHERE email='"+(String)key+"'";
            
            ResultSet rs = stm.executeQuery(sql);
            
            if (rs.next()) {
                
                user = new Utilizador(rs.getString(1), rs.getString(3), rs.getString(2), rs.getInt(4) > 0 ? true : false, rs.getInt(5) > 0 ? true : false);
            } 
            
            String sql_getamigos = "select a.emailAmigo from Utilizador u, Amigo a, AmigosDoUtilizador adu" + 
                                   " where u.email = adu.Utilizador_email and adu.Amigo_emailAmigo = a.emailAmigo and u.email = '" + (String)key + "';";

            Statement s2 = conn.createStatement();
            
            rs = s2.executeQuery(sql_getamigos);
            
            while (rs.next()) {
                
                user.adicionaAmigo(rs.getString(1));
            }
            
            return user;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}

    }
    
    /**
     * Adiciona lista de emails aos potenciais amigos de um utilizador
     * @param email email do utilizador
     * @param emailsAmigos colecao de emails dos amigos
     */
    public void adicionaPotenciaisAmigos(String email, Set<String> emailsAmigos) {
        
        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
                        
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        
            conn.setAutoCommit(false);

            for (String a : emailsAmigos) {

                Statement verifica = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
                String sql_verificaExistenciaAmigo = "select count(*) from Amigo a where a.emailAmigo = '" + a + "';"; 
                
                ResultSet rs = verifica.executeQuery(sql_verificaExistenciaAmigo);
            
                int existe = 0;
                
                if (rs.next()) {

                    existe = rs.getInt(1);
                }
                
                if (existe == 0) {
                                    
                    String sql_amigo = "insert into Amigo values ('" + a + "')";
                    stm.addBatch(sql_amigo);
                }

                String sql_amigoDoUser = "insert into AmigosDoUtilizador values ('" + email + "', '" + a + "');";                
                stm.addBatch(sql_amigoDoUser);
            }
            
            stm.executeBatch();
            
            conn.commit();
            
            return;
        }
        catch (Exception e) {
            
            System.out.println("Amigo já existe!");
        }
        
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean containsValue(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utilizador put(String arg0, Utilizador arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utilizador remove(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends String, ? extends Utilizador> arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Utilizador> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<String, Utilizador>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

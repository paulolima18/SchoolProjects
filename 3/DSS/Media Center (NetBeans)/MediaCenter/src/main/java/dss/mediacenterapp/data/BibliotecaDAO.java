
package dss.mediacenterapp.data;

import dss.mediacenterapp.model.conteudo.Conteudo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe que implementa um DAO para aceder às tabelas de conteudo da biblioteca
 * da base de dados.
 * 
 * @author Grupo 1
 */
public class BibliotecaDAO implements Map<String, Conteudo> {
    
    //**************************************************************************
    
    /**
     * instancia de bibliotecaDAO
     */
    private BibliotecaDAO inst = null;
    
    //**************************************************************************
    
    /**
     * Construtor vazio do DAO
     */
    public BibliotecaDAO () {
        
        try {
        
            Class.forName("com.mysql.cj.jdbc.Driver");       
            
        } catch (ClassNotFoundException e) {
            
            throw new NullPointerException(e.getMessage());
        }
    }
        
    /**
     * Verifica se um conteúdo existe na base de dados.
     * @param key nome do conteudo
     * @return true se existir
     */
    @Override
    public boolean containsKey(Object key) {
        
        Connection conn;
        
        try {
                        
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement();
            
            String sql = "SELECT * FROM Conteudo c WHERE c.idConteudo = '"+(String)key+"'";
            
            ResultSet rs = stm.executeQuery(sql);
                        
            if (rs.next()) {
                
                return rs.getString(1).equals((String) key);
            }
            
            return false;
        
        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }        
    }

    /**
     * Retorna o numero de conteudo existente na base de dados
     * @return
     */
    @Override
    public int size() {
        
        Connection conn;
        
        try {
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql = "SELECT COUNT(*) FROM Conteudo";
            
            ResultSet rs = stm.executeQuery(sql);

            rs.next();
            
            return rs.getInt(1);
        
        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }        
        
    }

    /**
     * Retorna a lista de emails dos utilizadores que possuem um determinado conteudo
     * @param nomeC nome do conteudo
     * @return lista de emails
     */
    public List<String> getOwners(String nomeC) {

        List<String> emails = new ArrayList<>();
        
        Connection conn;
        
        try {
                
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_emails = "select u.email "
                                  + "from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c where" +
                                    " u.email = adu.idUserADU" +
                                    " and adu.idAlbumADU = a.idAlbum" +
                                    " and cda.idAlbumCDA = a.idAlbum" +
                                    " and cda.idConteudoCDA = c.idConteudo" +
                                    " and c.idConteudo = '" + nomeC + "' ";
            
            ResultSet rs = stm.executeQuery(sql_emails);

            while (rs.next()) {
                
                emails.add(rs.getString(1));
            }        
            
            return emails;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }        
    }

    /**
     * Retorna a lista de emails de utilizadores que possuem um determinado conteudo
     * com uma certa categoria
     * @param nomeC nome do conteudo    
     * @param categoria nome da categoria
     * @return lista de emails
     */
    public List<String> getRealOwners(String nomeC, String categoria) {

        List<String> emails = new ArrayList<>();
        
        Connection conn;
        
        try {
      
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_emails = "select u.email from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c " +
                                "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and cda.idAlbumCDA = a.idAlbum and cda.idConteudoCDA = c.idConteudo " + 
                                "and c.idConteudo = '" + nomeC + "' and c.Categoria_idNomeCategoria = '" + categoria + "';";
                    
            ResultSet rs = stm.executeQuery(sql_emails);

            while (rs.next()) {
                
                emails.add(rs.getString(1));
            }        
            
            return emails;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }        
    }

    /**
     * Retorna o conteudo que se encontra associado a um determinado nome.
     * @param key nome do conteudo
     * @return conteudo correspondente a um nome
     */
    @Override
    public Conteudo get(Object key) {

        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Conteudo cont = null;
            
            Statement stm = conn.createStatement();
            
            String sql = "SELECT * FROM Conteudo WHERE idConteudo='"+(String)key+"'";
            
            ResultSet rs = stm.executeQuery(sql);
            
            if (rs.next()) {
                
            } 
            
            return cont;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}

    }

    /**
     * Retorna os nomes de conteudos que existem na base de dados
     * @return lista de nomes
     */
    @Override
    public Set<String> keySet() {

        Set<String> res = new HashSet<>();
        
        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement();
            
            String sql = "SELECT distinct c.idConteudo FROM Conteudo c";
            
            ResultSet rs = stm.executeQuery(sql);
            
            while (rs.next()) {
                
                res.add(rs.getString(1));
            } 
            
            return res;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}       
    }

    @Override
    public Conteudo put(String arg0, Conteudo arg1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conteudo remove(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends String, ? extends Conteudo> arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public Collection<Conteudo> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Map.Entry<String, Conteudo>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void clear() {
        
    }
    
}

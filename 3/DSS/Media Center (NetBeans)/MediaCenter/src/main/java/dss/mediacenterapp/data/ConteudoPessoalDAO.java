
package dss.mediacenterapp.data;

import dss.mediacenterapp.model.albuns.Album;
import dss.mediacenterapp.model.conteudo.Conteudo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe que implementa um DAO para aceder às tabelas de conteudo pessoal de um
 * utilizador, os seus albuns e conteudos
 * da base de dados.
 * 
 * @author Grupo 1
 */
public class ConteudoPessoalDAO implements Map<String, Conteudo> {

    //**************************************************************************
    
    /**
     * instancia de ConteudoPessoalDAO
     */
    private ConteudoPessoalDAO inst = null;
    
    /**
     * email do utilizador ao qual se pretende aceder ao seu conteudo
     */
    private String email_utilizador;
    
    //**************************************************************************

    /**
     * Construtor parameterizado do DAO com o email associado ao utilizador em questao
     * @param email_user email do utilizador
     */
    public ConteudoPessoalDAO (String email_user) {
        
        this.email_utilizador = email_user;
                
        try {
        
            Class.forName("com.mysql.cj.jdbc.Driver");       
            
        } catch (ClassNotFoundException e) {
            
            throw new NullPointerException(e.getMessage());
        }
        
    }
    
    /**
     * Adiciona um novo album ao conteudo pessoal do utilizador.
     * 
     * @param novoA Album novo 
     */
    public void adicionaAlbum(Album novoA) {
        
        Connection conn;
        
        try {
                
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
                      
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            //Criar o album
            String sql_criaAlbum = "insert into Album values ('" + novoA.getNome() + "', '" + novoA.getCategoria() + "');";
            //Adicionar o album ao utilizador atual
            String sql_ligaAlbumUtilizador = "insert into AlbunsDoUtilizador (idUserADU, idAlbumADU) values ('" + this.email_utilizador + "', '" + novoA.getNome() + "');";

          
            conn.setAutoCommit(false);

            stm.addBatch(sql_criaAlbum);
            stm.addBatch(sql_ligaAlbumUtilizador);
            
            List<Conteudo> conteudosParaAdicionar = novoA.getListaConteudo();
            
            for (Conteudo c : conteudosParaAdicionar) {
                
                //Ver se o conteudo com a categoria já foi adicionado à BD
                
                Statement verifica = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
                String sql_verificaExistenciaDeConteudoComCategoria = "select count(*) from Conteudo c where c.idConteudo = '" + c.getNome() + "' and c.Categoria_idNomeCategoria = '" + c.getCategoria() + "';"; 
                
                ResultSet rs = verifica.executeQuery(sql_verificaExistenciaDeConteudoComCategoria);
            
                int existe = 0;
                
                if (rs.next()) {

                    existe = rs.getInt(1);
                }
                
                if (existe == 0) {
                                    
                    String sql_insereConteudo = "insert into Conteudo values ('" + c.getNome() + "', '" + c.getArtista() + "', " + c.getIsMusic() + ", " + c.getIsVideo() + ", '" + c.getFilePath() + "', '" + c.getCategoria() + "');";
                    System.out.println(sql_insereConteudo);
                    stm.addBatch(sql_insereConteudo);
                }
                
                String sql_ligaConteudoAoAlbum = "insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ('" + novoA.getNome() + "', '" + c.getNome() + "', '" + c.getCategoria() + "');";
                
                stm.addBatch(sql_ligaConteudoAoAlbum);
            }
            
            //Executar todas as querys
            
            stm.executeBatch();
            
            conn.commit();
                        
            return;

        } catch (Exception e) {
        
            System.err.println(e.getMessage());
        }        
    }

    /**
     * Determina a categoria mais comum na coleção do utilizador
     * @return categoria favorita
     */
    public String categoriaFavorita() {
        
        String cat_fav = "Nenhuma";
        
        Connection conn;
        
        try {
                
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_categorias = "select c.Categoria_idNomeCategoria, count(c.Categoria_idNomeCategoria) "
                                  + "from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c where" +
                                    " u.email = adu.idUserADU" +
                                    " and adu.idAlbumADU = a.idAlbum" +
                                    " and cda.idAlbumCDA = a.idAlbum" +
                                    " and cda.idConteudoCDA = c.idConteudo" +
                                    " and u.email = '" + this.email_utilizador + "' "
                                  + "group by c.Categoria_idNomeCategoria "
                                  + "order by count(c.Categoria_idNomeCategoria) desc";
            
            ResultSet rs = stm.executeQuery(sql_categorias);
            
            if (rs.next()) {
                
                cat_fav = rs.getString(1);
            }
                        
            return cat_fav;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }
    }
    

    /**
     * Metodo que vai buscar o album associado a um dado nome na base de dados
     * do conteudo pessoal do utilizador.
     * @param nomeAlbum nome do album
     * @return o Album
     */
    public Album getAlbum(String nomeAlbum) {
        
        Album alb = null;
        
        Connection conn;
        
        try {
                
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_getAlbum = "select a.Categoria_idNomeCategoria "
                                + "from Utilizador u, AlbunsDoUtilizador adu, Album a "
                                + "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum "
                                + "and u.email = '" + this.email_utilizador + "'"
                                + " and a.idAlbum = '" + nomeAlbum + "'";
            
            ResultSet rs = stm.executeQuery(sql_getAlbum);

            rs.next();
            
            String cat_album = rs.getString(1);
            
            alb = new Album(nomeAlbum, cat_album);
            
            String sql_getConteudoAlbum = "select c.* from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c where" +
                                            " u.email = adu.idUserADU" +
                                            " and adu.idAlbumADU = a.idAlbum" +
                                            " and cda.idAlbumCDA = a.idAlbum" +
                                            " and cda.idConteudoCDA = c.idConteudo " +
                                            "and c.Categoria_idNomeCategoria = cda.idCategoriaCDA" +
                                            " and u.email = '" + this.email_utilizador + "'"
                                            + " and a.idAlbum = '" + nomeAlbum + "'";            
            
            rs = stm.executeQuery(sql_getConteudoAlbum);

            List<Conteudo> conteudoAlbum = new ArrayList<>();
             
            while (rs.next()) {
                
                Conteudo c = new Conteudo(rs.getString(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4), rs.getString(5), rs.getString(6));
                
               
                conteudoAlbum.add(c);
            }
            
            alb.addAllConteudo(conteudoAlbum);
            
            return alb;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }
        
    }
    
    /**
     * retorna a lista de nomes dos albuns que um utilizador possui
     * @return 
     */
    public List<String> getListaAlbuns() {
        
        List<String> nomesAlbuns = new ArrayList<>();
        
        Connection conn;
        
        try {
                
        
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql = "select a.idAlbum, a.Categoria_idNomeCategoria "
                    + "from Utilizador u, AlbunsDoUtilizador adu, Album a "
                    + "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum "
                    + "and u.email = '" + this.email_utilizador + "'";
            
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next()) {
                
                nomesAlbuns.add(rs.getString(1) + " [" + rs.getString(2) + "]");
            }
                 
            return nomesAlbuns;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }
    }
    

    /**
     * Determina se o utilizador possui ou não um determinado conteudo
     * @param nomeC nome do conteudo
     * @return true caso possua
     */
    public boolean containsConteudo(String nomeC) {

        boolean temConteudo = true;
        
       Connection conn;
        
        try {
                
        
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_temconteudo = "select count(c.idConteudo) from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c where" +
                                      " u.email = adu.idUserADU" +
                                      " and adu.idAlbumADU = a.idAlbum" +
                                      " and cda.idAlbumCDA = a.idAlbum" +
                                      " and cda.idConteudoCDA = c.idConteudo" +
                                      " and u.email = '" + this.email_utilizador + "'"
                                    + " and c.idConteudo = '" + nomeC + "'";            
            

            ResultSet rs = stm.executeQuery(sql_temconteudo);

            if (rs.next()) {
                
                temConteudo = rs.getInt(1) > 0 ? true : false;
            }
                 
            return temConteudo;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }       
    }

    /**
     * retorna a lista de nomes do conteudo que o utilizador possui 
     * @return lista de nomes dos conteudos
     */
    public List<String> getListaConteudo() {

        
        List<String> listaConteudo = new ArrayList<>();
        
        Connection conn;
        
        try {
                
        
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql_conteudoPessoal = "select c.idConteudo, c.Categoria_idNomeCategoria from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c " +
                                         "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and cda.idAlbumCDA = a.idAlbum and cda.idConteudoCDA = c.idConteudo " +
                                         "and c.Categoria_idNomeCategoria = cda.idCategoriaCDA " +
                                         "and u.email = '" + this.email_utilizador + "';";
            
            ResultSet rs = stm.executeQuery(sql_conteudoPessoal);

            while(rs.next()) {
                
                listaConteudo.add(rs.getString(1) + " [" + rs.getString(2) + "]");
            }
                 
            return listaConteudo;

        } catch (Exception e) {
        
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Vai buscar o conteudo associado a um dado nome
     * @param nomeC nome do conteudo
     * @return o Conteudo
     */
    public Conteudo getConteudo(String nomeC) {

        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Conteudo cont = null;
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String sql = "select * from Conteudo where idConteudo = '" + nomeC + "';";
            
            ResultSet rs = stm.executeQuery(sql);
            
            Conteudo c = null;
                    
            if (rs.next()) {
                
                c = new Conteudo(rs.getString(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4), rs.getString(5), rs.getString(6));
            }
                        
            return c;
        
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
        
    }

    /**
     * Atualiza o conteudo associado a um dado conteudo passado como argumento
     * @param c conteudo com categoria alterada
     * @param catAntiga categoria antiga
     */
    public void updateConteudo(Conteudo c, String catAntiga) {
        
        //----------------------------------------------------------------------
        
        String nomeAlbumDoConteudo = "";
        
        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        
            String sql = "select cda.idAlbumCDA from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c "
                    + "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and cda.idAlbumCDA = a.idAlbum and cda.idConteudoCDA = c.idConteudo "
                    + "and c.idConteudo = '" + c.getNome() + "' and u.email = '" + this.email_utilizador + "';"; 
                    
            ResultSet rs = stm.executeQuery(sql);
                                
            if (rs.next()) {
                
                nomeAlbumDoConteudo = rs.getString(1);               
            }
                        
            String deleteCDA = "delete from ConteudoDoAlbum where idAlbumCDA = '" + nomeAlbumDoConteudo + "' "
                             + "and idConteudoCDA = '" + c.getNome() + "' "
                             + "and idCategoriaCDA = '" + catAntiga + "';";
            
            String updateCont = "update Conteudo set Categoria_idNomeCategoria = '" + c.getCategoria() + "' "
                              + "where idConteudo = '" + c.getNome() + "' "
                              + "and Categoria_idNomeCategoria = '" + catAntiga + "';";

            String insertCDA = "insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) "
                             + "values ('" + nomeAlbumDoConteudo + "', '" + c.getNome() + "', '" + c.getCategoria() + "');";
            
            System.out.println("Starting update...");
            System.out.println(deleteCDA);
            System.out.println(updateCont);
            System.out.println(insertCDA);
            System.out.println("done.");

            conn.setAutoCommit(false);

            stm.addBatch(deleteCDA);
            stm.addBatch(updateCont);
            stm.addBatch(insertCDA);
            
            stm.executeBatch();
            conn.commit();
            
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }       

    }

    /**
     * Substitui um conteudo associado a um album do utilizador
     * @param c conteudo
     * @param catantiga categoria antiga 
     */
    public void replaceConteudo(Conteudo c, String catantiga) {
        
        String nomeAlbumDoConteudo = "";
        
        Connection conn;
        
        try {
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MediaCenterDB?useTimezone=true&serverTimezone=UTC","dss.projeto","dss.mediacenter");
            
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        
            String sql = "select cda.idAlbumCDA from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c "
                    + "where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and cda.idAlbumCDA = a.idAlbum and cda.idConteudoCDA = c.idConteudo "
                    + "and c.idConteudo = '" + c.getNome() + "' and u.email = '" + this.email_utilizador + "';"; 
                    
            ResultSet rs = stm.executeQuery(sql);
                                
            if (rs.next()) {
                
                nomeAlbumDoConteudo = rs.getString(1);               
            }
                        
            String deleteCDA = "delete from ConteudoDoAlbum where idAlbumCDA = '" + nomeAlbumDoConteudo + "' "
                             + "and idConteudoCDA = '" + c.getNome() + "' "
                             + "and idCategoriaCDA = '" + catantiga + "';";
            
            String insertConteudo = "insert into Conteudo values ('" + c.getNome() + "', '" + c.getNome() + "', 0, 1, '" + c.getFilePath() + "', '" + c.getCategoria() + "');";
            
            String insertCDA = "insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) "
                             + "values ('" + nomeAlbumDoConteudo + "', '" + c.getNome() + "', '" + c.getCategoria() + "');";
            
            System.out.println("Starting update...");
            System.out.println(deleteCDA);
            System.out.println(insertConteudo);
            System.out.println(insertCDA);
            System.out.println("done.");

            conn.setAutoCommit(false);

            stm.addBatch(deleteCDA);
            stm.addBatch(insertConteudo);
            stm.addBatch(insertCDA);
            
            stm.executeBatch();
            conn.commit();
            
       } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }          
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsKey(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conteudo get(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Conteudo> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<String, Conteudo>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

}

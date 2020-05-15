    
package dss.mediacenterapp.model;

import dss.mediacenterapp.data.BibliotecaDAO;
import dss.mediacenterapp.data.UtilizadorDAO;
import dss.mediacenterapp.model.albuns.Album;
import dss.mediacenterapp.model.conteudo.Conteudo;
import dss.mediacenterapp.model.utilizadores.Utilizador;
import dss.pubsub.DSSObservable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Esta classe implementa o Facade que contém toda a lógica de negócio do sistema Media Center.
 * 
 * @author Grupo 1
 */
public class MediaCenter_LN extends DSSObservable {
    
    //**************************************************************************
    
    /**
     * Armazena o Utilizador que atualmente está no sistema.
     */
    private Utilizador utilizadorAtual;
    
    
    /**
     * DAO para aceder às tabelas utilizadores na Base de Dados.
     */
    private UtilizadorDAO utilizadorDB;

    
    /**
     * DAO para aceder às tabelas de conteúdo da Biblioteca do Media Center.
     */
    private BibliotecaDAO bibliotecaDB;
    
    
    /**
     * Guarda o caminho base para os ficheiros com extensão mp3 e mp4.
     */
    private static final String DB_Files_PATH = "DB/Content/";
    
    //**************************************************************************

    /**
     * Construtor para a classe MediaCenter_LN.
     * Este construtor será usado como padrão.
     */
    public MediaCenter_LN () {
        
        this.utilizadorDB = new UtilizadorDAO();
        this.bibliotecaDB = new BibliotecaDAO();
    }
 
    /**
     * 
     * Construtor para a classe MediaCenter_LN,
     * com os parâmetros associados às variáveis de instância.
     * 
     * @param utilizadorAtual utilizador atual
     * @param utilizadorDB DAO utilizadores
     * @param bibliotecaDB DAO biblioteca
     */
    private MediaCenter_LN(Utilizador utilizadorAtual, 
                          UtilizadorDAO utilizadorDB, 
                          BibliotecaDAO bibliotecaDB) {
        this.utilizadorAtual = utilizadorAtual;
        this.utilizadorDB = utilizadorDB;
        this.bibliotecaDB = bibliotecaDB;
    }
    
    /**
     * 
     * Construtor cópia para a classe MediaCenter_LN
     * 
     * @param mc MediaCenter_LN para cópia.
     */
    private MediaCenter_LN (MediaCenter_LN mc) {
        
        this.utilizadorAtual = mc.getUtilizadorAtual();
        this.bibliotecaDB = mc.getBibliotecaDB();
        this.utilizadorDB = mc.getUtilizadorDB();       
    }

    //**************************************************************************
    
    /**
     * Getter para ir buscar o DAO de utilizadores.
     * @return
     */
    private UtilizadorDAO getUtilizadorDB() {
        return utilizadorDB;
    }

    /**
     * Getter para ir buscar o DAO da biblioteca.
     * @return 
     */
    private BibliotecaDAO getBibliotecaDB() {
        return bibliotecaDB;
    }

    /**
     * Getter para ir buscar o caminho para os ficheiros da biblioteca.
     * @return 
     */
    private static String getDB_Files_PATH() {
        return DB_Files_PATH;
    }
    
    //**************************************************************************

    /**
     * Atribui novo utilizador atual.
     * @param user 
     */
    private void setUtilizadorAtual(Utilizador user) {
        
        this.utilizadorAtual = user;
    }

    /**
     * Atribui novo DAO para utilizadores.
     * @param utilizadorDB 
     */
    private void setUtilizadorDB(UtilizadorDAO utilizadorDB) {
        this.utilizadorDB = utilizadorDB;
    }

    /**
     * Atribui novo DAO para a biblioteca.
     * @param bibliotecaDB 
     */
    private void setBibliotecaDB(BibliotecaDAO bibliotecaDB) {
        this.bibliotecaDB = bibliotecaDB;
    }
    
    //**************************************************************************

    /**
     * Método que atribui um novo utilizador atual caso as credenciais inseridas
     * correspondam a um utilizador existem/válido
     * @param Email email fornecido pelo GUI
     * @param Password password fornecida pelo GUI
     * @return true caso o utilizador exista e a password corresponder
     */
    public boolean loginUtilizador(String Email, String Password) {
        
        boolean loginOK = false;
        
        if (this.utilizadorDB.containsKey(Email)) {
            
            Utilizador userAtual = this.utilizadorDB.get(Email);
                       
            setUtilizadorAtual(userAtual);
            
            loginOK = this.utilizadorAtual.verificaCredenciais(Password);
            
        } else {
            
            loginOK = false;
        }
       
        return loginOK;
    }

    /**
     * Método que desassocia o utilizador atual do sistema e
     * atribui, para isso, null à variável de instância
     */
    public void logoutUtilizadorAtual() {
        
        setUtilizadorAtual(null);
    }
    
    /**
     * Método que associa o utilizador atual a um utilizador com propriedades
     * de convidado.
     */
    public void loginUtilizadorAsGuest() {
        
        setUtilizadorAtual(new Utilizador());
    }

    /**
     * Para ir buscar o nome do utilizador atual
     * @return o seu nome
     */
    public String getNomeUtilizadorAtual() {
        
        return this.utilizadorAtual.getNome();
    }

    /**
     * Para ir buscar o id geral do utilizador mediante as suas permissões.
     * @return id
     */
    public String getUtilizadorAtualID() {
        
        return this.utilizadorAtual.getGeneralID();
    }

    /**
     * Para ir buscar a lista de nomes de conteúdos que a biblioteca possui.
     * @return lista de nomes do conteudo
     */
    public List<String> getListaConteudoBiblioteca() {
        
        return this.bibliotecaDB.keySet().stream().collect(Collectors.toList());
    }

    /**
     * Para ir buscar a lista de nomes dos albuns do utilizador atual
     * @return lista de nomes dos albuns
     */
    public List<String> getListaAlbunsUserAtual() {
        
        return this.utilizadorAtual.getListaAlbuns();
    }

    /**
     * Para ir buscar a lista de Conteudo associada a um Album
     * @param nomeA nome do album
     * @return lista de Conteudo associada a esse album
     */
    public List<Conteudo> getListaConteudoAlbum(String nomeA) {
        
        Album a = this.utilizadorAtual.getAlbum(nomeA);
        
        this.utilizadorAtual.adicionaAlbum(a);
        
        return a.getListaConteudo();
    }

    /**
     * Método que implementa a funcionalidade de upload, assumindo que a diretoria inserida possui
     * pelo menos um ficheiro.
     * Adiciona apenas novos ficheiros caso ainda não existam na base de dados.
     * @param nomeAlbum nome do album
     * @param elementos nomes dos ficheiros
     * @param base_path caminho para a diretoria onde se encontram os ficheiros
     * @return true caso o upload seja realizado com sucesso, ou seja, pelo menos um
     * album foi criado para o utilizador
     * @throws IOException 
     */
    public boolean upload(String nomeAlbum, List<String> elementos, String base_path) throws IOException {

        boolean uploadOK = false;
        
        Set<String> emailsAmigos = new HashSet<>();
        
        String categoriaFavorita = this.utilizadorAtual.categoriaFavorita();
            
        Album novoA = new Album(nomeAlbum, categoriaFavorita);
                
        for (String nomeC : elementos) {
            
            if (nomeC.contains("'")) {
                
                System.out.println("Invalid file (contains \''\'): " + nomeC);
                
                continue;
            }
            
            boolean isMusic = isMusicFile(nomeC);
            
            Conteudo novoC = new Conteudo(nomeC, nomeC, isMusic, !isMusic, "DB/Content/" + nomeC, categoriaFavorita);
                    
            boolean existeConteudoBiblioteca = this.bibliotecaDB.containsKey(nomeC);
            
            if (existeConteudoBiblioteca == false) {
                
                novoA.adicionaConteudo(novoC);
                
                this.adicionaFicheiro(base_path + nomeC);
                
            } else {
                
                //Existe na biblioteca do Media Center
                
                List<String> owners = this.getOwners(nomeC);
                emailsAmigos.addAll(owners);
               
                if (!this.utilizadorAtual.temConteudo(nomeC)) {
                    
                    novoA.adicionaConteudo(novoC);
                }
            }
        }
                                        
        if (novoA.hasContent()) {
            
            System.out.println("Owners: " + emailsAmigos);
            System.out.println("Album: " + novoA.toString());

            this.utilizadorAtual.adicionaAlbum(novoA);
            this.utilizadorAtual.insereAlbumNoConteudoPessoal(novoA);
                    
            emailsAmigos.remove(this.utilizadorAtual.getEmail());
    
            this.utilizadorAtual.adicionaPotenciaisAmigos(emailsAmigos);
            this.utilizadorDB.adicionaPotenciaisAmigos(this.utilizadorAtual.getEmail(), emailsAmigos);        
        
            uploadOK = true;
            
        } else {
            
            uploadOK = false;
        }
                
        return uploadOK;
    }
    
    /**
     * Método que determina se um ficheiro corresponde a uma música válida para o sistema, 
     * i.e. com extensão mp3.
     * @param filename nome do ficheiro
     * @return true caso seja valido
     */
    public boolean isMusicFile(String filename) {
        
        return FilenameUtils.getExtension(filename).equals("mp3");
    }

    /**
     * Para obter a lista de utilizadores que possuem um determinado conteúdo
     * @param nomeC nome do conteúdo
     * @return lista de emais dos utilizadores
     */
    private List<String> getOwners(String nomeC) {
        
        return this.bibliotecaDB.getOwners(nomeC);
    
    }

    /**
     * Adiciona um ficheiro para a base de dados de ficheiros
     * @param filepath caminho para o ficheiro
     * @throws IOException caso existam erros de cópia de ficheiros
     */
    private void adicionaFicheiro(String filepath) throws IOException {
                
        System.out.println("Adicionando ficheiro:");
        System.out.println("\tSource: " + filepath);
        System.out.println("\tDest: " + "DB/Content/");
        
        File source = new File(filepath);
        File dest = new File(DB_Files_PATH);
        
        FileUtils.copyFileToDirectory(source, dest);
    }

    /**
     * Para obter o utilizador atual.
     * @return clone do utilizador.
     */
    public Utilizador getUtilizadorAtual() {
        
        return this.utilizadorAtual.clone();
    }

    /**
     * Para obter a lista de nomes de conteudo do utilizador atual
     * @return lista de nomes de conteudo
     */
    public List<String> getListaConteudoUserAtual() {
        
        return this.utilizadorAtual.getListaConteudoPessoal();
    }

    /**
     * Permite alterar a categoria de um conteudo de um utilizador para uma
     * nova categoria. Tem em consideracao outros utilizadores que possam já
     * ter esse conteúdo e assim cria duas associacoes para o mesmo conteúdo mas
     * com categorias diferentes.
     * @param conteudoSelecionado nome do conteudo que se pretende alterar
     * @param catnova nova categoria
     * @param catantiga categoria antiga
     */
    public void editarConteudoUtilizadorAtual(String conteudoSelecionado, String catnova, String catantiga) {
        
        List<String> owners = this.bibliotecaDB.getRealOwners(conteudoSelecionado, catantiga);
        
        int tam = owners.size();
        
        Conteudo c = this.utilizadorAtual.getConteudo(conteudoSelecionado);
        c.setCategoria(catnova);

        if (tam == 1) {
                        
            this.utilizadorAtual.updateConteudo(c, catantiga);
        
        } else {
            
            this.utilizadorAtual.replaceConteudo(c, catantiga);
        }
    }
}

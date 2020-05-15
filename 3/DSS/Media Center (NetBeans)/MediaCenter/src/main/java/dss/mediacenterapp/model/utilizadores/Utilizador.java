
package dss.mediacenterapp.model.utilizadores;

import dss.mediacenterapp.data.ConteudoPessoalDAO;
import dss.mediacenterapp.model.albuns.Album;
import dss.mediacenterapp.model.conteudo.Conteudo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A classe Utilizador define um Utilizador do sistema.
 * 
 * @author Grupo 1
 */
public class Utilizador {
    
    //**************************************************************************
    
    /**
     * Define uma variavel final para representar um Guest sob a forma de string.
     */
    private static final String GUEST = "Guest";

    /**
     * Define uma variavel final para representar um Utilizador registado sob a forma de string.
     */
    private static final String REG = "Registred";

    /**
     * Define uma variavel final para representar um Admin sob a forma de string.
     */
    private static final String ADM = "Admin";

    /**
     * Define uma variavel final para representar um Utilizador temporário sob a forma de string.
     */
    private static final String TMP = "Temporary User";
    
    /**
     * Guarda o tipo de utilizador: REG, TMP, ADM, GUEST.
     */
    private static String typeOfUser;

    //**************************************************************************
    
    /**
     * Email do Utilizador.
     */
    private String email;
    
    /**
     * Password do Utilizador.
     */
    private String password;

    /**
     * Nome do Utilizador.
     */
    private String nome;

    /**
     * Indica se é um utilizador temporário.
     */
    private boolean isTemporaryUser;

    /**
     * Indica se é um utilizador admin.
     */
    private boolean isAdministrator;

    /**
     * DAO para aceder ao conteudo pessoal de um utilizador.
     */
    private ConteudoPessoalDAO conteudoPessoal;
    
    /**
     * Albuns do utilizador.
     */
    private Map<String, Album> albuns;
    
    /**
     * Potenciais amigos do utilizador.
     */
    private Set<String> potenciaisAmigos;
    
    //**************************************************************************

    /**
     * Construtor parametrizado de Utilizador.
     * @param mail email
     * @param password password
     * @param nome nome 
     * @param isTempraryUser indica se é temporario
     * @param isAdministrator indica se é administrador
     */
    public Utilizador(String mail, 
                      String password, 
                      String nome, 
                      boolean isTempraryUser, 
                      boolean isAdministrator) {
        
        if (isAdministrator) {
            
            this.typeOfUser = "Administrator";
        
        } else if (isTemporaryUser) {
            
            this.typeOfUser = "Temporary User";
        
        } else {
            
            this.typeOfUser = "Registred";
        }
        
        this.email = mail;
        this.password = password;
        this.nome = nome;
        this.isTemporaryUser = isTempraryUser;
        this.isAdministrator = isAdministrator;
    
        this.conteudoPessoal = new ConteudoPessoalDAO(this.email);
        
        this.albuns = new HashMap<>();
        this.potenciaisAmigos = new HashSet<>();
    }

    /**
     * Construtor vazio de Utilizador.
     * Define um Utilizador com um Convidado.
     */
    public Utilizador() {
        
        this.typeOfUser = GUEST;
        
        this.email = "guest@mediacenter";
        this.nome = "Guest";
        this.password = "n/a";
        this.isAdministrator = false;
        this.isTemporaryUser = false;
        
        this.albuns = new HashMap<>();
        this.potenciaisAmigos = new HashSet<>();
    }
    
    /**
     * Construtor cópia de um Utilizador.
     * @param copia novo Utilizador.
     */
    public Utilizador (Utilizador copia) {
        
        this.nome = copia.getNome();
        this.email = copia.getEmail();
        this.password = copia.getPassword();
        this.typeOfUser = copia.getTypeOfUser();
        this.potenciaisAmigos = copia.getPotenciaisAmigos();
    }
    
    //**************************************************************************

    /**
     * Obtém uma cópia de um utilizador.
     * @return 
     */
    public Utilizador clone() {
        
        return new Utilizador(this);
    }
    
    /**
     * Retorna uma representação de um Utilizador sob a forma de uma String.
     * @return 
     */
    public String toString () {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Type of User: " + this.typeOfUser + "\n");
        sb.append("E-Mail: " + this.email + "\n");
        sb.append("Nome: " + this.nome + "\n");
        sb.append("Password: " + this.password + "\n");
        sb.append("Temporary: " + this.isTemporaryUser + "\n");
        sb.append("Admin: " + this.isAdministrator + "\n");
        
        return sb.toString();
    }

    /**
     * Indica se dois utilizadores são iguais.
     * @param obj Utilizador para comparação.
     * @return true caso sejam iguais.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utilizador other = (Utilizador) obj;
        if (this.isTemporaryUser != other.isTemporaryUser) {
            return false;
        }
        if (this.isAdministrator != other.isAdministrator) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.albuns, other.albuns)) {
            return false;
        }
        if (!Objects.equals(this.potenciaisAmigos, other.potenciaisAmigos)) {
            return false;
        }
        
        return true;
    }    

    //**************************************************************************
    
    /**
     * Retorna o id geral de um utilizador
     * @return 
     */
    public String getGeneralID() {
    
        return this.typeOfUser + ": " + this.email;
    }
    
    /**
     * Retorna se um utilizador é convidado.
     */
    public void setUserAsGuest() {
        
        this.typeOfUser = GUEST;
    }

    /**
     * Retorna se um Utilizador é temporário.
     * @return 
     */
    public boolean isTemporaryUser() {
        return isTemporaryUser;
    }

    /**
     * Retorna se um Utilizador é administrador
     * @return 
     */
    public boolean isAdministrator() {
        return isAdministrator;
    }
    
    //**************************************************************************
    
    /**
     * Verifica se a password em parametro corresponde à do utilizador.
     * @param Password password
     * @return true caso a password se verifique.
     */
    public boolean verificaCredenciais(String Password) {
        
        return this.password.equals(Password);
    }

    /**
     * Retorna o nome do Utilizador.
     * @return 
     */
    public String getNome() {
        
        return this.nome;
    }
    
    /**
     * Retorna o email do Utilizador.
     * @return 
     */
    public String getEmail() {
        
        return this.email;
    }

    /**
     * Retorna a lista de nomes dos albuns do Utilizador
     * @return 
     */
    public List<String> getListaAlbuns() {
        
        return this.conteudoPessoal.getListaAlbuns();
    }

    /**
     * Retorna um album do utilizador cujo nome é passado como parametro.
     * @param nomeAlbum nome do album
     * @return album com esse nome
     */
    public Album getAlbum(String nomeAlbum) {
        
        return this.conteudoPessoal.getAlbum(nomeAlbum);
    }

    /**
     * Adiciona album à coleção do Utilizador
     * @param a 
     */
    public void adicionaAlbum(Album a) {
        
        this.albuns.put(a.getNome(), a);
    }

    /**
     * Retorna a categoria mais comum de um utilizador
     * @return 
     */
    public String categoriaFavorita() {
        
        return this.conteudoPessoal.categoriaFavorita();
    }

    /**
     * Indica se o Utilizador tem um dado conteudo
     * @param nomeC nome do conteudo
     * @return 
     */
    public boolean temConteudo(String nomeC) {
        
        return this.conteudoPessoal.containsConteudo(nomeC);
    }

    /**
     * Adiciona uma coleção de emails de potenciais amigos ao utilizador
     * @param emailsAmigos 
     */
    public void adicionaPotenciaisAmigos(Set<String> emailsAmigos) {
        
        this.potenciaisAmigos.addAll(emailsAmigos);
    }
    
    /**
     * Adiciona amigo à coleção do utilizador
     * @param email 
     */
    public void adicionaAmigo(String email) {
        
        this.potenciaisAmigos.add(email);
    }

    /**
     * Insere um novo album no conteudo pessoal do utilizador (na base de dados)
     * @param novoA 
     */
    public void insereAlbumNoConteudoPessoal(Album novoA) {
        
        this.conteudoPessoal.adicionaAlbum(novoA);
    }

    /**
     * Retorna a password do Utilizador.
     * @return 
     */
    public String getPassword() {
        
        return this.password;
    }
    
    /**
     * Retorna o tipo do utilizador definido.
     * @return 
     */
    public String getTypeOfUser() {
        
        return this.typeOfUser;
    }

    /**
     * Retorna uma coleção de emails dos potenciais amigos do Utilizador.
     * @return 
     */
    public Set<String> getPotenciaisAmigos() {
        
        return new HashSet<>(this.potenciaisAmigos);
    }

    /**
     * Retorna uma lista de nomes dos conteudos do utilizador.
     * @return 
     */
    public List<String> getListaConteudoPessoal() {
        
        return this.conteudoPessoal.getListaConteudo();
    }

    /**
     * Retorna um conteudo do utilizador associado a um dado nome.
     * @param conteudoSelecionado nome do conteudo
     * @return conteudo da base de dados
     */
    public Conteudo getConteudo(String conteudoSelecionado) {
        
        return this.conteudoPessoal.getConteudo(conteudoSelecionado);
    }

    /**
     * Atualiza o conteudo da base de dados, alterando a sua categoria.
     * @param c conteudo com categoria alterada
     * @param catant categoria antiga
     */
    public void updateConteudo(Conteudo c, String catant) {
        
        this.conteudoPessoal.updateConteudo(c, catant);
    }

    /**
     * Atualiza o conteudo da base de dados do album do utilizador, alterando a sua categoria.
     * @param c conteudo com categoria alterada
     * @param catant categoria antiga
     */
    public void replaceConteudo(Conteudo c, String catantiga) {
        
        this.conteudoPessoal.replaceConteudo(c, catantiga);
    }
    
}

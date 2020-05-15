
package dss.mediacenterapp.model.albuns;

import dss.mediacenterapp.model.conteudo.Conteudo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A classe Album define uma coleção de conteúdo que pertence a um utilizador.
 * 
 * @author Grupo 1
 */
public class Album {
    
    //**************************************************************************
    
    /**
     * Nome do Album
     */
    private String nome;
    
    /**
     * Categoria do Album
     */
    private String categoria;

    /**
     * Conteudo do Album, mapeado pelo nome
     */
    private Map<String, Conteudo> conteudo;
    
    //**************************************************************************

    /**
     * Construtor parameterizado para a classe Album.
     * @param nome nome do album
     * @param cat categoria do album
     */
    public Album (String nome, String cat) {
        
        this.nome = nome;
        this.categoria = cat;
        
        this.conteudo = new HashMap<>();
    }

    /**
     * Construtor vazio para a classe Album.
     */
    public Album() {
        
        this.nome = "";
        this.categoria = "";
        
        this.conteudo = new HashMap<>();
    }

    /**
     * Construtor cópia de Album.
     * @param a 
     */
    private Album(Album a) {
        
        this.nome = a.getNome();
        this.categoria = a.getCategoria();
        this.conteudo = a.getConteudo();
    }
    
    //**************************************************************************
    
    /**
     * Para obter a representação do conteúdo no formato String
     * @return representação do conteudo.
     */
    public String toString() {
        
        
        return this.nome + " - (" + this.categoria + "): \n" + this.conteudo.toString();
    }
    
    /**
     * Metodo para gerar uma hash mediante o nome do conteudo.
     * Não utilizado.
     * @return 
     */
    @Override
    public int hashCode() {
        
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.nome);
        return hash;
                
    }

    /**
     * Para verificar se dois albuns sao iguais
     * @param obj objeto album para comparacao
     * @return true caso seja iguais
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
        final Album other = (Album) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.categoria, other.categoria)) {
            return false;
        }
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Retorne um clone do Album.
     * @return 
     */
    public Album clone () {
        
        return new Album(this);
    }

    //**************************************************************************

    /**
     * Retorna o conteudo de um album.
     * @return 
     */
    public Map<String, Conteudo> getConteudo() {
        return this.conteudo.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().clone(), (e1, e2) -> e2, HashMap::new));
    }
    
    /**
     * Retorna o conteudo sob o formato de uma lista
     * @return 
     */
    public List<Conteudo> getListaConteudo() {
        
        return this.conteudo.values().stream().map(Conteudo::clone).collect(Collectors.toList());
    }
    
    /**
     * Retorna o nome do album
     * @return 
     */
    public String getNome() {
        
        return this.nome;
    }

    /**
     * Retorna a categoria do album
     * @return 
     */
    public String getCategoria() {
        
        return this.categoria;
    }
    
    /**
     * Altera o nome do album
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Altera a categoria do album
     * @param categoria nova categoria
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Altera o conteudo da colecao do album
     * @param conteudo colecao nova de conteudo
     */
    public void setConteudo(Map<String, Conteudo> conteudo) {
        this.conteudo = conteudo;
    }

    //**************************************************************************

    /**
     * Adiciona uma coleção de conteudo ao album
     * @param conteudo List de conteudo
     */
    public void addAllConteudo(List<Conteudo> conteudo) {
        
        for (Conteudo c: conteudo) {
            
            this.conteudo.put(c.getNome(), c);
        }
    }   

    /**
     * Adiciona um conteudo à coleção do album
     * @param novoC novo conteudo
     */
    public void adicionaConteudo(Conteudo novoC) {
        
        this.conteudo.put(novoC.getNome(), novoC);
    }
    
    /**
     * Indica se o conteudo sem conteudo ou não.
     * @return true caso tenha
     */
    public boolean hasContent() {
        
        return this.conteudo.size() > 0;
    }
}

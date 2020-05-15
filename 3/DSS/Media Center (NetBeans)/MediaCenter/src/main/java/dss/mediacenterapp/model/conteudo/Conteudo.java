
package dss.mediacenterapp.model.conteudo;

import java.util.Objects;

/**
 * A classe Album define uma coleção de conteúdo que pertence a um utilizador.
 * 
 * @author Grupo 1
 */
public class Conteudo {
    
    //**************************************************************************
    
    /**
     * Nome do conteudo
     */
    private String nome;
    
    /**
     * Artista associado ao album
     */
    private String artista;
    
    /**
     * Booleano que indica se o conteudo é uma musica
     */
    private boolean isMusic;

    /**
     * Booleano que indica se o conteudo é um video
     */
    private boolean isVideo;
    
    /**
     * Caminho para o ficheiro do conteudo
     */
    private String filepath;
    
    /**
     * Categoria do conteudo
     */
    private String categoria;
    
    //**************************************************************************

    /**
     * Construtor parametrizado de conteudo.
     * @param nome nome do conteudo
     * @param artista artista do conteudo
     * @param music indica se é musica
     * @param video indica se é video
     * @param path caminho para o ficheiro
     * @param cat categoria do conteudo
     */
    public Conteudo (String nome,
                     String artista,
                     boolean music,
                     boolean video,
                     String path,
                     String cat) {
        
        
        this.nome = nome;
        this.artista = artista;
        this.isMusic = music;
        this.isVideo = video;
        this.filepath = path;
        this.categoria = cat;
    }
    
    /**
     * Construtor cópia de conteudo.
     * @param novo conteudo novo.
     */
    public Conteudo (Conteudo novo) {
        
        this.nome = novo.getNome();
        this.artista = novo.getArtista();
        this.categoria = novo.getCategoria();
        this.isMusic = novo.getIsMusic();
        this.isVideo = novo.getIsVideo();
        this.filepath = novo.getFilePath();
    }
    
    /**
     * Construtor vazio de conteudo.
     */
    public Conteudo () {
        
        this.nome = "";
        this.artista = "";
        this.categoria = "";
        this.isMusic = false;
        this.isVideo = false;
        this.filepath = "";
    }
    
    //**************************************************************************
    
    /**
     * Retorna um clone do Conteudo.
     * @return 
     */
    public Conteudo clone() {
        
        return new Conteudo(this);
    }

    /**
     * Representacao do conteudo sob o formato de uma String.
     * @return 
     */
    public String toString() {
        
        return "Conteudo: " + this.nome + (this.isMusic ? " [Music]": " [Video]") + "\n";
    } 

    /**
     * Retorna uma hash para o nome do conteudo.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    /**
     * Retorna verdade se dois Conteudos são iguais.
     * @param obj Conteudo para comparar
     * @return true caso sejam iguais
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
        final Conteudo other = (Conteudo) obj;
        if (this.isMusic != other.isMusic) {
            return false;
        }
        if (this.isVideo != other.isVideo) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.artista, other.artista)) {
            return false;
        }
        if (!Objects.equals(this.filepath, other.filepath)) {
            return false;
        }
        if (!Objects.equals(this.categoria, other.categoria)) {
            return false;
        }
        return true;
    }
    
    //**************************************************************************

    /**
     * Retorna o nome do conteudo.
     * @return 
     */
    public String getNome() {
        
        return this.nome;
    }

    /**
     * Retorna o artista do conteudo.
     * @return 
     */
    public String getArtista() {
        
        return this.artista;
    }
    
    /**
     * Retorna a categoria do conteudo
     * @return 
     */
    public String getCategoria() {
        
        return this.categoria;
    }

    /**
     * Retorna true se o conteudo é uma musica.
     * @return 
     */
    public boolean getIsMusic() {
        
        return this.isMusic;
    }

    /**
     * Retorna true se o conteudo é uma video.
     * @return 
     */
    public boolean getIsVideo() {
        
        return this.isVideo;
    }
    
    /**
     * Retorna o caminho do ficheiro para o conteudo.
     * @return 
     */
    public String getFilePath() {
        
        return this.filepath;
    }
    
    /**
     * Altera a categoria do conteudo
     * @param catnova 
     */
    public void setCategoria(String catnova) {
        
        this.categoria = catnova;
    }

    /**
     * Altera o nome do conteudo
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Altera o artista do conteudo
     * @param artista novo artista
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Define um conteudo como sendo musica.
     * @param isMusic 
     */
    public void setIsMusic(boolean isMusic) {
        this.isMusic = isMusic;
    }

    /**
     * Define um conteudo como video
     * @param isVideo 
     */
    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    /**
     * Define um caminho para o conteudo.
     * @param filepath 
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

}

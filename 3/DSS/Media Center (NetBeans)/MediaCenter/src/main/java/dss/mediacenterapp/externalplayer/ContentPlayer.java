
package dss.mediacenterapp.externalplayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.io.FilenameUtils;

/**
 * Implementa os métodos necessários para reproduzir ficheiros em threads.
 * 
 * @author Grupo 1
 */
public class ContentPlayer implements Runnable {

    //**************************************************************************
    
    /**
     * player para reproduzir o ficheiro
     */
    private Player player;
    /**
     * guarda a FIS associada ao ficheiro
     */
    private BufferedInputStream BIS;
    /**
     * guarda o BIS associada ao ficheiro
     */
    private FileInputStream FIS;
    
    /**
     * determina se um conteudo pode ser resumido
     */
    private boolean canResume;
    /**
     * guarda o caminho para o ficheiro a reproduzir
     */
    private String path;
    /**
     * guarda o numero total de frames que o conteudo tem
     */
    private int totalFrames;
    /**
     * determina se um conteudo foi parado
     */
    private int stopped;
    /**
     * determina se a ultima reprodução foi realizado com sucesso
     */
    private boolean playWorked;
    
    /**
     * Thread onde o ficheiro é reproduzido
     */
    private Thread currentThreadRunning;

    //**************************************************************************

    /**
     * Construtor vazio de um ContentPlayer
     */
    public ContentPlayer(){

        this.player = null;
        this.FIS = null;
        this.playWorked = false;
        this.BIS = null;
        this.path = null;
        this.totalFrames = 0;
        this.stopped = 0;
        this.canResume = false;
        this.currentThreadRunning = null;
    }

    //**************************************************************************
    
    /**
     * Determina se um conteudo acabou a sua reprodução
     * @return true caso tenha acabado
     */
    public boolean playingFinished() {
        
        try {
            
            if (FIS.available() == 0) {

                return true;
            }            
            
        } catch (Exception e) {
            
            return false;
        }
        
        return false;
    }
    
    /**
     * Termina a reprodução do conteudo
     */
    public void end() {
        
        if (this.currentThreadRunning == null || this.player == null) return;
        
        this.currentThreadRunning.stop();
            
        this.player.close();
    }
    
    /**
     * Determina se um conteudo pode ser resumido
     * @return 
     */
    public boolean canResume(){
        
        return this.canResume;
    }

    /**
     * Altera o caminho para um dado ficheiro
     * @param path 
     */
    public void setPath(String path){
        
        this.path = path;
    }

    /**
     * Pausa a reprodução de um conteudo
     */
    public void pause(){
 
        try{
        
            this.stopped = FIS.available();
            this.player.close();
            this.FIS = null;
            this.BIS = null;
            this.player = null;
 
            if(playWorked == true) 
                canResume = true;

        }catch(Exception e){

            System.out.println("Error pausing music");
        }
    }

    /**
     * Retoma a reprodução de um conteudo
     */
    public void resume(){
        
        if(this.canResume == false) 
            return;
        
        if(play(this.totalFrames - this.stopped)) {
            
            this.canResume = false;
        }
    }
    
    /**
     * Reproduz um conteudo a partir de um dado frame
     * @param frame frame inicial (0 indica inicio)
     * @return true caso o conteudo exista e a reprodução foi feita
     */
    public boolean play(int frame) {
        
        this.playWorked = true;
        this.canResume = false;
        
        try{
        
            this.FIS = new FileInputStream(path);
            this.totalFrames = FIS.available();
            
            if(frame >= 0) {
                
                this.FIS.skip(frame);
            }
            
            this.BIS = new BufferedInputStream(FIS);
            this.player = new Player(BIS);

            Thread runThread = new Thread(new Runnable() {
                @Override
                public void run() {
              
                    try {
                        player.play();
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(ContentPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            this.currentThreadRunning = runThread;
            this.currentThreadRunning.start();
                        
        } catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "Não foi possível reproduzir " + this.path);
           
            this.playWorked = false;
        }
        
        return this.playWorked;
         
    }
    
    /**
     * Para reproduzir conteudo num contexto multi-threaded, usado para reproduzir albuns.
     * @param frame frame inicial (0 indica inicio)
     * @return true caso o conteudo consiga ser reproduzido
     */
    public boolean playMT(int frame){

        this.playWorked = true;
        this.canResume = false;
        
        try{
        
            this.FIS = new FileInputStream(path);
            this.totalFrames = FIS.available();
            
            if(frame >= 0) {
                
                this.FIS.skip(frame);
            }
            
            this.BIS = new BufferedInputStream(FIS);
            this.player = new Player(BIS);
                                  
            synchronized(this) {
                player.play();
            }
                        
        } catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "Não foi possível reproduzir " + this.path);
           
            this.playWorked = false;
        }
        
        return this.playWorked;
    }
        
    private Object getExtensionOfFile(String list_content_selected) {
        
        return FilenameUtils.getExtension(list_content_selected);
    }
    
    /**
     * Metodo que reproduz um conteudo numa thread à parte
     */
    @Override
    public void run() {
        
        playMT(0);
    }
    
}

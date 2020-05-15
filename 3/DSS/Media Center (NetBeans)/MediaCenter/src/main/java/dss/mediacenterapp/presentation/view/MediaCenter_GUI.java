
package dss.mediacenterapp.presentation.view;

import dss.mediacenterapp.presentation.controller.MediaCenterController;
import dss.pubsub.DSSObservable;
import dss.pubsub.DSSObserver;
import dss.mediacenterapp.externalplayer.ContentPlayer;
import dss.mediacenterapp.model.conteudo.Conteudo;
import dss.mediacenterapp.model.utilizadores.Utilizador;
import java.awt.Desktop;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.io.FilenameUtils;

/*------------------------------------------------------------------------------*/

/**
 * Esta classe contém toda a implementação da interface com o Utilizador, desde 
 * a criação dos diferentes Menus (JFrames) e da interação do utilizador com
 * os mesmos. Utiliza toda a API do modelo disponibilizada pelo controlador.
 * 
 * @author Grupo 1
 */
public class MediaCenter_GUI extends javax.swing.JFrame implements DSSObserver {

    /*--------------------------------------------------------------------------*/

    /**
     * Define o tamanho das janelas de todo o GUI.
     */
    private static final int WINDOW_X = 900, WINDOW_Y = 600;
    
    /*--------------------------------------------------------------------------*/

    /**
     * Controlador da aplicação.
     */
    private MediaCenterController controller;
    
    /*--------------------------------------------------------------------------*/
    
    /**
     * Guarda o player que contém o conteudo a ser reproduzido no momento.
     */
    private ContentPlayer currentContentPlayer;
    
    /**
     * Guarda a thread de reprodução do álbum completo. 
     */
    private Thread albumThread;  
    
    /**
     * Define se está a ser reproduzido um album ou não.
     */
    private boolean playingAlbum;
    
    /**
     * Guarda o último caminho para o ficheiro que foi feito upload.
     */
    private String lastUploadPath;
    
    /*--------------------------------------------------------------------------*/
    
    /**
     * Construtor parameterizado do GUI.
     *
     * @param controller controlador da aplicação.
     */
    public MediaCenter_GUI(MediaCenterController controller) {
        
        this.controller = controller;
        
        this.currentContentPlayer = new ContentPlayer();
        
        this.playingAlbum = false;
    }
       
    /**
     * Define o formato inicial das janelas que compõem todo o GUI.
     */
    public void setInitialFormat() {
        
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));
        this.setTitle("Menu Inicial");
      
        this.UPLOAD_filechooser.setApproveButtonText("Listar conteúdo");
        this.UPLOAD_filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        this.Menu_CHOOSEalbum.setLocationRelativeTo(null);
        this.Menu_CHOOSEalbum.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_CHOOSEalbum.setVisible(false);
        this.Menu_CHOOSEalbum.setTitle("Álbuns");   
        
        this.Menu_UPLOAD.setLocationRelativeTo(null);
        this.Menu_UPLOAD.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_UPLOAD.setVisible(false);
        this.Menu_UPLOAD.setTitle("Menu Upload");   
        
        this.Menu_OPTIONS.setLocationRelativeTo(null);
        this.Menu_OPTIONS.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_OPTIONS.setVisible(false);
        this.Menu_OPTIONS.setTitle("Menu Principal");
        
        this.Menu_LOGINform.setLocationRelativeTo(null);
        this.Menu_LOGINform.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_LOGINform.setVisible(false);
        this.Menu_LOGINform.setTitle("Menu de Login");
     
        this.Menu_REPRODUZIRoptions.setLocationRelativeTo(null);
        this.Menu_REPRODUZIRoptions.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_REPRODUZIRoptions.setVisible(false);
        this.Menu_REPRODUZIRoptions.setTitle("Menu de Reproduzir");
        
        this.Menu_REPRODUZIR_biblioteca.setLocationRelativeTo(null);
        this.Menu_REPRODUZIR_biblioteca.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_REPRODUZIR_biblioteca.setVisible(false);
        this.Menu_REPRODUZIR_biblioteca.setTitle("Biblioteca do Media Center");

        this.Menu_REPRODUZIR_album.setLocationRelativeTo(null);
        this.Menu_REPRODUZIR_album.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_REPRODUZIR_album.setVisible(false);
        this.Menu_REPRODUZIR_album.setTitle("Conteúdo do Álbum");        
        
        this.Menu_PROFILE.setLocationRelativeTo(null);
        this.Menu_PROFILE.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_PROFILE.setVisible(false);
        this.Menu_PROFILE.setTitle("O meu perfil");        

        this.Menu_AlterarCategoriaConteudo.setLocationRelativeTo(null);
        this.Menu_AlterarCategoriaConteudo.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));       
        this.Menu_AlterarCategoriaConteudo.setVisible(false);
        this.Menu_AlterarCategoriaConteudo.setTitle("Alterar categoria de conteúdo");                
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_OPTIONS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_LOGINform.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_REPRODUZIRoptions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_REPRODUZIR_biblioteca.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_CHOOSEalbum.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_REPRODUZIR_album.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_UPLOAD.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_PROFILE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.Menu_AlterarCategoriaConteudo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void run() {
        
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);        
        
        this.initComponents();
        this.setInitialFormat();
        this.setVisible(true);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu_OPTIONS = new javax.swing.JFrame();
        OPT_reproduzir = new javax.swing.JButton();
        OPT_note = new javax.swing.JLabel();
        OPT_useridlabel = new javax.swing.JLabel();
        OPT_logout = new javax.swing.JButton();
        OPT_upload = new javax.swing.JButton();
        OPT_alterarcategoriaconteudo = new javax.swing.JButton();
        OPT_ButtonVerperfil = new javax.swing.JButton();
        OPT_tittle = new javax.swing.JLabel();
        Menu_LOGINform = new javax.swing.JFrame();
        LOGIN_emailfield = new javax.swing.JTextField();
        LOGIN_emaillabel = new javax.swing.JLabel();
        LOGIN_passlabel = new javax.swing.JLabel();
        LOGIN_ButtonLogin = new javax.swing.JButton();
        LOGIN_ButtonBack = new javax.swing.JButton();
        LOGIN_passfield = new javax.swing.JPasswordField();
        LOGIN_tittle = new javax.swing.JLabel();
        Menu_REPRODUZIRoptions = new javax.swing.JFrame();
        REPOPT_note = new javax.swing.JLabel();
        REPOPT_ButtonBibliotecaMC = new javax.swing.JButton();
        REPOPT_ButtonAlbuns = new javax.swing.JButton();
        REPOPT_ButtonBack = new javax.swing.JButton();
        REPOPT_tittle = new javax.swing.JLabel();
        Menu_REPRODUZIR_biblioteca = new javax.swing.JFrame();
        REPBIBL_scroll = new javax.swing.JScrollPane();
        REPBIBL_listaconteudo = new javax.swing.JList<>();
        REPBIBL_ButtonPlay = new javax.swing.JButton();
        REPBIBL_ButtonPause = new javax.swing.JButton();
        REPBIBL_ButtonResume = new javax.swing.JButton();
        REPBIBL_ButtonBack = new javax.swing.JButton();
        REPBIBL_currentcontentlabel = new javax.swing.JLabel();
        REPOPT_tittle1 = new javax.swing.JLabel();
        Menu_CHOOSEalbum = new javax.swing.JFrame();
        CHOOSEALB_tittle = new javax.swing.JLabel();
        CHOOSEALB_scroll = new javax.swing.JScrollPane();
        CHOOSEALB_listaalbuns = new javax.swing.JList<>();
        CHOOSEALB_ButtonRep = new javax.swing.JButton();
        CHOOSEALB_ButtonBack = new javax.swing.JButton();
        Menu_REPRODUZIR_album = new javax.swing.JFrame();
        REPALBUM_tittle = new javax.swing.JLabel();
        REPALBUM_scroll = new javax.swing.JScrollPane();
        REPALBUM_listaconteudo = new javax.swing.JList<>();
        REPALBUM_ButtonPlay = new javax.swing.JButton();
        REPALBUM_ButtonPause = new javax.swing.JButton();
        REPALBUM_ButtonResume = new javax.swing.JButton();
        REPALBUM_ButtonBack = new javax.swing.JButton();
        REPALBUM_CurrentContent = new javax.swing.JLabel();
        REPALBUM_albumname = new javax.swing.JLabel();
        REPALBUM_ButtonPlayAll = new javax.swing.JButton();
        REPALBUM_ButtonStopAlbum = new javax.swing.JButton();
        REPALBUM_randomizelist = new javax.swing.JButton();
        Menu_UPLOAD = new javax.swing.JFrame();
        UPLOAD_tittle = new javax.swing.JLabel();
        UPLOAD_caminholabel = new javax.swing.JLabel();
        UPLOAD_filechooser = new javax.swing.JFileChooser();
        UPLOAD_scroll = new javax.swing.JScrollPane();
        UPLOAD_listaconteudo = new javax.swing.JList<>();
        UPLOAD_caminholabel1 = new javax.swing.JLabel();
        UPLOAD_ButtonUpload = new javax.swing.JButton();
        UPLOAD_ButtonBack = new javax.swing.JButton();
        Menu_PROFILE = new javax.swing.JFrame();
        PROFILE_tittle = new javax.swing.JLabel();
        PROFILE_nomelabel = new javax.swing.JLabel();
        PROFILE_emaillabel = new javax.swing.JLabel();
        PROFILE_passwordllabel = new javax.swing.JLabel();
        PROFILE_statuslabel = new javax.swing.JLabel();
        PROFILE_emailfield = new javax.swing.JLabel();
        PROFILE_namefield1 = new javax.swing.JLabel();
        PROFILE_statusfield = new javax.swing.JLabel();
        PROFILE_passwordfield1 = new javax.swing.JLabel();
        PROFILE_amigoslabel = new javax.swing.JLabel();
        PROFILE_scrollamigos = new javax.swing.JScrollPane();
        PROFILE_listaamigos = new javax.swing.JList<>();
        PROFILE_ButtonBack = new javax.swing.JButton();
        Menu_AlterarCategoriaConteudo = new javax.swing.JFrame();
        ALTERAR_tittle = new javax.swing.JLabel();
        ALTERAR_selecionarconteudolabel = new javax.swing.JLabel();
        ALTERAR_scroll = new javax.swing.JScrollPane();
        ALTERAR_listaconteudo = new javax.swing.JList<>();
        ALTERAR_ButtonAlterarCat = new javax.swing.JButton();
        ALTERAR_checkbox_rock = new javax.swing.JCheckBox();
        ALTERAR_selecionarcategorialabel = new javax.swing.JLabel();
        ALTERAR_checkbox_metal = new javax.swing.JCheckBox();
        ALTERAR_checkbox_jazz = new javax.swing.JCheckBox();
        ALTERAR_checkbox_pop = new javax.swing.JCheckBox();
        ALTERAR_checkbox_blues = new javax.swing.JCheckBox();
        ALTERAR_checkbox_video = new javax.swing.JCheckBox();
        ALTERAR_ButtonBack = new javax.swing.JButton();
        MAIN_ButtonLogin = new javax.swing.JButton();
        MAIN_ButtonGuest = new javax.swing.JButton();
        MAIN_newuserlabel = new javax.swing.JLabel();
        MAIN_ButtonRegistar = new javax.swing.JButton();
        MAIN_tittle = new javax.swing.JLabel();

        Menu_OPTIONS.setResizable(false);
        Menu_OPTIONS.setSize(new java.awt.Dimension(900, 600));

        OPT_reproduzir.setBackground(java.awt.SystemColor.activeCaptionBorder);
        OPT_reproduzir.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        OPT_reproduzir.setText("Reproduzir");
        OPT_reproduzir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPT_reproduzirActionPerformed(evt);
            }
        });

        OPT_note.setFont(new java.awt.Font("Tlwg Typo", 2, 18)); // NOI18N
        OPT_note.setText("Nota: Outras funcionalidades encontram-se em desenvolvimento...");

        OPT_useridlabel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        OPT_useridlabel.setText("User ID Label");

        OPT_logout.setText("Log-Out");
        OPT_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPT_logoutActionPerformed(evt);
            }
        });

        OPT_upload.setBackground(java.awt.SystemColor.activeCaptionBorder);
        OPT_upload.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        OPT_upload.setText("Upload");
        OPT_upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPT_uploadActionPerformed(evt);
            }
        });

        OPT_alterarcategoriaconteudo.setBackground(java.awt.SystemColor.activeCaptionBorder);
        OPT_alterarcategoriaconteudo.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        OPT_alterarcategoriaconteudo.setText("Alterar Categoria de Conteudo");
        OPT_alterarcategoriaconteudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPT_alterarcategoriaconteudoActionPerformed(evt);
            }
        });

        OPT_ButtonVerperfil.setText("Visualizar o meu perfil");
        OPT_ButtonVerperfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPT_ButtonVerperfilActionPerformed(evt);
            }
        });

        OPT_tittle.setIcon(new javax.swing.ImageIcon("resources/imgs/main-tittle.png"));

        javax.swing.GroupLayout Menu_OPTIONSLayout = new javax.swing.GroupLayout(Menu_OPTIONS.getContentPane());
        Menu_OPTIONS.getContentPane().setLayout(Menu_OPTIONSLayout);
        Menu_OPTIONSLayout.setHorizontalGroup(
            Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_OPTIONSLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(OPT_useridlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_OPTIONSLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addGroup(Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_OPTIONSLayout.createSequentialGroup()
                        .addGroup(Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OPT_upload, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OPT_reproduzir, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OPT_alterarcategoriaconteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(239, 239, 239))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_OPTIONSLayout.createSequentialGroup()
                        .addGroup(Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(OPT_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OPT_ButtonVerperfil, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_OPTIONSLayout.createSequentialGroup()
                        .addComponent(OPT_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_OPTIONSLayout.createSequentialGroup()
                        .addComponent(OPT_note)
                        .addGap(80, 80, 80))))
        );
        Menu_OPTIONSLayout.setVerticalGroup(
            Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_OPTIONSLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(Menu_OPTIONSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OPT_useridlabel)
                    .addComponent(OPT_ButtonVerperfil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(OPT_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OPT_reproduzir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OPT_upload, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OPT_alterarcategoriaconteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(OPT_note)
                .addGap(18, 18, 18)
                .addComponent(OPT_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        Menu_LOGINform.setSize(new java.awt.Dimension(900, 600));

        LOGIN_emailfield.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        LOGIN_emailfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LOGIN_emailfieldActionPerformed(evt);
            }
        });

        LOGIN_emaillabel.setFont(new java.awt.Font("Ubuntu", 2, 18)); // NOI18N
        LOGIN_emaillabel.setText("E-Mail:");

        LOGIN_passlabel.setFont(new java.awt.Font("Ubuntu", 2, 18)); // NOI18N
        LOGIN_passlabel.setText("Password:");

        LOGIN_ButtonLogin.setText("Log-In");
        LOGIN_ButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LOGIN_ButtonLoginActionPerformed(evt);
            }
        });

        LOGIN_ButtonBack.setText("Back");
        LOGIN_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LOGIN_ButtonBackActionPerformed(evt);
            }
        });

        LOGIN_passfield.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        LOGIN_passfield.setText("jPasswordField1");

        LOGIN_tittle.setIcon(new javax.swing.ImageIcon("resources/imgs/main-tittle.png"));

        javax.swing.GroupLayout Menu_LOGINformLayout = new javax.swing.GroupLayout(Menu_LOGINform.getContentPane());
        Menu_LOGINform.getContentPane().setLayout(Menu_LOGINformLayout);
        Menu_LOGINformLayout.setHorizontalGroup(
            Menu_LOGINformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(LOGIN_ButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LOGIN_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                .addGroup(Menu_LOGINformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addGroup(Menu_LOGINformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LOGIN_emaillabel, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LOGIN_emailfield, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                            .addComponent(LOGIN_passlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LOGIN_passfield)))
                    .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(LOGIN_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        Menu_LOGINformLayout.setVerticalGroup(
            Menu_LOGINformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(LOGIN_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LOGIN_emaillabel)
                .addGap(18, 18, 18)
                .addComponent(LOGIN_emailfield, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(LOGIN_passlabel)
                .addGroup(Menu_LOGINformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_LOGINformLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LOGIN_passfield, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(LOGIN_ButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_LOGINformLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(LOGIN_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );

        Menu_REPRODUZIRoptions.setSize(new java.awt.Dimension(900, 600));

        REPOPT_note.setFont(new java.awt.Font("Tlwg Typo", 2, 18)); // NOI18N
        REPOPT_note.setText("Nota: Outras funcionalidades encontram-se em desenvolvimento...");

        REPOPT_ButtonBibliotecaMC.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPOPT_ButtonBibliotecaMC.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPOPT_ButtonBibliotecaMC.setText("Biblioteca do Media Center");
        REPOPT_ButtonBibliotecaMC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPOPT_ButtonBibliotecaMCActionPerformed(evt);
            }
        });

        REPOPT_ButtonAlbuns.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPOPT_ButtonAlbuns.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPOPT_ButtonAlbuns.setText("Albuns");
        REPOPT_ButtonAlbuns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPOPT_ButtonAlbunsActionPerformed(evt);
            }
        });

        REPOPT_ButtonBack.setText("Back");
        REPOPT_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPOPT_ButtonBackActionPerformed(evt);
            }
        });

        REPOPT_tittle.setIcon(new javax.swing.ImageIcon("resources/imgs/reproduzir-tittle.png"));

        javax.swing.GroupLayout Menu_REPRODUZIRoptionsLayout = new javax.swing.GroupLayout(Menu_REPRODUZIRoptions.getContentPane());
        Menu_REPRODUZIRoptions.getContentPane().setLayout(Menu_REPRODUZIRoptionsLayout);
        Menu_REPRODUZIRoptionsLayout.setHorizontalGroup(
            Menu_REPRODUZIRoptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIRoptionsLayout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addGroup(Menu_REPRODUZIRoptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIRoptionsLayout.createSequentialGroup()
                        .addComponent(REPOPT_note)
                        .addGap(101, 101, 101))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIRoptionsLayout.createSequentialGroup()
                        .addComponent(REPOPT_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIRoptionsLayout.createSequentialGroup()
                        .addGroup(Menu_REPRODUZIRoptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(REPOPT_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPOPT_ButtonAlbuns, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPOPT_ButtonBibliotecaMC, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(247, 247, 247))))
        );
        Menu_REPRODUZIRoptionsLayout.setVerticalGroup(
            Menu_REPRODUZIRoptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_REPRODUZIRoptionsLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(REPOPT_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(REPOPT_ButtonBibliotecaMC, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(REPOPT_ButtonAlbuns, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(REPOPT_note)
                .addGap(29, 29, 29)
                .addComponent(REPOPT_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        Menu_REPRODUZIR_biblioteca.setSize(new java.awt.Dimension(900, 600));

        REPBIBL_listaconteudo.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        REPBIBL_listaconteudo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        REPBIBL_scroll.setViewportView(REPBIBL_listaconteudo);

        REPBIBL_ButtonPlay.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPBIBL_ButtonPlay.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPBIBL_ButtonPlay.setText("Play");
        REPBIBL_ButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPBIBL_ButtonPlayActionPerformed(evt);
            }
        });

        REPBIBL_ButtonPause.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPBIBL_ButtonPause.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPBIBL_ButtonPause.setText("Pause");
        REPBIBL_ButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPBIBL_ButtonPauseActionPerformed(evt);
            }
        });

        REPBIBL_ButtonResume.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPBIBL_ButtonResume.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPBIBL_ButtonResume.setText("Resume");
        REPBIBL_ButtonResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPBIBL_ButtonResumeActionPerformed(evt);
            }
        });

        REPBIBL_ButtonBack.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPBIBL_ButtonBack.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPBIBL_ButtonBack.setText("Back");
        REPBIBL_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPBIBL_ButtonBackActionPerformed(evt);
            }
        });

        REPBIBL_currentcontentlabel.setFont(new java.awt.Font("Ubuntu", 2, 18)); // NOI18N
        REPBIBL_currentcontentlabel.setText("Nenhum conteúdo foi ainda selecionado da lista disponível...");

        REPOPT_tittle1.setIcon(new javax.swing.ImageIcon("resources/imgs/biblioteca-tittle.png"));

        javax.swing.GroupLayout Menu_REPRODUZIR_bibliotecaLayout = new javax.swing.GroupLayout(Menu_REPRODUZIR_biblioteca.getContentPane());
        Menu_REPRODUZIR_biblioteca.getContentPane().setLayout(Menu_REPRODUZIR_bibliotecaLayout);
        Menu_REPRODUZIR_bibliotecaLayout.setHorizontalGroup(
            Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                        .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                                .addComponent(REPBIBL_currentcontentlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(REPBIBL_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                                .addComponent(REPBIBL_ButtonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(REPBIBL_ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157)
                                .addComponent(REPBIBL_ButtonResume, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(REPBIBL_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(119, 119, 119))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                        .addComponent(REPOPT_tittle1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(241, 241, 241))))
        );
        Menu_REPRODUZIR_bibliotecaLayout.setVerticalGroup(
            Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(REPOPT_tittle1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(REPBIBL_ButtonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(REPBIBL_ButtonResume, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(REPBIBL_ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(REPBIBL_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(Menu_REPRODUZIR_bibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(REPBIBL_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(REPBIBL_currentcontentlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        Menu_CHOOSEalbum.setSize(new java.awt.Dimension(900, 600));

        CHOOSEALB_tittle.setFont(new java.awt.Font("URW Palladio L", 3, 90)); // NOI18N
        CHOOSEALB_tittle.setForeground(new java.awt.Color(1, 1, 1));
        CHOOSEALB_tittle.setText("Álbuns");

        CHOOSEALB_listaalbuns.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        CHOOSEALB_listaalbuns.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        CHOOSEALB_scroll.setViewportView(CHOOSEALB_listaalbuns);

        CHOOSEALB_ButtonRep.setBackground(java.awt.SystemColor.activeCaptionBorder);
        CHOOSEALB_ButtonRep.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        CHOOSEALB_ButtonRep.setText("Reproduzir ");
        CHOOSEALB_ButtonRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CHOOSEALB_ButtonRepActionPerformed(evt);
            }
        });

        CHOOSEALB_ButtonBack.setBackground(java.awt.SystemColor.activeCaptionBorder);
        CHOOSEALB_ButtonBack.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        CHOOSEALB_ButtonBack.setText("Back");
        CHOOSEALB_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CHOOSEALB_ButtonBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Menu_CHOOSEalbumLayout = new javax.swing.GroupLayout(Menu_CHOOSEalbum.getContentPane());
        Menu_CHOOSEalbum.getContentPane().setLayout(Menu_CHOOSEalbumLayout);
        Menu_CHOOSEalbumLayout.setHorizontalGroup(
            Menu_CHOOSEalbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_CHOOSEalbumLayout.createSequentialGroup()
                .addGap(0, 198, Short.MAX_VALUE)
                .addComponent(CHOOSEALB_ButtonRep, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(CHOOSEALB_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
            .addGroup(Menu_CHOOSEalbumLayout.createSequentialGroup()
                .addGroup(Menu_CHOOSEalbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_CHOOSEalbumLayout.createSequentialGroup()
                        .addGap(295, 295, 295)
                        .addComponent(CHOOSEALB_tittle))
                    .addGroup(Menu_CHOOSEalbumLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(CHOOSEALB_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Menu_CHOOSEalbumLayout.setVerticalGroup(
            Menu_CHOOSEalbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_CHOOSEalbumLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(CHOOSEALB_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(CHOOSEALB_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addGroup(Menu_CHOOSEalbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CHOOSEALB_ButtonRep, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CHOOSEALB_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        Menu_REPRODUZIR_album.setSize(new java.awt.Dimension(900, 600));

        REPALBUM_tittle.setFont(new java.awt.Font("URW Palladio L", 3, 90)); // NOI18N
        REPALBUM_tittle.setForeground(new java.awt.Color(1, 1, 1));
        REPALBUM_tittle.setText("Álbum");

        REPALBUM_listaconteudo.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        REPALBUM_listaconteudo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        REPALBUM_scroll.setViewportView(REPALBUM_listaconteudo);

        REPALBUM_ButtonPlay.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonPlay.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonPlay.setText("Play");
        REPALBUM_ButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonPlayActionPerformed(evt);
            }
        });

        REPALBUM_ButtonPause.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonPause.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonPause.setText("Pause");
        REPALBUM_ButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonPauseActionPerformed(evt);
            }
        });

        REPALBUM_ButtonResume.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonResume.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonResume.setText("Resume");
        REPALBUM_ButtonResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonResumeActionPerformed(evt);
            }
        });

        REPALBUM_ButtonBack.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonBack.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonBack.setText("Back");
        REPALBUM_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonBackActionPerformed(evt);
            }
        });

        REPALBUM_CurrentContent.setFont(new java.awt.Font("Ubuntu", 2, 18)); // NOI18N
        REPALBUM_CurrentContent.setText("Último conteúdo:");

        REPALBUM_albumname.setFont(new java.awt.Font("Ubuntu", 2, 24)); // NOI18N
        REPALBUM_albumname.setText("album_name");

        REPALBUM_ButtonPlayAll.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonPlayAll.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonPlayAll.setText("Play All");
        REPALBUM_ButtonPlayAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonPlayAllActionPerformed(evt);
            }
        });

        REPALBUM_ButtonStopAlbum.setBackground(java.awt.SystemColor.activeCaptionBorder);
        REPALBUM_ButtonStopAlbum.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        REPALBUM_ButtonStopAlbum.setText("Stop");
        REPALBUM_ButtonStopAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_ButtonStopAlbumActionPerformed(evt);
            }
        });

        REPALBUM_randomizelist.setText("Randomize");
        REPALBUM_randomizelist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REPALBUM_randomizelistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Menu_REPRODUZIR_albumLayout = new javax.swing.GroupLayout(Menu_REPRODUZIR_album.getContentPane());
        Menu_REPRODUZIR_album.getContentPane().setLayout(Menu_REPRODUZIR_albumLayout);
        Menu_REPRODUZIR_albumLayout.setHorizontalGroup(
            Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                        .addComponent(REPALBUM_tittle)
                        .addGap(40, 40, 40)
                        .addComponent(REPALBUM_albumname, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                        .addComponent(REPALBUM_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(REPALBUM_ButtonPlay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPALBUM_ButtonPause, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPALBUM_ButtonResume, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPALBUM_ButtonPlayAll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(REPALBUM_CurrentContent, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(REPALBUM_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(REPALBUM_ButtonStopAlbum)
                .addGap(20, 20, 20))
            .addGroup(Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                .addGap(349, 349, 349)
                .addComponent(REPALBUM_randomizelist)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Menu_REPRODUZIR_albumLayout.setVerticalGroup(
            Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(REPALBUM_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(REPALBUM_albumname))
                .addGap(27, 27, 27)
                .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_REPRODUZIR_albumLayout.createSequentialGroup()
                        .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(REPALBUM_ButtonPlayAll, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(REPALBUM_ButtonStopAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(REPALBUM_ButtonPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(REPALBUM_ButtonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(REPALBUM_ButtonResume, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(REPALBUM_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(REPALBUM_randomizelist)
                .addGap(37, 37, 37)
                .addGroup(Menu_REPRODUZIR_albumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(REPALBUM_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(REPALBUM_CurrentContent, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        Menu_UPLOAD.setSize(new java.awt.Dimension(900, 600));

        UPLOAD_tittle.setFont(new java.awt.Font("URW Palladio L", 3, 60)); // NOI18N
        UPLOAD_tittle.setForeground(new java.awt.Color(1, 1, 1));
        UPLOAD_tittle.setText("Upload");

        UPLOAD_caminholabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        UPLOAD_caminholabel.setText("Selecione o caminho para o conteúdo:");

        UPLOAD_filechooser.setForeground(new java.awt.Color(1, 1, 1));
        UPLOAD_filechooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UPLOAD_filechooserActionPerformed(evt);
            }
        });

        UPLOAD_listaconteudo.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        UPLOAD_listaconteudo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        UPLOAD_scroll.setViewportView(UPLOAD_listaconteudo);

        UPLOAD_caminholabel1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        UPLOAD_caminholabel1.setText("Conteúdo disponível (.mp3/.mp4):");

        UPLOAD_ButtonUpload.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        UPLOAD_ButtonUpload.setText("Carregar ficheiros");
        UPLOAD_ButtonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UPLOAD_ButtonUploadActionPerformed(evt);
            }
        });

        UPLOAD_ButtonBack.setText("Back");
        UPLOAD_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UPLOAD_ButtonBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Menu_UPLOADLayout = new javax.swing.GroupLayout(Menu_UPLOAD.getContentPane());
        Menu_UPLOAD.getContentPane().setLayout(Menu_UPLOADLayout);
        Menu_UPLOADLayout.setHorizontalGroup(
            Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UPLOADLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UPLOAD_caminholabel)
                    .addComponent(UPLOAD_filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(UPLOAD_caminholabel1)
                        .addComponent(UPLOAD_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UPLOADLayout.createSequentialGroup()
                        .addComponent(UPLOAD_ButtonUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)))
                .addGap(71, 71, 71))
            .addGroup(Menu_UPLOADLayout.createSequentialGroup()
                .addGap(337, 337, 337)
                .addComponent(UPLOAD_tittle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_UPLOADLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(UPLOAD_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        Menu_UPLOADLayout.setVerticalGroup(
            Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_UPLOADLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UPLOAD_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Menu_UPLOADLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_UPLOADLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(UPLOAD_caminholabel)
                        .addGap(18, 18, 18)
                        .addComponent(UPLOAD_filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Menu_UPLOADLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(UPLOAD_caminholabel1)
                        .addGap(18, 18, 18)
                        .addComponent(UPLOAD_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(UPLOAD_ButtonUpload)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(UPLOAD_ButtonBack)
                .addGap(26, 26, 26))
        );

        Menu_PROFILE.setSize(new java.awt.Dimension(900, 600));

        PROFILE_tittle.setFont(new java.awt.Font("URW Palladio L", 3, 60)); // NOI18N
        PROFILE_tittle.setForeground(new java.awt.Color(1, 1, 1));
        PROFILE_tittle.setText("O meu Perfil");

        PROFILE_nomelabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        PROFILE_nomelabel.setText("Nome:");

        PROFILE_emaillabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        PROFILE_emaillabel.setText("E-mail:");

        PROFILE_passwordllabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        PROFILE_passwordllabel.setText("Password:");

        PROFILE_statuslabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        PROFILE_statuslabel.setText("Account Status:");

        PROFILE_emailfield.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        PROFILE_emailfield.setText("\"email\"");

        PROFILE_namefield1.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        PROFILE_namefield1.setText("\"nome\"");

        PROFILE_statusfield.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        PROFILE_statusfield.setText("\"registred | temporary | admin\"");

        PROFILE_passwordfield1.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        PROFILE_passwordfield1.setText("\"password\"");

        PROFILE_amigoslabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        PROFILE_amigoslabel.setText("Potenciais amigos:");

        PROFILE_listaamigos.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        PROFILE_listaamigos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        PROFILE_scrollamigos.setViewportView(PROFILE_listaamigos);

        PROFILE_ButtonBack.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        PROFILE_ButtonBack.setText("Back");
        PROFILE_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PROFILE_ButtonBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Menu_PROFILELayout = new javax.swing.GroupLayout(Menu_PROFILE.getContentPane());
        Menu_PROFILE.getContentPane().setLayout(Menu_PROFILELayout);
        Menu_PROFILELayout.setHorizontalGroup(
            Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_PROFILELayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PROFILE_amigoslabel)
                    .addGroup(Menu_PROFILELayout.createSequentialGroup()
                        .addComponent(PROFILE_statuslabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PROFILE_statusfield, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PROFILE_scrollamigos, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Menu_PROFILELayout.createSequentialGroup()
                        .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PROFILE_emaillabel)
                            .addComponent(PROFILE_nomelabel))
                        .addGap(18, 18, 18)
                        .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PROFILE_namefield1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PROFILE_emailfield, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Menu_PROFILELayout.createSequentialGroup()
                            .addComponent(PROFILE_tittle)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PROFILE_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Menu_PROFILELayout.createSequentialGroup()
                            .addComponent(PROFILE_passwordllabel)
                            .addGap(18, 18, 18)
                            .addComponent(PROFILE_passwordfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        Menu_PROFILELayout.setVerticalGroup(
            Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_PROFILELayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PROFILE_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PROFILE_ButtonBack))
                .addGap(45, 45, 45)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PROFILE_nomelabel)
                    .addComponent(PROFILE_namefield1))
                .addGap(18, 18, 18)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PROFILE_emaillabel)
                    .addComponent(PROFILE_emailfield))
                .addGap(18, 18, 18)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PROFILE_passwordllabel)
                    .addComponent(PROFILE_passwordfield1))
                .addGap(18, 18, 18)
                .addGroup(Menu_PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PROFILE_statuslabel)
                    .addComponent(PROFILE_statusfield))
                .addGap(18, 18, 18)
                .addComponent(PROFILE_amigoslabel)
                .addGap(18, 18, 18)
                .addComponent(PROFILE_scrollamigos, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        Menu_AlterarCategoriaConteudo.setSize(new java.awt.Dimension(900, 600));

        ALTERAR_tittle.setFont(new java.awt.Font("URW Palladio L", 3, 50)); // NOI18N
        ALTERAR_tittle.setForeground(new java.awt.Color(1, 1, 1));
        ALTERAR_tittle.setText("Alterar categoria de conteúdo");

        ALTERAR_selecionarconteudolabel.setFont(new java.awt.Font("Ubuntu", 2, 20)); // NOI18N
        ALTERAR_selecionarconteudolabel.setText("Selecione um conteúdo da sua coleção pessoal:");

        ALTERAR_listaconteudo.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        ALTERAR_listaconteudo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ALTERAR_scroll.setViewportView(ALTERAR_listaconteudo);

        ALTERAR_ButtonAlterarCat.setBackground(java.awt.SystemColor.activeCaptionBorder);
        ALTERAR_ButtonAlterarCat.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        ALTERAR_ButtonAlterarCat.setText("Alterar categoria");
        ALTERAR_ButtonAlterarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_ButtonAlterarCatActionPerformed(evt);
            }
        });

        ALTERAR_checkbox_rock.setText("Rock");
        ALTERAR_checkbox_rock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_rockActionPerformed(evt);
            }
        });

        ALTERAR_selecionarcategorialabel.setFont(new java.awt.Font("Ubuntu", 2, 20)); // NOI18N
        ALTERAR_selecionarcategorialabel.setText("Selecione apenas uma categoria:");

        ALTERAR_checkbox_metal.setText("Metal");
        ALTERAR_checkbox_metal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_metalActionPerformed(evt);
            }
        });

        ALTERAR_checkbox_jazz.setText("Jazz");
        ALTERAR_checkbox_jazz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_jazzActionPerformed(evt);
            }
        });

        ALTERAR_checkbox_pop.setText("Pop");
        ALTERAR_checkbox_pop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_popActionPerformed(evt);
            }
        });

        ALTERAR_checkbox_blues.setText("Blues");
        ALTERAR_checkbox_blues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_bluesActionPerformed(evt);
            }
        });

        ALTERAR_checkbox_video.setText("Video");
        ALTERAR_checkbox_video.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_checkbox_videoActionPerformed(evt);
            }
        });

        ALTERAR_ButtonBack.setText("Back");
        ALTERAR_ButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ALTERAR_ButtonBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Menu_AlterarCategoriaConteudoLayout = new javax.swing.GroupLayout(Menu_AlterarCategoriaConteudo.getContentPane());
        Menu_AlterarCategoriaConteudo.getContentPane().setLayout(Menu_AlterarCategoriaConteudoLayout);
        Menu_AlterarCategoriaConteudoLayout.setHorizontalGroup(
            Menu_AlterarCategoriaConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                .addGroup(Menu_AlterarCategoriaConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(ALTERAR_tittle))
                    .addGroup(Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(ALTERAR_selecionarconteudolabel))
                    .addGroup(Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(ALTERAR_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(Menu_AlterarCategoriaConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addComponent(ALTERAR_selecionarcategorialabel)
                        .addGap(314, 314, 314))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addComponent(ALTERAR_checkbox_rock)
                        .addGap(18, 18, 18)
                        .addComponent(ALTERAR_checkbox_metal)
                        .addGap(18, 18, 18)
                        .addComponent(ALTERAR_checkbox_jazz)
                        .addGap(18, 18, 18)
                        .addComponent(ALTERAR_checkbox_pop)
                        .addGap(18, 18, 18)
                        .addComponent(ALTERAR_checkbox_blues)
                        .addGap(18, 18, 18)
                        .addComponent(ALTERAR_checkbox_video)
                        .addGap(216, 216, 216))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addComponent(ALTERAR_ButtonAlterarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(319, 319, 319))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                        .addComponent(ALTERAR_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );
        Menu_AlterarCategoriaConteudoLayout.setVerticalGroup(
            Menu_AlterarCategoriaConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu_AlterarCategoriaConteudoLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(ALTERAR_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ALTERAR_selecionarconteudolabel)
                .addGap(18, 18, 18)
                .addComponent(ALTERAR_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ALTERAR_selecionarcategorialabel)
                .addGap(53, 53, 53)
                .addGroup(Menu_AlterarCategoriaConteudoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ALTERAR_checkbox_rock)
                    .addComponent(ALTERAR_checkbox_metal)
                    .addComponent(ALTERAR_checkbox_jazz)
                    .addComponent(ALTERAR_checkbox_pop)
                    .addComponent(ALTERAR_checkbox_blues)
                    .addComponent(ALTERAR_checkbox_video))
                .addGap(18, 18, 18)
                .addComponent(ALTERAR_ButtonAlterarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ALTERAR_ButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(java.awt.Color.black);
        setResizable(false);
        setSize(new java.awt.Dimension(900, 60));

        MAIN_ButtonLogin.setBackground(java.awt.SystemColor.activeCaptionBorder);
        MAIN_ButtonLogin.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        MAIN_ButtonLogin.setText("Log-In");
        MAIN_ButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MAIN_ButtonLoginActionPerformed(evt);
            }
        });

        MAIN_ButtonGuest.setBackground(java.awt.SystemColor.activeCaptionBorder);
        MAIN_ButtonGuest.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        MAIN_ButtonGuest.setText("Convidado");
        MAIN_ButtonGuest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MAIN_ButtonGuestActionPerformed(evt);
            }
        });

        MAIN_newuserlabel.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        MAIN_newuserlabel.setText("Novo Utente?");

        MAIN_ButtonRegistar.setBackground(java.awt.SystemColor.activeCaptionBorder);
        MAIN_ButtonRegistar.setFont(new java.awt.Font("Padauk Book", 0, 24)); // NOI18N
        MAIN_ButtonRegistar.setText("Registar");
        MAIN_ButtonRegistar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MAIN_ButtonRegistarActionPerformed(evt);
            }
        });

        MAIN_tittle.setIcon(new javax.swing.ImageIcon("resources/imgs/main-tittle.png"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(191, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MAIN_ButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MAIN_ButtonGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MAIN_ButtonRegistar, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(MAIN_newuserlabel)))
                        .addGap(311, 311, 311))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(MAIN_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(159, 159, 159))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(MAIN_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(MAIN_ButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(MAIN_ButtonGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(MAIN_newuserlabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MAIN_ButtonRegistar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MAIN_ButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MAIN_ButtonLoginActionPerformed
        
        this.setVisible(false);
        this.Menu_LOGINform.setVisible(true);
        
        this.LOGIN_emailfield.setText("");
        this.LOGIN_passfield.setText("");
        
        this.OPT_alterarcategoriaconteudo.setEnabled(true);
        this.OPT_upload.setEnabled(true);
        this.REPOPT_ButtonAlbuns.setEnabled(true);
        this.OPT_logout.setText("Logout");
        
        this.PROFILE_listaamigos.setVisible(true);
        this.PROFILE_scrollamigos.setVisible(true);
        this.PROFILE_amigoslabel.setVisible(true);

    }//GEN-LAST:event_MAIN_ButtonLoginActionPerformed

    private void MAIN_ButtonGuestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MAIN_ButtonGuestActionPerformed

        this.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);
        
        this.controller.loginUtilizadorAsGuest();
        
        this.OPT_useridlabel.setText(this.controller.getUtilizadorAtualID());
        
        this.OPT_alterarcategoriaconteudo.setEnabled(false);
        this.OPT_upload.setEnabled(false);
        this.REPOPT_ButtonAlbuns.setEnabled(false);
        this.OPT_logout.setText("Back");
    }//GEN-LAST:event_MAIN_ButtonGuestActionPerformed

    private void MAIN_ButtonRegistarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MAIN_ButtonRegistarActionPerformed

        JOptionPane.showMessageDialog(this, "Funcionalidade de momento indisponível!");
    }//GEN-LAST:event_MAIN_ButtonRegistarActionPerformed

    private void OPT_reproduzirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPT_reproduzirActionPerformed

        this.updateBibliotecaList();
        
        //Se não for um guest
        if (!this.controller.getUtilizadorAtualID().equals("Guest: guest@mediacenter")) {
                    
            this.updateAlbunsList();
        }
        
        this.Menu_OPTIONS.setVisible(false);
        this.Menu_REPRODUZIRoptions.setVisible(true);
    }//GEN-LAST:event_OPT_reproduzirActionPerformed

    private void OPT_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPT_logoutActionPerformed
        
        if (this.controller.getNomeUtilizadorAtual().equals("Guest")) {
            
            //...é um guest
            
            this.Menu_OPTIONS.setVisible(false);
            this.setVisible(true);        

        } else {
                    
            this.Menu_OPTIONS.setVisible(false);
            this.setVisible(true);        

            JOptionPane.showMessageDialog(this, "Terminou sessão com sucesso!");
        }

        this.controller.logoutUtilizadorAtual();        
    }//GEN-LAST:event_OPT_logoutActionPerformed

    private void LOGIN_emailfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LOGIN_emailfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LOGIN_emailfieldActionPerformed

    private void LOGIN_ButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LOGIN_ButtonLoginActionPerformed
        
        String email = this.LOGIN_emailfield.getText();
        String password = this.LOGIN_passfield.getText();
        
        boolean loginok = this.controller.loginUtilizador(email, password);
        
        if (loginok == true) {
            
            this.Menu_OPTIONS.setVisible(true);
            this.Menu_LOGINform.setVisible(false);
            
            this.OPT_useridlabel.setText(this.controller.getUtilizadorAtualID());
                            
        } else {
            
            this.LOGIN_emailfield.setText("");
            this.LOGIN_passfield.setText("");
            
            JOptionPane.showMessageDialog(this, "As credenciais inseridas estão erradas! Tente novamente!");
        }
        
    }//GEN-LAST:event_LOGIN_ButtonLoginActionPerformed

    private void LOGIN_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LOGIN_ButtonBackActionPerformed
        
        this.Menu_LOGINform.setVisible(false);
        this.setVisible(true);
        this.LOGIN_emailfield.setText("");
        this.LOGIN_passfield.setText("");
    }//GEN-LAST:event_LOGIN_ButtonBackActionPerformed

    private void OPT_uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPT_uploadActionPerformed

        DefaultListModel model = new DefaultListModel();
        model.clear();
        this.UPLOAD_listaconteudo.setModel(model);
        
        this.Menu_OPTIONS.setVisible(false);
        this.Menu_UPLOAD.setVisible(true);
    }//GEN-LAST:event_OPT_uploadActionPerformed

    private void REPOPT_ButtonBibliotecaMCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPOPT_ButtonBibliotecaMCActionPerformed
        
        this.Menu_REPRODUZIRoptions.setVisible(false);
        this.Menu_REPRODUZIR_biblioteca.setVisible(true);
    }//GEN-LAST:event_REPOPT_ButtonBibliotecaMCActionPerformed

    private void REPOPT_ButtonAlbunsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPOPT_ButtonAlbunsActionPerformed
        
        this.Menu_REPRODUZIRoptions.setVisible(false);
        this.Menu_CHOOSEalbum.setVisible(true);
    }//GEN-LAST:event_REPOPT_ButtonAlbunsActionPerformed

    private void REPOPT_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPOPT_ButtonBackActionPerformed
        
        this.Menu_REPRODUZIRoptions.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);
    }//GEN-LAST:event_REPOPT_ButtonBackActionPerformed

    private void REPBIBL_ButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPBIBL_ButtonPlayActionPerformed
        
        String conteudoSelecionado = null;
        
        try {
            
            conteudoSelecionado = this.REPBIBL_listaconteudo.getSelectedValue().toString();
                                    
        } catch (Exception e) {
           
            JOptionPane.showMessageDialog(this.Menu_REPRODUZIR_biblioteca, "Selecione um conteúdo!");
            return;
        }
        
        try {
            
            if (this.getExtensionOfFile(conteudoSelecionado).equals("mp4")) {
                
                String video_path = "DB/Content/" + conteudoSelecionado;

                Desktop.getDesktop().open(new File(video_path));
                            
            } else {
            
                this.currentContentPlayer.end();

                String music_path = "DB/Content/" + conteudoSelecionado;
               
                boolean canPlayContent;

                this.currentContentPlayer.setPath(music_path);

                canPlayContent = this.currentContentPlayer.play(0);

                if (canPlayContent == true) {

                    this.REPBIBL_currentcontentlabel.setText("Última música: " + conteudoSelecionado);

                } else {

                    this.REPBIBL_currentcontentlabel.setText("O ficheiro selecionado não pode ser reproduzido.");
                }

            }
            
        } catch (Exception ex) {
        
            JOptionPane.showMessageDialog(null, "O ficheiro selecionado não pode ser reproduzido.");
        }        
    }//GEN-LAST:event_REPBIBL_ButtonPlayActionPerformed

    private void REPBIBL_ButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPBIBL_ButtonPauseActionPerformed
        
        this.currentContentPlayer.pause();
    }//GEN-LAST:event_REPBIBL_ButtonPauseActionPerformed

    private void REPBIBL_ButtonResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPBIBL_ButtonResumeActionPerformed
        
        this.currentContentPlayer.resume();
    }//GEN-LAST:event_REPBIBL_ButtonResumeActionPerformed

    private void REPBIBL_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPBIBL_ButtonBackActionPerformed
        
        this.Menu_REPRODUZIR_biblioteca.setVisible(false);
        this.Menu_REPRODUZIRoptions.setVisible(true);
        this.currentContentPlayer.end();
    }//GEN-LAST:event_REPBIBL_ButtonBackActionPerformed

    private void CHOOSEALB_ButtonRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CHOOSEALB_ButtonRepActionPerformed
        
        String nomeAlbumComCategoria = null;
        String nomeAlbum = null;        
        
        try {
            
            nomeAlbumComCategoria = this.CHOOSEALB_listaalbuns.getSelectedValue().toString();
                                  
        } catch (Exception e) {
           
            JOptionPane.showMessageDialog(this.Menu_CHOOSEalbum, "Selecione um álbum!");
            return;
        }

        String parts[] = nomeAlbumComCategoria.split(" \\[");
        nomeAlbum = parts[0];
        
        List<Conteudo> conteudoAlbum = this.controller.getListaConteudoAlbum(nomeAlbum);

        List<String> nomesConteudo = conteudoAlbum.stream().map(c -> c.getNome() + " [" + c.getCategoria() + "]").collect(Collectors.toList());
        
        //--------------------------------------------------------------------------------
                
        DefaultListModel model = new DefaultListModel();
        model.clear();
        
        model.addAll(nomesConteudo);
        
        this.REPALBUM_listaconteudo.setModel(model);
         
         //--------------------------------------------------------------------------------

        this.REPALBUM_albumname.setText(nomeAlbum);
        
        this.Menu_CHOOSEalbum.setVisible(false);
        this.Menu_REPRODUZIR_album.setVisible(true);
         
        this.REPALBUM_ButtonStopAlbum.setEnabled(false);
 
        this.REPALBUM_CurrentContent.setText("Último conteúdo:");
        this.playingAlbum = false;
    }//GEN-LAST:event_CHOOSEALB_ButtonRepActionPerformed

    private void REPALBUM_ButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonPlayActionPerformed
        
        if (this.playingAlbum) return;
        
        String conteudoSelecionado = null;
        
        try {
            
            conteudoSelecionado = this.REPALBUM_listaconteudo.getSelectedValue().toString();
            
        } catch (Exception e) {
           
            
            JOptionPane.showMessageDialog(this.Menu_REPRODUZIR_album, "Selecione um conteúdo!");
            return;
        }
        
        try {
                
            String parts[] = conteudoSelecionado.split(" \\[");
            
            conteudoSelecionado = parts[0];
            
            if (this.getExtensionOfFile(conteudoSelecionado).equals("mp4")) {
                
                String video_path = "DB/Content/" + conteudoSelecionado;

                Desktop.getDesktop().open(new File(video_path));
                            
            } else {
            
                this.currentContentPlayer.end();

                String music_path = "DB/Content/" + conteudoSelecionado;
               
                boolean canPlayContent;

                this.currentContentPlayer.setPath(music_path);
                               
                canPlayContent = this.currentContentPlayer.play(0);
                
                this.REPALBUM_CurrentContent.setText("Último conteúdo: " + parts[0]);

            }
            
        } catch (Exception ex) {
        
            JOptionPane.showMessageDialog(null, "O ficheiro selecionado não pode ser reproduzido.");
        }                        
    }//GEN-LAST:event_REPALBUM_ButtonPlayActionPerformed

    private void REPALBUM_ButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonPauseActionPerformed
        
        if (this.playingAlbum) return;
        
        this.currentContentPlayer.pause();
    }//GEN-LAST:event_REPALBUM_ButtonPauseActionPerformed

    private void REPALBUM_ButtonResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonResumeActionPerformed

        if (this.playingAlbum) return;
        
        this.currentContentPlayer.resume();
    }//GEN-LAST:event_REPALBUM_ButtonResumeActionPerformed

    private void REPALBUM_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonBackActionPerformed

        this.currentContentPlayer.end();
        if (this.albumThread != null) this.albumThread.stop();
        
        this.Menu_REPRODUZIR_album.setVisible(false);
        this.Menu_CHOOSEalbum.setVisible(true);
    }//GEN-LAST:event_REPALBUM_ButtonBackActionPerformed

    private void CHOOSEALB_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CHOOSEALB_ButtonBackActionPerformed
        
        this.Menu_CHOOSEalbum.setVisible(false);
        this.Menu_REPRODUZIRoptions.setVisible(true);
    }//GEN-LAST:event_CHOOSEALB_ButtonBackActionPerformed

    //-----------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------
    
    private void REPALBUM_ButtonPlayAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonPlayAllActionPerformed

        this.playingAlbum = true;
        this.REPALBUM_ButtonStopAlbum.setEnabled(true);
        
        //-----------------------------------------------------------------------
        
        List<String> elementos = new ArrayList<>();
        String tmp;
        
        for (int i = 0; i < this.REPALBUM_listaconteudo.getModel().getSize(); i++) {
            
            tmp = this.REPALBUM_listaconteudo.getModel().getElementAt(i);
            
            String parts[] = tmp.split(" \\[");
            
            elementos.add(parts[0]);
        }
        
        //-----------------------------------------------------------------------

        this.albumThread = new Thread(new Runnable() {
            @Override
            public void run() {
          
                playAlbum(elementos);
                
            }
        });

        this.albumThread.start();
    }//GEN-LAST:event_REPALBUM_ButtonPlayAllActionPerformed
    
    //-----------------------------------------------------------------------------------------

    public void playAlbum(List<String> content) {
        
        System.out.println("Album playing...");
        
        for (int nr = 0; nr < content.size(); nr++) {
            
            this.REPALBUM_CurrentContent.setText("Último conteúdo: " + content.get(nr));
            
            playContent(content.get(nr));
            
        }
        
        this.playingAlbum = false;
    }
    
    //-----------------------------------------------------------------------------------------

    public void playContent(String filename) {
        
        try {
                
            if (this.getExtensionOfFile(filename).equals("mp4")) {
                
                Desktop.getDesktop().open(new File("DB/Content/" + filename));
                            
            } else {
            
                this.currentContentPlayer.end();
               
                this.currentContentPlayer.setPath("DB/Content/" + filename);

                boolean canPlayContent = this.currentContentPlayer.playMT(0);

            }
            
        } catch (Exception ex) {
        
            JOptionPane.showMessageDialog(null, "O ficheiro selecionado não pode ser reproduzido.");
        }   

    }
    
    private void REPALBUM_ButtonStopAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_ButtonStopAlbumActionPerformed
        
        System.out.println("Album stopped...");

        this.REPALBUM_ButtonStopAlbum.setEnabled(false);
                
        this.playingAlbum = false;
        this.albumThread.stop();
    }//GEN-LAST:event_REPALBUM_ButtonStopAlbumActionPerformed

    private void REPALBUM_randomizelistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REPALBUM_randomizelistActionPerformed
        
        List<String> elementos = new ArrayList<>();
        
        for (int i = 0; i < this.REPALBUM_listaconteudo.getModel().getSize(); i++) {
            
            elementos.add(this.REPALBUM_listaconteudo.getModel().getElementAt(i));
        }
  
        //-----------------------------------------------------------------------

       
        Collections.shuffle(elementos);
            
        DefaultListModel model = new DefaultListModel();
        model.clear();
        model.addAll(elementos);
        this.REPALBUM_listaconteudo.setModel(model);
 
    }//GEN-LAST:event_REPALBUM_randomizelistActionPerformed

    private void OPT_alterarcategoriaconteudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPT_alterarcategoriaconteudoActionPerformed
        
        this.Menu_OPTIONS.setVisible(false);
        this.Menu_AlterarCategoriaConteudo.setVisible(true);
        
        DefaultListModel model = new DefaultListModel();
        model.clear();
        
        List<String> conteudoPessoal = this.controller.getListaConteudoUserAtual();
        
        model.addAll(conteudoPessoal);       
        
        this.ALTERAR_listaconteudo.setModel(model);
    }//GEN-LAST:event_OPT_alterarcategoriaconteudoActionPerformed

    private void UPLOAD_ButtonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UPLOAD_ButtonUploadActionPerformed
       
        List<String> elementos = new ArrayList<>();
        
        for (int i = 0; i < this.UPLOAD_listaconteudo.getModel().getSize(); i++) {
            
            elementos.add(this.UPLOAD_listaconteudo.getModel().getElementAt(i));
        }        

        if (elementos.size() > 0) {
            
            String nomeAlbum = JOptionPane.showInputDialog(this.Menu_UPLOAD, "Nome do álbum novo: ");
        
            String base_path = this.lastUploadPath;
            
            try {
                
                
                boolean ok = this.controller.upload(nomeAlbum, elementos, base_path);
                
                if (ok) {
                    
                    this.Menu_UPLOAD.setVisible(false);
                    this.Menu_OPTIONS.setVisible(true);
                    
                    JOptionPane.showMessageDialog(this.Menu_OPTIONS, "O upload foi efetuado com sucesso!");
                
                } else {
                    
                    JOptionPane.showMessageDialog(this.Menu_UPLOAD, "Não foi feito upload, já tem esse conteúdo na sua coleção!");                    
                }
                                
            } catch (IOException ex) {
                
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(this.Menu_UPLOAD, "Oops! O upload falhou, tente novamente mais tarde.");
            }
            
        } else {
            
            JOptionPane.showMessageDialog(this.Menu_UPLOAD, "Não existe conteúdo disponível na pasta selecionada!");
        }
        
    }//GEN-LAST:event_UPLOAD_ButtonUploadActionPerformed

    private void UPLOAD_filechooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UPLOAD_filechooserActionPerformed
        
        File file = this.UPLOAD_filechooser.getSelectedFile();
        String fullPath = file.getAbsolutePath();
        
        this.lastUploadPath = fullPath + "/";
        
        List<String> filtrada = this.getOnlyMP3andMP4Files(this.controller.listNomesFicheirosDir(fullPath));

        DefaultListModel model = new DefaultListModel();
        model.clear();
        model.addAll(filtrada);
                
        this.UPLOAD_listaconteudo.setModel(model);
    }//GEN-LAST:event_UPLOAD_filechooserActionPerformed

    private void UPLOAD_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UPLOAD_ButtonBackActionPerformed

        this.Menu_UPLOAD.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);
    }//GEN-LAST:event_UPLOAD_ButtonBackActionPerformed

    private void OPT_ButtonVerperfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPT_ButtonVerperfilActionPerformed
        
        this.Menu_OPTIONS.setVisible(false);
        this.Menu_PROFILE.setVisible(true);
        
        
        Utilizador userAtualCopia = this.controller.getUtilizadorAtual();   
        
        this.PROFILE_emailfield.setText(userAtualCopia.getEmail());
        this.PROFILE_namefield1.setText(userAtualCopia.getNome());
        this.PROFILE_passwordfield1.setText(userAtualCopia.getPassword());
        this.PROFILE_statusfield.setText(userAtualCopia.getTypeOfUser());

        if (userAtualCopia.getNome().equals("Guest")) {
            
            this.PROFILE_listaamigos.setVisible(false);
            this.PROFILE_amigoslabel.setVisible(false);
            this.PROFILE_scrollamigos.setVisible(false);
        }

        DefaultListModel model = new DefaultListModel();
        model.clear();
        
        model.addAll(userAtualCopia.getPotenciaisAmigos());
        
        this.PROFILE_listaamigos.setModel(model);       
        
    }//GEN-LAST:event_OPT_ButtonVerperfilActionPerformed

    private void PROFILE_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PROFILE_ButtonBackActionPerformed
        
        this.Menu_PROFILE.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);
    }//GEN-LAST:event_PROFILE_ButtonBackActionPerformed

    private void ALTERAR_ButtonAlterarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_ButtonAlterarCatActionPerformed

        String conteudoSelecionado = null;
        
        try {
            
            conteudoSelecionado = this.ALTERAR_listaconteudo.getSelectedValue().toString();
                                    
        } catch (Exception e) {
           
            JOptionPane.showMessageDialog(this.Menu_AlterarCategoriaConteudo, "Selecione um conteúdo!");
            return;
        }
        
        boolean blues = this.ALTERAR_checkbox_blues.isSelected();
        boolean rock = this.ALTERAR_checkbox_rock.isSelected();
        boolean jazz = this.ALTERAR_checkbox_jazz.isSelected();
        boolean metal = this.ALTERAR_checkbox_metal.isSelected();
        boolean pop = this.ALTERAR_checkbox_pop.isSelected();
        boolean video = this.ALTERAR_checkbox_video.isSelected();
        
        int sum = booleanToInt(blues) + booleanToInt(rock) + booleanToInt(jazz) + booleanToInt(metal) + booleanToInt(pop) + booleanToInt(video);
        
        if (sum == 0) {
            
            JOptionPane.showMessageDialog(this.Menu_AlterarCategoriaConteudo, "Selecione uma categoria!");
            return;            

        } else if (sum > 1) {
            
            JOptionPane.showMessageDialog(this.Menu_AlterarCategoriaConteudo, "Selecione apenas uma categoria!");
            return;                        
        }
        
        String cat = "Nenhuma";
        
        if (blues) cat = "Blues"; 
        if (rock) cat = "Rock"; 
        if (jazz) cat = "Jazz"; 
        if (metal) cat = "Metal"; 
        if (pop) cat = "Pop"; 
        if (video) cat = "Video"; 
        
        String parts[] = conteudoSelecionado.split(" \\[");
        String parts_cat[] = parts[1].split("\\]");
        
        if (parts_cat[0].equals(cat)) {
            
            JOptionPane.showMessageDialog(this.Menu_AlterarCategoriaConteudo, "Não pode alterar a categoria para a mesma!");
            return;                                    
        }
        
        System.err.println("Alterar categoria do conteúdo " + parts[0] + ", de " + parts_cat[0] + " para " + cat);
        
        this.controller.editarConteudoUtilizadorAtual(parts[0], cat, parts_cat[0]);       
        
        this.Menu_AlterarCategoriaConteudo.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);

        JOptionPane.showMessageDialog(this.Menu_OPTIONS, "Alterou a categoria do conteúdo com sucesso!");
    }//GEN-LAST:event_ALTERAR_ButtonAlterarCatActionPerformed

    public int booleanToInt(boolean in) {
        return in ? 1 : 0;
    }
    
    private void ALTERAR_checkbox_rockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_rockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_rockActionPerformed

    private void ALTERAR_checkbox_metalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_metalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_metalActionPerformed

    private void ALTERAR_checkbox_jazzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_jazzActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_jazzActionPerformed

    private void ALTERAR_checkbox_popActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_popActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_popActionPerformed

    private void ALTERAR_checkbox_bluesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_bluesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_bluesActionPerformed

    private void ALTERAR_checkbox_videoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_checkbox_videoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ALTERAR_checkbox_videoActionPerformed

    private void ALTERAR_ButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ALTERAR_ButtonBackActionPerformed
        
        this.Menu_AlterarCategoriaConteudo.setVisible(false);
        this.Menu_OPTIONS.setVisible(true);
    }//GEN-LAST:event_ALTERAR_ButtonBackActionPerformed
        
    public void update(DSSObservable o, Object arg) {
    
        
    } 
    
    public void exit() {
        
        System.exit(1);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ALTERAR_ButtonAlterarCat;
    private javax.swing.JButton ALTERAR_ButtonBack;
    private javax.swing.JCheckBox ALTERAR_checkbox_blues;
    private javax.swing.JCheckBox ALTERAR_checkbox_jazz;
    private javax.swing.JCheckBox ALTERAR_checkbox_metal;
    private javax.swing.JCheckBox ALTERAR_checkbox_pop;
    private javax.swing.JCheckBox ALTERAR_checkbox_rock;
    private javax.swing.JCheckBox ALTERAR_checkbox_video;
    private javax.swing.JList<String> ALTERAR_listaconteudo;
    private javax.swing.JScrollPane ALTERAR_scroll;
    private javax.swing.JLabel ALTERAR_selecionarcategorialabel;
    private javax.swing.JLabel ALTERAR_selecionarconteudolabel;
    private javax.swing.JLabel ALTERAR_tittle;
    private javax.swing.JButton CHOOSEALB_ButtonBack;
    private javax.swing.JButton CHOOSEALB_ButtonRep;
    private javax.swing.JList<String> CHOOSEALB_listaalbuns;
    private javax.swing.JScrollPane CHOOSEALB_scroll;
    private javax.swing.JLabel CHOOSEALB_tittle;
    private javax.swing.JButton LOGIN_ButtonBack;
    private javax.swing.JButton LOGIN_ButtonLogin;
    private javax.swing.JTextField LOGIN_emailfield;
    private javax.swing.JLabel LOGIN_emaillabel;
    private javax.swing.JPasswordField LOGIN_passfield;
    private javax.swing.JLabel LOGIN_passlabel;
    private javax.swing.JLabel LOGIN_tittle;
    private javax.swing.JButton MAIN_ButtonGuest;
    private javax.swing.JButton MAIN_ButtonLogin;
    private javax.swing.JButton MAIN_ButtonRegistar;
    private javax.swing.JLabel MAIN_newuserlabel;
    private javax.swing.JLabel MAIN_tittle;
    private javax.swing.JFrame Menu_AlterarCategoriaConteudo;
    private javax.swing.JFrame Menu_CHOOSEalbum;
    private javax.swing.JFrame Menu_LOGINform;
    private javax.swing.JFrame Menu_OPTIONS;
    private javax.swing.JFrame Menu_PROFILE;
    private javax.swing.JFrame Menu_REPRODUZIR_album;
    private javax.swing.JFrame Menu_REPRODUZIR_biblioteca;
    private javax.swing.JFrame Menu_REPRODUZIRoptions;
    private javax.swing.JFrame Menu_UPLOAD;
    private javax.swing.JButton OPT_ButtonVerperfil;
    private javax.swing.JButton OPT_alterarcategoriaconteudo;
    private javax.swing.JButton OPT_logout;
    private javax.swing.JLabel OPT_note;
    private javax.swing.JButton OPT_reproduzir;
    private javax.swing.JLabel OPT_tittle;
    private javax.swing.JButton OPT_upload;
    private javax.swing.JLabel OPT_useridlabel;
    private javax.swing.JButton PROFILE_ButtonBack;
    private javax.swing.JLabel PROFILE_amigoslabel;
    private javax.swing.JLabel PROFILE_emailfield;
    private javax.swing.JLabel PROFILE_emaillabel;
    private javax.swing.JList<String> PROFILE_listaamigos;
    private javax.swing.JLabel PROFILE_namefield1;
    private javax.swing.JLabel PROFILE_nomelabel;
    private javax.swing.JLabel PROFILE_passwordfield1;
    private javax.swing.JLabel PROFILE_passwordllabel;
    private javax.swing.JScrollPane PROFILE_scrollamigos;
    private javax.swing.JLabel PROFILE_statusfield;
    private javax.swing.JLabel PROFILE_statuslabel;
    private javax.swing.JLabel PROFILE_tittle;
    private javax.swing.JButton REPALBUM_ButtonBack;
    private javax.swing.JButton REPALBUM_ButtonPause;
    private javax.swing.JButton REPALBUM_ButtonPlay;
    private javax.swing.JButton REPALBUM_ButtonPlayAll;
    private javax.swing.JButton REPALBUM_ButtonResume;
    private javax.swing.JButton REPALBUM_ButtonStopAlbum;
    private javax.swing.JLabel REPALBUM_CurrentContent;
    private javax.swing.JLabel REPALBUM_albumname;
    private javax.swing.JList<String> REPALBUM_listaconteudo;
    private javax.swing.JButton REPALBUM_randomizelist;
    private javax.swing.JScrollPane REPALBUM_scroll;
    private javax.swing.JLabel REPALBUM_tittle;
    private javax.swing.JButton REPBIBL_ButtonBack;
    private javax.swing.JButton REPBIBL_ButtonPause;
    private javax.swing.JButton REPBIBL_ButtonPlay;
    private javax.swing.JButton REPBIBL_ButtonResume;
    private javax.swing.JLabel REPBIBL_currentcontentlabel;
    private javax.swing.JList<String> REPBIBL_listaconteudo;
    private javax.swing.JScrollPane REPBIBL_scroll;
    private javax.swing.JButton REPOPT_ButtonAlbuns;
    private javax.swing.JButton REPOPT_ButtonBack;
    private javax.swing.JButton REPOPT_ButtonBibliotecaMC;
    private javax.swing.JLabel REPOPT_note;
    private javax.swing.JLabel REPOPT_tittle;
    private javax.swing.JLabel REPOPT_tittle1;
    private javax.swing.JButton UPLOAD_ButtonBack;
    private javax.swing.JButton UPLOAD_ButtonUpload;
    private javax.swing.JLabel UPLOAD_caminholabel;
    private javax.swing.JLabel UPLOAD_caminholabel1;
    private javax.swing.JFileChooser UPLOAD_filechooser;
    private javax.swing.JList<String> UPLOAD_listaconteudo;
    private javax.swing.JScrollPane UPLOAD_scroll;
    private javax.swing.JLabel UPLOAD_tittle;
    // End of variables declaration//GEN-END:variables

    private void updateBibliotecaList() {
    
        List<String> biblioteca = this.controller.getListaConteudoBiblioteca();

        DefaultListModel model = new DefaultListModel();
        model.clear();
        
        model.addAll(biblioteca);
        
        this.REPBIBL_listaconteudo.setModel(model);
    }
    
    private void updateAlbunsList() {
        
        List<String> nomesAlbuns = this.controller.getListaAlbunsUserAtual();
        
        DefaultListModel model = new DefaultListModel();
        model.clear();
        
        model.addAll(nomesAlbuns);
        
        this.CHOOSEALB_listaalbuns.setModel(model);
    }
    
    

    private Object getExtensionOfFile(String list_content_selected) {
        
        return FilenameUtils.getExtension(list_content_selected);
    }

    private List<String> getOnlyMP3andMP4Files(List<String> listNomesFicheirosDir) {
        
        List<String> res = new ArrayList<>();
        
        for (String s: listNomesFicheirosDir) {
            
            if (this.getExtensionOfFile(s).equals("mp3") || this.getExtensionOfFile(s).equals("mp4")) {
                
                res.add(s);
            }
        }
        
        return res;
    }
}

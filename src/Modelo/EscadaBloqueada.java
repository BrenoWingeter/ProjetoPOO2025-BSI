package Modelo;

import Auxiliar.Consts;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class EscadaBloqueada extends Personagem implements Serializable {
    
    private boolean bloqueada;
    private ImageIcon imagemBloqueada;
    private ImageIcon imagemAberta;
    
    public EscadaBloqueada(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;  // Herói não pode passar enquanto bloqueada
        this.bMortal = false;        // Não mata o herói
        this.bloqueada = true;       // Inicia bloqueada
        
        // Salvar a imagem bloqueada
        this.imagemBloqueada = this.iImage;
        
        // Carregar imagem da escada aberta
        try {
            imagemAberta = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "escada.png");
            Image img = imagemAberta.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            imagemAberta = new ImageIcon(bi);
        } catch (IOException ex) {
            System.out.println("Erro ao carregar imagem da escada aberta: " + ex.getMessage());
            // Se não conseguir carregar, usar a mesma imagem
            imagemAberta = this.imagemBloqueada;
        }
    }
    
    public void desbloquear() {
        this.bloqueada = false;
        this.bTransponivel = true;   // Agora o herói pode passar
        this.iImage = imagemAberta;  // Mudar para imagem da escada aberta
        System.out.println("Escada desbloqueada! Agora você pode passar por ela.");
    }
    
    public boolean isBloqueada() {
        return bloqueada;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Escada bloqueada não se move, apenas desenha
    }
}
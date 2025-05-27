package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Hero extends Personagem implements Serializable{
    
    // Sistema de orientação
    private boolean olhandoParaEsquerda = false;
    private ImageIcon imagemDireita;   // hero1.png
    private ImageIcon imagemEsquerda;  // hero1_esquerda.png
    
    // Sistema de ataque (igual ao código anterior)
    private ImageIcon[][] imagensAtaque; // [direção][frame] - 4 direções x 3 frames cada
    private int contadorAtaque;
    private final int FRAMES_POR_DIRECAO = 3; // 3 frames (1, 4, 6)
    private final int TOTAL_FRAMES_ATAQUE = 9; // 3 ciclos por frame
    
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.contadorAtaque = 0;
        
        // Salvar imagem original (direita)
        this.imagemDireita = this.iImage;
        
        // Carregar imagem esquerda
        carregarImagemEsquerda();
        
        // Carregar imagens de ataque
        carregarImagensAtaque();
    }
    
    private void carregarImagemEsquerda() {
        try {
            String caminho = new java.io.File(".").getCanonicalPath() + Consts.PATH + "hero1_esquerda.png";
            
            java.io.File arquivo = new java.io.File(caminho);
            if (arquivo.exists()) {
                ImageIcon imagemTemp = new ImageIcon(caminho);
                
                // Redimensionar para o tamanho correto
                Image img = imagemTemp.getImage();
                BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                g.dispose();
                
                imagemEsquerda = new ImageIcon(bi);
            } else {
                imagemEsquerda = imagemDireita;
            }
        } catch (Exception ex) {
            imagemEsquerda = imagemDireita;
        }
    }
    
    private void carregarImagensAtaque() {
        imagensAtaque = new ImageIcon[4][FRAMES_POR_DIRECAO];
        String[] sufixosDirecao = {"up", "right", "down", "left"};
        int[] framesEscolhidos = {1, 4, 6}; // Primeiro, quarto e sexto frame
        
        try {
            for (int direcao = 0; direcao < 4; direcao++) {
                for (int frame = 0; frame < FRAMES_POR_DIRECAO; frame++) {
                    String nomeArquivo = "hero_ataque_" + sufixosDirecao[direcao] + "_" + framesEscolhidos[frame] + ".png";
                    
                    ImageIcon imagemOriginal = new ImageIcon(new java.io.File(".").getCanonicalPath() + 
                                                           Consts.PATH + nomeArquivo);
                    
                    Image img = imagemOriginal.getImage();
                    BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bi.createGraphics();
                    g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    g.dispose();
                    
                    imagensAtaque[direcao][frame] = new ImageIcon(bi);
                }
            }
        } catch (IOException ex) {
            for (int direcao = 0; direcao < 4; direcao++) {
                for (int frame = 0; frame < FRAMES_POR_DIRECAO; frame++) {
                    imagensAtaque[direcao][frame] = this.iImage;
                }
            }
        }
    }
    
    public void atacar() {
        if (!atacando) {
            atacando = true;
            frameAtaque = 0;
            contadorAtaque = 0;
            verificarAtaque();
        }
    }
    
    private void verificarAtaque() {
        int linhaAtaque = pPosicao.getLinha();
        int colunaAtaque = pPosicao.getColuna();
        
        // Calcular posição do ataque baseado na direção
        switch (direcao) {
            case 0: linhaAtaque--; break; // Cima
            case 1: colunaAtaque++; break; // Direita  
            case 2: linhaAtaque++; break; // Baixo
            case 3: colunaAtaque--; break; // Esquerda
        }
        
        // Verificar se há inimigos ou barris na posição de ataque
        Tela tela = Desenho.acessoATelaDoJogo();
        if (tela != null) {
            tela.processarAtaque(linhaAtaque, colunaAtaque);
        }
    }
    
    @Override
    public void autoDesenho() {
        if (atacando) {
            // Animação de ataque com os 3 frames (1, 4, 6)
            if (contadorAtaque < TOTAL_FRAMES_ATAQUE) {
                // 3 ciclos por frame (9 total / 3 frames = 3 ciclos por frame)
                int frameAtual = contadorAtaque / 3;
                
                if (frameAtual < FRAMES_POR_DIRECAO) {
                    // Desenhar o frame atual da direção atual
                    Desenho.desenhar(imagensAtaque[direcao][frameAtual], 
                                   this.pPosicao.getColuna(), 
                                   this.pPosicao.getLinha());
                } else {
                    // Fallback para desenho normal
                    desenharNormal();
                }
                contadorAtaque++;
            } else {
                // Fim da animação
                atacando = false;
                contadorAtaque = 0;
                desenharNormal();
            }
        } else {
            // Desenho normal com orientação
            desenharNormal();
        }
    }
    
    private void desenharNormal() {
        if (olhandoParaEsquerda && imagemEsquerda != imagemDireita) {
            ImageIcon imagemOriginal = this.iImage;
            this.iImage = imagemEsquerda;
            super.autoDesenho();
            this.iImage = imagemOriginal;
        } else {
            super.autoDesenho();
        }
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public boolean moveUp() {
        this.direcao = 0;
        if(super.moveUp())
            return validaPosicao();
        return false;
    }

    public boolean moveDown() {
        this.direcao = 2;
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        this.direcao = 1;
        this.olhandoParaEsquerda = false;
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        this.direcao = 3;
        this.olhandoParaEsquerda = true;
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }
}
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Hero1 extends Personagem implements Serializable{
    
    private ImageIcon[][] imagensAtaque; // [direção][frame] - 4 direções x 3 frames cada
    private int contadorAtaque;
    private final int FRAMES_POR_DIRECAO = 3; // Reduzido para 3 frames
    private final int TOTAL_FRAMES_ATAQUE = 9; // 3 ciclos por frame
    
    public Hero1(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.contadorAtaque = 0;
        carregarImagensAtaque();
    }
    
    private void carregarImagensAtaque() {
        imagensAtaque = new ImageIcon[4][FRAMES_POR_DIRECAO];
        String[] sufixosDirecao = {"up", "right", "down", "left"};
        int[] framesEscolhidos = {1, 4, 6};
        
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
    
    private String getDirecaoTexto() {
        switch (direcao) {
            case 0: return "CIMA";
            case 1: return "DIREITA"; 
            case 2: return "BAIXO";
            case 3: return "ESQUERDA";
            default: return "DESCONHECIDA";
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
            // Animação com 3 frames (primeiro, meio, último)
            if (contadorAtaque < TOTAL_FRAMES_ATAQUE) {
                // 3 ciclos por frame (9 total / 3 frames = 3 ciclos por frame)
                int frameAtual = contadorAtaque / 3; 
                
                if (frameAtual < FRAMES_POR_DIRECAO) {
                    // Desenhar o frame atual da direção atual
                    Desenho.desenhar(imagensAtaque[direcao][frameAtual], 
                                   this.pPosicao.getColuna(), 
                                   this.pPosicao.getLinha());
                } else {
                    // Fallback para imagem normal se algo der errado
                    super.autoDesenho();
                }
                contadorAtaque++;
            } else {
                // Fim da animação
                atacando = false;
                contadorAtaque = 0;
                super.autoDesenho();
            }
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
        this.direcao = 0; // Direção cima
        if(super.moveUp())
            return validaPosicao();
        return false;
    }

    public boolean moveDown() {
        this.direcao = 2; // Direção baixo
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        this.direcao = 1; // Direção direita
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        this.direcao = 3; // Direção esquerda
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }    
}
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Hero extends Personagem implements Serializable{
    
    private boolean olhandoParaEsquerda = false;
    private ImageIcon imagemDireita;
    private ImageIcon imagemEsquerda;
    
    private ImageIcon[][] imagensAtaque;
    private int contadorAtaque;
    private final int FRAMES_POR_DIRECAO = 3;
    private final int TOTAL_FRAMES_ATAQUE = 9;
    
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.contadorAtaque = 0;
        
        this.imagemDireita = this.iImage;
        carregarImagemEsquerda();
        carregarImagensAtaque();
    }
    
    private void carregarImagemEsquerda() {
        try {
            String caminho = new java.io.File(".").getCanonicalPath() + Consts.PATH + "hero1_esquerda.png";
            
            java.io.File arquivo = new java.io.File(caminho);
            if (arquivo.exists()) {
                ImageIcon imagemTemp = new ImageIcon(caminho);
                
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
    
    private void verificarAtaque() {
        int linhaAtaque = pPosicao.getLinha();
        int colunaAtaque = pPosicao.getColuna();
        
        switch (direcao) {
            case 0: linhaAtaque--; break;
            case 1: colunaAtaque++; break;
            case 2: linhaAtaque++; break;
            case 3: colunaAtaque--; break;
        }
        
        Tela tela = Desenho.acessoATelaDoJogo();
        if (tela != null) {
            tela.processarAtaque(linhaAtaque, colunaAtaque);
        }
    }
    
    @Override
    public void autoDesenho() {
        if (atacando) {
            if (contadorAtaque < TOTAL_FRAMES_ATAQUE) {
                int frameAtual = contadorAtaque / 3;
                
                if (frameAtual < FRAMES_POR_DIRECAO) {
                    Desenho.desenhar(imagensAtaque[direcao][frameAtual], 
                                   this.pPosicao.getColuna(), 
                                   this.pPosicao.getLinha());
                } else {
                    desenharNormal();
                }
                contadorAtaque++;
            } else {
                atacando = false;
                contadorAtaque = 0;
                desenharNormal();
            }
        } else {
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
    
    private Personagem verificarBlocoEmpurravel(int linha, int coluna) {
        Tela tela = Desenho.acessoATelaDoJogo();
        if (tela != null) {
            ArrayList<Personagem> fase = tela.getFaseAtual();
            if (fase != null) {
                for (Personagem p : fase) {
                    if ((p instanceof BlocoEmpurravel || 
                         p instanceof BlocoEmpurravelHorizontal || 
                         p instanceof BlocoEmpurravelVertical) &&
                        p.getPosicao().getLinha() == linha && 
                        p.getPosicao().getColuna() == coluna) {
                        return p;
                    }
                }
            }
        }
        return null;
    }
    
    private int calcularDirecaoEmpurrao(int blocoLinha, int blocoColuna) {
        int heroLinha = this.pPosicao.getLinha();
        int heroColuna = this.pPosicao.getColuna();
        
        int diferencaLinha = blocoLinha - heroLinha;
        int diferencaColuna = blocoColuna - heroColuna;
        
        if (Math.abs(diferencaLinha) >= Math.abs(diferencaColuna)) {
            if (diferencaLinha > 0) {
                return 2;
            } else {
                return 0;
            }
        } else {
            if (diferencaColuna > 0) {
                return 1;
            } else {
                return 3;
            }
        }
    }
    
    public boolean setPosicao(int linha, int coluna){
        return this.pPosicao.setPosicao(linha, coluna);
    }

    private boolean tentarMoverComEmpurrao(int novaLinha, int novaColuna){
        Personagem blocoParaEmpurrar = verificarBlocoEmpurravel(novaLinha, novaColuna);
        
        if (blocoParaEmpurrar != null) {
            int direcaoEmpurrao = calcularDirecaoEmpurrao(novaLinha, novaColuna);
            boolean empurrou = false;
            
            if (blocoParaEmpurrar instanceof BlocoEmpurravel) {
                empurrou = ((BlocoEmpurravel) blocoParaEmpurrar).empurrar(direcaoEmpurrao);
            } else if (blocoParaEmpurrar instanceof BlocoEmpurravelHorizontal) {
                empurrou = ((BlocoEmpurravelHorizontal) blocoParaEmpurrar).empurrar(direcaoEmpurrao);
            } else if (blocoParaEmpurrar instanceof BlocoEmpurravelVertical) {
                empurrou = ((BlocoEmpurravelVertical) blocoParaEmpurrar).empurrar(direcaoEmpurrao);
            }
            
            if (empurrou) {
                return this.setPosicao(novaLinha, novaColuna);
            } else {
                return false;
            }
        } else {
            Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
            if (Desenho.acessoATelaDoJogo().ehPosicaoValida(novaPosicao)) {
                return this.setPosicao(novaLinha, novaColuna);
            } else {
                return false;
            }
        }
    }
    
    public boolean moveUp() {
        this.direcao = 0;
        int novaLinha = this.pPosicao.getLinha() - 1;
        int novaColuna = this.pPosicao.getColuna();
        return tentarMoverComEmpurrao(novaLinha, novaColuna);
    }

    public boolean moveDown() {
        this.direcao = 2;
        int novaLinha = this.pPosicao.getLinha() + 1;
        int novaColuna = this.pPosicao.getColuna();
        return tentarMoverComEmpurrao(novaLinha, novaColuna);
    }

    public boolean moveRight() {
        this.direcao = 1;
        this.olhandoParaEsquerda = false;
        int novaLinha = this.pPosicao.getLinha();
        int novaColuna = this.pPosicao.getColuna() + 1;
        return tentarMoverComEmpurrao(novaLinha, novaColuna);
    }

    public boolean moveLeft() {
        this.direcao = 3;
        this.olhandoParaEsquerda = true;
        int novaLinha = this.pPosicao.getLinha();
        int novaColuna = this.pPosicao.getColuna() - 1;
        return tentarMoverComEmpurrao(novaLinha, novaColuna);
    }
}
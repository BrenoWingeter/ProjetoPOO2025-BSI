package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BichinhoVaiVemHorizontal extends Personagem implements Serializable {

    private boolean bRight;
    int iContador;

    public BichinhoVaiVemHorizontal(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        iContador = 0;
        this.bTransponivel = false; // NÃO pode ser atravessado
        this.bMortal = true;        // Causa dano ao herói
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (Desenho.acessoATelaDoJogo() != null && 
                !Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
                return false;
            }
            return true;
        }
        return false;       
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            
            boolean conseguiuMover = false;
            
            if (bRight) {
                // Tentar mover para direita
                if (this.moveRight()) {
                    conseguiuMover = true;
                } else {
                    // Se não conseguiu, mudar direção
                    bRight = false;
                }
            } else {
                // Tentar mover para esquerda
                if (this.moveLeft()) {
                    conseguiuMover = true;
                } else {
                    // Se não conseguiu, mudar direção
                    bRight = true;
                }
            }
        }
        
        super.autoDesenho();
        iContador++;
    }
    
    public boolean moveRight() {
        return this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
    }

    public boolean moveLeft() {
        return this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
    }
}
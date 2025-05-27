package Modelo;

import Auxiliar.Desenho;
import java.util.Random;

public class BichinhoVaiVemVertical extends Personagem{
    boolean bUp;
    
    public BichinhoVaiVemVertical(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bUp = true;
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

    public void autoDesenho(){
        boolean conseguiuMover = false;
        
        if(bUp) {
            // Tentar mover para cima
            if (this.moveUp()) {
                conseguiuMover = true;
            } else {
                // Se não conseguiu, mudar direção
                bUp = false;
            }
        } else {
            // Tentar mover para baixo
            if (this.moveDown()) {
                conseguiuMover = true;
            } else {
                // Se não conseguiu, mudar direção
                bUp = true;
            }
        }

        super.autoDesenho();
    }
    
    public boolean moveUp() {
        return this.setPosicao(pPosicao.getLinha() - 1, pPosicao.getColuna());
    }

    public boolean moveDown() {
        return this.setPosicao(pPosicao.getLinha() + 1, pPosicao.getColuna());
    }
}
package Modelo;

import Auxiliar.Desenho;
import java.util.Random;

public class ZigueZague extends Personagem{
    
    public ZigueZague(String sNomeImagePNG) {
        super(sNomeImagePNG);
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
        Random rand = new Random();
        int iDirecao = rand.nextInt(4) + 1; // 1 a 4
        
        boolean conseguiuMover = false;
        
        // Tentar mover na direção escolhida
        switch(iDirecao) {
            case 1: conseguiuMover = this.moveRight(); break;
            case 2: conseguiuMover = this.moveDown(); break;
            case 3: conseguiuMover = this.moveLeft(); break;
            case 4: conseguiuMover = this.moveUp(); break;
        }
        
        // Se não conseguiu mover, tentar outras direções
        if (!conseguiuMover) {
            for (int tentativa = 1; tentativa <= 4 && !conseguiuMover; tentativa++) {
                if (tentativa != iDirecao) { // Não tentar a mesma direção
                    switch(tentativa) {
                        case 1: conseguiuMover = this.moveRight(); break;
                        case 2: conseguiuMover = this.moveDown(); break;
                        case 3: conseguiuMover = this.moveLeft(); break;
                        case 4: conseguiuMover = this.moveUp(); break;
                    }
                }
            }
        }
        
        super.autoDesenho();
    }
    
    public boolean moveRight() {
        return this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
    }

    public boolean moveLeft() {
        return this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
    }
    
    public boolean moveUp() {
        return this.setPosicao(pPosicao.getLinha() - 1, pPosicao.getColuna());
    }

    public boolean moveDown() {
        return this.setPosicao(pPosicao.getLinha() + 1, pPosicao.getColuna());
    }
}
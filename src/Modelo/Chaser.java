package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.Serializable;

public class Chaser extends Personagem implements Serializable {

    private int contadorMovimento = 0;
    private Posicao ultimaPosicaoHeroi;

    public Chaser(String sNomeImagePNG) {
        super(sNomeImagePNG);
        
        this.bTransponivel = false; // NÃO pode ser atravessado
        this.bMortal = true;        // Causa dano ao herói
        this.ultimaPosicaoHeroi = new Posicao(0, 0);
    }

    public void computeDirection(Posicao heroPos) {
        // Salvar posição do herói para usar no movimento
        ultimaPosicaoHeroi.setPosicao(heroPos.getLinha(), heroPos.getColuna());
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
        super.autoDesenho();
        
        // Mover apenas a cada 5 ciclos para dar tempo ao jogador
        contadorMovimento++;
        if (contadorMovimento < 5) {
            return;
        }
        contadorMovimento = 0;
        
        if (ultimaPosicaoHeroi == null) {
            return;
        }
        
        // Calcular diferenças
        int diferencaLinha = ultimaPosicaoHeroi.getLinha() - this.getPosicao().getLinha();
        int diferencaColuna = ultimaPosicaoHeroi.getColuna() - this.getPosicao().getColuna();
        
        // Calcular distância total
        int distanciaTotal = Math.abs(diferencaLinha) + Math.abs(diferencaColuna);
        
        // Se estiver muito perto (1 célula), parar
        if (distanciaTotal <= 1) {
            return;
        }
        
        boolean conseguiuMover = false;
        
        // Priorizar movimento na direção de maior diferença
        if (Math.abs(diferencaLinha) >= Math.abs(diferencaColuna)) {
            // Mover verticalmente primeiro
            if (diferencaLinha > 0) {
                // Herói está abaixo
                if (this.moveDown()) {
                    conseguiuMover = true;
                }
            } else if (diferencaLinha < 0) {
                // Herói está acima
                if (this.moveUp()) {
                    conseguiuMover = true;
                }
            }
            
            // Se não conseguiu mover verticalmente, tenta horizontalmente
            if (!conseguiuMover) {
                if (diferencaColuna > 0) {
                    // Herói está à direita
                    if (this.moveRight()) {
                        conseguiuMover = true;
                    }
                } else if (diferencaColuna < 0) {
                    // Herói está à esquerda
                    if (this.moveLeft()) {
                        conseguiuMover = true;
                    }
                }
            }
        } else {
            // Mover horizontalmente primeiro
            if (diferencaColuna > 0) {
                // Herói está à direita
                if (this.moveRight()) {
                    conseguiuMover = true;
                }
            } else if (diferencaColuna < 0) {
                // Herói está à esquerda
                if (this.moveLeft()) {
                    conseguiuMover = true;
                }
            }
            
            // Se não conseguiu mover horizontalmente, tenta verticalmente
            if (!conseguiuMover) {
                if (diferencaLinha > 0) {
                    // Herói está abaixo
                    if (this.moveDown()) {
                        conseguiuMover = true;
                    }
                } else if (diferencaLinha < 0) {
                    // Herói está acima
                    if (this.moveUp()) {
                    }
                }
            }
        }
        
        if (!conseguiuMover) {
        }
    }
}
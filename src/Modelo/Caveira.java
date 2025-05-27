package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.io.Serializable;

public class Caveira extends Personagem implements Serializable{
    private int iContaIntervalos;
    private int contadorMovimento = 0;
    private Posicao ultimaPosicaoHeroi;
    private int distanciaAtaque = 3; // Para quando estiver a 3 células do herói
    private boolean emModoAtaque = false;
    
    public Caveira(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = true; // Causa dano se tocar
        this.iContaIntervalos = 0;
        this.ultimaPosicaoHeroi = new Posicao(0, 0);
    }
    
    public void computeDirection(Posicao heroPos) {
        // Salvar posição do herói
        ultimaPosicaoHeroi.setPosicao(heroPos.getLinha(), heroPos.getColuna());
    }

    public void autoDesenho() {
        super.autoDesenho();
        
        if (ultimaPosicaoHeroi == null) {
            return;
        }
        
        // Calcular distância até o herói
        int diferencaLinha = ultimaPosicaoHeroi.getLinha() - this.getPosicao().getLinha();
        int diferencaColuna = ultimaPosicaoHeroi.getColuna() - this.getPosicao().getColuna();
        int distanciaTotal = Math.abs(diferencaLinha) + Math.abs(diferencaColuna);
        
        // Se estiver na distância de ataque, parar e atirar
        if (distanciaTotal <= distanciaAtaque) {
            emModoAtaque = true;
            this.iContaIntervalos++;
            if(this.iContaIntervalos >= Consts.TIMER) {
                this.iContaIntervalos = 0;
                atirarNaDirecaoDoHeroi();
            }
            return; // Não se move quando está atacando
        } else {
            emModoAtaque = false;
        }
        
        // Mover em direção ao herói (mais devagar que o Chaser)
        contadorMovimento++;
        if (contadorMovimento < 8) { // Mais lento que o Chaser
            return;
        }
        contadorMovimento = 0;
        
        boolean conseguiuMover = false;
        
        // Priorizar movimento na direção de maior diferença
        if (Math.abs(diferencaLinha) >= Math.abs(diferencaColuna)) {
            // Mover verticalmente primeiro
            if (diferencaLinha > 0) {
                if (this.moveDown()) {
                    conseguiuMover = true;
                    System.out.println("💀 Caveira se aproximando (BAIXO)");
                }
            } else if (diferencaLinha < 0) {
                if (this.moveUp()) {
                    conseguiuMover = true;
                    System.out.println("💀 Caveira se aproximando (CIMA)");
                }
            }
            
            // Se não conseguiu mover verticalmente, tenta horizontalmente
            if (!conseguiuMover) {
                if (diferencaColuna > 0) {
                    if (this.moveRight()) {
                        conseguiuMover = true;
                        System.out.println("💀 Caveira se aproximando (DIREITA)");
                    }
                } else if (diferencaColuna < 0) {
                    if (this.moveLeft()) {
                        conseguiuMover = true;
                        System.out.println("💀 Caveira se aproximando (ESQUERDA)");
                    }
                }
            }
        } else {
            // Mover horizontalmente primeiro
            if (diferencaColuna > 0) {
                if (this.moveRight()) {
                    conseguiuMover = true;
                    System.out.println("💀 Caveira se aproximando (DIREITA)");
                }
            } else if (diferencaColuna < 0) {
                if (this.moveLeft()) {
                    conseguiuMover = true;
                    System.out.println("💀 Caveira se aproximando (ESQUERDA)");
                }
            }
            
            // Se não conseguiu mover horizontalmente, tenta verticalmente
            if (!conseguiuMover) {
                if (diferencaLinha > 0) {
                    if (this.moveDown()) {
                        conseguiuMover = true;
                        System.out.println("💀 Caveira se aproximando (BAIXO)");
                    }
                } else if (diferencaLinha < 0) {
                    if (this.moveUp()) {
                        conseguiuMover = true;
                        System.out.println("💀 Caveira se aproximando (CIMA)");
                    }
                }
            }
        }
        
        if (conseguiuMover && distanciaTotal <= distanciaAtaque + 1) {
            System.out.println("💀 Caveira entrou em modo de ataque!");
        }
    }
    
    private void atirarNaDirecaoDoHeroi() {
        int diferencaLinha = ultimaPosicaoHeroi.getLinha() - this.getPosicao().getLinha();
        int diferencaColuna = ultimaPosicaoHeroi.getColuna() - this.getPosicao().getColuna();
        
        // Criar bola de fogo na direção do herói
        Fogo f = new Fogo("fire.png");
        
        // Determinar direção da bola de fogo
        int novaLinha = this.getPosicao().getLinha();
        int novaColuna = this.getPosicao().getColuna();
        
        if (Math.abs(diferencaLinha) >= Math.abs(diferencaColuna)) {
            // Atirar verticalmente
            if (diferencaLinha > 0) {
                novaLinha++; // Atirar para baixo
                f.setDirecao(2); // Direção baixo
            } else {
                novaLinha--; // Atirar para cima
                f.setDirecao(0); // Direção cima
            }
        } else {
            // Atirar horizontalmente
            if (diferencaColuna > 0) {
                novaColuna++; // Atirar para direita
                f.setDirecao(1); // Direção direita
            } else {
                novaColuna--; // Atirar para esquerda
                f.setDirecao(3); // Direção esquerda
            }
        }
        
        f.setPosicao(novaLinha, novaColuna);
        Desenho.acessoATelaDoJogo().addPersonagem(f);
        System.out.println("🔥 Caveira atirou bola de fogo na direção do herói!");
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
}
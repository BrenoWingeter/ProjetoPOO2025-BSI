package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;

public class Fogo extends Personagem implements Serializable{
    
    private int direcaoMovimento = 1; // 0=cima, 1=direita, 2=baixo, 3=esquerda
    private int velocidade = 0;
            
    public Fogo(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;        // Causa dano ao herói
        this.bTransponivel = true;  // Herói pode passar por cima (mas recebe dano)
    }
    
    public void setDirecao(int direcao) {
        this.direcaoMovimento = direcao;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        
        // Mover a cada 2 ciclos para velocidade moderada
        velocidade++;
        if (velocidade < 2) {
            return;
        }
        velocidade = 0;
        
        boolean conseguiuMover = false;
        
        // Mover na direção definida
        switch (direcaoMovimento) {
            case 0: // Cima
                conseguiuMover = this.moveUp();
                break;
            case 1: // Direita
                conseguiuMover = this.moveRight();
                break;
            case 2: // Baixo
                conseguiuMover = this.moveDown();
                break;
            case 3: // Esquerda
                conseguiuMover = this.moveLeft();
                break;
        }
        
        // Se não conseguiu mover (bateu em parede), remover
        if (!conseguiuMover) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            System.out.println("💥 Bola de fogo destruída ao bater na parede!");
        }
    }
}
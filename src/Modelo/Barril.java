package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;
import java.util.Random;

public class Barril extends Personagem implements Serializable {
    
    private boolean quebrado;
    private Random random;
    
    public Barril(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;  // Herói não pode passar (até ser quebrado)
        this.bMortal = false;        // Não mata o herói
        this.quebrado = false;
        this.random = new Random();
    }
    
    public void quebrar() {
        if (!quebrado) {
            quebrado = true;
            this.bTransponivel = true; // Agora pode passar por cima
            
            // 50% de chance de dropar poção
            if (random.nextBoolean()) {
                Pocao pocao = new Pocao("pocao.png");
                pocao.getPosicao().setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
                Desenho.acessoATelaDoJogo().addPersonagem(pocao);
                System.out.println("Baú quebrado! Uma poção apareceu!");
            } else {
                System.out.println("Baú quebrado! Nada dentro...");
            }
            
            // Remover o barril da tela
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        }
    }
    
    public boolean isQuebrado() {
        return quebrado;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Baú não se move, apenas desenha
    }
}
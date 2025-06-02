package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;
import java.util.Random;

public class Barril extends Personagem implements Serializable {
    
    private boolean quebrado;
    private Random random;
    
    public Barril(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
        this.quebrado = false;
        this.random = new Random();
    }
    
    public void quebrar() {
        if (!quebrado) {
            quebrado = true;
            this.bTransponivel = true;
            
            if (random.nextBoolean()) {
                Pocao pocao = new Pocao("pocao.png");
                pocao.getPosicao().setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna());
                
                if (Desenho.acessoATelaDoJogo() != null) {
                    Desenho.acessoATelaDoJogo().addPersonagem(pocao);
                }
            }
            
            if (Desenho.acessoATelaDoJogo() != null) {
                Desenho.acessoATelaDoJogo().removePersonagem(this);
            }
        }
    }
    
    public boolean isQuebrado() {
        return quebrado;
    }

    @Override
    public void autoDesenho() {
        if (!quebrado) {
            super.autoDesenho();
        }
    }
}
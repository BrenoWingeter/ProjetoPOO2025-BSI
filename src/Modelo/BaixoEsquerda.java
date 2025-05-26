package Modelo;

import java.io.Serializable;

public class BaixoEsquerda extends Personagem implements Serializable {
    
    public BaixoEsquerda(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;  // Não pode passar por cima
        this.bMortal = false;        // Não mata o herói
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Muro não se move, apenas desenha
    }
}
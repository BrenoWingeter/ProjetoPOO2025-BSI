package Modelo;

import java.io.Serializable;

public class Murod extends Personagem implements Serializable {
    
    public Murod(String sNomeImagePNG) {
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
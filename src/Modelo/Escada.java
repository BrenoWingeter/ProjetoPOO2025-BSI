package Modelo;

import java.io.Serializable;

public class Escada extends Personagem implements Serializable {
    
    public Escada(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;   // Herói pode passar por cima
        this.bMortal = false;        // Não mata o herói
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Escada não se move, apenas desenha
    }
}
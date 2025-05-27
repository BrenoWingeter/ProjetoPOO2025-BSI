package Modelo;

import java.io.Serializable;

public class Chave extends Personagem implements Serializable {
    
    public Chave(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;   // Herói pode passar por cima
        this.bMortal = false;        // Não mata o herói
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Chave não se move, apenas desenha
    }
}
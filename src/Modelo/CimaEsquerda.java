package Modelo;

import java.io.Serializable;

public class CimaEsquerda extends Personagem implements Serializable {
    
    public CimaEsquerda(String sNomeImagePNG) {
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
package Modelo;

import java.io.Serializable;

public class Pocao extends Personagem implements Serializable {
    
    private int cura;
    
    public Pocao(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;   // Herói pode passar por cima
        this.bMortal = false;        // Não mata o herói
        this.cura = 25;              // Restaura 25 de vida
    }
    
    public int getCura() {
        return cura;
    }
    
    public void setCura(int cura) {
        this.cura = cura;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Poção não se move, apenas desenha
    }
}
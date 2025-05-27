package Modelo;

import java.io.Serializable;

public class Moeda extends Personagem implements Serializable {
    
    private int valor;
    
    public Moeda(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;   // Herói pode passar por cima
        this.bMortal = false;        // Não mata o herói
        this.valor = 10;             // Valor da moeda em pontos
    }
    
    public int getValor() {
        return valor;
    }
    
    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        // Moeda não se move, apenas desenha
    }
}
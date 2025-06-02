package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.Serializable;

public class BlocoEmpurravelHorizontal extends Personagem implements Serializable {
    
    public BlocoEmpurravelHorizontal(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
    }
    
    public boolean empurrar(int direcao) {
        // Só aceita movimento horizontal (esquerda e direita)
        if (direcao != 1 && direcao != 3) {
            return false;
        }
        
        int novaLinha = this.getPosicao().getLinha();
        int novaColuna = this.getPosicao().getColuna();
        
        switch (direcao) {
            case 1: novaColuna++; break; // Direita
            case 3: novaColuna--; break; // Esquerda
        }
        
        if (novaLinha < 0 || novaLinha >= Consts.MUNDO_ALTURA || 
            novaColuna < 0 || novaColuna >= Consts.MUNDO_LARGURA) {
            return false;
        }
        
        Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
        
        if (Desenho.acessoATelaDoJogo() != null && 
            !Desenho.acessoATelaDoJogo().ehPosicaoValidaParaBloco(novaPosicao, this)) {
            return false;
        }
        
        this.getPosicao().setPosicao(novaLinha, novaColuna);
        return true;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}

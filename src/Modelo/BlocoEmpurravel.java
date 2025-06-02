package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.Serializable;

public class BlocoEmpurravel extends Personagem implements Serializable {
    
    public BlocoEmpurravel(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
    }
    
    public boolean empurrar(int direcao) {
        int novaLinha = this.getPosicao().getLinha();
        int novaColuna = this.getPosicao().getColuna();
        
        switch (direcao) {
            case 0: novaLinha--; break;
            case 1: novaColuna++; break;
            case 2: novaLinha++; break;
            case 3: novaColuna--; break;
        }
        
        // Verificar limites do mundo
        if (novaLinha < 0 || novaLinha >= Consts.MUNDO_ALTURA || 
            novaColuna < 0 || novaColuna >= Consts.MUNDO_LARGURA) {
            return false;
        }
        
        // Criar posição temporária para teste
        Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
        
        // Verificar se a nova posição está livre usando método específico para blocos
        if (Desenho.acessoATelaDoJogo() != null && 
            !Desenho.acessoATelaDoJogo().ehPosicaoValidaParaBloco(novaPosicao, this)) {
            return false;
        }
        
        // Se chegou aqui, pode mover
        this.getPosicao().setPosicao(novaLinha, novaColuna);
        return true;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}
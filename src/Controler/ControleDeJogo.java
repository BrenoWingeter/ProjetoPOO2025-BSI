package Controler;

import Modelo.*;
import auxiliar.Posicao;
import java.util.ArrayList;

public class ControleDeJogo {
    
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
        }
    }
    
    public void processaTudo(ArrayList<Personagem> umaFase) {
        Hero hero = (Hero) umaFase.get(0);
        
        if (!hero.estaVivo()) {
            return;
        }
        
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                if (pIesimoPersonagem instanceof Pocao) {
                    Pocao pocao = (Pocao) pIesimoPersonagem;
                    hero.setVida(Math.min(100, hero.getVida() + pocao.getCura()));
                    umaFase.remove(pIesimoPersonagem);
                    i--;
                    continue;
                }
                
                if (pIesimoPersonagem instanceof Moeda) {
                    Moeda moeda = (Moeda) pIesimoPersonagem;
                    hero.adicionarPontuacao(moeda.getValor());
                    hero.setMoedas(hero.getMoedas() + 1);
                    umaFase.remove(pIesimoPersonagem);
                    i--;
                    continue;
                }
                
                if (pIesimoPersonagem instanceof Chave) {
                    hero.adicionarChave();
                    umaFase.remove(pIesimoPersonagem);
                    i--;
                    continue;
                }
                
                if (pIesimoPersonagem.isbMortal()) {
                    hero.receberDano(25);
                    
                    if (!hero.estaVivo()) {
                        return;
                    }
                }
            }
        }
        
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            if (pIesimoPersonagem instanceof Chaser) {
                Chaser chaser = (Chaser) pIesimoPersonagem;
                chaser.computeDirection(hero.getPosicao());
            } else if (pIesimoPersonagem instanceof Caveira) {
                Caveira caveira = (Caveira) pIesimoPersonagem;
                caveira.computeDirection(hero.getPosicao());
            }
        }
    }

    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem personagem = umaFase.get(i);
            if (!personagem.isbTransponivel()) {
                if (personagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean ehPosicaoValidaParaBloco(ArrayList<Personagem> umaFase, Posicao p, Personagem blocoQueEstaMovendo) {
        for (int i = 0; i < umaFase.size(); i++) {
            Personagem personagem = umaFase.get(i);
            
            if (personagem == blocoQueEstaMovendo) {
                continue;
            }
            
            if (!personagem.isbTransponivel()) {
                if (personagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
}
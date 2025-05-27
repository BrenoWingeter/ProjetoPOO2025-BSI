package Controler;

import Modelo.*;
import auxiliar.Posicao;
import java.util.ArrayList;
import java.util.Iterator;

public class ControleDeJogo {
    
    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
        }
    }
    
    public void processaTudo(ArrayList<Personagem> umaFase) {
        Hero hero = (Hero) umaFase.get(0);
        
        // Verificar se o herói ainda está vivo
        if (!hero.estaVivo()) {
            System.out.println("Game Over! O herói morreu!");
            return;
        }
        
        // Processar colisões primeiro (sem remover elementos ainda)
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {
                // Verificar se é uma poção
                if (pIesimoPersonagem instanceof Pocao) {
                    Pocao pocao = (Pocao) pIesimoPersonagem;
                    int vidaAntiga = hero.getVida();
                    hero.setVida(Math.min(100, hero.getVida() + pocao.getCura())); // Máximo 100 de vida
                    int vidaRecuperada = hero.getVida() - vidaAntiga;
                    System.out.println("🧪 Poção coletada! +" + vidaRecuperada + " de vida | Vida: " + hero.getVida());
                    umaFase.remove(pIesimoPersonagem);
                    i--; // Ajustar índice após remoção
                    continue;
                }
                
                // Verificar se é uma moeda
                if (pIesimoPersonagem instanceof Moeda) {
                    Moeda moeda = (Moeda) pIesimoPersonagem;
                    hero.adicionarPontuacao(moeda.getValor());
                    hero.setMoedas(hero.getMoedas() + 1);
                    System.out.println("💰 Moeda coletada! +" + moeda.getValor() + " pontos | Total: " + hero.getPontuacao());
                    umaFase.remove(pIesimoPersonagem);
                    i--; // Ajustar índice após remoção
                    continue;
                }
                
                // Verificar se é uma chave
                if (pIesimoPersonagem instanceof Chave) {
                    hero.adicionarChave();
                    System.out.println("🔑 Chave coletada! Total de chaves: " + hero.getChaves());
                    umaFase.remove(pIesimoPersonagem);
                    i--; // Ajustar índice após remoção
                    continue;
                }
                
                // Verificar colisão com personagens mortais
                if (pIesimoPersonagem.isbMortal()) {
                    hero.receberDano(25); // Dano por colisão
                    System.out.println("💔 Herói recebeu dano! Vida: " + hero.getVida());
                    
                    // REMOVIDO: Não remover inimigos automaticamente ao colidir
                    // Eles só morrem se forem atacados
                    
                    if (!hero.estaVivo()) {
                        System.out.println("☠️ Game Over!");
                        return;
                    }
                }
            }
        }
        
        // Processar movimento dos inimigos
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

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
}
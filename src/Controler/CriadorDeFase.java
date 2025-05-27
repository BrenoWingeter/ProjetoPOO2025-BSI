package Controler;

import Modelo.*;
import java.util.ArrayList;

public class CriadorDeFase {
    
    private Hero heroi;
    
    public ArrayList<Personagem> criarFase(String[] linhasFase) {
        ArrayList<Personagem> fase = new ArrayList<>();
        ArrayList<Personagem> outrosPersonagens = new ArrayList<>();
        heroi = null;
        
        // Primeiro, criar todos os personagens
        for (int linha = 0; linha < linhasFase.length; linha++) {
            String linhaMapa = linhasFase[linha];
            
            for (int coluna = 0; coluna < linhaMapa.length(); coluna++) {
                char caractere = linhaMapa.charAt(coluna);
                Personagem personagem = criarPersonagem(caractere, linha, coluna);
                
                if (personagem != null) {
                    if (personagem instanceof Hero) {
                        heroi = (Hero) personagem;
                    } else {
                        outrosPersonagens.add(personagem);
                    }
                }
            }
        }
        
        // Garantir que Hero seja sempre o primeiro (índice 0)
        if (heroi != null) {
            fase.add(heroi);
        }
        
        // Adicionar os outros personagens
        fase.addAll(outrosPersonagens);
        
        System.out.println("✅ Fase criada com " + fase.size() + " personagens");
        
        return fase;
    }
    
    public ArrayList<Personagem> criarFase(String faseTexto) {
        String[] linhas = faseTexto.split("\n");
        for (int i = 0; i < linhas.length; i++) {
            linhas[i] = linhas[i].trim();
        }
        return criarFase(linhas);
    }
    
    private Personagem criarPersonagem(char caractere, int linha, int coluna) {
        Personagem personagem = null;
        
        switch (caractere) {
            case 'H':
                personagem = new Hero("hero1.png");
                break;
            case '#':
                personagem = new Muroe("muroe.png");
                break;
            case 'I':
                personagem = new Chaser("caveira.png");
                break;
            case 'O':
                personagem = new BichinhoVaiVemHorizontal("caveira.png");
                break;
            case 'V':
                personagem = new BichinhoVaiVemVertical("caveira.png");
                break;
            case 'Z':
                personagem = new ZigueZague("robo.png");
                break;
            case 'C':
                personagem = new Caveira("orc_mago.png");
                break;
            case 'T':
                personagem = new Teto("teto.png");
                break;
            case '[':
                personagem = new CimaEsquerda("cimaesquerda.png");
                break;
            case ']':
                personagem = new CimaDireita("cimadireita.png");
                break;
            case '<':
                personagem = new BaixoEsquerda("baixoesquerda.png");
                break;
            case '>':
                personagem = new BaixoDireita("baixodireita.png");
                break;
            case '$':
                personagem = new Murod("murod.png");
                break;
            case '_':
                personagem = new Baixo("baixo.png");
                break;
            case 'E':
                personagem = new Escada("escada.png");
                break;
            case 'B':
                personagem = new EscadaBloqueada("escada_bloqueada.png"); // Imagem diferente para escada bloqueada
                break;
            case 'M':
                personagem = new Moeda("moeda.png"); // Nova classe Moeda
                break;
            case 'K':
                personagem = new Chave("chave.png"); // Nova classe Chave
                break;
            case 'D':
                personagem = new Barril("barril.png"); // Nova classe Barril
                break;
            case 'P':
                personagem = new Pocao("pocao.png"); // Nova classe Poção (para testes)
                break;
            case '.':
                break;
            default:
                return null; // Espaço vazio
        }
        
        if (personagem != null) {
            // Usar o método da superclasse para evitar validação durante criação
            personagem.getPosicao().setPosicao(linha, coluna);
        }
        
        return personagem;
    }
    
    public Hero getHeroi() {
        return heroi;
    }
}
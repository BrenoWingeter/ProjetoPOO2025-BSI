package Controler;

import Modelo.Hero;
import Modelo.Muroe;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import Modelo.Caveira;
import Modelo.Teto;
import Modelo.CimaEsquerda;
import Modelo.CimaDireita;
import Modelo.BaixoEsquerda;
import Modelo.BaixoDireita;
import Modelo.Murod;
import Modelo.Baixo;
import Modelo.Escada;
import Modelo.EscadaBloqueada;
import Modelo.Moeda;
import Modelo.Chave;
import Modelo.Barril;
import Modelo.Pocao;
import Modelo.BlocoEmpurravel;
import Modelo.BlocoEmpurravelHorizontal;
import Modelo.BlocoEmpurravelVertical;
import Modelo.Personagem;
import java.util.ArrayList;

public class CriadorDeFase {
    
    private Hero heroi;
    
    public ArrayList<Personagem> criarFase(String[] linhasFase) {
        ArrayList<Personagem> fase = new ArrayList<>();
        ArrayList<Personagem> outrosPersonagens = new ArrayList<>();
        heroi = null;
        
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
        
        if (heroi != null) {
            fase.add(heroi);
        }
        
        fase.addAll(outrosPersonagens);
        
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
                personagem = new Chaser("fantasma.png");
                break;
            case 'O':
                personagem = new BichinhoVaiVemHorizontal("fantasma.png");
                break;
            case 'V':
                personagem = new BichinhoVaiVemVertical("fantasma.png");
                break;
            case 'Z':
                personagem = new ZigueZague("fantasma.png");
                break;
            case 'C':
                personagem = new Caveira("fantasma_mago.png");
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
                personagem = new EscadaBloqueada("escada_bloqueada.png");
                break;
            case 'M':
                personagem = new Moeda("moeda.png");
                break;
            case 'K':
                personagem = new Chave("chave.png");
                break;
            case 'D':
                personagem = new Barril("barril.png");
                break;
            case 'P':
                personagem = new Pocao("pocao.png");
                break;
            case 'Q':
                personagem = new BlocoEmpurravel("bloco.png");
                break;
            case 'X':
                personagem = new BlocoEmpurravelHorizontal("bloco_horizontal.png");
                break;
            case 'L':
                personagem = new BlocoEmpurravelVertical("bloco_vertical.png");
                break;
            case '.':
                break;
            default:
                return null;
        }
        
        if (personagem != null) {
            personagem.getPosicao().setPosicao(linha, coluna);
        }
        
        return personagem;
    }
    
    public Hero getHeroi() {
        return heroi;
    }
}
package Controler;

import Modelo.Personagem;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import Modelo.Escada;
import Modelo.EscadaBloqueada;
import Modelo.Barril;
import Modelo.Pocao;
import Modelo.Moeda;
import Modelo.Chave;
import Modelo.BlocoEmpurravel;
import Modelo.BlocoEmpurravelHorizontal;
import Modelo.BlocoEmpurravelVertical;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private CriadorDeFase criadorDeFase;
    private int faseAtualNumero = 1;
    private boolean jogoTerminado = false;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        criadorDeFase = new CriadorDeFase();
        faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(1));
        hero = criadorDeFase.getHeroi();
        this.atualizaCamera();
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public ArrayList<Personagem> getFaseAtual() {
        return this.faseAtual;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }
    
    public boolean ehPosicaoValidaParaBloco(Posicao p, Personagem bloco) {
        return cj.ehPosicaoValidaParaBloco(this.faseAtual, p, bloco);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "piso1masmorra.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        desenharHUD(g2);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void desenharHUD(Graphics g) {
        if (hero != null) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            
            g.drawString("Vida: " + hero.getVida(), 10, 25);
            g.drawString("Pontuação: " + hero.getPontuacao(), 10, 45);
            g.drawString("Moedas: " + hero.getMoedas(), 10, 65);
            g.drawString("Chaves: " + hero.getChaves(), 10, 85);
            g.drawString("Fase: " + faseAtualNumero, 10, 105);
            
            if (!hero.estaVivo()) {
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.setColor(Color.RED);
                g.drawString("GAME OVER", 200, 300);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Pressione R para reiniciar", 250, 350);
            }
        }
    }

    private void atualizaCamera() {
        if (hero != null) {
            int linha = hero.getPosicao().getLinha();
            int coluna = hero.getPosicao().getColuna();

            cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
            cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
        }
    }
    
    public void processarAtaque(int linha, int coluna) {
        if (faseAtual == null) return;
        
        for (int deltaLinha = -1; deltaLinha <= 1; deltaLinha++) {
            for (int deltaColuna = -1; deltaColuna <= 1; deltaColuna++) {
                int linhaVerificar = linha + deltaLinha;
                int colunaVerificar = coluna + deltaColuna;
                
                for (int i = faseAtual.size() - 1; i >= 1; i--) {
                    Personagem p = faseAtual.get(i);
                    
                    if (p.getPosicao().getLinha() == linhaVerificar && 
                        p.getPosicao().getColuna() == colunaVerificar) {
                        
                        if (p instanceof Barril) {
                            Barril barril = (Barril) p;
                            barril.quebrar();
                            continue;
                        }
                        
                        if (p.isbMortal() || p instanceof Chaser || p instanceof Caveira || 
                            p instanceof BichinhoVaiVemHorizontal || p instanceof BichinhoVaiVemVertical || 
                            p instanceof ZigueZague) {
                            
                            faseAtual.remove(p);
                            hero.adicionarPontuacao(75);
                        }
                    }
                }
            }
        }
    }

    private void interagirComEscadas() {
        if (hero == null || faseAtual == null) return;
        
        int heroLinha = hero.getPosicao().getLinha();
        int heroColuna = hero.getPosicao().getColuna();
        
        int[] deltaLinhas = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] deltaColunas = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int i = 0; i < 8; i++) {
            int novaLinha = heroLinha + deltaLinhas[i];
            int novaColuna = heroColuna + deltaColunas[i];
            
            if (novaLinha >= 0 && novaLinha < Consts.MUNDO_ALTURA && 
                novaColuna >= 0 && novaColuna < Consts.MUNDO_LARGURA) {
                
                for (Personagem p : faseAtual) {
                    if (p instanceof EscadaBloqueada) {
                        EscadaBloqueada escadaBloqueada = (EscadaBloqueada) p;
                        
                        if (escadaBloqueada.getPosicao().getLinha() == novaLinha && 
                            escadaBloqueada.getPosicao().getColuna() == novaColuna) {
                            
                            if (escadaBloqueada.isBloqueada()) {
                                if (hero.getChaves() > 0) {
                                    hero.usarChave();
                                    escadaBloqueada.desbloquear();
                                    repaint();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void verificarEscada() {
        if (hero != null && faseAtual != null && hero.estaVivo()) {
            for (Personagem p : faseAtual) {
                if (p instanceof Escada) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        if (faseAtualNumero == 5) {
                            boolean temMoedas = false;
                            for (Personagem moeda : faseAtual) {
                                if (moeda instanceof Moeda) {
                                    temMoedas = true;
                                    break;
                                }
                            }
                            
                            if (!temMoedas) {
                                mostrarTelaFinal();
                            }
                        } else {
                            proximaFase();
                        }
                        return;
                    }
                }
                
                if (p instanceof EscadaBloqueada) {
                    EscadaBloqueada escadaBloqueada = (EscadaBloqueada) p;
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        if (!escadaBloqueada.isBloqueada()) {
                            proximaFase();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void proximaFase() {
        if (faseAtualNumero < 5) {
            faseAtualNumero++;
            
            int vidaAtual = hero.getVida();
            int pontuacaoAtual = hero.getPontuacao();
            int moedasAtuais = hero.getMoedas();
            int chavesAtuais = hero.getChaves();
            
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
            hero = criadorDeFase.getHeroi();
            
            hero.setVida(vidaAtual);
            hero.setPontuacao(pontuacaoAtual);
            hero.setMoedas(moedasAtuais);
            hero.setChaves(chavesAtuais);
            
            this.atualizaCamera();
        }
    }

    private void mostrarTelaFinal() {
        jogoTerminado = true;
        TelaFinal telaFinal = new TelaFinal(hero.getPontuacao());
        telaFinal.setVisible(true);
        this.setVisible(false);
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (!jogoTerminado) {
                    repaint();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (jogoTerminado) {
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (hero != null && hero.estaVivo()) {
                hero.moveUp();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (hero != null && hero.estaVivo()) {
                hero.moveDown();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (hero != null && hero.estaVivo()) {
                hero.moveLeft();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (hero != null && hero.estaVivo()) {
                hero.moveRight();
                verificarEscada();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (hero != null && hero.estaVivo() && !hero.isAtacando()) {
                hero.atacar();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            if (hero != null && hero.estaVivo()) {
                interagirComEscadas();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            salvarFase("fase_salva.ser");
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            carregarFase("fase_salva.ser");
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
            hero = criadorDeFase.getHeroi();
            this.atualizaCamera();
        }
        
        this.atualizaCamera();
        
        if (hero != null) {
            this.setTitle("Setas=Mover | ESPAÇO=Atacar | E=Interagir | R=Reiniciar - Fase:" + faseAtualNumero + 
                    " | Vida:" + hero.getVida() + "/100 | Pontos:" + hero.getPontuacao() + 
                    " | Chaves:" + hero.getChaves());
        } else {
            this.setTitle("POO2023-1 - Skooter - Fase " + faseAtualNumero);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (hero != null && hero.estaVivo() && !jogoTerminado) {
            int x = e.getX();
            int y = e.getY();

            this.setTitle("X: " + x + ", Y: " + y
                    + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

            this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);
            verificarEscada();

            repaint();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
    
    private void salvarFase(String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(faseAtual);
            oos.writeInt(faseAtualNumero);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarFase(String nomeArquivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            faseAtual = (ArrayList<Personagem>) ois.readObject();
            faseAtualNumero = ois.readInt();

            for (Personagem p : faseAtual) {
                Desenho.setCenario(this);
            }
            for (Personagem p : faseAtual) {
                if (p instanceof Hero) {
                    hero = (Hero) p;
                    break;
                }
            }
            atualizaCamera();
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
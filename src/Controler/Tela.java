package Controler;

import Modelo.Personagem;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private CriadorDeFase criadorDeFase;
    private int faseAtualNumero = 1;

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

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
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
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /**
         * ***********Desenha cenário de fundo*************
         */
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

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void atualizaCamera() {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }
    
    private void verificarEscada() {
    if (hero != null && faseAtual != null) {
        for (Personagem p : faseAtual) {
            if (p instanceof Modelo.Escada) {
                if (hero.getPosicao().igual(p.getPosicao())) {
                    // Herói pisou na escada - próxima fase
                    proximaFase();
                    break;
                }
            }
        }
    }
}

private void proximaFase() {
    if (faseAtualNumero < 3) {
        faseAtualNumero++;
        faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
        hero = criadorDeFase.getHeroi();
        this.atualizaCamera();
        System.out.println("Avançou para a Fase " + faseAtualNumero + "!");
    } else {
        System.out.println("Parabéns! Você completou todas as fases!");
    }
}

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_C) {
        this.faseAtual.clear();
    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
        if (hero != null) {
            hero.moveUp();
            verificarEscada(); // ADICIONAR ESTA LINHA
        }
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        if (hero != null) {
            hero.moveDown();
            verificarEscada(); // ADICIONAR ESTA LINHA
        }
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        if (hero != null) {
            hero.moveLeft();
            verificarEscada(); // ADICIONAR ESTA LINHA
        }
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        if (hero != null) {
            hero.moveRight();
            verificarEscada(); // ADICIONAR ESTA LINHA
        }
    } else if (e.getKeyCode() == KeyEvent.VK_S) {
        salvarFase("fase_salva.ser");
    } else if (e.getKeyCode() == KeyEvent.VK_L) {
        carregarFase("fase_salva.ser");
    } else if (e.getKeyCode() == KeyEvent.VK_R) {
        // Reiniciar fase (manter este controle)
        faseAtual = criadorDeFase.criarFase(ExemplosDeFases.obterFase(faseAtualNumero));
        hero = criadorDeFase.getHeroi();
        this.atualizaCamera();
    }
    
    
    this.atualizaCamera();
    
    if (hero != null) {
        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                + (hero.getPosicao().getLinha()) + " - Fase " + faseAtualNumero);
    } else {
        this.setTitle("POO2023-1 - Skooter - Fase " + faseAtualNumero);
    }
}

    public void mousePressed(MouseEvent e) {
    if (hero != null) {
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);
        verificarEscada(); // ADICIONAR ESTA LINHA

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
        System.out.println("Fase salva com sucesso em " + nomeArquivo);
    } catch (IOException e) {
        System.out.println("Erro ao salvar a fase: " + e.getMessage());
        e.printStackTrace();
    }
}

    @SuppressWarnings("unchecked")
    private void carregarFase(String nomeArquivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            faseAtual = (ArrayList<Personagem>) ois.readObject();

        // Atualiza o cenário para cada personagem
        for (Personagem p : faseAtual) {
            Desenho.setCenario(this);
        }
        // Reatribui o herói
        for (Personagem p : faseAtual) {
            if (p instanceof Hero) {
                hero = (Hero) p;
                break;
            }
        }
        // Atualiza a câmera com base no novo herói
        atualizaCamera();

        System.out.println("Fase carregada com sucesso!");
        repaint();
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Erro ao carregar a fase: " + e.getMessage());
        e.printStackTrace();
        }
    }
    
}

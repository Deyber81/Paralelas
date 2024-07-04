package serpiente.async;

import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class Process extends Thread {

    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3; // Nuevo panel

    private int targetX;
    private int targetY;
    private final Random random = new Random(); // Generador de números aleatorios

    public Process(JPanel jPanel1, JPanel jPanel2) {
        this.jPanel1 = jPanel1;
        this.jPanel2 = jPanel2;
        this.jPanel3 = new JPanel(); // Inicializar el nuevo panel
        this.jPanel3.setSize(jPanel2.getSize()); // Establecer el mismo tamaño que jPanel2
        this.jPanel3.setBackground(java.awt.Color.RED); // Establecer color del panel (puedes cambiarlo)
    }

    @Override
    public void run() {
        // Agregar jPanel2 al jPanel1
        jPanel1.add(jPanel2);

        // Escuchar eventos del mouse dentro del hilo Process
        jPanel1.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Obtener coordenadas del mouse
                targetX = e.getX();
                targetY = e.getY();
            }
        });

        // Hacer visible jPanel2
        jPanel2.setVisible(true);

        // Posicionar y agregar jPanel3 en una posición aleatoria
        positionPanel3Randomly();
        jPanel1.add(jPanel3);
        jPanel3.setVisible(true);

        // Timer para reposicionar jPanel3 cada 5 segundos
        Timer timer = new Timer(5000, e -> positionPanel3Randomly());
        timer.start();

        // Mantener el hilo Process en ejecución
        while (true) {
            // Mover jPanel2 hacia el cursor
            movePanelTowardsTarget();

            // Pausa para no consumir recursos excesivamente
            try {
                Thread.sleep(10); // Puedes ajustar el tiempo de espera según sea necesario
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Método para mover jPanel2 hacia el cursor con velocidad constante
    private void movePanelTowardsTarget() {
        SwingUtilities.invokeLater(() -> {
            int currentX = jPanel2.getX();
            int currentY = jPanel2.getY();

            if (currentX != targetX || currentY != targetY) {
                // Calcular dirección y distancia hacia el cursor
                int dx = targetX - currentX;
                int dy = targetY - currentY;

                // Mover una unidad hacia el cursor en cada iteración
                int moveX = dx > 0 ? 1 : (dx < 0 ? -1 : 0);
                int moveY = dy > 0 ? 1 : (dy < 0 ? -1 : 0);

                jPanel2.setLocation(currentX + moveX, currentY + moveY);
            }
        });
    }

    // Método para posicionar jPanel3 aleatoriamente dentro de jPanel1
    private void positionPanel3Randomly() {
        SwingUtilities.invokeLater(() -> {
            int panelWidth = jPanel1.getWidth();
            int panelHeight = jPanel1.getHeight();
            int x = random.nextInt(panelWidth - jPanel3.getWidth());
            int y = random.nextInt(panelHeight - jPanel3.getHeight());
            jPanel3.setLocation(x, y);
        });
    }
}



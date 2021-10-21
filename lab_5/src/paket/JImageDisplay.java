package paket;

import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class JImageDisplay extends JComponent{
    public BufferedImage bimage;

    public JImageDisplay(int w, int h){
        bimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // TYPE_INT_RGB обозначает, что R,G,B компоненты имеют по 8 битов, представленные в int формате
        super.setPreferredSize(new Dimension(w, h));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        // отрисовка изображения в компоненте:
        g.drawImage(bimage, 0, 0, bimage.getWidth(), bimage.getHeight(), null);
    }

    // Делаем все пиксели черными
    public void clearImage(){
        for (int i = 0; i < bimage.getWidth(); i++) {
            for (int j = 0; j < bimage.getHeight(); j++) {
                bimage.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
            }
        }

        this.repaint();
    }

    // Раскрашиваем пиксель в определенный цвет
    public void drawPixel(int x, int y, int rgbColor){
        bimage.setRGB(x, y, rgbColor);
    }

    // Вычисления фрактала Мандельброта...
}



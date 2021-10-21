package paket;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import java.awt.event.ActionListener;
import java.io.FileFilter.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer{
    private JComboBox box = new JComboBox();
    // Window size
    private int winSize; // 1
    // used to display updating
    private JImageDisplay display; // 2. ссылка на наш рисунок
    // for future features
    private FractalGenerator generator; // 3
    // for complex range
    private Rectangle2D.Double range; // 4

    private JLabel coordsLabel;

    private JFrame frame;
    private int rows_remaining;

    public FractalExplorer(int winSize){
        this.winSize = winSize;
        this.range = new Rectangle2D.Double();
        this.generator = new Mandelbrot();
        this.generator.getInitialRange(this.range);
        this.display = new JImageDisplay(winSize, winSize);
    }

    public void createAndShowGUI(){
        // JFrame init

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Fractals");
        panel.add(label, BorderLayout.WEST);
        panel.add(box, BorderLayout.EAST);
        frame = new JFrame("Pathfinder");
        frame.add(panel, BorderLayout.NORTH);

        JPanel but = new JPanel(); // панель для кнопок

        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());

        box.addActionListener(new action());

        display.setLayout(new BorderLayout());
        display.addMouseListener(new click());
        // операция закрытия окна по умолчанию
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.add(display, BorderLayout.CENTER); // расположение конпки по центру

        ActionListener aListener = new AListener();

        JButton droppingButton = new JButton("Drop image"); // задаем текст кнопки
        droppingButton.addActionListener(aListener);
        but.add(droppingButton);

        JButton save = new JButton("Save image"); // создание кнопки сохранения
        but.add(save);
        save.addActionListener(new Save());

        frame.add(but, BorderLayout.SOUTH);

        // эти операции поработают с вылезающим окном :)

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public class click implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e){    // для запрета нажатия на фрактал
            double x = FractalGenerator.getCoord(range.x, range.x + range.width, winSize, e.getX());
            double y = FractalGenerator.getCoord(range.y, range.y + range.height, winSize, e.getY());

            generator.recenterAndZoomRange(range, x, y, 0.5); // это позволяет увеличивать отображение рисунка
            drawFractal();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    private void drawFractal(){   // блок кнопок комбобокса, начало работы потоков
        enableUI(false);
        rows_remaining = winSize;
        for (int y = 0; y < winSize; y++){
            var frk = new FractalWorker(y);  // создание объекта потока
            frk.execute();
        }
//        display.repaint(); // обновление JImageDisplay
    }

    public void enableUI(boolean unblock){    // блок кнопок
        frame.setEnabled(unblock);
    }

    public static void main(String[] args) {
        FractalExplorer exp = new FractalExplorer(700); // инициализируем новый экземпляр данного класса с размером 700
        exp.createAndShowGUI();
        exp.drawFractal();
    }

    public class AListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            display.clearImage(); // сброс диапазона к начальному
            generator.getInitialRange(range);
            drawFractal(); // рисуем фрактал!
        }
    }

    public class action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent b){
            generator = (FractalGenerator) box.getSelectedItem(); // присваивает фракталу новый класс(вид)
            generator.getInitialRange(range); // обновляем
            drawFractal();
        }
    }

    public class Save implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent c){

            JFileChooser chooser = new JfileChooser(); // отвчает за диалоговое окно
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png"); // FileFilter предлагает только файлы png
            chooser.setFileFilter(filter);  // добавление настроек в ДО
            chooser.setAcceptAllFileFilterUsed(false); // не будет предлагаться остальные расширения

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                try {
                    ImageIO.write(display.bimage, "png", chooser.getSelectedFile());
                }
                catch (IOException ioException){
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(chooser,ioException.getMessage(),"Exception!",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class FractalWorker extends SwingWorker<Object, Object>{
        private int valY;   // у - координата
        private int[] RGB;
        public FractalWorker(int valY){
            this.valY = valY;
        }
        public Object doInBackground(){
            double xCoord, yCoord;
            float hue;
            int iters, rgbColor;
            RGB = new int[winSize];
            for (int x = 0; x < winSize; x++){
                xCoord = generator.getCoord(range.x, range.x + range.width, winSize, x);   // запоминание цветов
                // получение координаты х, соответствущей координате пикселя Х
                yCoord = generator.getCoord(range.y, range.y + range.height, winSize, valY);

                iters = generator.numIterations(xCoord, yCoord);  // кол-во итераций

                hue = 0.7f + (float)iters / 200f;
                // если точка не выходит за границы, то установить черный пиксель, иначе выбрать цвет в зависимости от кол-ва операций
                rgbColor = iters == -1 ? 0 : Color.HSBtoRGB(hue, 1f, 1f);
                RGB[x] = rgbColor;
//                display.drawPixel(x, y, rgbColor);
            }
            return null;
        }
        public void done(){   // отрисовка каждой строки, выгрузка ее
            for (int i = 0; i<winSize; i++){
                display.drawPixel(i, valY, RGB[i]);   // отрисовка отдельного пикселя в строке
            }
            display.repaint(0, 0, valY, winSize, 1);  // обновление только что отрисованноц строки
            rows_remaining--;

            if (rows_remaining == 0){
                enableUI(true);  // допуск нажатия на кнопки
            }
        }
    }
}


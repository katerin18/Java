package paket;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator{
    public final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range){  // методу передается прмяоугольный объект
        range.x = -2;
        range.y = -2;

        range.width = 4;
        range.height = 4;

    }

    public String toString(){
        return "Tricorn";
    }

    public int numIterations(double x, double y){
        ComplexNumber c = new ComplexNumber();

        int counter = 0;
        while (c.getSquare() < 4 && counter < MAX_ITERATIONS){
            c = c.KS();
            c.reQuad(x, y);
            counter++;
        }

        // возвращаем -1 в случае достижения значения max_iterations, чтобы показать, что точка не выходит за границы
        return counter >= MAX_ITERATIONS ? -1 : counter;
    }
}

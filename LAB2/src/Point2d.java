public class Point2d {
    private double xCoord = 3.14;
    private double yCoord;

    public Point2d (double x, double y) {
        xCoord = x;
        yCoord = y;
    }

    public Point2d () {
        this(0, 0);  // this значит, что я обращаюсь к конструктору заданного класса
        // конструктор определяет, как использовать входные данные
    }

    public double getX () {            // GET X //
        return xCoord;
    }
    public double getY () {            // GET Y //
        return yCoord;
    }

    public void setX (double val) {    // SET X //
        xCoord = val;
    }
    public void setY (double val) {    // SET Y //
        yCoord = val;
    }

}

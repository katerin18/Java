public class Lab1 {
    public static void main(String[] args){ // 3.1
        point3d first = new point3d(2,6,9.1); // создание нового экземпляра класса
        point3d second = new point3d(2,6,9.1);
        point3d third = new point3d(7,12.8,4);


        if (!equalss(first, second, third))
        System.out.println("Площадь равна " + computeArea(first, second, third));
        else
            System.out.println("Две точки равны");
    }

    public static double computeArea(point3d first, point3d second, point3d third){

        // вычисление сторон треугольника fst:
        double fs = first.distanceTo(second);
        double st = second.distanceTo(third);
        double tf = third.distanceTo(first);

        double p = (fs+st+tf)/2; // вычисление полупериметра
        double s = Math.sqrt(p*(p-fs)*(p-st)*(p-tf));  // вычисление площади по формуле Герона
        return s;
    }

    public static boolean equalss(point3d first, point3d second, point3d third) {
        if (first.equals(second) || second.equals(third) || (third.equals(first))) {
            return true; }
        return false; }}

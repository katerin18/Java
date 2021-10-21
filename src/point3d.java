public class point3d extends Point2d {
    private double zCoord;

    public void setZ(double valz){         // SET Z //
        zCoord = valz;
    }

    public double getZ(){                  // GET Z //
        return zCoord;
    }

    public point3d (double x, double y, double z) {   // 1.1
        // конструктор задает значения полей для класса
        setX(x); //xCoord = val;
        setY(y);
        zCoord = z;
    }

    public point3d() {
        this(0.0,0.0,0.0);   // 1.2
    }

    public boolean equals(point3d second){  // 1.4
        if ( (this.getX() == second.getX()) && (this.getY() == second.getY()) && (this.getZ() == second.getZ()) ){
            return true;
        }
            else
                return false;
    }

    public double distanceTo(point3d second){  // 2
        double d = Math.sqrt(Math.pow(second.getX()-this.getX(), 2) + Math.pow(second.getY()-this.getY(), 2) + Math.pow(second.getZ()-this.getZ(), 2));
        double result = Math.round(d*100)/100d;
        return result;
    }
}

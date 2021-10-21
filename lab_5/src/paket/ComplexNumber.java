package paket;


public class ComplexNumber{
    // real number part
    public double re;
    // imaginary number part
    public double im;

    public ComplexNumber(){
        this.re = 0;
        this.im = 0;
    }

    public ComplexNumber(double x, double y) {
        this.re=x;
        this.im=y;
    }

    public double getSquare(){
        return this.re * this.re + this.im * this.im;
    }

    public void reQuad(double x, double y){
        double u_re = re * re - im * im + x;
        double u_im = 2 * re * im + y;

        this.re = u_re;
        this.im = u_im;
    }

    public ComplexNumber abs (){
        return new ComplexNumber(Math.abs(this.re), Math.abs(this.im));
    }

    public ComplexNumber KS () {
        return new ComplexNumber(this.re, -this.im);
    }



}


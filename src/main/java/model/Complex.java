package model;

/**
 * This class modelises a complex number
 */
public class Complex {
    /**
     * The real part of the number
     */
    double ree;

    /**
     * The imaginary part of the number
     */
    double img;

    /**
     * Creates a new Complex
     *
     * @param r The real part
     * @param i The imaginary part
     */
    public Complex(double r, double i) {
        this.ree = r;
        this.img = i;
    }

    @Override
    public String toString() { return (ree + " + " + img + "*i"); }

    /**
     * @return the imaginary part
     */
    public double getImg() { return img; }

    /**
     * @return the real part
     */
    public double getRee() { return ree; }

    /**
     * @param z a Complex
     * @return the sum of this and z
     */
    public Complex somme(Complex z) {
        return new Complex(this.getRee() + z.getRee(), this.getImg() + z.getImg());
    }

    /**
     * @param z a Complex
     * @return the product of this and z
     */
    public Complex produit(Complex z) {
        double a = this.getRee();
        double b = this.getImg();
        double c = z.getRee();
        double d = z.getImg();
        return new Complex((a * c) - (b * d), (a * d) + (b * c));
    }

    /**
     * @param n the power
     * @return this at the power of n
     */
    public Complex puissance(int n) {
        if (n == 0){
            return new Complex(1, 0);
        }else{
            Complex z = this.copy();
            for (int i = 1; i < n; i++) {
                z = z.produit(this);
            }
            return z;
        }
    }

    /**
     * @return the "absolute value" of this.
     * It is nearly the module, but not exactly.
     */
    public double getAbs() {
        return ree * ree + img * img;
    }

    /**
     * @return a deep copy of this
     */
    public Complex copy() {
        return new Complex(getRee(), getImg());
    }

    /**
     * Computes the mandel number of this, with the constant z0
     *
     * @param z0 the constant
     */
    public void mandel(Complex z0) {
        double a = this.getRee();
        double b = this.getImg();

        ree = a * a - b * b + z0.ree;
        img = 2 * a * b + z0.img;
    }
}

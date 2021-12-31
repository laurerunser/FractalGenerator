package model;
//TODO EN doc
/**
 * Cette classe modelise un nombre complexe.
 */
public class Complex {
    double ree;
    double img;

    /**
     * Construit un complexe
     *
     * @param r - la partie reelle
     * @param i - la partie imaginaire
     */
    public Complex(double r, double i) {
        this.ree = r;
        this.img = i;
    }

    @Override
    public String toString() { return (ree + " + " + img + "*i"); }

    /**
     * @return la partie imaginaire
     */
    public double getImg() { return img; }

    /**
     * @return la partie reelle
     */
    public double getRee() { return ree; }

    /**
     * @param z - un Complexe
     * @return la somme de this et z
     */
    public Complex somme(Complex z) {
        return new Complex(this.getRee() + z.getRee(), this.getImg() + z.getImg());
    }

    /**
     * @param z - un Complexe
     * @return le produit de this et z
     */
    public Complex produit(Complex z) {
        double a = this.getRee();
        double b = this.getImg();
        double c = z.getRee();
        double d = z.getImg();
        return new Complex((a * c) - (b * d), (a * d) + (b * c));
    }

    /**
     * @return le module
     */
    public double getModule() {
        return Math.sqrt((this.img * this.img) + (this.ree * this.ree));
    }

    /**
     * @param n - la puissance
     * @return this puissance n
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
     * @return la valeur absolue d'un nombre complexe
     */
    public double getAbs() {
        return ree * ree + img * img;
    }

    public double getMul() { return 2 * ree * img; }

    /**
     * @return une copie de this
     */
    public Complex copy() {
        return new Complex(getRee(), getImg());
    }

    public void mandel (Complex z0){
        double a = this.getRee();
        double b = this.getImg();

        ree = a*a - b*b + z0.ree;
        img = 2*a*b + z0.img;

    }

}

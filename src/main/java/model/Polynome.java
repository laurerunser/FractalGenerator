package model;

import java.util.LinkedList;
import java.util.function.Function;
// TODO EN doc

/**
 * This class modelises a Polynom with Complex coefficients
 */
public class Polynome implements Function<Complex, Complex> {

    /**
     * The list of coefs
     * The polynom is : coef[0] + coef[1] * x + coef[2] * x^2 + ...
     */
    LinkedList<Complex> coef;

    /**
     * Creates a new Polynome from a list of Complex coefs
     *
     * @param coef the list of Complex coeds
     */
    private Polynome(LinkedList<Complex> coef) {
        this.coef = coef;
    }

    /**
     * Creates a polynome of the form z^2 + c
     *
     * @param c the constant of the Polynome
     * @return a polynome of the form z^2 + c
     */
    public static Polynome buildPolynomeSimple(Complex c){
        LinkedList<Complex> list = new LinkedList<>();
        list.add(c);
        list.add(new Complex(0, 0));
        list.add(new Complex (1, 0));
        return new Polynome(list);
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder(coef.get(0) + " ");
        for (int i=1; i<coef.size(); i++){
            s.append("+ (").append(coef.get(i)).append(") * z^").append(i);
        }
        return s.toString();
    }

    /**
     * Applies the polynome p to the complex number
     * @param arg0  the number to apply to polynome to
     * @return the result p(arg0)
     */
    @Override
    public Complex apply(Complex arg0) {
        Complex z = new Complex(0, 0);
        for (int i=0; i<coef.size(); i++){
            z = z.somme(((arg0.puissance(i)).produit(coef.get(i))));
        }
        return z;
    }
}
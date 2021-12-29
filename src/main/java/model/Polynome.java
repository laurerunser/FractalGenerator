package model;

import java.util.LinkedList;
import java.util.function.Function;
// TODO EN doc

/**
 * Cette classe modelise un polynome a coefficients complexes.
 */
public class Polynome implements Function<Complex,Complex> {

    /**
     * Liste des coefficients du polynome.
     * Le polynome est de la forme : coef[0] + coef[1] * x + coef[2] * x^2 + ...
     */
    LinkedList<Complex> coef;

    private Polynome (LinkedList<Complex> coef){
        this.coef=coef;
    }

    /**
     * Cree un polynome de la forme z^2 + c
     *
     * @param c - un Complexe qui sera la constante du polynome
     * @return un polynome de la forme z^2 + c
     */
    public static Polynome buildPolynomeSimple(Complex c){
        LinkedList<Complex> list = new LinkedList<>();
        list.add(c);
        list.add(new Complex(0, 0));
        list.add(new Complex (1, 0));
        return new Polynome(list);
    }

    /**
     * @return une String qui presente le polynome sous forme d'equation
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder(coef.get(0) + " ");
        for (int i=1; i<coef.size(); i++){
            s.append("+ (").append(coef.get(i)).append(") * z^").append(i);
        }
        return s.toString();
    }

    /**
     * Applique le polynome a un Complex arg0
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
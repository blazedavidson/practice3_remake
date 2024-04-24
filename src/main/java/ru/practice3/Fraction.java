package ru.practice3;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    public Fraction getStamp() {
        return new Fraction( this.num, this.denum);
   }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(300)
    public double doubleValue() {
        System.out.println("invoke double value");
        return (double) num/denum;
    }
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Fraction fraction = (Fraction) obj;
        return num == fraction.num && denum == fraction.denum;

    }
}

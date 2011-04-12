import java.awt.Color;

class Test{
    public static void main(String a[]){
        Rectangle r1=new Rectangle(30,40);
        Rectangle r2=new Rectangle();
        Rectangle r5=new Rectangle(25);
        Rectangle r3=new Rectangle(35,20);
        Rectangle r4=new Rectangle(35,20);
        Rectangle r6=null;
        System.out.println(r1.computeArea()); //1200.
        System.out.println(r2.computeArea()); //100.0
        System.out.println(r2.getWidth());   //10.0
        System.out.println(r2.getLength());  //10.0
        r2.setWidth(20);
        r2.setLength(15);
        System.out.println(r2.computeArea()); //300.0
        System.out.println(r3.computeArea()); //700.0
        System.out.println(r4.computeArea()); //700.0
        System.out.println(r5.computeArea()); //625.0
        System.out.println(r1.ID);      //1
        System.out.println(r3.ID);       //4
        //r2.ID = 99; will cause error
        System.out.println(r1.equals(r2));   //false
        System.out.println(r3.equals(r4));   //true

        // Added by Joel Edwards - 2011/04/10
        System.out.println("");
        r2.setWidth(-1);
        System.out.println("After r2.setWidth(-1):   r2.getWidth()=" + r2.getWidth());
        r2.setWidth(9);
        System.out.println("After r2.setWidth(9):    r2.getWidth()=" + r2.getWidth());
        r2.setWidth(101);
        System.out.println("After r2.setWidth(101):  r2.getWidth()=" + r2.getWidth());
        r2.setLength(-1);
        System.out.println("After r2.setLength(-1):  r2.getLength()=" + r2.getLength());
        r2.setLength(9);
        System.out.println("After r2.setLength(9):   r2.getLength()=" + r2.getLength());
        r2.setLength(101);
        System.out.println("After r2.setLength(101): r2.getLength()=" + r2.getLength());

        System.out.println("");
        r6 = r2.clone();
        System.out.println("After r6 = r2.clone(): r6.ID=" +r6.ID+ " r2.equals(r6)=" +r2.equals(r6));
        r6.setColor(Color.GREEN);
        System.out.println("After r6.setColor(Color.GREEN):     r2.equals(r6)=" +r2.equals(r6));
        r2.setColor(r6.getColor());
        System.out.println("After r2.setColor(r6.getColor()):   r2.equals(r6)=" +r2.equals(r6));
        r6.setWidth(13);
        System.out.println("After r6.setWidth(13):              r2.equals(r6)=" +r2.equals(r6));
        r2.setWidth(r6.getWidth()) ;
        System.out.println("After r2.setWidth(r6.getWidth()):   r2.equals(r6)=" +r2.equals(r6));
        r6.setLength(7);
        System.out.println("After r6.setLength(7):              r2.equals(r6)=" +r2.equals(r6));
        r2.setLength(r6.getLength());
        System.out.println("After r2.setLength(r6.getLength()): r2.equals(r6)=" +r2.equals(r6));
        System.out.println("");

        System.out.println("r1.ID=" + r1.ID);
        System.out.println("r2.ID=" + r2.ID);
        System.out.println("r3.ID=" + r3.ID);
        System.out.println("r4.ID=" + r4.ID);
        System.out.println("r5.ID=" + r5.ID);
        System.out.println("r6.ID=" + r6.ID);


    }
}

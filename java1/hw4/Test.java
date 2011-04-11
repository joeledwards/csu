class Test{
    public static void main(String a[]){
        Rectangle r1=new Rectangle(30,40);
        Rectangle r2=new Rectangle();
        Rectangle r5=new Rectangle(25);
        Rectangle r3=new Rectangle(35,20);
        Rectangle r4=new Rectangle(35,20);
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

        //r2.setWidth(-1); // generates DimensionOutOfRangeException
        //r2.setWidth(9); // generates DimensionOutOfRangeException
        //r2.setWidth(101); // generates DimensionOutOfRangeException
        //r2.setLength(-1); // generates DimensionOutOfRangeException
        //r2.setLength(9); // generates DimensionOutOfRangeException
        //r2.setLength(101); // generates DimensionOutOfRangeException

        //r2.ID = 13; // Compile error. Attempting to assign to final variable.
    }
}

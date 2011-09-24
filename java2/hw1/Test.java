import java.util.Observer;
import java.util.Observable;

class Test {
    public static void main(String argv[]) {
        soundOff(1);
        soundOff(0);
        soundOff(3);
        soundOff(4);
    }

    public static void soundOff( int x ){
      switch(x){
        case 1: System.out.print("One ") ;
        case 2: System.out.print("Two "); break ;
        case 3: System.out.print("Three ") ;
        default: System.out.print("Do What?") ;
      }
      System.out.print("\n");
    }
}


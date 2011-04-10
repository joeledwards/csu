class Boxd{
    public static void main(String args[]){      
        double vol;
        Box myBox = new Box();
        myBox.width=4;
        myBox.height=5;
        myBox.depth=6;
        vol=myBox.computeVolume();
        System.out.println(vol);
    }
}
class Box {
    double width;
    double height;
    double depth;

    double computeVolume(){
        return width*height*depth;
    }
} 

class Test{
    public static void main(String args[]) throws Exception{
        Circle x1 = new Circle(3);
        Circle x2 = new Circle(4);
        GraphicCircle c1 = new GraphicCircle(5,1);
        GraphicCircle c2 = new GraphicCircle(7,3);
        Rectangle y1 = new Rectangle (2,3);
        Rectangle y2 = new Rectangle (4,5);
        GraphicRectangle r1 = new GraphicRectangle (3,5,1);
        GraphicRectangle r2 = new GraphicRectangle (5,7,3);
        System.out.println("All Circles="+Circle.numCircles());
        System.out.println("All Rects="+Rectangle.numRectangles());
        System.out.println("All Shapes="+Shape.numShapes());

        Shape shapes[] = new Shape[8];
        shapes[0]=c1;
        shapes[1]=c2;
        shapes[2]=r1;
        shapes[3]=r2;
        shapes[4]=x1;
        shapes[5]=x2;
        shapes[6]=y1;
        shapes[7]=y2;
        double totalArea=0;
        for (int i=0; i<shapes.length; i++){
            totalArea+=shapes[i].area();
        }
        System.out.println("Total area= " +totalArea);
        Drawable drawables[] = new Drawable[4];
        drawables[0]=c1;
        drawables[1]=c2;
        drawables[2]=r1;
        drawables[3]=r2;
        for (int i=0; i<drawables.length; i++){
            drawables[i].draw();
        }
    }
}

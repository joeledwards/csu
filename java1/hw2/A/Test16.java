class Test16
{
    public static void main(String args[]) {
        int x = 0;
        for (int y=1; y<4; y++) {
            switch (y) {
                case 1: x=15; break;
                case 2: x=25;
                case 3: x=35; break;
            }
            System.out.println("y=" +y+ " x=" +x);
        }
    }

}

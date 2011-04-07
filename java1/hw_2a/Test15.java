class Test15
{
    public static void main(String args[]) {
        Result r = genA();
        System.out.println("A Results: g=" +r.g+ " c=" +r.c);
        r = genB();
        System.out.println("B Results: g=" +r.g+ " c=" +r.c);
    }

    private static Result genA() {
        int a=4, b=3, c=5, d=1, h=25, g=0;
        if (a>b & c++ >d) {
            g=h;
        }
        return new Result(g, c);
    }

    private static Result genB() {
        int a=0, b=3, c=5, d=1, h=25, g=0;
        if (a>b && c++ >d) {
            g=h;
        }
        return new Result(g, c);
    }
}

class Result {
    public int g = 0;
    public int c = 0;

    public Result(int g, int c) {
        this.g = g;
        this.c = c;
    }
}

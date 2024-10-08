public class Adapter {


    /*The Adapter pattern is pretty common in Java code.
    It’s very often used in systems based on some legacy code.
    In such cases, Adapters make legacy code work with modern classes.
 
    * RoundHoles are compatible with RoundPegs.
 */
    public class RoundHole {
        private double radius;

        public RoundHole(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        public boolean fits(RoundPeg peg) {
            boolean result;
            result = (this.getRadius() >= peg.getRadius());
            return result;
        }
    }

        
    /**
     * RoundPegs are compatible with RoundHoles.
     */
    public class RoundPeg {
        private double radius;

        public RoundPeg() {}

        public RoundPeg(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }
    }

        /**
     * SquarePegs are not compatible with RoundHoles (they were implemented by
     * previous development team). But we have to integrate them into our program.
     */
    public class SquarePeg {
        private double width;

        public SquarePeg(double width) {
            this.width = width;
        }

        public double getWidth() {
            return width;
        }

        public double getSquare() {
            double result;
            result = Math.pow(this.width, 2);
            return result;
        }
    }
        
        /**
     * Adapter allows fitting square pegs into round holes.
     */
    public class SquarePegAdapter extends RoundPeg {
        private SquarePeg peg;

        public SquarePegAdapter(SquarePeg peg) {
            this.peg = peg;
        }

        @Override
        public double getRadius() {
            double result;
            // Calculate a minimum circle radius, which can fit this peg.
            result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
            return result;
        }
    }


        /**
     * Somewhere in client code...
     */
    public class Demo {
        public static void main(String[] args) {

            Adapter adapter = new Adapter();
            // Round fits round, no surprise.
            RoundHole hole = adapter.new RoundHole(5);
            RoundPeg rpeg = adapter.new RoundPeg(5);
            if (hole.fits(rpeg)) {
                System.out.println("Round peg r5 fits round hole r5.");
            }

            SquarePeg smallSqPeg = adapter.new SquarePeg(2);
            SquarePeg largeSqPeg = adapter.new SquarePeg(20);
            // hole.fits(smallSqPeg); // Won't compile.

            // Adapter solves the problem.
            SquarePegAdapter smallSqPegAdapter = adapter.new SquarePegAdapter(smallSqPeg);
            SquarePegAdapter largeSqPegAdapter = adapter.new SquarePegAdapter(largeSqPeg);
            if (hole.fits(smallSqPegAdapter)) {
                System.out.println("Square peg w2 fits round hole r5.");
            }
            if (!hole.fits(largeSqPegAdapter)) {
                System.out.println("Square peg w20 does not fit into round hole r5.");
            }
        }
    }
}

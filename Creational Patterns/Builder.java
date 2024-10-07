public class BuilDer {

   /*  Builder is a creational design pattern that lets you construct
    complex objects step by step. The pattern allows you to produce
     different types and representations of an object 
     using the same construction code.
    */


   // Step-by-step car production

   /* In this example, the Builder pattern allows 
   step by step construction of different car models.
    The example also shows how Builder produces products 
    of different kinds (car manual) using the same building steps.
    The Director controls the order of the construction. 
    It knows which building steps to call to produce 
    this or that car model. It works with builders only via their
     common interface. This allows passing different 
     types of builders to the director.

    The end result is retrieved from the builder object 
    because the director can’t know the type of resulting product.
     Only the Builder object knows what does it build exactly.
    */

    /**
 * Builder interface defines all possible ways to configure a product.
 */
    public interface Builder {
        void setCarType(CarType type);
        void setSeats(int seats);
        void setEngine(Engine engine);
        void setTransmission(Transmission transmission);
        void setTripComputer(TripComputer tripComputer);
        void setGPSNavigator(GPSNavigator gpsNavigator);
    }

        /**
     * Concrete builders implement steps defined in the common interface.
     */
    public class CarBuilder implements Builder {
        private CarType type;
        private int seats;
        private Engine engine;
        private Transmission transmission;
        private TripComputer tripComputer;
        private GPSNavigator gpsNavigator;

        public void setCarType(CarType type) {
            this.type = type;
        }

        @Override
        public void setSeats(int seats) {
            this.seats = seats;
        }

        @Override
        public void setEngine(Engine engine) {
            this.engine = engine;
        }

        @Override
        public void setTransmission(Transmission transmission) {
            this.transmission = transmission;
        }

        @Override
        public void setTripComputer(TripComputer tripComputer) {
            this.tripComputer = tripComputer;
        }

        @Override
        public void setGPSNavigator(GPSNavigator gpsNavigator) {
            this.gpsNavigator = gpsNavigator;
        }

        public Car getResult() {
            return new Car(type, seats, engine, transmission, tripComputer, gpsNavigator);
        }
    }



        /**
     * Unlike other creational patterns, Builder can construct unrelated products,
     * which don't have the common interface.
     *
     * In this case we build a user manual for a car, using the same steps as we
     * built a car. This allows to produce manuals for specific car models,
     * configured with different features.
     */
    public class CarManualBuilder implements Builder{
        private CarType type;
        private int seats;
        private Engine engine;
        private Transmission transmission;
        private TripComputer tripComputer;
        private GPSNavigator gpsNavigator;

        @Override
        public void setCarType(CarType type) {
            this.type = type;
        }

        @Override
        public void setSeats(int seats) {
            this.seats = seats;
        }

        @Override
        public void setEngine(Engine engine) {
            this.engine = engine;
        }

        @Override
        public void setTransmission(Transmission transmission) {
            this.transmission = transmission;
        }

        @Override
        public void setTripComputer(TripComputer tripComputer) {
            this.tripComputer = tripComputer;
        }

        @Override
        public void setGPSNavigator(GPSNavigator gpsNavigator) {
            this.gpsNavigator = gpsNavigator;
        }

        public Manual getResult() {
            return new Manual(type, seats, engine, transmission, tripComputer, gpsNavigator);
        }
    }


        /**
     * Car is a product class.
     */
    public class Car {
        private final CarType carType;
        private final int seats;
        private final Engine engine;
        private final Transmission transmission;
        private final TripComputer tripComputer;
        private final GPSNavigator gpsNavigator;
        private double fuel = 0;

        public Car(CarType carType, int seats, Engine engine, Transmission transmission,
                TripComputer tripComputer, GPSNavigator gpsNavigator) {
            this.carType = carType;
            this.seats = seats;
            this.engine = engine;
            this.transmission = transmission;
            this.tripComputer = tripComputer;
            if (this.tripComputer != null) {
                this.tripComputer.setCar(this);
            }
            this.gpsNavigator = gpsNavigator;
        }

        public CarType getCarType() {
            return carType;
        }

        public double getFuel() {
            return fuel;
        }

        public void setFuel(double fuel) {
            this.fuel = fuel;
        }

        public int getSeats() {
            return seats;
        }

        public Engine getEngine() {
            return engine;
        }

        public Transmission getTransmission() {
            return transmission;
        }

        public TripComputer getTripComputer() {
            return tripComputer;
        }

        public GPSNavigator getGpsNavigator() {
            return gpsNavigator;
        }
    }


        
    public enum CarType {
        CITY_CAR, SPORTS_CAR, SUV
    }

    
    /**
     * Just another feature of a car.
     */
    public class Engine {
        private final double volume;
        private double mileage;
        private boolean started;

        public Engine(double volume, double mileage) {
            this.volume = volume;
            this.mileage = mileage;
        }

        public void on() {
            started = true;
        }

        public void off() {
            started = false;
        }

        public boolean isStarted() {
            return started;
        }

        public void go(double mileage) {
            if (started) {
                this.mileage += mileage;
            } else {
                System.err.println("Cannot go(), you must start engine first!");
            }
        }

        public double getVolume() {
            return volume;
        }

        public double getMileage() {
            return mileage;
        }
    }

    /**
     * Just another feature of a car.
     */
    public class GPSNavigator {
        private String route;

        public GPSNavigator() {
            this.route = "221b, Baker Street, London  to Scotland Yard, 8-10 Broadway, London";
        }

        public GPSNavigator(String manualRoute) {
            this.route = manualRoute;
        }

        public String getRoute() {
            return route;
        }
    }

        /**
     * Just another feature of a car.
     */
    public enum Transmission {
        SINGLE_SPEED, MANUAL, AUTOMATIC, SEMI_AUTOMATIC
    }

    /**
     * Just another feature of a car.
     */
    public class TripComputer {

        private Car car;

        public void setCar(Car car) {
            this.car = car;
        }

        public void showFuelLevel() {
            System.out.println("Fuel level: " + car.getFuel());
        }

        public void showStatus() {
            if (this.car.getEngine().isStarted()) {
                System.out.println("Car is started");
            } else {
                System.out.println("Car isn't started");
            }
        }
    }

        /**
     * Director defines the order of building steps. It works with a builder object
     * through common Builder interface. Therefore it may not know what product is
     * being built.
     */
    public class Director {

        public void constructSportsCar(Builder builder) {
            builder.setCarType(CarType.SPORTS_CAR);
            builder.setSeats(2);
            builder.setEngine(new Engine(3.0, 0));
            builder.setTransmission(Transmission.SEMI_AUTOMATIC);
            builder.setTripComputer(new TripComputer());
            builder.setGPSNavigator(new GPSNavigator());
        }

        public void constructCityCar(Builder builder) {
            builder.setCarType(CarType.CITY_CAR);
            builder.setSeats(2);
            builder.setEngine(new Engine(1.2, 0));
            builder.setTransmission(Transmission.AUTOMATIC);
            builder.setTripComputer(new TripComputer());
            builder.setGPSNavigator(new GPSNavigator());
        }

        public void constructSUV(Builder builder) {
            builder.setCarType(CarType.SUV);
            builder.setSeats(4);
            builder.setEngine(new Engine(2.5, 0));
            builder.setTransmission(Transmission.MANUAL);
            builder.setGPSNavigator(new GPSNavigator());
        }
    }

    /**
     * Demo class. Everything comes together here.
     */
    public class Demo {

        public static void main(String[] args) {
            Director director = new Director();

            // Director gets the concrete builder object from the client
            // (application code). That's because application knows better which
            // builder to use to get a specific product.
            CarBuilder builder = new CarBuilder();
            director.constructSportsCar(builder);

            // The final product is often retrieved from a builder object, since
            // Director is not aware and not dependent on concrete builders and
            // products.
            Car car = builder.getResult();
            System.out.println("Car built:\n" + car.getCarType());


            CarManualBuilder manualBuilder = new CarManualBuilder();

            // Director may know several building recipes.
            director.constructSportsCar(manualBuilder);
            Manual carManual = manualBuilder.getResult();
            System.out.println("\nCar manual built:\n" + carManual.print());
        }

    }


}

public class Singleton {
    

    /* 
    Singleton is a creational design pattern that 
    lets you ensure that a class has only one 
    instance, while providing a global access point to this instance.

    All implementations of the Singleton have these two steps in common:

    1. Make the default constructor private, to prevent other objects
     from using the new operator with the Singleton class.
    
    2.  Create a static creation method that acts as a constructor.
    Under the hood, this method calls the private constructor to create an
    object and saves it in a static field. All following
    calls to this method return the cached object.
    */

    // Naïve Singleton (single-threaded)

    /*It’s pretty easy to implement a sloppy Singleton. 
    You just need to hide the constructor and implement a static creation method.
    */

    public final class SingleTon {
        private static SingleTon instance;
        public String value;
    
        private SingleTon(String value) {

            // The following code emulates slow initialization.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.value = value;
        }
    
        public static SingleTon getInstance(String value) {
            Singleton mySingleton = new Singleton(); // Create an instance of Factory class which is the parent class
            if (instance == null) {
                instance = mySingleton.new SingleTon(value);
            }
            return instance;
        }
    }


    public class DemoSingleThread {
        public static void main(String[] args) {
            System.out.println("If you see the same value, then singleton was reused (yay!)" + "\n" +
                    "If you see different values, then 2 singletons were created (booo!!)" + "\n\n" +
                    "RESULT:" + "\n");
            Singleton mySingleton = new Singleton(); // Create an instance of Factory class which is the parent class
            Singleton singleton = mySingleton.SingleTon.getInstance("FOO");
            Singleton anotherSingleton = SingleTon.getInstance("BAR");
            System.out.println(singleTon.value);
            System.out.println(anotherSingleTon.value);
        }
    }





}

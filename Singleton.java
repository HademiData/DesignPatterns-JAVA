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

            SingleTon singleton = SingleTon.getInstance("FOO");
            SingleTon anotherSingleton = SingleTon.getInstance("BAR");
            System.out.println(singleton.value);
            System.out.println(anotherSingleton.value);
        }
    }


    /*  
     * Naïve Singleton (multithreaded)
    The same class behaves incorrectly in a multithreaded environment. 
    Multiple threads can call the creation method 
    simultaneously and get several instances of Singleton class.
     */

     public final class Singleton2 {
        private static Singleton2 instance;
        public String value;
    
        private Singleton2(String value) {
            // The following code emulates slow initialization.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.value = value;
        }
    
        public static Singleton2 getInstance(String value) {
            Singleton mySingleton = new Singleton(); // Create an instance of the parent class
            if (instance == null) {
                instance = mySingleton.new Singleton2(value);
            }
            return instance;
        }
    }

    public class DemoMultiThread {

        public static void main(String[] args) {
            System.out.println("If you see the same value, then singleton was reused (yay!)" + "\n" +
                    "If you see different values, then 2 singletons were created (booo!!)" + "\n\n" +
                    "RESULT:" + "\n");
            Thread threadFoo = new Thread(new ThreadFoo());
            Thread threadBar = new Thread(new ThreadBar());
            threadFoo.start();
            threadBar.start();
        }
    
        static class ThreadFoo implements Runnable {
            @Override
            public void run() {
                Singleton2 singleton = Singleton2.getInstance("FOO");
                System.out.println(singleton.value);
            }
        }
    
        static class ThreadBar implements Runnable {
            @Override
            public void run() {
                Singleton2 singleton = Singleton2.getInstance("BAR");
                System.out.println(singleton.value);
            }
        }
    }

    /*
     * Missing Synchronization: In the getInstance method of Singleton2,
     *  you're checking if instance is null before creating a new one. 
     * However, multiple threads can call this method simultaneously, seeing
     *  instance as null momentarily, and each thread might create a separate instance.
     */

     // To fix the problem, we have to synchronize threads 
     // during first creation of the Singleton object.

     /*
      * Synchronizing threads in programming means coordinating 
      their activities to ensure that they execute in a predictable and controlled manner, 
      especially when they are accessing shared resources.
       This is crucial to prevent race conditions, deadlocks, and other concurrency issues.
      */

      public final class Singleton3 {
        // The field must be declared volatile so that double check lock would work
        // correctly.
        private static volatile Singleton3 instance;
    
        public String value;

        
        private Singleton3(String value) {
            this.value = value;
        }
    
        public static Singleton3 getInstance(String value) {
            // The approach taken here is called double-checked locking (DCL). It
            // exists to prevent race condition between multiple threads that may
            // attempt to get singleton instance at the same time, creating separate
            // instances as a result.
            //
            // It may seem that having the `result` variable here is completely
            // pointless. There is, however, a very important caveat when
            // implementing double-checked locking in Java, which is solved by
            // introducing this local variable.
            
            Singleton3 result = instance;

            Singleton myParentSingleton = new Singleton(); // Create an instance of the parent class

            if (result != null) {
                return result;
            }
            synchronized(Singleton3.class) {
                if (instance == null) {
                    instance = myParentSingleton.new Singleton3(value);
                }
                return instance;
            }
        }
    }

    public class DemoMultiThread2 {
        public static void main(String[] args) {
            System.out.println("If you see the same value, then singleton was reused (yay!)" + "\n" +
                    "If you see different values, then 2 singletons were created (booo!!)" + "\n\n" +
                    "RESULT:" + "\n");
            Thread threadFoo = new Thread(new ThreadFoo());
            Thread threadBar = new Thread(new ThreadBar());
            threadFoo.start();
            threadBar.start();
        }
    
        static class ThreadFoo implements Runnable {
            @Override
            public void run() {
                Singleton3 singleton = Singleton3.getInstance("FOO");
                System.out.println(singleton.value);
            }
        }
    
        static class ThreadBar implements Runnable {
            @Override
            public void run() {
                Singleton3 singleton = Singleton3.getInstance("BAR");
                System.out.println(singleton.value);
            }
        }
    }

}

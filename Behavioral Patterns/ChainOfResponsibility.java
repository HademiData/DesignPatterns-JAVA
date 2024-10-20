
import java.util.HashMap;
import java.util.Map;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ChainOfResponsibility {

    /*
     * Chain of Responsibility is a behavioral design pattern that 
     * lets you pass requests along a chain of handlers. Upon receiving
     *  a request, each handler decides either to process the request
     *  or to pass it to the next handler in the chain.
     */
    

     /*The pattern allows multiple objects to handle the request without
      coupling sender class to the concrete classes of the receivers.
       The chain can be composed dynamically at runtime with any handler
        that follows a standard handler interface.
     */

    /*
     * Filtering access

     This example shows how a request containing user data passes a 
     sequential chain of handlers that perform various things such as 
     authentication, authorization, and validation.

     This example is a bit different from the canonical version
      of the pattern given by various authors. Most of the pattern 
      examples are built on the notion of looking for the right handler, 
      launching it and exiting the chain after that. But here we execute
       every handler until there’s one that can’t handle a request. Be 
       aware that this still is the Chain of Responsibility pattern, even
        though the flow is a bit different.
     */

        /**
     * Base middleware class.
     */
    public abstract class Middleware {
        private Middleware next;

        /**
         * Builds chains of middleware objects.
         */
        public static Middleware link(Middleware first, Middleware... chain) {
            Middleware head = first;
            for (Middleware nextInChain: chain) {
                head.next = nextInChain;
                head = nextInChain;
            }
            return first;
        }

        /**
         * Subclasses will implement this method with concrete checks.
         */
        public abstract boolean check(String email, String password);

        /**
         * Runs check on the next object in chain or ends traversing if we're in
         * last object in chain.
         */
        protected boolean checkNext(String email, String password) {
            if (next == null) {
                return true;
            }
            return next.check(email, password);
        }
    }

        /**
     * ConcreteHandler. Checks whether there are too many failed login requests.
     */
    public class ThrottlingMiddleware extends Middleware {
        private int requestPerMinute;
        private int request;
        private long currentTime;

        public ThrottlingMiddleware(int requestPerMinute) {
            this.requestPerMinute = requestPerMinute;
            this.currentTime = System.currentTimeMillis();
        }

        /**
         * Please, not that checkNext() call can be inserted both in the beginning
         * of this method and in the end.
         *
         * This gives much more flexibility than a simple loop over all middleware
         * objects. For instance, an element of a chain can change the order of
         * checks by running its check after all other checks.
         */
        public boolean check(String email, String password) {
            if (System.currentTimeMillis() > currentTime + 60_000) {
                request = 0;
                currentTime = System.currentTimeMillis();
            }

            request++;
            
            if (request > requestPerMinute) {
                System.out.println("Request limit exceeded!");
                Thread.currentThread().stop();
            }
            return checkNext(email, password);
        }
    }

        /**
     * ConcreteHandler. Checks whether a user with the given credentials exists.
     */
    public class UserExistsMiddleware extends Middleware {
        private Server server;

        public UserExistsMiddleware(Server server) {
            this.server = server;
        }

        public boolean check(String email, String password) {
            if (!server.hasEmail(email)) {
                System.out.println("This email is not registered!");
                return false;
            }
            if (!server.isValidPassword(email, password)) {
                System.out.println("Wrong password!");
                return false;
            }
            return checkNext(email, password);
        }
    }

        
    /**
     * ConcreteHandler. Checks a user's role.
     */
    public class RoleCheckMiddleware extends Middleware {
        public boolean check(String email, String password) {
            if (email.equals("admin@example.com")) {
                System.out.println("Hello, admin!");
                return true;
            }
            System.out.println("Hello, user!");
            return checkNext(email, password);
        }
    }

        /**
     * Server class.
     */
    public class Server {
        private Map<String, String> users = new HashMap<>();
        private Middleware middleware;

        /**
         * Client passes a chain of object to server. This improves flexibility and
         * makes testing the server class easier.
         */
        public void setMiddleware(Middleware middleware) {
            this.middleware = middleware;
        }

        /**
         * Server gets email and password from client and sends the authorization
         * request to the chain.
         */
        public boolean logIn(String email, String password) {
            if (middleware.check(email, password)) {
                System.out.println("Authorization have been successful!");

                // Do something useful here for authorized users.

                return true;
            }
            return false;
        }

        public void register(String email, String password) {
            users.put(email, password);
        }

        public boolean hasEmail(String email) {
            return users.containsKey(email);
        }

        public boolean isValidPassword(String email, String password) {
            return users.get(email).equals(password);
        }
    }


        /**
     * Demo class. Everything comes together here.
     */
    public class Demo {
        private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private static Server server;

        private static void init() {

            ChainOfResponsibility chaOfRes = new ChainOfResponsibility();
            server = chaOfRes.new Server();
            server.register("admin@example.com", "admin_pass");
            server.register("user@example.com", "user_pass");

            // All checks are linked. Client can build various chains using the same
            // components.
            Middleware middleware = Middleware.link(
                chaOfRes.new ThrottlingMiddleware(2),
                chaOfRes.new UserExistsMiddleware(server),
                chaOfRes.new RoleCheckMiddleware()
            );

            // Server gets a chain from client code.
            server.setMiddleware(middleware);
        }

        public static void main(String[] args) throws IOException {
            init();

            boolean success;
            do {
                System.out.print("Enter email: ");
                String email = reader.readLine();
                System.out.print("Input password: ");
                String password = reader.readLine();
                success = server.logIn(email, password);
            } while (!success);
        }
    }






}

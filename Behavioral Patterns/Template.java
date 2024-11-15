public class Template {
    
    /*
     * Template Method is a behavioral design pattern that defines the skeleton
     *  of an algorithm in the superclass but lets subclasses 
     * override specific steps of the algorithm without changing its structure.
     */

        
    /**
     * Base class of social network.
     */
    public abstract class Network {
        String userName;
        String password;

        Network() {}

        /**
         * Publish the data to whatever network.
         */
        public boolean post(String message) {
            // Authenticate before posting. Every network uses a different
            // authentication method.
            if (logIn(this.userName, this.password)) {
                // Send the post data.
                boolean result =  sendData(message.getBytes());
                logOut();
                return result;
            }
            return false;
        }

        abstract boolean logIn(String userName, String password);
        abstract boolean sendData(byte[] data);
        abstract void logOut();
    }


    

}

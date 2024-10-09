public class Bridge {

    /*
     * Bridge is a structural design pattern that divides business 
     * logic or huge class into separate class hierarchies that can 
     * be developed independently.

        One of these hierarchies (often called the Abstraction) 
        will get a reference to an object of the second hierarchy
         (Implementation). The abstraction will be able to delegate
          some (sometimes, most) of its calls to the implementations 
          object. Since all implementations will have a common 
          interface, they’d be interchangeable inside the abstraction.
     */

     // Bridge between devices and remote controls
// This example shows separation between the classes of remotes and 
// devices that they control.

//  Remotes act as abstractions, and devices are their 
// implementations. Thanks to the common interfaces, the 
// same remotes can work with different devices and vice versa.

    // The Bridge pattern allows changing or even creating new 
    // classes without touching the code of the opposite hierarchy.

        
    public interface Device {
        boolean isEnabled();

        void enable();

        void disable();

        int getVolume();

        void setVolume(int percent);

        int getChannel();

        void setChannel(int channel);

        void printStatus();
    }

        
    public class Radio implements Device {
        private boolean on = false;
        private int volume = 30;
        private int channel = 1;

        @Override
        public boolean isEnabled() {
            return on;
        }

        @Override
        public void enable() {
            on = true;
        }

        @Override
        public void disable() {
            on = false;
        }

        @Override
        public int getVolume() {
            return volume;
        }

        @Override
        public void setVolume(int volume) {
            if (volume > 100) {
                this.volume = 100;
            } else if (volume < 0) {
                this.volume = 0;
            } else {
                this.volume = volume;
            }
        }

        @Override
        public int getChannel() {
            return channel;
        }

        @Override
        public void setChannel(int channel) {
            this.channel = channel;
        }

        @Override
        public void printStatus() {
            System.out.println("------------------------------------");
            System.out.println("| I'm radio.");
            System.out.println("| I'm " + (on ? "enabled" : "disabled"));
            System.out.println("| Current volume is " + volume + "%");
            System.out.println("| Current channel is " + channel);
            System.out.println("------------------------------------\n");
        }
    }


    public class Tv implements Device {
        private boolean on = false;
        private int volume = 30;
        private int channel = 1;

        @Override
        public boolean isEnabled() {
            return on;
        }

        @Override
        public void enable() {
            on = true;
        }

        @Override
        public void disable() {
            on = false;
        }

        @Override
        public int getVolume() {
            return volume;
        }

        @Override
        public void setVolume(int volume) {
            if (volume > 100) {
                this.volume = 100;
            } else if (volume < 0) {
                this.volume = 0;
            } else {
                this.volume = volume;
            }
        }

        @Override
        public int getChannel() {
            return channel;
        }

        @Override
        public void setChannel(int channel) {
            this.channel = channel;
        }

        @Override
        public void printStatus() {
            System.out.println("------------------------------------");
            System.out.println("| I'm TV set.");
            System.out.println("| I'm " + (on ? "enabled" : "disabled"));
            System.out.println("| Current volume is " + volume + "%");
            System.out.println("| Current channel is " + channel);
            System.out.println("------------------------------------\n");
        }
    }


    public interface Remote {
        void power();

        void volumeDown();

        void volumeUp();

        void channelDown();

        void channelUp();
    }

    public class BasicRemote implements Remote {
        protected Device device;

        public BasicRemote() {}

        public BasicRemote(Device device) {
            this.device = device;
        }

        @Override
        public void power() {
            System.out.println("Remote: power toggle");
            if (device.isEnabled()) {
                device.disable();
            } else {
                device.enable();
            }
        }

        @Override
        public void volumeDown() {
            System.out.println("Remote: volume down");
            device.setVolume(device.getVolume() - 10);
        }

        @Override
        public void volumeUp() {
            System.out.println("Remote: volume up");
            device.setVolume(device.getVolume() + 10);
        }

        @Override
        public void channelDown() {
            System.out.println("Remote: channel down");
            device.setChannel(device.getChannel() - 1);
        }

        @Override
        public void channelUp() {
            System.out.println("Remote: channel up");
            device.setChannel(device.getChannel() + 1);
        }
    }



    public class AdvancedRemote extends BasicRemote {

        public AdvancedRemote(Device device) {
            super.device = device;
        }

        public void mute() {
            System.out.println("Remote: mute");
            device.setVolume(0);
        }
    }

    public class Demo {


        public static void main(String[] args) {
            Bridge Parent = new Bridge();
            testDevice(Parent.new Tv());
            testDevice(Parent.new Radio());
        }

        public static void testDevice(Device device) {
            Bridge Parent = new Bridge();
            System.out.println("Tests with basic remote.");
            BasicRemote basicRemote = Parent.new BasicRemote(device);
            basicRemote.power();
            device.printStatus();

            System.out.println("Tests with advanced remote.");
            AdvancedRemote advancedRemote = Parent.new AdvancedRemote(device);
            advancedRemote.power();
            advancedRemote.mute();
            device.printStatus();
        }
    }




}

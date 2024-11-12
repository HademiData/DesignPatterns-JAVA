

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class State {
    

    /*
     * State is a behavioral design pattern that lets an object alter its
     *  behavior when its internal state changes. 
     * It appears as if the object changed its class.
     */


    /*
     * The pattern extracts state-related behaviors into separate state 
     * classes and forces the original object to delegate the work to an 
     * instance of these classes, instead of acting on its own.
     */



     /*
      * In this example, the State pattern lets the same media player controls
       behave differently, depending on the current playback state. The main 
       class of the player contains a reference to a state object, which 
       performs most of the work for the player. Some actions may end-up 
       replacing the state object with another, which changes the way 
       the player reacts to the user interactions.
      */

        /**
     * Common interface for all states.
     */
    public abstract class state {
        Player player;

        /**
         * Context passes itself through the state constructor. This may help a
         * state to fetch some useful context data if needed.
         */
        state(Player player) {
            this.player = player;
        }

        public abstract String onLock();
        public abstract String onPlay();
        public abstract String onNext();
        public abstract String onPrevious();
    }

        
    /**
     * Concrete states provide the special implementation for all interface methods.
     */
    public class LockedState extends state {

        LockedState(Player player) {
            super(player);
            player.setPlaying(false);
        }

        @Override
        public String onLock() {
            if (player.isPlaying()) {

                State parentState = new State();
                player.changeState(new ReadyState(player));
                return "Stop playing";
            } else {
                return "Locked...";
            }
        }

        @Override
        public String onPlay() {
            player.changeState(new ReadyState(player));
            return "Ready";
        }

        @Override
        public String onNext() {
            return "Locked...";
        }

        @Override
        public String onPrevious() {
            return "Locked...";
        }
    }


        /**
     * They can also trigger state transitions in the context.
     */
    public class ReadyState extends state {

        public ReadyState(Player player) {
            super(player);
        }

        @Override
        public String onLock() {
            player.changeState(new LockedState(player));
            return "Locked...";
        }

        @Override
        public String onPlay() {
            String action = player.startPlayback();
            player.changeState(new PlayingState(player));
            return action;
        }

        @Override
        public String onNext() {
            return "Locked...";
        }

        @Override
        public String onPrevious() {
            return "Locked...";
        }
    }

    public class Player {
        private State state;
        private boolean playing = false;
        private List<String> playlist = new ArrayList<>();
        private int currentTrack = 0;
    
        public Player() {

            this.state = new ReadyState(this);
            setPlaying(true);
            for (int i = 1; i <= 12; i++) {
                playlist.add("Track " + i);
            }
        }
    
        public void changeState(State state) {
            this.state = state;
        }
    
        public State getState() {
            return state;
        }
    
        public void setPlaying(boolean playing) {
            this.playing = playing;
        }
    
        public boolean isPlaying() {
            return playing;
        }
    
        public String startPlayback() {
            return "Playing " + playlist.get(currentTrack);
        }
    
        public String nextTrack() {
            currentTrack++;
            if (currentTrack > playlist.size() - 1) {
                currentTrack = 0;
            }
            return "Playing " + playlist.get(currentTrack);
        }
    
        public String previousTrack() {
            currentTrack--;
            if (currentTrack < 0) {
                currentTrack = playlist.size() - 1;
            }
            return "Playing " + playlist.get(currentTrack);
        }
    
        public void setCurrentTrackAfterStop() {
            this.currentTrack = 0;
        }
    }

    public class UI {
        private Player player;
        private static JTextField textField = new JTextField();
    
        public UI(Player player) {
            this.player = player;
        }
    
        public void init() {
            JFrame frame = new JFrame("Test player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel context = new JPanel();
            context.setLayout(new BoxLayout(context, BoxLayout.Y_AXIS));
            frame.getContentPane().add(context);
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
            context.add(textField);
            context.add(buttons);
    
            // Context delegates handling user's input to a state object. Naturally,
            // the outcome will depend on what state is currently active, since all
            // states can handle the input differently.
            JButton play = new JButton("Play");
            play.addActionListener(e -> textField.setText(player.getState().onPlay()));
            JButton stop = new JButton("Stop");
            stop.addActionListener(e -> textField.setText(player.getState().onLock()));
            JButton next = new JButton("Next");
            next.addActionListener(e -> textField.setText(player.getState().onNext()));
            JButton prev = new JButton("Prev");
            prev.addActionListener(e -> textField.setText(player.getState().onPrevious()));
            frame.setVisible(true);
            frame.setSize(300, 100);
            buttons.add(play);
            buttons.add(stop);
            buttons.add(next);
            buttons.add(prev);
        }
    }

}

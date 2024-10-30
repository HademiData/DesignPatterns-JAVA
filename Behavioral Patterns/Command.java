import java.util.Stack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Command {
    /*
     * Command is a behavioral design pattern that turns 
     * a request into a stand-alone object that contains all
     *  information about the request. This transformation lets 
     * you pass requests as a method arguments, delay or queue a 
     * request’s execution, and support undoable operations.
     */
    

     // Command is behavioral design pattern that converts requests or simple operations into objects.

     // The conversion allows deferred or remote execution of commands, storing command history, etc.
     /*
      * Text editor commands and undo
The text editor in this example creates new command objects each time a user 
interacts with it. After executing its actions, a command is pushed to the history stack.

Now, to perform the undo operation, the application takes the last executed
 command from the history and either performs an inverse action or restores
  the past state of the editor, saved by that command.
      */

    public abstract class Command {
        public Editor editor;
        private String backup;
    
        Command(Editor editor) {
            this.editor = editor;
        }
    
        void backup() {
            backup = editor.textField.getText();
        }
    
        public void undo() {
            editor.textField.setText(backup);
        }
    
        public abstract boolean execute();
    }


        
    public class CopyCommand extends Command {

        public CopyCommand(Editor editor) {
            super(editor);
        }

        @Override
        public boolean execute() {
            editor.clipboard = editor.textField.getSelectedText();
            return false;
        }
    }

    public class PasteCommand extends Command {

        public PasteCommand(Editor editor) {
            super(editor);
        }

        @Override
        public boolean execute() {
            if (editor.clipboard == null || editor.clipboard.isEmpty()) return false;

            backup();
            editor.textField.insert(editor.clipboard, editor.textField.getCaretPosition());
            return true;
        }
    }

    public class CutCommand extends Command {

        public CutCommand(Editor editor) {
            super(editor);
        }

        @Override
        public boolean execute() {
            if (editor.textField.getSelectedText().isEmpty()) return false;

            backup();
            String source = editor.textField.getText();
            editor.clipboard = editor.textField.getSelectedText();
            editor.textField.setText(cutString(source));
            return true;
        }

        private String cutString(String source) {
            String start = source.substring(0, editor.textField.getSelectionStart());
            String end = source.substring(editor.textField.getSelectionEnd());
            return start + end;
        }
    }


    public class CommandHistory {
        private Stack<Command> history = new Stack<>();

        public void push(Command c) {
            history.push(c);
        }

        public Command pop() {
            return history.pop();
        }

        public boolean isEmpty() { return history.isEmpty(); }
    }

    public class Editor {
        public JTextArea textField;
        public String clipboard;
        private CommandHistory history = new CommandHistory();

        public void init() {
            JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
            JPanel content = new JPanel();
            frame.setContentPane(content);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            textField = new JTextArea();
            textField.setLineWrap(true);
            content.add(textField);
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton ctrlC = new JButton("Ctrl+C");
            JButton ctrlX = new JButton("Ctrl+X");
            JButton ctrlV = new JButton("Ctrl+V");
            JButton ctrlZ = new JButton("Ctrl+Z");
            Editor editor = this;
            ctrlC.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeCommand(new CopyCommand(editor));
                }
            });
            ctrlX.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeCommand(new CutCommand(editor));
                }
            });
            ctrlV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeCommand(new PasteCommand(editor));
                }
            });
            ctrlZ.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undo();
                }
            });
            buttons.add(ctrlC);
            buttons.add(ctrlX);
            buttons.add(ctrlV);
            buttons.add(ctrlZ);
            content.add(buttons);
            frame.setSize(450, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        private void executeCommand(Command command) {
            if (command.execute()) {
                history.push(command);
            }
        }

        private void undo() {
            if (history.isEmpty()) return;

            Command command = history.pop();
            if (command != null) {
                command.undo();
            }
        }
    }

    public class Demo {
        public static void main(String[] args) {
            Editor editor = new Editor();
            editor.init();
        }
    }



}

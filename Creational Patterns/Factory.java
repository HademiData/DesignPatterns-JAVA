import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



class Factory {

            /**
         * Common interface for all buttons.
         */
        public interface Button {
            void render();
            void onClick();
        }
    
        /**
         * HTML button implementation.
         */
        public class HtmlButton implements Button {

            public void render() {
                System.out.println("<button>Test Button</button>");
                onClick();
            }

            public void onClick() {
                System.out.println("Click! Button says - 'Hello World!'");
            }
        }

        /**
         * Windows button implementation.
         */
        public class WindowsButton implements Button {
            JPanel panel = new JPanel();
            JFrame frame = new JFrame();
            JButton button;

            public void render() {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JLabel label = new JLabel("Hello World!");
                label.setOpaque(true);
                label.setBackground(new Color(235, 233, 126));
                label.setFont(new Font("Dialog", Font.BOLD, 44));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                panel.setLayout(new FlowLayout(FlowLayout.CENTER));
                frame.getContentPane().add(panel);
                panel.add(label);
                onClick();
                panel.add(button);

                frame.setSize(320, 200);
                frame.setVisible(true);
                onClick();
            }

            public void onClick() {
                button = new JButton("Exit");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        System.exit(0);
                    }
                });
            }
        }


        /**
         * Base factory class. Note that "factory" is merely a role for the class. It
         * should have some core business logic which needs different products to be
         * created.
         */
        public abstract class Dialog {

            public void renderWindow() {
                // ... other code ...

                Button okButton = createButton();
                okButton.render();
            }

            /**
             * Subclasses will override this method in order to create specific button
             * objects.
             */
            public abstract Button createButton();
        }


        /**
         * Windows Dialog will produce Windows buttons.
         */
        public class WindowsDialog extends Dialog {

            @Override
            public Button createButton() {
                return new WindowsButton();
            }
        }

            /**
         * HTML Dialog will produce HTML buttons.
         */
        public class HtmlDialog extends Dialog {

            @Override
            public Button createButton() {
                return new HtmlButton();
            }
        }


        /**
         * Demo class. Everything comes together here.
         */
        public class Demo {
            private static Dialog dialog;

            public static void main(String[] args) {
                configure();
                runBusinessLogic();
            }

            /**
             * The concrete factory is usually chosen depending on configuration or
             * environment options.
             */
            static void configure() {
                Factory myFactory = new Factory(); // Create an instance of Factory class which is the parent class
                // and is needed for creating an instance of both dialog classes.
                if (System.getProperty("os.name").equals("Windows 10")) {
                    dialog = myFactory.new WindowsDialog();
                } else {
                    dialog = myFactory.new HtmlDialog();
                }
            }

            /**
             * All of the client code should work with factories and products through
             * abstract interfaces. This way it does not care which factory it works
             * with and what kind of product it returns.
             */
            static void runBusinessLogic() {
                dialog.renderWindow();
            }
        }


}
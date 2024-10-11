import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Composite {
    
/*
 * Composite is a structural design pattern that lets 
 * you compose objects into tree structures and 
 * then work with these structures as if they 
 * were individual objects.
 */
   
    /*
     * Using the Composite pattern makes sense only when the core model of your app can be represented as a tree.

    For example, imagine that you have two types of 
    objects: Products and Boxes. A Box can contain 
    several Products as well as a number of smaller 
    Boxes. These little Boxes can also hold
    some Products or even smaller Boxes, and so on.
     */


     /*
      * Simple and compound graphical shapes
    This example shows how to create complex graphical shapes, 
    composed of simpler shapes and treat both of them uniformly.
      */



    public interface Shape {
        int getX();
        int getY();
        int getWidth();
        int getHeight();
        void move(int x, int y);
        boolean isInsideBounds(int x, int y);
        void select();
        void unSelect();
        boolean isSelected();
        void paint(Graphics graphics);
    }


    abstract class BaseShape implements Shape {
        public int x;
        public int y;
        public Color color;
        private boolean selected = false;
    
        BaseShape(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    
        @Override
        public int getX() {
            return x;
        }
    
        @Override
        public int getY() {
            return y;
        }
    
        @Override
        public int getWidth() {
            return 0;
        }
    
        @Override
        public int getHeight() {
            return 0;
        }
    
        @Override
        public void move(int x, int y) {
            this.x += x;
            this.y += y;
        }
    
        @Override
        public boolean isInsideBounds(int x, int y) {
            return x > getX() && x < (getX() + getWidth()) &&
                    y > getY() && y < (getY() + getHeight());
        }
    
        @Override
        public void select() {
            selected = true;
        }
    
        @Override
        public void unSelect() {
            selected = false;
        }
    
        @Override
        public boolean isSelected() {
            return selected;
        }
    
        void enableSelectionStyle(Graphics graphics) {
            graphics.setColor(Color.LIGHT_GRAY);
    
            Graphics2D g2 = (Graphics2D) graphics;
            float[] dash1 = {2.0f};
            g2.setStroke(new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    2.0f, dash1, 0.0f));
        }
    
        void disableSelectionStyle(Graphics graphics) {
            graphics.setColor(color);
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setStroke(new BasicStroke());
        }
    
    
        @Override
        public void paint(Graphics graphics) {
            if (isSelected()) {
                enableSelectionStyle(graphics);
            }
            else {
                disableSelectionStyle(graphics);
            }
    
            // ...
        }
    }


    public class Dot extends BaseShape {
        private final int DOT_SIZE = 3;
    
        public Dot(int x, int y, Color color) {
            super(x, y, color);
        }
    
        @Override
        public int getWidth() {
            return DOT_SIZE;
        }
    
        @Override
        public int getHeight() {
            return DOT_SIZE;
        }
    
        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            graphics.fillRect(x - 1, y - 1, getWidth(), getHeight());
        }
    }

    public class Rectangle extends BaseShape {
        public int width;
        public int height;
    
        public Rectangle(int x, int y, int width, int height, Color color) {
            super(x, y, color);
            this.width = width;
            this.height = height;
        }
    
        @Override
        public int getWidth() {
            return width;
        }
    
        @Override
        public int getHeight() {
            return height;
        }
    
        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            graphics.drawRect(x, y, getWidth() - 1, getHeight() - 1);
        }
    }

    public class CompoundShape extends BaseShape {
    protected List<Shape> children = new ArrayList<>();

    public CompoundShape(Shape... components) {
        super(0, 0, Color.BLACK);
        add(components);
    }

    public void add(Shape component) {
        children.add(component);
    }

    public void add(Shape... components) {
        children.addAll(Arrays.asList(components));
    }

    public void remove(Shape child) {
        children.remove(child);
    }

    public void remove(Shape... components) {
        children.removeAll(Arrays.asList(components));
    }

    public void clear() {
        children.clear();
    }

    @Override
    public int getX() {
        if (children.size() == 0) {
            return 0;
        }
        int x = children.get(0).getX();
        for (Shape child : children) {
            if (child.getX() < x) {
                x = child.getX();
            }
        }
        return x;
    }

    @Override
    public int getY() {
        if (children.size() == 0) {
            return 0;
        }
        int y = children.get(0).getY();
        for (Shape child : children) {
            if (child.getY() < y) {
                y = child.getY();
            }
        }
        return y;
    }

    @Override
    public int getWidth() {
        int maxWidth = 0;
        int x = getX();
        for (Shape child : children) {
            int childsRelativeX = child.getX() - x;
            int childWidth = childsRelativeX + child.getWidth();
            if (childWidth > maxWidth) {
                maxWidth = childWidth;
            }
        }
        return maxWidth;
    }

    @Override
    public int getHeight() {
        int maxHeight = 0;
        int y = getY();
        for (Shape child : children) {
            int childsRelativeY = child.getY() - y;
            int childHeight = childsRelativeY + child.getHeight();
            if (childHeight > maxHeight) {
                maxHeight = childHeight;
            }
        }
        return maxHeight;
    }

    @Override
    public void move(int x, int y) {
        for (Shape child : children) {
            child.move(x, y);
        }
    }

    @Override
    public boolean isInsideBounds(int x, int y) {
        for (Shape child : children) {
            if (child.isInsideBounds(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unSelect() {
        super.unSelect();
        for (Shape child : children) {
            child.unSelect();
        }
    }

    public boolean selectChildAt(int x, int y) {
        for (Shape child : children) {
            if (child.isInsideBounds(x, y)) {
                child.select();
                return true;
            }
        }
        return false;
    }

    @Override
    public void paint(Graphics graphics) {
        if (isSelected()) {
            enableSelectionStyle(graphics);
            graphics.drawRect(getX() - 1, getY() - 1, getWidth() + 1, getHeight() + 1);
            disableSelectionStyle(graphics);
        }

        for (Shape child : children) {
            child.paint(graphics);
        }
        }
    }


public class ImageEditor {
    private EditorCanvas canvas;
    private CompoundShape allShapes = new CompoundShape();

    public ImageEditor() {
        canvas = new EditorCanvas();
    }

    public void loadShapes(Shape... shapes) {
        allShapes.clear();
        allShapes.add(shapes);
        canvas.refresh();
    }

    private class EditorCanvas extends Canvas {
        JFrame frame;

        private static final int PADDING = 10;

        EditorCanvas() {
            createFrame();
            refresh();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    allShapes.unSelect();
                    allShapes.selectChildAt(e.getX(), e.getY());
                    e.getComponent().repaint();
                }
            });
        }

        void createFrame() {
            frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel contentPanel = new JPanel();
            Border padding = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
            contentPanel.setBorder(padding);
            frame.setContentPane(contentPanel);

            frame.add(this);
            frame.setVisible(true);
            frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        }

        public int getWidth() {
            return allShapes.getX() + allShapes.getWidth() + PADDING;
        }

        public int getHeight() {
            return allShapes.getY() + allShapes.getHeight() + PADDING;
        }

        void refresh() {
            this.setSize(getWidth(), getHeight());
            frame.pack();
        }

        public void paint(Graphics graphics) {
            allShapes.paint(graphics);
        }
        }
    }

}

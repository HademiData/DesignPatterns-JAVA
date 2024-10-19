
import java.awt.*;


import java.util.HashMap;
import java.util.Map;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Flywheel {
    /*
     * Flyweight is a structural design pattern that allows programs to 
     * support vast quantities of objects by keeping their memory consumption low.

    The pattern achieves it by sharing parts of object state between
     multiple objects. In other words, the Flyweight saves RAM by 
     caching the same data used by different objects.
     */

     /*
      * 
      Rendering a forest
In this example, we’re going to render a forest (1.000.000 trees)! Each tree will be 
represented by its own object that has some state (coordinates, texture and so on). 
Although the program does its primary job, naturally, it consumes a lot of RAM.

The reason is simple: too many tree objects contain duplicate data (name, texture, color). 
That’s why we can apply the Flyweight pattern and store these values inside separate flyweight 
objects (the TreeType class). Now, instead of storing the same data in thousands of 

Tree objects, we’re going to reference one of the flyweight objects with a particular set of values.

The client code isn’t going to notice anything since the complexity of reusing
 flyweight objects is buried inside a flyweight factory.
      */


      public class Tree {
        private int x;
        private int y;
        private TreeType type;
    
        public Tree(int x, int y, TreeType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    
        public void draw(Graphics g) {
            type.draw(g, x, y);
        }
    }


    public class TreeType {
        private String name;
        private Color color;
        private String otherTreeData;
    
        public TreeType(String name, Color color, String otherTreeData) {
            this.name = name;
            this.color = color;
            this.otherTreeData = otherTreeData;
        }
    
        public void draw(Graphics g, int x, int y) {
            g.setColor(Color.BLACK);
            g.fillRect(x - 1, y, 3, 5);
            g.setColor(color);
            g.fillOval(x - 5, y - 10, 10, 10);
        }
    }

    public class TreeFactory {
    static Map<String, TreeType> treeTypes = new HashMap<>();

    public static TreeType getTreeType(String name, Color color, String otherTreeData) {
        Flywheel flywheel = new Flywheel();
        TreeType result = treeTypes.get(name);
        if (result == null) {
            result = flywheel.new TreeType(name, color, otherTreeData);
            treeTypes.put(name, result);
        }
        return result;
        }
    }

    public class Forest extends JFrame {
        private List<Tree> trees = new ArrayList<>();
    
        public void plantTree(int x, int y, String name, Color color, String otherTreeData) {
            TreeType type = TreeFactory.getTreeType(name, color, otherTreeData);
            Tree tree = new Tree(x, y, type);
            trees.add(tree);
        }
    
        @Override
        public void paint(Graphics graphics) {
            for (Tree tree : trees) {
                tree.draw(graphics);
            }
        }
    }



    public class Demo {
        static int CANVAS_SIZE = 500;
        static int TREES_TO_DRAW = 1000000;
        static int TREE_TYPES = 2;

        public static void main(String[] args) {
            Flywheel flywheel = new Flywheel();

            Forest forest = flywheel.new Forest();
            for (int i = 0; i < Math.floor(TREES_TO_DRAW / TREE_TYPES); i++) {
                forest.plantTree(random(0, CANVAS_SIZE), random(0, CANVAS_SIZE),
                        "Summer Oak", Color.GREEN, "Oak texture stub");
                forest.plantTree(random(0, CANVAS_SIZE), random(0, CANVAS_SIZE),
                        "Autumn Oak", Color.ORANGE, "Autumn Oak texture stub");
            }
            forest.setSize(CANVAS_SIZE, CANVAS_SIZE);
            forest.setVisible(true);

            System.out.println(TREES_TO_DRAW + " trees drawn");
            System.out.println("---------------------");
            System.out.println("Memory usage:");
            System.out.println("Tree size (8 bytes) * " + TREES_TO_DRAW);
            System.out.println("+ TreeTypes size (~30 bytes) * " + TREE_TYPES + "");
            System.out.println("---------------------");
            System.out.println("Total: " + ((TREES_TO_DRAW * 8 + TREE_TYPES * 30) / 1024 / 1024) +
                    "MB (instead of " + ((TREES_TO_DRAW * 38) / 1024 / 1024) + "MB)");
        }

        private static int random(int min, int max) {
            return min + (int) (Math.random() * ((max - min) + 1));
        }
    }




}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchgraf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;


public class SearchGraf extends Application {

    private static final double arrowLength = 10;
    private static final double arrowWidth = 3;
    private Node start = null;
    private Node end = null;
    private List<String> lines = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    //private final String filepath = "/Users/Boubik/Dropbox/programovani/JavaFX/SearchGraf/Graf.Boubik";
    private final String filepath = "grafs/Graf.Boubik";
    int i = 0;
    int genrnd = 0;

    @Override
    public void start(Stage stage) {

        if (genrnd == 1) {
            int countNodes = (int)(Math.random() * 8 + 2);
            //int countNodes = 2;

            int i = 0;
            Node a = null;
            while (i != countNodes) {
                //min xy = 10 max x = 590 y = 290
                int x = (int) (Math.random() * 580 + 10);
                int y = (int) (Math.random() * 280 + 10);
                a = new Node(x, y);
                a.setName((char) (65 + i) + "");
                nodes.add(a);
                if (start == null) {
                    start = a;
                } else {
                    if (end == null) {
                        end = a;
                    }
                }
                i++;
            }

            i = 0;
            while (i < nodes.size()) {
                int makeConn = (int) (Math.random() * 100);
                
                if(makeConn < 90){
                    int rank = (int) (Math.random() * 20 + 1);
                    Node b = getRndNode(nodes.get(i));
                    if (isInConn(nodes.get(i), b) == 0) {
                        Connection c = new Connection(nodes.get(i), b, rank);
                        a.addConnection(c);
                        nodes.get(i).addConnection(c);
                    }
                }
                i++;
            }
        }

        if (genrnd == 0) {
            try {
                File file = new File(filepath);    //creates a new file instance  
                FileReader fr = new FileReader(file);   //reads the file  
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
                StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters  
                String line;
                String[] curXY;
                Node a;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                    String[] values = line.split(";");
                    curXY = values[0].split(",");
                    
                    a = new Node(Integer.parseInt(curXY[0]), Integer.parseInt(curXY[1]));
                    a.setName(curXY[2]);
                    nodes.add(a);

                    if (start == null) {
                        start = a;
                    } else {
                        if (end == null) {
                            end = a;
                        }
                    }

                    sb.append(line);      //appends line to string buffer  
                    sb.append("\n");     //line feed   
                }
                fr.close();    //closes the stream and release the resources  
                System.out.println("Contents of File: ");
                System.out.println(sb.toString());   //returns a string that textually represents the object  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        int i = 0;
        while(i < lines.size()){
            String line = lines.get(i);
            String[] values = line.split(";");
            String[] xyname = values[0].split(",");
            Node a = getNodeByName(xyname[2]);
            Node b;
            Connection c;
            int rank;
            
            int k = 1;
            while(k < values.length){
                String[] namerank = values[k].split(",");
                b = getNodeByName(namerank[0]);
                rank = Integer.parseInt(namerank[1]);
                c = new Connection(a, b, rank);
                a.addConnection(c);
                k++;
            }
            i++;
        }

        // Creating a Path
        Path path = new Path();
        int point = 4;

        System.out.println("Nodes");
        nodes.forEach((item) -> {
            System.out.println("X: " + item.getX() + " Y: " + item.getY());

        });

        Group root = new Group(path);
        Graf graf = new Graf(start, end, nodes);
        String str_path = graf.getShortestPath();
        List<String> conns = new ArrayList<>();
        
       
        String regex = " [A-Z]\\[";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str_path);
        while (matcher.find())
        {
            conns.add(matcher.group().substring(1, 2));
        }
        Collections.reverse(conns);
        
        
        nodes.forEach(new Consumer<Node>() {
            @Override
            public void accept(Node item) {
                Circle circle = new Circle(item.getX(), item.getY(), point);
                
                Text a = new Text();
                a.setText(item.getName());
                a.setX(item.getX() + 3);
                a.setY(item.getY() - 3);
                
                if(start == item || end == item){
                    circle.setFill(javafx.scene.paint.Color.RED);
                    a.setFill(javafx.scene.paint.Color.RED);
                }
                root.getChildren().add(circle);
                root.getChildren().add(a);
                
                connections = item.GetConnection();
                List<Connection> visitedCon = new ArrayList<>();
                connections.forEach((Connection itemc) -> {
                    if (!visitedCon.contains(itemc)) {
                        double d = Math.sqrt((itemc.getAX() - itemc.getBX()) * (itemc.getAX() - itemc.getBX()) + (itemc.getAY() - itemc.getBY()) * (itemc.getAY() - itemc.getBY())) / 2;
                        Double D = Math.sqrt((itemc.getBX() - itemc.getAX()) * (itemc.getBX() - itemc.getAX()) + (itemc.getBY() - itemc.getAY()) * (itemc.getBY() - itemc.getAY()));
                        int x3 = (int) ((itemc.getAX() + (d / D) * (itemc.getBX() - itemc.getAX())));
                        int y3 = (int) ((itemc.getAY() + (d / D) * (itemc.getBY() - itemc.getAY())) - 5);
                        Text text = new Text();
                        text.setText(itemc.getRank() + "");
                        text.setX(x3);
                        text.setY(y3);
                        root.getChildren().add(text);
                    }
                    Line line = new Line(itemc.getAX(), itemc.getAY(), itemc.getBX(), itemc.getBY());
                    Line arrow1 = new Line();
                    Line arrow2 = new Line();
                    double ex = itemc.getAX();
                    double ey = itemc.getAY();
                    double sx = itemc.getBX();
                    double sy = itemc.getBY();
                    
                    arrow1.setEndX(ex);
                    arrow1.setEndY(ey);
                    arrow2.setEndX(ex);
                    arrow2.setEndY(ey);
                    if (ex == sx && ey == sy) {
                        // arrow parts of length 0
                        arrow1.setStartX(ex);
                        arrow1.setStartY(ey);
                        arrow2.setStartX(ex);
                        arrow2.setStartY(ey);
                    } else {
                        double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                        double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);
                        // part in direction of main line
                        double dx = (sx - ex) * factor;
                        double dy = (sy - ey) * factor;
                        // part ortogonal to main line
                        double ox = (sx - ex) * factorO;
                        double oy = (sy - ey) * factorO;
                        arrow1.setStartX(ex + dx - oy);
                        arrow1.setStartY(ey + dy + ox);
                        arrow2.setStartX(ex + dx + oy);
                        arrow2.setStartY(ey + dy - ox);
                    }
                    //if(conns.contains(itemc.getAName()) && conns.contains(itemc.getBName())){
                    if(conns.contains(itemc.getAName()) && conns.contains(itemc.getBName())){
                        line.setStroke(Color.RED);
                        try {
                            int i = conns.indexOf(itemc.getBName());
                            String now = itemc.getAName();
                            String next = itemc.getBName();
                            String now2 = conns.get(--i);
                            String next2 = conns.get(i);
                            if(next2.equals(next) || now2.equals(now)){
                                arrow1.setStroke(Color.RED);
                                arrow2.setStroke(Color.RED);
                            }
                        } catch ( IndexOutOfBoundsException e ) {
                        }
                    }
                    visitedCon.add(itemc);
                    root.getChildren().add(line);
                    root.getChildren().add(arrow1);
                    root.getChildren().add(arrow2);
                });
            }
        });

        // Creating a scene object
        Scene scene = new Scene(root, 600, 300);

        // Setting title to the Stage
        stage.setTitle("Graf");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
        
        System.out.println("\n\n" + str_path);
    }

    public static void main(String args[]) {
        launch(args);
    }

    public Node findSameNode(String[] curXY) {
        int i = 0;
        while (i < nodes.size()) {
            Node item = nodes.get(i);
            if (item.getX() == Integer.parseInt(curXY[0])) {
                if (item.getY() == Integer.parseInt(curXY[1])) {
                    return item;
                }
            }
            i++;
        }
        return null;
    }

    public Connection findSameConnection(Node a, Node b, int rank) {
        int i = 0;
        while (i < connections.size()) {
            Connection item = connections.get(i);
            if (item.getA() == a) {
                if (item.getB() == b) {
                    if (item.getRank() == rank) {
                        return item;
                    }
                }
            }
            i++;
        }
        return null;
    }

    public Node getRndNode(Node expectNode) {

        Node a = expectNode;
        while(a == expectNode){
            int node = (int) (Math.random() * nodes.size());
            a = nodes.get(node);
        }
        return a;
        
        
        /*List<Node> rn = shufleList(nodes);
        int i = 0;
        while (i < rn.size()) {
            if(rn.get(i) != expectNode){
                return rn.get(i);
            }
            i++;
        }

        return null;*/
    }

    public int isInConn(Node a, Node b) {
        int i = 0;
        List<Connection> con = a.GetConnection();
        while (i < con.size()) {
            if (con.get(i).getA() == a || con.get(i).getA() == b || con.get(i).getB() == a || con.get(i).getB() == b) {
                return 1;
            }
            i++;
        }
        i = 0;
        con = b.GetConnection();
        while (i < con.size()) {
            if (con.get(i).getA() == a || con.get(i).getA() == b || con.get(i).getB() == a || con.get(i).getB() == b) {
                return 1;
            }
            i++;
        }
        return 0;
    }

    public List<Node> shufleList(List<Node> nodes) {
        long SEED = System.currentTimeMillis();
        Random RANDOM = new Random(SEED);
        Node[] ar = (Node[]) nodes.toArray();
        for (int i = 0; i < ar.length; i++) {
            int swap = RANDOM.nextInt(i + 1);
            Node temp = ar[swap];
            ar[swap] = ar[i];
            ar[i] = temp;
        }
        List<Node> idk = Arrays.asList(ar);

        return idk;
    }

    public Node getNodeByName(String name) {
        
        int i = 0;
        while(i < nodes.size()){
            Node node = nodes.get(i);
            if(node.getName().equals(name)){
                return nodes.get(i);
            }
            i++;
        }

        return null;
    }
}

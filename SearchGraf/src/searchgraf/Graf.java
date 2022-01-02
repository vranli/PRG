/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchgraf;

import java.util.ArrayList;
import java.util.List;


public class Graf {

    private Node start;
    private Node end;
    private List<Node> nodes;

    public Graf(Node start, Node end, List<Node> list) {
        setStart(start);
        setEnd(end);
        setList(list);
    }

    public void setList(List<Node> list) {
        this.nodes = new ArrayList<>(list);
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public int getStartX() {
        return start.getX();
    }

    public int getStartY() {
        return start.getY();
    }

    public int getEndX() {
        return end.getX();
    }

    public int getEendY() {
        return end.getY();
    }
    public String getShortestPath(Node start, Node end) {
        setStart(start);
        setEnd(end);
        return getShortestPath();
    }

    public String getShortestPath() {
        //Dijkstra's algorithm
        List<Node> unvisited = new ArrayList<>(nodes);
        List<Node> visited = new ArrayList<>();
        List<String> table = new ArrayList<>();
        Node currentNode = new Node();
        Node subCurrentNode = new Node();
        List<Connection> currentConnections = new ArrayList<>();
        nodes.forEach((item) -> {
            if (start == item) {
                table.add(item.getX() + "," + item.getY() + ";0" + ";");
            } else {
                table.add(item.getX() + "," + item.getY() + ";9999999" + ";");
            }
        });
        System.out.println("\nSide table");
        for (String item : table) {
            System.out.println(item);
        }

        int k = 0;
        while (unvisited.size() != 1) {
            int startRank = 0;
            int lovest = -1;
            int i = 0;
            while (i < table.size()) {
                String[] values = table.get(i).split(";");
                subCurrentNode = getNode(values[0]);
                if ((lovest == -1 || Integer.parseInt(values[1]) < lovest) && unvisited.contains(subCurrentNode)) {
                    lovest = Integer.parseInt(values[1]);
                    currentNode = subCurrentNode;
                    currentConnections = currentNode.GetConnection();
                    startRank = Integer.parseInt(values[1]);
                }
                i++;
            }

            Node a = new Node();
            Node b = new Node();
            for (Connection item : currentConnections) {
                int rank = startRank + item.getRank();
                if (currentNode == item.getA()) {
                    a = item.getA();
                    b = item.getB();
                } else {
                    a = item.getB();
                    b = item.getA();
                }

                i = 0;
                while (i < table.size()) {
                    String[] values = table.get(i).split(";");
                    subCurrentNode = getNode(values[0]);
                    if (b == subCurrentNode && (rank < Integer.parseInt(values[1]) || Integer.parseInt(values[1]) == -1)) {
                        table.set(i, values[0] + ";" + rank + ";" + getNodePositionInTable(table, currentNode));
                        break;
                    }
                    i++;
                }
            }
            visited.add(a);
            unvisited.remove(a);
            k++;
        }
        System.out.println("\nSide table");
        for (String item : table) {
            System.out.println(item);
        }

        String path = end.getName() + "[" + end.getX() + "," + end.getY() + "]";
        String zacatek = "";
        Node curr = end;
        int i = 0;
        while (curr != start) {
            for (String item : table) {
                String[] snode = item.split(";");
                Node susCurr = getNode(snode[0]);
                if (susCurr == end) {
                    zacatek = "Délka: " + snode[1] + "\nCesta: ";
                }
                if (curr == susCurr) {
                    String[] next = table.get(Integer.parseInt(snode[2])).split(";");
                    curr = getNode(next[0]);
                    path = curr.getName() + "["+curr.getX() + "," + curr.getY()+"]" + " -> " + path;
                    break;
                }
            }
            i++;
        }

        return zacatek + path;
    }

    public Node getNode(String value) {
        String[] snode = value.split(",");

        for (Node item : nodes) {
            if (item.getX() == Integer.parseInt(snode[0]) && item.getY() == Integer.parseInt(snode[1])) {
                return item;
            }
        }

        return null;
    }

    public int getNodePositionInTable(List<String> table, Node currNode) {
        for (String item : table) {
            String[] values = item.split(";");
            String[] snode = values[0].split(",");
            if (currNode.getX() == Integer.parseInt(snode[0]) && currNode.getY() == Integer.parseInt(snode[1])) {
                return table.indexOf(item);
            }
        }
        return -1;
    }

}

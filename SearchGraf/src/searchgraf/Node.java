/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchgraf;

import java.util.ArrayList;
import java.util.List;


public class Node {
    
    private int x;
    private int y;
    private String name;
    private List<Connection> connections = new ArrayList<>();
    
    public Node(){
    }
    
    public Node(int x, int y){
        setX(x);
        setY(y);
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void addConnection(Connection connection){
        this.connections.add(connection);
    }
    
    public List<Connection> GetConnection(){
        return this.connections;
    }
}

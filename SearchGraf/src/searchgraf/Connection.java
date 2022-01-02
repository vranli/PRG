/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchgraf;


public class Connection {
    
    private Node a;
    private Node b;
    private int rank;
    
    public Connection(Node a, Node b, int rank){
        setA(a);
        setB(b);
        setRank(rank);
    }
    
    public void setA(Node a){
        this.a = a;
    }
    
    public void setB(Node b){
        this.b = b;
    }
    
    public void setRank(int rank){
        this.rank = rank;
    }
    
    public Node getA(){
        return a;
    }
    
    public Node getB(){
        return b;
    }
    
    public int getAX(){
        return a.getX();
    }
    
    public int getAY(){
        return a.getY();
    }
    
    public int getBX(){
        return b.getX();
    }
    
    public int getBY(){
        return b.getY();
    }
    
    public String getAName(){
        return a.getName();
    }
    
    public String getBName(){
        return b.getName();
    }
    
    public int getRank(){
        return rank;
    }
}

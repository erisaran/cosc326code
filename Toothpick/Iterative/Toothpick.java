/** Author - Benjaman Dutton
 * Stores the values of a toothpick and provides a method for drawing it to a canvas
 * Java version JDK 6.0_65
  */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Toothpick {
  
  private float x1;
  private float y1;
  private float x2;
  private float y2;
  private float size; // the size of the object in relation to the screen
  private int generation; //keeps track of what generation it is in
  private Toothpick left;
  private Toothpick right;
  private float ratio; // the ratio of how big this object is compared to its parent
  
  // starting toothpick
  public Toothpick(float scale, float r, int screen){
    float scr = new Integer(screen).floatValue();
    this.x1 = scr/2 - (0.5f*scale);
    this.y1 = scr/2;
    this.x2 = scr/2 + (0.5f*scale);
    this.y2 = scr/2;
    this.size = 1.0f * scale;
    this.generation = 0;
    this.ratio = r;
  }
  
  private Toothpick(float x, float y, float r, float s, boolean h,int parentGen){
    if (!h){
      this.x1 = x + (-0.5f * r * s);
      this.y1 = y;
      this.x2 = x + (0.5f * r * s);
      this.y2 = y;
    }else{
      this.x1 = x;
      this.y1 = y + (-0.5f * r * s);
      this.x2 = x;
      this.y2 = y + (0.5f * r * s);
    }
    this.size = s*r;
    this.generation = parentGen + 1;
  }
  
  public float getX(int i){
    return i == 0 ? x1: x2;
  }
  
  public float getY(int i){
    return i == 0 ? y1 : y2;
  }
  
  public float getSize(){
    return this.size;
  }
  
  public boolean getHor(){
    return y1 == y2 ? true : false;
  }
  
  public void display(Graphics g){
    g.setColor(Color.black);
    g.drawLine(new Float(x1).intValue(),new Float(y1).intValue(),new Float(x2).intValue(),new Float(y2).intValue());
  }
  
  public void makeNextGen(){
    this.left = new Toothpick(x1,y1,ratio,size,this.getHor(),generation);
    this.right = new Toothpick(x2,y2,ratio,size,this.getHor(),generation);
  }
  
  public Toothpick getLeft(){
    return this.left;
  }
  
  public Toothpick getRight(){
    return this.right;
  }
  
  public int getGen(){
    return this.generation;
  }
  
  public Toothpick getNode(int node, Graphics g){
    String path = Integer.toBinaryString(new Integer(node));
    int pathLength = path.length();
    Toothpick p = this;
    for (int i = 0; i < pathLength-1; i++){
      int position = pathLength - i;
      if (path.charAt(position -1) == '0') p = new Toothpick(p.getX(0),p.getY(0)
                                                             ,ratio,p.getSize(),p.getHor(),p.getGen());
      else p = new Toothpick(p.getX(1),p.getY(1),ratio,p.getSize(),p.getHor(),p.getGen());
    }
    p.display(g);
    return p;
    }
}
    
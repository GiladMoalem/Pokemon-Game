package gameClient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//for me!!!
public class MyFrame2 extends JFrame{
    private List<Point> points;

    public MyFrame2(){
        super();
        initFrame();
        initPanel();
    }
    public void update(List<Point> points){
        this.points = points;
//        updateFrame();
    }

    private void initFrame(){
        this.setSize(1000,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void initPanel(){
//        MyPanel panel = new MyPanel();
//        panel.setBackground(Color.gray);
//        this.add(panel);
    }
    public void paint(Graphics g) {
        super.paintComponents(g);
//        g.fillOval(23,100,45,20);
        for(Point p: points){
            g.fillOval((int)p.getX()-10,(int)p.getY()-10,20,20);
        }

    }


    }

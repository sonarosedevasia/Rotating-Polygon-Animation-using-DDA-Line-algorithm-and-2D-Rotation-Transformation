import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RotatingPolygonDDA extends JPanel implements ActionListener, KeyListener {

    int sides = 5;
    double angle = 0;
    Timer timer;

    int centerX = 300;
    int centerY = 250;
    int radius = 120;

    float hue = 0;
    String polygonName = "Pentagon";

    public RotatingPolygonDDA() {

        setBackground(Color.black);
        timer = new Timer(40,this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    // DDA Line Algorithm
    public void drawLineDDA(Graphics g,int x1,int y1,int x2,int y2){

        int dx = x2-x1;
        int dy = y2-y1;

        int steps = Math.max(Math.abs(dx),Math.abs(dy));

        float xinc = dx/(float)steps;
        float yinc = dy/(float)steps;

        float x = x1;
        float y = y1;

        for(int i=0;i<=steps;i++){
            g.fillRect(Math.round(x),Math.round(y),3,3);
            x += xinc;
            y += yinc;
        }
    }

    // Generate polygon
    public int[][] generatePolygon(){

        int[][] points = new int[sides][2];

        for(int i=0;i<sides;i++){

            double theta = 2*Math.PI*i/sides + angle;

            int x = (int)(centerX + radius*Math.cos(theta));
            int y = (int)(centerY + radius*Math.sin(theta));

            points[i][0] = x;
            points[i][1] = y;
        }

        return points;
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        int[][] p = generatePolygon();

        int[] xs = new int[p.length];
        int[] ys = new int[p.length];

        for(int i=0;i<p.length;i++){
            xs[i] = p[i][0];
            ys[i] = p[i][1];
        }

        // Changing color
        Color fillColor = Color.getHSBColor(hue,1f,1f);
        g.setColor(fillColor);

        g.fillPolygon(xs,ys,p.length);

        g.setColor(Color.white);

        for(int i=0;i<p.length;i++){

            int x1 = p[i][0];
            int y1 = p[i][1];

            int x2 = p[(i+1)%p.length][0];
            int y2 = p[(i+1)%p.length][1];

            drawLineDDA(g,x1,y1,x2,y2);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD,18));

        g.drawString("Polygon: "+polygonName,20,30);
        g.drawString("Press 1:Triangle 2:Square 3:Pentagon 4:Hexagon",20,60);
    }

    public void actionPerformed(ActionEvent e){

        angle += 0.03;

        hue += 0.01;
        if(hue>1) hue = 0;

        repaint();
    }

    public void keyPressed(KeyEvent e){

        if(e.getKeyChar()=='1'){
            sides = 3;
            polygonName = "Triangle";
        }

        if(e.getKeyChar()=='2'){
            sides = 4;
            polygonName = "Square";
        }

        if(e.getKeyChar()=='3'){
            sides = 5;
            polygonName = "Pentagon";
        }

        if(e.getKeyChar()=='4'){
            sides = 6;
            polygonName = "Hexagon";
        }

        repaint();
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    public static void main(String[] args){

        JFrame frame = new JFrame("Rotating Polygon using DDA Algorithm");

        RotatingPolygonDDA panel = new RotatingPolygonDDA();

        frame.add(panel);
        frame.setSize(600,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
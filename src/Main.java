import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Turtle Logo");
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left panel for Canvas
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(800, 800)); // Set preferred size for Canvas
        canvas.setBorder(lineBorder);
        canvas.setBackground(Color.WHITE); // Optional: Set background color
        canvas.setFocusable(true);

        //canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: Add border
        mainPanel.add(canvas, BorderLayout.CENTER);

        // Text Editor (or JTextArea) below the Button Bar
        //JTextArea textArea = new JTextArea();
        JTextField textArea = new JTextField();
        textArea.setForeground(Color.gray);
        textArea.setSize(800,100);
        textArea.setMargin(new Insets(3,3,10,10));
        Font font = new Font("Fira Code",Font.PLAIN,18);
        textArea.setFont(font);
        textArea.setText("Your code here");

        //add focus listener
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(textArea.getText().equals("Your code here")){
                    textArea.setForeground(Color.BLACK);
                    textArea.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText("Your code here");
                }
            }
        });
        mainPanel.add(textArea,BorderLayout.SOUTH);

        frame.add(mainPanel);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String code = textArea.getText().toLowerCase();
                    textArea.setText("");
                    evaluate(code);
                }
            }
            private void evaluate(String code) {
                String[] Tokens = code.split("\\s+");
                for(int i = 0 ; i < Tokens.length ; i++){
                    String command = Tokens[i];
                    switch(command){
                        case "rp":
                        case "repeat": //rp 6 [ rp 3 [ fd 50 rt 120 ] rt 60 ] -- no free spaces here
                            int times = Integer.parseInt(Tokens[++i]);
                            i++; // go to its [

                            int bracket = 1;
                            StringBuilder block = new StringBuilder();
                            while(bracket != 0) {
                                i++;
                                if (Tokens[i].equals("[")) {
                                    block.append(Tokens[i]).append(" ");
                                    bracket++;
                                }
                                else if (Tokens[i].equals("]")) {
                                    if(bracket != 1){ // only add ] if brackets != 1 meaning its not the last ]
                                        block.append(Tokens[i]).append(" ");
                                    }
                                    bracket--;
                                }else{
                                    block.append(Tokens[i]).append(" ");
                                }
                            }
                            System.out.println(times);
                            System.out.println(block.toString());
                            for(int j = 0 ; j < times ; j++){
                                evaluate(block.toString());
                            }
                            break;
                        case "fd":
                        case "forward":
                            canvas.forward(Integer.parseInt(Tokens[++i]));
                            break;
                        case "bk":
                        case "backward":
                            canvas.backward(Integer.parseInt(Tokens[++i]));
                            break;
                        case "rt":
                        case "right":
                            canvas.rotate(-Integer.parseInt(Tokens[++i]));
                            break;
                        case "lt":
                        case "left":
                            canvas.rotate(Integer.parseInt(Tokens[++i]));
                            break;
                        case "pu":
                        case "penup":
                            canvas.setPenUp();
                            break;
                        case "pd":
                        case "pendown":
                            canvas.setPenDown();
                            break;
                        case "clear":
                        case "cs":
                            canvas.clear();
                            break;
                        case "bye":
                            System.exit(0);
                            break;
                        case "st":
                        case "showturtle":
                            canvas.showturtle();
                            break;
                        case "ht":
                        case "hideturtle":
                            canvas.hideturtle();
                            break;
                        case "setpencolor":
                            canvas.setpencolor(Integer.parseInt(Tokens[++i]));
                           break;
                        case "setscreencolor":
                            canvas.setscreencolor(Integer.parseInt(Tokens[++i]));
                            break;
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}
class Canvas extends JPanel{



    private static class Line{
        int x1;
        int y1;
        int x2;
        int y2;
        public Line(int x1 , int y1 , int x2 , int y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private int new_x = 400;
    private int new_y = 400;
    private int old_x = new_x;
    private int old_y = new_y;
    private int angle = 0;
    private boolean PenDown = true;
    private boolean hiddenTurtle = false;
    private final java.util.List<Line> Lines = new ArrayList<>();
    private LogoColors lc = new LogoColors();
    private Color canvasColor = lc.getColor(7);
    private Color penColor = lc.getColor(0);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(canvasColor);
        if(old_x != new_x || old_y != new_y){
            for(Line l : Lines) {
                g.setColor(penColor);
                g.drawLine(l.x1 , l.y1 , l.x2 , l.y2);
            }
        }
        if(!hiddenTurtle){
            g.setColor(Color.blue);
            g.fillOval(new_x - 5,new_y - 5,10,10);
        }
    }

    public void forward(int steps){
        old_x = new_x;
        old_y = new_y;
        new_x += (int) (steps * Math.cos(Math.toRadians(angle)));
        new_y += -(int) (steps * Math.sin(Math.toRadians(angle)));
        if(PenDown){
            Lines.add(new Line(old_x , old_y , new_x , new_y));
        }
        repaint();
    }
    public void backward(int steps){
        old_x = new_x;
        old_y = new_y;
        new_x -= (int) (steps * Math.cos(Math.toRadians(angle)));
        new_y -= - (int) (steps * Math.sin(Math.toRadians(angle)));
        System.out.println(old_x + " " + old_y + " " + new_x + " " + new_y);
        if(PenDown){
            Lines.add(new Line(old_x , old_y , new_x , new_y));
        }
        repaint();
    }
    public void rotate(int angle){ //rotate
        this.angle += angle;
    }

    public void clear() {
        Lines.clear();
        new_x = 400 ;
        new_y = 400;
        old_x = new_x;
        old_y = new_y;
        angle=0;
        setPenDown();
        repaint();
    }
    public void setPenDown(){
        PenDown = true;
    }
    public void setPenUp(){
        PenDown = false;
    }
    public void showturtle() {
        hiddenTurtle = false;
        repaint();
    }
    public void hideturtle(){
        hiddenTurtle = true;
        repaint();
    }
    public void setpencolor(int index) {
        penColor = lc.getColor(index);
    }
    public void setscreencolor(int index) {
        canvasColor = lc.getColor(index);
        repaint();
    }
}
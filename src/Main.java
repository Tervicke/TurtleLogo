import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

        //canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: Add border
        mainPanel.add(canvas, BorderLayout.WEST);

        // Right panel for Button Bar and Text Editor
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Button Bar panel at the top
        JPanel buttonBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton resetButton = new JButton("Reset");
        JButton runButton = new JButton("Run");
        buttonBarPanel.add(resetButton);
        buttonBarPanel.add(runButton);
        rightPanel.add(buttonBarPanel, BorderLayout.NORTH);

        // Text Editor (or JTextArea) below the Button Bar
        JTextArea textArea = new JTextArea();
        textArea.setForeground(Color.gray);
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
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.clear();
            }
        });
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = textArea.getText();
                evaluate(code);
            }

            private void evaluate(String code) {
                String[] Tokens = code.split("\\s+");
                for(int i = 0 ; i < Tokens.length ; i++){
                    String command = Tokens[i];
                    System.out.println(command);
                    switch(command){
                        case "rp":
                        case "repeat":
                            int times = Integer.parseInt(Tokens[++i]);
                            String blockTokens = "";
                            i+=2; // go forward to the '['
                            while(!Tokens[i].equals("]")){
                                blockTokens += (Tokens[i]);
                                blockTokens += " ";
                                i++;
                            }
                            for(int j = 0 ; j < times ; j++){
                                evaluate(blockTokens);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(old_x != new_x || old_y != new_y){
            for(Line l : Lines) {
                g.setColor(Color.black);
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
}
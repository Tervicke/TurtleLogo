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
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Canvas extends JPanel {
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
    private java.util.List<String>  enteredCommands = new ArrayList<>();
    private int enteredCommandIndex = 0;
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
    public void newCommandEntered(String command){
        enteredCommands.add(command);
        enteredCommandIndex++;
        for(String c : enteredCommands){
            System.out.println(c);
        }
    }
    public String getPreviousCommand(){
        enteredCommandIndex--;
        if(enteredCommandIndex < 0){
            enteredCommandIndex = enteredCommands.size() - 1;
        }
        return enteredCommands.get(enteredCommandIndex);
    }
    public String getNextCommand(){
        enteredCommandIndex++;
        if(enteredCommandIndex >= enteredCommands.size()){
            enteredCommandIndex = 0;
        }
        return enteredCommands.get(enteredCommandIndex);
    }
}

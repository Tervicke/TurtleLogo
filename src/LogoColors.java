import java.awt.*;
public class LogoColors {
    private Color[] colors = {
            new Color(0, 0, 0),       // black
            new Color(0, 0, 255),     // blue
            new Color(0, 255, 0),     // green
            new Color(0, 255, 255),   // cyan (light blue)
            new Color(255, 0, 0),     // red
            new Color(255, 0, 255),   // magenta (reddish purple)
            new Color(255, 255, 0),   // yellow
            new Color(255, 255, 255), // white
            new Color(155, 96, 59),   // brown
            new Color(197, 136, 18),  // light brown
            new Color(100, 162, 64),  // dark green
            new Color(120, 187, 187), // darkish blue
            new Color(255, 149, 119), // tan
            new Color(144, 113, 208), // plum (purplish)
            new Color(255, 163, 0),   // orange
            new Color(183, 183, 183)  // gray
    };
    public Color getColor(int index){
        if(index < 0 || index >= colors.length){
            throw new IllegalArgumentException("invalid color code");
        }
        return colors[index];
    }
}

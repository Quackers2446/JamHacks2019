import java.lang.Runnable;

Button selectedButton;

class Button extends Component {
    String text = "";
    PImage icon;
    Runnable onPress;
    Runnable onRelease;
    boolean border = true;
    boolean fill = true;
    color colour = color(255, 2, 100);

    Button(String text, float xPos, float yPos, float bWidth, float bHeight) {
        super(xPos, yPos, bWidth, bHeight);
        this.text = text;
    }

    Button(PImage icon, float xPos, float yPos, float bWidth, float bHeight) {
        super(xPos, yPos, bWidth, bHeight);
        this.icon = icon;
    }

    void setOnClick(Runnable stuff) {
        onPress = stuff;
    }
    
    void setOnRelease(Runnable stuff) {
        onRelease = stuff;
    }

    void onSelect() {
        onPress.run();
        selectedButton = this;
        colour = color(55, 2, 150);
    }

    void onRelease() {
        onRelease.run();
        resetColour();
    }
    
    void resetColour() {
        colour = color(255, 2, 100);
    }

    void display() {
        fill(colour);
        super.display();
        pushMatrix();
        translate(position.x + size.x/2, position.y + size.y/2);
        textAlign(CENTER);
        textSize(150);
        fill(0);
        text(text, 0, 50);
        if (icon != null) {
            translate(-icon.width/2, -icon.height/2);
            image(icon, 0, 0);
        }
        popMatrix();
    }
}

Component[] components = {};

class Component {
    boolean visible = true;
    PVector position;
    PVector size;
    int number;

    public Component(float xPos, float yPos, float cWidth, float cHeight) {
        position = new PVector(xPos, yPos);
        size = new PVector(cWidth, cHeight);
        components = (Component[]) append(components, this);
    }

    boolean selected(float selectedX, float selectedY) {
        if (selectedX > position.x && selectedX < position.x + size.x && selectedY > position.y && selectedY < position.y + size.y) {
            return true;
        }
        return false;
    }

    void display() {
        pushMatrix();
        translate(position.x, position.y);
        rect(0, 0, size.x, size.y);
        popMatrix();
    }

    void onSelect() {
    }

    void onRelease() {
    }

}

void displayComponents() {
    for (int i = 0; i < components.length; i++) {
        components[i].display();
    }
}

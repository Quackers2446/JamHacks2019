float timePressed = 0;

void mousePressed() {
    for (int i = 0; i < components.length; i++) {
        select(components[i]);
    }
    timePressed = millis();
}

void select(Component component) {
    if (component.selected(mouseX, mouseY)) {
        component.onSelect();
    }
}

void mouseReleased() {
    if (selectedButton!=null) {
        selectedButton.onRelease();
    }
    for(Component i : components){
        if(i instanceof Button){
            ((Button) i).resetColour();
        }
    }        
}

String text;

void setup(){
    NetworkUtils.initializeSocket("192.168.1.51", 8000, "Donny");
    text = "HI";
    orientation(LANDSCAPE);
    fullScreen();
    textAlign(CENTER);
    Button left = new Button("L", width / 4 - 225, height / 2 - 75, 150, 150);
    left.setOnClick(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(3, true);
            text = "LEFT Click";
        }
    });
    left.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(3, false);
            text = "LEFT Release";
        }
    });
    Button right = new Button("R", width / 4 + 75, height / 2 - 75, 150, 150);
    right.setOnClick(new Runnable(){
        @Override
        public void run(){
            text = "RIGHT Click";
            NetworkUtils.sendUpdate(4, true);
        }
    });
    right.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(4, false);
            text = "RIGHT Release";
        }
    });
    Button up = new Button("U", width / 4 - 75, height / 2 - 225, 150, 150);
    up.setOnClick(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(1, true);
            text = "UP Click";
        }
    });
    up.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(1, false);
            text = "UP Release";
        }
    });
    Button down = new Button("D", width / 4 - 75, height / 2 + 75, 150, 150);
    down.setOnClick(new Runnable(){
        @Override
        public void run(){
            text = "DOWN Click";
            NetworkUtils.sendUpdate(2, true);
        }
    });
    down.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(2, false);
            text = "DOWN Release";
        }
    });
    
    Button water = new Button("W", 3 * width / 4 - 100, height / 2 - 100 , 200, 200);
    water.setOnClick(new Runnable(){
        @Override
        public void run(){
            text = "Water Click";
            NetworkUtils.sendUpdate(0, true);
        }
    });
    water.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(1, false);
            text = "Water Release";
        }
    });
    
    Button clockwise = new Button("C", 3 * width / 4 - 300, height / 2 - 250 , 200, 150);
    clockwise.setOnClick(new Runnable(){
        @Override
        public void run(){
            text = "C";
            NetworkUtils.sendUpdate(5, true);
        }
    });
    clockwise.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(5, false);
            text = "C Release";
        }
    });
    
    Button counterclockwise = new Button("W", 3 * width / 4 + 100, height / 2 - 250 , 200, 150);
    counterclockwise.setOnClick(new Runnable(){
        @Override
        public void run(){
            text = "CC";
            NetworkUtils.sendUpdate(6, true);
        }
    });
    counterclockwise.setOnRelease(new Runnable(){
        @Override
        public void run(){
            NetworkUtils.sendUpdate(6, false);
            text = "CC Release";
        }
    });
}

void draw(){
    background(255);
    textSize(100);
    text(text, 100, 100);
    displayComponents();
    
}

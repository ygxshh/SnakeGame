ArrayList<Integer> x_pos = new ArrayList<Integer>();
ArrayList<Integer> y_pos = new ArrayList<Integer>();

int hgt = 24, wdt = 24;
int block = 20;

int dir = 2;
int[] x_dir = {0, 0, 1, -1}; // Directions for movement
int[] y_dir = {1, -1, 0, 0};

int f_x_pos, f_y_pos;
boolean gamestatus = false;
int speed = 10;

void setup() {
    size(500, 500);
    resetGame(); // Initialize the game state
}

void draw() {
    if (!gamestatus) {
        background(0);
        fill(255);
        // Draw the snake
        for (int i = 0; i < x_pos.size(); i++) {
            rect(x_pos.get(i) * block, y_pos.get(i) * block, block, block);
        }
        // Move the snake at the given speed
        if (frameCount % speed == 0) {
            moveSnake();
            checkCollision();
        }
        // Draw the food
        fill(255);
        rect(f_x_pos * block, f_y_pos * block, block, block);
        // Display the score
        textAlign(LEFT);
        textSize(25);
        fill(255);
        text("Score: " + (x_pos.size() - 1), 0, 20);
    } else {
        gameOverScreen();
    }
}

void moveSnake() {
    x_pos.add(0, x_pos.get(0) + x_dir[dir]);
    y_pos.add(0, y_pos.get(0) + y_dir[dir]);
    // Check for wall collision
    if (x_pos.get(0) < 0 || y_pos.get(0) < 0 || x_pos.get(0) >= wdt || y_pos.get(0) >= hgt) {
        gamestatus = true;
    } else {
        // Check for self-collision
        for (int i = 1; i < x_pos.size(); i++) {
            if (x_pos.get(0) == x_pos.get(i) && y_pos.get(0) == y_pos.get(i)) {
                gamestatus = true;
            }
        }
    }
}

void spawnFood() {
    boolean onSnake = true;
    while (onSnake) {
        onSnake = false;
        f_x_pos = (int) random(0, wdt);
        f_y_pos = (int) random(0, hgt);
        for (int i = 0; i < x_pos.size(); i++) {
            if (x_pos.get(i) == f_x_pos && y_pos.get(i) == f_y_pos) {
                onSnake = true;
                break;
            }
        }
    }
}

void checkFood() {
    if (x_pos.get(0) == f_x_pos && y_pos.get(0) == f_y_pos) {
        spawnFood();
        // No need to remove tail, so the snake grows
        speed = max(1, speed - 1); // Increase speed as snake grows
    } else {
        // Remove the last segment of the snake (tail) to keep its length constant
        x_pos.remove(x_pos.size() - 1);
        y_pos.remove(y_pos.size() - 1);
    }
}

void checkCollision() {
    checkFood();
}

void gameOverScreen() {
    fill(222, 9, 12);
    textAlign(CENTER);
    textSize(30);
    text("Game Over \nScore: " + (x_pos.size() - 1) + "\nPress Enter to Restart", width / 2, height / 2);
    if (keyPressed && key == ENTER) {
        resetGame();
    }
}

void resetGame() {
    x_pos.clear();
    y_pos.clear();
    x_pos.add(4);
    y_pos.add(15);
    dir = 2;
    gamestatus = false;
    speed = 10;
    spawnFood();
}

void keyPressed() {
    int new_dir = -1;
    if (keyCode == DOWN) new_dir = 0;
    else if (keyCode == UP) new_dir = 1;
    else if (keyCode == LEFT) new_dir = 3;
    else if (keyCode == RIGHT) new_dir = 2;
    // Ensure the snake can't reverse direction
    if (new_dir != -1 && (x_dir[dir] != -x_dir[new_dir] || y_dir[dir] != -y_dir[new_dir])) {
        dir = new_dir;
    }
}

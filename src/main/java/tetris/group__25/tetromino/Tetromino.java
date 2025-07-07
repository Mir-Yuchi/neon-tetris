package tetris.group__25.tetromino;

import javafx.scene.paint.Color;

public abstract class Tetromino implements Cloneable {
    // Tetromino is the base class for all tetromino shapes in Tetris.
    protected int[][] shape;
    protected Color color;
    protected int x;
    protected int y;
    protected int rotationState;

    protected Tetromino(int[][] shape, int colorCode) {
        // Constructor initializes the tetromino with a shape and color.
        this.shape = deepCopy(shape);
        this.color = getColorFromCode(colorCode);
        this.x = 0;
        this.y = 0;
        this.rotationState = 0;
    }

    public int[][] rotate() {
        // Rotate the tetromino shape 90 degrees clockwise.
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                rotated[c][shape.length - 1 - r] = shape[r][c];
            }
        }
        rotationState = (rotationState + 1) % 4;
        return rotated;
    }

    @Override
    public Tetromino clone() {
        // Create a deep copy of the Tetromino object.
        try {
            Tetromino clone = (Tetromino) super.clone();
            clone.shape = deepCopy(this.shape);
            clone.color = this.color;
            clone.x = this.x;
            clone.y = this.y;
            clone.rotationState = this.rotationState;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }

    private int[][] deepCopy(int[][] original) {
        // Create a deep copy of a 2D array.
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public int[][] getShape() { return deepCopy(shape); }
    public void setShape(int[][] shape) { this.shape = deepCopy(shape); }
    public Color getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getRotationState() { return rotationState; }

    public static Color getColorFromCode(int code) {
        // Map the tetromino color code to a Color object.
        return switch (code) {
            case 1 -> Color.CYAN;    // I
            case 2 -> Color.BLUE;    // J
            case 3 -> Color.ORANGE;  // L
            case 4 -> Color.YELLOW;  // O
            case 5 -> Color.GREEN;   // S
            case 6 -> Color.PURPLE;  // T
            case 7 -> Color.RED;     // Z
            default -> Color.BLACK;
        };
    }
}
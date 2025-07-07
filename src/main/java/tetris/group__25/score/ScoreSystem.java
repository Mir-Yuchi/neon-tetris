package tetris.group__25.score;

public class ScoreSystem {
    // ScoreSystem tracks the player's score, level, total lines cleared, combo count, and back-to-back status.
    private int score;
    private int totalLines;
    private int level;
    private int combo;
    private boolean backToBack;

    public ScoreSystem() {
        // Initialize the score system with default values.
        this.score = 0;
        this.totalLines = 0;
        this.level = 0;
        this.combo = 0;
        this.backToBack = false;
    }

    public void addClear(int linesCleared) {
        // Add points based on the number of lines cleared.
        int points = switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };

        points *= (level + 1);
        if (linesCleared == 4) {
            // If a Tetris (4 lines cleared), apply back-to-back bonus if applicable.
            if (backToBack) points = (int) (points * 1.5);
            backToBack = true;
        } else {
            backToBack = false;
        }

        if (linesCleared > 0) {
            combo++;
            points += 50 * combo * (level + 1);
        } else {
            combo = 0;
        }

        score += points;
        totalLines += linesCleared;
        level = totalLines / 10;
    }

    public void reset() {
        // Reset the score system to its initial state.
        score = 0;
        totalLines = 0;
        level = 0;
        combo = 0;
        backToBack = false;
    }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getTotalLines() { return totalLines; }
    public int getCombo() { return combo; }
    public boolean isBackToBack() { return backToBack; }
}
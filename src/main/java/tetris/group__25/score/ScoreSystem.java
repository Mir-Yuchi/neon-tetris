package tetris.group__25.score;

public class ScoreSystem {
    private int score;
    private int totalLines;
    private int level;

    public ScoreSystem() {
        reset();
    }

    public void addClear(int linesCleared) {
        int base;
        switch (linesCleared) {
            case 1 -> base = 40;
            case 2 -> base = 100;
            case 3 -> base = 300;
            case 4 -> base = 1200;
            default -> base = 0;
        }
        score += base * (level + 1);
        totalLines += linesCleared;
        level = totalLines / 10;
    }

    public void reset() {
        score = 0;
        totalLines = 0;
        level = 0;
    }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getTotalLines() { return totalLines; }
}

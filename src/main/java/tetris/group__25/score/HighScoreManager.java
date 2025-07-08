package tetris.group__25.score;

import java.util.prefs.Preferences;

public class HighScoreManager {
    private static final String HIGH_SCORE_KEY = "highScore";
    private final Preferences prefs = Preferences.userNodeForPackage(HighScoreManager.class);

    public int getHighScore() {
        return prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    public void setHighScore(int score) {
        if (score > getHighScore()) {
            prefs.putInt(HIGH_SCORE_KEY, score);
        }
    }
}

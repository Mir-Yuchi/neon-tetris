package tetris.group__25.score;

import java.util.prefs.Preferences;

public class HighScoreManager {
// This class manages the high score using Java Preferences API.
    private static final String HIGH_SCORE_KEY = "highScore";
    private final Preferences prefs;

    public HighScoreManager() {
        // Initialize the Preferences instance for this class.
        prefs = Preferences.userNodeForPackage(HighScoreManager.class);
    }

    public int getHighScore() {
        // Retrieve the high score from preferences, defaulting to 0 if not set.
        return prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    public void setHighScore(int score) {
        // Set the high score in preferences only if the new score is higher than the current high score.
        int currentHighScore = getHighScore();
        if (score > currentHighScore) {
            prefs.putInt(HIGH_SCORE_KEY, score);
        }
    }
}
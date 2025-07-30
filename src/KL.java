import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KL implements KeyListener {
    private final Set<Integer> currentlyPressedKeys = new HashSet<>();

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used for movement
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentlyPressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentlyPressedKeys.remove(e.getKeyCode());
    }

    public boolean isKeyPressed(int keyCode) {
        return currentlyPressedKeys.contains(keyCode);
    }

    public void reset() {
        currentlyPressedKeys.clear();
    }
}
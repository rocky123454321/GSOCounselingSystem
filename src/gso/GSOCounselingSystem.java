package gso;

import gso.ui.GSOCounselingSystemFrame;
import javax.swing.SwingUtilities;

public final class GSOCounselingSystem {
    private GSOCounselingSystem() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GSOCounselingSystemFrame::new);
    }
}

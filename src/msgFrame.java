

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class msgFrame extends JFrame {


	/**
	 *
	 */
	private static final long serialVersionUID = 3548896420206765896L;

	public msgFrame(String msg) {
		JLabel label = new JLabel(msg, JLabel.CENTER);
		add(label);

        setLayout(new FlowLayout());
        setTitle("Error");
        setVisible(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
}



import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class multiFrame extends JFrame {


	/**
	 *
	 */
	private static final long serialVersionUID = 8367240108880889277L;

	public multiFrame(ArrayList<String> arrayList) {
		JComboBox<?> label = new JComboBox<Object>(arrayList.toArray());
		add(label);

        setLayout(new FlowLayout());
        setTitle("Error");
        setVisible(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
}

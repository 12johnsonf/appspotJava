package clientHack;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class markSelect extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3635981608702144530L;
	Map<String, String> cookies;
	JComboBox<?> box;
	JLabel label;
	JButton random;
	JButton select;

	public markSelect(Map<String, String> cookies2) throws IOException {

		cookies = cookies2;

		box = new JComboBox<Object>(methods.finds(cookies)
				.toArray());
		label = new JLabel("Pick a question number or hit random");
		random = new JButton("Random");
		select = new JButton("Select");

		JPanel gui = new JPanel(new BorderLayout(2, 3));
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(gui);
		gui.add(label, BorderLayout.WEST);
		gui.add(box, BorderLayout.CENTER);
		gui.add(random, BorderLayout.EAST);
		gui.add(select, BorderLayout.SOUTH);

		random.addActionListener(this);
		select.addActionListener(this);

		setTitle("Mark question");
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		String q_no;
		dispose();
		if (e.getSource() == random) {
			ArrayList<String> list = null;
			try {
				list = methods.finds(cookies);
			} catch (IOException e1) {}
			Random randomizer = new Random();
			q_no = list.get(randomizer.nextInt(list.size()));
		} else {
			q_no = String.valueOf(box.getSelectedItem());
		}
		try {
			new markFrame(cookies, q_no);
		} catch (IOException e1) {}
	}

}

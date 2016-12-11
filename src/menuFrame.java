

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class menuFrame extends JFrame implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 2792520817263434748L;
	JButton mark = new JButton("Mark questions");
	JButton answer = new JButton("Answer questions");
	JButton cheat = new JButton("Cheat at questions");
	JButton close = new JButton("Close");
	Map<String, String> cookies;
	JFrame main;

	public menuFrame(Map<String, String> cookies2) throws IOException {

		cookies = cookies2;

		Document qCheck = Jsoup
				.connect("http://ocrgcsecomputing.appspot.com/questions.jsp")
				.cookies(cookies).get();
		Document mCheck = Jsoup
				.connect("http://ocrgcsecomputing.appspot.com/markquestion.jsp")
				.cookies(cookies).get();
		boolean needAnswer = qCheck.getElementsByTag("h1").first() == null;
		boolean moderator = (mCheck.getElementsByTag("h1").first().text().equals("Only moderators can mark questions, at the moment  you have not be designated a moderator!"));

		main = new JFrame("Main menu");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (!moderator)
			mark.setEnabled(false);
		if (!needAnswer)
			answer.setEnabled(false);
		if (!moderator || !needAnswer)
			cheat.setEnabled(false);

		JPanel gui = new JPanel(new BorderLayout(3, 2));
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		main.setContentPane(gui);
		JPanel header = new JPanel(new GridLayout(1, 0));
		JPanel labels = new JPanel(new GridLayout(0, 1));
		JPanel controls = new JPanel(new GridLayout(0, 1));
		gui.add(header, BorderLayout.NORTH);
		gui.add(labels, BorderLayout.WEST);
		gui.add(controls, BorderLayout.CENTER);

		header.add(new JLabel("Main menu"));
		labels.add(mark);
		labels.add(close);
		controls.add(answer);
		controls.add(cheat);

		mark.addActionListener(this);
		answer.addActionListener(this);
		cheat.addActionListener(this);
		close.addActionListener(this);

		main.pack();
		main.setVisible(true);
		main.setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		main.dispose();
		if (e.getSource() == mark) {
			try {
				new markSelect(cookies);
			} catch (IOException e1) {
			}
		} else if (e.getSource() == answer) {
			try {
				new answerFrame(cookies);
			} catch (IOException | URISyntaxException e1) {
			}
		} else {
			try {
				new cheatFrame(cookies);
			} catch (IOException | URISyntaxException e1) {
			}
		}
	}
}

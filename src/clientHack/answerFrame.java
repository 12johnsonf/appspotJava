package clientHack;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class answerFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -996436564331235943L;
	Map<String, String> cookies;
    JLabel markscheme;
    JLabel answerLabel;
    JLabel remaining;
    JTextArea answer;
    JButton menu;
    JButton cancel;
    JButton next;
    String number;
	
	public answerFrame(Map<String, String> cookies2) throws IOException, URISyntaxException {
		cookies = cookies2;
		
		JPanel gui = new JPanel(new BorderLayout(3,2));

        gui.setBorder(new EmptyBorder(5,5,5,5));

        setContentPane(gui);

		ImageIcon markschemeImg = new ImageIcon(new URL(methods.findAnsImg(cookies, true)));
		int rem = methods.remaining(cookies);
		remaining = new JLabel("You have "+rem+" questions remaining");
		markscheme = new JLabel(markschemeImg);
		answerLabel = new JLabel("Answer:");
		answer = new JTextArea(10,20);
		answer.setLineWrap(true);
		menu = new JButton("<html>Submit and back to main menu</html>");
		next = new JButton("<html>Submit and next</html>");
		cancel = new JButton("<html>Discard and cancel</html>");
		
		
		JPanel marking = new JPanel(new BorderLayout(2,3));
		JPanel nav = new JPanel(new BorderLayout(3,2));
        
        marking.add(answerLabel, BorderLayout.WEST);
        marking.add(answer, BorderLayout.CENTER);
        
        nav.add(cancel, BorderLayout.WEST);
        nav.add(menu, BorderLayout.CENTER);
        nav.add(next, BorderLayout.EAST);
        nav.add(remaining, BorderLayout.NORTH);
        
       
        gui.add(markscheme, BorderLayout.NORTH);
        gui.add(marking,BorderLayout.CENTER);
        gui.add(nav, BorderLayout.SOUTH);
        
        menu.addActionListener(this);
        next.addActionListener(this);
        cancel.addActionListener(this);
        
        setTitle("Question marking");
        setVisible(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if (src==menu||src==next){
			try {
				methods.submitAnswer(answer.getText(), cookies);
			} catch (IOException e1) {}
		} if (src==next){
			try {
				new answerFrame(cookies);
			} catch (IOException | URISyntaxException e1) {}
		} else {
			try {
				new menuFrame(cookies);
			} catch (IOException e1) {}
		}
		dispose();
	}

}

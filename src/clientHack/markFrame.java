package clientHack;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class markFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4016323486195748050L;
	Map<String, String> cookies;
    JComboBox<?> box;
    JLabel label;
    JButton random;
    JButton select;
    JLabel markscheme;
    JTextField marks;
    JLabel marksLabel;
    JLabel userAnswer;
    JLabel commentLabel;
    JTextField comment;
    JButton menu;
    JButton cancel;
    JButton next;
    String number;
	
	public markFrame(Map<String, String> cookies2, String q_no) throws IOException {
		cookies = cookies2;
		number=q_no;
		
		box = new JComboBox<Object>(methods.finds(cookies).toArray());
		label = new JLabel("Pick a question number or hit random");
		random = new JButton("Random");
		select = new JButton("Select");
		JPanel selector = new JPanel(new BorderLayout(2,3));
		JPanel gui = new JPanel(new BorderLayout(3,2));
		String msURL = methods.toMsUrl(q_no, cookies);
		selector.add(label, BorderLayout.WEST);
        selector.add(box, BorderLayout.CENTER);
        selector.add(random, BorderLayout.EAST);
        selector.add(select, BorderLayout.SOUTH);

        gui.add(selector,BorderLayout.NORTH);
        gui.setBorder(new EmptyBorder(5,5,5,5));

        setContentPane(gui);
        
		if (!msURL.isEmpty()){
			ImageIcon markschemeImg = new ImageIcon(new URL(methods.toMsUrl(q_no, cookies)));
			markscheme = new JLabel(markschemeImg);
			marks = new JTextField(2);
			marksLabel = new JLabel("Marks:");
			userAnswer = new JLabel("<html>\""+methods.userAnswer(q_no, cookies).replaceAll("(\r\n|\n\r|\r|\n)", "<br>")+"\"</html>");
			commentLabel = new JLabel("Comment:");
			comment = new JTextField(20);
			menu = new JButton("<html>Submit and back to main menu</html>");
			next = new JButton("<html>Submit and next</html>");
			cancel = new JButton("<html>Discard and cancel</html>");
			
			
			JPanel marking = new JPanel(new BorderLayout(2,3));
			JPanel marksPanel = new JPanel(new BorderLayout(3,2));
			JPanel commentPanel = new JPanel(new BorderLayout(3,2));
			JPanel nav = new JPanel(new BorderLayout(3,2));
	        
	        marksPanel.add(userAnswer, BorderLayout.NORTH);
	        marksPanel.add(marksLabel, BorderLayout.WEST);
	        marksPanel.add(marks, BorderLayout.CENTER);
	        
	        commentPanel.add(commentLabel, BorderLayout.WEST);
	        commentPanel.add(comment, BorderLayout.CENTER);
	        
	        marking.add(marksPanel, BorderLayout.NORTH);
	        marking.add(commentPanel, BorderLayout.SOUTH);
	        
	        nav.add(cancel, BorderLayout.WEST);
	        nav.add(menu, BorderLayout.CENTER);
	        nav.add(next, BorderLayout.EAST);
	        
	        marking.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
	        selector.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
	        gui.add(markscheme, BorderLayout.WEST);
	        gui.add(marking,BorderLayout.EAST);
	        gui.add(nav, BorderLayout.SOUTH);
	        
	        menu.addActionListener(this);
	        next.addActionListener(this);
	        cancel.addActionListener(this);
	        
		} else{
			gui.add(new JLabel("No more of this question, select another"), BorderLayout.SOUTH);
		}
        
		random.addActionListener(this);
		select.addActionListener(this);
        
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
				methods.submitMark(number, marks.getText(), comment.getText(), cookies);
			} catch (IOException | URISyntaxException | InterruptedException e1) {}
		}if (src==menu||src==cancel){
			try {
				new menuFrame(cookies);
			} catch (IOException e1) {}
		}
		if (src == random) {
			ArrayList<String> list = null;
			try {
				list = methods.finds(cookies);
			} catch (IOException e1) {}
			Random randomizer = new Random();
			number = list.get(randomizer.nextInt(list.size()));
		} if (src==select){
			number = String.valueOf(box.getSelectedItem());
		}
		if (src==next||src==random||src==select){
			try {
				methods.newMarkFrame(cookies, number);
			} catch (IOException e1) {}
		}
		dispose();
	}

}

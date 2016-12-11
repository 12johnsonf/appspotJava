package clientHack;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class loginFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5992354732592148195L;
	JTextField name;
    JPasswordField pass;
    JButton submit;
    JFrame main;
	
	public loginFrame() {
		main = new JFrame("Login Form ");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        name = new JTextField(10);
        pass = new JPasswordField(10);

        JPanel gui = new JPanel(new BorderLayout(3,3));
        gui.setBorder(new EmptyBorder(5,5,5,5));
        main.setContentPane(gui);
        JPanel labels = new JPanel(new GridLayout(0,1));
        JPanel controls = new JPanel(new GridLayout(0,1));
        gui.add(labels, BorderLayout.WEST);
        gui.add(controls, BorderLayout.CENTER);

        labels.add(new JLabel("Username: "));
        controls.add(name);
        labels.add(new JLabel("Password: "));
        controls.add(pass);
        submit = new JButton("Submit");
        
        submit.addActionListener(this);

        gui.add(submit, BorderLayout.SOUTH);
        main.pack();
        main.setVisible(true);
        main.setLocationRelativeTo(null);
    }

    @SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
    	String username = name.getText();
    	String password = pass.getText();
    	main.dispose();
		Response res = null;
		try {
			res = Jsoup
					.connect("http://ocrgcsecomputing.appspot.com/login.jsp")
					.data("username2", username, "password", password)
					.method(Method.POST).execute();
		} catch (IOException e1) {
		}
		Map<String, String> cookies = res.cookies();
		Document loginCheck1 = null;
		try {
			loginCheck1 = Jsoup
					.connect(
							(String) "http://ocrgcsecomputing.appspot.com/questions.jsp")
					.cookies(cookies).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean loggedIn = !loginCheck1
				.getElementsByAttributeValue("href", "logout.jsp")
				.toString().isEmpty();
		if (loggedIn) {
			try {
				new menuFrame(cookies);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else{
			new msgFrame("Login failed. Please try again.");
		}
		
    }
}


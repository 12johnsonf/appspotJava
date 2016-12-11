

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;

public class proxyFrame extends JFrame implements ActionListener{
	/**
	 *
	 */
	private static final long serialVersionUID = 6768220396298070999L;
	JTextField host;
	JTextField port;
    JButton submit;
    JFrame main;

	public proxyFrame() {
		main = new JFrame("Proxy Settings");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        host = new JTextField(10);
        port = new JTextField(10);

        JPanel gui = new JPanel(new BorderLayout(3,2));
        gui.setBorder(new EmptyBorder(5,5,5,5));
        main.setContentPane(gui);
        JPanel header = new JPanel(new GridLayout(1,0));
        JPanel labels = new JPanel(new GridLayout(0,1));
        JPanel controls = new JPanel(new GridLayout(0,1));
        gui.add(header, BorderLayout.NORTH);
        gui.add(labels, BorderLayout.WEST);
        gui.add(controls, BorderLayout.CENTER);

        header.add(new JLabel("We have detected you are behind a proxy. Please enter your settings."));
        labels.add(new JLabel("Proxy Host: "));
        controls.add(host);
        labels.add(new JLabel("Proxy Port: "));
        controls.add(port);
        submit = new JButton("Submit");

        submit.addActionListener(this);

        gui.add(submit, BorderLayout.SOUTH);
        main.pack();
        main.setVisible(true);
        main.setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
    	System.setProperty("http.proxyHost", host.getText());
		System.setProperty("http.proxyPort", port.getText());

		main.dispose();
		try {
			Jsoup.connect(
					"http://ocrgcsecomputing.appspot.com/login.jsp")
					.get();
			new loginFrame();
		} catch (IOException g) {
			new msgFrame("Proxy Setting do not work, please try again.");
		}
    }
}

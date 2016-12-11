

//import a bunch of important things
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class methods {
	// make some global variables, some of which probably don't really need to
	// be global
	static boolean proxy;
	static Map<String, String> cookies;
	static String username;
	static String password;
	static boolean quit;
	static Scanner input = new Scanner(System.in);
	static boolean onProxy = false;

	// our first method - this one takes in a question number (with the cookies
	// too)
	public static String toMsUrl(String toLocate, Map<String, String> cookies2)
			throws IOException {

		// just set it to the global for REASONS
		cookies = cookies2;

		// grab the marking document with JSoup
		Document marking = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/markquestion.jsp")
				// send it the cookies so we stay logged in
				.cookies(cookies).get();
		// find all the elements that have the question number we want
		Elements b = marking.getElementsByAttributeValue("value", toLocate);
		// if there's no elements with the question number we want, send back an
		// empty string so we can handle this
		if (b.isEmpty()) {
			return ("");
		}
		// get the first element in the list, because we only really need one
		Element c = b.first();
		// get the element after, which should contain the question ID
		Element d = c.nextElementSibling();
		// save the question ID as q_id because logical naming is YAY
		String q_id = d.attr("value");
		// get a page with the question number and ID, so we can harvest the
		// markscheme URL
		Document ansPage = Jsoup
				.connect(
						(String) ("http://ocrgcsecomputing.appspot.com/markquestion.jsp?submitted_q_id="
								+ q_id + "&submitted_q_number=" + toLocate))
				.cookies(cookies).get();
		// pick out the image elements, the first of which should be the
		// markscheme
		Elements markscheme = ansPage.getElementsByTag("img");
		// pick out the first image which will be what we want, and return that
		return markscheme.first().attr("src");
	}

	// this is a simple method that I made because it wouldn't let me make a
	// markframe within a markframe for some reason - this fixed it
	public static void newMarkFrame(Map<String, String> cookies2, String q_no)
			throws IOException {
		new markFrame(cookies2, q_no);
	}

	// this function takes in the cookies and a boolean - if you want the
	// question image or the question number
	public static String findAnsImg(Map<String, String> cookies, boolean img)
			throws IOException, URISyntaxException {
		// get the answering page and save it
		Document answer = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/questions.jsp")
				// cookies yay
				.cookies(cookies).get();
		// find the question number element
		Elements b = answer.getElementsByAttributeValue("name", "qn");
		// find the image element
		Elements c = answer.getElementsByTag("img");
		// if we want the img (boolean in header)
		if (img)
			// return the first image element's src attribute (the URL)
			return c.first().attr("src");
		// a return will end the function, so no need for an else - if we're
		// here we need to return the question number
		return b.first().attr("value");
	}

	// this function will take in the an answer and send it off to the sever
	// with the magic of HTTP GET
	public static void submitAnswer(String uanswer, Map<String, String> cookies)
			throws IOException {
		// connect to the questions page
		Document answer = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/questions.jsp")
				.cookies(cookies).get();
		// get the question number for sending off in our request
		String b = answer.getElementsByAttributeValue("name", "qn").first()
				.attr("value");
		System.out.println(b);
		// send off the user answer with the question number
		Jsoup.connect("http://ocrgcsecomputing.appspot.com/questions.jsp")
				.data("answer", uanswer, "qn", b)
				.method(Method.POST).execute();
	}

	public static int remaining(Map<String, String> cookies) throws IOException {
		// connect to the questions page
		Document answer = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/questions.jsp")
				.cookies(cookies).get();
		// get the question number for sending off in our request
		int rem = Integer.parseInt(answer
				.getElementsByAttributeValue("type", "submit").get(1)
				.attr("value").replaceAll("[\\D]", ""));
		return rem;
	}

	// this will return the text of the answer we have to mark
	public static String userAnswer(String toMark, Map<String, String> cookies)
			throws IOException {
		// connect to the marking page
		Document marking = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/markquestion.jsp")
				.cookies(cookies).get();
		// select the question number for us
		Elements b = marking.getElementsByAttributeValue("value", toMark);
		// if there's none of that question number...
		if (b == null) {
			// return an empty string so we can handle that
			return ("");
		}
		// select the first element
		Element c = b.first();
		// get the next element after that, since that has our all-important
		// question ID
		Element d = c.nextElementSibling();
		// set the question number and ID to variables
		String q_id = d.attr("value");
		String q_no = c.attr("value");
		// connect to the page with the answer by sending it the ID and number
		Document ansPage = Jsoup
				.connect(
						(String) ("http://ocrgcsecomputing.appspot.com/markquestion.jsp?submitted_q_id="
								+ q_id + "&submitted_q_number=" + q_no))
				.cookies(cookies).get();
		// select the red text, which happens to be the answer
		Element usrAns = ansPage.getElementsByAttributeValue("style",
				"color:red").last();
		// return the text inside that element
		return usrAns.ownText();

	}

	// this will submit the mark and comment that we gave for the question
	public static void submitMark(String toMark, String marks, String comment,
			Map<String, String> cookies) throws IOException,
			URISyntaxException, InterruptedException {
		// basically does the same as UserAnswer - go read that
		Document marking = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/markquestion.jsp")
				.cookies(cookies).get();
		Elements b = marking.getElementsByAttributeValue("value", toMark);
		Element c = b.first();
		Element d = c.nextElementSibling();
		String q_id = d.attr("value");
		String q_no = c.attr("value");
		// here's where it gets different - instead of loading a question page,
		// we load the main page but submit out mark and comment
		Jsoup.connect(
				(String) ("http://ocrgcsecomputing.appspot.com/markquestion.jsp?submitted_mark_q_number="
						+ q_no
						+ "&submitted_mark_q_id="
						+ q_id
						+ "&submitted_mark_q_mark="
						+ marks
						+ "&submitted_comment=" + comment)).cookies(cookies)
				.get();
	}

	// returns an ArrayList of all the question numbers available for marking
	public static ArrayList<String> finds(Map<String, String> cookies)
			throws IOException {
		// loads the marking page with cookies so we're logged in
		Document marking = Jsoup
				.connect(
						(String) "http://ocrgcsecomputing.appspot.com/markquestion.jsp")
				.cookies(cookies).get();
		// create our ArrayList of strings
		ArrayList<String> a = new ArrayList<String>();
		// oooh fancy for loop syntax - this will iterate over every element
		// that has a "submitted_q_number", which is a question number -
		// basically e will be an HTML element
		for (Element e : marking.getElementsByAttributeValue("name",
				"submitted_q_number")) {
			// that number is already in the ArrayList (the page is ordered by
			// username) then...
			if (a.contains(e.attr("value")))
				// continue, which will ignore everything after it and go to the
				// next iteration on the for loop
				continue;
			// we don't need an else because of the continue - so at this point
			// we'll add the value (question number) to our ArrayList
			a.add(e.attr("value"));
		}
		// sort the ArrayList. It will put 100 above 11, but it's the best I can
		// be bothered to do
		Collections.sort(a);
		// return the ArrayList
		return a;
	}

	// onto the main method - this is what is done first when we run our .jar
	public static void main(String[] args) throws URISyntaxException,
			IOException, InterruptedException {
		// try = test if this works
		try {
			// if this works, no proxy
			Jsoup.connect(
					(String) "http://ocrgcsecomputing.appspot.com/login.jsp")
					.get();
			// catch = if that fails, do this
		} catch (IOException e) {
			// set the WHSB proxy settings
			System.setProperty("http.proxyHost", "d99.cache.e2bn.org");
			System.setProperty("http.proxyPort", "8084");
			// then try to connect
			try {
				Jsoup.connect("http://ocrgcsecomputing.appspot.com/login.jsp")
						.get();
				// otherwise, use a JFrame to get the user to set their own
				// proxy settings
			} catch (IOException f) {
				new proxyFrame();
				onProxy = true;

			}
		}
		// if there's no proxy, just bring up the login
		if (!onProxy)
			new loginFrame();
	}
}

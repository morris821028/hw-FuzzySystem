package calcModel.geneAlgorithm.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GenePoolDialog extends JDialog implements ActionListener,
		PropertyChangeListener {
	private String typedText = null;
	private JSpinner spinnerIterator;
	private JSpinner spinnerPoolSize;
	private JSpinner spinnerPcrossover;
	private JSpinner spinnerRcrossover;
	private JSpinner spinnerPmutation;
	private JSpinner spinnerRmutation;
	// private DialogDemo dd;

	private String magicWord;
	private JOptionPane optionPane;

	private String btnString1 = "Enter";
	private String btnString2 = "Cancel";

	/**
	 * Returns null if the typed string was invalid; otherwise, returns the
	 * string as the user entered it.
	 */
	public String getValidatedText() {
		return typedText;
	}

	public Object[] getInputValue() {
		Object[] ret = { spinnerPoolSize.getValue(),
				spinnerPcrossover.getValue(), spinnerRcrossover.getValue(),
				spinnerPmutation.getValue(), spinnerRmutation.getValue(),
				spinnerIterator.getValue() };
		return ret;
	}

	/** Creates the reusable dialog. */
	public GenePoolDialog(Frame aFrame, String aWord/* , DialogDemo parent */) {
		super(aFrame, true);
		// dd = parent;

		magicWord = aWord.toUpperCase();
		setTitle("Gene Pool Setting");
		SpinnerModel spinnerModel;
		spinnerModel = new SpinnerNumberModel(256, 1, 32767, 1);
		spinnerIterator = new JSpinner(spinnerModel);
		spinnerModel = new SpinnerNumberModel(256, 1, 32767, 1);
		spinnerPoolSize = new JSpinner(spinnerModel);
		spinnerModel = new SpinnerNumberModel(0.5f, 0, 1, 0.01);
		spinnerPcrossover = new JSpinner(spinnerModel);
		spinnerModel = new SpinnerNumberModel(0.5f, 0, 1, 0.01);
		spinnerRcrossover = new JSpinner(spinnerModel);
		spinnerModel = new SpinnerNumberModel(0.5f, 0, 1, 0.01);
		spinnerPmutation = new JSpinner(spinnerModel);
		spinnerModel = new SpinnerNumberModel(0.5f, 0, 1, 0.01);
		spinnerRmutation = new JSpinner(spinnerModel);
		// Create an array of the text and components to be displayed.
		String msgString1 = "請設定基因池訊息 (Setting Gene Pool)\n";
		String msgString2 = "族群大小 (Pool size)";
		String msgString3 = "交配機率 (probability Of Crossover)";
		String msgString4 = "交配比率 (ratio Of Crossover)";
		String msgString5 = "突變機率 (probability Of Mutation)";
		String msgString6 = "突變比率 (ratio Of Mutation)";
		String msgString7 = "迭代次數 (number Of offspring)";
		String msgStringComment = "(按下 Enter 後，載入原先 genePool.txt 建立好的資訊，\n如果不存在則在待會增加檔案，並且將最好的結果儲存)";

		Object[] array = { msgString1, msgString2, spinnerPoolSize, msgString3,
				spinnerPcrossover, msgString4, spinnerRcrossover, msgString5,
				spinnerPmutation, msgString6, spinnerRmutation, msgString7,
				spinnerIterator, msgStringComment };

		// Create an array specifying the number of dialog buttons
		// and their text.
		Object[] options = { btnString1, btnString2 };

		// Create the JOptionPane.
		optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[0]);

		// Make this dialog display it.
		setContentPane(optionPane);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				// textField.requestFocusInWindow();
			}
		});

		// Register an event handler that puts the text into the option pane.
		// textField.addActionListener(this);

		// Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}

			// Reset the JOptionPane's value.
			// If you don't do this, then if the user
			// presses the same button next time, no
			// property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (btnString1.equals(value)) {
				// typedText = textField.getText();g
				typedText = "Y";
				clearAndHide();
			} else { // user closed dialog or clicked cancel
				// dd.setLabel("It's OK.  " + "We won't force you to type "
				// + magicWord + ".");
				typedText = null;
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		// textField.setText(null);
		setVisible(false);
	}
}

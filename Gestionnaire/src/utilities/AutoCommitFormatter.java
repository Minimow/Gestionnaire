package utilities;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

/*
 * By using this formatter, every time the user write something in a field, you
 * can use a validator to check if the input is valid.
 */
public class AutoCommitFormatter extends DefaultFormatter {

	private static final long serialVersionUID = 1L;

	public AutoCommitFormatter() {
		this.setCommitsOnValidEdit(true);
		this.setOverwriteMode(false);
	}

	// Quand le textfield obtient le focus par un click, le caret se
	// positionne au bon endroit
	@Override
	public void install(final JFormattedTextField ftf) {
		int prevLen = ftf.getDocument().getLength();
		int savedCaretPos = ftf.getCaretPosition();
		super.install(ftf);
		if (ftf.getDocument().getLength() == prevLen) {
			ftf.setCaretPosition(savedCaretPos);
		}
	}
}
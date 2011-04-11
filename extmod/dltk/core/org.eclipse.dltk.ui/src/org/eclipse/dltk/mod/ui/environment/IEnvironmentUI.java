package org.eclipse.dltk.mod.ui.environment;

import org.eclipse.swt.widgets.Shell;

public interface IEnvironmentUI {
	public static final int DEFAULT = 0;
	public static final int EXECUTABLE = 1;
	public static final int ARCHIVE = 2;

	/**
	 * Open directory selection dialog. Dialog allow creation of new
	 * directories.
	 */
	String selectFolder(Shell shell);

	/**
	 * Open file selection dialog.
	 */
	String selectFile(Shell shell, int executable2);

}

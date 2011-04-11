package org.eclipse.dltk.mod.internal.ui.environment;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.mod.ui.environment.IEnvironmentUI;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class LocalEnvironmentUI implements IEnvironmentUI {

	public String selectFolder(Shell shell) {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		return dialog.open();
	}

	public String selectFile(Shell shell, int executable) {
		FileDialog dialog = new FileDialog(shell);
		if (executable == EXECUTABLE) {
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				dialog
						.setFilterExtensions(new String[] { "*.exe;*.bat;*.exe" }); //$NON-NLS-1$
			} else {
				dialog.setFilterExtensions(new String[] { "*" }); //$NON-NLS-1$
			}
			dialog.setFilterNames(new String[] { Messages.LocalEnvironmentUI_executables });
		}
		return dialog.open();
	}

}

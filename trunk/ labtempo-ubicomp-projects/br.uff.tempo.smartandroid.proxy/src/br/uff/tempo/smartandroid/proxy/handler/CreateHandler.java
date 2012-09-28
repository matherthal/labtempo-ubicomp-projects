package br.uff.tempo.smartandroid.proxy.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateHandler extends AbstractHandler {

	private static final String MY_CLASS = "IResourceAgent";
	private static final String SUPER_CLASS = "ResourceAgentStub";
	
	private Shell shell;
	private QualifiedName path = new QualifiedName("stubs", "path");

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		shell = HandlerUtil.getActiveShell(event);

		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;

		Object first = selection.getFirstElement();

		if (first instanceof ICompilationUnit) {

			ICompilationUnit comp = (ICompilationUnit) first;

			try {

				for (IType type : comp.getAllTypes()) {

					if (doesImplementResourceAgent(type)) {
						createProxy(comp);
					} else {
						MessageDialog.openInformation(shell, "Info",
								type.getElementName() + " must implement "
										+ MY_CLASS
										+ " to generate a SmartAndroid Proxy");
					}
				}

			} catch (JavaModelException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private void createProxy(ICompilationUnit comp) {
		createOutput(comp);
	}

	private void createOutput(ICompilationUnit cu) {
		String directory;
		IResource res = cu.getResource();
		boolean newDirectory = true;
		directory = getPersistentProperty(res, path);

		if (directory != null && directory.length() > 0) {
			newDirectory = !(MessageDialog.openQuestion(shell, "Question",
					"Use the previous output directory?"));
		}
		if (newDirectory) {
			DirectoryDialog fileDialog = new DirectoryDialog(shell);
			directory = fileDialog.open();

		}
		if (directory != null && directory.length() > 0) {
			setPersistentProperty(res, path, directory);
			write(directory, cu);
		}
	}

	protected String getPersistentProperty(IResource res, QualifiedName qn) {
		try {
			return res.getPersistentProperty(qn);
		} catch (CoreException e) {
			return "";
		}
	}

	protected void setPersistentProperty(IResource res, QualifiedName qn,
			String value) {
		try {
			res.setPersistentProperty(qn, value);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void write(String dir, ICompilationUnit cu) {
		try {
			
			String test = cu.getCorrespondingResource().getName();
			
			// Need
			String[] name = test.split("\\.");
			
			String className = name[0];
			
			if (className.charAt(0) == 'I' && Character.isUpperCase(className.charAt(1))) {
				className = className.substring(1);
			}
			
			className = className + "Stub";
			
			String htmlFile = dir + File.separator + className + ".java";
			
			FileWriter output = new FileWriter(htmlFile);
			BufferedWriter writer = new BufferedWriter(output);
			
			//write the necessary imports
			
			//Java File representing the Proxy (Stub)
			writer.write("public class " + className + " { extends " + SUPER_CLASS + " implements " + name[0] + "\n");
			
			//write the class methods;			
			
			writer.write("}");		
			
			writer.flush();
		} catch (JavaModelException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean doesImplementResourceAgent(IType type)
			throws JavaModelException {

		ITypeHierarchy th = type.newTypeHierarchy(null);
		IType[] types = th.getSuperInterfaces(type);

		for (IType t : types) {

			if (t.getElementName().equalsIgnoreCase(MY_CLASS)) {
				return true;
			}

			doesImplementResourceAgent(t);
		}

		return false;
	}
}

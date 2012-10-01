package br.uff.tempo.smartandroid.proxy.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateHandler extends AbstractHandler {

	private static final String MY_CLASS = "IResourceAgent";
	private static final String SUPER_CLASS = "ResourceAgentStub";

	private static final String[] STUB_IMPORTS = { "java.util.List",
			"java.util.ArrayList",
			"br.uff.tempo.middleware.comm.current.api.Tuple" };

	private static final Map<String, String> PRIMITIVE_TYPES = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				{
					put("int", "Integer");
					put("boolean", "Boolean");
					put("float", "Float");
					put("double", "Double");
					put("short", "Short");
					put("long", "Long");
					put("byte", "Byte");
					put("char", "Character");
				}
			});

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
						createProxy(comp, type);
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

	private void createProxy(ICompilationUnit comp, IType type) {
		createOutput(comp, type);
	}

	private void createOutput(ICompilationUnit cu, IType type) {
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
			write(directory, cu, type);
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

	private void write(String dir, ICompilationUnit cu, IType type) {
		try {

			String test = cu.getCorrespondingResource().getName();

			// Need
			String[] name = test.split("\\.");

			String className = name[0];

			if (className.charAt(0) == 'I'
					&& Character.isUpperCase(className.charAt(1))) {
				className = className.substring(1);
			}

			className = className + "Stub";

			String javaFile = dir + File.separator + className + ".java";

			FileWriter output = new FileWriter(javaFile);
			BufferedWriter writer = new BufferedWriter(output);

			StringBuilder str = new StringBuilder();
			// write the necessary imports
			str.append(createImports(cu));

			// Java File representing the Proxy (Stub)
			str.append("public class " + className + " extends " + SUPER_CLASS
					+ " implements " + name[0] + " {\n\n");

			// write the class methods;
			for (IMethod met : type.getMethods()) {

				// write method signature (return type, name, params-type and
				// params-name)
				str.append(createMethodDeclaration(met));

				// write the method body
				str.append(createMethodBody(met));
			}

			// end
			str.append("}\n");

			String source = str.toString();

//			// Formating source code (like ctrl + shif + F)
//			CodeFormatter cf = new DefaultCodeFormatter();
//
//			TextEdit te = cf.format(CodeFormatter.K_UNKNOWN, source, 0,
//					source.length(), 0, null);
//			IDocument doc = new Document(source);
//			
//			if (te != null) {
//				te.apply(doc);
//			}

			// Write the source code in the java file
			writer.write(source);
			writer.flush();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String createImports(ICompilationUnit cu) throws JavaModelException {

		StringBuilder str = new StringBuilder();

		for (String imp : STUB_IMPORTS) {
			str.append("import " + imp + ";\n");
		}

		for (IImportDeclaration id : cu.getImports()) {

			boolean declared = false;

			String imp = id.getElementName();

			for (String si : STUB_IMPORTS) {
				if (imp.equals(si)) {
					declared = true;
					break;
				}
			}

			if (!declared) {
				str.append("import " + imp + ";\n\n");
			}
		}

		return str.toString();
	}

	private String createMethodDeclaration(IMethod met)
			throws IllegalArgumentException, JavaModelException {

		String comma = "";
		StringBuilder str = new StringBuilder();

		str.append("\t@Override\n");

		str.append("\tpublic " + Signature.toString(met.getReturnType()) + " "
				+ met.getElementName() + "(");

		String[] parameterTypes = met.getParameterTypes();
		String[] parameterNames = met.getParameterNames();

		for (int i = 0; i < met.getParameterTypes().length; i++) {

			str.append(comma);
			str.append(Signature.toString(parameterTypes[i]));
			str.append(" ");
			str.append(parameterNames[i]);
			comma = ", ";
		}

		str.append(")");

		return str.toString();
	}

	private String createMethodBody(IMethod met)
			throws IllegalArgumentException, JavaModelException {

		StringBuilder str = new StringBuilder();

		str.append(" {\n");
		str.append("\t\tList<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();\n");

		String[] parameterTypes = met.getParameterTypes();
		String[] parameterNames = met.getParameterNames();

		for (int i = 0; i < met.getParameterTypes().length; i++) {

			str.append("\t\tparams.add(new Tuple<String, Object>(");

			// key
			str.append("\"" + Signature.toString(parameterTypes[i]) + "\"");
			str.append(", ");
			// value
			str.append(parameterNames[i]);

			str.append("));\n");
		}

		String returnType = Signature.toString(met.getReturnType());

		str.append("\n\t\t");
		
		if (!returnType.equalsIgnoreCase("void")) {
			str.append("return ");

			// if the return type is a primitive data type, gets its wrapper
			// class
			if (PRIMITIVE_TYPES.containsKey(returnType)) {
				returnType = PRIMITIVE_TYPES.get(returnType);
			}

			str.append("(" + returnType + ") ");
		}

		str.append("makeCall(\"" + met.getElementName() + "\", params);\n");

		// end method body
		str.append("\t}\n\n");

		return str.toString();
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

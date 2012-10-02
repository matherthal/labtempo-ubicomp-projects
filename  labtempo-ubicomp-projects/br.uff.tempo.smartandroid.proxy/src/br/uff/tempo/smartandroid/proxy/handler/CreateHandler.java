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
import org.eclipse.core.resources.ResourcesPlugin;
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
			"br.uff.tempo.middleware.comm.current.api.Tuple",
			"br.uff.tempo.middleware.management.stubs.ResourceAgentStub" };

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

					// TODO Review this method. It doesn't get the interface if
					// it extends a class that extends 'MY_CLASS'
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

		write(comp, type);
	}

	private void write(ICompilationUnit cu, IType type) {
		try {

			String dir = cu.getPath().makeAbsolute().toOSString();
			String workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation().toOSString();

			String parsedPath = splitAndConcat(dir, File.separator,
					File.separator, "stubs");

			String location = workspaceLocation + parsedPath;

			String test = cu.getCorrespondingResource().getName();

			// Need
			String[] name = test.split("\\.");

			// the pattern is -> an interface starts with 'I'.
			// if there is an 'I' char followed by an upper-case letter
			// ignore this 'I'

			String className = name[0];

			if (className.charAt(0) == 'I'
					&& Character.isUpperCase(className.charAt(1))) {
				className = className.substring(1);
			}

			className = className + "Stub";

			String javaFile = location + File.separator + className + ".java";

			// create the directory
			(new File(location)).mkdir();

			FileWriter output = new FileWriter(javaFile);
			BufferedWriter writer = new BufferedWriter(output);

			// it will be concat the expressions to create the Java file
			StringBuilder str = new StringBuilder();

			String interfaceFullName = type
					.getFullyQualifiedParameterizedName();

			// wirte the package declaration
			str.append(createPackage(interfaceFullName));

			// write the necessary imports
			str.append(createImports(cu, interfaceFullName));

			// write the class signature
			str.append("public class " + className + " extends " + SUPER_CLASS
					+ " implements " + name[0] + " {\n\n");

			// write the fields
			str.append("\tprivate static final long serialVersionUID = 1L;\n\n");

			// write the constructor method
			str.append(createConstructor(className));

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

			// Write the source code in the java file
			writer.write(source);
			writer.flush();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String createPackage(String interfaceFullName)
			throws JavaModelException {

		StringBuilder str = new StringBuilder();

		str.append("package ");

		str.append(splitAndConcat(interfaceFullName, "\\.", ".", "stubs;\n\n"));

		return str.toString();
	}

	private String createImports(ICompilationUnit cu, String interfaceFullName)
			throws JavaModelException {

		StringBuilder str = new StringBuilder();

		// imports for the stub stuff
		for (String imp : STUB_IMPORTS) {
			str.append("import " + imp + ";\n");
		}

		// import for interface implemented
		str.append("import " + interfaceFullName + ";\n");

		// imports from the interface implemented
		for (IImportDeclaration id : cu.getImports()) {

			//check for duplicates
			boolean declared = false;

			String imp = id.getElementName();

			for (String si : STUB_IMPORTS) {
				if (imp.equals(si) || imp.contains("IResourceAgent")) {
					declared = true;
					break;
				}
			}

			if (!declared) {
				str.append("import " + imp + ";\n");
			}
		}

		str.append("\n");

		return str.toString();
	}

	private String createConstructor(String className) {

		StringBuilder str = new StringBuilder();

		str.append("\tpublic " + className + " (String rai) {\n");
		str.append("\t\tsuper(rai);\n");
		str.append("\t}\n\n");

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
			str.append(Signature.toString(parameterTypes[i])
					+ ".class.getName()");
			str.append(", ");
			// value
			str.append(parameterNames[i]);

			str.append("));\n");
		}

		String returnType = Signature.toString(met.getReturnType());

		str.append("\n\t\t");

		String retClass = "";

		if (!returnType.equalsIgnoreCase("void")) {
			str.append("return ");

			// if the return type is a primitive data type, gets its wrapper
			// class
			if (PRIMITIVE_TYPES.containsKey(returnType)) {
				returnType = PRIMITIVE_TYPES.get(returnType);
			}

			str.append("(" + returnType + ") ");

			retClass = ", " + returnType + ".class";
		}

		str.append("makeCall(\"" + met.getElementName() + "\", params"
				+ retClass + ");\n");

		// end method body
		str.append("\t}\n\n");

		return str.toString();
	}

	private String splitAndConcat(String unparsedStr, String splitSeparator,
			String concatSeparator, String end) {

		StringBuilder str = new StringBuilder();
		
		if (splitSeparator.equals("\\")) {
			splitSeparator = "\\" + splitSeparator;
		}

		String[] separedStr = unparsedStr.split(splitSeparator);

		int tam = separedStr.length - 2;

		if (!separedStr[separedStr.length - 2].equalsIgnoreCase("interfaces")) {
			tam++;
		}

		for (int i = 0; i < tam; i++) {

			str.append(separedStr[i] + concatSeparator);
		}

		str.append(end);

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

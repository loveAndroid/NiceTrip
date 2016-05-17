package com.azir.anno;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.azir.anno.ViewInject")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ViewProcessor extends AbstractProcessor {

	private Elements elementUtils;
	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		elementUtils = processingEnv.getElementUtils();
		messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ViewInject.class);

		Map<TypeElement, ArrayList<VariableElement>> maps = new HashMap<>();

		// TypeElement classElement = null;// class
		// List<VariableElement> variableElements = new ArrayList<>();

		if (elements == null || elements.size() == 0) {
			return false;
		}
		
		for (Element ele : elements) {
			ElementKind kind = ele.getKind();
			if (kind == ElementKind.CLASS) {
				TypeElement classElement = (TypeElement) ele;
				maps.put(classElement, new ArrayList<VariableElement>());
			}
		}

		for (Element ele : elements) {
			ElementKind kind = ele.getKind();
			if (kind == ElementKind.FIELD) {
				VariableElement variableElement = (VariableElement) ele;
				TypeElement cls = (TypeElement) ele.getEnclosingElement();
				
				ArrayList<VariableElement> list = maps.get(cls);
				String pkgClsName = cls.getQualifiedName().toString();
				String fieldName = variableElement.getSimpleName().toString();
				
				messager.printMessage(
						Kind.NOTE,
						" kind class ....classElement.getQualifiedName() = " + pkgClsName + "fieldname = " + fieldName);
				list.add(variableElement);
			}
		}

		try {
			generateCode(maps);
		} catch (Exception e) {
		}
		return true;
	}

	public void generateCode(Map<TypeElement, ArrayList<VariableElement>> maps) throws Exception {

		if (maps == null || maps.size() == 0) {
			return;
		}

		Set<Entry<TypeElement, ArrayList<VariableElement>>> set = maps.entrySet();

		for (Iterator<Entry<TypeElement, ArrayList<VariableElement>>> iter = set.iterator(); iter.hasNext();) {

			Entry<TypeElement, ArrayList<VariableElement>> next = iter.next();

			TypeElement clsElement = next.getKey();
			ArrayList<VariableElement> variableElements = next.getValue();
			
			generate(clsElement, variableElements);

		}

	}

	private void generate(TypeElement clsElement, ArrayList<VariableElement> variableElements) throws IOException {
		
		PackageElement packageElement = elementUtils.getPackageOf(clsElement);
		String pkgName = packageElement.getQualifiedName().toString();
		String clsName = clsElement.getSimpleName().toString();
		
		String[] split = clsName.split("_");

		String originClassName = split[split.length - 1];
		clsName = originClassName + "Proxy";

		generate(clsElement, variableElements, pkgName, clsName, originClassName);
	}

	private void generate(TypeElement clsElement, ArrayList<VariableElement> variableElements, String pkgName,
			String clsName, String originClassName) throws IOException {
		Filer filer = processingEnv.getFiler();
		JavaFileObject fileObject = filer.createSourceFile(pkgName + "." + clsName, clsElement);

		Writer writer = fileObject.openWriter();

		writer.write("/////////generate by viewinject do not modify it///////// \n\n\n");

		writer.write("package " + pkgName + ";");
		writer.write("\n\n");

		writer.write("import com.azir.anno.Injecter;\n");
		writer.write("import android.view.View;\n");
		writer.write("import " + pkgName + "." + originClassName + ";\n");
		writer.write("import com.azir.anno.InjectUtils;\n\n");

		writer.write("public class " + clsName + "<T extends " + originClassName + ">"
				+ " implements Injecter<T> { \n");

		writer.write("\t public " + clsName + "() { \n\n");
		writer.write("\t}\n\n");

		writer.write("\t@Override\n");
		writer.write("\tpublic void inject(T target,View view) { \t\n");

		generate(variableElements, writer);

		writer.write("\t}\n");
		writer.write("}");

		writer.flush();
		writer.close();
	}

	private void generate(ArrayList<VariableElement> variableElements, Writer writer) throws IOException {
		for (VariableElement e : variableElements) {

			ViewInject viewInject = e.getAnnotation(ViewInject.class);
			int viewId = viewInject.viewId();

			String fieldName = e.getSimpleName().toString();
			String fieldType = e.asType().toString();

			writer.write("\t\t" + "target." + fieldName + " = InjectUtils.castView(view.findViewById(" + viewId
					+ "));\n");
		}
	}

}

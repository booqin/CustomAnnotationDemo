package com.boqin.customannotation.codegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.boqin.customannotation.BQConstant;
import com.boqin.customannotation.CustomAnnotation;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * TODO
 * Created by Boqin on 2017/3/31.
 * Modified by Boqin
 *
 * @Version
 */
@SupportedAnnotationTypes("com.boqin.customannotation.CustomAnnotation")
public class CustomAnnotationProcessor extends AbstractProcessor{

    public static final String TARGET = "target";
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        String methodName = null;
        String packageName = null;
        String simpleName = null;
        TypeName typeName = null;

        for (Element element : roundEnv.getElementsAnnotatedWith(CustomAnnotation.class)) {
            methodName = element.getSimpleName().toString();

            if (element.getEnclosingElement() instanceof PackageElement) {
                ((PackageElement) element.getEnclosingElement()).getQualifiedName();
            }
            Element e = element;
            while (!(e.getEnclosingElement() instanceof PackageElement)) {
                e = e.getEnclosingElement();
            }
            if (e.getEnclosingElement() instanceof PackageElement) {
                packageName = ((PackageElement) e.getEnclosingElement()).getQualifiedName().toString();  //获取包名
                simpleName = e.getSimpleName().toString();    //获取类名
                typeName = TypeName.get(e.asType());
            }

        }
        if (typeName!=null) {
            JavaFile javaFile = JavaFile.builder(packageName, createTypeSpec(simpleName, methodName, typeName, TARGET)).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private TypeSpec createTypeSpec(String simpleName, String methodName, TypeName typeName, String targer) {
        return TypeSpec.classBuilder(simpleName + BQConstant.CLASS_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(createConstructor(methodName, typeName, targer))
                .addMethods(createWithCheckMethods(methodName, typeName, targer))
                .build();
    }

    private MethodSpec createConstructor(String method, TypeName typeName, String targer) {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(typeName, targer)
                .addCode(CodeBlock.builder().add("\u0024N(\u0024N", method, targer).addStatement(")").build()).build();
    }

    private List<MethodSpec> createWithCheckMethods(String method, TypeName typeName, String targer){
        MethodSpec methodSpec = MethodSpec.methodBuilder(method).addModifiers(Modifier.PUBLIC).addParameter(typeName, targer)
                .addCode(CodeBlock.builder().add("\u0024N.\u0024N(", targer, method).addStatement(")").build()).build();
        List<MethodSpec> list = new ArrayList<>();
        list.add(methodSpec);
        return list;
    }

}

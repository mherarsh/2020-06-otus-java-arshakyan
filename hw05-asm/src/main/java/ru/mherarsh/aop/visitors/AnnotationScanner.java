package ru.mherarsh.aop.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import ru.mherarsh.aop.dao.MethodDescription;

import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner extends ClassVisitor {
    private final List<MethodDescription> annotatedMethods = new ArrayList<>();
    private final String annotation;

    public AnnotationScanner(int api, String annotation) {
        super(api);
        this.annotation = annotation;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        super.visitMethod(access, name, desc, signature, exceptions);
        return new MethodAnnotationScanner(api, name, desc);
    }

    public List<MethodDescription> getAnnotatedMethods() {
        return annotatedMethods;
    }

    private class MethodAnnotationScanner extends MethodVisitor {
        private final String methodName;
        private final String methodDescription;

        public MethodAnnotationScanner(int api, String methodName, String methodDescription) {
            super(api);
            this.methodName = methodName;
            this.methodDescription = methodDescription;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(annotation)) {
                annotatedMethods.add(new MethodDescription(methodName, methodDescription));
            }

            return super.visitAnnotation(descriptor, visible);
        }
    }
}

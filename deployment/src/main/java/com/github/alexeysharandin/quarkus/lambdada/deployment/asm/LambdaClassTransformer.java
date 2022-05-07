package com.github.alexeysharandin.quarkus.lambdada.deployment.asm;

import com.github.alexeysharandin.quarkus.lambdada.invoke.LambdaMetafactoryDecorator;
import org.jboss.logging.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


@SuppressWarnings("unused")
public class LambdaClassTransformer extends ClassVisitor {
    private static final Logger LOGGER = Logger.getLogger(LambdaClassTransformer.class);

    private static final String LAMBDA_METAFACTORY_CLASS_NAME = "java/lang/invoke/LambdaMetafactory";
    private static final String LAMBDA_METAFACTORY_METHOD_NAME = "metafactory";
    private static final String LAMBDA_METAFACTORY_DECORATOR_NAME =
            LambdaMetafactoryDecorator.class.getName().replace(".", "/");

    private final ClassVisitor cv;
    private final String className;

    public LambdaClassTransformer(ClassVisitor visitor, String className) {
        super(Opcodes.ASM9, visitor);
        this.className = className;
        this.cv = visitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new LambdaMethodVisitor(className, cv.visitMethod(access, name, descriptor, signature, exceptions));
    }

    private static class LambdaMethodVisitor extends MethodVisitor {
        private final String className;

        public LambdaMethodVisitor(String className, MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
            this.className = className;
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
            if (bootstrapMethodHandle.getOwner().equals(LAMBDA_METAFACTORY_CLASS_NAME)
                    && bootstrapMethodHandle.getName().equals(LAMBDA_METAFACTORY_METHOD_NAME)
            ) {
                processLambda(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
            } else {
                super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
            }
        }

        private void processLambda(String name, String descriptor, Handle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
            LOGGER.debug("Process lambda for class " + className);

            Handle handle = new Handle(
                    bootstrapMethodHandle.getTag(),
                    LAMBDA_METAFACTORY_DECORATOR_NAME,
                    bootstrapMethodHandle.getName(),
                    bootstrapMethodHandle.getDesc(),
                    bootstrapMethodHandle.isInterface()
            );

            super.visitInvokeDynamicInsn(name, descriptor, handle, bootstrapMethodArguments);
        }
    }


}

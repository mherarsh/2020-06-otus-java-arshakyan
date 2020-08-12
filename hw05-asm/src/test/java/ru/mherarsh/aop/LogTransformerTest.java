package ru.mherarsh.aop;

import org.junit.jupiter.api.*;
import org.objectweb.asm.ClassReader;
import ru.mherarsh.aop.utils.AsmUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.invoke.*;
import java.lang.reflect.Method;

class LogTransformerTest {
    private static final String CLASS_NAME = "ru.mherarsh.demo.TestLogging";
    private static Class<?> clazz;
    private static PrintStream defaultSystemOut;
    private static ByteArrayOutputStream tetBuffer;
    private Object testObject;

    @BeforeAll
    static void init() throws IOException {
        configSystemOutBuffer();
        initClassTransformation();
    }

    @AfterAll
    private static void resetOut() {
        System.setOut(defaultSystemOut);
    }

    @BeforeEach
    void createInstance() throws Exception {
        var constructor = clazz.getConstructor();
        testObject = constructor.newInstance();
    }

    @AfterEach
    private void resetBuffer() {
        tetBuffer.reset();
    }

    @Test
    void addTest() throws Throwable {
        var method = clazz.getMethod("add", int.class, int.class, int.class);

        method.invoke(testObject, 1, 2, 3);

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var logMessage = getLogMessageInvoker(method).invokeExact(1, 2, 3).toString();

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }

    @Test
    void sumTest() throws Throwable {
        var method = clazz.getMethod("sum", int.class, int.class);
        method.invoke(testObject, 10, 20);

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var logMessage = getLogMessageInvoker(method).invokeExact(10, 20).toString();

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }

    @Test
    void printHelloTest() throws Throwable {
        var method = clazz.getMethod("printHello");
        method.invoke(testObject);

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var logMessage = getLogMessageInvoker(method).invokeExact().toString();

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }

    @Test
    void printTextTest() throws Throwable {
        var method = clazz.getMethod("printText", String.class, String.class, int.class, double.class);
        method.invoke(testObject, "1", "2", 3, 4d);

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var logMessage = getLogMessageInvoker(method).invokeExact("1", "2", 3, 4d).toString();

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }

    @Test
    void returnSomeObjectTest() throws Throwable {
        var method = clazz.getMethod("returnSomeObject");
        method.invoke(testObject);

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var logMessage = getLogMessageInvoker(method).invokeExact().toString();

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }

    private MethodHandle getLogMessageInvoker(Method method) throws StringConcatException {
        var recipe = AsmUtils.getRecipe(method.getName(), method.getParameterCount());

        return StringConcatFactory.makeConcatWithConstants(
                MethodHandles.lookup(),
                "makeConcatWithConstants",
                MethodType.methodType(Object.class, method.getParameterTypes()), recipe)
                .dynamicInvoker();
    }

    private static void initClassTransformation() throws IOException {
        var bytecode = LogTransformer.transform(new ClassReader(CLASS_NAME));

        clazz = new ClassLoader() {
            Class<?> defineClass(byte[] bytecode) {
                return super.defineClass(CLASS_NAME, bytecode, 0, bytecode.length);
            }
        }.defineClass(bytecode);
    }

    private static void configSystemOutBuffer() {
        defaultSystemOut = System.out;
        tetBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(new BufferedOutputStream(tetBuffer)));
    }

    /*
    //Не работает, почему?
    @Test
    void addTest() throws Throwable {
        var method = clazz.getMethod("add", int.class, int.class, int.class);
        callMethodTest(method, 1, 2, 3);
    }

    void callMethodTest(Method method, Object... args) throws Throwable {
        method.invoke(testObject, args); //<<<--- работает

        System.out.flush();
        var logMessageFromSOut = tetBuffer.toString();

        var recipe = AsmUtils.getRecipe(method.getName(), method.getParameterCount());
        var logMessage = StringConcatFactory.makeConcatWithConstants(
                MethodHandles.lookup(),
                "makeConcatWithConstants",
                MethodType.methodType(Object.class, method.getParameterTypes()), recipe)
                .dynamicInvoker().invokeExact(args).toString();  //<<<--- не работает

        Assertions.assertEquals(logMessageFromSOut.trim(), logMessage.trim());
    }*/
}

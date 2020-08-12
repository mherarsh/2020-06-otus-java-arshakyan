#### ДЗ: Автоматическое логирование

**Цель**  
Понять как реализуется AOP, какие для этого есть технические средства.

**Разработайте такой функционал**  
метод класса можно пометить самодельной аннотацией @Log, например, так:

_class TestLogging {  
@Log
public void calculation(int param) {};  
}_

При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.  
Например так.

_class Demo {  
public void action() {  
new TestLogging().calculation(6);  
}}_

В консоле дожно быть:
executed method: calculation, param: 6

**Обратите внимание:** явного вызова логирования быть не должно.

Учтите, что аннотацию можно поставить, например, на такие методы:  
public void calculation(int param1)  
public void calculation(int param1, int param2)  
public void calculation(int param1, int param2, String param3)  

**P.S.**  
Для запуска вызвать следующую команду: gradlew hw05-asm:build & java -javaagent:hw05-asm\build\libs\hw05-asm-0.1.jar -jar hw05-asm\build\libs\hw05-asm-0.1.jar

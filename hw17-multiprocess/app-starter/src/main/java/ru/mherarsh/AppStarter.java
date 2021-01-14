package ru.mherarsh;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AppStarter {
    private static final String JAVA_CMD = "java";
    private static final String GRADLE_CMD = "gradlew.bat";
    private static final String SERVER_HEALTH_URL = "http://localhost:8080/actuator/health";

    public static void main(String[] args) throws Exception {
        System.out.println("Start building...");

        execProcess(GRADLE_CMD, "hw17-multiprocess:message-server:bootJar").waitFor(5, TimeUnit.MINUTES);
        execProcess(GRADLE_CMD, "hw17-multiprocess:db-server:bootJar").waitFor(5, TimeUnit.MINUTES);
        execProcess(GRADLE_CMD, "hw17-multiprocess:frontend:bootJar").waitFor(5, TimeUnit.MINUTES);

        System.out.println("Start server and clients...");

        var process = execProcess(JAVA_CMD, "-jar", "build/message-server/message-server.jar");

        var serverIsStarted = waitToStart(process);

        if (!serverIsStarted) {
            System.out.println("Can't start server");
        }

        execProcess(JAVA_CMD, "-jar", "build/db-server/db-server.jar");
        execProcess(JAVA_CMD, "-jar", "build/frontend/frontend.jar", "--spring.config.location=hw17-multiprocess/application-front1.yaml");
        execProcess(JAVA_CMD, "-jar", "build/frontend/frontend.jar", "--spring.config.location=hw17-multiprocess/application-front12.yaml");
        execProcess(JAVA_CMD, "-jar", "build/frontend/frontend.jar", "--spring.config.location=hw17-multiprocess/application-front2.yaml");
    }

    private static Process execProcess(String... parameter) throws IOException {
        return new ProcessBuilder(parameter)
                .directory(new File("./"))
                .inheritIO()
                .start();
    }

    private static boolean waitToStart(Process process) {
        var future = CompletableFuture.supplyAsync(() -> {
            var response = getHttpResponse();

            while (response == null || response.statusCode() != 200) {
                response = getHttpResponse();
                System.out.println("waiting for process...");
            }

            return true;
        })
                .orTimeout(1, TimeUnit.MINUTES)
                .exceptionally(throwable -> {
                    process.destroyForcibly();
                    return false;
                });

        future.join();
        return future.getNow(false);
    }

    private static HttpResponse<String> getHttpResponse() {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create(SERVER_HEALTH_URL)).build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }
    }
}

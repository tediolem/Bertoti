import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Main {
    public static void main(String[] args) throws Exception {

        String json = """
                {
                    "model": "gemma4:e2b",
                    "prompt": "Os titulos intercontinentais do santos futebol clube conquistados em 1962 e 1963, podem ser denominados como t[itulos mundiais? E quais as datas exatas da conquista dos titulos? responda de maneira enxuta evitando todo tipo de alucinação e informação desnecessária.",
                    "stream": false
                }
                """;

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMinutes(10))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("Enviando para Ollama...");

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());

        // Lê o JSON retornado pelo Ollama
        ObjectMapper mapper = new ObjectMapper();
        JsonNode respostaJson = mapper.readTree(response.body());

        // Pega somente o conteúdo da resposta da IA
        String resposta = respostaJson.get("response").asText();

        System.out.println("Resposta:");
        System.out.println(resposta);
    }
}
package gov.iti.jets.client.model;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatbotService {
    private static final String API_URL = "https://api-inference.huggingface.co/models/facebook/blenderbot-3B";
    private static final String ACCESS_TOKEN = "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";

    public static String getChatbotResponse(String userMessage) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(API_URL);
            request.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            request.setHeader("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("inputs", userMessage);

            request.setEntity(new StringEntity(json.toString()));

            try (CloseableHttpResponse response = client.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());


                System.out.println("Response Body: " + responseBody);


                if (responseBody.isEmpty() || !responseBody.startsWith("[")) {
                    return "mmm...sorry but i don't get what you are saying";
                }


                JSONArray jsonResponse = new JSONArray(responseBody);

                // Extract the generated text from the array
                String botResponse = jsonResponse.getJSONObject(0).getString("generated_text");

                return botResponse;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to connect to chatbot service.";
        }






}






















}

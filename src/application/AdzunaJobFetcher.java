package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdzunaJobFetcher {
    public static void main(String[] args) {
        String appId = "e401c888"; // Replace with your Adzuna App ID
        String appKey = "c01f20adfbb0b8ab63c037a69daf8add"; // Replace with your Adzuna App Key
        String country = "us"; // e.g., "us", "gb", "in"
        String what = "java developer";
        String urlString = String.format(
            "https://api.adzuna.com/v1/api/jobs/%s/search/1?app_id=%s&app_key=%s&what=%s",
            country, appId, appKey, what.replace(" ", "%20")
        );

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Parse JSON and print job details
            JSONObject json = new JSONObject(content.toString());
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject job = results.getJSONObject(i);
                String title = job.optString("title", "N/A");
                String company = job.has("company") ? job.getJSONObject("company").optString("display_name", "N/A") : "N/A";
                String location = job.has("location") ? job.getJSONObject("location").optString("display_name", "N/A") : "N/A";
                String description = job.optString("description", "N/A");
                String urlJob = job.optString("redirect_url", "N/A");
                double salaryMin = job.optDouble("salary_min", 0);
                double salaryMax = job.optDouble("salary_max", 0);
                System.out.println("Job Title: " + title);
                System.out.println("Company: " + company);
                System.out.println("Location: " + location);
                System.out.println("Salary: $" + salaryMin + " - $" + salaryMax);
                System.out.println("Description: " + description.substring(0, Math.min(100, description.length())) + "...");
                System.out.println("URL: " + urlJob);
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
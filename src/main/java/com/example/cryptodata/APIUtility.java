package com.example.cryptodata;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class APIUtility {
    /**
     * This method gets coins from API and stores it into local file in root directory
     * @param searchText - can accept any coin name or symbol
     * @param orderBy - accepts only price, marketCap, 24hVolume, change, listedAt
     * @throws IOException
     * @throws InterruptedException
     */
    public static APIResponse getCoinsFromAPI(String searchText, String orderBy) throws IOException, InterruptedException {
        searchText = searchText.replaceAll(" ","%20");
        String uri = "https://coinranking1.p.rapidapi.com/coins/?rapidapi-key=2cdf14a81emshb4221322bfabc0ap110dccjsn16362add46d0&orderBy="+ orderBy + "&search=" + searchText;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        APIResponse apiResponse = gson.fromJson(httpResponse.body(),APIResponse.class);

        return apiResponse;
    }
}

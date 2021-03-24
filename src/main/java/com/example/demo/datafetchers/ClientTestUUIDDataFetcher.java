package com.example.demo.datafetchers;

import com.example.demo.generated.DgsConstants;
import com.example.demo.generated.client.TestClientUUIDGraphQLQuery;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.client.DefaultGraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.HttpResponse;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.UUID;

@DgsComponent
public class ClientTestUUIDDataFetcher {
    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = "testClientUUID")
    public UUID testClientStr(@InputArgument("test") UUID test) {
        return test;
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = "testUUID")
    public UUID incorrectMapping() {
        return getData();
    }

    public UUID getData() {
        RestTemplate dgsRestTemplate = new RestTemplate();

        DefaultGraphQLClient graphQLClient = new DefaultGraphQLClient("http://localhost:8080/graphql");
        GraphQLResponse response = graphQLClient.executeQuery(buildQuery(), new HashMap<>(), (url, headers, body) -> {
            /**
             * The requestHeaders providers headers typically required to call a GraphQL endpoint, including the Accept and Content-Type headers.
             * To use RestTemplate, the requestHeaders need to be transformed into Spring's HttpHeaders.
             */
            HttpHeaders requestHeaders = new HttpHeaders();
            headers.forEach(requestHeaders::put);

            /**
             * Use RestTemplate to call the GraphQL service.
             * The response type should simply be String, because the parsing will be done by the GraphQLClient.
             */
            ResponseEntity<String> exchange = dgsRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity(body, requestHeaders), String.class);

            /**
             * Return a HttpResponse, which contains the HTTP status code and response body (as a String).
             * The way to get these depend on the HTTP client.
             */
            return new HttpResponse(exchange.getStatusCodeValue(), exchange.getBody());
        });

        // prevent extraction that hides the real error
        if(response.hasErrors()) {
            System.out.println(response.getErrors());
            return null;
        }

        UUID test = response.extractValueAsObject("testClient", UUID.class);
        return UUID.fromString(test.toString());
    }

    private String buildQuery() {
        GraphQLQueryRequest graphQLQueryRequest =
                new GraphQLQueryRequest(
                        new TestClientUUIDGraphQLQuery.Builder()
                        .test(UUID.randomUUID()).build()
                );
        return graphQLQueryRequest.serialize();
    }
}

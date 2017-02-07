package de.otto.flummi.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import de.otto.flummi.domain.index.Index;
import de.otto.flummi.util.HttpClientWrapper;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static de.otto.flummi.RequestBuilderUtil.buildUrl;
import static de.otto.flummi.RequestBuilderUtil.toHttpServerErrorException;
import static org.slf4j.LoggerFactory.getLogger;

public class IndexRequestBuilder implements RequestBuilder<Void> {
    public static final Logger LOG = getLogger(IndexRequestBuilder.class);
    private final Gson gson;
    private JsonPrimitive id;
    private String indexName;
    private String documentType;
    private String jsonBody;
    private Map<String, Object> queryParams;
    private HttpClientWrapper httpClient;
    private Index index;

    public IndexRequestBuilder(HttpClientWrapper httpClient) {
        this.httpClient = httpClient;
        this.gson = new Gson();
        this.queryParams = new HashMap<>();
    }

    public IndexRequestBuilder setId(String id) {
        this.id = new JsonPrimitive(id);
        return this;
    }

    public IndexRequestBuilder setId(int id) {
        this.id = new JsonPrimitive(id);
        return this;
    }

    public IndexRequestBuilder setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public IndexRequestBuilder setDocumentType(String documentType) {
        this.documentType = documentType;
        return this;
    }

    @Deprecated
    public IndexRequestBuilder setSource(JsonObject source) {
        this.jsonBody = gson.toJson(source);
        return this;
    }

    public IndexRequestBuilder setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
        return this;
    }

    public IndexRequestBuilder setIndex(Index index) {
        this.index = index;
        return this;
    }

    public IndexRequestBuilder setParent(Object parent) {
        return addQueryParam("parent", parent);
    }

    public IndexRequestBuilder addQueryParam(String key, Object value) {
        this.queryParams.put(key, value);
        return this;
    }

    @Override
    public Void execute() {
        if (jsonBody == null) {
            if (index == null) {
                throw new IllegalStateException("either jsonBody or indexSettings must exist");
            }
        } else {
            if (index != null) {
                throw new IllegalStateException("either jsonBody or indexSettings is allowed to specified");
            }
        }
        try {
            AsyncHttpClient.BoundRequestBuilder reqBuilder;
            if (id != null) {
                String url = buildUrl(indexName, documentType, URLEncoder.encode(id.getAsString(), "UTF-8"));
                reqBuilder = httpClient.preparePut(url);
            } else {
                String url = buildUrl(indexName, documentType);
                reqBuilder = httpClient.preparePost(url);
            }

            queryParams
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null)
                    .forEach(entry -> reqBuilder.addQueryParam(entry.getKey(), String.valueOf(entry.getValue())));

            String body = createBody();
            Response response = reqBuilder.setBody(body).setBodyEncoding("UTF-8").execute().get();
            if (response.getStatusCode() >= 300) {
                throw toHttpServerErrorException(response);
            }
            return null;
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String createBody() {
        if (jsonBody != null) {
            return jsonBody;
        }
        if (index != null) {
            return gson.toJson(index);
        }
        throw new IllegalStateException();
    }
}

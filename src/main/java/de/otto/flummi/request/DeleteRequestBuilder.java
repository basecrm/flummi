package de.otto.flummi.request;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import de.otto.flummi.RequestBuilderUtil;
import de.otto.flummi.util.HttpClientWrapper;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static de.otto.flummi.RequestBuilderUtil.buildUrl;
import static org.slf4j.LoggerFactory.getLogger;

public class DeleteRequestBuilder implements RequestBuilder<Void> {
    private final HttpClientWrapper httpClient;
    private String indexName;
    private String documentType;
    private String id;
    private Map<String, Object> queryParams;

    public static final Logger LOG = getLogger(DeleteRequestBuilder.class);

    public DeleteRequestBuilder(HttpClientWrapper httpClient) {
        this.httpClient = httpClient;
        this.queryParams = new HashMap<>();
    }

    public DeleteRequestBuilder setIndexName(final String indexName) {
        this.indexName = indexName;
        return this;
    }

    public DeleteRequestBuilder setDocumentType(final String documentType) {
        this.documentType = documentType;
        return this;
    }

    public DeleteRequestBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public DeleteRequestBuilder setParent(Object parent) {
        return addQueryParam("parent", parent);
    }

    public DeleteRequestBuilder setRouting(Object routing) {
        return addQueryParam("routing", routing);
    }

    public DeleteRequestBuilder addQueryParam(String key, Object value) {
        this.queryParams.put(key, value);
        return this;
    }

    public Void execute() {
        try {
            if (indexName==null || indexName.isEmpty()) {
                throw new RuntimeException("missing property 'indexName'");
            }
            if (documentType==null || documentType.isEmpty()) {
                throw new RuntimeException("missing property 'type'");
            }
            if (id==null || id.isEmpty()) {
                throw new RuntimeException("missing property 'id'");
            }
            final String url = buildUrl(indexName, documentType, URLEncoder.encode(id, "UTF-8"));
            final AsyncHttpClient.BoundRequestBuilder reqBuilder = httpClient.prepareDelete(url);

            queryParams
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null)
                    .forEach(entry -> reqBuilder.addQueryParam(entry.getKey(), String.valueOf(entry.getValue())));

            Response response = reqBuilder.execute().get();
            if (response.getStatusCode() >= 300) {
                throw RequestBuilderUtil.toHttpServerErrorException(response);
            }
            return null;
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

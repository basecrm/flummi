package de.otto.flummi.response;

import com.google.gson.JsonObject;

public class SearchHit {

    private final String id;
    private final JsonObject source;
    private JsonObject fields;
    private final Float score;
    private final Long version;
    private final String parent;
    private final String routing;
    private final String type;

    public SearchHit(final String id, final JsonObject source, final JsonObject fields, final Float score,
                     final Long version, final String parent, final String routing, final String type) {
        this.id = id;
        this.source = source;
        this.fields = fields;
        this.score = score;
        this.version = version;
        this.parent = parent;
        this.routing = routing;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public JsonObject getSource() {
        return source;
    }

    public Float getScore() {
        return score;
    }

    public JsonObject getFields() {
        return fields;
    }

    public Long getVersion() {
        return version;
    }

    public String getParent() {
        return parent;
    }

    public String getRouting() {
        return routing;
    }

    public String getType() {
        return type;
    }
}

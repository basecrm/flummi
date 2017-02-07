package de.otto.flummi.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BoolQueryBuilder implements QueryBuilder {
    private JsonArray mustFilter = new JsonArray();
    private JsonArray mustNotFilter = new JsonArray();
    private JsonArray shouldFilter = new JsonArray();

    @Override
    public JsonObject build() {
        if (mustFilter.size() == 0 && mustNotFilter.size() == 0 && shouldFilter.size() == 0) {
            throw new RuntimeException("mustFilter, mustNotFilter, and should filters are empty");
        }

        JsonObject boolObject = new JsonObject();
        addBoolFilter(boolObject, "must", mustFilter);
        addBoolFilter(boolObject, "must_not", mustNotFilter);
        addBoolFilter(boolObject, "should", shouldFilter);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("bool", boolObject);

        return jsonObject;
    }

    public boolean isEmpty() {
        return mustFilter.size() == 0 && mustNotFilter.size() == 0;
    }

    public BoolQueryBuilder must(JsonObject filter) {
        this.mustFilter.add(filter);
        return this;
    }

    public BoolQueryBuilder mustNot(JsonObject filter) {
        this.mustNotFilter.add(filter);
        return this;
    }

    public BoolQueryBuilder must(QueryBuilder queryBuilder) {
        must(queryBuilder.build());
        return this;
    }

    public void mustNot(QueryBuilder queryBuilder) {
        mustNot(queryBuilder.build());
    }

    public BoolQueryBuilder should(JsonObject filter) {
        this.shouldFilter.add(filter);
        return this;
    }

    public BoolQueryBuilder should(QueryBuilder queryBuilder) {
        should(queryBuilder.build());
        return this;
    }

    private void addBoolFilter(JsonObject boolObject, String name, JsonArray filter) {
        if (filter.size() > 0) {
            if (filter.size() == 1) {
                boolObject.add(name, filter.get(0));
            } else {
                boolObject.add(name, filter);
            }
        }
    }
}

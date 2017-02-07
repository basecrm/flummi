package de.otto.flummi.query;

import com.google.gson.JsonObject;

public class ConstantScoreQueryBuilder implements QueryBuilder {
    private final QueryBuilder filterQueryBuilder;

    public ConstantScoreQueryBuilder(QueryBuilder filterQueryBuilder) {
        this.filterQueryBuilder = filterQueryBuilder;
    }

    @Override
    public JsonObject build() {
        if (filterQueryBuilder == null) {
            throw new RuntimeException("missing filter QueryBuilder.");
        }
        JsonObject filter = new JsonObject();
        filter.add("filter", filterQueryBuilder.build());

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("constant_score", filter);
        return jsonObject;
    }
}

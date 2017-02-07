package de.otto.flummi.query;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.List;

import static de.otto.flummi.GsonCollectors.toJsonArray;
import static de.otto.flummi.request.GsonHelper.object;

public class QueryBuilders {

    public static QueryBuilder matchAll() {
        return () -> {
            JsonObject query = new JsonObject();
            query.add("match_all", new JsonObject());
            return query;
        };
    }

    public static QueryBuilder filteredQuery(QueryBuilder query, JsonObject filter) {
        return () -> {
            JsonObject outerQuery = new JsonObject();
            JsonObject filtered = new JsonObject();
            outerQuery.add("filtered", filtered);
            filtered.add("query", query.build());
            filtered.add("filter", filter);
            return outerQuery;
        };
    }

    public static TermsQueryBuilder termsQuery(String name, JsonElement values) {
        return new TermsQueryBuilder(name, values);
    }

    public static TermsQueryBuilder termsQuery(String name, String... values) {
        return new TermsQueryBuilder(name, Arrays.stream(values).map(JsonPrimitive::new).collect(toJsonArray()));
    }

    public static TermsQueryBuilder termsQuery(String name, Boolean... values) {
        return new TermsQueryBuilder(name, Arrays.stream(values).map(JsonPrimitive::new).collect(toJsonArray()));
    }

    public static TermsQueryBuilder termsQuery(String name, Number... values) {
        return new TermsQueryBuilder(name, Arrays.stream(values).map(JsonPrimitive::new).collect(toJsonArray()));
    }

    public static TermsQueryBuilder termsQuery(String name, List<String> values) {
        return new TermsQueryBuilder(name, values.stream().map(JsonPrimitive::new).collect(toJsonArray()));
    }

    public static TermQueryBuilder termQuery(String name, JsonElement value) {
        return new TermQueryBuilder(name, value);
    }

    public static TermQueryBuilder termQuery(String name, String value) {
        return new TermQueryBuilder(name, new JsonPrimitive(value));
    }

    public static TermQueryBuilder termQuery(String name, Boolean value) {
        return new TermQueryBuilder(name, new JsonPrimitive(value));
    }

    public static TermQueryBuilder termQuery(String name, Number value) {
        return new TermQueryBuilder(name, new JsonPrimitive(value));
    }

    public static WildcardQueryBuilder wildcardQuery(String name, String value) {
        return wildcardQuery(name, new JsonPrimitive(value));
    }

    public static WildcardQueryBuilder wildcardQuery(String name, JsonElement value) {
        return new WildcardQueryBuilder(name, value);
    }

    public static RegexpQueryBuilder regexpQuery(String name, String value) {
        return regexpQuery(name, new JsonPrimitive(value));
    }

    public static RegexpQueryBuilder regexpQuery(String name, JsonElement value) {
        return new RegexpQueryBuilder(name, value);
    }

    public static BoolQueryBuilder bool() {
        return new BoolQueryBuilder();
    }

    public static QueryBuilder notQuery(QueryBuilder nestedFilter) {
        return () -> object("not", nestedFilter.build());
    }

    public static QueryBuilder nestedQuery(String path, QueryBuilder queryBuilder) {
        return () -> {
            JsonObject jsonObject = new JsonObject();
            JsonObject nested = new JsonObject();
            nested.add("query", queryBuilder.build());
            nested.add("path", new JsonPrimitive(path));
            jsonObject.add("nested", nested);
            return jsonObject;
        };
    }

    public static QueryBuilder prefixFilter(String name, String prefix) {
        return () -> {
            JsonObject jsonObject = new JsonObject();
            JsonObject value = new JsonObject();
            value.add(name, new JsonPrimitive(prefix));
            jsonObject.add("prefix", value);
            return jsonObject;
        };
    }

    public static JsonObject existsFilter(String fieldName) {
        JsonObject jsonObject = new JsonObject();
        JsonObject existsObject = new JsonObject();
        jsonObject.add("exists", existsObject);
        existsObject.add("field", new JsonPrimitive(fieldName));
        return jsonObject;
    }

    public static AndQueryBuilder andQuery(QueryBuilder... queries) {
        return new AndQueryBuilder(queries);
    }

    public static AndQueryBuilder andQuery(List<QueryBuilder> queries) {
        return new AndQueryBuilder(queries);
    }

    public static NumberRangeQueryBuilder numberRangeFilter(String fieldName) {
        return new NumberRangeQueryBuilder(fieldName);
    }

    public static DateRangeQueryBuilder dateRangeFilter(String fieldName) {
        return new DateRangeQueryBuilder(fieldName);
    }

    public static HasParentQueryBuilder hasParent(String type, QueryBuilder query) {
        return new HasParentQueryBuilder(type, query);
    }

    public static HasChildQueryBuilder hasChild(String type, QueryBuilder query) {
        return new HasChildQueryBuilder(type, query);
    }

    public static FunctionScoreQueryBuilder functionScoreQuery(QueryBuilder innerQuery) {
        return new FunctionScoreQueryBuilder(innerQuery);
    }

    public static ConstantScoreQueryBuilder constantScoreQuery(QueryBuilder filterQuery) {
        return new ConstantScoreQueryBuilder(filterQuery);
    }
}

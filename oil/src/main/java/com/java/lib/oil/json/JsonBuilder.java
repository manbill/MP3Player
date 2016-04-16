package com.java.lib.oil.json;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonBuilder {
    private Map<Object, Object> json;

    public JsonBuilder() {
        this.json = new LinkedHashMap<>();
    }

    public JsonBuilder append(Object key, Object value) {
        this.json.put(key, value);
        return this;
    }

    public String build() {
        if(this.json.isEmpty()) {
            return "";
        }
        StringBuilder sBuilder = new StringBuilder();
        for(Map.Entry<Object, Object> entry : this.json.entrySet()) {
            if(sBuilder.length() == 0) {
                sBuilder.append("{");
            }
            else if(sBuilder.length() != 0) {
                sBuilder.append(",");
            }
            sBuilder.append(quote(entry.getKey()));
            sBuilder.append(":");
            sBuilder.append(quote(entry.getValue()));
        }
        sBuilder.append("}");

        return sBuilder.toString();
    }

    /**
     * i think this class may not be right every time, so you can override it.
     * @param value the value you want to be quoted
     * @return the quoted type of the value, which means a string
     */
    protected String quote(Object value) {
        if(value != null) {
            if(value instanceof Number) {
                return String.valueOf(value);
            }
            else if(value instanceof CharSequence) {
                return "\"" + String.valueOf(value) + "\"";
            }
            else {
                return value.toString();
            }
        }
        return "null";
    }

    @Override
    public String toString() {
        return build();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

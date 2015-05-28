package com.uwsoft.editor.renderer.utils;

import com.badlogic.gdx.utils.ArrayMap;


/**
 * Created by azakhary on 8/28/2014.
 */
public class CustomVariables {
    private static final StringBuilder string = new StringBuilder();
    private ArrayMap<String, String> variables = new ArrayMap<String, String>();

    public CustomVariables() {

    }

    public void loadFromString(String varString) {
        variables.clear();
        String[] vars = varString.split(";");
        for(int i = 0; i < vars.length; i++) {
            String[] tmp = vars[i].split(":");
            if(tmp.length > 1) {
                setVariable(tmp[0], tmp[1]);
            }
        }
    }

    public String saveAsString() {
        string.setLength(0);
        for (int i = 0; i < variables.size; i++) {
            String key = variables.getKeyAt(i);
            String value = variables.getValueAt(i);
            string.append(key).append(':').append(value).append(';');
        }
        if(string.length() > 0) {
            string.setLength(string.length()-1);
        }

        return string.toString();
    }

    public void setVariable(String key, String value) {
        variables.put(key, value);
    }

    public void removeVariable(String key) {
        variables.removeKey(key);
    }

    public String getStringVariable(String key) {
        return variables.get(key);
    }

    public Integer getIntegerVariable(String key) {
        Integer result = null;
        try {
            result = Integer.parseInt(variables.get(key));
        } catch(Exception e) {}

        return result;
    }

    public Float getFloatVariable(String key) {
        Float result = null;
        try {
            result = Float.parseFloat(variables.get(key));
        } catch(Exception e) {}

        return result;
    }

    public ArrayMap<String, String> getHashMap() {
        return variables;
    }

    public int getCount() {
        return variables.size;
    }

}

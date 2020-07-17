package com.example.dp3t_usp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


// Esta classe implementa um pacote padrão para get ou post para ser utilizado por um RequestMaker
public class RequestPackage {
    private String url;
    private String method = "GET";  // Método default é get
    private String postData = "";
    private Map<String, String> params = new HashMap<>(); // Onde a gente insere os params da req

    public static enum methods  {
            GET, POST
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams(){
        return params;
    }

    public void setParams(Map<String, String> params){this.params = params;}

    public String getPostData(){
        return postData;
    }

    public void setPostData(String postData){
        this.postData = postData;
    }

    // encoder de params para get
    public String getEncodedParams(){
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()){
            String value = null;
            try{
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            if (sb.length() > 0){
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    // json formatter para post
    public JSONObject getJSONParams() throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(postData);
        return jsonObject;
    }
}

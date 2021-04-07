package io.bitnews.oauth.integration;

import lombok.Data;

import java.util.Map;

@Data
public class IntegrationAuthentication {

    private String authType;
    private Map<String,String[]> authParameters;
    private String username;

    public String getAuthParameter(String paramter){
        String[] values = this.authParameters.get(paramter);
        if(values != null && values.length > 0){
            return values[0];
        }
        return null;
    }
}
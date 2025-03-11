package com.example.delivery_aggregator.dto.cdek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OAuthTokenResponse {
    //JWT-токен
    @JsonProperty("access_token")
    private String accessToken;

    //Тип токена. Всегда принимает значение bearer
    @JsonProperty("token_type")
    private String tokenType;

    //Срок действия токена (по умолчанию 3600 секунд)
    @JsonProperty("expires_in")
    private String expiresIn;

    //Область действия токена (доступ к объектам и операциям над ними)
    private String scope;

    //Уникальный идентификатор токена
    private String jti;
}

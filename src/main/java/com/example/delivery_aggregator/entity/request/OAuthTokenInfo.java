package com.example.delivery_aggregator.entity.request;

import lombok.Data;

@Data
public class OAuthTokenInfo {
    //JWT-токен
    private String access_token;

    //Тип токена. Всегда принимает значение bearer
    private String token_type;

    //Срок действия токена (по умолчанию 3600 секунд)
    private String expires_in;

    //Область действия токена (доступ к объектам и операциям над ними)
    private String scope;

    //Уникальный идентификатор токена
    private String jti;
}

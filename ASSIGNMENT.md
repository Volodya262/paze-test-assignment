API документация находится тут: https://app.paze.eu/docs/

Требуется создать простое веб-приложение на Spring Boot (дизайн не важен, можно использовать например Freemarker или Thymeleaf). Пользователь вводит сумму и нажимает кнопку "Оплатить". 

После этого происходит вызов метода createPayment нашего API для создания депозита:
```
POST https://app-demo.paze.eu/api/v1/payments
Authorization: Bearer ***SECRET***
Content-Type: application/json
{
    "paymentType": "DEPOSIT",
    "amount": {введенная пользователем сумма},
    "currency": "EUR"
}
```

В случае успеха пользователь редиректится на полученный в ответе redirectUrl. В случае ошибки - показывается страница с ошибкой.
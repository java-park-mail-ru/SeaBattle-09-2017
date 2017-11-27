# SeaBattle-09-2017

Классический морской бой
## Команда

- Михаил - [@MikeGus](https://github.com/MikeGus)
- Владислав - [@vla-parfenkov](https://github.com/vla-parfenkov)
- Александр - [@YAXo-O](https://github.com/YAXo-O)
- Евгений - [@JohnKeats97](https://github.com/JohnKeats97) 

## Rest Api 

### Register
Регистрация нового пользователя
#### URL
`/api/users`
#### Method
`POST`
#### Params
```
{
  "login": string,
  "email": string,
  "password": string,
}
```
#### Response
В случае успеха возвращает данные нового пользователя\
HttpStatus: 201
```
{
  "login": string,
  "email": string,
  "password": null,
  "score": integer
}
```
В случае, если пользователь с таким логином или почтой уже существует, возвращается вообщение об ошибке\
HttpStatus: 400
```
{
  "status": 4,
  "response": "User already exists!"
}
```
### Login
Авторизация
#### URL
`/api/login`
#### Method
`POST`
#### Params
```
{
  "loginEmail": string, 
  "password": string,
}
```
#### Response
В случае успеха возвращаются данные авторизованного пользователя\
HttpStatus 200
```
{
  "login": string,
  "email": string,
  "password": null,
  "score": integer
}
```
В случае, если логин/почта или пароль неверны возвращается сообщение\
HttpStatus 400
```
{
  "status": 1,
  "response": "Wrong login/email or password!"
}
```
### Logout
Завершение пользовательской сессии
##### URL
`/api/logout`
#### Method
`GET`
#### Response
В случае успеха возвращается сообщение\
HttpStatus: 200
```
{
  "status": 3,
  "response": "You successfully logged out!"
}
```
### Info
вывод информации о текущем пользователе
#### URL
`/api/info`
#### Method
`GET`
#### Response
В случае успеха, возвращает данные текущего пользователя\
HttpStatus: 200
```
{
  "login": string,
  "email": string,
  "password": null,
  "score": integer
}
```
В случае, если пользователь не авторизован, выдается сообщение\
HttpStatus 200
```
{
  "status": 0,
  "You are not currently logged in!"
}
```
### Change
Обновление пользовательских данных
#### URL
`/api/users/{changedUser}`
#### Params

```
{
  "login": string
  "email": string,
  "password": string,
}
```
#### Method
`POST`
#### Response
В случае успеха возвращает измененные данные пользователя\
HttpStatus: 200
```
{
  "login": string,
  "email": string,
  "password": null,
  "score": integer
}
```
В случае, если текущий пользователь не является изменяемым, выдается сообщение\
HttpStatus: 403
```
{
  "status": 6,
  "response": "You have no rights to change this user data!"
}
```
В случае, если изменяемого пользователя нет в базе, выдается сообщение\
HttpStatus: 404
```
{
  "status": 5,
  "response": "User not found!"
}
```
### Leaderboard
Вывод таблицы лидеров
#### URL
`/api/leaderboard`
#### Method
`GET`

#### Params
Integer limit
#### Response
В случае успеха возвращает лист пользователей до 10 человек в порядке убывания их счета\
HttpStatus: 200
```
[{
  "login": string,
  "email": null,
  "password": null,
  "score": integer
},
...
{
  "login": string,
  "email": null,
  "password": null,
  "score": integer
}]
```
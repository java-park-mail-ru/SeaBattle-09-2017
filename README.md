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
```
{
  "status": 5,
  "response": "You are now registered!",
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
```
{
  "status": 1,
  "response": "You are successfully logged in!",
}
```

### Logout
Завершение пользовательской сессии
##### URL
`/api/logout`
#### Method
`POST`
#### Response
```
{
  "status": 3,
  "response": "You successfully logged out!",
}
```
### Info
вывод информации о текущем пользователе
####URL
`/api/info`
####Method
`GET`
####Response
В случае успеха, возвращает данные текущего пользователя

### Change
Обновление пользователских данных
#### URL
`/api/users/{changetUser}`
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
```
{
  "status": 7,
  "response": "Data is successfully updated!",
}
```
 
 

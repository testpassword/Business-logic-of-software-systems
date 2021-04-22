import requests

class User:
    def __init__(self, email: str, password: str, name: str, token = ''):
        self.email = email
        self.password = password
        self.name = name
        self.token = token


api_root = 'http://localhost:17502/'
api = {
    'root': api_root,
    'admin': f'{api_root}admin/',
    'moderator': f'{api_root}moderate/',
    'user': f'{api_root}user/',
    'advert': f'{api_root}advert/'
}
user = User('kulbako2000@mail.ru', 'root', 'pupa')
moderator = User('moderator@lol.kek', 'root', 'lupa')
admin = User('god@lol.kek', 'toor', 'god')

print('1. регистрация пользователя 1')
res = requests.put(
    url = f'{api["user"]}register', 
    json = {
        'email': user.email,
        'password': user.password,
        'name': user.name
        }).json()
print(res)
user.token = res['token']
print()

print('2. регистрация модератора')
res = requests.put(
    url = f'{api["user"]}register',
    json = {
        'email': moderator.email,
        'password': moderator.password,
        'name': moderator.name
        }).json()
print(res)
moderator.token = res['token']
print()

print('3. админ делает второго пользователя модератором')
res = requests.post(
    url = f'{api["user"]}login',
    json = {
        'email': admin.email,
        'password': admin.password
    }).json()
print(res)
admin.token = res['token']
print(requests.patch(
    url = f'{api["admin"]}change_role/2',
    json = {
        'userId': 2,
        'userRole': 'MODERATOR'
    },
    headers = {
        'Authorization': f'Bearer {admin.token}'
    }).json())
print()

print('4. Пользователь добавляет объявление')
print(requests.post(
    url = f'{api["advert"]}add',
    json = {
        'cost': 10000,
        'location': 'SPB',
        'quantityOfRooms': 3,
        'area': 86,
        'name': 'flat',
        'mobileNumber': '88005553535'
    },
    headers = {
        'Authorization': f'Bearer {user.token}'
    }).json())

print('5. Модератор одобряет объявление')
print(requests.post(
    url = f'{api["moderator"]}change/1',
    json = {
        'status': 'APPROVED'
    },
    headers = {
        'Authorization': f'Bearer {moderator.token}'
    }).json())

print('6. Пользователь проверяет, что статус изменился')
print(requests.get(url = f'{api["advert"]}1').json())
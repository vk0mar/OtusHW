Домашнее задание
Реализовать WebDriverFactory класс
Цель: В результате выполнения дз, участники создадут для себя Factory-класс, предоставляющй удобный интерфейс для запуска драйверов разных браузеров
Создайте класс WebDriverFactory со статическим методом create();

Метод create() принимает обязательный параметр webDriverName и необязтельный параметр options, а возвращает соответствующий имени вебдрайвер с заданными (если были) options

Примеры использования
WebDriver wd = WebDriverFactory.createNewDriver('chrome');
или
ChromeOptions options = new ChromeOptions();
WebDriver wd = WebDriverFactory.createNewDriver(name, options);


Про паттерн Factory: https://refactoring.guru/ru/design-patterns/factory-method




Критерии оценки: Проверять буду запуском тестов из командной строки: mvn clean test -Dbrowser='cHrOmE' (да, именно cHrOmE)
Критерий оцениявания:
+1 балл - проект компилируется и собирается
+1 балл - в репозитории нет лишних файлов (.iml, директория idea и т.д.)
+1 балл - регистр значения параметра -Dbrowser не влияет на результат
+1 балл - для хранения имен драйверов используются Enum 
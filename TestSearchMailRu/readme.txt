Запускаем hub:
java -jar selenium-server-standalone-2.40.0.jar -role hub

Добавляем nodes по JSON:
java -jar selenium-server-standalone-2.40.0.jar -role webdriver
 -nodeConfig selenium-nodes.cfg.json -Dwebdriver.chrome.driver=chromedriver.exe

Для chrome путь к драйверу:
-Dwebdriver.chrome.driver=chromedriver.exe
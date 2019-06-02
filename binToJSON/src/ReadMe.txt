------ binToJSON ------------------
Русский:
Модуль предназначен для конвертации двоичного файла данных в файл JSON.
Подробное описание двоичного формата представлено в файле ToDO.txt

Использование модуля.
Для того чтобы сконвертировать бинарный файл в файл JSON, необходиме в качестве 
параметра командной строки передать имя бинарного файла модулю, а в качестве второго
параметра передать имя, под которым нужно сохранить файл JSON:
java -jar bintojson.jar <путь-к-бинарному-файлу> <путь-куда-сохранить-JSON>

Пример: 
java -jar bintojson.jar file.bin file.json

В результате запуска в консоли должно появится сообщение об успешной конвертации.

---------------------------------------------------
English: 
This module is intended to convert binary file of data into a JSON file.
Detailed description of binary file format is provided in ToDO.txt file.

Using module.
To convert a binary file into json file, simply pass file names as parameters:
first parameter should be file name of an existing binary file, and second parameter
should be file name for the output json file. Like this:
java -jar bintojson.jar <path-to-binary-file> <path-to-output-json-file>

Example: 
java -jar bintojson.jar file.bin file.json

As a result, a message of successful conversion should appear in the console.
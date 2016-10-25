# Транслитерация слов

> Нужно сделать транлитератор имен и фамилий с русского на английский и обратно. Например, Петров должен траслитерироваться в Petrov, Petroff,... Или наоборот, с английского Jurij переводится в Юрий. Файлы с таблицей траслитерации и с частотами имен/фамилий приложены к письму.
> 
> Хотелось бы, чтоб программа не выдавала заранее несуществующих имен и фамилий. Например, для имени Инесса не было бы варианта Inesssa. И работала на не знакомых словах которых нет в файле со статистикой.


## Решение

Для начала выделим отдельные слоги, биграммы и триграммы из слов частотного словаря (отдельные слоги и биграммы как стартовые последовательности слогов).

Разбиение на слоги будем осуществлять всеми возможными способами. Например, имя `алекс` может быть представлено как `а` + `л` + `е` + `к` + `с`, равно как и `а` + `л` + `е` + `кс`. При этом в первом случае выбираются N-граммы `а`, `а+л`, `а+л+е`, `л+е+к` и `е+к+с`, во втором же случае образуется набор `а`, `а+л`, `а+л+е` и `л+е+кс`. Частотой появления N-граммы для данного слова положим отношение частоты самого слова к количеству вариантов разбиения на слоги. Для выбранного выше имени частота составляет 21465, вариантов два, и результатом будет частотный словарик:

```
{"а" -> 2*21465/2, "ал" -> 2*21465/2, "але" -> 2*21465/2, "лек" -> 21465/2, "екс": 21465/2, "лекс": 21465/2}
```

Применим это разбиение и заданный расчёт частоты для остальных слов для каждого из алфавитов и просуммируем частоты соответствующих N-грамм. Полученные частотные словари N-грамм применим далее для расчёта вероятности существования слова для заданного алфавита.

Вероятность существования слова длины m определим как P(S<sub>1</sub>..S<sub>m</sub>) = P(S<sub>1</sub>) * P(S<sub>2</sub>|S<sub>1</sub>) * P(S<sub>3</sub>|S<sub>2</sub>, S<sub>1</sub>) * ... * P(S<sub>m</sub>|S<sub>m-1</sub>, S<sub>m-2</sub>), где P(S<sub>1</sub>) — доля присутствия слога S<sub>1</sub> в частотном словаре, P(S<sub>2</sub>|S<sub>1</sub>) — доля присутствия биграммы S<sub>1</sub>S<sub>2</sub> и т.д. Под долей присутствия я подразумеваю отношение частоты появления N-граммы к сумме всех частот.

Осталось непосредственно произвести транслитерацию. Для этого для каждого из возможных разбиений входного слова W<sub>1</sub>..W<sub>k</sub> подберём все возможные варианты транслитераций T<sub>1</sub>..T<sub>k</sub> и выберем пару, для которой совместная вероятность существования в соответствующих словарях максимальна, то есть выясним argmax<sub>W,T</sub> P(W) * P(T).

Пример использования можно найти в классе [TransliteratorTest](src/test/java/antivoland/amahir/translit/ngram/TransliteratorTest.java). Частотный словарь я сократил до 300000 первых строк, поскольку больше в память не помещалось:
 
```
¯\_(ツ)_/¯
```

Конечно, правильно было бы последовательно перегнать частичные частотные словари N-грамм в отдельный файл, и уже оттуда осуществлять чтение и агрегацию, однако для демонстрации хватит и части исходного словаря. Текущий вывод выглядит следующим образом:

```
Transliterating known russian names
александр -> alexandr (alexandr, 0)
сергей -> serge (sergey, 1)
елена -> elena (elena, 0)
андрей -> andre (andrey, 1)
алексей -> alexe (alexey, 1)
ольга -> oljga (olga, 1)
дмитрий -> dmitry (dmitrij, 2)
татьяна -> tatjana (tatyana, 1)
ирина -> irina (irina, 0)
инесса -> inessa (inessa, 0)
петров -> petrov (petrov, 0)
петров -> petrov (petroff, 2)
юрий -> ury (jurij, 3)

Transliterating unknown russian names
авундий -> avyndy (avundiy, 2)
плакилла -> placilla (plakilla, 1)
усфазан -> ysfazan (usfazan, 1)
феогния -> feognija (pheognia, 3)
эпиктет -> epiktet (epiktet, 0)

Transliterating known latin names
alexandr -> александр (александр, 0)
sergey -> сергеий (сергей, 1)
elena -> еленя (елена, 1)
andrey -> андреий (андрей, 1)
alexey -> алексеий (алексей, 1)
olga -> олгя (ольга, 2)
dmitrij -> дмитриъ (дмитрий, 1)
tatyana -> татяня (татьяна, 2)
irina -> ириня (ирина, 1)
inessa -> инся (инесса, 3)
petrov -> петров (петров, 0)
petroff -> петров (петров, 0)
jurij -> юриъ (юрий, 1)

Transliterating unknown latin names
avundiy -> авундиий (авундий, 1)
plakilla -> плакилля (плакилла, 1)
usfazan -> усфяцян (усфазан, 3)
pheognia -> феогня (феогния, 1)
epiktet -> епиктет (эпиктет, 1)
```

Тут представлены входные и транслитерированные слова, в скобках же указаны ожидаемые транслитерирации и расстояния Левенштейна между ними и фактическими значениями.

Дадьнейшее улучшение возможно, очевидно, с добавлением в модель некоторого обучающего набора соответствий слов и их транслитераций. Также можно разработать инструмент для определения оптимального количества слогов в N-грамме, однако триграммы подходят, казалось бы, оптимально.
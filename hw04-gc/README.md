До рефакторинга:
256m  - OutOfMemoryError
500m  - spend msec:12540, sec:12
750m  - spend msec:12890, sec:12
1024m - spend msec:11195, sec:11
1200m - spend msec:11200, sec:11
1500m - spend msec:11258, sec:11
1700m - spend msec:10354, sec:10
1800m - spend msec:11541, sec:11
1900m - spend msec:10677, sec:10
2048m - spend msec:10378, sec:10

Начиная с 1700m особой разницы в скорости работы не вижу

--------------------
После рефакторинга:
256m  - spend msec:904, sec:0
500m  - spend msec:880, sec:0
750m  - spend msec:962, sec:0
1024m - spend msec:973, sec:0
1200m - spend msec:945, sec:0
1500m - spend msec:1078, sec:1
1700m - spend msec:957, sec:0
1800m - spend msec:975, sec:0
1900m - spend msec:990, sec:0
2048m - spend msec:1050, sec:1

Можно смело юзать 256m
PS: не знаю, насколько често было заменить в этом задании ArrayList на массив, но это круто улучшило ситуацию, 
в отличие от простой замены на примитивы
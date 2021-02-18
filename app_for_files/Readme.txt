Nápověda pro nástroj na generování souborů
##############################################################
V souboru file.properties lze nalézt například tyto konfigurace pro MySQL a konfiguraci pro definici funkcí:
	mysqlXmlFile= vstupní XML soubor pro gramatiku (v tomto případě MySQL), který vznikl jako výstup ze semestrálního projektu (Analýza SQL příkazů ze StackOverflow viz. https://github.com/sol0095/semestralni_projekt)
	mysqlselectsFile= nastavení cesty výstupního souboru pro MySQL.
	mysqlpathToIdFile= nastavení cesty výstupního souborupro MySQL.
	mysqlpathIdWithRowIds= nastavení cesty výstupního souboru pro MySQL.
	mysqlpathsSize= nastavení cesty výstupního souboru pro MySQL.

	functionsForSimilarity= nastavení funkcí pro SQL. Každá funkce musí být oddělená čárkou a mezerou (tzn. ", "). Slouží pro lepší výstup podobností SQL dotazů s danými funkcemi (záleží na situaci, někdy je to neúčinné). K této konfiguraci byl přidán základní výčet funkcí viz příklad níže.

##############################################################
Pro správné fungování je nutné všechny konfigurace (cesty) nastavit pro každou gramatiku.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.

Příklad:
	mysqlXmlFile=data/MySQL.xml
	mysqlSelectsFile=data/MySqlSelects.txt
	mysqlPathToIdFile=data/MySqlPathToId.txt
	mysqlPathIdWithRowIdsFile=data/MySqlPathIdWithRowIds.txt
	mysqlPathsSizeFile=data/MySqlPathsSize.txt

	sqliteXmlFile=data/SQLite.xml
	sqliteSelectsFile=data/SQLiteSelects.txt
	sqlitePathToIdFile=data/SQLitePathToId.txt
	sqlitePathIdWithRowIdsFile=data/SQLitePathIdWithRowIds.txt
	sqlitePathsSizeFile=data/SQLitePathsSize.txt

	tsqlXmlFile=data/TSQL.xml
	tsqlSelectsFile=data/TSqlSelects.txt
	tsqlPathToIdFile=data/TSqlPathToId.txt
	tsqlPathIdWithRowIdsFile=data/TSqlPathIdWithRowIds.txt
	tsqlPathsSizeFile=data/TSqlPathsSize.txt

	plsqlXmlFile=data/PlSql.xml
	plsqlSelectsFile=data/PlSqlSelects.txt
	plsqlPathToIdFile=data/PlSqlPathToId.txt
	plsqlPathIdWithRowIdsFile=data/PlSqlPathIdWithRowIds.txt
	plsqlPathsSizeFile=data/PlSqlPathsSize.txt

	functionsForSimilarity=JOIN, GROUP BY, ORDER BY, MAX, MIN, SUM, COUNT, ASC, AVG, DESC, AND, OR, =, <=, >=, !=, >, <, <>, NULL, IN, \
	LIKE, ANY, ALL, BETWEEN, EXISTS, SOME, REGEXP, CONCAT, CONCAT_WS, LEFT, LEN, LOWER, REPLACE, REVERSE, RIGHT, STR, SUBSTRING, \
	UPPER, POWER, SQRT, EXP, ROUND, RAND, DATEADD, DATEDIFF, DATEPART, DAY, GETDATE, MONTH, YEAR, CAST, ISNULL, COALESCE, \
	DATALENGTH, CHARINDEX, DIFFERENCE, FORMAT, LTRIM, NCHAR, PATINDEX, QUOTENAME, REPLICATE, RTRIM, SOUNDEX, SPACE, STUFF, \
	TRANSLATE, TRIM, UNICODE, ABS, SQUARE, RADIANS, PI, LOG10, LOG, FLOOR, DEGREES, CEILING, CURRENT_TIMESTAMP, DATEFROMPARTS, \
	DATENAME, GETUTCDATE, ISDATE, SYSDATETIME, SYSDATE, SYSTIMESTAMP, CONVERT, IIF, ISNUMERIC, NULLIF, USER_NAME, SESSIONPROPERTY, FROM_UNIXTIME, \
	HEX, STRFTIME, DATE, IFNULL, INSTR, TIME, STR_TO_DATE, INITCAP, ASCII, LPAD, RPAD, SUBSTR, REGEXP_REPLACE, REGEXP_SUBSTR, \
	REGEXP_INSTR, CURRENT_DATE, EXTRACT, MONTHS_BETWEEN, TO_TIMESTAMP, TO_CHAR, TRUNC, UNISTR, TO_NUMBER, TO_NCHAR, TO_DATE, PRINTF

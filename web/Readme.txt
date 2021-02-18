Nápověda pro nástroj
##############################################################
V souboru file.properties lze nalézt například tyto konfigurace pro MySQL a konfiguraci IP serveru a konfiguraci pro definici funkcí:
	fileUpdaterIP= IP adresa serveru (localhostu).
	mysqlSelectsFile= cesta k souboru, kde se nachází dotazy, jejich unikátní ID a rowID pro MySQL.
	mysqlPathToIdFile= cesta k souboru, kde se nachází cesty a jejich unikátní ID pro MySQL.
	mysqlPathIdWithRowIdsFile= cesta k souboru, kde se nachází index pro MySQL. 
	mysqlPathsSizeFile= cesta k souboru, kde se nachází kolikrát daný dotaz byl obsažen v dané cestě.

	functionsForSimilarity= nastavení funkcí pro SQL. Každá funkce musí být oddělená čárkou a mezerou (tzn. ", "). Slouží pro lepší výstup podobností SQL dotazů s danými funkcemi (záleží na situaci, někdy je to neúčinné). Je zde nutné nastavit ty samé funkce, které byly nastaveny ve vytváření souborů (aplikaci na soubory). K této konfiguraci byl přidán základní výčet funkcí viz příklad níže.

##############################################################
Pro správné fungování je nutné všechny konfigurace (cesty) nastavit pro každou gramatiku.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.
Soubory lze vygenerovat druhou aplikací v rámci diplomové práce viz. https://github.com/sol0095/diplomova_prace/tree/master/app_for_files.

Příklad:
	fileUpdaterIP=127.0.0.1

	mysqlSelectsFile=data/MySQL/MySqlSelects.txt
	mysqlPathToIdFile=data/MySQL/MySqlPathToId.txt
	mysqlPathIdWithRowIdsFile=data/MySQL/MySqlPathIdWithRowIds.txt
	mysqlPathsSizeFile=data/MySQL/MySqlPathsSize.txt

	sqliteSelectsFile=data/SQLite/SQLiteSelects.txt
	sqlitePathToIdFile=data/SQLite/SQLitePathToId.txt
	sqlitePathIdWithRowIdsFile=data/SQLite/SQLitePathIdWithRowIds.txt
	sqlitePathsSizeFile=data/SQLite/SQLitePathsSize.txt

	tsqlSelectsFile=data/TSQL/TSqlSelects.txt
	tsqlPathToIdFile=data/TSQL/TSqlPathToId.txt
	tsqlPathIdWithRowIdsFile=data/TSQL/TSqlPathIdWithRowIds.txt
	tsqlPathsSizeFile=data/TSQL/TSqlPathsSize.txt

	plsqlSelectsFile=data/PLSQL/PlSqlSelects.txt
	plsqlPathToIdFile=data/PLSQL/PlSqlPathToId.txt
	plsqlPathIdWithRowIdsFile=data/PLSQL/PlSqlPathIdWithRowIds.txt
	plsqlPathsSizeFile=data/PLSQL/PlSqlPathsSize.txt

	functionsForSimilarity=JOIN, GROUP BY, ORDER BY, MAX, MIN, SUM, COUNT, ASC, AVG, DESC, AND, OR, =, <=, >=, !=, >, <, <>, NULL, IN, \
	LIKE, ANY, ALL, BETWEEN, EXISTS, SOME, REGEXP, CONCAT, CONCAT_WS, LEFT, LEN, LOWER, REPLACE, REVERSE, RIGHT, STR, SUBSTRING, \
	UPPER, POWER, SQRT, EXP, ROUND, RAND, DATEADD, DATEDIFF, DATEPART, DAY, GETDATE, MONTH, YEAR, CAST, ISNULL, COALESCE, \
	DATALENGTH, CHARINDEX, DIFFERENCE, FORMAT, LTRIM, NCHAR, PATINDEX, QUOTENAME, REPLICATE, RTRIM, SOUNDEX, SPACE, STUFF, \
	TRANSLATE, TRIM, UNICODE, ABS, SQUARE, RADIANS, PI, LOG10, LOG, FLOOR, DEGREES, CEILING, CURRENT_TIMESTAMP, DATEFROMPARTS, \
	DATENAME, GETUTCDATE, ISDATE, SYSDATETIME, SYSDATE, SYSTIMESTAMP, CONVERT, IIF, ISNUMERIC, NULLIF, USER_NAME, SESSIONPROPERTY, FROM_UNIXTIME, \
	HEX, STRFTIME, DATE, IFNULL, INSTR, TIME, STR_TO_DATE, INITCAP, ASCII, LPAD, RPAD, SUBSTR, REGEXP_REPLACE, REGEXP_SUBSTR, \
	REGEXP_INSTR, CURRENT_DATE, EXTRACT, MONTHS_BETWEEN, TO_TIMESTAMP, TO_CHAR, TRUNC, UNISTR, TO_NUMBER, TO_NCHAR, TO_DATE, PRINTF
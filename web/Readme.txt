Nápověda pro nástroj
##############################################################
V souboru file.properties lze nalézt například tyto konfigurace pro MySQL a IP serveru:
	fileUpdaterIP= IP adresa serveru (localhostu).
	mysqlSelectsFile= cesta k souboru, kde se nachází dotazy, jejich unikátní ID a rowID pro MySQL.
	mysqlPathToIdFile= cesta k souboru, kde se nachází cesty a jejich unikátní ID pro MySQL.
	mysqlPathIdWithRowIdsFile= cesta k souboru, kde se nachází index pro MySQL. 
	mysqlPathsSizeFile= cesta k souboru, kde se nachází kolikrát daný dotaz byl obsažen v dané cestě.

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
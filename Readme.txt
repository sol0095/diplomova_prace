Nápověda pro nástroj
##############################################################
V souboru file.properties lze nalézt tyto konfigurace:
	mysqlSelectsFile=cesta/data/MySQL/MySqlSelects.txt
	mysqlPathToIdFile=cesta/data/MySQL/MySqlPathToId.txt
	mysqlPathIdWithRowIdsFile=cesta/data/MySQL/MySqlPathIdWithRowIds.txt
	mysqlPathsSizeFile=cesta/data/MySQL/MySqlPathsSize.txt

	sqliteSelectsFile=cesta/data/SQLite/SQLiteSelects.txt
	sqlitePathToIdFile=cesta/data/SQLite/SQLitePathToId.txt
	sqlitePathIdWithRowIdsFile=cesta/data/SQLite/SQLitePathIdWithRowIds.txt
	sqlitePathsSizeFile=cesta/data/SQLite/SQLitePathsSize.txt

	tsqlSelectsFile=cesta/data/TSQL/TSqlSelects.txt
	tsqlPathToIdFile=cesta/data/TSQL/TSqlPathToId.txt
	tsqlPathIdWithRowIdsFile=cesta/data/TSQL/TSqlPathIdWithRowIds.txt
	tsqlPathsSizeFile=cesta/data/TSQL/TSqlPathsSize.txt

	plsqlSelectsFile=cesta/data/PLSQL/PlSqlSelects.txt
	plsqlPathToIdFile=cesta/data/PLSQL/PlSqlPathToId.txt
	plsqlPathIdWithRowIdsFile=cesta/data/PLSQL/PlSqlPathIdWithRowIds.txt
	plsqlPathsSizeFile=cesta/PLSQL/PlSqlPathsSize.txt

##############################################################
Pro správné fungování je nutné všechny uvedené konfigurace (cesty) nastavit.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.
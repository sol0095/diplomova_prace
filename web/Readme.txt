Nápověda pro nástroj
##############################################################
V souboru file.properties lze nalézt tyto konfigurace:
	mysqlSelectsFile=cesta/MySqlSelects.txt
	mysqlPathToIdFile=cesta/MySqlPathToId.txt
	mysqlPathIdWithRowIdsFile=cesta/MySqlPathIdWithRowIds.txt
	mysqlPathsSizeFile=cesta/MySqlPathsSize.txt

	sqliteSelectsFile=cesta/SQLiteSelects.txt
	sqlitePathToIdFile=cesta/SQLitePathToId.txt
	sqlitePathIdWithRowIdsFile=cesta/SQLitePathIdWithRowIds.txt
	sqlitePathsSizeFile=cesta/SQLitePathsSize.txt

	tsqlSelectsFile=cesta/TSqlSelects.txt
	tsqlPathToIdFile=cesta/TSqlPathToId.txt
	tsqlPathIdWithRowIdsFile=cesta/TSqlPathIdWithRowIds.txt
	tsqlPathsSizeFile=cesta/TSqlPathsSize.txt

	plsqlSelectsFile=cesta/PlSqlSelects.txt
	plsqlPathToIdFile=cesta/PlSqlPathToId.txt
	plsqlPathIdWithRowIdsFile=cesta/PlSqlPathIdWithRowIds.txt
	plsqlPathsSizeFile=cesta/PlSqlPathsSize.txt

##############################################################
Pro správné fungování je nutné všechny uvedené konfigurace (cesty) nastavit.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.
Soubory lze vygenerovat druhou aplikací v rámci diplomové práce viz. https://github.com/sol0095/diplomova_prace/tree/master/app_for_files.
Nápověda pro nástroj na generování souborů
##############################################################
V souboru file.properties lze nalézt například tyto konfigurace pro MySQL:
	mysqlXmlFile= vstupní XML soubor pro gramatiku (v tomto případě MySQL), který vznikl jako výstup ze semestrálního projektu (Analýza SQL příkazů ze StackOverflow viz. https://github.com/sol0095/semestralni_projekt)
	mysqlselectsFile= nastavení cesty výstupního souboru pro MySQL.
	mysqlpathToIdFile= nastavení cesty výstupního souborupro MySQL.
	mysqlpathIdWithRowIds= nastavení cesty výstupního souboru pro MySQL.
	mysqlpathsSize= nastavení cesty výstupního souboru pro MySQL.

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

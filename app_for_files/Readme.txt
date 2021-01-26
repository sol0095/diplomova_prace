Nápověda pro nástroj na generování souborů
##############################################################
V souboru file.properties lze nalézt tyto konfigurace:
	inputXmlFile= vstupní XML soubor, který vznikl jako výstup ze semestrálního projektu (Analýza SQL příkazů ze StackOverflow viz. https://github.com/sol0095/semestralni_projekt)
	selectsFile= nastavení cesty výstupního souboru.
	pathToIdFile= nastavení cesty výstupního souboru.
	pathIdWithRowIds= nastavení cesty výstupního souboru.
	pathsSize= nastavení cesty výstupního souboru.
	grammar= číslo gramatiky (0 - MySQL, 1 - SQLite, 2 - T-SQL, 3 - PL/SQL).


##############################################################
Pro správné fungování je nutné všechny uvedené konfigurace (cesty) nastavit.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.

Příklad:
	inputXmlFile=data/SQLite.xml
	selectsFile=data/SQLiteSelects.txt
	pathToIdFile=data/SQLitePathToId.txt
	pathIdWithRowIds=data/SQLitePathIdWithRowIds.txt
	pathsSize=data/SQLitePathsSize.txt
	grammar=1

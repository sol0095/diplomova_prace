Nápověda pro nástroj
##############################################################
V souboru file.properties lze nalézt tyto konfigurace:
	inputXmlFile - nastavení cesty k souboru s derivačními stromy z SQL (výstup z upraveného semestrálního projektu, kde je jeden dotaz na jednom řádku).
	selectsFile - nastavení cesty k souboru s SQL dotazy (+ jejich rowID) a obsaženými cestami z derivačních stromů SQL (od kořene k listu). Pokud soubor neexistuje, tak se vygeneruje.
	pathToIdFile - nastavení cesty k souboru "cesta-ID cesty" z derivačních stromů SQL (od kořene k listu). Pokud soubor neexistuje, tak se vygeneruje.
	pathIdWithRowIds - nastavení cesty k souboru "ID cesty-rowID" všech dotazů, které obsahují danou cestu. Pokud soubor neexistuje, tak se vygeneruje.
	generatePathsFile - pokud ještě neexistují soubory selectsFile.txt, pathToIdFile.txt a pathIdWithRowIds.txt, tak nastavit na hodnotu true, jinak false.
	grammar - číslo, které udává, na jakou gramatiku se nástroj spustí. Nyní je to 0 pro MySQL a 1 pro SQLite.
	inputQuery - vstupní dotaz, ke kterému se mají vypsat podobné dotazy.

##############################################################
Spuštení s generováním souborů trvá déle. Teprve až jak jsou soubory vygenerované, tak samotný výpočet podobnosti je rychlý.
Pro správné fungování je nutné všechny uvedené konfigurace nastavit.
Pozor na lomítka v absolutních cestách. Pokud se zadá absolutní cesta s opačným lomítkem, vyvolá se výjimka.

##############################################################
##############################################################
Příklad použití:
	inputXmlFile=data/MySQL.xml
	selectsFile=data/selectsFile.txt
	pathToIdFile=data/pathToIdFile.txt
	pathIdWithRowIds=data/pathIdWithRowIds.txt
	generatePathsFile=true
	grammar=0
	inputQuery=SELECT * FROM products WHERE (price BETWEEN 1.0 AND 2.0) AND (quantity BETWEEN 1000 AND 2000)
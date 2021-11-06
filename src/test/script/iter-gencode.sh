#! /bin/sh


# Auteur : gl49
# Version initiale : 17/01/2021

# Test itérative sur les fichiers de tests de codegen.
# On lance test_context sur un fichier valide, et les tests invalides.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
	rm -f "${cas_de_test%.deca}".ass 2>/dev/null
	decac cas_de_test || exit 1
	if [ ! -f "${cas_de_test%.deca}".ass ]; then
    		echo "Fichier  non généré pour $cas_de_test ."
   		exit 1
	fi
	ima "${cas_de_test%.deca}".ass > "${cas_de_test%.deca}".restrou
	rm -f "${cas_de_test%.deca}".ass
	if [$(diff "${cas_de_test%.deca}".restrou "${cas_de_test%.deca}".resatt) = ""]; then
		echo "Tout va bien pour $cas_de_test"
	else
    		echo "Résultat inattendu de ima pour $cas_de_test:"
    		exit 1
	fi
done

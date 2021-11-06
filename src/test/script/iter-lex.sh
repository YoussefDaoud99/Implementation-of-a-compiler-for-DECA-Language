#! /bin/sh


# Auteur : gl49
# Version initiale : 14/01/2021

# Test de lexique itérative sur les fichiers de tests fournis.
# On lance test_synt sur un fichier valide, et les tests invalides.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_lex_invalide () {
    # $1 = premier argument.
    if test_lex "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:" 

    then
        echo "Echec attendu pour test_lex sur $1."
    else
        echo "Succes inattendu de test_lex sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/lexique/invalid/*.deca
do
    test_lex_invalide "$cas_de_test"
done


test_lex_valide () {
    # $1 = premier argument.
    if test_lex "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
      echo "Echec inattendu pour test_lex sur $1."
      exit 1
    else
      echo "Succes attendu de test_lex sur $1."

    fi
}

for cas_de_test in src/test/deca/lexique/valid/*.deca
do
    test_lex_valide "$cas_de_test"
done

#! /bin/sh


# Auteur : gl49
# Version initiale : 14/01/2021

# Test de syntaxe itérative sur les fichiers de tests fournis.
# On lance test_synt sur un fichier valide, et les tests invalides.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1."
    else
        echo "Succes inattendu de test_synt sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
do
    test_synt_invalide "$cas_de_test"
done

test_synt_valide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
      echo "Echec inattendu pour test_synt sur $1."
      exit 1
    else
      echo "Succes attendu de test_synt sur $1."

    fi
}

for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
    test_synt_valide "$cas_de_test"
done

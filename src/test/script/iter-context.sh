#! /bin/sh


# Auteur : gl49
# Version initiale : 16/01/2021

# Test itérative sur les fichiers de tests de contextes.
# On lance test_context sur un fichier valide, et les tests invalides.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_context_invalide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_context sur $1."
    else
        echo "Succes inattendu de test_context sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/context/invalid/provided/*.deca
do
    test_context_invalide "$cas_de_test"
done

test_context_valide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
      echo "Echec inattendu pour test_context sur $1."
      exit 1
    else
      echo "Succes attendu de test_context sur $1."

    fi
}

for cas_de_test in src/test/deca/context/valid/provided/*.deca
do
    test_context_valide "$cas_de_test"
done

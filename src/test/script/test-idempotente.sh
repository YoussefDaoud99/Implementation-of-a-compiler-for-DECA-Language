#! /bin/sh

# Auteur : gl49
# Version initiale : 17/01/2021

# Test itérative sur les fichiers de tests de syntaxe
# On lance decac -p  et on verifie l'idempotente

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
PATH=./src/test/script/launchers:./src/main/bin:"$PATH"



for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
  decac -p $cas_de_test > test1$$.deca
  decac -p test1$$.deca > test2$$.deca

					if diff test2$$.deca test1$$.deca > resdiff$$
					then
							echo $cas_de_test "success :idempotente verifié"
					else
							echo $cas_de_test "erreur  :idempotente non verifié "

		      fi
					rm -f test2$$.deca test1$$.deca resdiff$$

done

# AutoChef : Projet de génie logiciel et gestion de projet (INFO-F-307)

Autochef est un projet ayant pour but principal la génération automatique d'une liste de courses, afin d'aider l'utilisateur à limiter le gaspillage 
alimentaire ainsi qu'à réaliser des économies. Il permet en effet dans les détails suivant: de donc créer une liste de course personnelle et de vérifier à la fin les prix
de cette liste dans les différents magasins présents. Le client peut aussi créer des recettes à sa guise et transformer cette celà en liste de course permettant de garder
les plats favoris du client à portée de main. Et l'atout principal de notre application est qu'elle utilise une carte représentant la position du client et ses aléantours.
Il peut donc regarder quel est le magasin le plus proche, s'il n'est pas repertorié il peut ajouter lui même le magasin. De plus il peut même ajouter des produtis dans les
magasins où il manquerait des informations. L'utilisateur peut faire des recherches et donc créer des filtres s'il veut cherche un certain magasin
, une certaine heure d'ouverture ou un certain jour. En plus de cela il peut choisir les magasins en fonction de leur critère de vente, c'est à dire
par exemple bio, ou produits locaux. Enfin, l'application propose au client le magasin le plus proche - avec 3 "modes": en vélo, en voiture ou à pied - pour acheter sa liste de course et aussi
le magasin où sa liste de course reviendra le moins cher. 

# Utilisation

Nous avons utilisé la version Java 8 et mySQl 5.7 javascript 1.8.x.

## Compilation

Il suffit d'éxécuter le .exe, les détails de pré-requis pour le démarrage sont mentionnés dans le paragraphe suivant.

## Démarrage 
Il faut lancer le script bash nommé "start.sh", il va créer lui même et remplir d'exemple la base de données MySQL, ensuite il va lancer l'executable jar de l'itération.

# Configuration :

## Serveur 

Toutes les informations liées à la base de données étant locales, l'application ne possède pas de serveur. Nous avons une webview , ce qui pourrait le plus se rapprocher d'un
"serveur web".

## Client

Le client doit juster se connecter à son application en créant un compte et interragire avec la base de données locale via une interface graphique qui lui propose plusieurs services (création d'une
liste de courses, recherche de prix, ajout d'un produit dans la base de données, etc...)

# Tests

En ce qui concerne les tests, ils ont été fait sur le modèle (avec base de donnée) et sur les controllers et pas sur les vues. Pour les utiliser il suffit de trouver la classe
que l'on veut tester, chercher son test équivalant qui porte le même nom mais qui se finit par "test" et le run. Ils testent en général que chaque attribut et fonctionnalité 
d'un objet est correctement utilisé.

# Misc

## Développement

## Screenshot

## License

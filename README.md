# Projet de L3 S5 pour le cours de CPOO

## Etudiantes

Laure Runser Lily Olivier

## Compilation et exécution

Pour compiler, lancer `./gradlew build`. Pour exécuter, lancer `./gradlew run`. Le programme vous demande ensuite de
choisir entre 'cli' ou 'gui'. Si vous choisissez 'cli', le programme vous demandera les paramètres pour votre fractale
de facon interactive. Nous avons préféré un systeme interactif plutot qu'une ligne de commande très longue, peu lisible,
et où il est facile de se tromper.

Si vous choisissez 'gui', le programme ouvre une nouvelle fenêtre et vous pouvez entrer vos paramètres dans le panel de
droite.

## Fonctionnalites

- Vous pouvez afficher l'ensemble de Mandelbrot, et les ensembles de Julia de la forme z^2 + c.
- Vous pouvez choisir la taille de l'image (en pixels), la partie du plan complexe a générer, la constante complexe (
  pour Julia), le nombre maximal d'itérations, et le code couleur.
- Vous pouvez sauvegarder l'image, et un fichier texte qui reprend vos paramètres est automatiquement généré.
- La génération des fractales se fait avec du parallelisme grâce à des IntStream parallèles. Le gain de temps est tres
  conséquent : on passe de plusieurs secondes à presque instantanné pour des fractales de taille raisonnable.
- Dans le mode GUI, vous pouvez zoomer et naviguer dans l'image. Vous avez la possibilité de revenir au départ en
  cliquant sur le bouton "Reset Zoom".
- Dans le mode GUI, vous pouvez changer le code couleur sans recalculer l'image.
- Dans le mode GUI, en bas du panel de paramêtres, vous pouvez choisir des examples pré-préparés.

## Extensions que nous n'avons pas faites

Nous n'avons pas eu le temps d'ajouter ces fonctionnalités :

- laisser l'utilisateur choisir où sauvegarder le fichier.
- laisser l'utilisateur paramétrer sa fractale dans un fichier texte de configuration qui sera lu par le programme.
- pour Julia, laisser l'utilisateur entrer un polynome quelconque.
- pour calculer l'indice de divergence pour Julia, nous voulions utiliser notre classe Polynome, et sa fonction apply
  comme demandé par le sujet (as first-class citizens). Cependant, nous nous sommes rendues compte lors de l'ajout du
  parallelisme qu'il était beaucoup plus efficace de faire les calculs à la main. Nous avons dû mal implementer quelque
  chose mais nous n'avons pas eu le temps de solutionner le probleme.